import { PageLoader } from "@/components/custom/page-loader";
import { useUser } from "@/context/user-provider";
import { Routing } from "@/utils/routing";
import { Toaster } from "sonner";

export function App() {
  const { loading, user } = useUser();

  return (
    <>
      {loading ? <PageLoader /> : <Routing isLoggedIn={!!user} />}
      <Toaster richColors position="top-center" duration={5000} />
    </>
  );
}
