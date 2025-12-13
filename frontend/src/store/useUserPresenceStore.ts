import { create } from 'zustand';
import { devtools } from 'zustand/middleware';

interface PresenceState {
  onlineUserIds: number[];
  setOnlineUserIds: (ids: number[]) => void;
  addOnlineUserId: (id: number) => void;
  removeOnlineUserId: (id: number) => void;
}

export const useUserPresenceStore = create<PresenceState>()(
  devtools(
    (set) => ({
      onlineUserIds: [],

      setOnlineUserIds: (ids) =>
        set({
          onlineUserIds: ids.filter(
            (id): id is number => typeof id === 'number',
          ),
        }),

      addOnlineUserId: (id) =>
        set((state) => {
          if (typeof id !== 'number') return state;

          return state.onlineUserIds.includes(id)
            ? state
            : { onlineUserIds: [...state.onlineUserIds, id] };
        }),

      removeOnlineUserId: (id) =>
        set((state) => ({
          onlineUserIds: state.onlineUserIds.filter((uid) => uid !== id),
        })),
    }),
    { name: 'UserPresenceStore' },
  ),
);
