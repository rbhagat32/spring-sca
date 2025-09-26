import { useState } from "react";
import { toast } from "sonner";

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
        className="flex-1 rounded-xl border border-zinc-500 px-4 py-2.5 text-sm text-gray-100 placeholder-gray-400 transition-all duration-200 focus:border-gray-600 focus:ring-2 focus:ring-gray-600/20 focus:outline-none"
        value={message}
        onChange={(e) => setMessage(e.target.value)}
        onKeyDown={(e) => {
          if (e.key === "Enter") handleSendMessage();
        }}
      />
      <button
        onClick={handleSendMessage}
        className="cursor-pointer rounded-xl bg-zinc-800 p-3 text-white shadow-lg transition-all duration-200 hover:bg-zinc-700 hover:shadow-xl active:bg-zinc-600"
      >
        <svg className="h-4 w-4" fill="none" stroke="currentColor" viewBox="0 0 24 24">
          <path
            strokeLinecap="round"
            strokeLinejoin="round"
            strokeWidth={2}
            d="M12 5l7 7-7 7M5 12h14"
          />
        </svg>
      </button>
    </div>
  );
}
