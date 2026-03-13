<script lang="ts">
  import { onMount } from 'svelte';
  import { Bell, Search, Menu, Utensils, Truck, BellRing } from 'lucide-svelte';
  import { authStore } from '../../../entities/auth';
  import { notificationStore, unreadCount, type Notification } from '../../../entities/notification';
  import { getInitials, formatShortDate } from '../../../shared/lib/utils';

  export let title: string = 'Dashboard';
  export let onMenuClick: (() => void) | undefined = undefined;

  let showNotificationDropdown = false;
  let isEnablingPush = false;

  $: userName = $authStore?.name || 'User';
  $: userRole = $authStore?.role || 'user';
  $: notifications = $notificationStore.notifications;
  $: notificationCount = $unreadCount;
  $: pushPermission = $notificationStore.pushPermission;
  $: isPushEnabled = pushPermission === 'granted';
  $: isPushDenied = pushPermission === 'denied';

  function getRoleLabel(role: string): string {
    const labels: Record<string, string> = {
      owner: 'Pemilik',
      admin: 'Admin',
      user: 'Staf',
    };
    return labels[role] || 'Pengguna';
  }

  function toggleNotificationDropdown() {
    showNotificationDropdown = !showNotificationDropdown;
  }

  function closeDropdown() {
    showNotificationDropdown = false;
  }

  function handleNotificationClick(notification: Notification) {
    notificationStore.markAsRead(notification.id);
    window.location.href = '/reminders';
    closeDropdown();
  }

  function handleMarkAllRead() {
    notificationStore.markAllAsRead();
  }

  async function handleEnablePush() {
    isEnablingPush = true;
    const granted = await notificationStore.requestPushPermission();
    if (granted) {
      await notificationStore.testPushNotification();
    }
    isEnablingPush = false;
  }

  onMount(() => {
    notificationStore.initPush();
    notificationStore.fetchNotifications();
    
    const interval = setInterval(() => {
      notificationStore.fetchNotifications();
    }, 60000);

    return () => clearInterval(interval);
  });
</script>

<svelte:window on:click={(e) => {
  const target = e.target as HTMLElement;
  if (!target.closest('.notification-wrapper')) {
    closeDropdown();
  }
}} />

<header class="header">
  <div class="header-left">
    {#if onMenuClick}
      <button class="menu-btn" on:click={onMenuClick} aria-label="Toggle menu">
        <Menu size={24} />
      </button>
    {/if}
    <h1 class="header-title">{title}</h1>
  </div>

  <div class="header-right">
    <div class="search-box">
      <Search size={18} />
      <input type="text" placeholder="Cari..." class="search-input" />
    </div>

    <div class="notification-wrapper">
      <button 
        class="icon-btn notification-btn" 
        on:click={toggleNotificationDropdown}
        aria-label="Notifications"
      >
        <Bell size={20} />
        {#if notificationCount > 0}
          <span class="notification-badge">{notificationCount > 99 ? '99+' : notificationCount}</span>
        {/if}
      </button>

      {#if showNotificationDropdown}
        <div class="notification-dropdown">
          <div class="dropdown-header">
            <h3>Notifikasi</h3>
            {#if notifications.length > 0}
              <button class="mark-all-btn" on:click={handleMarkAllRead}>
                Tandai semua dibaca
              </button>
            {/if}
          </div>

          {#if !isPushEnabled && !isPushDenied}
            <div class="push-banner">
              <div class="push-banner-content">
                <BellRing size={18} />
                <span>Aktifkan notifikasi perangkat</span>
              </div>
              <button 
                class="push-enable-btn" 
                on:click={handleEnablePush}
                disabled={isEnablingPush}
              >
                {isEnablingPush ? 'Mengaktifkan...' : 'Aktifkan'}
              </button>
            </div>
          {:else if isPushEnabled}
            <div class="push-banner enabled">
              <BellRing size={16} />
              <span>Notifikasi perangkat aktif</span>
            </div>
          {/if}
          
          <div class="dropdown-content">
            {#if notifications.length === 0}
              <div class="empty-state">
                <Bell size={32} />
                <p>Tidak ada notifikasi</p>
              </div>
            {:else}
              {#each notifications as notification}
                <button
                  class="notification-item"
                  class:unread={!notification.isRead}
                  on:click={() => handleNotificationClick(notification)}
                >
                  <div class="notification-icon" class:feed={notification.type === 'feed'} class:harvest={notification.type === 'harvest'}>
                    {#if notification.type === 'feed'}
                      <Utensils size={16} />
                    {:else}
                      <Truck size={16} />
                    {/if}
                  </div>
                  <div class="notification-content">
                    <span class="notification-title">{notification.title}</span>
                    <span class="notification-message">{notification.message}</span>
                    {#if notification.pondName}
                      <span class="notification-pond">Kolam: {notification.pondName}</span>
                    {/if}
                    <span class="notification-time">
                      {formatShortDate(notification.dueDate)} - {notification.dueTime}
                    </span>
                  </div>
                </button>
              {/each}
            {/if}
          </div>

          {#if notifications.length > 0}
            <div class="dropdown-footer">
              <a href="/reminders" on:click={closeDropdown}>Lihat semua pengingat</a>
            </div>
          {/if}
        </div>
      {/if}
    </div>

    <div class="user-menu">
      <div class="user-avatar">
        {getInitials(userName)}
      </div>
      <div class="user-info">
        <span class="user-name">{userName}</span>
        <span class="user-role">{getRoleLabel(userRole)}</span>
      </div>
    </div>
  </div>
</header>

<style>
  .header {
    display: flex;
    align-items: center;
    justify-content: space-between;
    padding: 0 1.5rem;
    height: 64px;
    background: var(--color-card);
    border-bottom: 1px solid var(--color-border);
    position: sticky;
    top: 0;
    z-index: 50;
  }

  .header-left {
    display: flex;
    align-items: center;
    gap: 1rem;
    min-width: 0;
  }

  .menu-btn {
    display: none;
    align-items: center;
    justify-content: center;
    width: 40px;
    height: 40px;
    background: none;
    border: none;
    color: var(--color-text);
    cursor: pointer;
    border-radius: 0.5rem;
    flex-shrink: 0;
  }

  .menu-btn:hover {
    background: var(--color-secondary);
  }

  .header-title {
    margin: 0;
    font-size: 1.5rem;
    font-weight: 600;
    color: var(--color-text);
    white-space: nowrap;
    overflow: hidden;
    text-overflow: ellipsis;
  }

  .header-right {
    display: flex;
    align-items: center;
    gap: 0.75rem;
    flex-shrink: 0;
  }

  .search-box {
    display: flex;
    align-items: center;
    gap: 0.5rem;
    padding: 0.5rem 1rem;
    background: var(--color-secondary);
    border-radius: 0.5rem;
    color: var(--color-text-muted);
  }

  .search-input {
    border: none;
    background: none;
    outline: none;
    font-size: 0.875rem;
    color: var(--color-text);
    width: 200px;
  }

  .search-input::placeholder {
    color: var(--color-text-muted);
  }

  .notification-wrapper {
    position: relative;
  }

  .icon-btn {
    display: flex;
    align-items: center;
    justify-content: center;
    width: 40px;
    height: 40px;
    background: none;
    border: none;
    color: var(--color-text-muted);
    cursor: pointer;
    border-radius: 0.5rem;
    position: relative;
  }

  .icon-btn:hover {
    background: var(--color-secondary);
    color: var(--color-text);
  }

  .notification-badge {
    position: absolute;
    top: 2px;
    right: 2px;
    min-width: 18px;
    height: 18px;
    padding: 0 4px;
    background: var(--color-danger);
    color: white;
    font-size: 0.625rem;
    font-weight: 600;
    display: flex;
    align-items: center;
    justify-content: center;
    border-radius: 9px;
  }

  .notification-dropdown {
    position: absolute;
    top: calc(100% + 8px);
    right: 0;
    width: 360px;
    max-height: 480px;
    background: var(--color-card);
    border: 1px solid var(--color-border);
    border-radius: 0.75rem;
    box-shadow: 0 10px 40px rgba(0, 0, 0, 0.15);
    display: flex;
    flex-direction: column;
    overflow: hidden;
    z-index: 100;
  }

  .dropdown-header {
    display: flex;
    align-items: center;
    justify-content: space-between;
    padding: 1rem;
    border-bottom: 1px solid var(--color-border);
  }

  .dropdown-header h3 {
    margin: 0;
    font-size: 1rem;
    font-weight: 600;
    color: var(--color-text);
  }

  .mark-all-btn {
    background: none;
    border: none;
    color: var(--color-primary);
    font-size: 0.75rem;
    font-weight: 500;
    cursor: pointer;
    padding: 0;
  }

  .mark-all-btn:hover {
    text-decoration: underline;
  }

  .push-banner {
    display: flex;
    align-items: center;
    justify-content: space-between;
    padding: 0.75rem 1rem;
    background: var(--color-primary-light);
    border-bottom: 1px solid var(--color-border);
  }

  .push-banner.enabled {
    background: #d1fae5;
    color: #059669;
    justify-content: flex-start;
    gap: 0.5rem;
    font-size: 0.8125rem;
    font-weight: 500;
  }

  .push-banner-content {
    display: flex;
    align-items: center;
    gap: 0.5rem;
    color: var(--color-primary);
    font-size: 0.8125rem;
    font-weight: 500;
  }

  .push-enable-btn {
    padding: 0.375rem 0.75rem;
    background: var(--color-primary);
    color: white;
    border: none;
    border-radius: 0.375rem;
    font-size: 0.75rem;
    font-weight: 500;
    cursor: pointer;
    transition: background 0.2s;
  }

  .push-enable-btn:hover:not(:disabled) {
    background: var(--color-primary-dark);
  }

  .push-enable-btn:disabled {
    opacity: 0.7;
    cursor: not-allowed;
  }

  .dropdown-content {
    flex: 1;
    overflow-y: auto;
    max-height: 360px;
  }

  .empty-state {
    display: flex;
    flex-direction: column;
    align-items: center;
    justify-content: center;
    padding: 2rem;
    color: var(--color-text-muted);
    gap: 0.5rem;
  }

  .empty-state p {
    margin: 0;
    font-size: 0.875rem;
  }

  .notification-item {
    display: flex;
    align-items: flex-start;
    gap: 0.75rem;
    padding: 0.875rem 1rem;
    width: 100%;
    background: none;
    border: none;
    border-bottom: 1px solid var(--color-border);
    cursor: pointer;
    text-align: left;
    transition: background 0.2s;
  }

  .notification-item:hover {
    background: var(--color-secondary);
  }

  .notification-item.unread {
    background: var(--color-primary-light);
  }

  .notification-item.unread:hover {
    background: #e0e7ff;
  }

  .notification-icon {
    width: 32px;
    height: 32px;
    border-radius: 50%;
    display: flex;
    align-items: center;
    justify-content: center;
    flex-shrink: 0;
  }

  .notification-icon.feed {
    background: #fef3c7;
    color: #d97706;
  }

  .notification-icon.harvest {
    background: #d1fae5;
    color: #059669;
  }

  .notification-content {
    flex: 1;
    min-width: 0;
    display: flex;
    flex-direction: column;
    gap: 0.125rem;
  }

  .notification-title {
    font-size: 0.875rem;
    font-weight: 600;
    color: var(--color-text);
  }

  .notification-message {
    font-size: 0.8125rem;
    color: var(--color-text-muted);
    white-space: nowrap;
    overflow: hidden;
    text-overflow: ellipsis;
  }

  .notification-pond {
    font-size: 0.75rem;
    color: var(--color-primary);
    font-weight: 500;
  }

  .notification-time {
    font-size: 0.6875rem;
    color: var(--color-text-muted);
  }

  .dropdown-footer {
    padding: 0.75rem 1rem;
    border-top: 1px solid var(--color-border);
    text-align: center;
  }

  .dropdown-footer a {
    color: var(--color-primary);
    font-size: 0.875rem;
    font-weight: 500;
    text-decoration: none;
  }

  .dropdown-footer a:hover {
    text-decoration: underline;
  }

  .user-menu {
    display: flex;
    align-items: center;
    gap: 0.75rem;
    padding: 0.375rem 0.75rem 0.375rem 0.375rem;
    background: var(--color-secondary);
    border-radius: 2rem;
    cursor: pointer;
  }

  .user-menu:hover {
    background: var(--color-border);
  }

  .user-avatar {
    width: 36px;
    height: 36px;
    background: var(--color-primary);
    color: white;
    font-weight: 600;
    font-size: 0.875rem;
    display: flex;
    align-items: center;
    justify-content: center;
    border-radius: 50%;
    flex-shrink: 0;
  }

  .user-info {
    display: flex;
    flex-direction: column;
  }

  .user-name {
    font-size: 0.875rem;
    font-weight: 600;
    color: var(--color-text);
    line-height: 1.2;
    max-width: 120px;
    white-space: nowrap;
    overflow: hidden;
    text-overflow: ellipsis;
  }

  .user-role {
    font-size: 0.75rem;
    color: var(--color-text-muted);
  }

  /* Responsive Styles */
  @media (max-width: 1024px) {
    .search-input {
      width: 160px;
    }
  }

  @media (max-width: 768px) {
    .header {
      padding: 0 1rem;
    }

    .menu-btn {
      display: flex;
    }

    .search-box {
      display: none;
    }

    .header-title {
      font-size: 1.25rem;
    }

    .notification-dropdown {
      position: fixed;
      top: 64px;
      left: 0;
      right: 0;
      width: 100%;
      max-height: calc(100vh - 64px);
      border-radius: 0;
      border-left: none;
      border-right: none;
    }
  }

  @media (max-width: 480px) {
    .header {
      padding: 0 0.75rem;
      height: 56px;
    }

    .header-right {
      gap: 0.5rem;
    }

    .header-title {
      font-size: 1.125rem;
    }

    .user-info {
      display: none;
    }

    .user-menu {
      padding: 0.25rem;
      background: transparent;
    }

    .user-avatar {
      width: 32px;
      height: 32px;
      font-size: 0.75rem;
    }

    .icon-btn {
      width: 36px;
      height: 36px;
    }

    .menu-btn {
      width: 36px;
      height: 36px;
    }
  }
</style>
