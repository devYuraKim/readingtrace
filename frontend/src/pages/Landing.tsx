import { useAuthStore } from '@/store/useAuthStore';
import { Navigate } from 'react-router-dom';

const Landing = () => {
  // TODO: create isAuthenticated state
  if (!useAuthStore((state) => state.user)) {
    return <Navigate to="/login" />;
  }
  return <div>Landing</div>;
};
export default Landing;
