import React, { useContext, useEffect, useState } from "react";
// import Button from "react-bootstrap/Button";
import Container from "react-bootstrap/Container";
import Form from "react-bootstrap/Form";
import Nav from "react-bootstrap/Nav";
import Navbar from "react-bootstrap/Navbar";
import { Link, useNavigate } from "react-router-dom";
import { AppContext } from "../context/AppContext";
import axiosInstance from "../config/axiosConfig";
import AsyncSelect from "react-select/async";
import { components } from "react-select";
import { FaSearch } from "react-icons/fa";
import Button from "@mui/material/Button";
import LoginIcon from "@mui/icons-material/Login";
import IconButton from "@mui/material/IconButton";
import ShoppingCartIcon from "@mui/icons-material/ShoppingCart";
import PersonIcon from "@mui/icons-material/Person";
import StoreIcon from "@mui/icons-material/Store";
import BedtimeIcon from "@mui/icons-material/Bedtime";
import SunnyIcon from "@mui/icons-material/Sunny";
import SearchIcon from "@mui/icons-material/Search";
import { styled, alpha } from "@mui/material/styles";
import InputBase from "@mui/material/InputBase";

export default function AppNavbar() {
  const { theme, toggleTheme, isLoggedIn } = useContext(AppContext);
  const { role } = useContext(AppContext);
  const isAdmin = role === "ADMIN";
  const navigate = useNavigate();
  const isDarkMode = theme === "dark";
  const [options, setOptions] = useState([]);
  const [searchInput, setSearchInput] = useState("");

  const IconControl = (props) => (
    <components.Control {...props}>
      <FaSearch style={{ marginLeft: 8, color: "#00B9E8" }} />
      {props.children}
    </components.Control>
  );

  const loadOptions = async (inputValue) => {
    if (!inputValue || inputValue.length < 2) {
      return [];
    }
    try {
      const response = await axiosInstance.get("/books/search", {
        params: { q: inputValue },
      });
      setOptions(response.data);
    } catch (error) {
      console.error("Search failed:", error);
      return [];
    }
  };

  const handleToggleTheme = () => {
    if (theme === "dark") {
      toggleTheme();
    } else {
      return;
    }
  };

  const fixedWidthStyles = {
    container: (base) => ({
      ...base,
      width: 350,
      minWidth: 350,
    }),
    control: (base) => ({
      ...base,
      width: "100%",
      minHeight: 40,
      boxSizing: "border-box",
      backgroundColor: isDarkMode ? "#1a202c" : "#fff",
      borderColor: isDarkMode ? "#4a5568" : base.borderColor,
      color: isDarkMode ? "#eee" : base.color,
    }),
    input: (base) => ({
      ...base,
      margin: 0,
      padding: 0,
      color: isDarkMode ? "#eee" : base.color,
    }),
    valueContainer: (base) => ({
      ...base,
      width: "100%",
      overflow: "hidden",
    }),
    singleValue: (base) => ({
      ...base,
      overflow: "hidden",
      textOverflow: "ellipsis",
      whiteSpace: "nowrap",
      color: isDarkMode ? "#eee" : base.color,
    }),
    menu: (base) => ({
      ...base,
      width: 350,
      backgroundColor: isDarkMode ? "#2d3748" : "#fff",
      color: isDarkMode ? "#f3f4f6" : "#333",
    }),
    option: (base, { isFocused, isSelected }) => ({
      ...base,
      backgroundColor: isSelected
        ? isDarkMode
          ? "#90cdf4"
          : "#2684ff"
        : isFocused
        ? isDarkMode
          ? "#2d3748"
          : "#f0f0f0"
        : undefined,
      color: isDarkMode ? "#f3f4f6" : "#333",
      cursor: "pointer",
    }),
    placeholder: (base) => ({
      ...base,
      color: isDarkMode ? "#a0aec0" : "#999",
    }),
    dropdownIndicator: (base) => ({
      ...base,
      color: isDarkMode ? "#a0aec0" : base.color,
    }),
    clearIndicator: (base) => ({
      ...base,
      color: isDarkMode ? "#a0aec0" : base.color,
    }),
    multiValue: (base) => ({
      ...base,
      backgroundColor: isDarkMode ? "#4a5568" : base.backgroundColor,
    }),
    multiValueLabel: (base) => ({
      ...base,
      color: isDarkMode ? "#f3f4f6" : base.color,
    }),
    multiValueRemove: (base) => ({
      ...base,
      color: isDarkMode ? "#f56565" : base.color,
      ":hover": {
        backgroundColor: isDarkMode ? "#c53030" : base.backgroundColor,
        color: "white",
      },
    }),
  };

  const handleInputChange = (value) => {
    setSearchInput(value);
    loadOptions(value);
  };

  const Search = styled("div")(({ theme }) => ({
    position: "relative",
    borderRadius: theme.shape.borderRadius,
    backgroundColor: alpha(theme.palette.common.black, 0.1),
    "&:hover": {
      backgroundColor: alpha(theme.palette.common.black, 0.15),
    },
    marginLeft: 0,
    width: "100%",
    [theme.breakpoints.up("sm")]: {
      marginLeft: theme.spacing(1),
      width: "auto",
    },
  }));

  const SearchIconWrapper = styled("div")(({ theme }) => ({
    padding: theme.spacing(0, 2),
    height: "100%",
    position: "absolute",
    pointerEvents: "none",
    display: "flex",
    alignItems: "center",
    justifyContent: "center",
  }));

  const StyledInputBase = styled(InputBase)(({ theme }) => ({
    color: "inherit",
    width: "100%",
    "& .MuiInputBase-input": {
      padding: theme.spacing(1, 10, 1, 0),
      paddingLeft: `calc(1em + ${theme.spacing(4)})`,
      transition: theme.transitions.create("width"),
      [theme.breakpoints.up("sm")]: {
        width: "20ch",
        "&:focus": {
          width: "45ch",
        },
      },
    },
  }));

  return (
    <Navbar bg={theme} variant={theme} expand="lg">
      <Container fluid>
        <Navbar.Brand as={Link} to="/">
          Bookstore
        </Navbar.Brand>
        <Navbar.Toggle aria-controls="navbarScroll" />
        <Navbar.Collapse id="navbarScroll">
          <Nav
            className="me-auto my-2 my-lg-0"
            style={{ maxHeight: "100px" }}
            navbarScroll
          >
            <IconButton
              onClick={toggleTheme}
              style={{ cursor: "pointer" }}
              size="small"
            >
              {theme === "dark" ? <BedtimeIcon /> : <SunnyIcon />}
            </IconButton>
          </Nav>
          {isAdmin && (
            <Nav.Link as={Link} to="/admin" className="me-2">
              <i className="bi bi-gear"></i>
            </Nav.Link>
          )}
          {/* <div className="d-flex me-auto">
            <AsyncSelect
              cacheOptions
              loadOptions={loadOptions}
              onChange={handleChange}
              placeholder="Search for a book..."
              components={{
                Control: IconControl,
                DropdownIndicator: () => null,
                IndicatorSeparator: () => null,
              }}
              styles={fixedWidthStyles}
              noOptionsMessage={() => "No books found"}
            />
          </div> */}
          <div className="me-auto">
            <Search>
              <SearchIconWrapper>
                <SearchIcon />
              </SearchIconWrapper>
              <StyledInputBase
                placeholder="Search…"
                onChange={e => handleInputChange(e.target.value)}
                value={searchInput}
              />
            </Search>
          </div>
          <IconButton component={Link} to="/shop">
            <StoreIcon />
          </IconButton>

          {isLoggedIn ? (
            <IconButton size="small" component={Link} to="/cart">
              <ShoppingCartIcon style={{ width: "21px", height: "21px" }} />
            </IconButton>
          ) : (
            <Button
              variant="outlined"
              size="small"
              endIcon={<LoginIcon />}
              component={Link}
              to="/login"
              onClick={handleToggleTheme}
            >
              Login
            </Button>
          )}
          {isLoggedIn && (
            <IconButton
              component={Link}
              to="/profile"
              className="me-1"
              color="primary"
            >
              <PersonIcon />
            </IconButton>
          )}
        </Navbar.Collapse>
      </Container>
    </Navbar>
  );
}
