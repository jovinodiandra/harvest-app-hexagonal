# Ternak Lele - Frontend

Frontend aplikasi manajemen budidaya lele menggunakan Svelte dengan arsitektur Feature-Sliced Design (FSD).

## Tech Stack

- **Framework**: Svelte + Vite
- **Language**: TypeScript
- **Styling**: CSS Variables + Scoped CSS
- **Icons**: Lucide Svelte
- **Routing**: svelte-routing

## Struktur Folder (Feature-Sliced Design)

```
src/
├── app/                    # App layer - global setup
│   ├── App.svelte         # Root component dengan routing
│   └── styles/
│       └── global.css     # Global styles & CSS variables
│
├── pages/                  # Pages layer - route pages
│   ├── dashboard/         # Dashboard page
│   ├── auth/              # Login & Register
│   ├── ponds/             # Manajemen Kolam
│   ├── seeds/             # Manajemen Bibit
│   ├── finance/           # Keuangan
│   ├── harvest/           # Panen
│   └── water-quality/     # Kualitas Air
│
├── widgets/                # Widgets layer - complex UI blocks
│   ├── header/            # Header component
│   ├── sidebar/           # Sidebar navigation
│   └── layout/            # Main layout wrapper
│
├── features/               # Features layer - user interactions
│   └── (future features)
│
├── entities/               # Entities layer - business entities
│   ├── auth/              # Auth store & types
│   └── pond/              # Pond store & components
│
└── shared/                 # Shared layer - reusable code
    ├── api/               # API client & mock services
    ├── config/            # App configuration
    ├── lib/               # Utilities
    ├── mock/              # Mock data
    ├── types/             # TypeScript types
    └── ui/                # UI components
        ├── Alert.svelte
        ├── Badge.svelte
        ├── Button.svelte
        ├── Card.svelte
        ├── Input.svelte
        ├── Modal.svelte
        ├── Pagination.svelte
        ├── Select.svelte
        ├── StatCard.svelte
        ├── Table.svelte
        └── Textarea.svelte
```

## Menjalankan Aplikasi

### Development

```bash
# Install dependencies
npm install

# Run development server
npm run dev
```

Buka http://localhost:3000

### Production Build

```bash
# Build untuk production
npm run build

# Preview build
npm run preview
```

## Fitur

### Dashboard
- Overview statistik kolam, ikan, panen, dan keuangan
- Ringkasan panen terbaru
- Transaksi keuangan terbaru

### Manajemen Kolam
- CRUD kolam budidaya
- Informasi kapasitas dan lokasi

### Manajemen Bibit
- Pencatatan tebar bibit
- Tracking per kolam

### Panen
- Pencatatan hasil panen
- Statistik berat dan jumlah ikan
- Estimasi pendapatan

### Keuangan
- Tracking pemasukan dan pengeluaran
- Kategorisasi transaksi
- Laporan profit

### Kualitas Air
- Pengukuran pH, suhu, dan dissolved oxygen
- Monitoring per kolam

## Demo Login

```
Email: ahmad@lele.com
Password: password123
```

## API Contract

Lihat [API_CONTRACT.md](./API_CONTRACT.md) untuk dokumentasi lengkap API.

## Pengembangan Selanjutnya

Halaman berikut masih dalam pengembangan:
- Jadwal Pakan (`/feed-schedules`)
- Pertumbuhan (`/growth-records`)
- Kematian (`/death-records`)
- Penyakit (`/diseases`)
- Supplier (`/suppliers`)
- Pengingat (`/reminders`)
- Pengguna (`/users`)

## Scripts

```bash
npm run dev      # Development server
npm run build    # Production build
npm run preview  # Preview production build
```
