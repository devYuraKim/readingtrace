import PrivateLayout from '@/layouts/PrivateLayout';
import PublicLayout from '@/layouts/PublicLayout';
import Landing from '@/pages/Landing';
import LoginPage from '@/pages/LoginPage';
import MainPage from '@/pages/MainPage';
import SignupPage from '@/pages/SignupPage';
import {
  createBrowserRouter,
  createRoutesFromElements,
  Route,
} from 'react-router-dom';

const router = createBrowserRouter(
  createRoutesFromElements(
    <>
      <Route path="/login" element={<LoginPage />} />
      <Route path="/signup" element={<SignupPage />} />

      <Route path="/" element={<PublicLayout />}>
        <Route index element={<Landing />} />
      </Route>

      <Route path="/:id" element={<PrivateLayout />}>
        <Route index element={<MainPage />} />
      </Route>
    </>,
  ),
);

export default router;
