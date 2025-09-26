import { MessageContainer } from "@/components/core/message-container";
import { MessageInput } from "@/components/core/message-input";
import { useSocket } from "@/context/socket-provider";

export function HomePage() {
  const { sendMessage, messages, loading } = useSocket();

  return (
    <>
      <MessageContainer messages={messages} loading={loading} />
      <MessageInput sendMessage={sendMessage} />
    </>
  );
}
