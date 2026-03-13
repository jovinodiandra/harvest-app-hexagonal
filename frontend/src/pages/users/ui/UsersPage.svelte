<script lang="ts">
  import { onMount } from 'svelte';
  import { Plus, Edit2, Trash2, Users, Shield, User } from 'lucide-svelte';
  import { MainLayout } from '../../../widgets';
  import { Button, Card, Modal, Input, Select, Table, Pagination, Alert, StatCard, Badge } from '../../../shared/ui';
  import { api } from '../../../shared/api';
  import { isOwner, isAdmin } from '../../../entities/auth';
  import type { User as UserType } from '../../../shared/types';

  let users: UserType[] = [];
  let loading = false;
  let pagination = { page: 1, limit: 10, total: 0, totalPages: 0 };
  
  let showModal = false;
  let editingUser: UserType | null = null;
  let deleteConfirm: UserType | null = null;
  let successMessage = '';
  let errorMessage = '';
  
  let formData = {
    name: '',
    email: '',
    password: '',
    role: 'user' as 'owner' | 'admin' | 'user',
    organizationId: 'org-001',
  };

  const roleOptions = [
    { value: 'user', label: 'User' },
    { value: 'admin', label: 'Admin' },
  ];

  $: ownerCount = users.filter(u => u.role === 'owner').length;
  $: adminCount = users.filter(u => u.role === 'admin').length;
  $: userCount = users.filter(u => u.role === 'user').length;

  onMount(() => fetchUsers());

  async function fetchUsers(page = 1) {
    loading = true;
    const response = await api.getUsers(page, 10);
    if (response.success && response.data) {
      users = response.data.data;
      pagination = {
        page: response.data.page,
        limit: response.data.limit,
        total: response.data.total,
        totalPages: response.data.totalPages,
      };
    }
    loading = false;
  }

  function getRoleBadgeVariant(role: string): 'primary' | 'success' | 'default' {
    if (role === 'owner') return 'primary';
    if (role === 'admin') return 'success';
    return 'default';
  }

  function getRoleLabel(role: string): string {
    if (role === 'owner') return 'Owner';
    if (role === 'admin') return 'Admin';
    return 'User';
  }

  function openCreateModal() {
    editingUser = null;
    formData = {
      name: '',
      email: '',
      password: '',
      role: 'user',
      organizationId: 'org-001',
    };
    showModal = true;
  }

  function openEditModal(user: UserType) {
    if (user.role === 'owner') {
      errorMessage = 'Tidak dapat mengedit akun Owner';
      setTimeout(() => errorMessage = '', 3000);
      return;
    }
    editingUser = user;
    formData = { 
      name: user.name,
      email: user.email,
      password: '',
      role: user.role,
      organizationId: user.organizationId,
    };
    showModal = true;
  }

  function closeModal() {
    showModal = false;
    editingUser = null;
  }

  async function handleSubmit() {
    loading = true;
    errorMessage = '';
    
    if (editingUser) {
      const updateData: Partial<UserType> = { 
        name: formData.name, 
        role: formData.role 
      };
      const result = await api.updateUser(editingUser.id, updateData);
      if (result.success) {
        successMessage = 'Pengguna berhasil diperbarui';
        await fetchUsers(pagination.page);
        closeModal();
      } else {
        errorMessage = result.error || 'Gagal memperbarui pengguna';
      }
    } else {
      const result = await api.createUser({ ...formData, password: formData.password });
      if (result.success) {
        successMessage = 'Pengguna berhasil ditambahkan';
        await fetchUsers(pagination.page);
        closeModal();
      } else {
        errorMessage = result.error || 'Gagal menambahkan pengguna';
      }
    }
    
    loading = false;
    if (successMessage) setTimeout(() => successMessage = '', 3000);
    if (errorMessage) setTimeout(() => errorMessage = '', 5000);
  }

  async function handleDelete() {
    if (deleteConfirm) {
      if (deleteConfirm.role === 'owner') {
        errorMessage = 'Tidak dapat menghapus akun Owner';
        deleteConfirm = null;
        setTimeout(() => errorMessage = '', 3000);
        return;
      }
      const result = await api.deleteUser(deleteConfirm.id);
      if (result.success) {
        successMessage = 'Pengguna berhasil dihapus';
        await fetchUsers(pagination.page);
        deleteConfirm = null;
      }
      setTimeout(() => successMessage = '', 3000);
    }
  }

  function renderCell(item: unknown, key: string, value: unknown): string {
    const user = item as UserType;
    if (key === 'role') {
      return getRoleLabel(user.role);
    }
    return value != null ? String(value) : '-';
  }

  const columns = [
    { key: 'name', label: 'Nama' },
    { key: 'email', label: 'Email' },
    { key: 'role', label: 'Role', align: 'center' as const },
  ];
</script>

<MainLayout currentPath="/users" pageTitle="Pengguna">
  <div class="page">
    {#if successMessage}
      <Alert variant="success" dismissible on:dismiss={() => successMessage = ''}>
        {successMessage}
      </Alert>
    {/if}

    {#if errorMessage}
      <Alert variant="error" dismissible on:dismiss={() => errorMessage = ''}>
        {errorMessage}
      </Alert>
    {/if}

    <div class="stats-grid">
      <StatCard 
        title="Owner" 
        value="{ownerCount}"
        variant="primary"
        icon={Shield}
      />
      <StatCard 
        title="Admin" 
        value="{adminCount}"
        variant="success"
        icon={Shield}
      />
      <StatCard 
        title="User" 
        value="{userCount}"
        variant="default"
        icon={User}
      />
      <StatCard 
        title="Total Pengguna" 
        value="{users.length}"
        variant="info"
        icon={Users}
      />
    </div>

    <div class="page-header">
      <div>
        <h2 class="page-title">Manajemen Pengguna</h2>
        <p class="page-subtitle">Kelola pengguna dan hak akses dalam organisasi</p>
      </div>
      <Button on:click={openCreateModal}>
        <Plus size={18} />
        Tambah Pengguna
      </Button>
    </div>

    <Card padding="none">
      <Table {columns} data={users} {loading} {renderCell}>
        {#snippet actions({ item })}
          {@const user = item as UserType}
          <div class="action-buttons">
            <Button 
              variant="ghost" 
              size="sm" 
              on:click={() => openEditModal(user)}
              disabled={user.role === 'owner'}
            >
              <Edit2 size={16} />
            </Button>
            <Button 
              variant="ghost" 
              size="sm" 
              on:click={() => deleteConfirm = user}
              disabled={user.role === 'owner'}
            >
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
          on:change={(e) => fetchUsers(e.detail)}
        />
      </div>
    </Card>
  </div>

  <Modal open={showModal} title={editingUser ? 'Edit Pengguna' : 'Tambah Pengguna'} on:close={closeModal}>
    <form class="form" on:submit|preventDefault={handleSubmit}>
      <Input
        label="Nama Lengkap"
        placeholder="Contoh: Budi Santoso"
        bind:value={formData.name}
        required
      />
      <Input
        type="email"
        label="Email"
        placeholder="Contoh: budi@email.com"
        bind:value={formData.email}
        required
        disabled={!!editingUser}
      />
      {#if !editingUser}
        <Input
          type="password"
          label="Password"
          placeholder="Minimal 8 karakter"
          bind:value={formData.password}
          required
        />
      {/if}
      <Select
        label="Role"
        options={roleOptions}
        bind:value={formData.role}
        required
      />
      <div class="role-info">
        <p><strong>User:</strong> Dapat melihat dan mengelola data operasional</p>
        <p><strong>Admin:</strong> Dapat mengelola semua data termasuk pengguna</p>
      </div>
    </form>
    <div slot="footer">
      <Button variant="secondary" on:click={closeModal}>Batal</Button>
      <Button on:click={handleSubmit} {loading}>
        {editingUser ? 'Simpan' : 'Tambah'}
      </Button>
    </div>
  </Modal>

  <Modal open={!!deleteConfirm} title="Konfirmasi Hapus" size="sm" on:close={() => deleteConfirm = null}>
    <p>Apakah Anda yakin ingin menghapus pengguna <strong>{deleteConfirm?.name}</strong>?</p>
    <p class="warning-text">Pengguna tidak akan dapat mengakses sistem lagi.</p>
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
    grid-template-columns: repeat(auto-fit, minmax(180px, 1fr));
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

  .role-info {
    padding: 0.75rem;
    background: var(--color-secondary);
    border-radius: 0.5rem;
    font-size: 0.8125rem;
    color: var(--color-text-muted);
  }

  .role-info p {
    margin: 0.25rem 0;
  }

  .role-info strong {
    color: var(--color-text);
  }

  .warning-text {
    color: var(--color-danger);
    font-size: 0.875rem;
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

    .role-info {
      font-size: 0.75rem;
      padding: 0.625rem;
    }
  }

  @media (max-width: 400px) {
    .stats-grid {
      grid-template-columns: 1fr;
    }
  }
</style>
