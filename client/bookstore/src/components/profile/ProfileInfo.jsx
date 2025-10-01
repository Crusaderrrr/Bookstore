import React, { useState } from "react";
import { Alert, Button, ButtonGroup, Card, Col, Container, Form, Row } from "react-bootstrap";
import default_profile_image from "../../assets/default_profile_image.jpg";
import "../../style/authorForm.css";
import axiosInstance from "../../config/axiosConfig";

export default function ProfileInfo({
  username,
  email,
  onLogout,
  image,
  onImageChange,
}) {
  const [becomeAuthor, setBecomeAuthor] = useState(false);
  const [name, setName] = React.useState("");
  const [surname, setSurname] = React.useState("");
  const [genres, setGenres] = React.useState(["Comedy", "Drama"]);
  const [bio, setBio] = React.useState("");
  const [pseudonym, setPseudonym] = React.useState("");
  const [alert, setAlert] = useState(false);

  const handleSubmitAuthor = async (event) => {
    event.preventDefault();

    const authorData = {
      name,
      surname,
      genres,
      bio,
      pseudonym,
    };

    console.log(authorData);

    try {
      const response = await axiosInstance.post("/authors/new", authorData);
      if (response.status === 200) {
        setAlert(true);
      }
    } catch (err) {
      console.error(err);
    }
  };

  const handleBecomeAuthor = () => {
    setBecomeAuthor(!becomeAuthor);
  };

  return (
    <Container>
      <Row className="justify-content-center">
        <Col className="w-75 mx-auto">
          {becomeAuthor && (
            <div className="background-blur">
              <div className="form-center">
                <div className="d-flex align-items-center">
                  {alert && (
                    <Alert className="ms-auto" variant="success">
                      You are an author now
                    </Alert>
                  )}
                  <Button
                    variant="outline-secondary"
                    size="sm"
                    className="ms-auto mb-2 "
                    onClick={() => setBecomeAuthor(false)}
                  >
                    X
                  </Button>
                </div>
                <Form onSubmit={handleSubmitAuthor}>
                  <Form.Group className="mb-3" controlId="formBasicEmail">
                    <Form.Label>Name</Form.Label>
                    <Form.Control
                      type="text"
                      value={name}
                      onChange={(e) => setName(e.target.value)}
                    />
                  </Form.Group>
                  <Form.Group className="mb-3" controlId="formBasicPassword">
                    <Form.Label>Surname</Form.Label>
                    <Form.Control
                      type="text"
                      value={surname}
                      onChange={(e) => setSurname(e.target.value)}
                    />
                  </Form.Group>
                  <Form.Group className="mb-3" controlId="formBasicPassword">
                    <Form.Label>Genres</Form.Label>
                    <Form.Control type="text" />
                  </Form.Group>
                  <Form.Group className="mb-3" controlId="formBasicPassword">
                    <Form.Label>Enter your Bio</Form.Label>
                    <Form.Control
                      type="text"
                      value={bio}
                      onChange={(e) => setBio(e.target.value)}
                    />
                  </Form.Group>
                  <Form.Group className="mb-3" controlId="formBasicPassword">
                    <Form.Label>Pseudonym</Form.Label>
                    <Form.Control
                      type="text"
                      value={pseudonym}
                      onChange={(e) => setPseudonym(e.target.value)}
                    />
                  </Form.Group>

                  <Button
                    variant="primary"
                    type="submit"
                    className="d-flex mx-auto"
                  >
                    Submit
                  </Button>
                </Form>
              </div>
            </div>
          )}
          <Card className="text-center">
            <Card.Header>User Info</Card.Header>
            <Card.Body>
              <Card.Img
                className="rounded-circle mb-2 mt-2 text-center"
                style={{ width: "110px", height: "110px" }}
                src={image || default_profile_image}
              />
              <input
                type="file"
                accept="image/*"
                style={{ display: "none" }}
                id="imageUpload"
                onChange={onImageChange}
              />
              <Card.Title className="display-6 text-center">
                {username}
              </Card.Title>
              <div className="d-flex flex-column flex-sm-row align-items-center justify-content-evenly mb-3 ms-auto">
                <Button variant="danger" onClick={onLogout}>
                  Logout
                </Button>
                <Button
                  variant="primary"
                  className="mt-2 mt-sm-0"
                  onClick={() => document.getElementById("imageUpload").click()}
                >
                  Change Image
                </Button>
                <Button className="mt-2 mt-sm-0" onClick={handleBecomeAuthor}>
                  Author
                </Button>
              </div>
            </Card.Body>
          </Card>
        </Col>
      </Row>
    </Container>
  );
}
