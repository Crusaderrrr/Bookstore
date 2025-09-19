import React, { useEffect, useState } from "react";
import { Col, Container, Row } from "react-bootstrap";
import ProfileInfo from "../components/profile/ProfileInfo";
import axios from "axios";

export default function UserProfilePage() {
  const [user, setUser] = useState(null);

  useEffect(() => {
    async function fetchUser() {
      try {
        const response = await axios.get("http://localhost:8080/users/self", {
          withCredentials: true,
          headers: {
            Authorization: `Bearer ${localStorage.getItem("accessToken")}`,
          },
        });
        setUser(response.data);
      } catch (err) {
        console.error(err);
      }
    }
    fetchUser();
  }, []);

  const handleLogout = async () => {
    try {
      axios.post(
        "http://localhost:8080/logout",
        {
          withCredentials: true,
        }
      );
      localStorage.removeItem("accessToken");
      localStorage.removeItem("userRole");
      window.location.href = "/";
    } catch (err) {
      console.error(err);
    }
  };

  return (
    <Container fluid>
      <Row className="justify-content-center mt-4">
        <Col xs={6}>
          <ProfileInfo
            username={user?.username}
            email={user?.email}
            onLogout={handleLogout}
          />
        </Col>
      </Row>
    </Container>
  );
}
