<script lang="ts">
  export let title: string = '';
  export let subtitle: string = '';
  export let padding: 'none' | 'sm' | 'md' | 'lg' = 'md';
  export let hoverable: boolean = false;
</script>

<div 
  class="card card-padding-{padding}"
  class:card-hoverable={hoverable}
  on:click
  {...$$restProps}
>
  {#if title || $$slots.header}
    <div class="card-header">
      {#if $$slots.header}
        <slot name="header" />
      {:else}
        <h3 class="card-title">{title}</h3>
        {#if subtitle}
          <p class="card-subtitle">{subtitle}</p>
        {/if}
      {/if}
    </div>
  {/if}
  
  <div class="card-body">
    <slot />
  </div>
  
  {#if $$slots.footer}
    <div class="card-footer">
      <slot name="footer" />
    </div>
  {/if}
</div>

<style>
  .card {
    background: var(--color-card);
    border-radius: 0.75rem;
    border: 1px solid var(--color-border);
    overflow: hidden;
    transition: transform 0.2s, box-shadow 0.2s;
  }

  .card-hoverable {
    cursor: pointer;
  }

  .card-hoverable:hover {
    transform: translateY(-2px);
    box-shadow: 0 8px 25px rgba(0, 0, 0, 0.1);
  }

  .card-padding-none .card-body { padding: 0; }
  .card-padding-sm .card-body { padding: 0.75rem; }
  .card-padding-md .card-body { padding: 1.25rem; }
  .card-padding-lg .card-body { padding: 1.75rem; }

  .card-header {
    padding: 1rem 1.25rem;
    border-bottom: 1px solid var(--color-border);
  }

  .card-title {
    margin: 0;
    font-size: 1.125rem;
    font-weight: 600;
    color: var(--color-text);
  }

  .card-subtitle {
    margin: 0.25rem 0 0;
    font-size: 0.875rem;
    color: var(--color-text-muted);
  }

  .card-footer {
    padding: 1rem 1.25rem;
    border-top: 1px solid var(--color-border);
    background: var(--color-secondary);
  }

  @media (max-width: 640px) {
    .card {
      border-radius: 0.5rem;
    }

    .card-header {
      padding: 0.875rem 1rem;
    }

    .card-title {
      font-size: 1rem;
    }

    .card-padding-sm .card-body { padding: 0.5rem; }
    .card-padding-md .card-body { padding: 1rem; }
    .card-padding-lg .card-body { padding: 1.25rem; }

    .card-footer {
      padding: 0.875rem 1rem;
    }
  }
</style>
