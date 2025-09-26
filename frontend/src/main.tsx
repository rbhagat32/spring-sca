import { StrictMode } from "react";
import { createRoot } from "react-dom/client";
import "@/index.css";
import { App } from "@/App.tsx";
import { BrowserRouter } from "react-router-dom";
import { UserProvider } from "@/context/user-provider";
import { SocketProvider } from "@/context/socket-provider";

createRoot(document.getElementById("root")!).render(
  <StrictMode>
    <BrowserRouter>
      <SocketProvider>
        <UserProvider>
          <App />
        </UserProvider>
      </SocketProvider>
    </BrowserRouter>
  </StrictMode>
);
