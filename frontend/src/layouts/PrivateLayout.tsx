import { Outlet } from "react-router-dom";

const PrivateLayout = () => {
  return (
    <>
      <div>PrivateLayout</div>;<div>header component here</div>
      <div>sidebar component here</div>
      <Outlet />
    </>
  );
};
export default PrivateLayout;
