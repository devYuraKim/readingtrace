import { create } from 'zustand';
import { devtools } from 'zustand/middleware';

export type User = {
  userId: number;
  email: string;
  roles: string[];
};

export type UserProfile = {
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
  setUserProfile: (userProfile: UserProfile | null) => void;
  setAuth: (
    user: User | null,
    userProfile: UserProfile | null,
    token: string,
  ) => void;
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
      setUserProfile: (userProfile) => set({ userProfile }),
      setAuth: (user, userProfile, token) =>
        set({ user, userProfile, accessToken: token }),
      clearAuth: () =>
        set({ user: null, userProfile: null, accessToken: null }),
    }),
    { name: 'AuthStore' },
  ), // optional name for Redux DevTools
);
