import { useEffect } from 'react';
import OnboardingPage from '@/pages/OnboardingPage';
import { apiClient } from '@/queries/axios';
import { useAuthStore } from '@/store/useAuthStore';
import { Outlet } from 'react-router-dom';
import { Spinner } from '../ui/spinner';

const UserProfileProvider = () => {
  const user = useAuthStore((state) => state.user);
  const userProfile = useAuthStore((state) => state.userProfile);
  const isUserProfileSet = useAuthStore((state) => state.isUserProfileSet);
  const setUserProfile = useAuthStore((state) => state.setUserProfile);
  const SetIsUserProfileSet = useAuthStore(
    (state) => state.setIsUserProfileSet,
  );

  useEffect(() => {
    if (user && !userProfile) {
      apiClient
        .get(`/users/${user.userId}/profile`)
        .then((res) => {
          setUserProfile(res.data);
          SetIsUserProfileSet();
        })
        .catch((err) => {
          console.error('Failed to load user profile', err);
        });
    }
  }, [user, userProfile, setUserProfile, SetIsUserProfileSet]);

  if (!isUserProfileSet) return <Spinner />;

  if (isUserProfileSet && !userProfile?.isOnboardingCompleted)
    return <OnboardingPage />;

  if (isUserProfileSet && userProfile?.isOnboardingCompleted) return <Outlet />;
};

export default UserProfileProvider;
