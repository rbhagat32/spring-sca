import { useSocket } from "@/context/socket-provider";
import { OnlineUsers } from "@/components/core/online-users";
import { MessageContainer } from "@/components/core/message-container";
import { MessageInput } from "@/components/core/message-input";

export function HomePage() {
  const { sendMessage, messages, onlineUsers, loading } = useSocket();

  return (
    <section className="relative">
      <OnlineUsers onlineUsers={onlineUsers} />
      <MessageContainer messages={messages} loading={loading} />
      <MessageInput sendMessage={sendMessage} />
    </section>
  );
}
