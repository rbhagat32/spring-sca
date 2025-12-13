import { createRootRouteWithContext, Outlet } from "@tanstack/react-router";
import { TanStackRouterDevtools } from "@tanstack/react-router-devtools";
import { Fragment } from "react";
import { Toaster } from "sonner";

import { PageLoader } from "@/components/custom/page-loader";
import { PageNotFound } from "@/components/custom/page-not-found";
import { useAuth } from "@/context/auth-provider";
import { SocketProvider } from "@/context/socket-provider";

export const Route = createRootRouteWithContext<IRouterContext>()({
  component: RootComponent,
  notFoundComponent: PageNotFound,
});

function RootComponent() {
  const { loading } = useAuth();

  return (
    <Fragment>
      {loading ? (
        <PageLoader fullScreen />
      ) : (
        <SocketProvider>
          <Outlet />
        </SocketProvider>
      )}

      <Toaster richColors position="top-center" duration={5000} />
      <TanStackRouterDevtools />
    </Fragment>
  );
}
