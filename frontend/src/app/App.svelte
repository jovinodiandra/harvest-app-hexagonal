<script lang="ts">
  import { isAuthenticated } from '../entities/auth';
  import './styles/global.css';
  
  import DashboardPage from '../pages/dashboard/ui/DashboardPage.svelte';
  import LoginPage from '../pages/auth/ui/LoginPage.svelte';
  import RegisterPage from '../pages/auth/ui/RegisterPage.svelte';
  import PondsPage from '../pages/ponds/ui/PondsPage.svelte';
  import SeedsPage from '../pages/seeds/ui/SeedsPage.svelte';
  import FinancePage from '../pages/finance/ui/FinancePage.svelte';
  import HarvestPage from '../pages/harvest/ui/HarvestPage.svelte';
  import WaterQualityPage from '../pages/water-quality/ui/WaterQualityPage.svelte';
  import FeedSchedulesPage from '../pages/feed-schedules/ui/FeedSchedulesPage.svelte';
  import FeedPricesPage from '../pages/feed-prices/ui/FeedPricesPage.svelte';
  import GrowthRecordsPage from '../pages/growth-records/ui/GrowthRecordsPage.svelte';
  import DeathRecordsPage from '../pages/death-records/ui/DeathRecordsPage.svelte';
  import DiseasesPage from '../pages/diseases/ui/DiseasesPage.svelte';
  import SuppliersPage from '../pages/suppliers/ui/SuppliersPage.svelte';
  import RemindersPage from '../pages/reminders/ui/RemindersPage.svelte';
  import UsersPage from '../pages/users/ui/UsersPage.svelte';

  let currentPath = $state(window.location.pathname);

  function handleNavigation() {
    currentPath = window.location.pathname;
  }

  $effect(() => {
    window.addEventListener('popstate', handleNavigation);
    return () => window.removeEventListener('popstate', handleNavigation);
  });

  $effect(() => {
    if (!$isAuthenticated && currentPath !== '/login' && currentPath !== '/register') {
      window.history.pushState({}, '', '/login');
      currentPath = '/login';
    }
  });
</script>

{#if currentPath === '/login'}
  <LoginPage />
{:else if currentPath === '/register'}
  <RegisterPage />
{:else if currentPath === '/'}
  <DashboardPage />
{:else if currentPath === '/ponds'}
  <PondsPage />
{:else if currentPath === '/seeds'}
  <SeedsPage />
{:else if currentPath === '/feed-schedules'}
  <FeedSchedulesPage />
{:else if currentPath === '/feed-prices'}
  <FeedPricesPage />
{:else if currentPath === '/growth-records'}
  <GrowthRecordsPage />
{:else if currentPath === '/death-records'}
  <DeathRecordsPage />
{:else if currentPath === '/water-quality'}
  <WaterQualityPage />
{:else if currentPath === '/diseases'}
  <DiseasesPage />
{:else if currentPath === '/harvest'}
  <HarvestPage />
{:else if currentPath === '/finance'}
  <FinancePage />
{:else if currentPath === '/suppliers'}
  <SuppliersPage />
{:else if currentPath === '/reminders'}
  <RemindersPage />
{:else if currentPath === '/users'}
  <UsersPage />
{:else}
  <DashboardPage />
{/if}
