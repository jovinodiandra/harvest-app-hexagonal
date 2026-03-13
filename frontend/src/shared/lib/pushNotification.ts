interface NotificationOptions {
  title: string;
  body: string;
  icon?: string;
  badge?: string;
  tag?: string;
  requireInteraction?: boolean;
}

class PushNotificationService {
  private permissionState: NotificationPermission | 'unsupported' = 'default';
  private swRegistration: ServiceWorkerRegistration | null = null;

  async init(): Promise<boolean> {
    if (!('Notification' in window)) {
      this.permissionState = 'unsupported';
      return false;
    }

    this.permissionState = Notification.permission;

    if ('serviceWorker' in navigator) {
      try {
        this.swRegistration = await navigator.serviceWorker.register('/sw.js');
        console.warn('Service Worker registered');
      } catch (error) {
        console.error('Service Worker registration failed:', error);
      }
    }

    return true;
  }

  async requestPermission(): Promise<boolean> {
    if (!('Notification' in window)) {
      return false;
    }

    try {
      const permission = await Notification.requestPermission();
      this.permissionState = permission;
      return permission === 'granted';
    } catch (error) {
      console.error('Failed to request notification permission:', error);
      return false;
    }
  }

  isPermissionGranted(): boolean {
    return this.permissionState === 'granted';
  }

  isPermissionDenied(): boolean {
    return this.permissionState === 'denied';
  }

  isPermissionDefault(): boolean {
    return this.permissionState === 'default';
  }

  getPermissionState(): 'granted' | 'denied' | 'default' | 'unsupported' {
    if (this.permissionState === 'unsupported') return 'unsupported';
    return this.permissionState;
  }

  async showNotification(options: NotificationOptions): Promise<boolean> {
    if (!this.isPermissionGranted()) {
      return false;
    }

    try {
      if (this.swRegistration) {
        await this.swRegistration.showNotification(options.title, {
          body: options.body,
          icon: options.icon || '/icon-192.svg',
          badge: options.badge || '/icon-192.svg',
          tag: options.tag,
          requireInteraction: options.requireInteraction,
        });
      } else {
        new Notification(options.title, {
          body: options.body,
          icon: options.icon || '/icon-192.svg',
          tag: options.tag,
        });
      }
      return true;
    } catch (error) {
      console.error('Failed to show notification:', error);
      return false;
    }
  }

  async showReminderNotification(
    type: 'feed' | 'harvest',
    message: string,
    pondName?: string
  ): Promise<boolean> {
    const title = type === 'feed' ? '🐟 Pengingat Pakan' : '🚜 Pengingat Panen';
    const body = pondName ? `${message} - ${pondName}` : message;

    return this.showNotification({
      title,
      body,
      tag: `reminder-${type}-${Date.now()}`,
      requireInteraction: true,
    });
  }
}

export const pushNotificationService = new PushNotificationService();
