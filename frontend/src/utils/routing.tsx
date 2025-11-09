import { Route, Routes } from "react-router-dom";
import { ProtectedRoute } from "@/utils/protected-route";
import { NotFound } from "@/components/custom/page-not-found";
import { HomePage } from "@/pages/home";
import { AuthLayout } from "@/layouts/auth";
import { LoginPage } from "@/pages/login";
import { SignUpPage } from "@/pages/signup";
import { RootLayout } from "@/layouts/root";
import { SocketProvider } from "@/context/socket-provider";

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

      <Route path="*" element={<NotFound />} />
    </Routes>
  );
};

export { Routing };
