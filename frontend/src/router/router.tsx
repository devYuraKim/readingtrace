import Landing from '@/pages/Landing';
import LoginPage from '@/pages/LoginPage';
import OnboardingPage from '@/pages/OnboardingPage';
import SignupPage from '@/pages/SignupPage';
import {
  createBrowserRouter,
  createRoutesFromElements,
  Route,
} from 'react-router-dom';
import AuthProvider from '@/components/AuthProvider';
import PrivateLayout from '@/components/layouts/PrivateLayout';
import PublicLayout from '@/components/layouts/PublicLayout';
import OAuth2Callback from '@/components/OAuth2Callback';
import StartByBookCollection from '@/components/StartBy/StartByBookCollection';
import StartByBookSearch from '@/components/StartBy/StartByBookSearch';
import SupportChat from '@/components/SupportChat';
import UserBookChatDetails from '@/components/userbook/UserBookChatDetails';
import UserShelfDetails from '@/components/usershelf/UserShelfDetails';
import AuthenticatedRoutes from './AuthenticatedRoutes';
import UnauthenticatedRoutes from './UnauthenticatedRoutes';

const router = createBrowserRouter(
  createRoutesFromElements(
    <Route element={<AuthProvider />}>
      <Route path="/" element={<PublicLayout />}>
        <Route index element={<Landing />} />
      </Route>

      <Route element={<UnauthenticatedRoutes />}>
        <Route path="/login" element={<LoginPage />} />
        <Route path="/signup" element={<SignupPage />} />
        <Route path="/oauth2/callback" element={<OAuth2Callback />} />
      </Route>

      <Route element={<AuthenticatedRoutes />}>
        <Route path="/users/:userId/onboarding" element={<OnboardingPage />} />
        <Route path="/users/:userId" element={<PrivateLayout />}>
          <Route index element={<StartByBookCollection />} />
          <Route path="searchBook" element={<StartByBookSearch />} />
          <Route path="supportChat" element={<SupportChat />} />
          <Route path="books" element={<UserShelfDetails />} />
          <Route path="books/:bookId" element={<UserBookChatDetails />} />
        </Route>
      </Route>
    </Route>,
  ),
);

export default router;
