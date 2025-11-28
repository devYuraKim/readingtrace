import { create } from 'zustand';
import { devtools } from 'zustand/middleware';

type User = {
  userId: number;
  email: string;
  roles: string[];
};

type UserProfile = User & {
  nickname: string;
  profileImageUrl: string;
  readingGoalCount: number;
  readingGoalUnit: string;
  readingGoalTimeframe: string;
  favoredGenres: string;
  isOnboardingCompleted: boolean;
};

type AuthState = {
  isAuthChecked: boolean;
  user: User | null;
  userProfile: UserProfile | null;
  accessToken: string | null;
  setIsAuthChecked: () => void;
  setAuth: (user: User | null, token: string) => void;
  clearAuth: () => void;
};

export const useAuthStore = create<AuthState>()(
  devtools(
    (set) => ({
      isAuthChecked: false,
      user: null,
      userProfile: null,
      accessToken: null,
      setIsAuthChecked: () => set({ isAuthChecked: true }),
      setAuth: (user, token) => set({ user, accessToken: token }),
      clearAuth: () => set({ user: null, accessToken: null }),
    }),
    { name: 'AuthStore' },
  ), // optional name for Redux DevTools
);
