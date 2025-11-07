import { PageLoader } from "@/components/custom/page-loader";
import { isAdmin } from "@/helpers/check-admin";
import moment from "moment";
import { useEffect, useRef } from "react";

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
      style={{ height: "calc(100vh - 150px)", overflowY: "auto" }}
      className="flex-1 space-y-3 p-4"
    >
      {loading ? (
        <PageLoader />
      ) : (
        messages.map((msg, index) => (
          <div
            key={index}
            className="mx-auto flex w-[350px] items-center justify-between rounded-2xl bg-zinc-800 px-5 py-3 text-zinc-100 shadow-lg"
          >
            <div>
              <p className="mb-2 font-semibold wrap-break-word">{msg?.content}</p>
              <p className="text-sm text-zinc-500">
                {moment(msg?.createdAt).format("DD MMM YYYY [at] hh:mm A")}
              </p>
              <p className="text-sm text-zinc-500">
                {"by "}
                <span className="text-zinc-300">{msg?.sender?.name}</span>
                {isAdmin(msg?.sender!) && (
                  <span className="font-semibold text-blue-500"> (Admin)</span>
                )}
              </p>
            </div>

            <div
              className={`size-12 overflow-hidden rounded-full ${onlineUsers.some((user) => user?.id === msg?.sender?.id) && "ring-2 ring-green-500"}`}
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
