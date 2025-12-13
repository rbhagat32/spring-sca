import { createRouter } from "@tanstack/react-router";
import { StrictMode } from "react";
import { createRoot } from "react-dom/client";

import { App } from "@/app";
import { AuthProvider } from "@/context/auth-provider";
import "@/index.css";
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

createRoot(document.getElementById("root")!).render(
  <StrictMode>
    <AuthProvider>
      <App />
    </AuthProvider>
  </StrictMode>
);
