<script lang="ts">
  type ButtonVariant = 'primary' | 'secondary' | 'danger' | 'ghost';
  type ButtonSize = 'sm' | 'md' | 'lg';

  export let variant: ButtonVariant = 'primary';
  export let size: ButtonSize = 'md';
  export let disabled: boolean = false;
  export let loading: boolean = false;
  export let type: 'button' | 'submit' | 'reset' = 'button';
  export let fullWidth: boolean = false;
</script>

<button
  {type}
  {disabled}
  class="btn btn-{variant} btn-{size}"
  class:btn-loading={loading}
  class:btn-full={fullWidth}
  on:click
  {...$$restProps}
>
  {#if loading}
    <span class="spinner"></span>
  {/if}
  <slot />
</button>

<style>
  .btn {
    display: inline-flex;
    align-items: center;
    justify-content: center;
    gap: 0.5rem;
    font-weight: 500;
    border-radius: 0.5rem;
    border: none;
    cursor: pointer;
    transition: all 0.2s ease;
    font-family: inherit;
  }

  .btn:disabled {
    opacity: 0.6;
    cursor: not-allowed;
  }

  .btn-sm { padding: 0.375rem 0.75rem; font-size: 0.875rem; }
  .btn-md { padding: 0.5rem 1rem; font-size: 1rem; }
  .btn-lg { padding: 0.75rem 1.5rem; font-size: 1.125rem; }

  .btn-primary {
    background: var(--color-primary);
    color: white;
  }
  .btn-primary:hover:not(:disabled) {
    background: var(--color-primary-dark);
  }

  .btn-secondary {
    background: var(--color-secondary);
    color: var(--color-text);
  }
  .btn-secondary:hover:not(:disabled) {
    background: var(--color-secondary-dark);
  }

  .btn-danger {
    background: var(--color-danger);
    color: white;
  }
  .btn-danger:hover:not(:disabled) {
    background: var(--color-danger-dark);
  }

  .btn-ghost {
    background: transparent;
    color: var(--color-text);
  }
  .btn-ghost:hover:not(:disabled) {
    background: var(--color-secondary);
  }

  .btn-full { width: 100%; }

  .btn-loading { pointer-events: none; }

  .spinner {
    width: 1em;
    height: 1em;
    border: 2px solid transparent;
    border-top-color: currentColor;
    border-radius: 50%;
    animation: spin 0.6s linear infinite;
  }

  @keyframes spin {
    to { transform: rotate(360deg); }
  }
</style>
