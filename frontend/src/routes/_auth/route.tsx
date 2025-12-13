import { createFileRoute, Outlet, redirect } from "@tanstack/react-router";

export const Route = createFileRoute("/_auth")({
  beforeLoad: ({ context }) => {
    const { isLoggedIn } = context;

    if (isLoggedIn === undefined) return;
    if (isLoggedIn) throw redirect({ to: "/" });
  },
  component: RouteComponent,
});

function RouteComponent() {
  return (
    <main className="flex min-h-svh flex-col items-center justify-center p-6 md:p-10">
      <section className="w-full max-w-sm md:max-w-3xl">
        <Outlet />
      </section>
    </main>
  );
}
