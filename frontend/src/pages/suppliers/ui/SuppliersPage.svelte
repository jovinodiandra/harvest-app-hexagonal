<script lang="ts">
  import { onMount } from 'svelte';
  import { Plus, Edit2, Trash2, Building2, Phone, Mail } from 'lucide-svelte';
  import { MainLayout } from '../../../widgets';
  import { Button, Card, Modal, Input, Textarea, Table, Pagination, Alert, StatCard } from '../../../shared/ui';
  import { api } from '../../../shared/api';
  import type { Supplier } from '../../../shared/types';

  let suppliers: Supplier[] = [];
  let loading = false;
  let pagination = { page: 1, limit: 10, total: 0, totalPages: 0 };
  
  let showModal = false;
  let editingSupplier: Supplier | null = null;
  let deleteConfirm: Supplier | null = null;
  let successMessage = '';
  let errorMessage = '';
  
  let formData = {
    name: '',
    address: '',
    phone: '',
    email: '',
    notes: '',
    organizationId: 'org-001',
  };

  onMount(() => fetchSuppliers());

  async function fetchSuppliers(page = 1) {
    loading = true;
    const response = await api.getSuppliers(page, 10);
    if (response.success && response.data) {
      suppliers = response.data.data;
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
    editingSupplier = null;
    formData = {
      name: '',
      address: '',
      phone: '',
      email: '',
      notes: '',
      organizationId: 'org-001',
    };
    showModal = true;
  }

  function openEditModal(supplier: Supplier) {
    editingSupplier = supplier;
    formData = { ...supplier };
    showModal = true;
  }

  function closeModal() {
    showModal = false;
    editingSupplier = null;
  }

  async function handleSubmit() {
    loading = true;
    errorMessage = '';
    
    try {
      if (editingSupplier) {
        const result = await api.updateSupplier(editingSupplier.id, formData);
        if (result.success) {
          successMessage = 'Supplier berhasil diperbarui';
          await fetchSuppliers(pagination.page);
          closeModal();
        } else {
          errorMessage = result.error || 'Gagal memperbarui supplier';
        }
      } else {
        const result = await api.createSupplier(formData);
        if (result.success) {
          successMessage = 'Supplier berhasil ditambahkan';
          await fetchSuppliers(pagination.page);
          closeModal();
        } else {
          errorMessage = result.error || 'Gagal menambahkan supplier';
        }
      }
    } catch (err) {
      errorMessage = 'Terjadi kesalahan. Silakan coba lagi.';
    }
    
    loading = false;
    if (successMessage) setTimeout(() => successMessage = '', 3000);
    if (errorMessage) setTimeout(() => errorMessage = '', 5000);
  }

  async function handleDelete() {
    if (deleteConfirm) {
      const result = await api.deleteSupplier(deleteConfirm.id);
      if (result.success) {
        successMessage = 'Supplier berhasil dihapus';
        await fetchSuppliers(pagination.page);
        deleteConfirm = null;
      }
      setTimeout(() => successMessage = '', 3000);
    }
  }

  function renderCell(item: unknown, key: string, value: unknown): string {
    return value != null ? String(value) : '-';
  }

  const columns = [
    { key: 'name', label: 'Nama Supplier' },
    { key: 'phone', label: 'Telepon' },
    { key: 'email', label: 'Email' },
    { key: 'address', label: 'Alamat' },
  ];
</script>

<MainLayout currentPath="/suppliers" pageTitle="Supplier">
  <div class="page">
    {#if successMessage}
      <Alert variant="success" dismissible on:dismiss={() => successMessage = ''}>
        {successMessage}
      </Alert>
    {/if}

    <div class="stats-grid">
      <StatCard 
        title="Total Supplier" 
        value="{suppliers.length}"
        subtitle="supplier terdaftar"
        variant="primary"
        icon={Building2}
      />
    </div>

    <div class="page-header">
      <div>
        <h2 class="page-title">Daftar Supplier</h2>
        <p class="page-subtitle">Kelola data supplier bibit, pakan, dan kebutuhan lainnya</p>
      </div>
      <Button on:click={openCreateModal}>
        <Plus size={18} />
        Tambah Supplier
      </Button>
    </div>

    <Card padding="none">
      <Table {columns} data={suppliers} {loading} {renderCell}>
        {#snippet actions({ item })}
          <div class="action-buttons">
            <Button variant="ghost" size="sm" on:click={() => openEditModal(item as Supplier)}>
              <Edit2 size={16} />
            </Button>
            <Button variant="ghost" size="sm" on:click={() => deleteConfirm = item as Supplier}>
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
          on:change={(e) => fetchSuppliers(e.detail)}
        />
      </div>
    </Card>
  </div>

  <Modal open={showModal} title={editingSupplier ? 'Edit Supplier' : 'Tambah Supplier'} size="lg" on:close={closeModal}>
    {#if errorMessage}
      <Alert variant="error" dismissible on:dismiss={() => errorMessage = ''}>
        {errorMessage}
      </Alert>
    {/if}
    <form class="form" on:submit|preventDefault={handleSubmit}>
      <Input
        label="Nama Supplier"
        placeholder="Contoh: CV Maju Jaya"
        bind:value={formData.name}
        required
      />
      <div class="form-row">
        <Input
          type="tel"
          label="Nomor Telepon"
          placeholder="Contoh: 081234567890"
          bind:value={formData.phone}
          required
        />
        <Input
          type="email"
          label="Email"
          placeholder="Contoh: supplier@email.com"
          bind:value={formData.email}
        />
      </div>
      <Textarea
        label="Alamat"
        placeholder="Alamat lengkap supplier..."
        bind:value={formData.address}
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
        {editingSupplier ? 'Simpan' : 'Tambah'}
      </Button>
    </div>
  </Modal>

  <Modal open={!!deleteConfirm} title="Konfirmasi Hapus" size="sm" on:close={() => deleteConfirm = null}>
    <p>Apakah Anda yakin ingin menghapus supplier <strong>{deleteConfirm?.name}</strong>?</p>
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
