import { Info } from "lucide-react";

import { Button } from "@/components/ui/button";
import {
  DropdownMenu,
  DropdownMenuContent,
  DropdownMenuTrigger,
} from "@/components/ui/dropdown-menu";

interface OnlineUsersProps {
  onlineUsers: IUser[];
}

export function OnlineUsers({ onlineUsers }: OnlineUsersProps) {
  return (
    <DropdownMenu>
      <DropdownMenuTrigger asChild>
        <Button size="icon" variant="outline" className="border">
          <Info className="size-4" />
        </Button>
      </DropdownMenuTrigger>

      <DropdownMenuContent>
        <div className="p-1">
          <h4 className="mb-2 text-center font-semibold">Online Users</h4>

          <ul className="max-h-60 overflow-y-auto text-sm">
            {onlineUsers.map((user) => (
              <li key={user?.id} className="bg-muted mb-2 rounded-md p-2">
                {user?.name}
              </li>
            ))}
          </ul>
        </div>
      </DropdownMenuContent>
    </DropdownMenu>
  );
}
