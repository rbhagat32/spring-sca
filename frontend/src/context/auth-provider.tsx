import { createContext, useContext, useEffect, useState, type ReactNode } from "react";

import { api } from "@/lib/axios";
import { router } from "@/main";

type IAuthContext = {
  user: IUser;
  loading: boolean;
  submitting: boolean;
  login: (email: string, password: string) => Promise<void>;
  signup: (name: string, email: string, password: string, avatar?: File) => Promise<void>;
  logout: () => void;
};

const AuthContext = createContext<IAuthContext | undefined>(undefined);

function AuthProvider({ children }: { children: ReactNode }) {
  const [user, setUser] = useState<IUser>(null);
  const [loading, setLoading] = useState<boolean>(true);
  const [submitting, setSubmitting] = useState<boolean>(false);

  useEffect(() => {
    getLoggedInUser();
  }, []);

  // update the router context when user state changes
  useEffect(() => {
    router.invalidate();
  }, [user]);

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
      setUser(null);
    } finally {
      setLoading(false);
    }
  };

  return (
    <AuthContext.Provider value={{ user, loading, submitting, login, signup, logout }}>
      {children}
    </AuthContext.Provider>
  );
}

function useAuth() {
  const state = useContext(AuthContext);
  if (!state) throw new Error("useAuth must be used within AuthProvider");
  return state;
}

export { AuthProvider, useAuth };
