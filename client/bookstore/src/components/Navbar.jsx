import React, { useContext } from "react";
import Button from "react-bootstrap/Button";
import Container from "react-bootstrap/Container";
import Form from "react-bootstrap/Form";
import Nav from "react-bootstrap/Nav";
import Navbar from "react-bootstrap/Navbar";
import { Link } from "react-router-dom";
import { AppContext } from "../context/AppContext";

export default function AppNavbar() {
  const { theme, toggleTheme, isLoggedIn } = useContext(AppContext);
  const { role } = useContext(AppContext);
  const isAdmin = role === "ADMIN";


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
            <Nav.Link as={Link} to="/profile">
              My Profile <i className="bi bi-person"></i>
            </Nav.Link>
            <Nav.Link as={Link} to="/shop">
              Shop
            </Nav.Link>
            {isLoggedIn ? (
              <Nav.Link as={Link} to="/cart">
                <i className="bi bi-cart" role="img" aria-label="Cart" />
              </Nav.Link>
            ) : (
              <Nav.Link as={Link} to="/login">
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
          <Form className="d-flex">
            <Form.Control
              type="search"
              placeholder="Search"
              className="me-2"
              aria-label="Search"
            />
            <Button variant="outline-success">Search</Button>
          </Form>
        </Navbar.Collapse>
      </Container>
    </Navbar>
  );
}
