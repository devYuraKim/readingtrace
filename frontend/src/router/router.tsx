import Landing from '@/pages/Landing';
import LoginPage from '@/pages/LoginPage';
import OnboardingPage from '@/pages/OnboardingPage';
import SignupPage from '@/pages/SignupPage';
import {
  createBrowserRouter,
  createRoutesFromElements,
  Route,
} from 'react-router-dom';
import AuthProvider from '@/components/Auth/AuthProvider';
import OAuth2Callback from '@/components/Auth/OAuth2Callback';
import UserProfileProvider from '@/components/Auth/UserProfileProvider';
import ChatDetails from '@/components/Community/ChatDetails';
import FriendDetails from '@/components/Community/FriendDetails';
import MeetupDetails from '@/components/Community/MeetupDetails';
import Dashboard from '@/components/Dashboard/Dashboard';
import PrivateLayout from '@/components/layouts/PrivateLayout';
import PublicLayout from '@/components/layouts/PublicLayout';
import StartByBookCollection from '@/components/StartBy/StartByBookCollection';
import StartByBookSearch from '@/components/StartBy/StartByBookSearch';
import SupportChat from '@/components/SupportChat';
import UserBookChatDetails from '@/components/userbook/UserBookChatDetails';
import UserBookNoteDetails from '@/components/userbook/UserBookNoteDetails';
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
        <Route element={<UserProfileProvider />}>
          <Route path="/users/:userId" element={<PrivateLayout />}>
            <Route index element={<Dashboard />} />
            <Route path="collection" element={<StartByBookCollection />} />
            <Route path="search" element={<StartByBookSearch />} />
            <Route path="supportChat" element={<SupportChat />} />
            <Route path="books" element={<UserShelfDetails />} />
            <Route
              path="books/:bookId/chats"
              element={<UserBookChatDetails />}
            />
            <Route
              path="books/:bookId/notes"
              element={<UserBookNoteDetails />}
            />
            <Route path="community/friends" element={<FriendDetails />} />
            <Route path="community/chats" element={<ChatDetails />} />
            <Route path="community/meetups" element={<MeetupDetails />} />
          </Route>
        </Route>
      </Route>
    </Route>,
  ),
);

export default router;
