import { useAuthStore } from '@/store/useAuthStore';
import { Navigate, Outlet, useLocation } from 'react-router-dom';
import { Spinner } from '@/components/ui/spinner';

const AuthenticatedRoutes = () => {
  const user = useAuthStore((state) => state.user);
  const isAuthChecked = useAuthStore((state) => state.isAuthChecked);
  const userProfile = useAuthStore((state) => state.userProfile);
  const location = useLocation();

  if (!isAuthChecked) {
    return <Spinner />;
  }

  if (!user) {
    return <Navigate to="/login" />;
  }

  //TODO: find cleaner and more robust way to handle onboarding cases
  if (
    !userProfile?.isOnboardingCompleted &&
    !location.pathname.includes('/onboarding')
  ) {
    return <Navigate to={`/users/${user.userId}/onboarding`} />;
  }

  return <Outlet />;
};

export default AuthenticatedRoutes;
