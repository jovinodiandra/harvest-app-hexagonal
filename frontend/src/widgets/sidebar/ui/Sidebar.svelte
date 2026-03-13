<script lang="ts">
  import { 
    LayoutDashboard, 
    Waves, 
    Fish, 
    CalendarClock, 
    TrendingUp, 
    Skull, 
    Droplets, 
    Bug, 
    Truck,
    DollarSign,
    Bell,
    Users,
    LogOut,
    ChevronLeft,
    ChevronRight,
    Menu,
    Store,
    X,
  } from 'lucide-svelte';
  import { authStore } from '../../../entities/auth';

  export let currentPath: string = '/';
  export let collapsed: boolean = false;
  export let mobileOpen: boolean = false;
  export let onNavigate: (() => void) | undefined = undefined;

  const menuItems = [
    { icon: LayoutDashboard, label: 'Dashboard', path: '/' },
    { icon: Waves, label: 'Kolam', path: '/ponds' },
    { icon: Fish, label: 'Bibit', path: '/seeds' },
    { icon: CalendarClock, label: 'Jadwal Pakan', path: '/feed-schedules' },
    { icon: TrendingUp, label: 'Pertumbuhan', path: '/growth-records' },
    { icon: Skull, label: 'Kematian', path: '/death-records' },
    { icon: Droplets, label: 'Kualitas Air', path: '/water-quality' },
    { icon: Bug, label: 'Penyakit', path: '/diseases' },
    { icon: Truck, label: 'Panen', path: '/harvest' },
    { icon: DollarSign, label: 'Keuangan', path: '/finance' },
    { icon: Store, label: 'Supplier', path: '/suppliers' },
    { icon: Bell, label: 'Pengingat', path: '/reminders' },
    { icon: Users, label: 'Pengguna', path: '/users' },
  ];

  function handleLogout() {
    authStore.logout();
    window.location.href = '/login';
  }

  function toggleSidebar() {
    collapsed = !collapsed;
  }

  function handleNavClick(path: string) {
    window.history.pushState({}, '', path);
    window.dispatchEvent(new PopStateEvent('popstate'));
    if (onNavigate) onNavigate();
  }
</script>

<aside class="sidebar" class:collapsed class:mobile-open={mobileOpen}>
  <div class="sidebar-header">
    {#if !collapsed}
      <div class="logo">
        <Fish size={32} />
        <span class="logo-text">Ternak Lele</span>
      </div>
    {:else}
      <Fish size={28} />
    {/if}
    <button class="toggle-btn" on:click={toggleSidebar}>
      {#if collapsed}
        <ChevronRight size={20} />
      {:else}
        <ChevronLeft size={20} />
      {/if}
    </button>
  </div>

  <nav class="sidebar-nav">
    {#each menuItems as item}
      <a 
        href={item.path}
        class="nav-item"
        class:active={currentPath === item.path}
        title={collapsed ? item.label : ''}
        on:click|preventDefault={() => handleNavClick(item.path)}
      >
        <svelte:component this={item.icon} size={20} />
        {#if !collapsed || mobileOpen}
          <span class="nav-label">{item.label}</span>
        {/if}
      </a>
    {/each}
  </nav>

  <div class="sidebar-footer">
    <button class="nav-item logout-btn" on:click={handleLogout} title={collapsed ? 'Keluar' : ''}>
      <LogOut size={20} />
      {#if !collapsed}
        <span class="nav-label">Keluar</span>
      {/if}
    </button>
  </div>
</aside>

<style>
  .sidebar {
    display: flex;
    flex-direction: column;
    width: 260px;
    height: 100vh;
    background: var(--color-card);
    border-right: 1px solid var(--color-border);
    position: fixed;
    left: 0;
    top: 0;
    z-index: 100;
    transition: width 0.3s ease, transform 0.3s ease;
  }

  .sidebar.collapsed {
    width: 72px;
  }

  .sidebar-header {
    display: flex;
    align-items: center;
    justify-content: space-between;
    padding: 1rem;
    border-bottom: 1px solid var(--color-border);
    min-height: 64px;
  }

  .logo {
    display: flex;
    align-items: center;
    gap: 0.75rem;
    color: var(--color-primary);
  }

  .logo-text {
    font-size: 1.25rem;
    font-weight: 700;
  }

  .toggle-btn {
    display: flex;
    align-items: center;
    justify-content: center;
    width: 32px;
    height: 32px;
    background: var(--color-secondary);
    border: none;
    border-radius: 0.375rem;
    color: var(--color-text-muted);
    cursor: pointer;
    transition: all 0.2s;
  }

  .toggle-btn:hover {
    background: var(--color-primary-light);
    color: var(--color-primary);
  }

  .sidebar-nav {
    flex: 1;
    padding: 0.75rem;
    overflow-y: auto;
    display: flex;
    flex-direction: column;
    gap: 0.25rem;
  }

  .nav-item {
    display: flex;
    align-items: center;
    gap: 0.75rem;
    padding: 0.75rem;
    color: var(--color-text-muted);
    text-decoration: none;
    border-radius: 0.5rem;
    transition: all 0.2s;
    font-size: 0.9375rem;
    font-weight: 500;
  }

  .collapsed .nav-item {
    justify-content: center;
    padding: 0.75rem;
  }

  .nav-item:hover {
    background: var(--color-secondary);
    color: var(--color-text);
  }

  .nav-item.active {
    background: var(--color-primary-light);
    color: var(--color-primary);
  }

  .nav-label {
    white-space: nowrap;
  }

  .sidebar-footer {
    padding: 0.75rem;
    border-top: 1px solid var(--color-border);
  }

  .logout-btn {
    width: 100%;
    background: none;
    border: none;
    cursor: pointer;
    font-family: inherit;
  }

  .logout-btn:hover {
    background: #fee2e2;
    color: #dc2626;
  }

  /* Responsive Styles */
  @media (max-width: 768px) {
    .sidebar {
      transform: translateX(-100%);
      width: 280px;
      box-shadow: 4px 0 20px rgba(0, 0, 0, 0.15);
    }

    .sidebar.collapsed {
      width: 280px;
    }

    .sidebar.mobile-open {
      transform: translateX(0);
    }

    .toggle-btn {
      display: none;
    }

    .collapsed .nav-item {
      justify-content: flex-start;
    }

    .collapsed .nav-label {
      display: inline;
    }
  }

  @media (max-width: 480px) {
    .sidebar {
      width: 100%;
    }

    .sidebar.collapsed {
      width: 100%;
    }

    .sidebar-header {
      min-height: 56px;
    }
  }
</style>
