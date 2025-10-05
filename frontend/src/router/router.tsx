import PrivateLayout from '@/layouts/PrivateLayout';
import PublicLayout from '@/layouts/PublicLayout';
import Landing from '@/pages/Landing';
import LoginPage from '@/pages/LoginPage';
import SignupPage from '@/pages/SignupPage';
import {
  createBrowserRouter,
  createRoutesFromElements,
  Route,
} from 'react-router-dom';
import StartByBookCollection from '@/components/StartByBookCollection';
import StartByBookSearch from '@/components/StartByBookSearch';
import StartByBookSearchReactive from '@/components/StartByBookSearchReactive';
import SupportChat from '@/components/SupportChat';

const router = createBrowserRouter(
  createRoutesFromElements(
    <>
      <Route path="/login" element={<LoginPage />} />
      <Route path="/signup" element={<SignupPage />} />

      <Route path="/" element={<PublicLayout />}>
        <Route index element={<Landing />} />
      </Route>

      <Route path="/:userId" element={<PrivateLayout />}>
        <Route index element={<StartByBookCollection />} />
        <Route path="searchBook" element={<StartByBookSearchReactive />} />
        <Route path="supportChat" element={<SupportChat />} />
      </Route>
    </>,
  ),
);

export default router;
