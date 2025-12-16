import { useState } from 'react';
import { Fragment } from 'react/jsx-runtime';
import { apiClient } from '@/queries/axios';
import { useAuthStore } from '@/store/useAuthStore';
import { useUserPresenceStore } from '@/store/useUserPresenceStore';
import { useWebSocketStore } from '@/store/useWebSocketStore';
import { useMutation, useQuery, useQueryClient } from '@tanstack/react-query';
import { UserRoundMinus, UserRoundPlus } from 'lucide-react';
import DirectMessage from './DirectMessage';

const FriendDetails = () => {
  const queryClient = useQueryClient();
  const userId = useAuthStore((state) => state.user?.userId);

  const onlineUserIds = useUserPresenceStore((state) => state.onlineUserIds);
  const client = useWebSocketStore((state) => state.stompClient);

  const [isDmOpen, setIsDmOpen] = useState(false);
  const [profileUserId, setProfileUserId] = useState(0);

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

  const handleUserClick = (profileUserId: number) => {
    setProfileUserId(profileUserId);
    setIsDmOpen(true);
  };

  return (
    <div>
      FriendDetails
      {isDmOpen && <DirectMessage receiverId={profileUserId} />}
      {!isPending &&
        profiles.map((profile) => (
          <div
            key={profile.userId}
            className="flex items-center gap-2 border-1 p-2"
          >
            <div
              className="flex items-center w-50 gap-2 border-red-300 border-1 truncate cursor-pointer"
              onClick={() => handleUserClick(profile.userId)}
            >
              <img
                src={profile.profileImageUrl}
                alt={profile.nickname}
                className="rounded-full w-10 h-10"
              />
              <div>{profile.nickname}</div>
            </div>
            {onlineUserIds.includes(profile.userId) ? '● online' : '○ offline'}
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
