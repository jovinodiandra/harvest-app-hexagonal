// ============================================
// Domain Types for Ternak Lele Application
// ============================================

export interface Organization {
  id: string;
  name: string;
}

export interface User {
  id: string;
  name: string;
  email: string;
  organizationId: string;
  role: 'owner' | 'admin' | 'user';
}

export interface UserSession {
  userId: string;
  name: string;
  email: string;
  organizationId: string;
  organizationName: string;
  role: 'owner' | 'admin' | 'user';
  token: string;
}

export interface Pond {
  id: string;
  name: string;
  capacity: number;
  location: string;
  organizationId: string;
}

export interface Seed {
  id: string;
  pondsId: string;
  type: string;
  quantity: number;
  stockDate: string;
  organizationId: string;
}

export interface FeedSchedule {
  id: string;
  pondsId: string;
  feedType: string;
  feedAmount: number;
  feedTime: string;
  organizationId: string;
}

export interface GrowthRecord {
  id: string;
  pondsId: string;
  recordDate: string;
  averageWidth: number;
  averageLength: number;
  organizationId: string;
}

export interface DeathRecord {
  id: string;
  pondsId: string;
  recordDate: string;
  deathCount: number;
  notes: string;
  organizationId: string;
}

export interface WaterQuality {
  id: string;
  pondsId: string;
  recordDate: string;
  ph: number;
  temperature: number;
  dissolvedOxygen: number;
  organizationId: string;
}

export interface DiseasesRecord {
  id: string;
  pondsId: string;
  diseaseName: string;
  symptoms: string;
  infectedFishCount: number;
  diseasesDate: string;
  notes: string;
  organizationId: string;
}

export interface HarvestRecord {
  id: string;
  pondsName: string;
  pondsId: string;
  harvestDate: string;
  organizationId: string;
  harvestFishCount: number;
  totalWeight: number;
  notes: string;
}

export interface FinanceRecord {
  id: string;
  transactionType: 'income' | 'expense';
  transactionDate: string;
  amount: number;
  category: string;
  notes: string;
  organizationId: string;
}

export interface Supplier {
  id: string;
  name: string;
  address: string;
  phone: string;
  email: string;
  notes: string;
  organizationId: string;
}

export interface Contact {
  id: string;
  supplierId: string;
  name: string;
  address: string;
  phone: string;
  email: string;
  notes: string;
}

export interface FeedReminder {
  id: string;
  pondsId: string;
  reminderDate: string;
  reminderTime: string;
  feedType: string;
  notes: string;
  organizationId: string;
}

export interface HarvestReminder {
  id: string;
  pondId: string;
  reminderDate: string;
  reminderTime: string;
  notes: string;
  organizationId: string;
}

export interface FishStatistics {
  pondId: string;
  totalPonds: number;
  totalSeedsStocked: number;
  totalFishAlive: number;
  totalFishDead: number;
}

export interface GrowthChart {
  pondId: string;
  date: string;
  averageWidth: number;
  averageLength: number;
}

// API Response Types
export interface PaginatedResponse<T> {
  data: T[];
  page: number;
  limit: number;
  total: number;
  totalPages: number;
}

export interface ApiResponse<T> {
  success: boolean;
  data?: T;
  error?: string;
  message?: string;
}

// Form Types
export interface LoginForm {
  email: string;
  password: string;
}

export interface RegisterForm {
  name: string;
  email: string;
  password: string;
  organizationName: string;
}

// Report Types
export interface HarvestReport {
  pondId: string;
  pondName: string;
  totalHarvests: number;
  totalFishCount: number;
  totalWeight: number;
  averageWeight: number;
}

export interface FinanceReport {
  totalIncome: number;
  totalExpense: number;
  netProfit: number;
  incomeByCategory: Record<string, number>;
  expenseByCategory: Record<string, number>;
}
