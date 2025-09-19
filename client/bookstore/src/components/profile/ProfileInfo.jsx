import React from "react";
import { Button, Card } from "react-bootstrap";

export default function ProfileInfo({ username, email, onLogout }) {
  return (
    <Card className="shadow-sm">
      <Card.Header>User Info</Card.Header>
      <Card.Body className="d-flex align-items-center">
        <Card.Img
          className="rounded-circle mb-2 me-3 mt-2"
          style={{ width: "110px", height: "110px" }}
          src="https://images.unsplash.com/photo-1593696954577-ab3d39317b97?fm=jpg&q=60&w=3000&ixlib=rb-4.1.0&ixid=M3wxMjA3fDB8MHxzZWFyY2h8MTZ8fGZyZWUlMjBpbWFnZXN8ZW58MHx8MHx8fDA%3D"
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
