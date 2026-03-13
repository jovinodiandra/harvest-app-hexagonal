<script lang="ts">
  import { onMount } from 'svelte';
  import { Plus, Edit2, Trash2, Truck, Scale } from 'lucide-svelte';
  import { MainLayout } from '../../../widgets';
  import { Button, Card, Modal, Input, Select, Textarea, Table, Pagination, Alert, StatCard } from '../../../shared/ui';
  import { api } from '../../../shared/api';
  import { pondStore, pondsForSelect } from '../../../entities/pond';
  import type { HarvestRecord } from '../../../shared/types';
  import { formatNumber, formatShortDate, formatWeight, formatCurrency } from '../../../shared/lib/utils';

  let records: HarvestRecord[] = [];
  let loading = false;
  let pagination = { page: 1, limit: 10, total: 0, totalPages: 0 };
  
  let showModal = false;
  let editingRecord: HarvestRecord | null = null;
  let deleteConfirm: HarvestRecord | null = null;
  let successMessage = '';
  
  let formData = {
    pondsId: '',
    pondsName: '',
    harvestDate: new Date().toISOString().split('T')[0],
    harvestFishCount: 0,
    totalWeight: 0,
    notes: '',
    organizationId: 'org-001',
  };

  $: totalWeight = records.reduce((sum, r) => sum + r.totalWeight, 0);
  $: totalFish = records.reduce((sum, r) => sum + r.harvestFishCount, 0);
  $: estimatedRevenue = totalWeight * 30000;

  onMount(async () => {
    await pondStore.fetchAllPonds();
    await fetchRecords();
  });

  async function fetchRecords(page = 1) {
    loading = true;
    const response = await api.getHarvestRecords(page, 10);
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
      pondsId: '',
      pondsName: '',
      harvestDate: new Date().toISOString().split('T')[0],
      harvestFishCount: 0,
      totalWeight: 0,
      notes: '',
      organizationId: 'org-001',
    };
    showModal = true;
  }

  function openEditModal(record: HarvestRecord) {
    editingRecord = record;
    formData = { ...record };
    showModal = true;
  }

  function closeModal() {
    showModal = false;
    editingRecord = null;
  }

  function handlePondChange() {
    const pond = $pondStore.ponds.find(p => p.id === formData.pondsId);
    if (pond) {
      formData.pondsName = pond.name;
    }
  }

  async function handleSubmit() {
    loading = true;
    if (editingRecord) {
      const result = await api.updateHarvestRecord(editingRecord.id, formData);
      if (result.success) {
        successMessage = 'Data panen berhasil diperbarui';
        await fetchRecords(pagination.page);
        closeModal();
      }
    } else {
      const result = await api.createHarvestRecord(formData);
      if (result.success) {
        successMessage = 'Data panen berhasil ditambahkan';
        await fetchRecords(pagination.page);
        closeModal();
      }
    }
    loading = false;
    setTimeout(() => successMessage = '', 3000);
  }

  async function handleDelete() {
    if (deleteConfirm) {
      const result = await api.deleteHarvestRecord(deleteConfirm.id);
      if (result.success) {
        successMessage = 'Data panen berhasil dihapus';
        await fetchRecords(pagination.page);
        deleteConfirm = null;
      }
      setTimeout(() => successMessage = '', 3000);
    }
  }

  function renderCell(item: unknown, key: string, value: unknown): string {
    const record = item as HarvestRecord;
    if (key === 'harvestDate') {
      return formatShortDate(record.harvestDate);
    }
    if (key === 'harvestFishCount') {
      return `${formatNumber(record.harvestFishCount)} ekor`;
    }
    if (key === 'totalWeight') {
      return formatWeight(record.totalWeight);
    }
    return value != null ? String(value) : '-';
  }

  const columns = [
    { key: 'harvestDate', label: 'Tanggal Panen' },
    { key: 'pondsName', label: 'Kolam' },
    { key: 'harvestFishCount', label: 'Jumlah Ikan', align: 'right' as const },
    { key: 'totalWeight', label: 'Total Berat', align: 'right' as const },
    { key: 'notes', label: 'Catatan' },
  ];
</script>

<MainLayout currentPath="/harvest" pageTitle="Panen">
  <div class="page">
    {#if successMessage}
      <Alert variant="success" dismissible on:dismiss={() => successMessage = ''}>
        {successMessage}
      </Alert>
    {/if}

    <div class="stats-grid">
      <StatCard 
        title="Total Panen" 
        value="{records.length} kali"
        variant="primary"
        icon={Truck}
      />
      <StatCard 
        title="Total Berat" 
        value={formatWeight(totalWeight)}
        variant="success"
        icon={Scale}
      />
      <StatCard 
        title="Estimasi Pendapatan" 
        value={formatCurrency(estimatedRevenue)}
        subtitle="@Rp 30.000/kg"
        variant="warning"
      />
    </div>

    <div class="page-header">
      <div>
        <h2 class="page-title">Riwayat Panen</h2>
        <p class="page-subtitle">Catatan hasil panen dari semua kolam</p>
      </div>
      <Button on:click={openCreateModal}>
        <Plus size={18} />
        Catat Panen
      </Button>
    </div>

    <Card padding="none">
      <Table {columns} data={records} {loading} {renderCell}>
        {#snippet actions({ item })}
          <div class="action-buttons">
            <Button variant="ghost" size="sm" on:click={() => openEditModal(item as HarvestRecord)}>
              <Edit2 size={16} />
            </Button>
            <Button variant="ghost" size="sm" on:click={() => deleteConfirm = item as HarvestRecord}>
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

  <Modal open={showModal} title={editingRecord ? 'Edit Data Panen' : 'Catat Panen Baru'} on:close={closeModal}>
    <form class="form" on:submit|preventDefault={handleSubmit}>
      <Select
        label="Kolam"
        options={$pondsForSelect}
        bind:value={formData.pondsId}
        on:change={handlePondChange}
        required
      />
      <Input
        type="date"
        label="Tanggal Panen"
        bind:value={formData.harvestDate}
        required
      />
      <Input
        type="number"
        label="Jumlah Ikan (ekor)"
        placeholder="Contoh: 4000"
        bind:value={formData.harvestFishCount}
        required
      />
      <Input
        type="number"
        label="Total Berat (kg)"
        placeholder="Contoh: 400"
        bind:value={formData.totalWeight}
        required
      />
      <Textarea
        label="Catatan"
        placeholder="Catatan tambahan..."
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
    <p>Apakah Anda yakin ingin menghapus data panen ini?</p>
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
      grid-template-columns: repeat(2, 1fr);
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

    .page-header :global(button) {
      width: 100%;
      justify-content: center;
    }

    .pagination-wrapper {
      padding: 0 0.75rem;
    }
  }

  @media (max-width: 400px) {
    .stats-grid {
      grid-template-columns: 1fr;
    }
  }
</style>
