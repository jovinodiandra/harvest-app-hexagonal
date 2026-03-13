<script lang="ts">
  import { createEventDispatcher } from 'svelte';

  export let currentPage: number = 1;
  export let totalPages: number = 1;
  export let totalItems: number = 0;
  export let itemsPerPage: number = 10;

  const dispatch = createEventDispatcher<{ change: number }>();

  $: startItem = (currentPage - 1) * itemsPerPage + 1;
  $: endItem = Math.min(currentPage * itemsPerPage, totalItems);

  $: pages = (() => {
    const items: (number | string)[] = [];
    const delta = 2;
    const left = currentPage - delta;
    const right = currentPage + delta + 1;
    let prev: number | undefined;

    for (let i = 1; i <= totalPages; i++) {
      if (i === 1 || i === totalPages || (i >= left && i < right)) {
        if (prev && i - prev > 1) {
          items.push('...');
        }
        items.push(i);
        prev = i;
      }
    }
    return items;
  })();

  function goToPage(page: number) {
    if (page >= 1 && page <= totalPages && page !== currentPage) {
      dispatch('change', page);
    }
  }
</script>

{#if totalPages > 1}
  <div class="pagination">
    <div class="pagination-info">
      Menampilkan {startItem}-{endItem} dari {totalItems} item
    </div>
    
    <div class="pagination-controls">
      <button 
        class="pagination-btn"
        disabled={currentPage === 1}
        on:click={() => goToPage(currentPage - 1)}
        aria-label="Previous page"
      >
        <svg width="16" height="16" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2">
          <path d="M15 18l-6-6 6-6"/>
        </svg>
      </button>
      
      {#each pages as page}
        {#if typeof page === 'number'}
          <button 
            class="pagination-btn"
            class:active={page === currentPage}
            on:click={() => goToPage(page)}
          >
            {page}
          </button>
        {:else}
          <span class="pagination-ellipsis">{page}</span>
        {/if}
      {/each}
      
      <button 
        class="pagination-btn"
        disabled={currentPage === totalPages}
        on:click={() => goToPage(currentPage + 1)}
        aria-label="Next page"
      >
        <svg width="16" height="16" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2">
          <path d="M9 18l6-6-6-6"/>
        </svg>
      </button>
    </div>
  </div>
{/if}

<style>
  .pagination {
    display: flex;
    align-items: center;
    justify-content: space-between;
    padding: 1rem 0;
    gap: 1rem;
    flex-wrap: wrap;
  }

  .pagination-info {
    font-size: 0.875rem;
    color: var(--color-text-muted);
  }

  .pagination-controls {
    display: flex;
    align-items: center;
    gap: 0.25rem;
  }

  .pagination-btn {
    display: flex;
    align-items: center;
    justify-content: center;
    min-width: 2rem;
    height: 2rem;
    padding: 0 0.5rem;
    font-size: 0.875rem;
    font-weight: 500;
    color: var(--color-text);
    background: var(--color-bg);
    border: 1px solid var(--color-border);
    border-radius: 0.375rem;
    cursor: pointer;
    transition: all 0.2s;
  }

  .pagination-btn:hover:not(:disabled) {
    background: var(--color-secondary);
    border-color: var(--color-primary);
  }

  .pagination-btn:disabled {
    opacity: 0.5;
    cursor: not-allowed;
  }

  .pagination-btn.active {
    background: var(--color-primary);
    border-color: var(--color-primary);
    color: white;
  }

  .pagination-ellipsis {
    padding: 0 0.5rem;
    color: var(--color-text-muted);
  }
</style>
