import { api } from "@/utils/axios";
import { createContext, useContext, useEffect, useState, type ReactNode } from "react";

type UserContextType = {
  user: IUser;
  loading: boolean;
  login: (email: string, password: string) => Promise<void>;
  signup: (name: string, email: string, password: string, avatar?: File) => Promise<void>;
  logout: () => void;
  getUserById: (id: string) => Promise<IUser>;
};

const UserContext = createContext<UserContextType | undefined>(undefined);

function UserProvider({ children }: { children: ReactNode }) {
  const [user, setUser] = useState<IUser>(null);
  const [loading, setLoading] = useState(true);

  useEffect(() => {
    getLoggedInUser();
  }, []);

  const login = async (email: string, password: string) => {
    setLoading(true);
    try {
      await api.post("/api/auth/login", { email, password });
      await getLoggedInUser();
    } catch (err) {
      console.error(err);
    } finally {
      setLoading(false);
    }
  };

  const signup = async (name: string, email: string, password: string, avatar?: File) => {
    setLoading(true);
    try {
      const formData = new FormData();
      formData.append("name", name);
      formData.append("email", email);
      formData.append("password", password);

      if (avatar) {
        formData.append("avatar", avatar);
      }

      await api.post("/api/auth/register", formData, {
        headers: { "Content-Type": "multipart/form-data" },
      });
      await getLoggedInUser();
    } catch (err) {
      console.error(err);
    } finally {
      setLoading(false);
    }
  };

  const logout = async () => {
    setLoading(true);
    try {
      await api.post("/api/auth/logout");
      setUser(null);
    } catch (err) {
      console.error(err);
      setUser(null);
    } finally {
      setLoading(false);
    }
  };

  const getLoggedInUser = async (): Promise<void> => {
    setLoading(true);
    try {
      const res = await api.get<IUser>("/api/auth/get-user-1");
      setUser(res.data);
    } catch (err) {
      console.error(err);
      setUser(null);
    } finally {
      setLoading(false);
    }
  };

  const getUserById = async (id: string): Promise<IUser> => {
    try {
      const res = await api.get<IUser>(`/api/user/${id}`);
      return res.data;
    } catch (err) {
      console.error(err);
      return null;
    }
  };

  return (
    <UserContext.Provider value={{ user, loading, login, signup, logout, getUserById }}>
      {children}
    </UserContext.Provider>
  );
}

function useUser() {
  const state = useContext(UserContext);
  if (!state) throw new Error("useUser must be used within UserProvider");
  return state;
}

export { UserProvider, useUser };
