import { apiClient } from '@/queries/axios';
import { useAuthStore } from '@/store/useAuthStore';
import { useMutation, useQuery } from '@tanstack/react-query';
import { UserRoundPlus } from 'lucide-react';

const FriendDetails = () => {
  const userId = useAuthStore((state) => state.user?.userId);
  const { data: profiles, isPending } = useQuery({
    queryKey: ['userProfilesExceptUserId', userId],
    queryFn: async () => {
      const res = await apiClient.get(`users/${userId}/profiles`);
      return res.data;
    },
  });

  const { mutate } = useMutation({
    mutationKey: ['follow', userId],
    mutationFn: async (followedUserId) => {
      const res = await apiClient.post(
        `/users/${userId}/friends/${followedUserId}`,
      );
      return res.data;
    },
  });

  const toggleFollow = (followedUserId) => {
    mutate(followedUserId);
  };

  return (
    <div>
      FriendDetails
      {!isPending &&
        profiles.map((profile) => (
          <div
            key={profile.userId}
            className="flex items-center gap-2 border-1 p-2"
          >
            <img
              src={profile.profileImageUrl}
              alt={profile.nickname}
              className="rounded-full w-10 h-10"
            />
            <div>{profile.nickname}</div>
            <div
              className="p-2 cursor-pointer hover:bg-amber-200"
              onClick={() => toggleFollow(profile.userId)}
            >
              <UserRoundPlus />
            </div>
          </div>
        ))}
    </div>
  );
};

export default FriendDetails;
