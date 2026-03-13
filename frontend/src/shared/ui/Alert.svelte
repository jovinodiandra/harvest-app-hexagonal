<script lang="ts">
  import { createEventDispatcher } from 'svelte';

  type AlertVariant = 'info' | 'success' | 'warning' | 'error';

  export let variant: AlertVariant = 'info';
  export let title: string = '';
  export let dismissible: boolean = false;

  const dispatch = createEventDispatcher();

  const icons = {
    info: 'M13 16h-1v-4h-1m1-4h.01M21 12a9 9 0 11-18 0 9 9 0 0118 0z',
    success: 'M9 12l2 2 4-4m6 2a9 9 0 11-18 0 9 9 0 0118 0z',
    warning: 'M12 9v2m0 4h.01m-6.938 4h13.856c1.54 0 2.502-1.667 1.732-3L13.732 4c-.77-1.333-2.694-1.333-3.464 0L3.34 16c-.77 1.333.192 3 1.732 3z',
    error: 'M10 14l2-2m0 0l2-2m-2 2l-2-2m2 2l2 2m7-2a9 9 0 11-18 0 9 9 0 0118 0z',
  };
</script>

<div class="alert alert-{variant}" role="alert">
  <svg class="alert-icon" width="20" height="20" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2">
    <path d={icons[variant]} stroke-linecap="round" stroke-linejoin="round"/>
  </svg>
  <div class="alert-content">
    {#if title}
      <strong class="alert-title">{title}</strong>
    {/if}
    <p class="alert-message"><slot /></p>
  </div>
  {#if dismissible}
    <button class="alert-dismiss" on:click={() => dispatch('dismiss')} aria-label="Dismiss alert">
      <svg width="16" height="16" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2">
        <path d="M18 6L6 18M6 6l12 12"/>
      </svg>
    </button>
  {/if}
</div>

<style>
  .alert {
    display: flex;
    align-items: flex-start;
    gap: 0.75rem;
    padding: 1rem;
    border-radius: 0.5rem;
    border: 1px solid;
  }

  .alert-info {
    background: #eff6ff;
    border-color: #bfdbfe;
    color: #1e40af;
  }

  .alert-success {
    background: #f0fdf4;
    border-color: #bbf7d0;
    color: #166534;
  }

  .alert-warning {
    background: #fffbeb;
    border-color: #fde68a;
    color: #92400e;
  }

  .alert-error {
    background: #fef2f2;
    border-color: #fecaca;
    color: #991b1b;
  }

  .alert-icon {
    flex-shrink: 0;
    margin-top: 0.125rem;
  }

  .alert-content {
    flex: 1;
  }

  .alert-title {
    display: block;
    font-weight: 600;
    margin-bottom: 0.25rem;
  }

  .alert-message {
    margin: 0;
    font-size: 0.875rem;
  }

  .alert-dismiss {
    background: none;
    border: none;
    color: inherit;
    cursor: pointer;
    opacity: 0.7;
    padding: 0.25rem;
    flex-shrink: 0;
  }

  .alert-dismiss:hover {
    opacity: 1;
  }
</style>
