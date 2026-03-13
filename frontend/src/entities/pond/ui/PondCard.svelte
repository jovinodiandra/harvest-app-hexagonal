<script lang="ts">
  import { MapPin, Waves, Edit2, Trash2 } from 'lucide-svelte';
  import type { Pond } from '../../../shared/types';
  import { Card, Badge, Button } from '../../../shared/ui';

  export let pond: Pond;
  export let onEdit: (() => void) | undefined = undefined;
  export let onDelete: (() => void) | undefined = undefined;
</script>

<Card padding="md" hoverable>
  <div class="pond-card">
    <div class="pond-header">
      <h3 class="pond-name">{pond.name}</h3>
      <Badge variant="primary">{pond.capacity.toLocaleString()} ekor</Badge>
    </div>
    
    <div class="pond-info">
      <div class="info-item">
        <MapPin size={16} />
        <span>{pond.location}</span>
      </div>
    </div>
    
    {#if onEdit || onDelete}
      <div class="pond-actions">
        {#if onEdit}
          <Button variant="ghost" size="sm" on:click={onEdit}>
            <Edit2 size={16} />
            Edit
          </Button>
        {/if}
        {#if onDelete}
          <Button variant="ghost" size="sm" on:click={onDelete}>
            <Trash2 size={16} />
            Hapus
          </Button>
        {/if}
      </div>
    {/if}
  </div>
</Card>

<style>
  .pond-card {
    display: flex;
    flex-direction: column;
    gap: 0.75rem;
  }

  .pond-header {
    display: flex;
    align-items: center;
    justify-content: space-between;
    gap: 0.5rem;
  }

  .pond-name {
    margin: 0;
    font-size: 1.125rem;
    font-weight: 600;
    color: var(--color-text);
  }

  .pond-info {
    display: flex;
    flex-direction: column;
    gap: 0.375rem;
  }

  .info-item {
    display: flex;
    align-items: center;
    gap: 0.5rem;
    font-size: 0.875rem;
    color: var(--color-text-muted);
  }

  .pond-actions {
    display: flex;
    gap: 0.5rem;
    padding-top: 0.75rem;
    border-top: 1px solid var(--color-border);
    margin-top: 0.25rem;
  }
</style>
