import { type JSX } from "react";
import { Navigate, Outlet } from "react-router-dom";

interface ProtectedRouteProps {
  children?: JSX.Element;
  isLoggedIn: boolean;
  redirect: string;
}

const ProtectedRoute = ({ children, isLoggedIn, redirect }: ProtectedRouteProps) => {
  if (!isLoggedIn) return <Navigate to={redirect} />;

  return children ? children : <Outlet />;
};

export { ProtectedRoute };
