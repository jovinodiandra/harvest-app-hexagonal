<script lang="ts">
  import type { ComponentType } from 'svelte';

  type StatVariant = 'default' | 'primary' | 'success' | 'warning' | 'danger' | 'info';

  export let title: string;
  export let value: string | number;
  export let subtitle: string = '';
  export let variant: StatVariant = 'default';
  export let icon: ComponentType | null = null;
</script>

<div class="stat-card stat-{variant}">
  <div class="stat-content">
    <p class="stat-title">{title}</p>
    <p class="stat-value">{value}</p>
    {#if subtitle}
      <p class="stat-subtitle">{subtitle}</p>
    {/if}
  </div>
  {#if icon}
    <div class="stat-icon">
      <svelte:component this={icon} size={28} />
    </div>
  {/if}
</div>

<style>
  .stat-card {
    display: flex;
    align-items: flex-start;
    justify-content: space-between;
    padding: 1.25rem;
    background: var(--color-card);
    border-radius: 0.75rem;
    border: 1px solid var(--color-border);
    transition: transform 0.2s, box-shadow 0.2s;
  }

  .stat-card:hover {
    transform: translateY(-2px);
    box-shadow: 0 4px 12px rgba(0, 0, 0, 0.1);
  }

  .stat-content {
    flex: 1;
  }

  .stat-title {
    margin: 0;
    font-size: 0.875rem;
    font-weight: 500;
    color: var(--color-text-muted);
  }

  .stat-value {
    margin: 0.5rem 0 0;
    font-size: 1.75rem;
    font-weight: 700;
    color: var(--color-text);
    line-height: 1.2;
  }

  .stat-subtitle {
    margin: 0.25rem 0 0;
    font-size: 0.75rem;
    color: var(--color-text-muted);
  }

  .stat-icon {
    display: flex;
    align-items: center;
    justify-content: center;
    width: 48px;
    height: 48px;
    border-radius: 0.75rem;
    flex-shrink: 0;
  }

  .stat-default .stat-icon { background: var(--color-secondary); color: var(--color-text-muted); }
  .stat-primary .stat-icon { background: var(--color-primary-light); color: var(--color-primary); }
  .stat-success .stat-icon { background: #dcfce7; color: #16a34a; }
  .stat-warning .stat-icon { background: #fef3c7; color: #d97706; }
  .stat-danger .stat-icon { background: #fee2e2; color: #dc2626; }
  .stat-info .stat-icon { background: #dbeafe; color: #2563eb; }
</style>
