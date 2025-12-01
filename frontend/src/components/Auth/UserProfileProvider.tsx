import { useEffect } from 'react';
import { apiClient } from '@/queries/axios';
import { useAuthStore } from '@/store/useAuthStore';
import { Outlet } from 'react-router-dom';

const UserProfileProvider = () => {
  const user = useAuthStore((state) => state.user);
  const userProfile = useAuthStore((state) => state.userProfile);
  const setUserProfile = useAuthStore((state) => state.setUserProfile);

  useEffect(() => {
    if (user && !userProfile) {
      apiClient
        .get(`/users/${user.userId}/profile`)
        .then((res) => {
          setUserProfile(res.data);
        })
        .catch((err) => {
          console.error('Failed to load user profile', err);
        });
    }
  }, [user, userProfile, setUserProfile]);

  return <Outlet />;
};

export default UserProfileProvider;
