import { StrictMode } from "react";
import { createRoot } from "react-dom/client";

import { AuthProvider } from "@/context/auth-provider";
import { ThemeProvider } from "@/context/theme-provider";
import "@/index.css";
import { Router } from "@/router";

createRoot(document.getElementById("root")!).render(
  <StrictMode>
    <ThemeProvider>
      <AuthProvider>
        <Router />
      </AuthProvider>
    </ThemeProvider>
  </StrictMode>
);
