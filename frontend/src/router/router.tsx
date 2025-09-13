import PrivateLayout from "@/layouts/PrivateLayout";
import PublicLayout from "@/layouts/PublicLayout";
import Landing from "@/pages/Landing";
import Login from "@/pages/Login";
import {
  createBrowserRouter,
  createRoutesFromElements,
  Route,
} from "react-router-dom";

const router = createBrowserRouter(
  createRoutesFromElements(
    <>
      {/* Public routes */}
      <Route path="/" element={<PublicLayout />}>
        <Route index element={<Landing />} />
        <Route path="/login" element={<Login />} />
      </Route>

      {/* Protected routes */}
      <Route path="/something" element={<PrivateLayout />}>
        <Route index element={<Landing />} />
      </Route>
    </>
  )
);

export default router;
