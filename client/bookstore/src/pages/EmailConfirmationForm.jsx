import React, { useState } from "react";
import { Button, Form, Alert, Row, Col, Container } from "react-bootstrap";
import backgroundImage from "../assets/background_image.webp";
import axiosInstance from "../config/axiosConfig";
import { useNavigate } from "react-router-dom";

export default function EmailConfirmationModal() {
  const navigate = useNavigate();
  const [code, setCode] = useState("");
  const [error, setError] = useState("");
  const [success, setSuccess] = useState(false);

  const handleSubmit = (e) => {
    e.preventDefault();
    setError("");
    setSuccess(false);
    try {
      const response = axiosInstance.post("/users/confirm-email", code);
      if (response.status === 200) {
        setSuccess(true);
        // navigate("/");
      } else {
        setError("Invalid confirmation code");
      }

    } catch (err) {
      console.error(err);
      setError("Invalid confirmation code");
      setSuccess(false);
    } finally {
      setSuccess(false);
      setError("");
    }
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
          <Form onSubmit={handleSubmit} noValidate>
            <Form.Group controlId="confirmationCode" className="mb-3">
              <Form.Label>Enter Confirmation Code</Form.Label>
              <Form.Control
                type="text"
                placeholder="Enter code"
                value={code}
                onChange={(e) => setCode(e.target.value)}
                isInvalid={!!error}
              />
              <Form.Control.Feedback type="invalid">
                {error}
              </Form.Control.Feedback>
            </Form.Group>
            <Button type="submit" disabled={success} className="w-100">
              Confirm
            </Button>
            {success && (
              <Alert variant="success" className="mt-3">
                Email verified successfully!
              </Alert>
            )}
          </Form>
        </Col>
      </Row>
    </Container>
  );
}
