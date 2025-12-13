import { createFileRoute, Outlet, redirect } from "@tanstack/react-router";

import { NavBar } from "@/components/core/navbar";
import { SocketProvider } from "@/context/socket-provider";

export const Route = createFileRoute("/_protected")({
  beforeLoad: ({ context }) => {
    const { isLoggedIn } = context;

    if (isLoggedIn === undefined) return;
    if (!isLoggedIn) throw redirect({ to: "/login" });
  },
  component: RouteComponent,
});

function RouteComponent() {
  return (
    <SocketProvider>
      <main className="bg-card relative mx-auto min-h-screen max-w-screen-sm overflow-y-hidden rounded-sm border p-4">
        <NavBar />
        <Outlet />
      </main>
    </SocketProvider>
  );
}
