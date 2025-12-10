import { create } from 'zustand';
import { devtools } from 'zustand/middleware';

interface PresenceState {
  onlineMap: Record<number, boolean>; // userId → true/false
  updatePresence: (payload: { userId: number; status: string }) => void;
}

export const useUserPresenceStore = create<PresenceState>()(
  devtools(
    (set) => ({
      onlineMap: {},
      updatePresence: ({ userId, status }) =>
        set((state) => ({
          onlineMap: {
            ...state.onlineMap,
            [userId]: status === 'ONLINE',
          },
        })),
    }),
    { name: 'UserPresenceStore' },
  ),
);
