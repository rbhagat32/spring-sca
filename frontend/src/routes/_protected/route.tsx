import { createFileRoute, Outlet, redirect } from "@tanstack/react-router";

import { NavBar } from "@/components/core/navbar";
import { SocketProvider } from "@/context/socket-provider";

export const Route = createFileRoute("/_protected")({
  beforeLoad: ({ context }) => {
    const { isLoggedIn } = context;
    if (!isLoggedIn) throw redirect({ to: "/login" });
  },
  component: RouteComponent,
});

function RouteComponent() {
  return (
    <SocketProvider>
      <main className="relative mx-auto min-h-screen max-w-screen-sm overflow-y-hidden rounded-sm border bg-zinc-900 p-4">
        <NavBar />
        <Outlet />
      </main>
    </SocketProvider>
  );
}
