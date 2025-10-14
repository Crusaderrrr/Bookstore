import React, { useEffect, useState } from "react";
import { Col, Container, Row } from "react-bootstrap";
import ProfileInfo from "../components/profile/ProfileInfo";
import axiosInstance from "../config/axiosConfig";
import MyBooks from "../components/profile/MyBooks";
import RequestsDrawer from "../components/profile/RequestsDrawer";

export default function UserProfilePage() {
  const [user, setUser] = useState(null);
  const [isAuthor, setIsAuthor] = useState(false);
  const [books, setBooks] = useState([]);
  const [openDrawer, setOpenDrawer] = useState(false);
  const [tabValue, setTabValue] = useState(0);
  const [anchorEl, setAnchorEl] = useState(null);
  const [requests, setRequests] = useState([]);


  const handleToggleDrawer = (open) => {
    setOpenDrawer(open);
    setAnchorEl(null);
  };

  const handleTabChange = (event, newValue) => {
    setTabValue(newValue);
  };

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

    async function fetchRequests() {
      try {
        const response = await axiosInstance.get("/moderation/requests/my");
        setRequests(response.data);
      } catch (err) {
        console.error(err);
      }
    }
    fetchRequests();
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
      const response = await axiosInstance.post(
        "/users/image_upload",
        formData,
        {
          headers: {
            "Content-Type": "multipart/form-data",
          },
        }
      );
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
            toggleDrawer={handleToggleDrawer}
            anchorEl={anchorEl}
            setAnchorEl={setAnchorEl}
            setIsAuthor={setIsAuthor}
          />
          {isAuthor && <MyBooks books={books} />}
        </Col>
        <RequestsDrawer
          openDrawer={openDrawer}
          onClose={() => handleToggleDrawer(false)}
          tabValue={tabValue}
          handleTabChange={handleTabChange}
          requests={requests}
        />
      </Row>
    </Container>
  );
}
