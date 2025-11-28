import { apiClient } from '@/queries/axios';
import { useAuthStore, UserProfile } from '@/store/useAuthStore';
import { useQuery } from '@tanstack/react-query';

const Dashboard = () => {
  const userId = useAuthStore((state) => state.user?.userId);

  const { data: userProfile, isPending: isPendingUserProfile } =
    useQuery<UserProfile>({
      queryKey: ['userProfile', userId],
      queryFn: async () => {
        const res = await apiClient.get(`/users/${userId}/profile`);
        return res.data;
      },
    });

  //fix store values not being loaded when rendering the component
  //currently getting 'undefined' for all the variables
  return (
    <>
      {!isPendingUserProfile && (
        <>
          <div>
            <img src={userProfile?.profileImageUrl} className="w-10 h-10" />
            <span>{userProfile?.nickname}</span>
          </div>
          <div>
            {userProfile?.readingGoalCount}{' '}
            {userProfile?.readingGoalUnit.toLowerCase()}
            {userProfile?.readingGoalCount >= 2 ? 's' : ''}{' '}
            {userProfile?.readingGoalTimeframe.toLowerCase()}
          </div>
          <div>
            {userProfile?.favoredGenres
              .split(',')
              .map((genre) => genre)
              .join(', ')}
          </div>
        </>
      )}
    </>
  );
};

export default Dashboard;
