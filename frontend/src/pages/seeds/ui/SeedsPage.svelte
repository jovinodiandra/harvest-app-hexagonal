<script lang="ts">
  import { onMount } from 'svelte';
  import { Plus, Edit2, Trash2 } from 'lucide-svelte';
  import { MainLayout } from '../../../widgets';
  import { Button, Card, Modal, Input, Select, Table, Pagination, Alert } from '../../../shared/ui';
  import { api } from '../../../shared/api';
  import { pondStore, pondsForSelect } from '../../../entities/pond';
  import type { Seed } from '../../../shared/types';
  import { formatNumber, formatShortDate } from '../../../shared/lib/utils';

  let seeds: Seed[] = [];
  let loading = false;
  let pagination = { page: 1, limit: 10, total: 0, totalPages: 0 };
  
  let showModal = false;
  let editingSeed: Seed | null = null;
  let deleteConfirm: Seed | null = null;
  let successMessage = '';
  
  let formData = {
    pondsId: '',
    type: '',
    quantity: 0,
    stockDate: new Date().toISOString().split('T')[0],
    organizationId: 'org-001',
  };

  const seedTypes = [
    { value: 'Lele Sangkuriang', label: 'Lele Sangkuriang' },
    { value: 'Lele Dumbo', label: 'Lele Dumbo' },
    { value: 'Lele Phyton', label: 'Lele Phyton' },
    { value: 'Lele Mutiara', label: 'Lele Mutiara' },
  ];

  onMount(async () => {
    await pondStore.fetchAllPonds();
    await fetchSeeds();
  });

  async function fetchSeeds(page = 1) {
    loading = true;
    const response = await api.getSeeds(page, 10);
    if (response.success && response.data) {
      seeds = response.data.data;
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
    editingSeed = null;
    formData = {
      pondsId: '',
      type: '',
      quantity: 0,
      stockDate: new Date().toISOString().split('T')[0],
      organizationId: 'org-001',
    };
    showModal = true;
  }

  function openEditModal(seed: Seed) {
    editingSeed = seed;
    formData = { ...seed };
    showModal = true;
  }

  function closeModal() {
    showModal = false;
    editingSeed = null;
  }

  async function handleSubmit() {
    loading = true;
    if (editingSeed) {
      const result = await api.updateSeed(editingSeed.id, formData);
      if (result.success) {
        successMessage = 'Bibit berhasil diperbarui';
        await fetchSeeds(pagination.page);
        closeModal();
      }
    } else {
      const result = await api.createSeed(formData);
      if (result.success) {
        successMessage = 'Bibit berhasil ditambahkan';
        await fetchSeeds(pagination.page);
        closeModal();
      }
    }
    loading = false;
    setTimeout(() => successMessage = '', 3000);
  }

  async function handleDelete() {
    if (deleteConfirm) {
      const result = await api.deleteSeed(deleteConfirm.id);
      if (result.success) {
        successMessage = 'Bibit berhasil dihapus';
        await fetchSeeds(pagination.page);
        deleteConfirm = null;
      }
      setTimeout(() => successMessage = '', 3000);
    }
  }

  function renderCell(item: unknown, key: string, value: unknown): string {
    const seed = item as Seed;
    if (key === 'pondsId') {
      return getPondName(seed.pondsId);
    }
    if (key === 'quantity') {
      return `${formatNumber(seed.quantity)} ekor`;
    }
    if (key === 'stockDate') {
      return formatShortDate(seed.stockDate);
    }
    return value != null ? String(value) : '-';
  }

  const columns = [
    { key: 'pondsId', label: 'Kolam' },
    { key: 'type', label: 'Jenis Bibit' },
    { key: 'quantity', label: 'Jumlah', align: 'right' as const },
    { key: 'stockDate', label: 'Tanggal Tebar' },
  ];
</script>

<MainLayout currentPath="/seeds" pageTitle="Manajemen Bibit">
  <div class="page">
    {#if successMessage}
      <Alert variant="success" dismissible on:dismiss={() => successMessage = ''}>
        {successMessage}
      </Alert>
    {/if}

    <div class="page-header">
      <div>
        <h2 class="page-title">Daftar Bibit</h2>
        <p class="page-subtitle">Kelola stok bibit lele di setiap kolam</p>
      </div>
      <Button on:click={openCreateModal}>
        <Plus size={18} />
        Tambah Bibit
      </Button>
    </div>

    <Card padding="none">
      <Table {columns} data={seeds} {loading} {renderCell}>
        {#snippet actions({ item })}
          <div class="action-buttons">
            <Button variant="ghost" size="sm" on:click={() => openEditModal(item as Seed)}>
              <Edit2 size={16} />
            </Button>
            <Button variant="ghost" size="sm" on:click={() => deleteConfirm = item as Seed}>
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
          on:change={(e) => fetchSeeds(e.detail)}
        />
      </div>
    </Card>
  </div>

  <Modal open={showModal} title={editingSeed ? 'Edit Bibit' : 'Tambah Bibit'} on:close={closeModal}>
    <form class="form" on:submit|preventDefault={handleSubmit}>
      <Select
        label="Kolam"
        options={$pondsForSelect}
        bind:value={formData.pondsId}
        required
      />
      <Select
        label="Jenis Bibit"
        options={seedTypes}
        bind:value={formData.type}
        required
      />
      <Input
        type="number"
        label="Jumlah (ekor)"
        placeholder="Contoh: 5000"
        bind:value={formData.quantity}
        required
      />
      <Input
        type="date"
        label="Tanggal Tebar"
        bind:value={formData.stockDate}
        required
      />
    </form>
    <div slot="footer">
      <Button variant="secondary" on:click={closeModal}>Batal</Button>
      <Button on:click={handleSubmit} {loading}>
        {editingSeed ? 'Simpan' : 'Tambah'}
      </Button>
    </div>
  </Modal>

  <Modal open={!!deleteConfirm} title="Konfirmasi Hapus" size="sm" on:close={() => deleteConfirm = null}>
    <p>Apakah Anda yakin ingin menghapus data bibit ini?</p>
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

  @media (max-width: 640px) {
    .page {
      gap: 1rem;
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
</style>
