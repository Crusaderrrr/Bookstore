import React, { useEffect, useState } from "react";
import { Container, Row, Col, Card, Button } from "react-bootstrap";
import axiosInstance from "../config/axiosConfig";

export default function ShopPage() {
  const [items, setItems] = useState([]);

  useEffect(() => {
    const fetchBooks = async () => {
      try {
        const response = await axiosInstance.get("/books/all");
        setItems(response.data);
      } catch (err) {
        console.error("Error fetching books:", err);
      }
    }
    fetchBooks();
  }, []);

  const handleOpenBookDetails = (id) => {
    window.location.href = `/books/${id}`;
  };

  return (
    <Container className="mt-4">
      <h1 className="mb-4">Shop Our Books</h1>
      <Row xs={1} md={2} lg={4} className="g-4">
        {items.map((book) => (
          <Col key={book.id}>
            <Card className="h-100" onClick={() => handleOpenBookDetails(book.id)}>
              <Card.Img
                variant="top"
                alt="Book Image"
              />
              <Card.Body className="d-flex flex-column">
                <Card.Title>{book.title}</Card.Title>
                <Card.Text>{book.description}</Card.Text>
                <div className="mt-auto d-flex justify-content-between align-items-center">
                  <span className="fw-bold">${book.price}</span>
                </div>
              </Card.Body>
            </Card>
          </Col>
        ))}
      </Row>
    </Container>
  );
}
