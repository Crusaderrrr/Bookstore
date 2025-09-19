import React, { createContext, useState, useMemo, useEffect } from "react";

export const AppContext = createContext({
  theme: "light",
  toggleTheme: () => {},
  isLoggedIn: false,
  setIsLoggedIn: () => {},
});

export const ContextProvider = ({ children }) => {
  const [theme, setTheme] = useState("light");
  const [isLoggedIn, setIsLoggedIn] = useState(!!localStorage.getItem("accessToken"));
  const [role, setRole] = useState(localStorage.getItem("userRole"));

  useEffect(() => {
    document.documentElement.setAttribute("data-bs-theme", theme);
  }, [theme]);

  const toggleTheme = () => {
    setTheme((prevTheme) => (prevTheme === "light" ? "dark" : "light"));
  };

  const value = useMemo(() => ({ theme, toggleTheme, isLoggedIn, setIsLoggedIn, role, setRole }), [theme, isLoggedIn, role]);

  return (
    <AppContext.Provider value={value}>{children}</AppContext.Provider>
  );
};
