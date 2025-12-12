import { Toaster } from "sonner";

import { PageLoader } from "@/components/custom/page-loader";
import { useAuth } from "@/context/auth-provider";
import { Routing } from "@/utils/routing";

export function App() {
  const { loading, user } = useAuth();

  return (
    <>
      {loading ? <PageLoader /> : <Routing isLoggedIn={!!user} />}
      <Toaster richColors position="top-center" duration={5000} />
    </>
  );
}
