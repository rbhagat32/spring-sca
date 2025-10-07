import { useEffect, useRef } from "react";
import { PageLoader } from "../partials/page-loader";
import moment from "moment";
import { isAdmin } from "@/helpers/check-admin";

interface MessageContainerProps {
  messages: IMessage[];
  loading: boolean;
}

export function MessageContainer({ messages, loading }: MessageContainerProps) {
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
            className="mx-auto flex w-[350px] items-center justify-between rounded-2xl bg-zinc-800 px-5 py-3 text-gray-100 shadow-lg"
          >
            <div>
              <p className="mb-2 font-semibold break-words">{msg?.content}</p>
              <p className="text-sm text-zinc-500">
                {moment(msg?.createdAt).format("DD MMM YYYY [at] hh:mm A")}
              </p>
              <p className="text-sm text-zinc-500">
                {"by "}
                {msg?.sender?.name}
                {isAdmin(msg?.sender!) && " (Admin)"}
              </p>
            </div>

            <div className="size-12 overflow-hidden rounded-full">
              <img
                src={msg?.sender?.avatarUrl || "/favicon.svg"}
                alt="Sender Profile Picture"
                className="size-full object-cover"
              />
            </div>
          </div>
        ))
      )}
    </div>
  );
}
