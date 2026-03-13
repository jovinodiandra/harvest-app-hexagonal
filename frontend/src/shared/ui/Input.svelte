<script lang="ts">
  export let type: string = 'text';
  export let value: string | number = '';
  export let label: string = '';
  export let placeholder: string = '';
  export let error: string = '';
  export let disabled: boolean = false;
  export let required: boolean = false;
  export let name: string = '';
  export let id: string = name || `input-${Math.random().toString(36).substr(2, 9)}`;
</script>

<div class="input-wrapper">
  {#if label}
    <label for={id} class="input-label">
      {label}
      {#if required}<span class="required">*</span>{/if}
    </label>
  {/if}
  
  <input
    {id}
    {name}
    {type}
    {placeholder}
    {disabled}
    {required}
    bind:value
    class="input"
    class:input-error={error}
    on:input
    on:change
    on:blur
    on:focus
    {...$$restProps}
  />
  
  {#if error}
    <span class="error-text">{error}</span>
  {/if}
</div>

<style>
  .input-wrapper {
    display: flex;
    flex-direction: column;
    gap: 0.375rem;
  }

  .input-label {
    font-size: 0.875rem;
    font-weight: 500;
    color: var(--color-text);
  }

  .required {
    color: var(--color-danger);
  }

  .input {
    padding: 0.625rem 0.875rem;
    font-size: 1rem;
    border: 1px solid var(--color-border);
    border-radius: 0.5rem;
    background: var(--color-bg);
    color: var(--color-text);
    transition: border-color 0.2s, box-shadow 0.2s;
    font-family: inherit;
  }

  .input:focus {
    outline: none;
    border-color: var(--color-primary);
    box-shadow: 0 0 0 3px var(--color-primary-light);
  }

  .input:disabled {
    background: var(--color-secondary);
    cursor: not-allowed;
  }

  .input-error {
    border-color: var(--color-danger);
  }

  .input-error:focus {
    box-shadow: 0 0 0 3px rgba(239, 68, 68, 0.2);
  }

  .error-text {
    font-size: 0.75rem;
    color: var(--color-danger);
  }
</style>
