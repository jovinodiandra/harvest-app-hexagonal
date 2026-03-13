<script lang="ts">
  export let value: string = '';
  export let label: string = '';
  export let placeholder: string = '';
  export let error: string = '';
  export let disabled: boolean = false;
  export let required: boolean = false;
  export let rows: number = 4;
  export let name: string = '';
  export let id: string = name || `textarea-${Math.random().toString(36).substr(2, 9)}`;
</script>

<div class="textarea-wrapper">
  {#if label}
    <label for={id} class="textarea-label">
      {label}
      {#if required}<span class="required">*</span>{/if}
    </label>
  {/if}
  
  <textarea
    {id}
    {name}
    {placeholder}
    {disabled}
    {required}
    {rows}
    bind:value
    class="textarea"
    class:textarea-error={error}
    on:input
    on:change
    on:blur
    on:focus
    {...$$restProps}
  ></textarea>
  
  {#if error}
    <span class="error-text">{error}</span>
  {/if}
</div>

<style>
  .textarea-wrapper {
    display: flex;
    flex-direction: column;
    gap: 0.375rem;
  }

  .textarea-label {
    font-size: 0.875rem;
    font-weight: 500;
    color: var(--color-text);
  }

  .required {
    color: var(--color-danger);
  }

  .textarea {
    padding: 0.625rem 0.875rem;
    font-size: 1rem;
    border: 1px solid var(--color-border);
    border-radius: 0.5rem;
    background: var(--color-bg);
    color: var(--color-text);
    transition: border-color 0.2s, box-shadow 0.2s;
    font-family: inherit;
    resize: vertical;
    min-height: 80px;
  }

  .textarea:focus {
    outline: none;
    border-color: var(--color-primary);
    box-shadow: 0 0 0 3px var(--color-primary-light);
  }

  .textarea:disabled {
    background: var(--color-secondary);
    cursor: not-allowed;
  }

  .textarea-error {
    border-color: var(--color-danger);
  }

  .error-text {
    font-size: 0.75rem;
    color: var(--color-danger);
  }
</style>
