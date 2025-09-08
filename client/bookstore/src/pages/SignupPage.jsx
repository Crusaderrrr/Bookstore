import React, { useState } from "react";
import axios from "axios";
import backgroundImage from "../assets/background_image.webp";
import { Link } from "react-router-dom";
import { useNavigate } from "react-router-dom";

function SignupPage() {
  const navigate = useNavigate();
  const [username, setUsername] = useState("");
  const [password, setPassword] = useState("");
  const [email, setEmail] = useState("");
  const [error, setError] = useState("");
  const [alertMessage, setAlertMessage] = useState("");
  const [alertType, setAlertType] = useState("");
  const [isEmailValid, setIsEmailValid] = useState(false);
  const [isPasswordValid, setIsPasswordValid] = useState(false);

  const handleSubmit = async (e) => {
    e.preventDefault();
    setError("");

    try {
      const response = await axios.post("http://localhost:8080/users/register", {
        username,
        password,
        email,
      }, {
        withCredentials: true,
      }, { headers: { 'Content-Type': 'application/json' } });
      console.log(response);
      setAlertMessage("Signup successful!");
      setAlertType("success");
      navigate("/home");
    } catch (err) {
      setError("Invalid username or password. Please try again.");
      setAlertType("danger");
    }
  };

  const validateEmail = (email) => {
    const regex = /^[^\s@]+@[^\s@]+\.[^\s@]+$/;
    return regex.test(email);
  };

  const validatePassword = (password) => {
    return password.length >= 1;
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
  }

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
              Create your account!
            </p>
            <h1 className="display-6 mb-4 fw-normal text-center">Sign Up</h1>

            <div className="mb-3">
              <label className="form-label">Email</label>
              <div className="input-group">
                <input
                  type="email"
                  className={`form-control ${
                    email ? (isEmailValid ? "is-valid" : "is-invalid") : ""
                  }`}
                  id="email"
                  value={email}
                  onChange={handleEmailChange}
                />
                <span className="input-group-text">
                  <i className="bi bi-envelope"></i>
                </span>
              </div>
            </div>

            <div className="mb-2">
              <label className="form-label">Name</label>
              <div className="input-group">
                <input
                  type="username"
                  className={`form-control`}
                  id="username"
                  value={username}
                  onChange={handleUsernameChange}
                />
                <span className="input-group-text">
                  <i className="bi bi-lock"></i>
                </span>
              </div>
            </div>

            <div className="mb-4">
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
            <button
              type="submit"
              onSubmit={handleSubmit}
              className="btn btn-primary w-100"
            >
              Sign Up
            </button>
          </form>
        </div>
      </div>
    </div>
  );
}

export default SignupPage;