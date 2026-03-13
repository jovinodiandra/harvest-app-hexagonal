import { writable, derived } from 'svelte/store';
import { api } from '../../../shared/api';
import { pushNotificationService } from '../../../shared/lib/pushNotification';
import type { FeedReminder, HarvestReminder } from '../../../shared/types';

export interface Notification {
  id: string;
  type: 'feed' | 'harvest';
  title: string;
  message: string;
  pondName?: string;
  dueDate: string;
  dueTime: string;
  isRead: boolean;
  pushSent: boolean;
}

interface NotificationState {
  notifications: Notification[];
  loading: boolean;
  lastFetched: Date | null;
  pushPermission: 'granted' | 'denied' | 'default' | 'unsupported';
  pushInitialized: boolean;
}

function createNotificationStore() {
  const initialState: NotificationState = {
    notifications: [],
    loading: false,
    lastFetched: null,
    pushPermission: 'default',
    pushInitialized: false,
  };

  const { subscribe, set, update } = writable<NotificationState>(initialState);

  const sentPushIds = new Set<string>();

  function isReminderDue(reminderDate: string, reminderTime: string): boolean {
    const now = new Date();
    const today = now.toISOString().split('T')[0];
    const currentTime = now.toTimeString().slice(0, 5);
    
    if (reminderDate < today) return true;
    if (reminderDate === today && reminderTime <= currentTime) return true;
    
    const tomorrow = new Date(now);
    tomorrow.setDate(tomorrow.getDate() + 1);
    const tomorrowStr = tomorrow.toISOString().split('T')[0];
    if (reminderDate === tomorrowStr) return true;
    
    return false;
  }

  function isExactlyDue(reminderDate: string, reminderTime: string): boolean {
    const now = new Date();
    const today = now.toISOString().split('T')[0];
    const currentTime = now.toTimeString().slice(0, 5);
    
    return reminderDate === today && reminderTime === currentTime;
  }

  async function sendPushNotification(notification: Notification) {
    if (sentPushIds.has(notification.id)) return;
    
    const success = await pushNotificationService.showReminderNotification(
      notification.type,
      notification.message,
      notification.pondName
    );

    if (success) {
      sentPushIds.add(notification.id);
    }
  }

  return {
    subscribe,

    initPush: async () => {
      const supported = await pushNotificationService.init();
      
      update(s => ({
        ...s,
        pushInitialized: true,
        pushPermission: supported 
          ? pushNotificationService.getPermissionState() 
          : 'unsupported',
      }));

      return supported;
    },

    requestPushPermission: async () => {
      const granted = await pushNotificationService.requestPermission();
      
      update(s => ({
        ...s,
        pushPermission: pushNotificationService.getPermissionState(),
      }));

      return granted;
    },

    fetchNotifications: async () => {
      update(s => ({ ...s, loading: true }));

      try {
        const [feedRes, harvestRes] = await Promise.all([
          api.getFeedReminders(),
          api.getHarvestReminders(),
        ]);

        const notifications: Notification[] = [];

        if (feedRes.success && feedRes.data) {
          feedRes.data.data.forEach((reminder: FeedReminder & { pondName?: string }) => {
            if (isReminderDue(reminder.reminderDate, reminder.reminderTime)) {
              const notif: Notification = {
                id: `feed-${reminder.id}`,
                type: 'feed',
                title: 'Pengingat Pakan',
                message: `Waktu pemberian pakan ${reminder.feedType}`,
                pondName: reminder.pondName || '',
                dueDate: reminder.reminderDate,
                dueTime: reminder.reminderTime,
                isRead: false,
                pushSent: sentPushIds.has(`feed-${reminder.id}`),
              };
              notifications.push(notif);

              if (isExactlyDue(reminder.reminderDate, reminder.reminderTime)) {
                sendPushNotification(notif);
              }
            }
          });
        }

        if (harvestRes.success && harvestRes.data) {
          harvestRes.data.data.forEach((reminder: HarvestReminder & { pondName?: string }) => {
            if (isReminderDue(reminder.reminderDate, reminder.reminderTime)) {
              const notif: Notification = {
                id: `harvest-${reminder.id}`,
                type: 'harvest',
                title: 'Pengingat Panen',
                message: reminder.notes || 'Waktunya panen!',
                pondName: reminder.pondName || '',
                dueDate: reminder.reminderDate,
                dueTime: reminder.reminderTime,
                isRead: false,
                pushSent: sentPushIds.has(`harvest-${reminder.id}`),
              };
              notifications.push(notif);

              if (isExactlyDue(reminder.reminderDate, reminder.reminderTime)) {
                sendPushNotification(notif);
              }
            }
          });
        }

        notifications.sort((a, b) => {
          const dateA = new Date(`${a.dueDate}T${a.dueTime}`);
          const dateB = new Date(`${b.dueDate}T${b.dueTime}`);
          return dateA.getTime() - dateB.getTime();
        });

        update(s => ({
          ...s,
          notifications,
          loading: false,
          lastFetched: new Date(),
        }));
      } catch (error) {
        update(s => ({ ...s, loading: false }));
      }
    },

    testPushNotification: async () => {
      return pushNotificationService.showNotification({
        title: '🐟 Ternak Lele',
        body: 'Notifikasi berhasil diaktifkan! Anda akan menerima pengingat pakan dan panen.',
        tag: 'test',
      });
    },

    markAsRead: (id: string) => {
      update(s => ({
        ...s,
        notifications: s.notifications.map(n =>
          n.id === id ? { ...n, isRead: true } : n
        ),
      }));
    },

    markAllAsRead: () => {
      update(s => ({
        ...s,
        notifications: s.notifications.map(n => ({ ...n, isRead: true })),
      }));
    },

    clear: () => set(initialState),
  };
}

export const notificationStore = createNotificationStore();

export const unreadCount = derived(
  notificationStore,
  $store => $store.notifications.filter(n => !n.isRead).length
);

export const hasNotifications = derived(
  notificationStore,
  $store => $store.notifications.length > 0
);
