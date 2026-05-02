import type {
  ApiResponse,
  PaginatedResponse,
  User,
  UserSession,
  Pond,
  Seed,
  FeedSchedule,
  FeedPrice,
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
  FishStatistics,
  GrowthChart,
  HarvestReport,
  FinanceReport,
} from '../types';
import { apiGet, apiPost, apiPut, apiDelete } from './client';

interface BackendResponse<T> {
  success: boolean;
  message: string;
  data: T;
}

interface LoginResponse {
  accessToken: string;
  tokenType: string;
  user: {
    userId: string;
    name: string;
    email: string;
    role: string;
  };
  organization: {
    organizationId: string;
    organizationName: string;
  };
}

interface RegisterResponse {
  userId: string;
  organizationId: string;
  name: string;
  email: string;
  organizationName: string;
  role: string;
  createdAt: string;
}

function mapRole(role: string): 'owner' | 'admin' | 'user' {
  if (!role) return 'user';
  const normalized = role.toLowerCase();
  if (normalized === 'owner') return 'owner';
  if (normalized === 'admin') return 'admin';
  if (normalized === 'staff' || normalized === 'user') return 'user';
  return 'user';
}

function extractBackendData<T>(response: ApiResponse<BackendResponse<T>>): T | null {
  if (!response.success || !response.data) {
    return null;
  }

  return (response.data.data ?? response.data) as T;
}

export const apiService = {
  // ==================== AUTH ====================
  async login(form: LoginForm): Promise<ApiResponse<UserSession>> {
    const response = await apiPost<BackendResponse<LoginResponse>>('/auth/login', form);
    
    if (response.success && response.data?.data) {
      const loginData = response.data.data;
      const session: UserSession = {
        userId: loginData.user.userId,
        name: loginData.user.name,
        email: loginData.user.email,
        organizationId: loginData.organization.organizationId,
        organizationName: loginData.organization.organizationName,
        role: mapRole(loginData.user.role),
        token: loginData.accessToken,
      };
      return { success: true, data: session };
    }
    
    return { success: false, error: response.error || 'Login gagal' };
  },

  async register(form: RegisterForm): Promise<ApiResponse<UserSession>> {
    const response = await apiPost<BackendResponse<RegisterResponse>>('/auth/register', {
      name: form.name,
      email: form.email,
      password: form.password,
      confirmPassword: form.password,
      organizationName: form.organizationName,
    });
    
    if (response.success && response.data?.data) {
      const loginResponse = await this.login({ email: form.email, password: form.password });
      return loginResponse;
    }
    
    return { success: false, error: response.error || 'Registrasi gagal' };
  },

  // ==================== USERS ====================
  async getUsers(page = 1, limit = 10): Promise<ApiResponse<PaginatedResponse<User>>> {
    const timestamp = Date.now();
    const response = await apiGet<BackendResponse<{ users: any[] }>>(`/users?page=${page}&limit=${limit}&_t=${timestamp}`);
    
    if (response.success && response.data?.data) {
      const users = (response.data.data.users || []).map((u: any) => ({
        id: u.id,
        name: u.name,
        email: u.email,
        organizationId: '',
        role: mapRole(u.role),
      }));
      return {
        success: true,
        data: {
          data: users,
          page,
          limit,
          total: users.length,
          totalPages: Math.ceil(users.length / limit),
        },
      };
    }
    
    return { success: false, error: response.error || response.data?.message || 'Terjadi kesalahan' };
  },

  async createUser(user: Omit<User, 'id'> & { password: string }): Promise<ApiResponse<User>> {
    const response = await apiPost<BackendResponse<any>>('/users', {
      name: user.name,
      email: user.email,
      password: user.password,
    });
    
    if (response.success && response.data?.data) {
      const u = response.data.data;
      return {
        success: true,
        data: {
          id: u.id,
          name: u.name,
          email: u.email,
          organizationId: '',
          role: mapRole(u.role),
        },
      };
    }
    
    return { success: false, error: response.error || response.data?.message || 'Terjadi kesalahan' };
  },

  async updateUser(id: string, data: Partial<User>): Promise<ApiResponse<User>> {
    const response = await apiPut<BackendResponse<any>>(`/users/${id}`, {
      name: data.name,
      role: data.role?.toUpperCase(),
    });
    
    if (response.success) {
      const u = response.data?.data || response.data;
      if (u && u.id) {
        return {
          success: true,
          data: {
            id: u.id,
            name: u.name,
            email: u.email,
            organizationId: '',
            role: mapRole(u.role),
          },
        };
      }
    }
    
    return { success: false, error: response.error || response.data?.message || 'Terjadi kesalahan' };
  },

  async deleteUser(id: string): Promise<ApiResponse<void>> {
    const response = await apiDelete<BackendResponse<void>>(`/users/${id}`);
    return { success: response.success, error: response.error };
  },

  // ==================== PONDS ====================
  async getPonds(page = 1, limit = 10): Promise<ApiResponse<PaginatedResponse<Pond>>> {
    const response = await apiGet<BackendResponse<{ ponds: any[] }>>(`/ponds?page=${page}&limit=${limit}`);
    
    if (response.success && response.data?.data) {
      const ponds = (response.data.data.ponds || []).map((p: any) => ({
        id: p.id,
        name: p.name,
        capacity: p.capacity,
        location: p.location,
        organizationId: '',
      }));
      return {
        success: true,
        data: {
          data: ponds,
          page,
          limit,
          total: ponds.length,
          totalPages: Math.ceil(ponds.length / limit),
        },
      };
    }
    
    return { success: false, error: response.error || response.data?.message || 'Terjadi kesalahan' };
  },

  async getAllPonds(): Promise<ApiResponse<Pond[]>> {
    const response = await apiGet<BackendResponse<{ ponds: any[] }>>('/ponds?page=1&limit=100');
    
    if (response.success && response.data?.data) {
      const ponds = (response.data.data.ponds || []).map((p: any) => ({
        id: p.id,
        name: p.name,
        capacity: p.capacity,
        location: p.location,
        organizationId: '',
      }));
      return { success: true, data: ponds };
    }
    
    return { success: false, error: response.error || response.data?.message || 'Terjadi kesalahan' };
  },

  async createPond(pond: Omit<Pond, 'id'>): Promise<ApiResponse<Pond>> {
    const response = await apiPost<BackendResponse<any>>('/ponds', {
      name: pond.name,
      location: pond.location,
      capacity: pond.capacity,
    });
    
    if (response.success && response.data?.data) {
      const p = response.data.data;
      return {
        success: true,
        data: {
          id: p.id,
          name: p.name,
          capacity: p.capacity,
          location: p.location,
          organizationId: '',
        },
      };
    }
    
    return { success: false, error: response.error || response.data?.message || 'Terjadi kesalahan' };
  },

  async updatePond(id: string, data: Partial<Pond>): Promise<ApiResponse<Pond>> {
    const response = await apiPut<BackendResponse<any>>(`/ponds/${id}`, {
      name: data.name,
      location: data.location,
      capacity: data.capacity,
    });
    
    if (response.success) {
      const p = response.data?.data || response.data;
      if (p && p.id) {
        return {
          success: true,
          data: {
            id: p.id,
            name: p.name,
            capacity: p.capacity,
            location: p.location,
            organizationId: '',
          },
        };
      }
    }
    
    return { success: false, error: response.error || response.data?.message || 'Terjadi kesalahan' };
  },

  async deletePond(id: string): Promise<ApiResponse<void>> {
    const response = await apiDelete<BackendResponse<void>>(`/ponds/${id}`);
    return { success: response.success, error: response.error };
  },

  // ==================== SEEDS ====================
  async getSeeds(page = 1, limit = 10): Promise<ApiResponse<PaginatedResponse<Seed>>> {
    const response = await apiGet<BackendResponse<{ seeds: any[] }>>(`/seeds?page=${page}&limit=${limit}`);
    
    if (response.success && response.data?.data) {
      const seeds = (response.data.data.seeds || []).map((s: any) => ({
        id: s.id,
        pondsId: s.pondId || s.pondsId,
        type: s.type,
        quantity: s.quantity,
        stockDate: s.stockDate,
        organizationId: '',
      }));
      return {
        success: true,
        data: {
          data: seeds,
          page,
          limit,
          total: seeds.length,
          totalPages: Math.ceil(seeds.length / limit),
        },
      };
    }
    
    return { success: false, error: response.error || response.data?.message || 'Terjadi kesalahan' };
  },

  async createSeed(seed: Omit<Seed, 'id'>): Promise<ApiResponse<Seed>> {
    const response = await apiPost<BackendResponse<any>>('/seeds', {
      pondsId: seed.pondsId,
      type: seed.type,
      quantity: seed.quantity,
      stockDate: seed.stockDate,
    });
    
    if (response.success && response.data?.data) {
      const s = response.data.data;
      return {
        success: true,
        data: {
          id: s.id,
          pondsId: s.pondId || s.pondsId,
          type: s.type,
          quantity: s.quantity,
          stockDate: s.stockDate,
          organizationId: '',
        },
      };
    }
    
    return { success: false, error: response.error || response.data?.message || 'Terjadi kesalahan' };
  },

  async updateSeed(id: string, data: Partial<Seed>): Promise<ApiResponse<Seed>> {
    const response = await apiPut<BackendResponse<any>>(`/seeds/${id}`, {
      pondId: data.pondsId,
      type: data.type,
      quantity: data.quantity,
      stockDate: data.stockDate,
    });
    
    if (response.success) {
      const s = response.data?.data || response.data;
      if (s && s.id) {
        return {
          success: true,
          data: {
            id: s.id,
            pondsId: s.pondId || s.pondsId,
            type: s.type,
            quantity: s.quantity,
            stockDate: s.stockDate,
            organizationId: '',
          },
        };
      }
    }
    
    return { success: false, error: response.error || response.data?.message || 'Terjadi kesalahan' };
  },

  async deleteSeed(id: string): Promise<ApiResponse<void>> {
    const response = await apiDelete<BackendResponse<void>>(`/seeds/${id}`);
    return { success: response.success, error: response.error };
  },

  // ==================== FEED SCHEDULES ====================
  async getFeedSchedules(page = 1, limit = 10): Promise<ApiResponse<PaginatedResponse<FeedSchedule>>> {
    const response = await apiGet<BackendResponse<{ feedSchedules: any[] }>>(`/feed-schedules?page=${page}&limit=${limit}`);
    
    if (response.success && response.data?.data) {
      const schedules = (response.data.data.feedSchedules || []).map((f: any) => ({
        id: f.id,
        pondsId: f.pondsId,
        feedType: f.feedType,
        feedAmount: f.feedAmount,
        feedTime: f.feedTime,
        organizationId: '',
      }));
      return {
        success: true,
        data: {
          data: schedules,
          page,
          limit,
          total: schedules.length,
          totalPages: Math.ceil(schedules.length / limit),
        },
      };
    }
    
    return { success: false, error: response.error || response.data?.message || 'Terjadi kesalahan' };
  },

  async createFeedSchedule(schedule: Omit<FeedSchedule, 'id'>): Promise<ApiResponse<FeedSchedule>> {
    const response = await apiPost<BackendResponse<any>>('/feed-schedules', {
      pondsId: schedule.pondsId,
      feedType: schedule.feedType,
      feedAmount: schedule.feedAmount,
      feedTime: schedule.feedTime,
    });
    
    if (response.success && response.data?.data) {
      const f = response.data.data;
      return {
        success: true,
        data: {
          id: f.id,
          pondsId: f.pondsId,
          feedType: f.feedType,
          feedAmount: f.feedAmount,
          feedTime: f.feedTime,
          organizationId: '',
        },
      };
    }
    
    return { success: false, error: response.error || response.data?.message || 'Terjadi kesalahan' };
  },

  async updateFeedSchedule(id: string, data: Partial<FeedSchedule>): Promise<ApiResponse<FeedSchedule>> {
    const response = await apiPut<BackendResponse<any>>(`/feed-schedules/${id}`, {
      pondsId: data.pondsId,
      feedType: data.feedType,
      feedAmount: data.feedAmount,
      feedTime: data.feedTime,
    });
    
    if (response.success) {
      const f = response.data?.data || response.data;
      if (f && f.id) {
        return {
          success: true,
          data: {
            id: f.id,
            pondsId: f.pondsId,
            feedType: f.feedType,
            feedAmount: f.feedAmount,
            feedTime: f.feedTime,
            organizationId: '',
          },
        };
      }
    }
    
    return { success: false, error: response.error || response.data?.message || 'Terjadi kesalahan' };
  },

  async deleteFeedSchedule(id: string): Promise<ApiResponse<void>> {
    const response = await apiDelete<BackendResponse<void>>(`/feed-schedules/${id}`);
    return { success: response.success, error: response.error };
  },

  async getFeedPrices(
    page = 1,
    limit = 10,
    status: 'ACTIVE' | 'NONACTIVE' | 'ALL' = 'ACTIVE'
  ): Promise<ApiResponse<PaginatedResponse<FeedPrice>>> {
    const response = await apiGet<BackendResponse<{ feedPrices?: any[] }>>(
      `/feed-prices?page=${page}&limit=${limit}&status=${status}`
    );

    const dataPayload = extractBackendData<{ feedPrices?: any[]; data?: any[] }>(response);
    const rawFeedPrices = Array.isArray(dataPayload)
      ? dataPayload
      : dataPayload?.feedPrices ?? dataPayload?.data ?? [];

    if (response.success && rawFeedPrices) {
      const feedPrices = (rawFeedPrices || []).map((f: any) => ({
        id: f.id,
        feedName: f.feedName,
        pricePerKiloGram: f.pricePerKiloGram,
        effectiveDate: f.effectiveDate,
        status: (f.status || 'ACTIVE') as 'ACTIVE' | 'NONACTIVE',
        description: f.description || '',
        createdAt: f.createdAt,
        organizationId: '',
      }));
      return {
        success: true,
        data: {
          data: feedPrices,
          page,
          limit,
          total: feedPrices.length,
          totalPages: Math.ceil(feedPrices.length / limit),
        },
      };
    }

    return { success: false, error: response.error || response.data?.message || 'Terjadi kesalahan' };
  },

  async createFeedPrice(feedPrice: Omit<FeedPrice, 'id'>): Promise<ApiResponse<FeedPrice>> {
    const response = await apiPost<BackendResponse<any>>('/feed-prices', {
      feedName: feedPrice.feedName,
      pricePerKiloGram: feedPrice.pricePerKiloGram,
      effectiveDate: feedPrice.effectiveDate,
      description: feedPrice.description,
    });

    const f = extractBackendData<any>(response);
    if (response.success && f) {
      return {
        success: true,
        data: {
          id: f.id,
          feedName: f.feedName,
          pricePerKiloGram: f.pricePerKiloGram,
          effectiveDate: f.effectiveDate,
          status: (f.status || 'ACTIVE') as 'ACTIVE' | 'NONACTIVE',
          description: f.description || '',
          createdAt: f.createdAt,
          organizationId: '',
        },
      };
    }

    return { success: false, error: response.error || response.data?.message || 'Terjadi kesalahan' };
  },

  async updateFeedPrice(id: string, data: Partial<FeedPrice>): Promise<ApiResponse<FeedPrice>> {
    const response = await apiPut<BackendResponse<any>>(`/feed-prices/${id}`, {
      feedName: data.feedName,
      pricePerKiloGram: data.pricePerKiloGram,
      effectiveDate: data.effectiveDate,
      description: data.description,
    });

    if (response.success) {
      const f = extractBackendData<any>(response);
      if (f && f.id) {
        return {
          success: true,
          data: {
            id: f.id,
            feedName: f.feedName,
            pricePerKiloGram: f.pricePerKiloGram,
            effectiveDate: f.effectiveDate,
            status: (f.status || 'ACTIVE') as 'ACTIVE' | 'NONACTIVE',
            description: f.description || '',
            createdAt: f.createdAt,
            organizationId: '',
          },
        };
      }
    }

    return { success: false, error: response.error || response.data?.message || 'Terjadi kesalahan' };
  },

  async deleteFeedPrice(id: string): Promise<ApiResponse<void>> {
    const response = await apiDelete<BackendResponse<void>>(`/feed-prices/${id}`);
    return { success: response.success, error: response.error };
  },

  // ==================== GROWTH RECORDS ====================
  async getGrowthRecords(page = 1, limit = 10, pondsId?: string): Promise<ApiResponse<PaginatedResponse<GrowthRecord>>> {
    if (!pondsId) {
      return { success: false, error: 'pondsId is required' };
    }
    
    const response = await apiGet<BackendResponse<{ growthRecords: any[] }>>(`/growth-records?pondsId=${pondsId}&page=${page}&limit=${limit}`);
    
    if (response.success && response.data?.data) {
      const records = (response.data.data.growthRecords || []).map((g: any) => ({
        id: g.id,
        pondsId: g.pondsId,
        recordDate: g.recordDate,
        averageWidth: g.averageWeight || 0,
        averageLength: g.averageLength,
        organizationId: '',
      }));
      return {
        success: true,
        data: {
          data: records,
          page,
          limit,
          total: records.length,
          totalPages: Math.ceil(records.length / limit),
        },
      };
    }
    
    return { success: false, error: response.error || response.data?.message || 'Terjadi kesalahan' };
  },

  async createGrowthRecord(record: Omit<GrowthRecord, 'id'>): Promise<ApiResponse<GrowthRecord>> {
    const response = await apiPost<BackendResponse<any>>('/growth-records', {
      pondsId: record.pondsId,
      recordDate: record.recordDate,
      averageLength: record.averageLength,
      averageWeight: record.averageWidth,
    });
    
    if (response.success && response.data?.data) {
      const g = response.data.data;
      return {
        success: true,
        data: {
          id: g.id,
          pondsId: g.pondsId,
          recordDate: g.recordDate,
          averageWidth: g.averageWeight,
          averageLength: g.averageLength,
          organizationId: '',
        },
      };
    }
    
    return { success: false, error: response.error || response.data?.message || 'Terjadi kesalahan' };
  },

  async updateGrowthRecord(id: string, data: Partial<GrowthRecord>): Promise<ApiResponse<GrowthRecord>> {
    const response = await apiPut<BackendResponse<any>>(`/growth-records/${id}`, {
      pondsId: data.pondsId,
      recordDate: data.recordDate,
      averageLength: data.averageLength,
      averageWeight: data.averageWidth,
    });
    
    if (response.success) {
      const g = response.data?.data || response.data;
      if (g && g.id) {
        return {
          success: true,
          data: {
            id: g.id,
            pondsId: g.pondsId,
            recordDate: g.recordDate,
            averageWidth: g.averageWeight,
            averageLength: g.averageLength,
            organizationId: '',
          },
        };
      }
    }
    
    return { success: false, error: response.error || response.data?.message || 'Terjadi kesalahan' };
  },

  async deleteGrowthRecord(id: string): Promise<ApiResponse<void>> {
    const response = await apiDelete<BackendResponse<void>>(`/growth-records/${id}`);
    return { success: response.success, error: response.error };
  },

  // ==================== DEATH RECORDS ====================
  async getDeathRecords(page = 1, limit = 10, pondsId?: string): Promise<ApiResponse<PaginatedResponse<DeathRecord>>> {
    if (!pondsId) {
      return { success: false, error: 'pondsId is required' };
    }
    
    const response = await apiGet<BackendResponse<{ deathRecords: any[] }>>(`/death-records?pondsId=${pondsId}&page=${page}&limit=${limit}`);
    
    if (response.success && response.data?.data) {
      const records = (response.data.data.deathRecords || []).map((d: any) => ({
        id: d.id,
        pondsId: d.pondsId,
        recordDate: d.recordDate,
        deathCount: d.deathCount,
        notes: d.notes || '',
        organizationId: '',
      }));
      return {
        success: true,
        data: {
          data: records,
          page,
          limit,
          total: records.length,
          totalPages: Math.ceil(records.length / limit),
        },
      };
    }
    
    return { success: false, error: response.error || response.data?.message || 'Terjadi kesalahan' };
  },

  async createDeathRecord(record: Omit<DeathRecord, 'id'>): Promise<ApiResponse<DeathRecord>> {
    const response = await apiPost<BackendResponse<any>>('/death-records', {
      pondsId: record.pondsId,
      recordDate: record.recordDate,
      deathCount: record.deathCount,
      notes: record.notes,
    });
    
    if (response.success && response.data?.data) {
      const d = response.data.data;
      return {
        success: true,
        data: {
          id: d.id,
          pondsId: d.pondsId,
          recordDate: d.recordDate,
          deathCount: d.deathCount,
          notes: d.notes || '',
          organizationId: '',
        },
      };
    }
    
    return { success: false, error: response.error || response.data?.message || 'Terjadi kesalahan' };
  },

  async updateDeathRecord(id: string, data: Partial<DeathRecord>): Promise<ApiResponse<DeathRecord>> {
    const response = await apiPut<BackendResponse<any>>(`/death-records/${id}`, {
      pondsId: data.pondsId,
      recordDate: data.recordDate,
      deathCount: data.deathCount,
      notes: data.notes,
    });
    
    if (response.success) {
      const d = response.data?.data || response.data;
      if (d && d.id) {
        return {
          success: true,
          data: {
            id: d.id,
            pondsId: d.pondsId,
            recordDate: d.recordDate,
            deathCount: d.deathCount,
            notes: d.notes || '',
            organizationId: '',
          },
        };
      }
    }
    
    return { success: false, error: response.error || response.data?.message || 'Terjadi kesalahan' };
  },

  async deleteDeathRecord(id: string): Promise<ApiResponse<void>> {
    const response = await apiDelete<BackendResponse<void>>(`/death-records/${id}`);
    return { success: response.success, error: response.error };
  },

  // ==================== WATER QUALITY ====================
  async getWaterQuality(page = 1, limit = 10, pondsId?: string): Promise<ApiResponse<PaginatedResponse<WaterQuality>>> {
    if (!pondsId) {
      return { success: false, error: 'pondsId is required' };
    }
    
    const response = await apiGet<BackendResponse<{ watterQualities: any[] }>>(`/watter-quality?pondsId=${pondsId}&page=${page}&limit=${limit}`);
    
    if (response.success && response.data?.data) {
      const records = (response.data.data.watterQualities || []).map((w: any) => ({
        id: w.id,
        pondsId: w.pondsId,
        recordDate: w.recordDate,
        ph: w.ph,
        temperature: w.temperature,
        dissolvedOxygen: w.dissolvedOxygen,
        organizationId: '',
      }));
      return {
        success: true,
        data: {
          data: records,
          page,
          limit,
          total: records.length,
          totalPages: Math.ceil(records.length / limit),
        },
      };
    }
    
    return { success: false, error: response.error || response.data?.message || 'Terjadi kesalahan' };
  },

  async createWaterQuality(record: Omit<WaterQuality, 'id'>): Promise<ApiResponse<WaterQuality>> {
    const response = await apiPost<BackendResponse<any>>('/watter-quality', {
      pondsId: record.pondsId,
      recordDate: record.recordDate,
      ph: record.ph,
      temperature: record.temperature,
      dissolvedOxygen: record.dissolvedOxygen,
    });
    
    if (response.success && response.data?.data) {
      const w = response.data.data;
      return {
        success: true,
        data: {
          id: w.id,
          pondsId: w.pondsId,
          recordDate: w.recordDate,
          ph: w.ph,
          temperature: w.temperature,
          dissolvedOxygen: w.dissolvedOxygen,
          organizationId: '',
        },
      };
    }
    
    return { success: false, error: response.error || response.data?.message || 'Terjadi kesalahan' };
  },

  async updateWaterQuality(id: string, data: Partial<WaterQuality>): Promise<ApiResponse<WaterQuality>> {
    const response = await apiPut<BackendResponse<any>>(`/watter-quality/${id}`, {
      pondsId: data.pondsId,
      recordDate: data.recordDate,
      ph: data.ph,
      temperature: data.temperature,
      dissolvedOxygen: data.dissolvedOxygen,
    });
    
    if (response.success) {
      const w = response.data?.data || response.data;
      if (w && w.id) {
        return {
          success: true,
          data: {
            id: w.id,
            pondsId: w.pondsId,
            recordDate: w.recordDate,
            ph: w.ph,
            temperature: w.temperature,
            dissolvedOxygen: w.dissolvedOxygen,
            organizationId: '',
          },
        };
      }
    }
    
    return { success: false, error: response.error || response.data?.message || 'Terjadi kesalahan' };
  },

  async deleteWaterQuality(id: string): Promise<ApiResponse<void>> {
    const response = await apiDelete<BackendResponse<void>>(`/watter-quality/${id}`);
    return { success: response.success, error: response.error };
  },

  // ==================== DISEASES RECORDS ====================
  async getDiseasesRecords(page = 1, limit = 10, pondId?: string): Promise<ApiResponse<PaginatedResponse<DiseasesRecord>>> {
    if (!pondId) {
      return { success: false, error: 'pondId is required' };
    }
    
    const response = await apiGet<BackendResponse<{ records: any[] }>>(`/diseases-records?pondId=${pondId}&page=${page}&limit=${limit}`);
    
    if (response.success && response.data?.data) {
      const records = (response.data.data.records || []).map((d: any) => ({
        id: d.id,
        pondsId: d.pondId || d.pondsId,
        diseaseName: d.diseaseName,
        symptoms: d.symptoms,
        infectedFishCount: d.infectedFishCount,
        diseasesDate: d.diseaseDate || d.diseasesDate,
        notes: d.notes || '',
        organizationId: '',
      }));
      return {
        success: true,
        data: {
          data: records,
          page,
          limit,
          total: records.length,
          totalPages: Math.ceil(records.length / limit),
        },
      };
    }
    
    return { success: false, error: response.error || response.data?.message || 'Terjadi kesalahan' };
  },

  async createDiseasesRecord(record: Omit<DiseasesRecord, 'id'>): Promise<ApiResponse<DiseasesRecord>> {
    const response = await apiPost<BackendResponse<any>>('/diseases-records', {
      pondId: record.pondsId,
      diseaseName: record.diseaseName,
      symptoms: record.symptoms,
      infectedFishCount: record.infectedFishCount,
      diseaseDate: record.diseasesDate,
      notes: record.notes,
    });
    
    if (response.success && response.data?.data) {
      const d = response.data.data;
      return {
        success: true,
        data: {
          id: d.id,
          pondsId: d.pondId,
          diseaseName: d.diseaseName,
          symptoms: d.symptoms,
          infectedFishCount: d.infectedFishCount,
          diseasesDate: d.diseaseDate,
          notes: d.notes || '',
          organizationId: '',
        },
      };
    }
    
    return { success: false, error: response.error || response.data?.message || 'Terjadi kesalahan' };
  },

  async updateDiseasesRecord(id: string, data: Partial<DiseasesRecord>): Promise<ApiResponse<DiseasesRecord>> {
    const response = await apiPut<BackendResponse<any>>(`/diseases-records/${id}`, {
      pondId: data.pondsId,
      diseaseName: data.diseaseName,
      symptoms: data.symptoms,
      infectedFishCount: data.infectedFishCount,
      diseaseDate: data.diseasesDate,
      notes: data.notes,
    });
    
    if (response.success) {
      const d = response.data?.data || response.data;
      if (d && d.id) {
        return {
          success: true,
          data: {
            id: d.id,
            pondsId: d.pondId,
            diseaseName: d.diseaseName,
            symptoms: d.symptoms,
            infectedFishCount: d.infectedFishCount,
            diseasesDate: d.diseaseDate,
            notes: d.notes || '',
            organizationId: '',
          },
        };
      }
    }
    
    return { success: false, error: response.error || response.data?.message || 'Terjadi kesalahan' };
  },

  async deleteDiseasesRecord(id: string): Promise<ApiResponse<void>> {
    const response = await apiDelete<BackendResponse<void>>(`/diseases-records/${id}`);
    return { success: response.success, error: response.error };
  },

  // ==================== HARVEST RECORDS ====================
  async getHarvestRecords(page = 1, limit = 10): Promise<ApiResponse<PaginatedResponse<HarvestRecord>>> {
    const response = await apiGet<BackendResponse<{ harvestRecords: any[] }>>(`/harvest-records?page=${page}&limit=${limit}`);
    
    if (response.success && response.data?.data) {
      const records = (response.data.data.harvestRecords || []).map((h: any) => ({
        id: h.id,
        pondsName: h.pondsName || '',
        pondsId: h.pondsId,
        harvestDate: h.harvestDate,
        organizationId: '',
        harvestFishCount: h.harvestFishCount,
        totalWeight: h.totalWeight,
        notes: h.notes || '',
      }));
      return {
        success: true,
        data: {
          data: records,
          page,
          limit,
          total: records.length,
          totalPages: Math.ceil(records.length / limit),
        },
      };
    }
    
    return { success: false, error: response.error || response.data?.message || 'Terjadi kesalahan' };
  },

  async createHarvestRecord(record: Omit<HarvestRecord, 'id'>): Promise<ApiResponse<HarvestRecord>> {
    const response = await apiPost<BackendResponse<any>>('/harvest-records', {
      pondId: record.pondsId,
      harvestDate: record.harvestDate,
      harvestFishCount: record.harvestFishCount,
      totalWeight: record.totalWeight,
      notes: record.notes,
    });
    
    if (response.success && response.data?.data) {
      const h = response.data.data;
      return {
        success: true,
        data: {
          id: h.id,
          pondsName: h.pondsName || '',
          pondsId: h.pondsId,
          harvestDate: h.harvestDate,
          organizationId: '',
          harvestFishCount: h.harvestFishCount,
          totalWeight: h.totalWeight,
          notes: h.notes || '',
        },
      };
    }
    
    return { success: false, error: response.error || response.data?.message || 'Terjadi kesalahan' };
  },

  async updateHarvestRecord(id: string, data: Partial<HarvestRecord>): Promise<ApiResponse<HarvestRecord>> {
    const response = await apiPut<BackendResponse<any>>(`/harvest-records/${id}`, {
      pondId: data.pondsId,
      harvestDate: data.harvestDate,
      harvestFishCount: data.harvestFishCount,
      totalWeight: data.totalWeight,
      notes: data.notes,
    });
    
    if (response.success) {
      const h = response.data?.data || response.data;
      if (h && h.id) {
        return {
          success: true,
          data: {
            id: h.id,
            pondsName: h.pondsName || '',
            pondsId: h.pondsId,
            harvestDate: h.harvestDate,
            organizationId: '',
            harvestFishCount: h.harvestFishCount,
            totalWeight: h.totalWeight,
            notes: h.notes || '',
          },
        };
      }
    }
    
    return { success: false, error: response.error || response.data?.message || 'Terjadi kesalahan' };
  },

  async deleteHarvestRecord(id: string): Promise<ApiResponse<void>> {
    const response = await apiDelete<BackendResponse<void>>(`/harvest-records/${id}`);
    return { success: response.success, error: response.error };
  },

  async getHarvestReport(pondId?: string, startDate?: string, endDate?: string): Promise<ApiResponse<HarvestReport[]>> {
    let url = '/harvest-records/report?';
    if (pondId) url += `pondId=${pondId}&`;
    if (startDate) url += `startDate=${startDate}&`;
    if (endDate) url += `endDate=${endDate}`;
    
    const response = await apiGet<BackendResponse<any>>(url);
    
    if (response.success && response.data?.data) {
      return { success: true, data: response.data.data };
    }
    
    return { success: false, error: response.error || response.data?.message || 'Terjadi kesalahan' };
  },

  // ==================== FINANCE RECORDS ====================
  async getFinanceRecords(page = 1, limit = 10, type?: 'income' | 'expense'): Promise<ApiResponse<PaginatedResponse<FinanceRecord>>> {
    let url = `/finance-records?page=${page}&limit=${limit}`;
    if (type) url += `&transactionType=${type.toUpperCase()}`;
    
    const response = await apiGet<BackendResponse<{ records: any[] }>>(url);
    
    if (response.success && response.data?.data) {
      const records = (response.data.data.records || []).map((f: any) => ({
        id: f.id,
        transactionType: f.transactionType?.toLowerCase() as 'income' | 'expense',
        transactionDate: f.transactionDate,
        amount: f.amount,
        category: f.category,
        notes: f.notes || '',
        organizationId: '',
      }));
      return {
        success: true,
        data: {
          data: records,
          page,
          limit,
          total: records.length,
          totalPages: Math.ceil(records.length / limit),
        },
      };
    }
    
    return { success: false, error: response.error || response.data?.message || 'Terjadi kesalahan' };
  },

  async createFinanceRecord(record: Omit<FinanceRecord, 'id'>): Promise<ApiResponse<FinanceRecord>> {
    const response = await apiPost<BackendResponse<any>>('/finance-records', {
      transactionType: record.transactionType.toUpperCase(),
      transactionDate: record.transactionDate,
      amount: record.amount,
      category: record.category,
      notes: record.notes,
    });
    
    if (response.success && response.data?.data) {
      const f = response.data.data;
      return {
        success: true,
        data: {
          id: f.id,
          transactionType: f.transactionType?.toLowerCase() as 'income' | 'expense',
          transactionDate: f.transactionDate,
          amount: f.amount,
          category: f.category,
          notes: f.notes || '',
          organizationId: '',
        },
      };
    }
    
    return { success: false, error: response.error || response.data?.message || 'Terjadi kesalahan' };
  },

  async updateFinanceRecord(id: string, data: Partial<FinanceRecord>): Promise<ApiResponse<FinanceRecord>> {
    const response = await apiPut<BackendResponse<any>>(`/finance-records/${id}`, {
      transactionType: data.transactionType?.toUpperCase(),
      transactionDate: data.transactionDate,
      amount: data.amount,
      category: data.category,
      notes: data.notes,
    });
    
    if (response.success) {
      const f = response.data?.data || response.data;
      if (f && f.id) {
        return {
          success: true,
          data: {
            id: f.id,
            transactionType: f.transactionType?.toLowerCase() as 'income' | 'expense',
            transactionDate: f.transactionDate,
            amount: f.amount,
            category: f.category,
            notes: f.notes || '',
            organizationId: '',
          },
        };
      }
    }
    
    return { success: false, error: response.error || response.data?.message || 'Terjadi kesalahan' };
  },

  async deleteFinanceRecord(id: string): Promise<ApiResponse<void>> {
    const response = await apiDelete<BackendResponse<void>>(`/finance-records/${id}`);
    return { success: response.success, error: response.error };
  },

  async getFinanceReport(startDate?: string, endDate?: string): Promise<ApiResponse<FinanceReport>> {
    let url = '/finance-records/report?';
    if (startDate) url += `startDate=${startDate}&`;
    if (endDate) url += `endDate=${endDate}`;
    
    const response = await apiGet<BackendResponse<any>>(url);
    
    if (response.success && response.data?.data) {
      return { success: true, data: response.data.data };
    }
    
    return { success: false, error: response.error || response.data?.message || 'Terjadi kesalahan' };
  },

  // ==================== SUPPLIERS ====================
  async getSuppliers(page = 1, limit = 10, name?: string): Promise<ApiResponse<PaginatedResponse<Supplier>>> {
    let url = `/suppliers?page=${page}&limit=${limit}`;
    if (name) url += `&name=${encodeURIComponent(name)}`;
    
    const response = await apiGet<BackendResponse<{ suppliers: any[] }>>(url);
    
    if (response.success && response.data?.data) {
      const suppliers = (response.data.data.suppliers || []).map((s: any) => ({
        id: s.id,
        name: s.name,
        address: s.address,
        phone: s.phone,
        email: s.email,
        notes: s.notes || '',
        organizationId: '',
      }));
      return {
        success: true,
        data: {
          data: suppliers,
          page,
          limit,
          total: suppliers.length,
          totalPages: Math.ceil(suppliers.length / limit),
        },
      };
    }
    
    return { success: false, error: response.error || response.data?.message || 'Terjadi kesalahan' };
  },

  async createSupplier(supplier: Omit<Supplier, 'id'>): Promise<ApiResponse<Supplier>> {
    const response = await apiPost<BackendResponse<any>>('/suppliers', {
      name: supplier.name,
      address: supplier.address,
      phone: supplier.phone,
      email: supplier.email,
      notes: supplier.notes,
    });
    
    if (response.success && response.data?.data) {
      const s = response.data.data;
      return {
        success: true,
        data: {
          id: s.id,
          name: s.name,
          address: s.address,
          phone: s.phone,
          email: s.email,
          notes: s.notes || '',
          organizationId: '',
        },
      };
    }
    
    return { success: false, error: response.error || response.data?.message || 'Terjadi kesalahan' };
  },

  async updateSupplier(id: string, data: Partial<Supplier>): Promise<ApiResponse<Supplier>> {
    const response = await apiPut<BackendResponse<any>>(`/suppliers/${id}`, {
      name: data.name,
      address: data.address,
      phone: data.phone,
      email: data.email,
      notes: data.notes,
    });
    
    if (response.success) {
      const s = response.data?.data || response.data;
      if (s && s.id) {
        return {
          success: true,
          data: {
            id: s.id,
            name: s.name,
            address: s.address,
            phone: s.phone,
            email: s.email,
            notes: s.notes || '',
            organizationId: '',
          },
        };
      }
    }
    
    return { success: false, error: response.error || response.data?.message || 'Terjadi kesalahan' };
  },

  async deleteSupplier(id: string): Promise<ApiResponse<void>> {
    const response = await apiDelete<BackendResponse<void>>(`/suppliers/${id}`);
    return { success: response.success, error: response.error };
  },

  // ==================== CONTACTS ====================
  async getContacts(page = 1, limit = 10, supplierId?: string): Promise<ApiResponse<PaginatedResponse<Contact>>> {
    if (!supplierId) {
      return { success: false, error: 'supplierId is required' };
    }
    
    const response = await apiGet<BackendResponse<{ contacts: any[] }>>(`/contacts?supplierId=${supplierId}&page=${page}&limit=${limit}`);
    
    if (response.success && response.data?.data) {
      const contacts = (response.data.data.contacts || []).map((c: any) => ({
        id: c.id,
        supplierId: c.supplierId,
        name: c.name,
        address: c.address || '',
        phone: c.phone,
        email: c.email,
        notes: c.notes || '',
      }));
      return {
        success: true,
        data: {
          data: contacts,
          page,
          limit,
          total: contacts.length,
          totalPages: Math.ceil(contacts.length / limit),
        },
      };
    }
    
    return { success: false, error: response.error || response.data?.message || 'Terjadi kesalahan' };
  },

  async createContact(contact: Omit<Contact, 'id'>): Promise<ApiResponse<Contact>> {
    const response = await apiPost<BackendResponse<any>>('/contacts', {
      supplierId: contact.supplierId,
      name: contact.name,
      phone: contact.phone,
      email: contact.email,
      address: contact.address,
      notes: contact.notes,
    });
    
    if (response.success && response.data?.data) {
      const c = response.data.data;
      return {
        success: true,
        data: {
          id: c.id,
          supplierId: c.supplierId,
          name: c.name,
          address: c.address || '',
          phone: c.phone,
          email: c.email,
          notes: c.notes || '',
        },
      };
    }
    
    return { success: false, error: response.error || response.data?.message || 'Terjadi kesalahan' };
  },

  async updateContact(id: string, data: Partial<Contact>): Promise<ApiResponse<Contact>> {
    const response = await apiPut<BackendResponse<any>>(`/contacts/${id}`, {
      name: data.name,
      phone: data.phone,
      email: data.email,
      address: data.address,
      notes: data.notes,
    });
    
    if (response.success) {
      const c = response.data?.data || response.data;
      if (c && c.id) {
        return {
          success: true,
          data: {
            id: c.id,
            supplierId: c.supplierId,
            name: c.name,
            address: c.address || '',
            phone: c.phone,
            email: c.email,
            notes: c.notes || '',
          },
        };
      }
    }
    
    return { success: false, error: response.error || response.data?.message || 'Terjadi kesalahan' };
  },

  async deleteContact(id: string): Promise<ApiResponse<void>> {
    const response = await apiDelete<BackendResponse<void>>(`/contacts/${id}`);
    return { success: response.success, error: response.error };
  },

  // ==================== FEED REMINDERS ====================
  async getFeedReminders(pondId?: string, date?: string): Promise<ApiResponse<PaginatedResponse<FeedReminder>>> {
    let url = '/feed-reminders?';
    if (pondId) url += `pondId=${pondId}&`;
    if (date) url += `date=${date}`;
    
    const response = await apiGet<BackendResponse<{ reminders: any[] }>>(url);
    
    if (response.success && response.data?.data) {
      const reminders = (response.data.data.reminders || []).map((r: any) => ({
        id: r.id,
        pondsId: r.pondId,
        reminderDate: r.reminderDate,
        reminderTime: r.reminderTime,
        feedType: r.feedType,
        notes: r.notes || '',
        organizationId: '',
      }));
      return {
        success: true,
        data: {
          data: reminders,
          page: 1,
          limit: reminders.length,
          total: reminders.length,
          totalPages: 1,
        },
      };
    }
    
    return { success: false, error: response.error || response.data?.message || 'Terjadi kesalahan' };
  },

  async createFeedReminder(reminder: Omit<FeedReminder, 'id'>): Promise<ApiResponse<FeedReminder>> {
    const response = await apiPost<BackendResponse<any>>('/feed-reminders', {
      pondId: reminder.pondsId,
      reminderDate: reminder.reminderDate,
      reminderTime: reminder.reminderTime,
      feedType: reminder.feedType,
      notes: reminder.notes,
    });
    
    if (response.success && response.data?.data) {
      const r = response.data.data;
      return {
        success: true,
        data: {
          id: r.id,
          pondsId: r.pondId,
          reminderDate: r.reminderDate,
          reminderTime: r.reminderTime,
          feedType: r.feedType,
          notes: r.notes || '',
          organizationId: '',
        },
      };
    }
    
    return { success: false, error: response.error || response.data?.message || 'Terjadi kesalahan' };
  },

  async updateFeedReminder(id: string, data: Partial<FeedReminder>): Promise<ApiResponse<FeedReminder>> {
    const response = await apiPut<BackendResponse<any>>(`/feed-reminders/${id}`, {
      pondId: data.pondsId,
      reminderDate: data.reminderDate,
      reminderTime: data.reminderTime,
      feedType: data.feedType,
      notes: data.notes,
    });
    
    if (response.success) {
      const r = response.data?.data || response.data;
      if (r && r.id) {
        return {
          success: true,
          data: {
            id: r.id,
            pondsId: r.pondId,
            reminderDate: r.reminderDate,
            reminderTime: r.reminderTime,
            feedType: r.feedType,
            notes: r.notes || '',
            organizationId: '',
          },
        };
      }
    }
    
    return { success: false, error: response.error || response.data?.message || 'Terjadi kesalahan' };
  },

  async deleteFeedReminder(id: string): Promise<ApiResponse<void>> {
    const response = await apiDelete<BackendResponse<void>>(`/feed-reminders/${id}`);
    return { success: response.success, error: response.error };
  },

  // ==================== HARVEST REMINDERS ====================
  async getHarvestReminders(pondId?: string, date?: string): Promise<ApiResponse<PaginatedResponse<HarvestReminder>>> {
    let url = '/harvest-reminders?';
    if (pondId) url += `pondId=${pondId}&`;
    if (date) url += `date=${date}`;
    
    const response = await apiGet<BackendResponse<{ reminders: any[] }>>(url);
    
    if (response.success && response.data?.data) {
      const reminders = (response.data.data.reminders || []).map((r: any) => ({
        id: r.id,
        pondId: r.pondId,
        reminderDate: r.reminderDate,
        reminderTime: r.reminderTime,
        notes: r.notes || '',
        organizationId: '',
      }));
      return {
        success: true,
        data: {
          data: reminders,
          page: 1,
          limit: reminders.length,
          total: reminders.length,
          totalPages: 1,
        },
      };
    }
    
    return { success: false, error: response.error || response.data?.message || 'Terjadi kesalahan' };
  },

  async createHarvestReminder(reminder: Omit<HarvestReminder, 'id'>): Promise<ApiResponse<HarvestReminder>> {
    const response = await apiPost<BackendResponse<any>>('/harvest-reminders', {
      pondId: reminder.pondId,
      reminderDate: reminder.reminderDate,
      reminderTime: reminder.reminderTime,
      notes: reminder.notes,
    });
    
    if (response.success && response.data?.data) {
      const r = response.data.data;
      return {
        success: true,
        data: {
          id: r.id,
          pondId: r.pondId,
          reminderDate: r.reminderDate,
          reminderTime: r.reminderTime,
          notes: r.notes || '',
          organizationId: '',
        },
      };
    }
    
    return { success: false, error: response.error || response.data?.message || 'Terjadi kesalahan' };
  },

  async updateHarvestReminder(id: string, data: Partial<HarvestReminder>): Promise<ApiResponse<HarvestReminder>> {
    const response = await apiPut<BackendResponse<any>>(`/harvest-reminders/${id}`, {
      pondId: data.pondId,
      reminderDate: data.reminderDate,
      reminderTime: data.reminderTime,
      notes: data.notes,
    });
    
    if (response.success) {
      const r = response.data?.data || response.data;
      if (r && r.id) {
        return {
          success: true,
          data: {
            id: r.id,
            pondId: r.pondId,
            reminderDate: r.reminderDate,
            reminderTime: r.reminderTime,
            notes: r.notes || '',
            organizationId: '',
          },
        };
      }
    }
    
    return { success: false, error: response.error || response.data?.message || 'Terjadi kesalahan' };
  },

  async deleteHarvestReminder(id: string): Promise<ApiResponse<void>> {
    const response = await apiDelete<BackendResponse<void>>(`/harvest-reminders/${id}`);
    return { success: response.success, error: response.error };
  },

  // ==================== REPORTS ====================
  async getFishStatistics(startDate: string, endDate: string): Promise<ApiResponse<FishStatistics[]>> {
    const response = await apiGet<BackendResponse<any>>(`/reports/fish-statistics?startDate=${startDate}&endDate=${endDate}`);
    
    if (response.success && response.data?.data) {
      const payload = response.data.data;
      // Backend returns: { fishStatistics: [...] }
      const fishStatistics = Array.isArray(payload?.fishStatistics) ? payload.fishStatistics : [];
      return { success: true, data: fishStatistics };
    }
    
    return { success: false, error: response.error || response.data?.message || 'Terjadi kesalahan' };
  },

  async getGrowthCharts(pondsId: string, startDate: string, endDate: string): Promise<ApiResponse<GrowthChart[]>> {
    const response = await apiGet<BackendResponse<any>>(`/reports/growth-chart?pondsId=${pondsId}&startDate=${startDate}&endDate=${endDate}`);
    
    if (response.success && response.data?.data) {
      return { success: true, data: response.data.data };
    }
    
    return { success: false, error: response.error || response.data?.message || 'Terjadi kesalahan' };
  },
};
