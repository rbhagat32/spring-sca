import { Info, X } from "lucide-react";
import { useEffect, useRef, useState } from "react";

import { Button } from "@/components/ui/button";

interface OnlineUsersProps {
  onlineUsers: IUser[];
}

export function OnlineUsers({ onlineUsers }: OnlineUsersProps) {
  const [isOpen, setIsOpen] = useState<boolean>(false);
  const panelRef = useRef<HTMLDivElement>(null);

  useEffect(() => {
    function handleClickOutside(event: MouseEvent) {
      if (panelRef.current && !panelRef.current.contains(event.target as Node)) {
        setIsOpen(false);
      }
    }

    if (isOpen) {
      document.addEventListener("mousedown", handleClickOutside);
    } else {
      document.removeEventListener("mousedown", handleClickOutside);
    }

    return () => {
      document.removeEventListener("mousedown", handleClickOutside);
    };
  }, [isOpen]);

  return (
    <div className="absolute right-2">
      {!isOpen && (
        <Button size="icon" variant="outline" onClick={() => setIsOpen(true)} className="border">
          <Info className="size-4" />
        </Button>
      )}

      {isOpen && (
        <div
          ref={panelRef}
          className="w-56 rounded-xl border bg-white p-3 shadow-lg dark:bg-neutral-900"
        >
          <div className="mb-4 flex items-center justify-between">
            <h4 className="font-semibold">Online Users</h4>
            <Button size="icon" variant="ghost" onClick={() => setIsOpen(false)} className="size-6">
              <X className="size-4" />
            </Button>
          </div>

          <ul className="max-h-60 overflow-y-auto text-sm">
            {onlineUsers.map((user) => (
              <li key={user?.id} className="mb-2 rounded-md bg-zinc-800 p-2">
                {user?.name}
              </li>
            ))}
          </ul>
        </div>
      )}
    </div>
  );
}
