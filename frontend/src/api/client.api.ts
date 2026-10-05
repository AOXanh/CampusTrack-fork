import axios from "axios";

const BASE_URL = process.env.NEXT_PUBLIC_BASE_API_URL;

const apiClient = axios.create({
  baseURL: BASE_URL ?? "/api",
  headers: {
    "Content-Type": "application/json"
  }
});

export default apiClient;