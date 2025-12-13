import { ArrowRight } from "lucide-react";
import { useState } from "react";
import { toast } from "sonner";

import { Button } from "@/components/ui/button";

export function MessageInput({ sendMessage }: { sendMessage: (msg: string) => void }) {
  const [message, setMessage] = useState<string>("");

  const handleSendMessage = () => {
    if (message.trim() === "") {
      toast.error("Cannot send empty message !");
    } else {
      sendMessage(message);
      setMessage("");
    }
  };

  return (
    <div className="flex w-full items-center space-x-3">
      <input
        type="text"
        placeholder="Type your message..."
        className="flex-1 rounded-md border border-zinc-500 px-4 py-2 text-sm transition-all duration-200 focus:border-gray-600 focus:ring-2 focus:ring-gray-600/20 focus:outline-none"
        value={message}
        onChange={(e) => setMessage(e.target.value)}
        onKeyDown={(e) => {
          if (e.key === "Enter") handleSendMessage();
        }}
      />
      <Button
        onClick={handleSendMessage}
        size="icon"
        variant="outline"
        className="rounded-md border transition-all duration-200"
      >
        <ArrowRight />
      </Button>
    </div>
  );
}
