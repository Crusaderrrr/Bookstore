import { createTheme } from "@mui/material/styles";

export const lightTheme = createTheme({
  palette: {
    mode: "light",
    primary: { main: "#2EB3E8" }, 
    background: { default: "#fff" },
  },
});

export const darkTheme = createTheme({
  palette: {
    mode: "dark",
    primary: { main: "#9c7aff" },
    background: { default: "#121212" },
  },
});