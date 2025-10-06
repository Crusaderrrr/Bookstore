import React, { useEffect, useState } from "react";
import { Col, Container, Row } from "react-bootstrap";
import ProfileInfo from "../components/profile/ProfileInfo";
import axiosInstance from "../config/axiosConfig";
import MyBooks from "../components/profile/MyBooks";

export default function UserProfilePage() {
  const [user, setUser] = useState(null);
  const [isAuthor, setIsAuthor] = useState(false);
  const [books, setBooks] = useState([]);

  useEffect(() => {
    async function fetchUser() {
      try {
        const response = await axiosInstance.get("/users/self");
        setUser(response.data.user);
        setBooks(response.data.books);
        setIsAuthor(response.data.user.roles === "ROLE_AUTHOR");
      } catch (err) {
        console.error(err);
      }
    }
    fetchUser();
  }, []);

  const handleLogout = async () => {
    try {
      axiosInstance.post("/logout");
      localStorage.removeItem("accessToken");
      localStorage.removeItem("userRole");
      window.location.href = "/";
    } catch (err) {
      console.error(err);
    }
  };

  const handleImageChange = async (e) => {
    const file = e.target.files[0];
    if (!file) return;

    const formData = new FormData();
    formData.append("file", file);

    try {
      const response = await axiosInstance.post("/users/image_upload", formData, {
        headers: {
          "Content-Type": "multipart/form-data"
        }
      });
      setUser({ ...user, image: response.data });
    } catch (err) {
      console.error("Image upload failed", err);
    }
  };

  return (
    <Container fluid>
      <Row className="justify-content-center mt-4">
        <Col xs={8} md={6}>
          <ProfileInfo
            username={user?.username}
            email={user?.email}
            onLogout={handleLogout}
            image={user?.image?.url}
            onImageChange={handleImageChange}
            isAuthor={isAuthor}
          />
          {isAuthor && <MyBooks books={books}/>}
        </Col>
      </Row>
    </Container>
  );
}
