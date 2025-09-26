import { useEffect, useRef } from "react";
import { SkeletonLoader } from "./skeleton-loader";

interface MessageContainerProps {
  messages: string[];
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
        <SkeletonLoader />
      ) : (
        messages.map((msg, index) => (
          <div
            key={index}
            className="mx-auto w-[350px] rounded-2xl bg-gray-800 px-3 py-2 text-gray-100 shadow-lg"
          >
            <p className="text-sm break-words">{msg}</p>
          </div>
        ))
      )}
    </div>
  );
}
