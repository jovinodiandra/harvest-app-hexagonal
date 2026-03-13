# Ternaklele

**Sistem Manajemen Budidaya Ikan Modern**

Ternaklele adalah aplikasi web full-stack untuk manajemen budidaya ikan (aquaculture) yang komprehensif. Dibangun dengan arsitektur Clean/Hexagonal yang memisahkan business logic dari infrastruktur, memungkinkan skalabilitas dan maintainability yang tinggi.

## Fitur Utama

### Manajemen Kolam & Budidaya
- **Ponds Management** - Kelola data kolam budidaya
- **Seed Management** - Tracking bibit/benih ikan
- **Growth Records** - Pantau pertumbuhan ikan secara berkala
- **Death Records** - Catat dan analisis mortalitas

### Monitoring & Kualitas
- **Water Quality** - Monitor pH, suhu, oksigen, dan parameter air lainnya
- **Diseases Records** - Catat riwayat penyakit dan penanganannya
- **Fish Statistics** - Dashboard statistik populasi ikan
- **Growth Charts** - Visualisasi data pertumbuhan

### Pakan & Jadwal
- **Feed Schedules** - Atur jadwal pemberian pakan
- **Feed Reminders** - Notifikasi pengingat waktu pakan
- **Harvest Reminders** - Pengingat waktu panen optimal

### Keuangan & Operasional
- **Finance Records** - Catat pemasukan dan pengeluaran
- **Finance Reports** - Laporan keuangan dan analisis ROI
- **Harvest Reports** - Laporan hasil panen
- **Supplier Management** - Kelola data supplier
- **Contact Management** - Manajemen kontak bisnis

### Sistem & Keamanan
- **Multi-tenant Organization** - Dukungan multi-organisasi
- **User Management** - Manajemen pengguna dengan role-based access
- **JWT Authentication** - Autentikasi aman dengan token JWT
- **Password Encryption** - Enkripsi password dengan BCrypt

## Tech Stack

### Backend
| Technology | Version | Description |
|------------|---------|-------------|
| Java | 21 | Programming language |
| Spring Boot | 4.0.3 | Application framework |
| PostgreSQL | 16 | Relational database |
| HikariCP | 5.1.0 | Connection pooling |
| JJWT | 0.12.7 | JWT token handling |
| BCrypt | 0.4 | Password hashing |
| SpringDoc OpenAPI | 3.0.0 | API documentation |
| Lombok | - | Boilerplate reduction |

### Frontend
| Technology | Version | Description |
|------------|---------|-------------|
| Svelte | 5.53 | Reactive UI framework |
| Vite | 8.0 | Build tool & dev server |
| TypeScript | 5.9 | Type-safe JavaScript |
| Lucide | 0.577 | Icon library |

### Infrastructure
| Technology | Description |
|------------|-------------|
| Docker | Containerization |
| Nginx | Reverse proxy & static serving |
| Docker Compose | Multi-container orchestration |

## Arsitektur

### Clean Architecture / Hexagonal Architecture

```
┌─────────────────────────────────────────────────────────────────┐
│                        INFRASTRUCTURE                            │
│  ┌─────────────────┐  ┌─────────────────┐  ┌─────────────────┐  │
│  │ spring-http-    │  │ postgre-adapter │  │  jwt-adapter    │  │
│  │ adapter         │  │                 │  │                 │  │
│  │ (REST API)      │  │ (Persistence)   │  │ (Security)      │  │
│  └────────┬────────┘  └────────┬────────┘  └────────┬────────┘  │
│           │                    │                    │           │
│           └────────────────────┼────────────────────┘           │
│                                │                                │
│  ┌─────────────────────────────▼─────────────────────────────┐  │
│  │                         CORE                               │  │
│  │  ┌─────────────────────────────────────────────────────┐  │  │
│  │  │                   Application                        │  │  │
│  │  │  • Use Cases (Business Logic)                       │  │  │
│  │  │  • Ports (Inbound & Outbound Interfaces)            │  │  │
│  │  │  • DTOs (Data Transfer Objects)                     │  │  │
│  │  └─────────────────────────────────────────────────────┘  │  │
│  │  ┌─────────────────────────────────────────────────────┐  │  │
│  │  │                    Domain                            │  │  │
│  │  │  • Entities (Ponds, Seeds, Records, etc.)           │  │  │
│  │  │  • Value Objects                                     │  │  │
│  │  │  • Domain Services                                   │  │  │
│  │  └─────────────────────────────────────────────────────┘  │  │
│  └───────────────────────────────────────────────────────────┘  │
└─────────────────────────────────────────────────────────────────┘
```

### Frontend Architecture (Feature-Sliced Design)

```
frontend/src/
├── app/          # Application initialization, providers, styles
├── pages/        # Page components & routing
├── widgets/      # Complex UI blocks (header, sidebar, etc.)
├── features/     # User interactions & business features
├── entities/     # Business entities (ponds, seeds, users)
└── shared/       # Reusable utilities, UI kit, types, API
```

### Project Structure

```
ternaklele/
├── core/                      # Domain & Application Layer
│   └── src/main/java/org/harvest/
│       ├── domain/            # Domain entities
│       └── application/       # Use cases & ports
│
├── spring-http-adapter/       # REST API Layer
│   └── src/main/java/org/harvest/springhttpadapter/
│       ├── controller/        # REST controllers
│       ├── config/            # Spring configurations
│       └── middleware/        # Interceptors & filters
│
├── postgre-adapter/           # Database Layer
│   └── src/main/java/         # Repository implementations
│
├── jwt-adapter/               # JWT Authentication
│   └── src/main/java/         # Token service implementation
│
├── password-security-adapter/ # Password Security
│   └── src/main/java/         # BCrypt implementation
│
├── frontend/                  # Svelte Frontend
│   ├── src/
│   └── Dockerfile
│
├── docker-compose.yml         # Container orchestration
├── Dockerfile.backend         # Backend container
└── README.md
```

## Quick Start

### Prerequisites
- Docker & Docker Compose
- (Untuk development) Java 21, Maven 3.9+, Node.js 22+

### Menjalankan dengan Docker

```bash
# 1. Clone repository
git clone https://github.com/username/ternaklele.git
cd ternaklele

# 2. Copy environment file
cp .env.example .env

# 3. Build dan jalankan semua services
docker-compose up --build

# Atau jalankan di background
docker-compose up -d --build
```

### Akses Aplikasi

| Service | URL |
|---------|-----|
| Frontend | http://localhost:3000 |
| Backend API | http://localhost:8080 |
| Swagger UI | http://localhost:8080/swagger-ui.html |
| API Docs | http://localhost:8080/api-docs |

### Development Mode

**Backend:**
```bash
# Install dependencies untuk setiap module
cd core && mvn install -DskipTests
cd ../jwt-adapter && mvn install -DskipTests
cd ../password-security-adapter && mvn install -DskipTests
cd ../postgre-adapter && mvn install -DskipTests

# Jalankan Spring Boot
cd ../spring-http-adapter
mvn spring-boot:run
```

**Frontend:**
```bash
cd frontend
npm install
npm run dev
```

## API Documentation

API didokumentasikan menggunakan OpenAPI/Swagger. Setelah menjalankan backend, akses dokumentasi di:
- Swagger UI: http://localhost:8080/swagger-ui.html
- OpenAPI JSON: http://localhost:8080/api-docs

### Contoh Endpoints

```
Authentication
POST   /api/auth/register     # Registrasi user baru
POST   /api/auth/login        # Login user

Ponds
GET    /api/ponds             # List semua kolam
POST   /api/ponds             # Buat kolam baru
PUT    /api/ponds/{id}        # Update kolam
DELETE /api/ponds/{id}        # Hapus kolam

Growth Records
GET    /api/growth-records    # List catatan pertumbuhan
POST   /api/growth-records    # Catat pertumbuhan baru

Water Quality
GET    /api/water-quality     # List kualitas air
POST   /api/water-quality     # Catat parameter air

Finance
GET    /api/finance           # List transaksi keuangan
GET    /api/reports/finance   # Laporan keuangan
```

## Environment Variables

| Variable | Default | Description |
|----------|---------|-------------|
| `POSTGRES_DB` | ternaklele | Database name |
| `POSTGRES_USER` | ternaklele | Database user |
| `POSTGRES_PASSWORD` | ternaklele123 | Database password |
| `JWT_SECRET` | - | Secret key untuk JWT (wajib diganti di production) |
| `JWT_EXPIRATION` | 86400000 | Token expiration (ms) |
| `VITE_API_URL` | http://localhost:8080 | Backend API URL |

## Screenshots

*Coming soon*

## Roadmap

- [ ] Mobile responsive optimization
- [ ] Push notifications untuk reminders
- [ ] Export data ke Excel/PDF
- [ ] Dashboard analytics dengan grafik
- [ ] Multi-language support (ID/EN)
- [ ] Integration dengan IoT sensors

## Kontribusi

Kontribusi sangat diterima! Silakan buat issue atau pull request.

## Lisensi

MIT License - lihat file [LICENSE](LICENSE) untuk detail.

---

**Ternaklele** - Memudahkan pengelolaan budidaya ikan Anda.
