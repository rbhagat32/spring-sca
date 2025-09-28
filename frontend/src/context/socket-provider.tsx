import { api } from "@/utils/axios";
import { Client, type IMessage as StompMessageType } from "@stomp/stompjs";
import {
  createContext,
  useCallback,
  useContext,
  useEffect,
  useRef,
  useState,
  type ReactNode,
} from "react";
import SockJS from "sockjs-client";
import { useUser } from "./user-provider";
import { toast } from "sonner";

const SERVER_URL = import.meta.env.VITE_BACKEND_URL;

interface SocketProviderProps {
  children?: ReactNode;
}

interface ISocketContext {
  sendMessage: (msg: string) => void;
  messages: IMessage[];
  onlineUsers: IUser[];
  loading: boolean;
  isConnected: boolean;
}

const SocketContext = createContext<ISocketContext | null>(null);

const SocketProvider: React.FC<SocketProviderProps> = ({ children }) => {
  const [messages, setMessages] = useState<IMessage[]>([]);
  const [onlineUsers, setOnlineUsers] = useState<IUser[]>([]);
  const [loading, setLoading] = useState<boolean>(true);
  const [isConnected, setIsConnected] = useState<boolean>(false);
  const stompClientRef = useRef<Client | null>(null);

  const { user } = useUser();

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

        stompClient.subscribe("/topic/message", (message: StompMessageType) => {
          const receivedMessage: IMessage = JSON.parse(message.body);
          console.log("Message Received from Server:", receivedMessage);
          setMessages((prevMessages) => [...prevMessages, receivedMessage]);
        });

        stompClient.subscribe("/topic/online-users", (message: StompMessageType) => {
          const users: IUser[] = JSON.parse(message.body);
          console.log("Online Users Received from Server:", users);
          setOnlineUsers(users);
        });

        stompClient.subscribe("/topic/errors", (message: StompMessageType) => {
          const error: string = JSON.parse(message.body);
          console.error("Error Received from Server:", error);
          toast.error(`Message must be less than 100 characters !`);
        });

        fetchMessages();
        getOnlineUsers();
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
        destination: "/emit/message",
        body: JSON.stringify({ content: msg, senderId: user?.id }),
      });
    } else {
      console.error("STOMP client not connected");
    }
  }, []);

  const fetchMessages = useCallback(async () => {
    setLoading(true);
    try {
      const response = await api.get<IMessage[]>(`/api/message/get-all-messages`);
      setMessages(response.data);
    } catch (err) {
      console.error("Error fetching messages:", err);
    } finally {
      setLoading(false);
    }
  }, []);

  const getOnlineUsers = useCallback(async () => {
    setLoading(true);
    try {
      const response = await api.get<IUser[]>(`/api/user/get-online-users`);
      setOnlineUsers(response.data);
    } catch (err) {
      console.error("Error fetching online users:", err);
    } finally {
      setLoading(false);
    }
  }, [onlineUsers]);

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
    <SocketContext.Provider value={{ sendMessage, messages, onlineUsers, loading, isConnected }}>
      {children}
    </SocketContext.Provider>
  );
};

const useSocket = () => {
  const state = useContext(SocketContext);
  if (!state) throw new Error("useSocket must be used within a SocketProvider");
  return state;
};

export { SocketProvider, useSocket };
