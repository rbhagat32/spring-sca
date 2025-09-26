import { Routing } from "@/utils/routing";
import { PageLoader } from "@/components/partials/page-loader";
import { Toaster } from "sonner";
import { useUser } from "@/context/user-provider";

export function App() {
  const { loading, user } = useUser();

  return loading ? (
    <PageLoader fullScreen={true} />
  ) : (
    <>
      <Toaster richColors position="top-center" duration={5000} />
      <Routing isLoggedIn={!!user} />
    </>
  );
}
