<script lang="ts">
  import { onMount } from 'svelte';
  import { Plus, Trash2, Edit2, Bell, Clock, Calendar, Utensils, Truck } from 'lucide-svelte';
  import { MainLayout } from '../../../widgets';
  import { Button, Card, Modal, Input, Select, Textarea, Table, Pagination, Alert, StatCard, Badge } from '../../../shared/ui';
  import { api } from '../../../shared/api';
  import { pondStore, pondsForSelect } from '../../../entities/pond';
  import type { FeedReminder, HarvestReminder } from '../../../shared/types';
  import { formatShortDate } from '../../../shared/lib/utils';

  let feedReminders: FeedReminder[] = [];
  let harvestReminders: HarvestReminder[] = [];
  let loading = false;
  let activeTab: 'feed' | 'harvest' = 'feed';
  
  let showModal = false;
  let reminderType: 'feed' | 'harvest' = 'feed';
  let editingFeedReminder: FeedReminder | null = null;
  let editingHarvestReminder: HarvestReminder | null = null;
  let deleteConfirm: { type: 'feed' | 'harvest', item: FeedReminder | HarvestReminder } | null = null;
  let successMessage = '';
  let errorMessage = '';
  
  let feedFormData = {
    pondsId: '',
    reminderDate: new Date().toISOString().split('T')[0],
    reminderTime: '07:00',
    feedType: '',
    notes: '',
    organizationId: 'org-001',
  };

  let harvestFormData = {
    pondId: '',
    reminderDate: new Date().toISOString().split('T')[0],
    reminderTime: '06:00',
    notes: '',
    organizationId: 'org-001',
  };

  const feedTypes = [
    { value: 'PF-500', label: 'PF-500 ' },
    { value: 'PF-1000', label: 'PF-1000 ' },
    { value: 'HI-PRO-VITE 781-1', label: 'HI-PRO-VITE 781-1' },
    { value: 'HI-PRO-VITE 781', label: 'HI-PRO-VITE 781' },
  ];

  onMount(async () => {
    await pondStore.fetchAllPonds();
    await fetchReminders();
  });

  async function fetchReminders() {
    loading = true;
    const [feedRes, harvestRes] = await Promise.all([
      api.getFeedReminders(),
      api.getHarvestReminders(),
    ]);
    
    if (feedRes.success && feedRes.data) {
      feedReminders = feedRes.data.data;
    }
    if (harvestRes.success && harvestRes.data) {
      harvestReminders = harvestRes.data.data;
    }
    loading = false;
  }

  function getPondName(pondsId: string): string {
    const pond = $pondStore.ponds.find(p => p.id === pondsId);
    return pond?.name || '-';
  }

  function openCreateModal(type: 'feed' | 'harvest') {
    reminderType = type;
    editingFeedReminder = null;
    editingHarvestReminder = null;
    if (type === 'feed') {
      feedFormData = {
        pondsId: '',
        reminderDate: new Date().toISOString().split('T')[0],
        reminderTime: '07:00',
        feedType: '',
        notes: '',
        organizationId: 'org-001',
      };
    } else {
      harvestFormData = {
        pondId: '',
        reminderDate: new Date().toISOString().split('T')[0],
        reminderTime: '06:00',
        notes: '',
        organizationId: 'org-001',
      };
    }
    showModal = true;
  }

  function openEditFeedModal(reminder: FeedReminder) {
    reminderType = 'feed';
    editingFeedReminder = reminder;
    editingHarvestReminder = null;
    feedFormData = { ...reminder };
    showModal = true;
  }

  function openEditHarvestModal(reminder: HarvestReminder) {
    reminderType = 'harvest';
    editingHarvestReminder = reminder;
    editingFeedReminder = null;
    harvestFormData = { ...reminder };
    showModal = true;
  }

  function closeModal() {
    showModal = false;
    editingFeedReminder = null;
    editingHarvestReminder = null;
    errorMessage = '';
  }

  async function handleSubmit() {
    loading = true;
    errorMessage = '';
    
    try {
      if (reminderType === 'feed') {
        if (editingFeedReminder) {
          const result = await api.updateFeedReminder(editingFeedReminder.id, feedFormData);
          if (result.success) {
            successMessage = 'Pengingat pakan berhasil diperbarui';
            await fetchReminders();
            closeModal();
          } else {
            errorMessage = result.error || 'Gagal memperbarui pengingat';
          }
        } else {
          const result = await api.createFeedReminder(feedFormData);
          if (result.success) {
            successMessage = 'Pengingat pakan berhasil ditambahkan';
            await fetchReminders();
            closeModal();
          } else {
            errorMessage = result.error || 'Gagal menambahkan pengingat';
          }
        }
      } else {
        if (editingHarvestReminder) {
          const result = await api.updateHarvestReminder(editingHarvestReminder.id, harvestFormData);
          if (result.success) {
            successMessage = 'Pengingat panen berhasil diperbarui';
            await fetchReminders();
            closeModal();
          } else {
            errorMessage = result.error || 'Gagal memperbarui pengingat';
          }
        } else {
          const result = await api.createHarvestReminder(harvestFormData);
          if (result.success) {
            successMessage = 'Pengingat panen berhasil ditambahkan';
            await fetchReminders();
            closeModal();
          } else {
            errorMessage = result.error || 'Gagal menambahkan pengingat';
          }
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
      let result;
      if (deleteConfirm.type === 'feed') {
        result = await api.deleteFeedReminder(deleteConfirm.item.id);
      } else {
        result = await api.deleteHarvestReminder(deleteConfirm.item.id);
      }
      
      if (result.success) {
        successMessage = 'Pengingat berhasil dihapus';
        await fetchReminders();
        deleteConfirm = null;
      }
      setTimeout(() => successMessage = '', 3000);
    }
  }

  function renderFeedCell(item: unknown, key: string, value: unknown): string {
    const reminder = item as FeedReminder;
    if (key === 'reminderDate') {
      return formatShortDate(reminder.reminderDate);
    }
    if (key === 'pondsId') {
      return getPondName(reminder.pondsId);
    }
    return value != null ? String(value) : '-';
  }

  function renderHarvestCell(item: unknown, key: string, value: unknown): string {
    const reminder = item as HarvestReminder;
    if (key === 'reminderDate') {
      return formatShortDate(reminder.reminderDate);
    }
    if (key === 'pondId') {
      return getPondName(reminder.pondId);
    }
    return value != null ? String(value) : '-';
  }

  const feedColumns = [
    { key: 'reminderDate', label: 'Tanggal' },
    { key: 'reminderTime', label: 'Waktu', align: 'center' as const },
    { key: 'pondsId', label: 'Kolam' },
    { key: 'feedType', label: 'Jenis Pakan' },
    { key: 'notes', label: 'Catatan' },
  ];

  const harvestColumns = [
    { key: 'reminderDate', label: 'Tanggal' },
    { key: 'reminderTime', label: 'Waktu', align: 'center' as const },
    { key: 'pondId', label: 'Kolam' },
    { key: 'notes', label: 'Catatan' },
  ];
</script>

<MainLayout currentPath="/reminders" pageTitle="Pengingat">
  <div class="page">
    {#if successMessage}
      <Alert variant="success" dismissible on:dismiss={() => successMessage = ''}>
        {successMessage}
      </Alert>
    {/if}

    <div class="stats-grid">
      <StatCard 
        title="Pengingat Pakan" 
        value="{feedReminders.length}"
        subtitle="pengingat aktif"
        variant="primary"
        icon={Utensils}
      />
      <StatCard 
        title="Pengingat Panen" 
        value="{harvestReminders.length}"
        subtitle="pengingat aktif"
        variant="success"
        icon={Truck}
      />
      <StatCard 
        title="Total Pengingat" 
        value="{feedReminders.length + harvestReminders.length}"
        variant="info"
        icon={Bell}
      />
    </div>

    <div class="page-header">
      <div>
        <h2 class="page-title">Pengingat</h2>
        <p class="page-subtitle">Atur pengingat untuk pemberian pakan dan panen</p>
      </div>
    </div>

    <div class="tabs">
      <button 
        class="tab" 
        class:active={activeTab === 'feed'}
        on:click={() => activeTab = 'feed'}
      >
        <Utensils size={18} />
        Pengingat Pakan
        <Badge variant="primary" size="sm">{feedReminders.length}</Badge>
      </button>
      <button 
        class="tab"
        class:active={activeTab === 'harvest'}
        on:click={() => activeTab = 'harvest'}
      >
        <Truck size={18} />
        Pengingat Panen
        <Badge variant="success" size="sm">{harvestReminders.length}</Badge>
      </button>
    </div>

    {#if activeTab === 'feed'}
      <Card padding="none">
        <div class="card-header-actions">
          <Button size="sm" on:click={() => openCreateModal('feed')}>
            <Plus size={16} />
            Tambah Pengingat Pakan
          </Button>
        </div>
        <Table columns={feedColumns} data={feedReminders} {loading} renderCell={renderFeedCell}>
          {#snippet actions({ item })}
            <div class="action-buttons">
              <Button variant="ghost" size="sm" on:click={() => openEditFeedModal(item as FeedReminder)}>
                <Edit2 size={16} />
              </Button>
              <Button variant="ghost" size="sm" on:click={() => deleteConfirm = { type: 'feed', item: item as FeedReminder }}>
                <Trash2 size={16} />
              </Button>
            </div>
          {/snippet}
        </Table>
      </Card>
    {:else}
      <Card padding="none">
        <div class="card-header-actions">
          <Button size="sm" on:click={() => openCreateModal('harvest')}>
            <Plus size={16} />
            Tambah Pengingat Panen
          </Button>
        </div>
        <Table columns={harvestColumns} data={harvestReminders} {loading} renderCell={renderHarvestCell}>
          {#snippet actions({ item })}
            <div class="action-buttons">
              <Button variant="ghost" size="sm" on:click={() => openEditHarvestModal(item as HarvestReminder)}>
                <Edit2 size={16} />
              </Button>
              <Button variant="ghost" size="sm" on:click={() => deleteConfirm = { type: 'harvest', item: item as HarvestReminder }}>
                <Trash2 size={16} />
              </Button>
            </div>
          {/snippet}
        </Table>
      </Card>
    {/if}
  </div>

  <Modal 
    open={showModal} 
    title={reminderType === 'feed' 
      ? (editingFeedReminder ? 'Edit Pengingat Pakan' : 'Tambah Pengingat Pakan') 
      : (editingHarvestReminder ? 'Edit Pengingat Panen' : 'Tambah Pengingat Panen')} 
    on:close={closeModal}
  >
    {#if errorMessage}
      <Alert variant="error" dismissible on:dismiss={() => errorMessage = ''}>
        {errorMessage}
      </Alert>
    {/if}
    <form class="form" on:submit|preventDefault={handleSubmit}>
      {#if reminderType === 'feed'}
        <Select
          label="Kolam"
          options={$pondsForSelect}
          bind:value={feedFormData.pondsId}
          required
        />
        <div class="form-row">
          <Input
            type="date"
            label="Tanggal"
            bind:value={feedFormData.reminderDate}
            required
          />
          <Input
            type="time"
            label="Waktu"
            bind:value={feedFormData.reminderTime}
            required
          />
        </div>
        <Select
          label="Jenis Pakan"
          options={feedTypes}
          bind:value={feedFormData.feedType}
          required
        />
        <Textarea
          label="Catatan"
          placeholder="Catatan tambahan..."
          bind:value={feedFormData.notes}
        />
      {:else}
        <Select
          label="Kolam"
          options={$pondsForSelect}
          bind:value={harvestFormData.pondId}
          required
        />
        <div class="form-row">
          <Input
            type="date"
            label="Tanggal"
            bind:value={harvestFormData.reminderDate}
            required
          />
          <Input
            type="time"
            label="Waktu"
            bind:value={harvestFormData.reminderTime}
            required
          />
        </div>
        <Textarea
          label="Catatan"
          placeholder="Catatan persiapan panen..."
          bind:value={harvestFormData.notes}
        />
      {/if}
    </form>
    <div slot="footer">
      <Button variant="secondary" on:click={closeModal}>Batal</Button>
      <Button on:click={handleSubmit} {loading}>
        {(reminderType === 'feed' ? editingFeedReminder : editingHarvestReminder) ? 'Simpan' : 'Tambah'}
      </Button>
    </div>
  </Modal>

  <Modal open={!!deleteConfirm} title="Konfirmasi Hapus" size="sm" on:close={() => deleteConfirm = null}>
    <p>Apakah Anda yakin ingin menghapus pengingat ini?</p>
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
    grid-template-columns: repeat(auto-fit, minmax(200px, 1fr));
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

  .tabs {
    display: flex;
    gap: 0.5rem;
    border-bottom: 1px solid var(--color-border);
    padding-bottom: 0;
  }

  .tab {
    display: flex;
    align-items: center;
    gap: 0.5rem;
    padding: 0.75rem 1rem;
    background: none;
    border: none;
    border-bottom: 2px solid transparent;
    color: var(--color-text-muted);
    font-weight: 500;
    cursor: pointer;
    transition: all 0.2s;
    margin-bottom: -1px;
  }

  .tab:hover {
    color: var(--color-text);
  }

  .tab.active {
    color: var(--color-primary);
    border-bottom-color: var(--color-primary);
  }

  .card-header-actions {
    display: flex;
    justify-content: flex-end;
    padding: 1rem;
    border-bottom: 1px solid var(--color-border);
  }

  .action-buttons {
    display: flex;
    gap: 0.25rem;
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

    .tabs {
      flex-direction: column;
    }

    .tab {
      border-bottom: none;
      border-left: 2px solid transparent;
    }

    .tab.active {
      border-left-color: var(--color-primary);
    }
  }
</style>
