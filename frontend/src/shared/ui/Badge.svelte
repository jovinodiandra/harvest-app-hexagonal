<script lang="ts">
  type BadgeVariant = 'default' | 'primary' | 'success' | 'warning' | 'danger' | 'info';
  type BadgeSize = 'sm' | 'md';

  export let variant: BadgeVariant = 'default';
  export let size: BadgeSize = 'md';
</script>

<span class="badge badge-{variant} badge-{size}">
  <slot />
</span>

<style>
  .badge {
    display: inline-flex;
    align-items: center;
    font-weight: 500;
    border-radius: 9999px;
  }

  .badge-sm { padding: 0.125rem 0.5rem; font-size: 0.75rem; }
  .badge-md { padding: 0.25rem 0.75rem; font-size: 0.875rem; }

  .badge-default {
    background: var(--color-secondary);
    color: var(--color-text-muted);
  }

  .badge-primary {
    background: var(--color-primary-light);
    color: var(--color-primary-dark);
  }

  .badge-success {
    background: #dcfce7;
    color: #166534;
  }

  .badge-warning {
    background: #fef3c7;
    color: #92400e;
  }

  .badge-danger {
    background: #fee2e2;
    color: #991b1b;
  }

  .badge-info {
    background: #dbeafe;
    color: #1e40af;
  }
</style>
