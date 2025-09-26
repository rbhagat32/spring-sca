import { NavBar } from "@/components/core/navbar";

export function RootLayout({ children }: { children: React.ReactNode }) {
  return (
    <main className="relative mx-auto min-h-screen max-w-screen-sm overflow-y-hidden rounded-sm border bg-zinc-900 p-4">
      <NavBar />
      <section>{children}</section>
    </main>
  );
}
