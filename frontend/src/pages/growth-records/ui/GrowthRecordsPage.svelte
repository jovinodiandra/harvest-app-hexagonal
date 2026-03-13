<script lang="ts">
  import { onMount } from 'svelte';
  import { Plus, Edit2, Trash2, TrendingUp, Ruler } from 'lucide-svelte';
  import { MainLayout } from '../../../widgets';
  import { Button, Card, Modal, Input, Select, Table, Pagination, Alert, StatCard } from '../../../shared/ui';
  import { api } from '../../../shared/api';
  import { pondStore, pondsForSelect } from '../../../entities/pond';
  import type { GrowthRecord } from '../../../shared/types';
  import { formatShortDate } from '../../../shared/lib/utils';

  let records: GrowthRecord[] = [];
  let loading = false;
  let pagination = { page: 1, limit: 10, total: 0, totalPages: 0 };
  let selectedPondId = '';
  
  let showModal = false;
  let editingRecord: GrowthRecord | null = null;
  let deleteConfirm: GrowthRecord | null = null;
  let successMessage = '';
  
  let formData = {
    pondsId: '',
    recordDate: new Date().toISOString().split('T')[0],
    averageWidth: 0,
    averageLength: 0,
    organizationId: 'org-001',
  };

  $: avgWidth = records.length ? (records.reduce((sum, r) => sum + r.averageWidth, 0) / records.length).toFixed(1) : '-';
  $: avgLength = records.length ? (records.reduce((sum, r) => sum + r.averageLength, 0) / records.length).toFixed(1) : '-';
  $: latestRecord = records.length ? records[0] : null;
  
  $: if ($pondStore.ponds.length > 0 && !selectedPondId) {
    selectedPondId = $pondStore.ponds[0].id;
    fetchRecords();
  }

  onMount(async () => {
    await pondStore.fetchAllPonds();
  });

  async function fetchRecords(page = 1) {
    if (!selectedPondId) return;
    loading = true;
    const response = await api.getGrowthRecords(page, 10, selectedPondId);
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
  
  async function handlePondChange() {
    await fetchRecords(1);
  }

  function getPondName(pondsId: string): string {
    const pond = $pondStore.ponds.find(p => p.id === pondsId);
    return pond?.name || '-';
  }

  function openCreateModal() {
    editingRecord = null;
    formData = {
      pondsId: selectedPondId,
      recordDate: new Date().toISOString().split('T')[0],
      averageWidth: 0,
      averageLength: 0,
      organizationId: 'org-001',
    };
    showModal = true;
  }

  function openEditModal(record: GrowthRecord) {
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
      const result = await api.updateGrowthRecord(editingRecord.id, formData);
      if (result.success) {
        successMessage = 'Data pertumbuhan berhasil diperbarui';
        await fetchRecords(pagination.page);
        closeModal();
      }
    } else {
      const result = await api.createGrowthRecord(formData);
      if (result.success) {
        successMessage = 'Data pertumbuhan berhasil ditambahkan';
        await fetchRecords(pagination.page);
        closeModal();
      }
    }
    loading = false;
    setTimeout(() => successMessage = '', 3000);
  }

  async function handleDelete() {
    if (deleteConfirm) {
      const result = await api.deleteGrowthRecord(deleteConfirm.id);
      if (result.success) {
        successMessage = 'Data pertumbuhan berhasil dihapus';
        await fetchRecords(pagination.page);
        deleteConfirm = null;
      }
      setTimeout(() => successMessage = '', 3000);
    }
  }

  function renderCell(item: unknown, key: string, value: unknown): string {
    const record = item as GrowthRecord;
    if (key === 'recordDate') {
      return formatShortDate(record.recordDate);
    }
    if (key === 'pondsId') {
      return getPondName(record.pondsId);
    }
    if (key === 'averageWidth') {
      return `${record.averageWidth} cm`;
    }
    if (key === 'averageLength') {
      return `${record.averageLength} cm`;
    }
    return value != null ? String(value) : '-';
  }

  const columns = [
    { key: 'recordDate', label: 'Tanggal' },
    { key: 'pondsId', label: 'Kolam' },
    { key: 'averageWidth', label: 'Lebar Rata-rata', align: 'center' as const },
    { key: 'averageLength', label: 'Panjang Rata-rata', align: 'center' as const },
  ];
</script>

<MainLayout currentPath="/growth-records" pageTitle="Pertumbuhan">
  <div class="page">
    {#if successMessage}
      <Alert variant="success" dismissible on:dismiss={() => successMessage = ''}>
        {successMessage}
      </Alert>
    {/if}

    <div class="stats-grid">
      <StatCard 
        title="Rata-rata Lebar" 
        value="{avgWidth} cm"
        variant="primary"
        icon={Ruler}
      />
      <StatCard 
        title="Rata-rata Panjang" 
        value="{avgLength} cm"
        variant="success"
        icon={TrendingUp}
      />
      <StatCard 
        title="Total Pengukuran" 
        value="{records.length}"
        subtitle="catatan pertumbuhan"
        variant="info"
      />
    </div>

    <div class="page-header">
      <div>
        <h2 class="page-title">Catatan Pertumbuhan</h2>
        <p class="page-subtitle">Pantau perkembangan ukuran ikan di setiap kolam</p>
      </div>
      <div class="header-actions">
        <Select
          label=""
          options={$pondsForSelect}
          bind:value={selectedPondId}
          on:change={handlePondChange}
          placeholder="Pilih Kolam"
        />
        <Button on:click={openCreateModal}>
          <Plus size={18} />
          Tambah Catatan
        </Button>
      </div>
    </div>

    <Card padding="none">
      <Table {columns} data={records} {loading} {renderCell}>
        {#snippet actions({ item })}
          <div class="action-buttons">
            <Button variant="ghost" size="sm" on:click={() => openEditModal(item as GrowthRecord)}>
              <Edit2 size={16} />
            </Button>
            <Button variant="ghost" size="sm" on:click={() => deleteConfirm = item as GrowthRecord}>
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

  <Modal open={showModal} title={editingRecord ? 'Edit Catatan Pertumbuhan' : 'Tambah Catatan Pertumbuhan'} on:close={closeModal}>
    <form class="form" on:submit|preventDefault={handleSubmit}>
      <Select
        label="Kolam"
        options={$pondsForSelect}
        bind:value={formData.pondsId}
        required
      />
      <Input
        type="date"
        label="Tanggal Pengukuran"
        bind:value={formData.recordDate}
        required
      />
      <Input
        type="number"
        step="0.1"
        label="Lebar Rata-rata (cm)"
        placeholder="Contoh: 2.5"
        bind:value={formData.averageWidth}
        required
      />
      <Input
        type="number"
        step="0.1"
        label="Panjang Rata-rata (cm)"
        placeholder="Contoh: 8.0"
        bind:value={formData.averageLength}
        required
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
    <p>Apakah Anda yakin ingin menghapus catatan pertumbuhan ini?</p>
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
    align-items: flex-end;
    gap: 1rem;
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
