<script lang="ts">
  import { untrack } from 'svelte';
  import Sidebar from '../../sidebar/ui/Sidebar.svelte';
  import Header from '../../header/ui/Header.svelte';

  interface Props {
    currentPath?: string;
    pageTitle?: string;
    children?: import('svelte').Snippet;
  }

  let { currentPath = '/', pageTitle = 'Dashboard', children }: Props = $props();

  let sidebarCollapsed = $state(false);
  let mobileMenuOpen = $state(false);

  function toggleMobileMenu() {
    mobileMenuOpen = !mobileMenuOpen;
  }

  function closeMobileMenu() {
    mobileMenuOpen = false;
  }

  $effect(() => {
    if (mobileMenuOpen) {
      document.body.style.overflow = 'hidden';
    } else {
      document.body.style.overflow = '';
    }
  });
</script>

<div class="layout" class:sidebar-collapsed={sidebarCollapsed}>
  <Sidebar 
    {currentPath} 
    bind:collapsed={sidebarCollapsed} 
    mobileOpen={mobileMenuOpen}
    onNavigate={closeMobileMenu}
  />
  
  {#if mobileMenuOpen}
    <div 
      class="mobile-overlay" 
      onclick={toggleMobileMenu}
      onkeydown={(e) => e.key === 'Escape' && toggleMobileMenu()}
      role="button"
      tabindex="-1"
      aria-label="Close menu"
    ></div>
  {/if}

  <div class="main-wrapper">
    <Header title={pageTitle} onMenuClick={toggleMobileMenu} />
    <main class="main-content">
      {@render children?.()}
    </main>
  </div>
</div>

<style>
  .layout {
    display: flex;
    min-height: 100vh;
    background: var(--color-bg);
  }

  .main-wrapper {
    flex: 1;
    margin-left: 260px;
    display: flex;
    flex-direction: column;
    transition: margin-left 0.3s ease;
  }

  .layout.sidebar-collapsed .main-wrapper {
    margin-left: 72px;
  }

  .main-content {
    flex: 1;
    padding: 1.5rem;
  }

  .mobile-overlay {
    display: none;
    position: fixed;
    inset: 0;
    background: rgba(0, 0, 0, 0.5);
    z-index: 99;
  }

  @media (max-width: 768px) {
    .main-wrapper {
      margin-left: 0;
    }

    .layout.sidebar-collapsed .main-wrapper {
      margin-left: 0;
    }

    .mobile-overlay {
      display: block;
    }

    .main-content {
      padding: 1rem;
    }
  }
</style>
