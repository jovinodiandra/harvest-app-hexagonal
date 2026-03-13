<script lang="ts">
  import { onMount } from 'svelte';
  import { Plus, Edit2, Trash2, TrendingUp, TrendingDown, DollarSign } from 'lucide-svelte';
  import { MainLayout } from '../../../widgets';
  import { Button, Card, Modal, Input, Select, Textarea, Table, Pagination, Alert, StatCard } from '../../../shared/ui';
  import { api } from '../../../shared/api';
  import type { FinanceRecord } from '../../../shared/types';
  import { formatCurrency, formatShortDate } from '../../../shared/lib/utils';

  let records: FinanceRecord[] = [];
  let loading = false;
  let pagination = { page: 1, limit: 10, total: 0, totalPages: 0 };
  let filterType: 'all' | 'income' | 'expense' = 'all';
  
  let showModal = false;
  let editingRecord: FinanceRecord | null = null;
  let deleteConfirm: FinanceRecord | null = null;
  let successMessage = '';
  
  let formData = {
    transactionType: 'expense' as 'income' | 'expense',
    transactionDate: new Date().toISOString().split('T')[0],
    amount: 0,
    category: '',
    notes: '',
    organizationId: 'org-001',
  };

  const incomeCategories = [
    { value: 'Penjualan', label: 'Penjualan Ikan' },
    { value: 'Investasi', label: 'Investasi' },
    { value: 'Lainnya', label: 'Pendapatan Lainnya' },
  ];

  const expenseCategories = [
    { value: 'Bibit', label: 'Pembelian Bibit' },
    { value: 'Pakan', label: 'Pembelian Pakan' },
    { value: 'Obat', label: 'Obat & Vitamin' },
    { value: 'Listrik', label: 'Listrik' },
    { value: 'Tenaga Kerja', label: 'Tenaga Kerja' },
    { value: 'Peralatan', label: 'Peralatan' },
    { value: 'Lainnya', label: 'Pengeluaran Lainnya' },
  ];

  $: categories = formData.transactionType === 'income' ? incomeCategories : expenseCategories;
  $: totalIncome = records.filter(r => r.transactionType === 'income').reduce((sum, r) => sum + r.amount, 0);
  $: totalExpense = records.filter(r => r.transactionType === 'expense').reduce((sum, r) => sum + r.amount, 0);
  $: netProfit = totalIncome - totalExpense;

  onMount(() => fetchRecords());

  async function fetchRecords(page = 1) {
    loading = true;
    const type = filterType === 'all' ? undefined : filterType;
    const response = await api.getFinanceRecords(page, 10, type);
    if (response.success && response.data) {
      records = response.data.data;
      pagination = {
        page: response.data.page,
        limit: response.data.limit,
        total: response.data.total,
        totalPages: response.data.totalPages,
      };
    }
    loading = false;
  }

  function openCreateModal() {
    editingRecord = null;
    formData = {
      transactionType: 'expense',
      transactionDate: new Date().toISOString().split('T')[0],
      amount: 0,
      category: '',
      notes: '',
      organizationId: 'org-001',
    };
    showModal = true;
  }

  function openEditModal(record: FinanceRecord) {
    editingRecord = record;
    formData = { ...record };
    showModal = true;
  }

  function closeModal() {
    showModal = false;
    editingRecord = null;
  }

  async function handleSubmit() {
    loading = true;
    if (editingRecord) {
      const result = await api.updateFinanceRecord(editingRecord.id, formData);
      if (result.success) {
        successMessage = 'Transaksi berhasil diperbarui';
        await fetchRecords(pagination.page);
        closeModal();
      }
    } else {
      const result = await api.createFinanceRecord(formData);
      if (result.success) {
        successMessage = 'Transaksi berhasil ditambahkan';
        await fetchRecords(pagination.page);
        closeModal();
      }
    }
    loading = false;
    setTimeout(() => successMessage = '', 3000);
  }

  async function handleDelete() {
    if (deleteConfirm) {
      const result = await api.deleteFinanceRecord(deleteConfirm.id);
      if (result.success) {
        successMessage = 'Transaksi berhasil dihapus';
        await fetchRecords(pagination.page);
        deleteConfirm = null;
      }
      setTimeout(() => successMessage = '', 3000);
    }
  }

  function renderCell(item: unknown, key: string, value: unknown): string {
    const record = item as FinanceRecord;
    if (key === 'transactionDate') {
      return formatShortDate(record.transactionDate);
    }
    if (key === 'transactionType') {
      return record.transactionType === 'income' ? 'Pemasukan' : 'Pengeluaran';
    }
    if (key === 'amount') {
      const prefix = record.transactionType === 'income' ? '+' : '-';
      return `${prefix}${formatCurrency(record.amount)}`;
    }
    return value != null ? String(value) : '-';
  }

  const columns = [
    { key: 'transactionDate', label: 'Tanggal' },
    { key: 'transactionType', label: 'Tipe' },
    { key: 'category', label: 'Kategori' },
    { key: 'amount', label: 'Jumlah', align: 'right' as const },
    { key: 'notes', label: 'Catatan' },
  ];
</script>

<MainLayout currentPath="/finance" pageTitle="Keuangan">
  <div class="page">
    {#if successMessage}
      <Alert variant="success" dismissible on:dismiss={() => successMessage = ''}>
        {successMessage}
      </Alert>
    {/if}

    <div class="stats-grid">
      <StatCard 
        title="Total Pemasukan" 
        value={formatCurrency(totalIncome)}
        variant="success"
        icon={TrendingUp}
      />
      <StatCard 
        title="Total Pengeluaran" 
        value={formatCurrency(totalExpense)}
        variant="danger"
        icon={TrendingDown}
      />
      <StatCard 
        title="Profit Bersih" 
        value={formatCurrency(netProfit)}
        variant={netProfit >= 0 ? 'success' : 'danger'}
        icon={DollarSign}
      />
    </div>

    <div class="page-header">
      <div>
        <h2 class="page-title">Riwayat Transaksi</h2>
        <p class="page-subtitle">Kelola pemasukan dan pengeluaran</p>
      </div>
      <div class="header-actions">
        <Select
          options={[
            { value: 'all', label: 'Semua' },
            { value: 'income', label: 'Pemasukan' },
            { value: 'expense', label: 'Pengeluaran' },
          ]}
          bind:value={filterType}
          on:change={() => fetchRecords(1)}
        />
        <Button on:click={openCreateModal}>
          <Plus size={18} />
          Tambah Transaksi
        </Button>
      </div>
    </div>

    <Card padding="none">
      <Table {columns} data={records} {loading} {renderCell}>
        {#snippet actions({ item })}
          <div class="action-buttons">
            <Button variant="ghost" size="sm" on:click={() => openEditModal(item as FinanceRecord)}>
              <Edit2 size={16} />
            </Button>
            <Button variant="ghost" size="sm" on:click={() => deleteConfirm = item as FinanceRecord}>
              <Trash2 size={16} />
            </Button>
          </div>
        {/snippet}
      </Table>
      
      <div class="pagination-wrapper">
        <Pagination 
          currentPage={pagination.page}
          totalPages={pagination.totalPages}
          totalItems={pagination.total}
          itemsPerPage={pagination.limit}
          on:change={(e) => fetchRecords(e.detail)}
        />
      </div>
    </Card>
  </div>

  <Modal open={showModal} title={editingRecord ? 'Edit Transaksi' : 'Tambah Transaksi'} on:close={closeModal}>
    <form class="form" on:submit|preventDefault={handleSubmit}>
      <Select
        label="Tipe Transaksi"
        options={[
          { value: 'expense', label: 'Pengeluaran' },
          { value: 'income', label: 'Pemasukan' },
        ]}
        bind:value={formData.transactionType}
        required
      />
      <Input
        type="date"
        label="Tanggal"
        bind:value={formData.transactionDate}
        required
      />
      <Select
        label="Kategori"
        options={categories}
        bind:value={formData.category}
        required
      />
      <Input
        type="number"
        label="Jumlah (Rp)"
        placeholder="Contoh: 1500000"
        bind:value={formData.amount}
        required
      />
      <Textarea
        label="Catatan"
        placeholder="Keterangan tambahan..."
        bind:value={formData.notes}
      />
    </form>
    <div slot="footer">
      <Button variant="secondary" on:click={closeModal}>Batal</Button>
      <Button on:click={handleSubmit} {loading}>
        {editingRecord ? 'Simpan' : 'Tambah'}
      </Button>
    </div>
  </Modal>

  <Modal open={!!deleteConfirm} title="Konfirmasi Hapus" size="sm" on:close={() => deleteConfirm = null}>
    <p>Apakah Anda yakin ingin menghapus transaksi ini?</p>
    <div slot="footer">
      <Button variant="secondary" on:click={() => deleteConfirm = null}>Batal</Button>
      <Button variant="danger" on:click={handleDelete}>Hapus</Button>
    </div>
  </Modal>
</MainLayout>

<style>
  .page {
    display: flex;
    flex-direction: column;
    gap: 1.5rem;
  }

  .stats-grid {
    display: grid;
    grid-template-columns: repeat(auto-fit, minmax(240px, 1fr));
    gap: 1rem;
  }

  .page-header {
    display: flex;
    align-items: center;
    justify-content: space-between;
    flex-wrap: wrap;
    gap: 1rem;
  }

  .page-title {
    margin: 0;
    font-size: 1.5rem;
    font-weight: 600;
  }

  .page-subtitle {
    margin: 0.25rem 0 0;
    color: var(--color-text-muted);
    font-size: 0.875rem;
  }

  .header-actions {
    display: flex;
    gap: 0.75rem;
    align-items: center;
  }

  .action-buttons {
    display: flex;
    gap: 0.25rem;
  }

  .pagination-wrapper {
    padding: 0 1rem;
  }

  .form {
    display: flex;
    flex-direction: column;
    gap: 1rem;
  }

  @media (max-width: 640px) {
    .page {
      gap: 1rem;
    }

    .stats-grid {
      grid-template-columns: 1fr;
      gap: 0.75rem;
    }

    .page-header {
      flex-direction: column;
      align-items: stretch;
    }

    .page-header > div:first-child {
      text-align: center;
    }

    .page-title {
      font-size: 1.25rem;
    }

    .page-subtitle {
      font-size: 0.8125rem;
    }

    .header-actions {
      flex-direction: column;
      width: 100%;
    }

    .header-actions :global(select),
    .header-actions :global(button) {
      width: 100%;
    }

    .header-actions :global(button) {
      justify-content: center;
    }

    .pagination-wrapper {
      padding: 0 0.75rem;
    }
  }
</style>
