import {
  createContext,
  useCallback,
  useContext,
  useEffect,
  useRef,
  useState,
  type ReactNode,
} from "react";
import { Client, type IMessage } from "@stomp/stompjs";
import SockJS from "sockjs-client";

const SERVER_URL = import.meta.env.VITE_BACKEND_URL;

interface SocketProviderProps {
  children?: ReactNode;
}

interface ISocketContext {
  sendMessage: (msg: string) => void;
  messages: string[];
  loading: boolean;
  isConnected: boolean;
}

interface StompMessage {
  id: string;
  content: string;
  createdAt: string;
}

const SocketContext = createContext<ISocketContext | null>(null);

const useSocket = () => {
  const state = useContext(SocketContext);
  if (!state) throw new Error("useSocket must be used within a SocketProvider");
  return state;
};

const SocketProvider: React.FC<SocketProviderProps> = ({ children }) => {
  const [messages, setMessages] = useState<string[]>([]);
  const [loading, setLoading] = useState<boolean>(true);
  const [isConnected, setIsConnected] = useState<boolean>(false);
  const stompClientRef = useRef<Client | null>(null);

  const connect = useCallback(() => {
    const socket = new SockJS(`${SERVER_URL}/api/ws`);

    const stompClient = new Client({
      webSocketFactory: () => socket,
      reconnectDelay: 5000,
      heartbeatIncoming: 4000,
      heartbeatOutgoing: 4000,
      onConnect: () => {
        console.log("Connected to STOMP broker");
        setIsConnected(true);
        setLoading(false);

        stompClient.subscribe("/topic/notifications", (message: IMessage) => {
          try {
            const receivedMessage: string = JSON.parse(message.body).message;
            console.log("Message Received from Server:", receivedMessage);

            setMessages((prevMessages) => [...prevMessages, receivedMessage]);
          } catch (error) {
            console.error("Error parsing message:", error);
          }
        });

        fetchMessages();
      },
      onDisconnect: () => {
        console.log("Disconnected from STOMP broker");
        setIsConnected(false);
      },
      onStompError: (frame) => {
        console.error("STOMP error:", frame);
        setIsConnected(false);
      },
      onWebSocketError: (event) => {
        console.error("WebSocket error:", event);
        setIsConnected(false);
      },
    });

    stompClient.activate();
    stompClientRef.current = stompClient;
  }, []);

  const disconnect = useCallback(() => {
    if (stompClientRef.current) {
      stompClientRef.current.deactivate();
      stompClientRef.current = null;
    }
    setIsConnected(false);
  }, []);

  const sendMessage: ISocketContext["sendMessage"] = useCallback((msg: string) => {
    console.log(`Sending Message to Server: ${msg}`);

    if (stompClientRef.current && stompClientRef.current.connected) {
      stompClientRef.current.publish({
        destination: "/app/sendMessage",
        body: JSON.stringify({ message: msg }),
      });
    } else {
      console.error("STOMP client not connected");
    }
  }, []);

  const fetchMessages = useCallback(async () => {
    setLoading(true);
    try {
      const res = await fetch(`${SERVER_URL}/api/messages`);
      if (!res.ok) throw new Error("Failed to Fetch Messages!");

      const data: StompMessage[] = await res.json();
      setMessages(data.map((msg) => msg.content));
    } catch (err) {
      console.error("Error fetching messages:", err);
    } finally {
      setLoading(false);
    }
  }, []);

  useEffect(() => {
    connect();

    return () => {
      disconnect();
    };
  }, [connect, disconnect]);

  // auto-reconnect when Socket connection is lost
  useEffect(() => {
    if (!isConnected && !loading) {
      const timer = setTimeout(() => {
        console.log("Attempting to reconnect...");
        connect();
      }, 5000);

      return () => clearTimeout(timer);
    }
  }, [isConnected, loading, connect]);

  return (
    <SocketContext.Provider value={{ sendMessage, messages, loading, isConnected }}>
      {children}
    </SocketContext.Provider>
  );
};

export { SocketProvider, useSocket };
