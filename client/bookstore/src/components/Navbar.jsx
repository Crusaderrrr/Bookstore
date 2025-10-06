import React, { useContext, useEffect, useState } from "react";
import Button from "react-bootstrap/Button";
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

export default function AppNavbar() {
  const { theme, toggleTheme, isLoggedIn } = useContext(AppContext);
  const { role } = useContext(AppContext);
  const isAdmin = role === "ADMIN";
  const [options, setOptions] = useState([]);
  const [inputValue, setInputValue] = useState("");
  const [selectedOption, setSelectedOption] = useState("");
  const navigate = useNavigate();
  const isDarkMode = theme === "dark";

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
      return response.data.map((book) => ({
        value: book.id,
        label: book.title,
        description: book.description,
      }));
    } catch (error) {
      console.error("Search failed:", error);
      return [];
    }
  };

  const handleChange = (selectedOption) => {
    if (selectedOption) {
      navigate(`/books/${selectedOption.value}`, { state: { mode: "view" } });
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
      backgroundColor: isDarkMode ? "#1a202c" : "#fff", // dark bg for control
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
            {isLoggedIn && (
              <Nav.Link as={Link} to="/profile">
                My Profile <i className="bi bi-person"></i>
              </Nav.Link>
            )}
            <Nav.Link as={Link} to="/shop">
              Shop
            </Nav.Link>
            {isLoggedIn ? (
              <Nav.Link as={Link} to="/cart">
                <i className="bi bi-cart" role="img" aria-label="Cart" />
              </Nav.Link>
            ) : (
              <Nav.Link
                as={Link}
                to="/login"
                onClick={theme === "dark" ? toggleTheme : null}
              >
                Login
              </Nav.Link>
            )}
            <Nav.Link onClick={toggleTheme} style={{ cursor: "pointer" }}>
              {theme === "dark" ? (
                <i className="bi bi-moon-fill"></i>
              ) : (
                <i className="bi bi-sun-fill"></i>
              )}
            </Nav.Link>
          </Nav>
          {isAdmin && (
            <Nav.Link as={Link} to="/admin" className="me-2">
              <i className="bi bi-gear"></i>
            </Nav.Link>
          )}
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
        </Navbar.Collapse>
      </Container>
    </Navbar>
  );
}
