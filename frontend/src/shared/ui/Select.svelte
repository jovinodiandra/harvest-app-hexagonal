<script lang="ts">
  type Option = { value: string; label: string };

  export let options: Option[] = [];
  export let value: string = '';
  export let label: string = '';
  export let placeholder: string = 'Pilih...';
  export let error: string = '';
  export let disabled: boolean = false;
  export let required: boolean = false;
  export let name: string = '';
  export let id: string = name || `select-${Math.random().toString(36).substr(2, 9)}`;
</script>

<div class="select-wrapper">
  {#if label}
    <label for={id} class="select-label">
      {label}
      {#if required}<span class="required">*</span>{/if}
    </label>
  {/if}
  
  <select
    {id}
    {name}
    {disabled}
    {required}
    bind:value
    class="select"
    class:select-error={error}
    on:change
    {...$$restProps}
  >
    <option value="" disabled>{placeholder}</option>
    {#each options as option}
      <option value={option.value}>{option.label}</option>
    {/each}
  </select>
  
  {#if error}
    <span class="error-text">{error}</span>
  {/if}
</div>

<style>
  .select-wrapper {
    display: flex;
    flex-direction: column;
    gap: 0.375rem;
  }

  .select-label {
    font-size: 0.875rem;
    font-weight: 500;
    color: var(--color-text);
  }

  .required {
    color: var(--color-danger);
  }

  .select {
    padding: 0.625rem 0.875rem;
    font-size: 1rem;
    border: 1px solid var(--color-border);
    border-radius: 0.5rem;
    background: var(--color-bg);
    color: var(--color-text);
    transition: border-color 0.2s, box-shadow 0.2s;
    font-family: inherit;
    cursor: pointer;
    appearance: none;
    background-image: url("data:image/svg+xml,%3Csvg xmlns='http://www.w3.org/2000/svg' width='16' height='16' viewBox='0 0 24 24' fill='none' stroke='%236b7280' stroke-width='2' stroke-linecap='round' stroke-linejoin='round'%3E%3Cpath d='m6 9 6 6 6-6'/%3E%3C/svg%3E");
    background-repeat: no-repeat;
    background-position: right 0.75rem center;
    padding-right: 2.5rem;
  }

  .select:focus {
    outline: none;
    border-color: var(--color-primary);
    box-shadow: 0 0 0 3px var(--color-primary-light);
  }

  .select:disabled {
    background-color: var(--color-secondary);
    cursor: not-allowed;
  }

  .select-error {
    border-color: var(--color-danger);
  }

  .error-text {
    font-size: 0.75rem;
    color: var(--color-danger);
  }
</style>
