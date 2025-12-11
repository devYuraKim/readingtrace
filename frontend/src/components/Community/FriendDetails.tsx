import { Fragment } from 'react/jsx-runtime';
import { apiClient } from '@/queries/axios';
import { useAuthStore } from '@/store/useAuthStore';
import { useUserPresenceStore } from '@/store/useUserPresenceStore';
import { useMutation, useQuery, useQueryClient } from '@tanstack/react-query';
import { UserRoundMinus, UserRoundPlus } from 'lucide-react';

const FriendDetails = () => {
  const queryClient = useQueryClient();
  const userId = useAuthStore((state) => state.user?.userId);
  const onlineMap = useUserPresenceStore((state) => state.onlineMap);

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
    onSuccess: () => {
      queryClient.invalidateQueries({
        queryKey: ['userProfilesExceptUserId', userId],
      });
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
            {onlineMap[profile.userId] ? '● online' : '○ offline'}
            <div
              className="p-2 cursor-pointer hover:bg-amber-200"
              onClick={() => toggleFollow(profile.userId)}
            >
              {profile.isFriend ? (
                <div className="flex">
                  <UserRoundMinus /> unfriend
                </div>
              ) : (
                <div className="flex">
                  <UserRoundPlus /> friend
                </div>
              )}
            </div>

            <div className="flex gap-2">
              {profile.userReadingStatusDto.length !== 0 &&
                profile.userReadingStatusDto.map((status) => (
                  <Fragment key={status.userReadingStatusId}>
                    <div key={status.userReadingStatusId}>
                      <img
                        src={status.bookDto.imageLinks}
                        className="w-15 h-full"
                      />
                    </div>
                    <div>
                      <span>{status.bookDto.title}</span>
                      <span>{status.status}</span>
                    </div>
                  </Fragment>
                ))}
            </div>
          </div>
        ))}
    </div>
  );
};

export default FriendDetails;
