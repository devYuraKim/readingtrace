import { useEffect, useState } from 'react';
import OnboardingPage from '@/pages/OnboardingPage';
import { apiClient } from '@/queries/axios';
import { useAuthStore } from '@/store/useAuthStore';
import { Outlet } from 'react-router-dom';
import { Spinner } from '../ui/spinner';

const UserProfileProvider = () => {
  const user = useAuthStore((state) => state.user);
  const userProfile = useAuthStore((state) => state.userProfile);
  const setUserProfile = useAuthStore((state) => state.setUserProfile);

  const [isUserProfileLoading, setIsUserProfileLoading] = useState(true);

  useEffect(() => {
    if (user && !userProfile) {
      setIsUserProfileLoading(true);
      apiClient
        .get(`/users/${user.userId}/profile`)
        .then((res) => {
          setUserProfile(res.data);
        })
        .catch((err) => {
          console.error('Failed to load user profile', err);
        })
        .finally(() => setIsUserProfileLoading(false));
    } else {
      setIsUserProfileLoading(false);
    }
  }, [user, userProfile, setUserProfile]);

  if (isUserProfileLoading)
    return (
      <>
        UserProfileProvider
        <Spinner />
      </>
    );

  //TODO: check if adding !userProfile case is valid
  if (!userProfile || (userProfile && !userProfile.isOnboardingCompleted))
    return <OnboardingPage />;

  return <Outlet />;
};

export default UserProfileProvider;
