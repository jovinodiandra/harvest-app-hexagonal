<script lang="ts">
  import { onMount } from 'svelte';
  import { 
    Waves, 
    Fish, 
    TrendingUp, 
    DollarSign, 
    Truck,
    Bell,
    Activity,
    ArrowUpRight,
    ArrowDownRight,
  } from 'lucide-svelte';
  import { MainLayout } from '../../../widgets';
  import { StatCard, Card, Badge } from '../../../shared/ui';
  import { api } from '../../../shared/api';
  import type { HarvestRecord, FinanceRecord } from '../../../shared/types';
  import { formatCurrency, formatShortDate, formatNumber } from '../../../shared/lib/utils';

  let stats = {
    totalPonds: 0,
    activePonds: 0,
    totalFish: 0,
    weeklyHarvest: 0,
    monthlyIncome: 0,
    monthlyExpense: 0,
    todayFeedCount: 0,
    pendingReminders: 0,
  };
  let recentHarvests: HarvestRecord[] = [];
  let recentFinance: FinanceRecord[] = [];

  onMount(async () => {
    await Promise.all([
      fetchDashboardData(),
      fetchRecentHarvests(),
      fetchRecentFinance(),
    ]);
  });

  async function fetchDashboardData() {
    const pondsRes = await api.getPonds(1, 100);
    if (pondsRes.success && pondsRes.data) {
      stats.totalPonds = pondsRes.data.total;
      stats.activePonds = pondsRes.data.total;
    }
    
    const harvestRes = await api.getHarvestRecords(1, 100);
    if (harvestRes.success && harvestRes.data) {
      const thisWeek = new Date();
      thisWeek.setDate(thisWeek.getDate() - 7);
      stats.weeklyHarvest = harvestRes.data.data
        .filter(h => new Date(h.harvestDate) >= thisWeek)
        .reduce((sum, h) => sum + h.totalWeight, 0);
    }

    const financeRes = await api.getFinanceRecords(1, 100);
    if (financeRes.success && financeRes.data) {
      const thisMonth = new Date();
      thisMonth.setDate(1);
      const monthlyData = financeRes.data.data.filter(f => new Date(f.transactionDate) >= thisMonth);
      stats.monthlyIncome = monthlyData.filter(f => f.transactionType === 'income').reduce((sum, f) => sum + f.amount, 0);
      stats.monthlyExpense = monthlyData.filter(f => f.transactionType === 'expense').reduce((sum, f) => sum + f.amount, 0);
    }
    
    const feedRes = await api.getFeedReminders();
    if (feedRes.success && feedRes.data) {
      const today = new Date().toISOString().split('T')[0];
      stats.todayFeedCount = feedRes.data.data.filter(f => f.reminderDate === today).length;
      stats.pendingReminders = feedRes.data.data.length;
    }
  }

  async function fetchRecentHarvests() {
    const res = await api.getHarvestRecords(1, 3);
    if (res.success && res.data) {
      recentHarvests = res.data.data;
    }
  }

  async function fetchRecentFinance() {
    const res = await api.getFinanceRecords(1, 5);
    if (res.success && res.data) {
      recentFinance = res.data.data;
    }
  }
</script>

<MainLayout currentPath="/" pageTitle="Dashboard">
  <div class="dashboard">
    <div class="stats-grid">
      <StatCard 
        title="Total Kolam" 
        value={stats.totalPonds}
        subtitle="{stats.activePonds} kolam aktif"
        variant="primary"
        icon={Waves}
      />
      <StatCard 
        title="Total Ikan" 
        value={formatNumber(stats.totalFish)}
        subtitle="Ikan hidup saat ini"
        variant="info"
        icon={Fish}
      />
      <StatCard 
        title="Panen Minggu Ini" 
        value="{formatNumber(stats.weeklyHarvest)} kg"
        subtitle="Total berat panen"
        variant="success"
        icon={Truck}
      />
      <StatCard 
        title="Pendapatan Bulan Ini" 
        value={formatCurrency(stats.monthlyIncome)}
        subtitle="Pengeluaran: {formatCurrency(stats.monthlyExpense)}"
        variant="warning"
        icon={DollarSign}
      />
    </div>

    <div class="dashboard-grid">
      <Card title="Panen Terbaru">
        <div class="list">
          {#each recentHarvests as harvest}
            <div class="list-item">
              <div class="list-item-icon success">
                <Truck size={18} />
              </div>
              <div class="list-item-content">
                <span class="list-item-title">{harvest.pondsName}</span>
                <span class="list-item-subtitle">{formatShortDate(harvest.harvestDate)}</span>
              </div>
              <div class="list-item-value">
                <span class="value-main">{formatNumber(harvest.totalWeight)} kg</span>
                <span class="value-sub">{formatNumber(harvest.harvestFishCount)} ekor</span>
              </div>
            </div>
          {/each}
        </div>
        <div slot="footer">
          <a href="/harvest" class="view-all-link">Lihat semua panen</a>
        </div>
      </Card>

      <Card title="Transaksi Terbaru">
        <div class="list">
          {#each recentFinance as finance}
            <div class="list-item">
              <div class="list-item-icon" class:success={finance.transactionType === 'income'} class:danger={finance.transactionType === 'expense'}>
                {#if finance.transactionType === 'income'}
                  <ArrowUpRight size={18} />
                {:else}
                  <ArrowDownRight size={18} />
                {/if}
              </div>
              <div class="list-item-content">
                <span class="list-item-title">{finance.category}</span>
                <span class="list-item-subtitle">{formatShortDate(finance.transactionDate)}</span>
              </div>
              <div class="list-item-value">
                <span 
                  class="value-main" 
                  class:text-success={finance.transactionType === 'income'}
                  class:text-danger={finance.transactionType === 'expense'}
                >
                  {finance.transactionType === 'income' ? '+' : '-'}{formatCurrency(finance.amount)}
                </span>
              </div>
            </div>
          {/each}
        </div>
        <div slot="footer">
          <a href="/finance" class="view-all-link">Lihat semua transaksi</a>
        </div>
      </Card>

      <Card title="Ringkasan">
        <div class="summary-grid">
          <div class="summary-item">
            <Activity size={20} />
            <div>
              <span class="summary-label">Jadwal Pakan Hari Ini</span>
              <span class="summary-value">{stats.todayFeedCount} jadwal</span>
            </div>
          </div>
          <div class="summary-item">
            <Bell size={20} />
            <div>
              <span class="summary-label">Pengingat Pending</span>
              <span class="summary-value">{stats.pendingReminders} pengingat</span>
            </div>
          </div>
          <div class="summary-item">
            <TrendingUp size={20} />
            <div>
              <span class="summary-label">Profit Bulan Ini</span>
              <span class="summary-value text-success">{formatCurrency(stats.monthlyIncome - stats.monthlyExpense)}</span>
            </div>
          </div>
        </div>
      </Card>
    </div>
  </div>
</MainLayout>

<style>
  .dashboard {
    display: flex;
    flex-direction: column;
    gap: 1.5rem;
  }

  .stats-grid {
    display: grid;
    grid-template-columns: repeat(auto-fit, minmax(240px, 1fr));
    gap: 1rem;
  }

  .dashboard-grid {
    display: grid;
    grid-template-columns: repeat(auto-fit, minmax(350px, 1fr));
    gap: 1.5rem;
  }

  @media (max-width: 640px) {
    .dashboard {
      gap: 1rem;
    }

    .stats-grid {
      grid-template-columns: repeat(2, 1fr);
      gap: 0.75rem;
    }

    .dashboard-grid {
      grid-template-columns: 1fr;
      gap: 1rem;
    }
  }

  @media (max-width: 400px) {
    .stats-grid {
      grid-template-columns: 1fr;
    }
  }

  .list {
    display: flex;
    flex-direction: column;
  }

  .list-item {
    display: flex;
    align-items: center;
    gap: 0.75rem;
    padding: 0.875rem 0;
    border-bottom: 1px solid var(--color-border);
  }

  .list-item:last-child {
    border-bottom: none;
  }

  .list-item-icon {
    display: flex;
    align-items: center;
    justify-content: center;
    width: 36px;
    height: 36px;
    border-radius: 0.5rem;
    background: var(--color-secondary);
    color: var(--color-text-muted);
  }

  .list-item-icon.success {
    background: #dcfce7;
    color: #16a34a;
  }

  .list-item-icon.danger {
    background: #fee2e2;
    color: #dc2626;
  }

  .list-item-content {
    flex: 1;
    display: flex;
    flex-direction: column;
  }

  .list-item-title {
    font-weight: 500;
    color: var(--color-text);
    font-size: 0.9375rem;
  }

  .list-item-subtitle {
    font-size: 0.8125rem;
    color: var(--color-text-muted);
  }

  .list-item-value {
    text-align: right;
    display: flex;
    flex-direction: column;
  }

  .value-main {
    font-weight: 600;
    color: var(--color-text);
  }

  .value-sub {
    font-size: 0.75rem;
    color: var(--color-text-muted);
  }

  .text-success { color: #16a34a; }
  .text-danger { color: #dc2626; }

  .view-all-link {
    color: var(--color-primary);
    text-decoration: none;
    font-weight: 500;
    font-size: 0.875rem;
  }

  .view-all-link:hover {
    text-decoration: underline;
  }

  .summary-grid {
    display: flex;
    flex-direction: column;
    gap: 1rem;
  }

  .summary-item {
    display: flex;
    align-items: center;
    gap: 0.75rem;
    padding: 0.75rem;
    background: var(--color-secondary);
    border-radius: 0.5rem;
    color: var(--color-text-muted);
  }

  .summary-item div {
    display: flex;
    flex-direction: column;
  }

  .summary-label {
    font-size: 0.8125rem;
    color: var(--color-text-muted);
  }

  .summary-value {
    font-weight: 600;
    color: var(--color-text);
  }
</style>
