import React, { useContext, useEffect, useState } from "react";
import Container from "react-bootstrap/Container";
import Nav from "react-bootstrap/Nav";
import Navbar from "react-bootstrap/Navbar";
import { Link, useNavigate } from "react-router-dom";
import { AppContext } from "../context/AppContext";
import axiosInstance from "../config/axiosConfig";
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
import ManageAccountsIcon from "@mui/icons-material/ManageAccounts";
import "../style/search.css";
import TextField from "@mui/material/TextField";
import CircularProgress from "@mui/material/CircularProgress";
import {
  Box,
  Typography,
  Backdrop,
  Paper,
  List,
  ListItem,
  ListItemAvatar,
  Avatar,
  ListItemText,
  InputAdornment,
  Menu,
  MenuItem,
} from "@mui/material";
import book_cover from "../assets/book_cover.jpg";

export default function AppNavbar() {
  const { theme, toggleTheme, isLoggedIn } = useContext(AppContext);
  const { role } = useContext(AppContext);
  const isAdmin = role === "ADMIN";
  const navigate = useNavigate();
  const [options, setOptions] = useState([]);
  const [searchInput, setSearchInput] = useState("");
  const [isSearching, setIsSearching] = useState(false);
  const [loading, setLoading] = useState(false);
  const [anchorEl, setAnchorEl] = useState(null);
  const [searchFilter, setSearchFilter] = useState("");
  const [genres, setGenres] = useState([]);
  const open = Boolean(anchorEl);

  const formatGenreName = (genre) => {
    return genre
      .split("_")
      .map((word) => word.charAt(0) + word.slice(1).toLowerCase())
      .join(" ");
  };

  useEffect(() => {
    async function fetchGenres() {
      try {
        const response = await axiosInstance.get("/genres");
        setGenres(response.data);
      } catch (err) {
        console.error(err);
      }
    }
    fetchGenres();
  }, []);

  const handleMenuClose = () => {
    setAnchorEl(null);
  };

  const handleMenuClick = (event) => {
    setAnchorEl(event.currentTarget);
  };

  const IconControl = (props) => (
    <components.Control {...props}>
      <FaSearch style={{ marginLeft: 8, color: "#00B9E8" }} />
      {props.children}
    </components.Control>
  );

  const loadOptions = async (inputValue) => {
    if (!inputValue || inputValue.trim().length < 2) {
      setOptions([]);
      setLoading(false);
      return;
    }

    setLoading(true);
    if (!inputValue || inputValue.length < 2) {
      return [];
    }
    try {
      const response = await axiosInstance.get("/books/search", {
        params: { q: inputValue, genre: searchFilter },
      });
      setOptions(response.data);
      setLoading(false);
    } catch (error) {
      setLoading(false);
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

  const handleInputChange = (value) => {
    setSearchInput(value);
    if (!value || value.trim().length < 2) {
      setOptions([]);
      setLoading(false);
      return;
    }
    loadOptions(value);
  };

  const handleResultClick = (bookId) => {
    navigate(`/books/${bookId}`);
    setIsSearching(false);
  };

  const handleSetSearchFilter = (genre) => () => {
    setSearchFilter(genre);
    handleMenuClose();
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
        "&:hover": {
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
            className="me-auto my-lg-0"
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
          <Box className="search-component">
            <Search>
              <SearchIconWrapper>
                <SearchIcon />
              </SearchIconWrapper>
              <StyledInputBase
                placeholder="Search…"
                onClick={() => setIsSearching(true)}
                onChange={(e) => setSearchInput(e.target.value)}
              />
            </Search>

            <Backdrop
              open={isSearching}
              onClick={() => setIsSearching(false)}
              className="background-blur"
            />

            {isSearching && (
              <Paper
                elevation={5}
                className="search-container"
                sx={{
                  width: { xs: "90%", sm: 500, md: 600 },
                }}
              >
                <Box
                  sx={{
                    pt: 2,
                    px: 2,
                    borderBottom: 1,
                    borderColor: "divider",
                  }}
                >
                  <TextField
                    label="Search books"
                    variant="outlined"
                    size="small"
                    fullWidth
                    autoFocus
                    value={searchInput}
                    onChange={(e) => handleInputChange(e.target.value)}
                    slotProps={{
                      input: {
                        startAdornment: (
                          <InputAdornment position="start">
                            <SearchIcon />
                          </InputAdornment>
                        ),
                      },
                    }}
                  />
                  <Button
                    className="mt-2 mb-2"
                    variant="contained"
                    onClick={handleMenuClick}
                  >
                    {searchFilter !== "" ? formatGenreName(searchFilter): "All genres"}
                  </Button>
                  <Menu
                    anchorEl={anchorEl}
                    open={open}
                    onClose={handleMenuClose}
                    anchorOrigin={{ vertical: "bottom", horizontal: "left" }}
                    transformOrigin={{ vertical: "top", horizontal: "left" }}
                  >
                    {genres.map((genre) => (
                      <MenuItem
                        key={genre}
                        onClick={handleSetSearchFilter(genre)}
                      >
                        {formatGenreName(genre)}
                      </MenuItem>
                    ))}
                  </Menu>
                </Box>

                <Box className="results-list">
                  {loading ? (
                    <Box className="loading-container">
                      <CircularProgress size={32} />
                      <Typography
                        variant="body2"
                        color="text.secondary"
                        className="loading-animation"
                      >
                        Searching...
                      </Typography>
                    </Box>
                  ) : options.length > 0 ? (
                    <List disablePadding>
                      {options.map((result, index) => (
                        <ListItem
                          key={result.id}
                          onClick={() => handleResultClick(result.id)}
                          divider={index < options.length - 1}
                          className="list-item"
                          sx={{
                            "&:hover": {
                              backgroundColor: (theme) =>
                                theme.palette.mode === "light"
                                  ? theme.palette.grey[100]
                                  : theme.palette.grey[800],
                            },
                          }}
                        >
                          <ListItemAvatar>
                            <Avatar
                              variant="rounded"
                              src={result.bookImage?.url || book_cover}
                              alt={result.title}
                              sx={{ height: 65, width: 48 }}
                            />
                          </ListItemAvatar>
                          <ListItemText
                            primary={
                              <Typography variant="body1" fontWeight={500}>
                                {result.title}
                              </Typography>
                            }
                            secondary={
                              <Typography
                                variant="body2"
                                color="text.secondary"
                              >
                                {result.authorInfo.name}{" "}
                                {result.authorInfo.surname} • $
                                {result.price.toFixed(2)}
                              </Typography>
                            }
                            sx={{
                              "& .MuiListItemText-primary": {
                                overflow: "hidden",
                                textOverflow: "ellipsis",
                                whiteSpace: "nowrap",
                              },
                              "& .MuiListItemText-secondary": {
                                overflow: "hidden",
                                textOverflow: "ellipsis",
                                whiteSpace: "nowrap",
                              },
                            }}
                          />
                        </ListItem>
                      ))}
                    </List>
                  ) : (
                    <Box className="items-not-found-container">
                      <Typography variant="body1" color="text.secondary">
                        No results found for "{searchInput}"
                      </Typography>
                    </Box>
                  )}
                </Box>
              </Paper>
            )}
          </Box>
          {isAdmin && (
            <IconButton
              component={Link}
              to="/admin"
              size="small"
              color="warning"
            >
              <ManageAccountsIcon />
            </IconButton>
          )}
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
