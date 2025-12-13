import moment from "moment";
import { useEffect, useRef } from "react";

import { PageLoader } from "@/components/custom/page-loader";
import { isAdmin } from "@/lib/check-admin";

interface MessageContainerProps {
  messages: IMessage[];
  onlineUsers: IUser[];
  loading: boolean;
}

export function MessageContainer({ messages, onlineUsers, loading }: MessageContainerProps) {
  const messagesContainerRef = useRef<HTMLDivElement | null>(null);

  useEffect(() => {
    const container = messagesContainerRef.current;
    if (container) {
      container.scrollTop = container.scrollHeight;
    }
  }, [messages]);

  return (
    <div
      ref={messagesContainerRef}
      style={{ height: "calc(100vh - 145px)", overflowY: "auto" }}
      className="flex-1 space-y-3 p-4"
    >
      {loading ? (
        <PageLoader />
      ) : (
        messages.map((msg, index) => (
          <div
            key={index}
            className="bg-muted mx-auto flex w-100 items-center justify-between gap-4 rounded-2xl border px-5 py-3 shadow-sm"
          >
            <div className="flex min-w-0 flex-col">
              <p className="mb-2 font-semibold wrap-break-word">{msg?.content}</p>
              <p className="text-muted-foreground text-sm">
                {moment(msg?.createdAt).format("DD MMM YYYY [at] hh:mm A")}
              </p>
              <p className="text-muted-foreground text-sm">
                {"by "}
                <span className="text-card-foreground">{msg?.sender?.name}</span>
                {isAdmin(msg?.sender!) && (
                  <span className="font-semibold text-blue-500"> (Admin)</span>
                )}
              </p>
            </div>

            <div
              className={`size-12 shrink-0 overflow-hidden rounded-full ${onlineUsers.some((user) => user?.id === msg?.sender?.id) && "ring-2 ring-green-500"}`}
            >
              <img
                src={msg?.sender?.avatarUrl || "/favicon.svg"}
                alt="Sender Profile Picture"
                className="size-full object-cover"
                referrerPolicy="no-referrer"
              />
            </div>
          </div>
        ))
      )}
    </div>
  );
}
