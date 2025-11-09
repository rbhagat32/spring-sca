import {
  createContext,
  useCallback,
  useContext,
  useEffect,
  useRef,
  useState,
  type ReactNode,
} from "react";
import { api } from "@/utils/axios";
import SockJS from "sockjs-client";
import { Client, type IMessage as StompMessageType } from "@stomp/stompjs";
import { useUser } from "./user-provider";
import { toast } from "sonner";

const SERVER_URL = import.meta.env.VITE_BACKEND_URL;

interface ISocketContext {
  sendMessage: (msg: string) => void;
  messages: IMessage[];
  onlineUsers: IUser[];
  loading: boolean;
  isConnected: boolean;
}

const SocketContext = createContext<ISocketContext | null>(null);

const SocketProvider: React.FC<{ children?: ReactNode }> = ({ children }) => {
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
      onConnect: () => {
        setLoading(false);
        setIsConnected(true);

        // socket listeners
        stompClient.subscribe("/topic/message", (message: StompMessageType) => {
          const receivedMessage: IMessage = JSON.parse(message.body);
          setMessages((prevMessages) => [...prevMessages, receivedMessage]);
        });

        stompClient.subscribe("/topic/online-users", (message: StompMessageType) => {
          const users: IUser[] = JSON.parse(message.body);
          setOnlineUsers(users);
        });

        stompClient.subscribe("/topic/errors", (message: StompMessageType) => {
          const error: string = JSON.parse(message.body);
          console.error("Error Received from Server:", error);
          toast.error(`Message must be less than 100 characters !`);
        });

        getAllMessages();
        getOnlineUsers();
      },
      onDisconnect: () => setIsConnected(false),
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

  useEffect(() => {
    connect();

    return () => {
      disconnect();
    };
  }, [connect, disconnect]);

  // attempt to reconnect every 5 seconds if disconnected
  useEffect(() => {
    if (!isConnected && !loading) {
      const timer = setInterval(() => {
        console.log("Attempting to reconnect...");
        connect();
      }, 5000);

      return () => clearInterval(timer);
    }
  }, [isConnected, loading, connect]);

  const sendMessage: ISocketContext["sendMessage"] = useCallback((msg: string) => {
    if (stompClientRef.current && stompClientRef.current.connected) {
      stompClientRef.current.publish({
        destination: "/emit/message-scalable",
        body: JSON.stringify({ content: msg, senderId: user?.id }),
      });
    } else {
      console.error("STOMP client not connected");
    }
  }, []);

  const getAllMessages = useCallback(async () => {
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
