import { create } from 'zustand';

type User = {
  id: number;
  email: string;
  roles: string[];
};

type AuthState = {
  user: User | null;
  accessToken: string | null;
  setAuth: (user: User | null, token: string) => void;
  clearAuth: () => void;
};

export const useAuthStore = create<AuthState>((set) => ({
  user: null,
  accessToken: null,
  setAuth: (user, token) => set({ user, accessToken: token }),
  clearAuth: () => set({ user: null, accessToken: null }),
}));
