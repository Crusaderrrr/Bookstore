import React, { useState } from "react";
import { Button, ButtonGroup, Col, Form, Row } from "react-bootstrap";
import BookCard from "../BookCard";
import "../../style/authorForm.css";
import axiosInstance from "../../config/axiosConfig";

export default function MyBooks({ books }) {
  const [createBook, setCreateBook] = useState(false);
  const [alertMessage, setAlertMessage] = useState("");
  const [alertType, setAlertType] = useState("");
  const [title, setTitle] = useState("");
  const [description, setDescription] = useState("");
  const [price, setPrice] = useState("");
  const [file, setFile] = useState(null);

  const handleSubmitBook = async (e) => {
    e.preventDefault();

    if (!file) {
      setAlertType("danger");
      setAlertMessage("Please select an image.");
      return;
    }

    const bookData = {
      title,
      description,
      price,
    };

    const formData = new FormData();
    formData.append(
      "book",
      new Blob([JSON.stringify(bookData)], { type: "application/json" })
    );
    formData.append("file", file);

    try {
      const response = await axiosInstance.post("/books/new", formData, {
        headers: { "Content-Type": "multipart/form-data" },
      });
      if (response.status === 200) {
        setAlertType("success");
        setAlertMessage("Book created successfully");
      }
      setCreateBook(false);
    } catch (err) {
      console.error(err);
      setAlertMessage("Error creating book");
      setAlertType("danger");
    }
  };

  const handlePriceChange = (e) => {
    const value = e.target.value;
    if (value === "" || /^\d*\.?\d*$/.test(value)) {
      setPrice(value);
    }
  };

  return (
    <>
      <div className="d-flex align-items-center mt-3">
        <h1 className="ms-3 mt-2 display-6">My Books</h1>
        <ButtonGroup className="ms-auto">
          <Button variant="outline-success" onClick={() => setCreateBook(true)}>
            +
          </Button>
          <Button variant="outline-danger">
            <i className="bi bi-trash3"></i>
          </Button>
        </ButtonGroup>
      </div>
      <Row className="mt-3">
        {books.map((book) => (
          <Col key={book.id} xs={12} md={4} className="mb-4">
            <BookCard book={book} />
          </Col>
        ))}
      </Row>
      {createBook && (
        <div className="background-blur">
          <div className="form-center">
            <div className="d-flex align-items-center">
              {alertMessage && (
                <Alert className="ms-auto" variant={alertType}>
                  {alertMessage}
                </Alert>
              )}
              <Button
                variant="outline-secondary"
                size="sm"
                className="ms-auto mb-2 "
                onClick={() => setCreateBook(false)}
              >
                X
              </Button>
            </div>
            <Form onSubmit={handleSubmitBook}>
              <Form.Group className="mb-3">
                <Form.Label>Book Image</Form.Label>
                <Form.Control
                  type="file"
                  accept="image/*"
                  onChange={(e) => setFile(e.target.files[0])}
                  required
                />
              </Form.Group>
              <Form.Group className="mb-3">
                <Form.Label>Title</Form.Label>
                <Form.Control
                  type="text"
                  value={title}
                  onChange={(e) => setTitle(e.target.value)}
                />
              </Form.Group>
              <Form.Group className="mb-3">
                <Form.Label>Description</Form.Label>
                <Form.Control
                  type="text"
                  value={description}
                  onChange={(e) => setDescription(e.target.value)}
                />
              </Form.Group>
              <Form.Group className="mb-3">
                <Form.Label>Price</Form.Label>
                <Form.Control
                  type="text"
                  value={price}
                  onChange={handlePriceChange}
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
    </>
  );
}
