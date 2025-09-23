import React, { useContext, useState } from "react";
import axios from "axios";
import backgroundImage from "../assets/background_image.webp";
import { Link } from "react-router-dom";
import { AppContext } from "../context/AppContext";
import { useNavigate } from "react-router-dom";
import {
  Container,
  Row,
  Col,
  Form,
  Button,
  Alert,
  InputGroup,
} from "react-bootstrap";
import axiosInstance from "../config/axiosConfig";

function LoginPage() {
  const navigate = useNavigate();
  const { setIsLoggedIn, setRole } = useContext(AppContext);
  const [password, setPassword] = useState("");
  const [showPassword, setShowPassword] = useState(false);
  const [username, setUsername] = useState("");
  const [alertMessage, setAlertMessage] = useState("");
  const [alertType, setAlertType] = useState("");
  const [isPasswordValid, setIsPasswordValid] = useState(false);

  const handleSubmit = async (e) => {
    e.preventDefault();
    setAlertMessage("");
    setAlertType("");

    try {
      const response = await axiosInstance.post("/users/login", {
        username,
        password,
      });
      localStorage.setItem("accessToken", response.data.accessToken);
      setRole(response.data.role.slice(5));
      console.log(response.data.role.slice(5));
      localStorage.setItem("userRole", response.data.role.slice(5));
      setIsLoggedIn(true);
      setAlertMessage("Login successful!");
      setAlertType("success");
      navigate("/");
    } catch (err) {
      setAlertMessage(err.response.data.error);
      setAlertType("danger");
    }
  };

  const validatePassword = (password) => {
    return password.length >= 1;
  };

  const handleUsernameChange = (e) => {
    const value = e.target.value;
    setUsername(value);
  };

  const handlePasswordChange = (e) => {
    const value = e.target.value;
    setPassword(value);
    setIsPasswordValid(validatePassword(value));
  };

  return (
    <Container fluid>
      <Row
        className="min-vh-100 d-flex justify-content-center align-items-center"
        style={{
          backgroundImage: `url(${backgroundImage})`,
          backgroundSize: "cover",
          backgroundPosition: "center",
          backgroundRepeat: "no-repeat",
        }}
      >
        <Col
          xs={12}
          md={8}
          lg={6}
          className="p-4 shadow-lg rounded"
          style={{
            maxWidth: "650px",
            backgroundColor: "rgba(255, 255, 255, 0.95)",
          }}
        >
          <Form className="mx-3" onSubmit={handleSubmit}>
            {alertMessage && (
              <Alert
                variant={alertType}
                onClose={() => setAlertMessage("")}
                className="mb-4"
              >
                {alertMessage}
              </Alert>
            )}
            <p className="mb-2 text-muted fs-5 text-center">
              Start your journey!
            </p>
            <h1 className="display-6 mb-4 fw-normal text-center">Login</h1>

            <Form.Group className="mb-3" controlId="username">
              <Form.Label>Username</Form.Label>
              <InputGroup>
                <Form.Control
                  type="text"
                  value={username}
                  onChange={handleUsernameChange}
                  placeholder="Enter username"
                />
                <InputGroup.Text>
                  <i className="bi bi-envelope"></i>
                </InputGroup.Text>
              </InputGroup>
            </Form.Group>

            <Form.Group className="mb-2" controlId="password">
              <Form.Label>Password</Form.Label>
              <InputGroup>
                <Form.Control
                  type={showPassword ? "text" : "password"}
                  value={password}
                  onChange={handlePasswordChange}
                  placeholder="Enter password"
                />
                <Button
                  variant="outline-secondary"
                  type="button"
                  onClick={() => setShowPassword((prev) => !prev)}
                  tabIndex={-1}
                >
                  {showPassword ? (
                    <i className="bi bi-eye"></i>
                  ) : (
                    <i className="bi bi-eye-slash"></i>
                  )}
                </Button>
              </InputGroup>
            </Form.Group>

            <div className="d-flex justify-content-between align-items-center mb-4">
              <Form.Check
                type="checkbox"
                id="remember_me"
                label={<span className="text-secondary">Remember me</span>}
              />
              <div className="forgot">
                <small>
                  <a href="#">Forgot Password?</a>
                </small>
              </div>
            </div>

            <Button type="submit" className="w-100" variant="primary">
              Log In
            </Button>

            <div className="text-center mt-3">
              <small className="text-muted">
                Don't have an account?
                <Link to="/signup" className="ms-1">
                  Sign Up
                </Link>
              </small>
            </div>
          </Form>
        </Col>
      </Row>
    </Container>
  );
}

export default LoginPage;
