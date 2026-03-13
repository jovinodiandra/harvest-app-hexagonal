<script lang="ts">
  import { onMount } from 'svelte';
  import { Plus, Edit2, Trash2, Clock, Calendar } from 'lucide-svelte';
  import { MainLayout } from '../../../widgets';
  import { Button, Card, Modal, Input, Select, Table, Pagination, Alert } from '../../../shared/ui';
  import { api } from '../../../shared/api';
  import { pondStore, pondsForSelect } from '../../../entities/pond';
  import type { FeedSchedule } from '../../../shared/types';

  let schedules: FeedSchedule[] = [];
  let loading = false;
  let pagination = { page: 1, limit: 10, total: 0, totalPages: 0 };
  
  let showModal = false;
  let editingSchedule: FeedSchedule | null = null;
  let deleteConfirm: FeedSchedule | null = null;
  let successMessage = '';
  
  let formData = {
    pondsId: '',
    feedType: '',
    feedAmount: 0,
    feedTime: '07:00',
    organizationId: 'org-001',
  };

  const feedTypes = [
    { value: 'PF-500', label: 'PF-500 (Starter)' },
    { value: 'PF-1000', label: 'PF-1000 (Grower)' },
    { value: 'HI-PRO', label: 'HI-PRO (Finisher)' },
    { value: 'Pelet Apung', label: 'Pelet Apung' },
    { value: 'Pelet Tenggelam', label: 'Pelet Tenggelam' },
  ];

  onMount(async () => {
    await pondStore.fetchAllPonds();
    await fetchSchedules();
  });

  async function fetchSchedules(page = 1) {
    loading = true;
    const response = await api.getFeedSchedules(page, 10);
    if (response.success && response.data) {
      schedules = response.data.data;
      pagination = {
        page: response.data.page,
        limit: response.data.limit,
        total: response.data.total,
        totalPages: response.data.totalPages,
      };
    }
    loading = false;
  }

  function getPondName(pondsId: string): string {
    const pond = $pondStore.ponds.find(p => p.id === pondsId);
    return pond?.name || '-';
  }

  function openCreateModal() {
    editingSchedule = null;
    formData = {
      pondsId: '',
      feedType: '',
      feedAmount: 0,
      feedTime: '07:00',
      organizationId: 'org-001',
    };
    showModal = true;
  }

  function openEditModal(schedule: FeedSchedule) {
    editingSchedule = schedule;
    formData = { ...schedule };
    showModal = true;
  }

  function closeModal() {
    showModal = false;
    editingSchedule = null;
  }

  async function handleSubmit() {
    loading = true;
    if (editingSchedule) {
      const result = await api.updateFeedSchedule(editingSchedule.id, formData);
      if (result.success) {
        successMessage = 'Jadwal pakan berhasil diperbarui';
        await fetchSchedules(pagination.page);
        closeModal();
      }
    } else {
      const result = await api.createFeedSchedule(formData);
      if (result.success) {
        successMessage = 'Jadwal pakan berhasil ditambahkan';
        await fetchSchedules(pagination.page);
        closeModal();
      }
    }
    loading = false;
    setTimeout(() => successMessage = '', 3000);
  }

  async function handleDelete() {
    if (deleteConfirm) {
      const result = await api.deleteFeedSchedule(deleteConfirm.id);
      if (result.success) {
        successMessage = 'Jadwal pakan berhasil dihapus';
        await fetchSchedules(pagination.page);
        deleteConfirm = null;
      }
      setTimeout(() => successMessage = '', 3000);
    }
  }

  function renderCell(item: unknown, key: string, value: unknown): string {
    const schedule = item as FeedSchedule;
    if (key === 'pondsId') {
      return getPondName(schedule.pondsId);
    }
    if (key === 'feedAmount') {
      return `${schedule.feedAmount} kg`;
    }
    return value != null ? String(value) : '-';
  }

  const columns = [
    { key: 'pondsId', label: 'Kolam' },
    { key: 'feedType', label: 'Jenis Pakan' },
    { key: 'feedAmount', label: 'Jumlah', align: 'right' as const },
    { key: 'feedTime', label: 'Waktu', align: 'center' as const },
  ];
</script>

<MainLayout currentPath="/feed-schedules" pageTitle="Jadwal Pakan">
  <div class="page">
    {#if successMessage}
      <Alert variant="success" dismissible on:dismiss={() => successMessage = ''}>
        {successMessage}
      </Alert>
    {/if}

    <div class="page-header">
      <div>
        <h2 class="page-title">Jadwal Pemberian Pakan</h2>
        <p class="page-subtitle">Atur jadwal pemberian pakan untuk setiap kolam</p>
      </div>
      <Button on:click={openCreateModal}>
        <Plus size={18} />
        Tambah Jadwal
      </Button>
    </div>

    <Card padding="none">
      <Table {columns} data={schedules} {loading} {renderCell}>
        {#snippet actions({ item })}
          <div class="action-buttons">
            <Button variant="ghost" size="sm" on:click={() => openEditModal(item as FeedSchedule)}>
              <Edit2 size={16} />
            </Button>
            <Button variant="ghost" size="sm" on:click={() => deleteConfirm = item as FeedSchedule}>
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
          on:change={(e) => fetchSchedules(e.detail)}
        />
      </div>
    </Card>
  </div>

  <Modal open={showModal} title={editingSchedule ? 'Edit Jadwal Pakan' : 'Tambah Jadwal Pakan'} on:close={closeModal}>
    <form class="form" on:submit|preventDefault={handleSubmit}>
      <Select
        label="Kolam"
        options={$pondsForSelect}
        bind:value={formData.pondsId}
        required
      />
      <Select
        label="Jenis Pakan"
        options={feedTypes}
        bind:value={formData.feedType}
        required
      />
      <Input
        type="number"
        step="0.1"
        label="Jumlah Pakan (kg)"
        placeholder="Contoh: 5"
        bind:value={formData.feedAmount}
        required
      />
      <Input
        type="time"
        label="Waktu Pemberian"
        bind:value={formData.feedTime}
        required
      />
    </form>
    <div slot="footer">
      <Button variant="secondary" on:click={closeModal}>Batal</Button>
      <Button on:click={handleSubmit} {loading}>
        {editingSchedule ? 'Simpan' : 'Tambah'}
      </Button>
    </div>
  </Modal>

  <Modal open={!!deleteConfirm} title="Konfirmasi Hapus" size="sm" on:close={() => deleteConfirm = null}>
    <p>Apakah Anda yakin ingin menghapus jadwal pakan ini?</p>
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
</style>
