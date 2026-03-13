<script lang="ts">
  import { onMount } from 'svelte';
  import { Plus, Edit2, Trash2, MapPin } from 'lucide-svelte';
  import { MainLayout } from '../../../widgets';
  import { Button, Card, Modal, Input, Table, Pagination, Alert } from '../../../shared/ui';
  import { pondStore } from '../../../entities/pond';
  import type { Pond } from '../../../shared/types';
  import { formatNumber } from '../../../shared/lib/utils';

  let showModal = false;
  let editingPond: Pond | null = null;
  let deleteConfirm: Pond | null = null;
  let successMessage = '';
  
  let formData = {
    name: '',
    capacity: 0,
    location: '',
    organizationId: 'org-001',
  };

  $: ({ ponds, loading, pagination } = $pondStore);

  onMount(() => {
    pondStore.fetchPonds();
  });

  function openCreateModal() {
    editingPond = null;
    formData = { name: '', capacity: 0, location: '', organizationId: 'org-001' };
    showModal = true;
  }

  function openEditModal(pond: Pond) {
    editingPond = pond;
    formData = { 
      name: pond.name, 
      capacity: pond.capacity, 
      location: pond.location,
      organizationId: pond.organizationId,
    };
    showModal = true;
  }

  function closeModal() {
    showModal = false;
    editingPond = null;
  }

  async function handleSubmit() {
    if (editingPond) {
      const result = await pondStore.updatePond(editingPond.id, formData);
      if (result.success) {
        successMessage = 'Kolam berhasil diperbarui';
        closeModal();
      }
    } else {
      const result = await pondStore.createPond(formData);
      if (result.success) {
        successMessage = 'Kolam berhasil ditambahkan';
        closeModal();
      }
    }
    setTimeout(() => successMessage = '', 3000);
  }

  async function handleDelete() {
    if (deleteConfirm) {
      const result = await pondStore.deletePond(deleteConfirm.id);
      if (result.success) {
        successMessage = 'Kolam berhasil dihapus';
        deleteConfirm = null;
      }
      setTimeout(() => successMessage = '', 3000);
    }
  }

  function handlePageChange(event: CustomEvent<number>) {
    pondStore.fetchPonds(event.detail, pagination.limit);
  }

  function renderCell(item: unknown, key: string, value: unknown): string {
    const pond = item as Pond;
    if (key === 'capacity') {
      return `${formatNumber(pond.capacity)} ekor`;
    }
    return value != null ? String(value) : '-';
  }

  const columns = [
    { key: 'name', label: 'Nama Kolam' },
    { key: 'capacity', label: 'Kapasitas', align: 'right' as const },
    { key: 'location', label: 'Lokasi' },
  ];
</script>

<MainLayout currentPath="/ponds" pageTitle="Manajemen Kolam">
  <div class="page">
    {#if successMessage}
      <Alert variant="success" dismissible on:dismiss={() => successMessage = ''}>
        {successMessage}
      </Alert>
    {/if}

    <div class="page-header">
      <div>
        <h2 class="page-title">Daftar Kolam</h2>
        <p class="page-subtitle">Kelola semua kolam budidaya lele Anda</p>
      </div>
      <Button on:click={openCreateModal}>
        <Plus size={18} />
        Tambah Kolam
      </Button>
    </div>

    <Card padding="none">
      <Table {columns} data={ponds} {loading} {renderCell}>
        {#snippet actions({ item })}
          <div class="action-buttons">
            <Button variant="ghost" size="sm" on:click={() => openEditModal(item as Pond)}>
              <Edit2 size={16} />
            </Button>
            <Button variant="ghost" size="sm" on:click={() => deleteConfirm = item as Pond}>
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
          on:change={handlePageChange}
        />
      </div>
    </Card>
  </div>

  <Modal open={showModal} title={editingPond ? 'Edit Kolam' : 'Tambah Kolam'} on:close={closeModal}>
    <form class="form" on:submit|preventDefault={handleSubmit}>
      <Input
        label="Nama Kolam"
        placeholder="Contoh: Kolam A1"
        bind:value={formData.name}
        required
      />
      <Input
        type="number"
        label="Kapasitas (ekor)"
        placeholder="Contoh: 5000"
        bind:value={formData.capacity}
        required
      />
      <Input
        label="Lokasi"
        placeholder="Contoh: Blok A - Utara"
        bind:value={formData.location}
        required
      />
    </form>
    <div slot="footer">
      <Button variant="secondary" on:click={closeModal}>Batal</Button>
      <Button on:click={handleSubmit} loading={loading}>
        {editingPond ? 'Simpan' : 'Tambah'}
      </Button>
    </div>
  </Modal>

  <Modal open={!!deleteConfirm} title="Konfirmasi Hapus" size="sm" on:close={() => deleteConfirm = null}>
    <p>Apakah Anda yakin ingin menghapus kolam <strong>{deleteConfirm?.name}</strong>?</p>
    <p class="warning-text">Tindakan ini tidak dapat dibatalkan.</p>
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
    color: var(--color-text);
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

  .warning-text {
    color: var(--color-danger);
    font-size: 0.875rem;
  }
</style>
