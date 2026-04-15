import { Navigate, Outlet } from "react-router-dom";

export default function RequireAuth() {
  const token = localStorage.getItem("token");

  // Robust check for invalid token values
  const isAuthenticated = token && token !== "undefined" && token !== "null";

  if (!isAuthenticated) {
    return <Navigate to="/" replace />;
  }

  return <Outlet />;
}
