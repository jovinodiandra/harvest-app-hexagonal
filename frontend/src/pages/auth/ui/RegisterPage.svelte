<script lang="ts">
  import { Fish } from 'lucide-svelte';
  import { Input, Button, Alert } from '../../../shared/ui';
  import { api } from '../../../shared/api';
  import { authStore } from '../../../entities/auth';

  let name = '';
  let email = '';
  let password = '';
  let confirmPassword = '';
  let organizationName = '';
  let loading = false;
  let error = '';

  async function handleSubmit() {
    if (!name || !email || !password || !confirmPassword || !organizationName) {
      error = 'Semua field harus diisi';
      return;
    }

    if (password !== confirmPassword) {
      error = 'Password dan Konfirmasi Password tidak sama';
      return;
    }

    if (password.length < 8) {
      error = 'Password minimal 8 karakter';
      return;
    }

    loading = true;
    error = '';

    const response = await api.register({
      name,
      email,
      password,
      organizationName,
    });

    if (response.success && response.data) {
      authStore.login(response.data);
      window.location.href = '/';
    } else {
      error = response.error || 'Registrasi gagal. Silakan coba lagi.';
    }

    loading = false;
  }
</script>

<div class="register-page">
  <div class="register-card">
    <div class="register-header">
      <div class="logo">
        <Fish size={48} />
      </div>
      <h1 class="title">Ternak Lele</h1>
      <p class="subtitle">Daftar Akun Baru</p>
    </div>

    <form class="register-form" on:submit|preventDefault={handleSubmit}>
      {#if error}
        <Alert variant="error">{error}</Alert>
      {/if}

      <Input
        type="text"
        label="Nama Lengkap"
        placeholder="Masukkan nama lengkap"
        bind:value={name}
        required
      />

      <Input
        type="email"
        label="Email"
        placeholder="nama@email.com"
        bind:value={email}
        required
      />

      <Input
        type="text"
        label="Nama Organisasi/Usaha"
        placeholder="Contoh: Lele Maju Jaya"
        bind:value={organizationName}
        required
      />

      <Input
        type="password"
        label="Password"
        placeholder="Minimal 8 karakter"
        bind:value={password}
        required
      />

      <Input
        type="password"
        label="Konfirmasi Password"
        placeholder="Masukkan ulang password"
        bind:value={confirmPassword}
        required
      />

      <Button type="submit" fullWidth {loading}>
        {loading ? 'Memproses...' : 'Daftar'}
      </Button>
    </form>

    <div class="register-footer">
      <p>Sudah punya akun? <a href="/login">Masuk di sini</a></p>
    </div>
  </div>
</div>

<style>
  .register-page {
    min-height: 100vh;
    display: flex;
    align-items: center;
    justify-content: center;
    padding: 1rem;
    background: linear-gradient(135deg, var(--color-primary-light) 0%, var(--color-bg) 100%);
  }

  .register-card {
    width: 100%;
    max-width: 400px;
    background: var(--color-card);
    border-radius: 1rem;
    padding: 2rem;
    box-shadow: 0 25px 50px rgba(0, 0, 0, 0.1);
  }

  .register-header {
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

  .register-form {
    display: flex;
    flex-direction: column;
    gap: 1.25rem;
  }

  .register-footer {
    text-align: center;
    margin-top: 1.5rem;
    padding-top: 1.5rem;
    border-top: 1px solid var(--color-border);
  }

  .register-footer p {
    margin: 0;
    color: var(--color-text-muted);
    font-size: 0.875rem;
  }

  .register-footer a {
    color: var(--color-primary);
    text-decoration: none;
    font-weight: 500;
  }

  .register-footer a:hover {
    text-decoration: underline;
  }

  @media (max-width: 480px) {
    .register-page {
      padding: 0.5rem;
      align-items: flex-start;
      padding-top: 1rem;
    }

    .register-card {
      padding: 1.5rem;
      border-radius: 0.75rem;
    }

    .register-header {
      margin-bottom: 1.5rem;
    }

    .logo {
      width: 64px;
      height: 64px;
      border-radius: 0.75rem;
    }

    .logo :global(svg) {
      width: 36px !important;
      height: 36px !important;
    }

    .title {
      font-size: 1.5rem;
    }

    .subtitle {
      font-size: 0.875rem;
    }

    .register-form {
      gap: 1rem;
    }
  }
</style>
