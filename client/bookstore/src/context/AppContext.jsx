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
  const [isAdmin, setIsAdmin] = useState(localStorage.getItem("userRole") === "ADMIN" ? true : false);

  useEffect(() => {
    document.documentElement.setAttribute("data-bs-theme", theme);
  }, [theme]);

  const toggleTheme = () => {
    setTheme((prevTheme) => (prevTheme === "light" ? "dark" : "light"));
  };

  const value = useMemo(() => ({ theme, toggleTheme, isLoggedIn, setIsLoggedIn, isAdmin, setIsAdmin }), [theme, isLoggedIn, isAdmin]);

  return (
    <AppContext.Provider value={value}>{children}</AppContext.Provider>
  );
};
