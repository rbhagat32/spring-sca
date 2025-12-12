import { MessageContainer } from "@/components/core/message-container";
import { MessageInput } from "@/components/core/message-input";
import { OnlineUsers } from "@/components/core/online-users";
import { useSocket } from "@/context/socket-provider";

export function HomePage() {
  const { sendMessage, messages, onlineUsers, loading } = useSocket();

  return (
    <section className="relative">
      <OnlineUsers onlineUsers={onlineUsers} />
      <MessageContainer messages={messages} onlineUsers={onlineUsers} loading={loading} />
      <MessageInput sendMessage={sendMessage} />
    </section>
  );
}
