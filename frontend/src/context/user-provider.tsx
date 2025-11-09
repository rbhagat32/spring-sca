import { api } from "@/utils/axios";
import { createContext, useContext, useEffect, useState, type ReactNode } from "react";

type UserContextType = {
  user: IUser;
  loading: boolean;
  submitting: boolean;
  login: (email: string, password: string) => Promise<void>;
  signup: (name: string, email: string, password: string, avatar?: File) => Promise<void>;
  logout: () => void;
};

const UserContext = createContext<UserContextType | undefined>(undefined);

function UserProvider({ children }: { children: ReactNode }) {
  const [user, setUser] = useState<IUser>(null);
  const [loading, setLoading] = useState<boolean>(true);
  const [submitting, setSubmitting] = useState<boolean>(false);

  useEffect(() => {
    getLoggedInUser();
  }, []);

  const login = async (email: string, password: string) => {
    setSubmitting(true);
    try {
      await api.post("/api/auth/login", { email, password });
      await getLoggedInUser();
    } catch (err) {
      console.error(err);
      throw err;
    } finally {
      setSubmitting(false);
    }
  };

  const signup = async (name: string, email: string, password: string, avatar?: File) => {
    setSubmitting(true);
    try {
      const formData = new FormData();
      formData.append("name", name);
      formData.append("email", email);
      formData.append("password", password);
      if (avatar) formData.append("avatar", avatar);

      await api.post("/api/auth/register", formData, {
        headers: { "Content-Type": "multipart/form-data" },
      });

      await getLoggedInUser();
    } catch (err) {
      console.error(err);
      throw err;
    } finally {
      setSubmitting(false);
    }
  };

  const logout = async () => {
    try {
      await api.post("/api/auth/logout");
      setUser(null);
    } catch (err) {
      console.error(err);
      setUser(null);
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

  return (
    <UserContext.Provider value={{ user, loading, submitting, login, signup, logout }}>
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
