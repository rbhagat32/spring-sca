import { createRootRouteWithContext, Outlet } from "@tanstack/react-router";
import { Fragment } from "react";
import { Toaster } from "sonner";

import { PageNotFound } from "@/components/custom/page-not-found";

export const Route = createRootRouteWithContext<IRouterContext>()({
  component: RootComponent,
  notFoundComponent: PageNotFound,
});

function RootComponent() {
  return (
    <Fragment>
      <Outlet />
      <Toaster richColors position="top-center" duration={5000} />
    </Fragment>
  );
}
