import axios from "axios";

const API = axios.create({
  baseURL: import.meta.env.VITE_API_URL || "",
});

// attach token automatically
API.interceptors.request.use((req) => {
  const token = localStorage.getItem("token");
  if (token && token !== "undefined" && token !== "null") {
    req.headers.Authorization = `Bearer ${token}`;
  } else {
    // Clean up if it's invalid
    localStorage.removeItem("token");
    localStorage.removeItem("role");
    if (window.location.pathname !== "/" && window.location.pathname !== "/register") {
       window.location.href = "/";
    }
  }
  return req;
});

export default API;