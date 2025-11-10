import { useAuthStore } from '@/store/useAuthStore';
import { Navigate, Outlet } from 'react-router-dom';
import { Spinner } from '@/components/ui/spinner';

const UnauthenticatedRoutes = () => {
  const user = useAuthStore((state) => state.userProfile);
  const isAuthChecked = useAuthStore((state) => state.isAuthChecked);

  if (!isAuthChecked) {
    return <Spinner />;
  }

  if (user) {
    return <Navigate to={`/users/${user.userId}`} />;
  }

  return <Outlet />;
};

export default UnauthenticatedRoutes;
