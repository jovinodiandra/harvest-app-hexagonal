import { writable, derived } from 'svelte/store';
import type { Pond, PaginatedResponse } from '../../../shared/types';
import { api } from '../../../shared/api';

interface PondState {
  ponds: Pond[];
  loading: boolean;
  error: string | null;
  pagination: {
    page: number;
    limit: number;
    total: number;
    totalPages: number;
  };
}

function createPondStore() {
  const initialState: PondState = {
    ponds: [],
    loading: false,
    error: null,
    pagination: { page: 1, limit: 10, total: 0, totalPages: 0 },
  };

  const { subscribe, set, update } = writable<PondState>(initialState);

  return {
    subscribe,
    
    fetchPonds: async (page = 1, limit = 10) => {
      update(s => ({ ...s, loading: true, error: null }));
      
      const response = await api.getPonds(page, limit);
      
      if (response.success && response.data) {
        update(s => ({
          ...s,
          loading: false,
          ponds: response.data!.data,
          pagination: {
            page: response.data!.page,
            limit: response.data!.limit,
            total: response.data!.total,
            totalPages: response.data!.totalPages,
          },
        }));
      } else {
        update(s => ({ ...s, loading: false, error: response.error || 'Failed to fetch ponds' }));
      }
    },

    fetchAllPonds: async () => {
      const response = await api.getAllPonds();
      if (response.success && response.data) {
        update(s => ({ ...s, ponds: response.data! }));
      }
    },

    createPond: async (pond: Omit<Pond, 'id'>) => {
      update(s => ({ ...s, loading: true }));
      const response = await api.createPond(pond);
      
      if (response.success && response.data) {
        update(s => ({
          ...s,
          loading: false,
          ponds: [...s.ponds, response.data!],
          pagination: { ...s.pagination, total: s.pagination.total + 1 },
        }));
        return { success: true, data: response.data };
      }
      
      update(s => ({ ...s, loading: false }));
      return { success: false, error: response.error };
    },

    updatePond: async (id: string, data: Partial<Pond>) => {
      update(s => ({ ...s, loading: true }));
      const response = await api.updatePond(id, data);
      
      if (response.success && response.data) {
        update(s => ({
          ...s,
          loading: false,
          ponds: s.ponds.map(p => p.id === id ? response.data! : p),
        }));
        return { success: true };
      }
      
      update(s => ({ ...s, loading: false }));
      return { success: false, error: response.error };
    },

    deletePond: async (id: string) => {
      update(s => ({ ...s, loading: true }));
      const response = await api.deletePond(id);
      
      if (response.success) {
        update(s => ({
          ...s,
          loading: false,
          ponds: s.ponds.filter(p => p.id !== id),
          pagination: { ...s.pagination, total: s.pagination.total - 1 },
        }));
        return { success: true };
      }
      
      update(s => ({ ...s, loading: false }));
      return { success: false, error: response.error };
    },

    reset: () => set(initialState),
  };
}

export const pondStore = createPondStore();

export const pondsForSelect = derived(
  pondStore,
  $store => $store.ponds.map(p => ({ value: p.id, label: p.name }))
);
