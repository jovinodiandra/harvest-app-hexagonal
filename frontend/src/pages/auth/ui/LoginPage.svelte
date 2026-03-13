<script lang="ts">
  import { Fish } from 'lucide-svelte';
  import { Input, Button, Alert } from '../../../shared/ui';
  import { api } from '../../../shared/api';
  import { authStore } from '../../../entities/auth';

  let email = '';
  let password = '';
  let loading = false;
  let error = '';

  async function handleSubmit() {
    if (!email || !password) {
      error = 'Email dan password harus diisi';
      return;
    }

    loading = true;
    error = '';

    const response = await api.login({ email, password });

    if (response.success && response.data) {
      authStore.login(response.data);
      window.location.href = '/';
    } else {
      error = response.error || 'Login gagal. Periksa email dan password Anda.';
    }

    loading = false;
  }
</script>

<div class="login-page">
  <div class="login-card">
    <div class="login-header">
      <div class="logo">
        <Fish size={48} />
      </div>
      <h1 class="title">Ternak Lele</h1>
      <p class="subtitle">Sistem Manajemen Budidaya Lele</p>
    </div>

    <form class="login-form" on:submit|preventDefault={handleSubmit}>
      {#if error}
        <Alert variant="error">{error}</Alert>
      {/if}

      <Input
        type="email"
        label="Email"
        placeholder="nama@email.com"
        bind:value={email}
        required
      />

      <Input
        type="password"
        label="Password"
        placeholder="Masukkan password"
        bind:value={password}
        required
      />

      <Button type="submit" fullWidth {loading}>
        {loading ? 'Memproses...' : 'Masuk'}
      </Button>
    </form>

    <div class="login-footer">
      <p>Belum punya akun? <a href="/register">Daftar sekarang</a></p>
    </div>

    <div class="demo-info">
      <p><strong>Info:</strong></p>
      <p>Silahkan login dengan akun yang sudah terdaftar atau daftar akun baru.</p>
    </div>
  </div>
</div>

<style>
  .login-page {
    min-height: 100vh;
    display: flex;
    align-items: center;
    justify-content: center;
    padding: 1rem;
    background: linear-gradient(135deg, var(--color-primary-light) 0%, var(--color-bg) 100%);
  }

  .login-card {
    width: 100%;
    max-width: 400px;
    background: var(--color-card);
    border-radius: 1rem;
    padding: 2rem;
    box-shadow: 0 25px 50px rgba(0, 0, 0, 0.1);
  }

  .login-header {
    text-align: center;
    margin-bottom: 2rem;
  }

  .logo {
    display: inline-flex;
    align-items: center;
    justify-content: center;
    width: 80px;
    height: 80px;
    background: var(--color-primary);
    color: white;
    border-radius: 1rem;
    margin-bottom: 1rem;
  }

  .title {
    margin: 0;
    font-size: 1.75rem;
    font-weight: 700;
    color: var(--color-text);
  }

  .subtitle {
    margin: 0.5rem 0 0;
    color: var(--color-text-muted);
    font-size: 0.9375rem;
  }

  .login-form {
    display: flex;
    flex-direction: column;
    gap: 1.25rem;
  }

  .login-footer {
    text-align: center;
    margin-top: 1.5rem;
    padding-top: 1.5rem;
    border-top: 1px solid var(--color-border);
  }

  .login-footer p {
    margin: 0;
    color: var(--color-text-muted);
    font-size: 0.875rem;
  }

  .login-footer a {
    color: var(--color-primary);
    text-decoration: none;
    font-weight: 500;
  }

  .login-footer a:hover {
    text-decoration: underline;
  }

  .demo-info {
    margin-top: 1.5rem;
    padding: 1rem;
    background: var(--color-secondary);
    border-radius: 0.5rem;
    font-size: 0.8125rem;
    color: var(--color-text-muted);
  }

  .demo-info p {
    margin: 0.25rem 0;
  }

  .demo-info strong {
    color: var(--color-text);
  }
</style>
