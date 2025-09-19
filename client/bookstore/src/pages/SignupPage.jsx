import React, { useState } from "react";
import axios from "axios";
import backgroundImage from "../assets/background_image.webp";
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
import { AppContext } from "../context/AppContext";
import { useContext } from "react";

function SignupPage() {
  const navigate = useNavigate();
  const { setIsLoggedIn } = useContext(AppContext);
  const [username, setUsername] = useState("");
  const [password, setPassword] = useState("");
  const [showPassword, setShowPassword] = useState(false);
  const [email, setEmail] = useState("");
  const [alertMessage, setAlertMessage] = useState("");
  const [alertType, setAlertType] = useState("");
  const [isEmailValid, setIsEmailValid] = useState(false);
  const [isPasswordValid, setIsPasswordValid] = useState(false);

  const handleSubmit = async (e) => {
    e.preventDefault();
    setAlertMessage("");
    setAlertType("");

    try {
      const response = await axios.post(
        "http://localhost:8080/users/new",
        {
          username,
          password,
          email,
          roles: "USER",
          active: true,
        },
        {
          withCredentials: true,
        }
      );
      setIsLoggedIn(true);
      localStorage.setItem("accessToken", response.data.accessToken);
      localStorage.setItem("userRole", "USER");
      setAlertMessage("Signup successful!");
      setAlertType("success");
      navigate("/");
    } catch (err) {
      console.error(err);
      setAlertMessage("Error");
      setAlertType("danger");
    }
  };

  const validateEmail = (email) => {
    const regex = /^[^\s@]+@[^\s@]+\.[^\s@]+$/;
    return regex.test(email);
  };

  const validatePassword = (password) => {
    return (
      password.length >= 8 &&
      /[A-Z]/.test(password) &&
      /[a-z]/.test(password) &&
      /[0-9]/.test(password)
    );
  };

  const handleEmailChange = (e) => {
    const value = e.target.value;
    setEmail(value);
    setIsEmailValid(validateEmail(value));
  };

  const handlePasswordChange = (e) => {
    const value = e.target.value;
    setPassword(value);
    setIsPasswordValid(validatePassword(value));
  };

  const handleUsernameChange = (e) => {
    const value = e.target.value;
    setUsername(value);
  };

  return (
    <Container fluid>
      <Row
        className="row min-vh-100 d-flex justify-content-center align-items-center"
        style={{
          backgroundImage: `url(${backgroundImage})`,
          backgroundSize: "cover",
          backgroundPosition: "center",
          backgroundRepeat: "no-repeat",
        }}
      >
        <Col
          className="col-6 p-4 shadow-lg rounded"
          style={{
            width: "100%",
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
              Create your account!
            </p>
            <h1 className="display-6 mb-4 fw-normal text-center">Sign Up</h1>

            <Form.Group className="mb-3" controlId="email">
              <Form.Label>Email</Form.Label>
              <InputGroup>
                <Form.Control
                  type="email"
                  value={email}
                  onChange={handleEmailChange}
                  placeholder="Enter email"
                  isValid={!!email && isEmailValid}
                  isInvalid={!!email && !isEmailValid}
                />
                <InputGroup.Text>
                  <i className="bi bi-envelope"></i>
                </InputGroup.Text>
              </InputGroup>
            </Form.Group>

            <Form.Group className="mb-2" controlId="username">
              <Form.Label>Name</Form.Label>
              <InputGroup className="input-group">
                <Form.Control
                  type="text"
                  value={username}
                  onChange={handleUsernameChange}
                  placeholder="Enter username"
                />
                <InputGroup.Text>
                  <i className="bi bi-lock"></i>
                </InputGroup.Text>
              </InputGroup>
            </Form.Group>

            <Form.Group className="mb-4" controlId="password">
              <Form.Label>Password</Form.Label>
              <InputGroup>
                <Form.Control
                  type={showPassword ? "text" : "password"}
                  value={password}
                  onChange={handlePasswordChange}
                  isValid={!!password && isPasswordValid}
                  isInvalid={!!password && !isPasswordValid}
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
            <button
              type="submit"
              onSubmit={handleSubmit}
              className="btn btn-primary w-100"
            >
              Sign Up
            </button>
          </Form>
        </Col>
      </Row>
    </Container>
  );
}

export default SignupPage;
