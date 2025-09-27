import { useState } from "react";
import { Info, X } from "lucide-react";
import { Button } from "../ui/button";

interface OnlineUsersProps {
  onlineUsers: IUser[];
}

export function OnlineUsers({ onlineUsers }: OnlineUsersProps) {
  const [isOpen, setIsOpen] = useState<boolean>(false);

  return (
    <div className="absolute top-1 right-1">
      {!isOpen && (
        <Button size="icon" variant="secondary" onClick={() => setIsOpen(true)}>
          <Info className="h-4 w-4" />
        </Button>
      )}

      {isOpen && (
        <div className="w-56 rounded-xl border bg-white p-3 shadow-lg dark:bg-neutral-900">
          <div className="mb-2 flex items-center justify-between">
            <h4 className="text-sm font-semibold">Online Users</h4>
            <Button size="icon" variant="ghost" onClick={() => setIsOpen(false)} className="size-6">
              <X className="size-4" />
            </Button>
          </div>

          {onlineUsers.length > 0 ? (
            <ul className="max-h-60 overflow-y-auto text-sm">
              {onlineUsers.map((user) => (
                <li
                  key={user?.id}
                  className="rounded-md px-2 py-1 hover:bg-neutral-100 dark:hover:bg-neutral-800"
                >
                  {user?.name}
                </li>
              ))}
            </ul>
          ) : (
            <p className="text-sm text-neutral-500">No users online</p>
          )}
        </div>
      )}
    </div>
  );
}
