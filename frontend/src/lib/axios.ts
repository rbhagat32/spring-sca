import axios from "axios";

const SERVER_URL = import.meta.env.VITE_BACKEND_URL;

const instance = axios.create({
  baseURL: SERVER_URL as string,
  withCredentials: true,
});

export { instance as api };
