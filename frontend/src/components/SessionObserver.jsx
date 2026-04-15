import { useEffect } from "react";
import { useNavigate } from "react-router-dom";

export default function SessionObserver() {
  const navigate = useNavigate();

  useEffect(() => {
    const handleStorageChange = (e) => {
      // Check for logout in other tabs (token removal)
      if (e.key === "token" && !e.newValue) {
        // Just redirect, don't double-remove the token
        navigate("/");
      }
    };

    window.addEventListener("storage", handleStorageChange);

    return () => {
      window.removeEventListener("storage", handleStorageChange);
    };
  }, [navigate]);

  return null; // This component doesn't render anything
}
