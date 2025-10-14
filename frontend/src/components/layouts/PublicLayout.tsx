import { Outlet } from "react-router-dom";

const PublicLayout = () => {
  return (
    <>
      <div>PublicLayout</div>
      <div>header component here</div>
      <Outlet />
    </>
  );
};

export default PublicLayout;
