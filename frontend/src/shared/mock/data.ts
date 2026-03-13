import type {
  User,
  Organization,
  Pond,
  Seed,
  FeedSchedule,
  GrowthRecord,
  DeathRecord,
  WaterQuality,
  DiseasesRecord,
  HarvestRecord,
  FinanceRecord,
  Supplier,
  Contact,
  FeedReminder,
  HarvestReminder,
  FishStatistics,
  GrowthChart,
} from '../types';

const ORG_ID = 'org-001';

export const mockOrganizations: Organization[] = [
  { id: ORG_ID, name: 'Lele Makmur Farm' },
  { id: 'org-002', name: 'Berkah Lele' },
];

export const mockUsers: User[] = [
  { id: 'user-001', name: 'Ahmad Budiman', email: 'ahmad@lele.com', organizationId: ORG_ID, role: 'owner' },
  { id: 'user-002', name: 'Budi Santoso', email: 'budi@lele.com', organizationId: ORG_ID, role: 'admin' },
  { id: 'user-003', name: 'Citra Dewi', email: 'citra@lele.com', organizationId: ORG_ID, role: 'user' },
  { id: 'user-004', name: 'Dedi Kurniawan', email: 'dedi@lele.com', organizationId: ORG_ID, role: 'user' },
];

export const mockPonds: Pond[] = [
  { id: 'pond-001', name: 'Kolam A1', capacity: 5000, location: 'Blok A - Utara', organizationId: ORG_ID },
  { id: 'pond-002', name: 'Kolam A2', capacity: 4500, location: 'Blok A - Selatan', organizationId: ORG_ID },
  { id: 'pond-003', name: 'Kolam B1', capacity: 6000, location: 'Blok B - Timur', organizationId: ORG_ID },
  { id: 'pond-004', name: 'Kolam B2', capacity: 5500, location: 'Blok B - Barat', organizationId: ORG_ID },
  { id: 'pond-005', name: 'Kolam C1', capacity: 7000, location: 'Blok C', organizationId: ORG_ID },
];

export const mockSeeds: Seed[] = [
  { id: 'seed-001', pondsId: 'pond-001', type: 'Lele Sangkuriang', quantity: 4500, stockDate: '2024-01-15', organizationId: ORG_ID },
  { id: 'seed-002', pondsId: 'pond-002', type: 'Lele Dumbo', quantity: 4000, stockDate: '2024-01-20', organizationId: ORG_ID },
  { id: 'seed-003', pondsId: 'pond-003', type: 'Lele Sangkuriang', quantity: 5500, stockDate: '2024-02-01', organizationId: ORG_ID },
  { id: 'seed-004', pondsId: 'pond-004', type: 'Lele Phyton', quantity: 5000, stockDate: '2024-02-10', organizationId: ORG_ID },
  { id: 'seed-005', pondsId: 'pond-005', type: 'Lele Mutiara', quantity: 6500, stockDate: '2024-02-15', organizationId: ORG_ID },
];

export const mockFeedSchedules: FeedSchedule[] = [
  { id: 'feed-001', pondsId: 'pond-001', feedType: 'PF-1000', feedAmount: 5, feedTime: '07:00', organizationId: ORG_ID },
  { id: 'feed-002', pondsId: 'pond-001', feedType: 'PF-1000', feedAmount: 5, feedTime: '12:00', organizationId: ORG_ID },
  { id: 'feed-003', pondsId: 'pond-001', feedType: 'PF-1000', feedAmount: 5, feedTime: '17:00', organizationId: ORG_ID },
  { id: 'feed-004', pondsId: 'pond-002', feedType: 'HI-PRO', feedAmount: 4, feedTime: '07:30', organizationId: ORG_ID },
  { id: 'feed-005', pondsId: 'pond-002', feedType: 'HI-PRO', feedAmount: 4, feedTime: '17:30', organizationId: ORG_ID },
  { id: 'feed-006', pondsId: 'pond-003', feedType: 'PF-500', feedAmount: 6, feedTime: '08:00', organizationId: ORG_ID },
];

export const mockGrowthRecords: GrowthRecord[] = [
  { id: 'growth-001', pondsId: 'pond-001', recordDate: '2024-01-22', averageWidth: 1.5, averageLength: 5.0, organizationId: ORG_ID },
  { id: 'growth-002', pondsId: 'pond-001', recordDate: '2024-01-29', averageWidth: 2.0, averageLength: 7.0, organizationId: ORG_ID },
  { id: 'growth-003', pondsId: 'pond-001', recordDate: '2024-02-05', averageWidth: 2.8, averageLength: 9.5, organizationId: ORG_ID },
  { id: 'growth-004', pondsId: 'pond-001', recordDate: '2024-02-12', averageWidth: 3.5, averageLength: 12.0, organizationId: ORG_ID },
  { id: 'growth-005', pondsId: 'pond-001', recordDate: '2024-02-19', averageWidth: 4.2, averageLength: 15.0, organizationId: ORG_ID },
  { id: 'growth-006', pondsId: 'pond-002', recordDate: '2024-01-27', averageWidth: 1.8, averageLength: 6.0, organizationId: ORG_ID },
  { id: 'growth-007', pondsId: 'pond-002', recordDate: '2024-02-03', averageWidth: 2.5, averageLength: 8.5, organizationId: ORG_ID },
];

export const mockDeathRecords: DeathRecord[] = [
  { id: 'death-001', pondsId: 'pond-001', recordDate: '2024-01-18', deathCount: 15, notes: 'Adaptasi awal', organizationId: ORG_ID },
  { id: 'death-002', pondsId: 'pond-001', recordDate: '2024-01-25', deathCount: 8, notes: 'Normal', organizationId: ORG_ID },
  { id: 'death-003', pondsId: 'pond-002', recordDate: '2024-01-23', deathCount: 20, notes: 'Cuaca panas', organizationId: ORG_ID },
  { id: 'death-004', pondsId: 'pond-003', recordDate: '2024-02-05', deathCount: 12, notes: 'Kualitas air menurun', organizationId: ORG_ID },
];

export const mockWaterQuality: WaterQuality[] = [
  { id: 'wq-001', pondsId: 'pond-001', recordDate: '2024-02-20', ph: 7.2, temperature: 28, dissolvedOxygen: 5.5, organizationId: ORG_ID },
  { id: 'wq-002', pondsId: 'pond-001', recordDate: '2024-02-19', ph: 7.0, temperature: 27, dissolvedOxygen: 5.8, organizationId: ORG_ID },
  { id: 'wq-003', pondsId: 'pond-002', recordDate: '2024-02-20', ph: 7.5, temperature: 29, dissolvedOxygen: 5.2, organizationId: ORG_ID },
  { id: 'wq-004', pondsId: 'pond-003', recordDate: '2024-02-20', ph: 6.8, temperature: 26, dissolvedOxygen: 6.0, organizationId: ORG_ID },
  { id: 'wq-005', pondsId: 'pond-004', recordDate: '2024-02-20', ph: 7.3, temperature: 28, dissolvedOxygen: 5.6, organizationId: ORG_ID },
];

export const mockDiseasesRecords: DiseasesRecord[] = [
  { 
    id: 'disease-001', 
    pondsId: 'pond-002', 
    diseaseName: 'White Spot', 
    symptoms: 'Bintik putih pada tubuh', 
    infectedFishCount: 50, 
    diseasesDate: '2024-02-10', 
    notes: 'Sudah diobati dengan garam', 
    organizationId: ORG_ID 
  },
  { 
    id: 'disease-002', 
    pondsId: 'pond-003', 
    diseaseName: 'Jamur', 
    symptoms: 'Lapisan putih seperti kapas', 
    infectedFishCount: 25, 
    diseasesDate: '2024-02-15', 
    notes: 'Ditangani dengan methylene blue', 
    organizationId: ORG_ID 
  },
];

export const mockHarvestRecords: HarvestRecord[] = [
  { 
    id: 'harvest-001', 
    pondsName: 'Kolam A1', 
    pondsId: 'pond-001', 
    harvestDate: '2024-03-15', 
    organizationId: ORG_ID, 
    harvestFishCount: 4200, 
    totalWeight: 420, 
    notes: 'Panen penuh, hasil bagus' 
  },
  { 
    id: 'harvest-002', 
    pondsName: 'Kolam A2', 
    pondsId: 'pond-002', 
    harvestDate: '2024-03-20', 
    organizationId: ORG_ID, 
    harvestFishCount: 3800, 
    totalWeight: 380, 
    notes: 'Ada sedikit yang mati sebelum panen' 
  },
  { 
    id: 'harvest-003', 
    pondsName: 'Kolam B1', 
    pondsId: 'pond-003', 
    harvestDate: '2024-04-01', 
    organizationId: ORG_ID, 
    harvestFishCount: 5200, 
    totalWeight: 520, 
    notes: 'Hasil memuaskan' 
  },
];

export const mockFinanceRecords: FinanceRecord[] = [
  { id: 'fin-001', transactionType: 'expense', transactionDate: '2024-01-15', amount: 2500000, category: 'Bibit', notes: 'Pembelian bibit Kolam A1', organizationId: ORG_ID },
  { id: 'fin-002', transactionType: 'expense', transactionDate: '2024-01-16', amount: 1500000, category: 'Pakan', notes: 'Pakan PF-1000 (10 sak)', organizationId: ORG_ID },
  { id: 'fin-003', transactionType: 'expense', transactionDate: '2024-02-01', amount: 500000, category: 'Obat', notes: 'Obat ikan', organizationId: ORG_ID },
  { id: 'fin-004', transactionType: 'expense', transactionDate: '2024-02-15', amount: 1500000, category: 'Pakan', notes: 'Pakan HI-PRO (8 sak)', organizationId: ORG_ID },
  { id: 'fin-005', transactionType: 'income', transactionDate: '2024-03-15', amount: 12600000, category: 'Penjualan', notes: 'Panen Kolam A1 - 420kg x 30rb', organizationId: ORG_ID },
  { id: 'fin-006', transactionType: 'income', transactionDate: '2024-03-20', amount: 11400000, category: 'Penjualan', notes: 'Panen Kolam A2 - 380kg x 30rb', organizationId: ORG_ID },
  { id: 'fin-007', transactionType: 'expense', transactionDate: '2024-03-25', amount: 300000, category: 'Listrik', notes: 'Tagihan listrik Maret', organizationId: ORG_ID },
  { id: 'fin-008', transactionType: 'income', transactionDate: '2024-04-01', amount: 15600000, category: 'Penjualan', notes: 'Panen Kolam B1 - 520kg x 30rb', organizationId: ORG_ID },
];

export const mockSuppliers: Supplier[] = [
  { id: 'sup-001', name: 'CV Maju Jaya', address: 'Jl. Raya Bogor No. 123', phone: '081234567890', email: 'majujaya@email.com', notes: 'Supplier bibit terpercaya', organizationId: ORG_ID },
  { id: 'sup-002', name: 'Toko Pakan Sejahtera', address: 'Jl. Pasar Minggu No. 45', phone: '082345678901', email: 'pakansejahtera@email.com', notes: 'Harga pakan kompetitif', organizationId: ORG_ID },
  { id: 'sup-003', name: 'PT Aqua Medika', address: 'Jl. Industri No. 78', phone: '083456789012', email: 'aquamedika@email.com', notes: 'Obat dan vitamin ikan', organizationId: ORG_ID },
];

export const mockContacts: Contact[] = [
  { id: 'contact-001', supplierId: 'sup-001', name: 'Pak Joko', address: 'Jl. Raya Bogor No. 123', phone: '081234567890', email: 'joko@majujaya.com', notes: 'Sales Manager' },
  { id: 'contact-002', supplierId: 'sup-001', name: 'Bu Siti', address: 'Jl. Raya Bogor No. 123', phone: '081234567891', email: 'siti@majujaya.com', notes: 'Admin' },
  { id: 'contact-003', supplierId: 'sup-002', name: 'Pak Bambang', address: 'Jl. Pasar Minggu No. 45', phone: '082345678901', email: 'bambang@pakansejahtera.com', notes: 'Owner' },
];

export const mockFeedReminders: FeedReminder[] = [
  { id: 'fr-001', pondsId: 'pond-001', reminderDate: '2024-02-21', reminderTime: '07:00', feedType: 'PF-1000', notes: 'Pemberian pagi', organizationId: ORG_ID },
  { id: 'fr-002', pondsId: 'pond-001', reminderDate: '2024-02-21', reminderTime: '17:00', feedType: 'PF-1000', notes: 'Pemberian sore', organizationId: ORG_ID },
  { id: 'fr-003', pondsId: 'pond-002', reminderDate: '2024-02-21', reminderTime: '07:30', feedType: 'HI-PRO', notes: 'Pemberian pagi', organizationId: ORG_ID },
];

export const mockHarvestReminders: HarvestReminder[] = [
  { id: 'hr-001', pondId: 'pond-004', reminderDate: '2024-04-10', reminderTime: '06:00', notes: 'Persiapan panen Kolam B2', organizationId: ORG_ID },
  { id: 'hr-002', pondId: 'pond-005', reminderDate: '2024-04-15', reminderTime: '06:00', notes: 'Cek kesiapan panen Kolam C1', organizationId: ORG_ID },
];

export const mockFishStatistics: FishStatistics = {
  pondId: '',
  totalPonds: 5,
  totalSeedsStocked: 25500,
  totalFishAlive: 24245,
  totalFishDead: 1255,
};

export const mockGrowthCharts: GrowthChart[] = [
  { pondId: 'pond-001', date: '2024-01-22', averageWidth: 1.5, averageLength: 5.0 },
  { pondId: 'pond-001', date: '2024-01-29', averageWidth: 2.0, averageLength: 7.0 },
  { pondId: 'pond-001', date: '2024-02-05', averageWidth: 2.8, averageLength: 9.5 },
  { pondId: 'pond-001', date: '2024-02-12', averageWidth: 3.5, averageLength: 12.0 },
  { pondId: 'pond-001', date: '2024-02-19', averageWidth: 4.2, averageLength: 15.0 },
  { pondId: 'pond-001', date: '2024-02-26', averageWidth: 5.0, averageLength: 18.0 },
  { pondId: 'pond-001', date: '2024-03-04', averageWidth: 5.8, averageLength: 21.0 },
  { pondId: 'pond-001', date: '2024-03-11', averageWidth: 6.5, averageLength: 24.0 },
];

export const mockDashboardStats = {
  totalPonds: 5,
  activePonds: 3,
  totalFish: 24245,
  todayFeedCount: 8,
  weeklyHarvest: 520,
  monthlyIncome: 39600000,
  monthlyExpense: 6300000,
  pendingReminders: 5,
};
