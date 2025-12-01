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
    <div className="p-6 space-y-6">
      {/* Profile Card */}
      <div className="flex items-center space-x-4 bg-white p-4 rounded-2xl shadow-md">
        <img
          src={userProfile?.profileImageUrl}
          alt="Profile"
          className="w-16 h-16 rounded-full object-cover"
        />
        <div>
          <h2 className="text-xl font-semibold">{userProfile?.nickname}</h2>
          <p className="text-gray-500 text-sm">
            {userProfile?.readingGoalCount}{' '}
            {userProfile?.readingGoalUnit.toLowerCase()}
            {userProfile?.readingGoalCount >= 2 ? 's' : ''} /{' '}
            {userProfile?.readingGoalTimeframe.toLowerCase()}
          </p>
        </div>
      </div>

      {/* Reading Goal Progress */}
      <div className="bg-white p-4 rounded-2xl shadow-md">
        <h3 className="font-semibold mb-2">Reading Goal Progress</h3>
        <div className="w-full bg-gray-200 h-4 rounded-full overflow-hidden">
          <div
            className="bg-blue-500 h-4"
            style={{
              width: `${Math.min(
                (userProfile?.readingGoalCount / 10) * 100,
                100,
              )}%`,
            }}
          ></div>
        </div>
        <p className="text-gray-500 text-sm mt-1">
          {userProfile?.readingGoalCount} of 10 {userProfile?.readingGoalUnit}s
        </p>
      </div>

      {/* Favored Genres */}
      <div className="bg-white p-4 rounded-2xl shadow-md">
        <h3 className="font-semibold mb-2">Favored Genres</h3>
        <div className="flex flex-wrap gap-2">
          {userProfile?.favoredGenres.split(',').map((genre) => (
            <span
              key={genre}
              className="bg-purple-100 text-purple-800 px-3 py-1 rounded-full text-sm"
            >
              {genre}
            </span>
          ))}
        </div>
      </div>

      {/* Quick Stats */}
      <div className="grid grid-cols-2 gap-4">
        <div className="bg-white p-4 rounded-2xl shadow-md text-center">
          <h4 className="text-gray-500 text-sm">Books Read</h4>
          <p className="font-semibold text-lg">0</p>
        </div>
        <div className="bg-white p-4 rounded-2xl shadow-md text-center">
          <h4 className="text-gray-500 text-sm">Days Active</h4>
          <p className="font-semibold text-lg">0</p>
        </div>
      </div>
    </div>
  );
};

export default Dashboard;
