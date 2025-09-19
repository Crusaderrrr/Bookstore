import React, { useContext, useEffect, useState } from "react";
import Button from "react-bootstrap/Button";
import axios from "axios";
import { Col, Container, Row } from "react-bootstrap";

export default function HomePage() {
  const [users, setUsers] = useState([]);

  async function handleSendRequest() {
    try {
      const response = await axios.get("http://localhost:8080/users/all", {
        withCredentials: true,
        headers: {
          Authorization: `Bearer ${localStorage.getItem("accessToken")}`,
        },
      });
      setUsers(response.data);
      console.log("Users:", response.data);
    } catch (err) {
      console.error("Failed to fetch users:", err);
    }
  }

  return (
    <Container fluid>
      <Row>
        <Col className="justify-content-center mt-4">
          <h1 className="text-center display-4">Bestsellers</h1>
          <Button
            variant="primary"
            style={{
              position: "absolute",
              top: "50%",
              left: "50%",
              transform: "translate(-50%, -50%)",
            }}
            onClick={handleSendRequest}
          >
            Send the request
          </Button>
        </Col>
      </Row>
    </Container>
  );
}
