import { create } from 'zustand';
import { devtools } from 'zustand/middleware';

type User = {
  userId: number;
  email: string;
  roles: string[];
};

type AuthState = {
  user: User | null;
  accessToken: string | null;
  setAuth: (user: User | null, token: string) => void;
  clearAuth: () => void;
};

export const useAuthStore = create<AuthState>()(
  devtools(
    (set) => ({
      user: null,
      accessToken: null,
      setAuth: (user, token) => set({ user, accessToken: token }),
      clearAuth: () => set({ user: null, accessToken: null }),
    }),
    { name: 'AuthStore' },
  ), // optional name for Redux DevTools
);
