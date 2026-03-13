# API Contract - Ternak Lele

Dokumentasi API untuk aplikasi manajemen budidaya lele.

## Base URL

```
Development: http://localhost:8080
Production: https://api.ternaklele.com
```

## Authentication

Semua endpoint (kecuali `/auth/*`) memerlukan header authorization:

```
Authorization: Bearer <JWT_TOKEN>
```

---

## 1. Authentication

### 1.1 Register

Registrasi pengguna baru beserta organisasi.

**Endpoint:** `POST /auth/register`

**Request Body:**
```json
{
  "name": "string",
  "email": "string",
  "password": "string",
  "organizationName": "string"
}
```

**Response (201 Created):**
```json
{
  "success": true,
  "data": {
    "userId": "string",
    "organizationId": "string",
    "role": "owner",
    "token": "string"
  }
}
```

### 1.2 Login

Login ke aplikasi.

**Endpoint:** `POST /auth/login`

**Request Body:**
```json
{
  "email": "string",
  "password": "string"
}
```

**Response (200 OK):**
```json
{
  "success": true,
  "data": {
    "userId": "string",
    "organizationId": "string",
    "role": "owner | admin | user",
    "token": "string"
  }
}
```

### 1.3 Logout

Logout dan invalidate session.

**Endpoint:** `POST /auth/logout`

**Response (200 OK):**
```json
{
  "success": true,
  "message": "Logged out successfully"
}
```

---

## 2. Users

### 2.1 List Users

Mendapatkan daftar pengguna dalam organisasi.

**Endpoint:** `GET /users`

**Query Parameters:**
| Parameter | Type | Default | Description |
|-----------|------|---------|-------------|
| page | number | 1 | Nomor halaman |
| limit | number | 10 | Jumlah item per halaman |

**Response (200 OK):**
```json
{
  "success": true,
  "data": {
    "data": [
      {
        "id": "string",
        "name": "string",
        "email": "string",
        "organizationId": "string",
        "role": "owner | admin | user"
      }
    ],
    "page": 1,
    "limit": 10,
    "total": 100,
    "totalPages": 10
  }
}
```

### 2.2 Create User

Membuat pengguna baru dalam organisasi.

**Endpoint:** `POST /users`

**Request Body:**
```json
{
  "name": "string",
  "email": "string",
  "password": "string",
  "role": "admin | user"
}
```

**Response (201 Created):**
```json
{
  "success": true,
  "data": {
    "id": "string",
    "name": "string",
    "email": "string",
    "organizationId": "string",
    "role": "string"
  }
}
```

### 2.3 Update User

Memperbarui data pengguna.

**Endpoint:** `PUT /users/{id}`

**Request Body:**
```json
{
  "name": "string",
  "role": "admin | user"
}
```

**Response (200 OK):**
```json
{
  "success": true,
  "data": {
    "id": "string",
    "name": "string",
    "email": "string",
    "organizationId": "string",
    "role": "string"
  }
}
```

### 2.4 Delete User

Menghapus pengguna.

**Endpoint:** `DELETE /users/{id}`

**Response (200 OK):**
```json
{
  "success": true,
  "message": "User deleted successfully"
}
```

---

## 3. Ponds (Kolam)

### 3.1 List Ponds

**Endpoint:** `GET /ponds`

**Query Parameters:**
| Parameter | Type | Default | Description |
|-----------|------|---------|-------------|
| page | number | 1 | Nomor halaman |
| limit | number | 10 | Jumlah item per halaman |

**Response (200 OK):**
```json
{
  "success": true,
  "data": {
    "data": [
      {
        "id": "string",
        "name": "string",
        "capacity": 5000,
        "location": "string",
        "organizationId": "string"
      }
    ],
    "page": 1,
    "limit": 10,
    "total": 100,
    "totalPages": 10
  }
}
```

### 3.2 Get Pond by ID

**Endpoint:** `GET /ponds/{id}`

**Response (200 OK):**
```json
{
  "success": true,
  "data": {
    "id": "string",
    "name": "string",
    "capacity": 5000,
    "location": "string",
    "organizationId": "string"
  }
}
```

### 3.3 Create Pond

**Endpoint:** `POST /ponds`

**Request Body:**
```json
{
  "name": "string",
  "capacity": 5000,
  "location": "string"
}
```

**Response (201 Created):**
```json
{
  "success": true,
  "data": {
    "id": "string",
    "name": "string",
    "capacity": 5000,
    "location": "string",
    "organizationId": "string"
  }
}
```

### 3.4 Update Pond

**Endpoint:** `PUT /ponds/{id}`

**Request Body:**
```json
{
  "name": "string",
  "capacity": 5000,
  "location": "string"
}
```

### 3.5 Delete Pond

**Endpoint:** `DELETE /ponds/{id}`

---

## 4. Seeds (Bibit)

### 4.1 List Seeds

**Endpoint:** `GET /seeds`

**Query Parameters:**
| Parameter | Type | Default | Description |
|-----------|------|---------|-------------|
| page | number | 1 | Nomor halaman |
| limit | number | 10 | Jumlah item per halaman |
| pondsId | string | - | Filter by pond ID |

**Response (200 OK):**
```json
{
  "success": true,
  "data": {
    "data": [
      {
        "id": "string",
        "pondsId": "string",
        "type": "string",
        "quantity": 5000,
        "stockDate": "2024-01-15",
        "organizationId": "string"
      }
    ],
    "page": 1,
    "limit": 10,
    "total": 100,
    "totalPages": 10
  }
}
```

### 4.2 Create Seed

**Endpoint:** `POST /seeds`

**Request Body:**
```json
{
  "pondsId": "string",
  "type": "string",
  "quantity": 5000,
  "stockDate": "2024-01-15"
}
```

### 4.3 Update Seed

**Endpoint:** `PUT /seeds/{id}`

### 4.4 Delete Seed

**Endpoint:** `DELETE /seeds/{id}`

---

## 5. Feed Schedules (Jadwal Pakan)

### 5.1 List Feed Schedules

**Endpoint:** `GET /feed-schedules`

**Query Parameters:**
| Parameter | Type | Default | Description |
|-----------|------|---------|-------------|
| page | number | 1 | Nomor halaman |
| limit | number | 10 | Jumlah item per halaman |
| pondsId | string | - | Filter by pond ID |

**Response (200 OK):**
```json
{
  "success": true,
  "data": {
    "data": [
      {
        "id": "string",
        "pondsId": "string",
        "feedType": "string",
        "feedAmount": 5,
        "feedTime": "07:00",
        "organizationId": "string"
      }
    ],
    "page": 1,
    "limit": 10,
    "total": 100,
    "totalPages": 10
  }
}
```

### 5.2 Create Feed Schedule

**Endpoint:** `POST /feed-schedules`

**Request Body:**
```json
{
  "pondsId": "string",
  "feedType": "string",
  "feedAmount": 5,
  "feedTime": "07:00"
}
```

### 5.3 Update Feed Schedule

**Endpoint:** `PUT /feed-schedules/{id}`

### 5.4 Delete Feed Schedule

**Endpoint:** `DELETE /feed-schedules/{id}`

---

## 6. Growth Records (Catatan Pertumbuhan)

### 6.1 List Growth Records

**Endpoint:** `GET /growth-records`

**Query Parameters:**
| Parameter | Type | Default | Description |
|-----------|------|---------|-------------|
| page | number | 1 | Nomor halaman |
| limit | number | 10 | Jumlah item per halaman |
| pondsId | string | - | Filter by pond ID |

**Response (200 OK):**
```json
{
  "success": true,
  "data": {
    "data": [
      {
        "id": "string",
        "pondsId": "string",
        "recordDate": "2024-02-01",
        "averageWidth": 2.5,
        "averageLength": 8.0,
        "organizationId": "string"
      }
    ],
    "page": 1,
    "limit": 10,
    "total": 100,
    "totalPages": 10
  }
}
```

### 6.2 Create Growth Record

**Endpoint:** `POST /growth-records`

**Request Body:**
```json
{
  "pondsId": "string",
  "recordDate": "2024-02-01",
  "averageWidth": 2.5,
  "averageLength": 8.0
}
```

### 6.3 Update Growth Record

**Endpoint:** `PUT /growth-records/{id}`

### 6.4 Delete Growth Record

**Endpoint:** `DELETE /growth-records/{id}`

---

## 7. Death Records (Catatan Kematian)

### 7.1 List Death Records

**Endpoint:** `GET /death-records`

**Query Parameters:**
| Parameter | Type | Default | Description |
|-----------|------|---------|-------------|
| page | number | 1 | Nomor halaman |
| limit | number | 10 | Jumlah item per halaman |
| pondsId | string | - | Filter by pond ID |

**Response (200 OK):**
```json
{
  "success": true,
  "data": {
    "data": [
      {
        "id": "string",
        "pondsId": "string",
        "recordDate": "2024-02-01",
        "deathCount": 15,
        "notes": "string",
        "organizationId": "string"
      }
    ],
    "page": 1,
    "limit": 10,
    "total": 100,
    "totalPages": 10
  }
}
```

### 7.2 Create Death Record

**Endpoint:** `POST /death-records`

**Request Body:**
```json
{
  "pondsId": "string",
  "recordDate": "2024-02-01",
  "deathCount": 15,
  "notes": "string"
}
```

### 7.3 Update Death Record

**Endpoint:** `PUT /death-records/{id}`

### 7.4 Delete Death Record

**Endpoint:** `DELETE /death-records/{id}`

---

## 8. Water Quality (Kualitas Air)

### 8.1 List Water Quality Records

**Endpoint:** `GET /water-quality`

**Query Parameters:**
| Parameter | Type | Default | Description |
|-----------|------|---------|-------------|
| page | number | 1 | Nomor halaman |
| limit | number | 10 | Jumlah item per halaman |
| pondsId | string | - | Filter by pond ID |

**Response (200 OK):**
```json
{
  "success": true,
  "data": {
    "data": [
      {
        "id": "string",
        "pondsId": "string",
        "recordDate": "2024-02-01",
        "ph": 7.2,
        "temperature": 28.0,
        "dissolvedOxygen": 5.5,
        "organizationId": "string"
      }
    ],
    "page": 1,
    "limit": 10,
    "total": 100,
    "totalPages": 10
  }
}
```

### 8.2 Create Water Quality Record

**Endpoint:** `POST /water-quality`

**Request Body:**
```json
{
  "pondsId": "string",
  "recordDate": "2024-02-01",
  "ph": 7.2,
  "temperature": 28.0,
  "dissolvedOxygen": 5.5
}
```

### 8.3 Update Water Quality Record

**Endpoint:** `PUT /water-quality/{id}`

### 8.4 Delete Water Quality Record

**Endpoint:** `DELETE /water-quality/{id}`

---

## 9. Diseases Records (Catatan Penyakit)

### 9.1 List Diseases Records

**Endpoint:** `GET /diseases-records`

**Query Parameters:**
| Parameter | Type | Default | Description |
|-----------|------|---------|-------------|
| page | number | 1 | Nomor halaman |
| limit | number | 10 | Jumlah item per halaman |
| pondsId | string | - | Filter by pond ID |

**Response (200 OK):**
```json
{
  "success": true,
  "data": {
    "data": [
      {
        "id": "string",
        "pondsId": "string",
        "diseaseName": "string",
        "symptoms": "string",
        "infectedFishCount": 50,
        "diseasesDate": "2024-02-10",
        "notes": "string",
        "organizationId": "string"
      }
    ],
    "page": 1,
    "limit": 10,
    "total": 100,
    "totalPages": 10
  }
}
```

### 9.2 Create Diseases Record

**Endpoint:** `POST /diseases-records`

**Request Body:**
```json
{
  "pondsId": "string",
  "diseaseName": "string",
  "symptoms": "string",
  "infectedFishCount": 50,
  "diseasesDate": "2024-02-10",
  "notes": "string"
}
```

### 9.3 Update Diseases Record

**Endpoint:** `PUT /diseases-records/{id}`

### 9.4 Delete Diseases Record

**Endpoint:** `DELETE /diseases-records/{id}`

---

## 10. Harvest Records (Catatan Panen)

### 10.1 List Harvest Records

**Endpoint:** `GET /harvest-records`

**Query Parameters:**
| Parameter | Type | Default | Description |
|-----------|------|---------|-------------|
| page | number | 1 | Nomor halaman |
| limit | number | 10 | Jumlah item per halaman |
| pondsId | string | - | Filter by pond ID |

**Response (200 OK):**
```json
{
  "success": true,
  "data": {
    "data": [
      {
        "id": "string",
        "pondsName": "string",
        "pondsId": "string",
        "harvestDate": "2024-03-15",
        "organizationId": "string",
        "harvestFishCount": 4200,
        "totalWeight": 420,
        "notes": "string"
      }
    ],
    "page": 1,
    "limit": 10,
    "total": 100,
    "totalPages": 10
  }
}
```

### 10.2 Create Harvest Record

**Endpoint:** `POST /harvest-records`

**Request Body:**
```json
{
  "pondsId": "string",
  "harvestDate": "2024-03-15",
  "harvestFishCount": 4200,
  "totalWeight": 420,
  "notes": "string"
}
```

### 10.3 Update Harvest Record

**Endpoint:** `PUT /harvest-records/{id}`

### 10.4 Delete Harvest Record

**Endpoint:** `DELETE /harvest-records/{id}`

---

## 11. Finance Records (Catatan Keuangan)

### 11.1 List Finance Records

**Endpoint:** `GET /finance-records`

**Query Parameters:**
| Parameter | Type | Default | Description |
|-----------|------|---------|-------------|
| page | number | 1 | Nomor halaman |
| limit | number | 10 | Jumlah item per halaman |
| transactionType | string | - | Filter: "income" atau "expense" |
| startDate | string | - | Filter dari tanggal (YYYY-MM-DD) |
| endDate | string | - | Filter sampai tanggal (YYYY-MM-DD) |

**Response (200 OK):**
```json
{
  "success": true,
  "data": {
    "data": [
      {
        "id": "string",
        "transactionType": "income | expense",
        "transactionDate": "2024-03-15",
        "amount": 12600000,
        "category": "string",
        "notes": "string",
        "organizationId": "string"
      }
    ],
    "page": 1,
    "limit": 10,
    "total": 100,
    "totalPages": 10
  }
}
```

### 11.2 Create Finance Record

**Endpoint:** `POST /finance-records`

**Request Body:**
```json
{
  "transactionType": "income | expense",
  "transactionDate": "2024-03-15",
  "amount": 12600000,
  "category": "string",
  "notes": "string"
}
```

### 11.3 Update Finance Record

**Endpoint:** `PUT /finance-records/{id}`

### 11.4 Delete Finance Record

**Endpoint:** `DELETE /finance-records/{id}`

---

## 12. Suppliers

### 12.1 List Suppliers

**Endpoint:** `GET /suppliers`

**Query Parameters:**
| Parameter | Type | Default | Description |
|-----------|------|---------|-------------|
| page | number | 1 | Nomor halaman |
| limit | number | 10 | Jumlah item per halaman |

**Response (200 OK):**
```json
{
  "success": true,
  "data": {
    "data": [
      {
        "id": "string",
        "name": "string",
        "address": "string",
        "phone": "string",
        "email": "string",
        "notes": "string",
        "organizationId": "string"
      }
    ],
    "page": 1,
    "limit": 10,
    "total": 100,
    "totalPages": 10
  }
}
```

### 12.2 Create Supplier

**Endpoint:** `POST /suppliers`

**Request Body:**
```json
{
  "name": "string",
  "address": "string",
  "phone": "string",
  "email": "string",
  "notes": "string"
}
```

### 12.3 Update Supplier

**Endpoint:** `PUT /suppliers/{id}`

### 12.4 Delete Supplier

**Endpoint:** `DELETE /suppliers/{id}`

---

## 13. Contacts

### 13.1 List Contacts

**Endpoint:** `GET /contacts`

**Query Parameters:**
| Parameter | Type | Default | Description |
|-----------|------|---------|-------------|
| page | number | 1 | Nomor halaman |
| limit | number | 10 | Jumlah item per halaman |
| supplierId | string | - | Filter by supplier ID |

**Response (200 OK):**
```json
{
  "success": true,
  "data": {
    "data": [
      {
        "id": "string",
        "supplierId": "string",
        "name": "string",
        "address": "string",
        "phone": "string",
        "email": "string",
        "notes": "string"
      }
    ],
    "page": 1,
    "limit": 10,
    "total": 100,
    "totalPages": 10
  }
}
```

### 13.2 Create Contact

**Endpoint:** `POST /contacts`

### 13.3 Update Contact

**Endpoint:** `PUT /contacts/{id}`

### 13.4 Delete Contact

**Endpoint:** `DELETE /contacts/{id}`

---

## 14. Feed Reminders (Pengingat Pakan)

### 14.1 List Feed Reminders

**Endpoint:** `GET /feed-reminders`

**Response (200 OK):**
```json
{
  "success": true,
  "data": {
    "data": [
      {
        "id": "string",
        "pondsId": "string",
        "reminderDate": "2024-02-21",
        "reminderTime": "07:00",
        "feedType": "string",
        "notes": "string",
        "organizationId": "string"
      }
    ],
    "page": 1,
    "limit": 10,
    "total": 100,
    "totalPages": 10
  }
}
```

### 14.2 Create Feed Reminder

**Endpoint:** `POST /feed-reminders`

### 14.3 Update Feed Reminder

**Endpoint:** `PUT /feed-reminders/{id}`

### 14.4 Delete Feed Reminder

**Endpoint:** `DELETE /feed-reminders/{id}`

---

## 15. Harvest Reminders (Pengingat Panen)

### 15.1 List Harvest Reminders

**Endpoint:** `GET /harvest-reminders`

### 15.2 Create Harvest Reminder

**Endpoint:** `POST /harvest-reminders`

### 15.3 Update Harvest Reminder

**Endpoint:** `PUT /harvest-reminders/{id}`

### 15.4 Delete Harvest Reminder

**Endpoint:** `DELETE /harvest-reminders/{id}`

---

## 16. Reports

### 16.1 Harvest Report

Laporan panen berdasarkan periode waktu.

**Endpoint:** `GET /reports/harvest`

**Query Parameters:**
| Parameter | Type | Required | Description |
|-----------|------|----------|-------------|
| pondsId | string | No | Filter by pond ID |
| startDate | string | Yes | Tanggal mulai (YYYY-MM-DD) |
| endDate | string | Yes | Tanggal akhir (YYYY-MM-DD) |

**Response (200 OK):**
```json
{
  "success": true,
  "data": {
    "pondId": "string",
    "pondName": "string",
    "totalHarvests": 5,
    "totalFishCount": 20000,
    "totalWeight": 2000,
    "averageWeight": 0.1
  }
}
```

### 16.2 Finance Report

Laporan keuangan berdasarkan periode waktu.

**Endpoint:** `GET /reports/finance`

**Query Parameters:**
| Parameter | Type | Required | Description |
|-----------|------|----------|-------------|
| startDate | string | Yes | Tanggal mulai (YYYY-MM-DD) |
| endDate | string | Yes | Tanggal akhir (YYYY-MM-DD) |

**Response (200 OK):**
```json
{
  "success": true,
  "data": {
    "totalIncome": 50000000,
    "totalExpense": 15000000,
    "netProfit": 35000000,
    "incomeByCategory": {
      "Penjualan": 48000000,
      "Lainnya": 2000000
    },
    "expenseByCategory": {
      "Bibit": 5000000,
      "Pakan": 8000000,
      "Obat": 500000,
      "Listrik": 1500000
    }
  }
}
```

### 16.3 Fish Statistics

Statistik ikan berdasarkan periode waktu.

**Endpoint:** `GET /reports/fish-statistics`

**Query Parameters:**
| Parameter | Type | Required | Description |
|-----------|------|----------|-------------|
| startDate | string | Yes | Tanggal mulai (YYYY-MM-DD) |
| endDate | string | Yes | Tanggal akhir (YYYY-MM-DD) |

**Response (200 OK):**
```json
{
  "success": true,
  "data": {
    "totalPonds": 5,
    "totalSeedsStocked": 25500,
    "totalFishAlive": 24245,
    "totalFishDead": 1255
  }
}
```

### 16.4 Growth Chart

Data grafik pertumbuhan ikan.

**Endpoint:** `GET /reports/growth-chart`

**Query Parameters:**
| Parameter | Type | Required | Description |
|-----------|------|----------|-------------|
| pondsId | string | Yes | Pond ID |
| startDate | string | Yes | Tanggal mulai (YYYY-MM-DD) |
| endDate | string | Yes | Tanggal akhir (YYYY-MM-DD) |

**Response (200 OK):**
```json
{
  "success": true,
  "data": [
    {
      "pondId": "string",
      "date": "2024-01-22",
      "averageWidth": 1.5,
      "averageLength": 5.0
    },
    {
      "pondId": "string",
      "date": "2024-01-29",
      "averageWidth": 2.0,
      "averageLength": 7.0
    }
  ]
}
```

---

## Error Responses

Semua error response menggunakan format:

```json
{
  "success": false,
  "error": "Error message",
  "code": "ERROR_CODE"
}
```

### Common Error Codes

| HTTP Status | Code | Description |
|-------------|------|-------------|
| 400 | BAD_REQUEST | Request tidak valid |
| 401 | UNAUTHORIZED | Token tidak valid atau expired |
| 403 | FORBIDDEN | Tidak memiliki akses |
| 404 | NOT_FOUND | Resource tidak ditemukan |
| 409 | CONFLICT | Data duplikat |
| 422 | VALIDATION_ERROR | Validasi gagal |
| 500 | INTERNAL_ERROR | Server error |

---

## Data Types

### TransactionType
```typescript
type TransactionType = 'income' | 'expense';
```

### UserRole
```typescript
type UserRole = 'owner' | 'admin' | 'user';
```

### Date Format
Semua tanggal menggunakan format ISO 8601: `YYYY-MM-DD`

### Time Format
Semua waktu menggunakan format 24 jam: `HH:mm`

---

## Rate Limiting

- 100 requests per minute per user
- 1000 requests per hour per user

Header response:
```
X-RateLimit-Limit: 100
X-RateLimit-Remaining: 95
X-RateLimit-Reset: 1614556800
```
