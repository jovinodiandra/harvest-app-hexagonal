<script lang="ts">
  import type { Snippet } from 'svelte';

  type Column = {
    key: string;
    label: string;
    width?: string;
    align?: 'left' | 'center' | 'right';
  };

  interface Props {
    columns?: Column[];
    data?: unknown[];
    loading?: boolean;
    emptyMessage?: string;
    renderCell?: (item: unknown, key: string, value: unknown) => string;
    actions?: Snippet<[{ item: unknown; index: number }]>;
  }

  let { 
    columns = [], 
    data = [], 
    loading = false, 
    emptyMessage = 'Tidak ada data',
    renderCell,
    actions,
  }: Props = $props();

  function getValue(item: unknown, key: string): unknown {
    const keys = key.split('.');
    let value: unknown = item;
    for (const k of keys) {
      if (value && typeof value === 'object') {
        value = (value as Record<string, unknown>)[k];
      }
    }
    return value;
  }

  function formatValue(item: unknown, key: string): string {
    const value = getValue(item, key);
    if (renderCell) {
      return renderCell(item, key, value);
    }
    return value != null ? String(value) : '-';
  }
</script>

<div class="table-wrapper">
  <table class="table">
    <thead>
      <tr>
        {#each columns as column}
          <th 
            style={column.width ? `width: ${column.width}` : ''}
            class="align-{column.align || 'left'}"
          >
            {column.label}
          </th>
        {/each}
        {#if actions}
          <th class="actions-column">Aksi</th>
        {/if}
      </tr>
    </thead>
    <tbody>
      {#if loading}
        <tr>
          <td colspan={columns.length + (actions ? 1 : 0)} class="loading-cell">
            <div class="loading-content">
              <div class="spinner"></div>
              <span>Memuat data...</span>
            </div>
          </td>
        </tr>
      {:else if data.length === 0}
        <tr>
          <td colspan={columns.length + (actions ? 1 : 0)} class="empty-cell">
            {emptyMessage}
          </td>
        </tr>
      {:else}
        {#each data as item, index}
          <tr>
            {#each columns as column}
              <td class="align-{column.align || 'left'}">
                {formatValue(item, column.key)}
              </td>
            {/each}
            {#if actions}
              <td class="actions-cell">
                {@render actions({ item, index })}
              </td>
            {/if}
          </tr>
        {/each}
      {/if}
    </tbody>
  </table>
</div>

<style>
  .table-wrapper {
    overflow-x: auto;
    border: 1px solid var(--color-border);
    border-radius: 0.5rem;
  }

  .table {
    width: 100%;
    border-collapse: collapse;
    font-size: 0.875rem;
  }

  th, td {
    padding: 0.75rem 1rem;
    text-align: left;
    border-bottom: 1px solid var(--color-border);
  }

  th {
    background: var(--color-secondary);
    font-weight: 600;
    color: var(--color-text);
    white-space: nowrap;
  }

  tbody tr:hover {
    background: var(--color-secondary);
  }

  tbody tr:last-child td {
    border-bottom: none;
  }

  .align-left { text-align: left; }
  .align-center { text-align: center; }
  .align-right { text-align: right; }

  .actions-column {
    width: 100px;
    text-align: center;
  }

  .actions-cell {
    text-align: center;
    white-space: nowrap;
  }

  .loading-cell,
  .empty-cell {
    text-align: center;
    padding: 2rem;
    color: var(--color-text-muted);
  }

  .loading-content {
    display: flex;
    align-items: center;
    justify-content: center;
    gap: 0.5rem;
  }

  .spinner {
    width: 1.25rem;
    height: 1.25rem;
    border: 2px solid var(--color-border);
    border-top-color: var(--color-primary);
    border-radius: 50%;
    animation: spin 0.6s linear infinite;
  }

  @keyframes spin {
    to { transform: rotate(360deg); }
  }
</style>
