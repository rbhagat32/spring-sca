import { createFileRoute } from "@tanstack/react-router";

import { MessageContainer } from "@/components/core/message-container";
import { MessageInput } from "@/components/core/message-input";
import { useSocket } from "@/context/socket-provider";

export const Route = createFileRoute("/_protected/")({
  component: HomePage,
});

function HomePage() {
  const { sendMessage, messages, onlineUsers, loading } = useSocket();

  return (
    <section className="relative">
      <MessageContainer messages={messages} onlineUsers={onlineUsers} loading={loading} />
      <MessageInput sendMessage={sendMessage} />
    </section>
  );
}
