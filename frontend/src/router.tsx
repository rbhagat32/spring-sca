import { createRouter, RouterProvider } from "@tanstack/react-router";

import { PageLoader } from "@/components/custom/page-loader";
import { useAuth } from "@/context/auth-provider";
import { routeTree } from "@/routeTree.gen";

export const router = createRouter({
  routeTree,
  context: {
    isLoggedIn: undefined!,
  } satisfies IRouterContext,
});

declare module "@tanstack/react-router" {
  interface Register {
    router: typeof router;
  }
}

export function Router() {
  const { loading, user } = useAuth();

  return loading ? (
    <PageLoader fullScreen />
  ) : (
    <RouterProvider router={router} context={{ isLoggedIn: !!user }} />
  );
}
