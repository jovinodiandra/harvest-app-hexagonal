import { mockService } from './mockService';
import { apiService } from './apiService';

const USE_MOCK_API = import.meta.env.VITE_USE_MOCK_API === 'true';

export const api = USE_MOCK_API ? mockService : apiService;

export { mockService } from './mockService';
export { apiService } from './apiService';
export { apiGet, apiPost, apiPut, apiDelete, apiRequest } from './client';
