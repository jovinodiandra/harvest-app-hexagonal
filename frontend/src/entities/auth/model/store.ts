import { writable, derived } from 'svelte/store';
import type { UserSession, User } from '../../../shared/types';

function createAuthStore() {
  const storedSession = typeof localStorage !== 'undefined' 
    ? localStorage.getItem('session') 
    : null;
  
  const initialSession: UserSession | null = storedSession 
    ? JSON.parse(storedSession) 
    : null;

  const { subscribe, set, update } = writable<UserSession | null>(initialSession);

  return {
    subscribe,
    login: (session: UserSession) => {
      localStorage.setItem('session', JSON.stringify(session));
      set(session);
    },
    logout: () => {
      localStorage.removeItem('session');
      set(null);
    },
    updateSession: (data: Partial<UserSession>) => {
      update(session => {
        if (!session) return null;
        const updated = { ...session, ...data };
        localStorage.setItem('session', JSON.stringify(updated));
        return updated;
      });
    },
  };
}

export const authStore = createAuthStore();

export const isAuthenticated = derived(
  authStore,
  $auth => $auth !== null
);

export const currentUser = derived(
  authStore,
  $auth => $auth ? {
    userId: $auth.userId,
    organizationId: $auth.organizationId,
    role: $auth.role,
  } : null
);

export const isOwner = derived(
  authStore,
  $auth => $auth?.role === 'owner'
);

export const isAdmin = derived(
  authStore,
  $auth => $auth?.role === 'admin' || $auth?.role === 'owner'
);
