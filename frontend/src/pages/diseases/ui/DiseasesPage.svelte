<script lang="ts">
  import { onMount } from 'svelte';
  import { Plus, Edit2, Trash2, Bug, AlertCircle } from 'lucide-svelte';
  import { MainLayout } from '../../../widgets';
  import { Button, Card, Modal, Input, Select, Textarea, Table, Pagination, Alert, StatCard } from '../../../shared/ui';
  import { api } from '../../../shared/api';
  import { pondStore, pondsForSelect } from '../../../entities/pond';
  import type { DiseasesRecord } from '../../../shared/types';
  import { formatShortDate, formatNumber } from '../../../shared/lib/utils';

  let records: DiseasesRecord[] = [];
  let loading = false;
  let pagination = { page: 1, limit: 10, total: 0, totalPages: 0 };
  let selectedPondId = '';
  
  let showModal = false;
  let editingRecord: DiseasesRecord | null = null;
  let deleteConfirm: DiseasesRecord | null = null;
  let successMessage = '';
  
  let formData = {
    pondsId: '',
    diseaseName: '',
    symptoms: '',
    infectedFishCount: 0,
    diseasesDate: new Date().toISOString().split('T')[0],
    notes: '',
    organizationId: 'org-001',
  };

  const commonDiseases = [
    { value: 'White Spot', label: 'White Spot (Ich)' },
    { value: 'Jamur', label: 'Infeksi Jamur' },
    { value: 'Bakteri', label: 'Infeksi Bakteri' },
    { value: 'Parasit', label: 'Serangan Parasit' },
    { value: 'Fin Rot', label: 'Fin Rot (Busuk Sirip)' },
    { value: 'Dropsy', label: 'Dropsy (Perut Kembung)' },
    { value: 'Lainnya', label: 'Penyakit Lainnya' },
  ];

  $: totalInfected = records.reduce((sum, r) => sum + r.infectedFishCount, 0);
  $: uniqueDiseases = [...new Set(records.map(r => r.diseaseName))].length;
  
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
    const response = await api.getDiseasesRecords(page, 10, selectedPondId);
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
      diseaseName: '',
      symptoms: '',
      infectedFishCount: 0,
      diseasesDate: new Date().toISOString().split('T')[0],
      notes: '',
      organizationId: 'org-001',
    };
    showModal = true;
  }

  function openEditModal(record: DiseasesRecord) {
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
      const result = await api.updateDiseasesRecord(editingRecord.id, formData);
      if (result.success) {
        successMessage = 'Data penyakit berhasil diperbarui';
        await fetchRecords(pagination.page);
        closeModal();
      }
    } else {
      const result = await api.createDiseasesRecord(formData);
      if (result.success) {
        successMessage = 'Data penyakit berhasil ditambahkan';
        await fetchRecords(pagination.page);
        closeModal();
      }
    }
    loading = false;
    setTimeout(() => successMessage = '', 3000);
  }

  async function handleDelete() {
    if (deleteConfirm) {
      const result = await api.deleteDiseasesRecord(deleteConfirm.id);
      if (result.success) {
        successMessage = 'Data penyakit berhasil dihapus';
        await fetchRecords(pagination.page);
        deleteConfirm = null;
      }
      setTimeout(() => successMessage = '', 3000);
    }
  }

  function renderCell(item: unknown, key: string, value: unknown): string {
    const record = item as DiseasesRecord;
    if (key === 'diseasesDate') {
      return formatShortDate(record.diseasesDate);
    }
    if (key === 'pondsId') {
      return getPondName(record.pondsId);
    }
    if (key === 'infectedFishCount') {
      return `${formatNumber(record.infectedFishCount)} ekor`;
    }
    return value != null ? String(value) : '-';
  }

  const columns = [
    { key: 'diseasesDate', label: 'Tanggal' },
    { key: 'pondsId', label: 'Kolam' },
    { key: 'diseaseName', label: 'Nama Penyakit' },
    { key: 'infectedFishCount', label: 'Ikan Terinfeksi', align: 'right' as const },
    { key: 'symptoms', label: 'Gejala' },
  ];
</script>

<MainLayout currentPath="/diseases" pageTitle="Penyakit">
  <div class="page">
    {#if successMessage}
      <Alert variant="success" dismissible on:dismiss={() => successMessage = ''}>
        {successMessage}
      </Alert>
    {/if}

    <div class="stats-grid">
      <StatCard 
        title="Total Ikan Terinfeksi" 
        value="{formatNumber(totalInfected)} ekor"
        variant="danger"
        icon={Bug}
      />
      <StatCard 
        title="Jenis Penyakit" 
        value="{uniqueDiseases}"
        subtitle="penyakit berbeda"
        variant="warning"
        icon={AlertCircle}
      />
      <StatCard 
        title="Total Kejadian" 
        value="{records.length}"
        subtitle="kasus tercatat"
        variant="default"
      />
    </div>

    <div class="page-header">
      <div>
        <h2 class="page-title">Catatan Penyakit Ikan</h2>
        <p class="page-subtitle">Dokumentasi penyakit untuk monitoring dan penanganan</p>
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
            <Button variant="ghost" size="sm" on:click={() => openEditModal(item as DiseasesRecord)}>
              <Edit2 size={16} />
            </Button>
            <Button variant="ghost" size="sm" on:click={() => deleteConfirm = item as DiseasesRecord}>
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

  <Modal open={showModal} title={editingRecord ? 'Edit Catatan Penyakit' : 'Tambah Catatan Penyakit'} size="lg" on:close={closeModal}>
    <form class="form" on:submit|preventDefault={handleSubmit}>
      <div class="form-row">
        <Select
          label="Kolam"
          options={$pondsForSelect}
          bind:value={formData.pondsId}
          required
        />
        <Input
          type="date"
          label="Tanggal Ditemukan"
          bind:value={formData.diseasesDate}
          required
        />
      </div>
      <div class="form-row">
        <Select
          label="Nama Penyakit"
          options={commonDiseases}
          bind:value={formData.diseaseName}
          required
        />
        <Input
          type="number"
          label="Jumlah Ikan Terinfeksi"
          placeholder="Contoh: 50"
          bind:value={formData.infectedFishCount}
          required
        />
      </div>
      <Textarea
        label="Gejala yang Terlihat"
        placeholder="Deskripsi gejala yang diamati..."
        bind:value={formData.symptoms}
        required
      />
      <Textarea
        label="Penanganan / Catatan"
        placeholder="Tindakan yang sudah atau akan dilakukan..."
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
    <p>Apakah Anda yakin ingin menghapus catatan penyakit ini?</p>
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

  .form-row {
    display: grid;
    grid-template-columns: 1fr 1fr;
    gap: 1rem;
  }

  @media (max-width: 640px) {
    .form-row {
      grid-template-columns: 1fr;
    }
  }
</style>
