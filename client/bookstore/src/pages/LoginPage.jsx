import React, { useState } from "react";
import axios from "axios";
import backgroundImage from "../assets/background_image.webp";
import { Link } from "react-router-dom";
import { useNavigate } from "react-router-dom";

function Login() {
  const navigate = useNavigate();
  const [password, setPassword] = useState("");
  const [username, setUsername] = useState("");
  const [error, setError] = useState("");
  const [alertMessage, setAlertMessage] = useState("");
  const [alertType, setAlertType] = useState("");
  const [isEmailValid, setIsEmailValid] = useState(false);
  const [isPasswordValid, setIsPasswordValid] = useState(false);

  const handleSubmit = async (e) => {
    e.preventDefault();
    setError("");
    const params = new URLSearchParams();
    params.append("username", username);
    params.append("password", password);

    try {
      const response = await axios.post("http://localhost:8080/login", params, {
        withCredentials: true,
      });
      setAlertMessage("Login successful!");
      setAlertType("success");
      navigate("/home");
    } catch (err) {
      setError("Invalid username or password. Please try again.");
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
    <div className="container-fluid">
      <div
        className="row min-vh-100 d-flex justify-content-center align-items-center"
        style={{
          backgroundImage: `url(${backgroundImage})`,
          backgroundSize: "cover",
          backgroundPosition: "center",
          backgroundRepeat: "no-repeat",
        }}
      >
        <div
          className="col-6 p-4 shadow-lg rounded"
          style={{
            width: "100%",
            maxWidth: "650px",
            backgroundColor: "rgba(255, 255, 255, 0.95)",
          }}
        >
          <form className="mx-3" onSubmit={handleSubmit}>
            {alertMessage && (
              <div
                className={`alert alert-${alertType} alert-dismissible fade show mb-4`}
                role="alert"
              >
                {alertMessage}
                <button
                  type="button"
                  className="btn-close"
                  onClick={() => setAlertMessage("")}
                  aria-label="Close"
                ></button>
              </div>
            )}
            <p className="mb-2 text-muted fs-5 text-center">
              Start your journey!
            </p>
            <h1 className="display-6 mb-4 fw-normal text-center">Login</h1>

            <div className="mb-3">
              <label className="form-label">Username</label>
              <div className="input-group">
                <input
                  type="username"
                  className={`form-control`}
                  id="username"
                  value={username}
                  onChange={handleUsernameChange}
                />
                <span className="input-group-text">
                  <i className="bi bi-envelope"></i>
                </span>
              </div>
            </div>

            <div className="mb-2">
              <label className="form-label">Password</label>
              <div className="input-group">
                <input
                  type="password"
                  className={`form-control ${
                    password
                      ? isPasswordValid
                        ? "is-valid"
                        : "is-invalid"
                      : ""
                  }`}
                  id="password"
                  value={password}
                  onChange={handlePasswordChange}
                />
                <span className="input-group-text">
                  <i className="bi bi-lock"></i>
                </span>
              </div>
            </div>

            <div className="input-group mb-4 d-flex justify-content-between">
              <div className="form-check">
                <input
                  type="checkbox"
                  className="form-check-input"
                  id="remember_me"
                />
                <label
                  className="form-check-label text-secondary"
                  htmlFor="remember_me"
                >
                  Remember me
                </label>
              </div>
              <div className="forgot">
                <small>
                  <a href="#">Forgot Password?</a>
                </small>
              </div>
            </div>

            <button
              type="submit"
              onSubmit={handleSubmit}
              className="btn btn-primary w-100"
            >
              Log In
            </button>

            <div className="text-center mt-3">
              <small className="text-muted">
                Don't have an account?
                <Link to="/signup" className="ms-1">
                  Sign Up
                </Link>
              </small>
            </div>
          </form>
        </div>
      </div>
    </div>
  );
}

export default Login;
