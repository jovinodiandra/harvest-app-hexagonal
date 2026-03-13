import type { 
  ApiResponse, 
  PaginatedResponse,
  User,
  UserSession,
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
  LoginForm,
  RegisterForm,
} from '../types';
import { createPaginatedResponse } from './client';
import * as mockData from '../mock/data';

const delay = (ms: number = 300) => new Promise(resolve => setTimeout(resolve, ms));

function generateId(prefix: string): string {
  return `${prefix}-${Date.now()}-${Math.random().toString(36).substr(2, 9)}`;
}

// Mutable copies for CRUD operations
let users = [...mockData.mockUsers];
let ponds = [...mockData.mockPonds];
let seeds = [...mockData.mockSeeds];
let feedSchedules = [...mockData.mockFeedSchedules];
let growthRecords = [...mockData.mockGrowthRecords];
let deathRecords = [...mockData.mockDeathRecords];
let waterQuality = [...mockData.mockWaterQuality];
let diseasesRecords = [...mockData.mockDiseasesRecords];
let harvestRecords = [...mockData.mockHarvestRecords];
let financeRecords = [...mockData.mockFinanceRecords];
let suppliers = [...mockData.mockSuppliers];
let contacts = [...mockData.mockContacts];
let feedReminders = [...mockData.mockFeedReminders];
let harvestReminders = [...mockData.mockHarvestReminders];

export const mockService = {
  // Auth
  async login(form: LoginForm): Promise<ApiResponse<UserSession>> {
    await delay();
    const user = users.find(u => u.email === form.email);
    if (user && form.password === 'password123') {
      const session: UserSession = {
        userId: user.id,
        name: user.name,
        email: user.email,
        organizationId: user.organizationId,
        organizationName: 'Ternak Lele Farm',
        role: user.role,
        token: `mock-jwt-token-${Date.now()}`,
      };
      return { success: true, data: session };
    }
    return { success: false, error: 'Email atau password salah' };
  },

  async register(form: RegisterForm): Promise<ApiResponse<UserSession>> {
    await delay();
    const existingUser = users.find(u => u.email === form.email);
    if (existingUser) {
      return { success: false, error: 'Email sudah terdaftar' };
    }
    const newUser: User = {
      id: generateId('user'),
      name: form.name,
      email: form.email,
      organizationId: 'org-001',
      role: 'owner',
    };
    users.push(newUser);
    const session: UserSession = {
      userId: newUser.id,
      name: newUser.name,
      email: newUser.email,
      organizationId: newUser.organizationId,
      organizationName: form.organizationName,
      role: newUser.role,
      token: `mock-jwt-token-${Date.now()}`,
    };
    return { success: true, data: session };
  },

  // Users
  async getUsers(page = 1, limit = 10): Promise<ApiResponse<PaginatedResponse<User>>> {
    await delay();
    return { success: true, data: createPaginatedResponse(users, page, limit) };
  },

  async createUser(user: Omit<User, 'id'>): Promise<ApiResponse<User>> {
    await delay();
    const newUser: User = { ...user, id: generateId('user') };
    users.push(newUser);
    return { success: true, data: newUser };
  },

  async updateUser(id: string, data: Partial<User>): Promise<ApiResponse<User>> {
    await delay();
    const index = users.findIndex(u => u.id === id);
    if (index === -1) return { success: false, error: 'User not found' };
    users[index] = { ...users[index], ...data };
    return { success: true, data: users[index] };
  },

  async deleteUser(id: string): Promise<ApiResponse<void>> {
    await delay();
    users = users.filter(u => u.id !== id);
    return { success: true };
  },

  // Ponds
  async getPonds(page = 1, limit = 10): Promise<ApiResponse<PaginatedResponse<Pond>>> {
    await delay();
    return { success: true, data: createPaginatedResponse(ponds, page, limit) };
  },

  async getPondById(id: string): Promise<ApiResponse<Pond>> {
    await delay();
    const pond = ponds.find(p => p.id === id);
    if (!pond) return { success: false, error: 'Pond not found' };
    return { success: true, data: pond };
  },

  async createPond(pond: Omit<Pond, 'id'>): Promise<ApiResponse<Pond>> {
    await delay();
    const newPond: Pond = { ...pond, id: generateId('pond') };
    ponds.push(newPond);
    return { success: true, data: newPond };
  },

  async updatePond(id: string, data: Partial<Pond>): Promise<ApiResponse<Pond>> {
    await delay();
    const index = ponds.findIndex(p => p.id === id);
    if (index === -1) return { success: false, error: 'Pond not found' };
    ponds[index] = { ...ponds[index], ...data };
    return { success: true, data: ponds[index] };
  },

  async deletePond(id: string): Promise<ApiResponse<void>> {
    await delay();
    ponds = ponds.filter(p => p.id !== id);
    return { success: true };
  },

  // Seeds
  async getSeeds(page = 1, limit = 10, pondsId?: string): Promise<ApiResponse<PaginatedResponse<Seed>>> {
    await delay();
    let filtered = seeds;
    if (pondsId) filtered = seeds.filter(s => s.pondsId === pondsId);
    return { success: true, data: createPaginatedResponse(filtered, page, limit) };
  },

  async createSeed(seed: Omit<Seed, 'id'>): Promise<ApiResponse<Seed>> {
    await delay();
    const newSeed: Seed = { ...seed, id: generateId('seed') };
    seeds.push(newSeed);
    return { success: true, data: newSeed };
  },

  async updateSeed(id: string, data: Partial<Seed>): Promise<ApiResponse<Seed>> {
    await delay();
    const index = seeds.findIndex(s => s.id === id);
    if (index === -1) return { success: false, error: 'Seed not found' };
    seeds[index] = { ...seeds[index], ...data };
    return { success: true, data: seeds[index] };
  },

  async deleteSeed(id: string): Promise<ApiResponse<void>> {
    await delay();
    seeds = seeds.filter(s => s.id !== id);
    return { success: true };
  },

  // Feed Schedules
  async getFeedSchedules(page = 1, limit = 10, pondsId?: string): Promise<ApiResponse<PaginatedResponse<FeedSchedule>>> {
    await delay();
    let filtered = feedSchedules;
    if (pondsId) filtered = feedSchedules.filter(f => f.pondsId === pondsId);
    return { success: true, data: createPaginatedResponse(filtered, page, limit) };
  },

  async createFeedSchedule(schedule: Omit<FeedSchedule, 'id'>): Promise<ApiResponse<FeedSchedule>> {
    await delay();
    const newSchedule: FeedSchedule = { ...schedule, id: generateId('feed') };
    feedSchedules.push(newSchedule);
    return { success: true, data: newSchedule };
  },

  async updateFeedSchedule(id: string, data: Partial<FeedSchedule>): Promise<ApiResponse<FeedSchedule>> {
    await delay();
    const index = feedSchedules.findIndex(f => f.id === id);
    if (index === -1) return { success: false, error: 'Feed schedule not found' };
    feedSchedules[index] = { ...feedSchedules[index], ...data };
    return { success: true, data: feedSchedules[index] };
  },

  async deleteFeedSchedule(id: string): Promise<ApiResponse<void>> {
    await delay();
    feedSchedules = feedSchedules.filter(f => f.id !== id);
    return { success: true };
  },

  // Growth Records
  async getGrowthRecords(page = 1, limit = 10, pondsId?: string): Promise<ApiResponse<PaginatedResponse<GrowthRecord>>> {
    await delay();
    let filtered = growthRecords;
    if (pondsId) filtered = growthRecords.filter(g => g.pondsId === pondsId);
    return { success: true, data: createPaginatedResponse(filtered, page, limit) };
  },

  async createGrowthRecord(record: Omit<GrowthRecord, 'id'>): Promise<ApiResponse<GrowthRecord>> {
    await delay();
    const newRecord: GrowthRecord = { ...record, id: generateId('growth') };
    growthRecords.push(newRecord);
    return { success: true, data: newRecord };
  },

  async updateGrowthRecord(id: string, data: Partial<GrowthRecord>): Promise<ApiResponse<GrowthRecord>> {
    await delay();
    const index = growthRecords.findIndex(g => g.id === id);
    if (index === -1) return { success: false, error: 'Growth record not found' };
    growthRecords[index] = { ...growthRecords[index], ...data };
    return { success: true, data: growthRecords[index] };
  },

  async deleteGrowthRecord(id: string): Promise<ApiResponse<void>> {
    await delay();
    growthRecords = growthRecords.filter(g => g.id !== id);
    return { success: true };
  },

  // Death Records
  async getDeathRecords(page = 1, limit = 10, pondsId?: string): Promise<ApiResponse<PaginatedResponse<DeathRecord>>> {
    await delay();
    let filtered = deathRecords;
    if (pondsId) filtered = deathRecords.filter(d => d.pondsId === pondsId);
    return { success: true, data: createPaginatedResponse(filtered, page, limit) };
  },

  async createDeathRecord(record: Omit<DeathRecord, 'id'>): Promise<ApiResponse<DeathRecord>> {
    await delay();
    const newRecord: DeathRecord = { ...record, id: generateId('death') };
    deathRecords.push(newRecord);
    return { success: true, data: newRecord };
  },

  async updateDeathRecord(id: string, data: Partial<DeathRecord>): Promise<ApiResponse<DeathRecord>> {
    await delay();
    const index = deathRecords.findIndex(d => d.id === id);
    if (index === -1) return { success: false, error: 'Death record not found' };
    deathRecords[index] = { ...deathRecords[index], ...data };
    return { success: true, data: deathRecords[index] };
  },

  async deleteDeathRecord(id: string): Promise<ApiResponse<void>> {
    await delay();
    deathRecords = deathRecords.filter(d => d.id !== id);
    return { success: true };
  },

  // Water Quality
  async getWaterQuality(page = 1, limit = 10, pondsId?: string): Promise<ApiResponse<PaginatedResponse<WaterQuality>>> {
    await delay();
    let filtered = waterQuality;
    if (pondsId) filtered = waterQuality.filter(w => w.pondsId === pondsId);
    return { success: true, data: createPaginatedResponse(filtered, page, limit) };
  },

  async createWaterQuality(record: Omit<WaterQuality, 'id'>): Promise<ApiResponse<WaterQuality>> {
    await delay();
    const newRecord: WaterQuality = { ...record, id: generateId('wq') };
    waterQuality.push(newRecord);
    return { success: true, data: newRecord };
  },

  async updateWaterQuality(id: string, data: Partial<WaterQuality>): Promise<ApiResponse<WaterQuality>> {
    await delay();
    const index = waterQuality.findIndex(w => w.id === id);
    if (index === -1) return { success: false, error: 'Water quality record not found' };
    waterQuality[index] = { ...waterQuality[index], ...data };
    return { success: true, data: waterQuality[index] };
  },

  async deleteWaterQuality(id: string): Promise<ApiResponse<void>> {
    await delay();
    waterQuality = waterQuality.filter(w => w.id !== id);
    return { success: true };
  },

  // Diseases Records
  async getDiseasesRecords(page = 1, limit = 10, pondsId?: string): Promise<ApiResponse<PaginatedResponse<DiseasesRecord>>> {
    await delay();
    let filtered = diseasesRecords;
    if (pondsId) filtered = diseasesRecords.filter(d => d.pondsId === pondsId);
    return { success: true, data: createPaginatedResponse(filtered, page, limit) };
  },

  async createDiseasesRecord(record: Omit<DiseasesRecord, 'id'>): Promise<ApiResponse<DiseasesRecord>> {
    await delay();
    const newRecord: DiseasesRecord = { ...record, id: generateId('disease') };
    diseasesRecords.push(newRecord);
    return { success: true, data: newRecord };
  },

  async updateDiseasesRecord(id: string, data: Partial<DiseasesRecord>): Promise<ApiResponse<DiseasesRecord>> {
    await delay();
    const index = diseasesRecords.findIndex(d => d.id === id);
    if (index === -1) return { success: false, error: 'Disease record not found' };
    diseasesRecords[index] = { ...diseasesRecords[index], ...data };
    return { success: true, data: diseasesRecords[index] };
  },

  async deleteDiseasesRecord(id: string): Promise<ApiResponse<void>> {
    await delay();
    diseasesRecords = diseasesRecords.filter(d => d.id !== id);
    return { success: true };
  },

  // Harvest Records
  async getHarvestRecords(page = 1, limit = 10, pondsId?: string): Promise<ApiResponse<PaginatedResponse<HarvestRecord>>> {
    await delay();
    let filtered = harvestRecords;
    if (pondsId) filtered = harvestRecords.filter(h => h.pondsId === pondsId);
    return { success: true, data: createPaginatedResponse(filtered, page, limit) };
  },

  async createHarvestRecord(record: Omit<HarvestRecord, 'id'>): Promise<ApiResponse<HarvestRecord>> {
    await delay();
    const newRecord: HarvestRecord = { ...record, id: generateId('harvest') };
    harvestRecords.push(newRecord);
    return { success: true, data: newRecord };
  },

  async updateHarvestRecord(id: string, data: Partial<HarvestRecord>): Promise<ApiResponse<HarvestRecord>> {
    await delay();
    const index = harvestRecords.findIndex(h => h.id === id);
    if (index === -1) return { success: false, error: 'Harvest record not found' };
    harvestRecords[index] = { ...harvestRecords[index], ...data };
    return { success: true, data: harvestRecords[index] };
  },

  async deleteHarvestRecord(id: string): Promise<ApiResponse<void>> {
    await delay();
    harvestRecords = harvestRecords.filter(h => h.id !== id);
    return { success: true };
  },

  // Finance Records
  async getFinanceRecords(page = 1, limit = 10, type?: 'income' | 'expense'): Promise<ApiResponse<PaginatedResponse<FinanceRecord>>> {
    await delay();
    let filtered = financeRecords;
    if (type) filtered = financeRecords.filter(f => f.transactionType === type);
    return { success: true, data: createPaginatedResponse(filtered, page, limit) };
  },

  async createFinanceRecord(record: Omit<FinanceRecord, 'id'>): Promise<ApiResponse<FinanceRecord>> {
    await delay();
    const newRecord: FinanceRecord = { ...record, id: generateId('fin') };
    financeRecords.push(newRecord);
    return { success: true, data: newRecord };
  },

  async updateFinanceRecord(id: string, data: Partial<FinanceRecord>): Promise<ApiResponse<FinanceRecord>> {
    await delay();
    const index = financeRecords.findIndex(f => f.id === id);
    if (index === -1) return { success: false, error: 'Finance record not found' };
    financeRecords[index] = { ...financeRecords[index], ...data };
    return { success: true, data: financeRecords[index] };
  },

  async deleteFinanceRecord(id: string): Promise<ApiResponse<void>> {
    await delay();
    financeRecords = financeRecords.filter(f => f.id !== id);
    return { success: true };
  },

  // Suppliers
  async getSuppliers(page = 1, limit = 10): Promise<ApiResponse<PaginatedResponse<Supplier>>> {
    await delay();
    return { success: true, data: createPaginatedResponse(suppliers, page, limit) };
  },

  async createSupplier(supplier: Omit<Supplier, 'id'>): Promise<ApiResponse<Supplier>> {
    await delay();
    const newSupplier: Supplier = { ...supplier, id: generateId('sup') };
    suppliers.push(newSupplier);
    return { success: true, data: newSupplier };
  },

  async updateSupplier(id: string, data: Partial<Supplier>): Promise<ApiResponse<Supplier>> {
    await delay();
    const index = suppliers.findIndex(s => s.id === id);
    if (index === -1) return { success: false, error: 'Supplier not found' };
    suppliers[index] = { ...suppliers[index], ...data };
    return { success: true, data: suppliers[index] };
  },

  async deleteSupplier(id: string): Promise<ApiResponse<void>> {
    await delay();
    suppliers = suppliers.filter(s => s.id !== id);
    return { success: true };
  },

  // Contacts
  async getContacts(page = 1, limit = 10, supplierId?: string): Promise<ApiResponse<PaginatedResponse<Contact>>> {
    await delay();
    let filtered = contacts;
    if (supplierId) filtered = contacts.filter(c => c.supplierId === supplierId);
    return { success: true, data: createPaginatedResponse(filtered, page, limit) };
  },

  async createContact(contact: Omit<Contact, 'id'>): Promise<ApiResponse<Contact>> {
    await delay();
    const newContact: Contact = { ...contact, id: generateId('contact') };
    contacts.push(newContact);
    return { success: true, data: newContact };
  },

  async updateContact(id: string, data: Partial<Contact>): Promise<ApiResponse<Contact>> {
    await delay();
    const index = contacts.findIndex(c => c.id === id);
    if (index === -1) return { success: false, error: 'Contact not found' };
    contacts[index] = { ...contacts[index], ...data };
    return { success: true, data: contacts[index] };
  },

  async deleteContact(id: string): Promise<ApiResponse<void>> {
    await delay();
    contacts = contacts.filter(c => c.id !== id);
    return { success: true };
  },

  // Feed Reminders
  async getFeedReminders(page = 1, limit = 10): Promise<ApiResponse<PaginatedResponse<FeedReminder>>> {
    await delay();
    return { success: true, data: createPaginatedResponse(feedReminders, page, limit) };
  },

  async createFeedReminder(reminder: Omit<FeedReminder, 'id'>): Promise<ApiResponse<FeedReminder>> {
    await delay();
    const newReminder: FeedReminder = { ...reminder, id: generateId('fr') };
    feedReminders.push(newReminder);
    return { success: true, data: newReminder };
  },

  async updateFeedReminder(id: string, data: Partial<FeedReminder>): Promise<ApiResponse<FeedReminder>> {
    await delay();
    const index = feedReminders.findIndex(f => f.id === id);
    if (index === -1) return { success: false, error: 'Feed reminder not found' };
    feedReminders[index] = { ...feedReminders[index], ...data };
    return { success: true, data: feedReminders[index] };
  },

  async deleteFeedReminder(id: string): Promise<ApiResponse<void>> {
    await delay();
    feedReminders = feedReminders.filter(f => f.id !== id);
    return { success: true };
  },

  // Harvest Reminders
  async getHarvestReminders(page = 1, limit = 10): Promise<ApiResponse<PaginatedResponse<HarvestReminder>>> {
    await delay();
    return { success: true, data: createPaginatedResponse(harvestReminders, page, limit) };
  },

  async createHarvestReminder(reminder: Omit<HarvestReminder, 'id'>): Promise<ApiResponse<HarvestReminder>> {
    await delay();
    const newReminder: HarvestReminder = { ...reminder, id: generateId('hr') };
    harvestReminders.push(newReminder);
    return { success: true, data: newReminder };
  },

  async updateHarvestReminder(id: string, data: Partial<HarvestReminder>): Promise<ApiResponse<HarvestReminder>> {
    await delay();
    const index = harvestReminders.findIndex(h => h.id === id);
    if (index === -1) return { success: false, error: 'Harvest reminder not found' };
    harvestReminders[index] = { ...harvestReminders[index], ...data };
    return { success: true, data: harvestReminders[index] };
  },

  async deleteHarvestReminder(id: string): Promise<ApiResponse<void>> {
    await delay();
    harvestReminders = harvestReminders.filter(h => h.id !== id);
    return { success: true };
  },

  // Dashboard & Reports
  async getDashboardStats(): Promise<ApiResponse<typeof mockData.mockDashboardStats>> {
    await delay();
    return { success: true, data: mockData.mockDashboardStats };
  },

  async getFishStatistics(): Promise<ApiResponse<typeof mockData.mockFishStatistics>> {
    await delay();
    return { success: true, data: mockData.mockFishStatistics };
  },

  async getGrowthCharts(pondsId: string): Promise<ApiResponse<typeof mockData.mockGrowthCharts>> {
    await delay();
    const filtered = mockData.mockGrowthCharts.filter(g => g.pondId === pondsId);
    return { success: true, data: filtered };
  },

  // Get all ponds without pagination (for dropdowns)
  async getAllPonds(): Promise<ApiResponse<Pond[]>> {
    await delay();
    return { success: true, data: ponds };
  },
};
