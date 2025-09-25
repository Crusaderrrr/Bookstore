import React from "react";
import { Button, Card } from "react-bootstrap";
import default_profile_image from "../../assets/default_profile_image.jpg"

export default function ProfileInfo({ username, email, onLogout, image }) {
  return (
    <Card className="shadow-sm">
      <Card.Header>User Info</Card.Header>
      <Card.Body className="d-flex align-items-center">
        <Card.Img
          className="rounded-circle mb-2 me-3 mt-2"
          style={{ width: "110px", height: "110px" }}
          src={!!image ? image : default_profile_image}
        />
        <div className="d-flex flex-column align-items-start mb-3">
          <Card.Title className="display-6">{username}</Card.Title>
          <Card.Text className="fs-4 fw-light">{email}</Card.Text>
        </div>
        <Button className="ms-auto" variant="danger" onClick={onLogout}>
          Logout
        </Button>
      </Card.Body>
    </Card>
  );
}
