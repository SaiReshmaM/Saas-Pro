// import React, { useState } from "react";
// import Login from "./Login";
// import Dashboard from "./Dashboard";

// function App() {
//   const [token, setToken] = useState(localStorage.getItem("token"));

//   return (
//     <>
//       {!token ? (
//         <Login setToken={setToken} />
//       ) : (
//         <Dashboard token={token} />
//       )}
//     </>
//   );
// }

// export default App;


import { BrowserRouter, Routes, Route } from "react-router-dom";
import Login from "./pages/Login";
import Register from "./pages/Register";
import Dashboard from "./pages/Dashboard";
import Customers from "./pages/Customers";
import Invoices from "./pages/Invoices";
import Profile from "./pages/Profile";
import Settings from "./pages/Settings";
import Layout from "./components/Layout";
import RequireAuth from "./components/RequireAuth";
import SessionObserver from "./components/SessionObserver";

export default function App() {
  return (
    <BrowserRouter>
      <SessionObserver />
      <Routes>
        <Route path="/" element={<Login />} />
        <Route path="/register" element={<Register />} />
        
        {/* Authenticated Routes protected by RequireAuth */}
        <Route element={<RequireAuth />}>
          <Route element={<Layout />}>
            <Route path="/dashboard" element={<Dashboard />} />
            <Route path="/customers" element={<Customers />} />
            <Route path="/invoices" element={<Invoices />} />
            <Route path="/profile" element={<Profile />} />
            <Route path="/settings" element={<Settings />} />
          </Route>
        </Route>
      </Routes>
    </BrowserRouter>
  );
}