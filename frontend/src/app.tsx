import { useAuth } from "@/context/auth-provider";
import { router } from "@/main";
import { RouterProvider } from "@tanstack/react-router";

export function App() {
  const isLoggedIn = !!useAuth().user;

  return <RouterProvider router={router} context={{ isLoggedIn }} />;
}
