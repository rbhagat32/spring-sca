import { Button } from "@/components/ui/button";
import { useUser } from "@/context/user-provider";
import { isAdmin } from "@/helpers/check-admin";

const NavBar = () => {
  const { loading, user, logout } = useUser();

  return (
    <nav className="mb-4 flex items-center justify-between border-b border-zinc-500 pb-4">
      <div className="flex items-center gap-10">
        <div className="flex items-center gap-2">
          <div className="size-10 overflow-hidden rounded-full">
            <img
              src={loading ? "/favicon.svg" : user?.avatarUrl || "/favicon.svg"}
              alt="User's Profile Picture"
              className="size-full object-cover"
              referrerPolicy="no-referrer"
            />
          </div>
          <h1 className="text-xl font-semibold tracking-tighter">
            {user?.name}
            {isAdmin(user) && <span className="text-blue-500"> (Admin)</span>}
          </h1>
        </div>
      </div>

      <Button
        onClick={logout}
        disabled={loading}
        className="bg-red-500 text-white hover:bg-red-600"
      >
        Logout
      </Button>
    </nav>
  );
};

export { NavBar };
