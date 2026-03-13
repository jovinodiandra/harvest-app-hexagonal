<script lang="ts">
  import { onMount } from 'svelte';
  import { Plus, Edit2, Trash2, Droplets, Thermometer, Activity } from 'lucide-svelte';
  import { MainLayout } from '../../../widgets';
  import { Button, Card, Modal, Input, Select, Table, Pagination, Alert, StatCard } from '../../../shared/ui';
  import { api } from '../../../shared/api';
  import { pondStore, pondsForSelect } from '../../../entities/pond';
  import type { WaterQuality } from '../../../shared/types';
  import { formatShortDate } from '../../../shared/lib/utils';

  let records: WaterQuality[] = [];
  let loading = false;
  let pagination = { page: 1, limit: 10, total: 0, totalPages: 0 };
  let selectedPondId = '';
  
  let showModal = false;
  let editingRecord: WaterQuality | null = null;
  let deleteConfirm: WaterQuality | null = null;
  let successMessage = '';
  
  let formData = {
    pondsId: '',
    recordDate: new Date().toISOString().split('T')[0],
    ph: 7.0,
    temperature: 28,
    dissolvedOxygen: 5.5,
    organizationId: 'org-001',
  };

  function getPondName(pondsId: string): string {
    const pond = $pondStore.ponds.find(p => p.id === pondsId);
    return pond?.name || '-';
  }

  $: avgPh = records.length ? (records.reduce((sum, r) => sum + r.ph, 0) / records.length).toFixed(1) : '-';
  $: avgTemp = records.length ? (records.reduce((sum, r) => sum + r.temperature, 0) / records.length).toFixed(1) : '-';
  $: avgOxygen = records.length ? (records.reduce((sum, r) => sum + r.dissolvedOxygen, 0) / records.length).toFixed(1) : '-';
  
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
    const response = await api.getWaterQuality(page, 10, selectedPondId);
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

  function openCreateModal() {
    editingRecord = null;
    formData = {
      pondsId: selectedPondId,
      recordDate: new Date().toISOString().split('T')[0],
      ph: 7.0,
      temperature: 28,
      dissolvedOxygen: 5.5,
      organizationId: 'org-001',
    };
    showModal = true;
  }

  function openEditModal(record: WaterQuality) {
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
      const result = await api.updateWaterQuality(editingRecord.id, formData);
      if (result.success) {
        successMessage = 'Data kualitas air berhasil diperbarui';
        await fetchRecords(pagination.page);
        closeModal();
      }
    } else {
      const result = await api.createWaterQuality(formData);
      if (result.success) {
        successMessage = 'Data kualitas air berhasil ditambahkan';
        await fetchRecords(pagination.page);
        closeModal();
      }
    }
    loading = false;
    setTimeout(() => successMessage = '', 3000);
  }

  async function handleDelete() {
    if (deleteConfirm) {
      const result = await api.deleteWaterQuality(deleteConfirm.id);
      if (result.success) {
        successMessage = 'Data berhasil dihapus';
        await fetchRecords(pagination.page);
        deleteConfirm = null;
      }
      setTimeout(() => successMessage = '', 3000);
    }
  }

  function renderCell(item: unknown, key: string, value: unknown): string {
    const record = item as WaterQuality;
    if (key === 'recordDate') {
      return formatShortDate(record.recordDate);
    }
    if (key === 'pondsId') {
      return getPondName(record.pondsId);
    }
    if (key === 'temperature') {
      return `${record.temperature}°C`;
    }
    if (key === 'dissolvedOxygen') {
      return `${record.dissolvedOxygen} mg/L`;
    }
    return value != null ? String(value) : '-';
  }

  const columns = [
    { key: 'recordDate', label: 'Tanggal' },
    { key: 'pondsId', label: 'Kolam' },
    { key: 'ph', label: 'pH', align: 'center' as const },
    { key: 'temperature', label: 'Suhu', align: 'center' as const },
    { key: 'dissolvedOxygen', label: 'DO', align: 'center' as const },
  ];
</script>

<MainLayout currentPath="/water-quality" pageTitle="Kualitas Air">
  <div class="page">
    {#if successMessage}
      <Alert variant="success" dismissible on:dismiss={() => successMessage = ''}>
        {successMessage}
      </Alert>
    {/if}

    <div class="stats-grid">
      <StatCard 
        title="Rata-rata pH" 
        value={avgPh}
        subtitle="Ideal: 6.5 - 8.5"
        variant="info"
        icon={Activity}
      />
      <StatCard 
        title="Rata-rata Suhu" 
        value="{avgTemp}°C"
        subtitle="Ideal: 25 - 30°C"
        variant="warning"
        icon={Thermometer}
      />
      <StatCard 
        title="Rata-rata DO" 
        value="{avgOxygen} mg/L"
        subtitle="Ideal: > 5 mg/L"
        variant="primary"
        icon={Droplets}
      />
    </div>

    <div class="page-header">
      <div>
        <h2 class="page-title">Pengukuran Kualitas Air</h2>
        <p class="page-subtitle">Pantau kondisi air di setiap kolam</p>
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
          Tambah Pengukuran
        </Button>
      </div>
    </div>

    <Card padding="none">
      <Table {columns} data={records} {loading} {renderCell}>
        {#snippet actions({ item })}
          <div class="action-buttons">
            <Button variant="ghost" size="sm" on:click={() => openEditModal(item as WaterQuality)}>
              <Edit2 size={16} />
            </Button>
            <Button variant="ghost" size="sm" on:click={() => deleteConfirm = item as WaterQuality}>
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

  <Modal open={showModal} title={editingRecord ? 'Edit Pengukuran' : 'Tambah Pengukuran'} on:close={closeModal}>
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
        label="pH"
        placeholder="Contoh: 7.2"
        bind:value={formData.ph}
        required
      />
      <Input
        type="number"
        step="0.1"
        label="Suhu (°C)"
        placeholder="Contoh: 28"
        bind:value={formData.temperature}
        required
      />
      <Input
        type="number"
        step="0.1"
        label="Dissolved Oxygen (mg/L)"
        placeholder="Contoh: 5.5"
        bind:value={formData.dissolvedOxygen}
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
    <p>Apakah Anda yakin ingin menghapus data pengukuran ini?</p>
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
