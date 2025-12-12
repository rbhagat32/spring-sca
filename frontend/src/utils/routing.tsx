import { Route, Routes } from "react-router-dom";

import { PageNotFound } from "@/components/custom/page-not-found";
import { SocketProvider } from "@/context/socket-provider";
import { AuthLayout } from "@/layouts/auth";
import { RootLayout } from "@/layouts/root";
import { HomePage } from "@/pages/home";
import { LoginPage } from "@/pages/login";
import { SignUpPage } from "@/pages/signup";
import { ProtectedRoute } from "@/utils/protected-route";

const Routing = ({ isLoggedIn = false }: { isLoggedIn: boolean }) => {
  return (
    <Routes>
      <Route
        element={
          <SocketProvider>
            <ProtectedRoute isLoggedIn={isLoggedIn} redirect="/login" />
          </SocketProvider>
        }
      >
        <Route
          path="/"
          element={
            <RootLayout>
              <HomePage />
            </RootLayout>
          }
        />
      </Route>

      <Route element={<ProtectedRoute isLoggedIn={!isLoggedIn} redirect="/" />}>
        <Route
          path="/login"
          element={
            <AuthLayout>
              <LoginPage />
            </AuthLayout>
          }
        />
        <Route
          path="/signup"
          element={
            <AuthLayout>
              <SignUpPage />
            </AuthLayout>
          }
        />
      </Route>

      <Route path="*" element={<PageNotFound />} />
    </Routes>
  );
};

export { Routing };
