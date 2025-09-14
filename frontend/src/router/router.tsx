import {
  createBrowserRouter,
  createRoutesFromElements,
  Route,
} from "react-router-dom";
import PrivateLayout from "@/layouts/PrivateLayout";
import PublicLayout from "@/layouts/PublicLayout";
import Landing from "@/pages/Landing";
import LoginPage from "@/pages/LoginPage";
import SignupPage from "@/pages/SignupPage";

const router = createBrowserRouter(
  createRoutesFromElements(
    <>
      {/* Public routes */}
      <Route path="/login" element={<LoginPage />} />
      <Route path="/signup" element={<SignupPage />} />
      <Route path="/" element={<PublicLayout />}>
        <Route index element={<Landing />} />
      </Route>

      {/* Protected routes */}
      <Route path="/something" element={<PrivateLayout />}>
        <Route index element={<Landing />} />
      </Route>
    </>
  )
);

export default router;
