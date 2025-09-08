import React from "react";
import { Container, Row, Col, Card, Button } from "react-bootstrap";

export default function ShopPage() {

  const items = Array.from({ length: 8 }, (_, i) => ({
    id: i + 1,
    title: `Book Title ${i + 1}`,
    description:
      "A brief description of the book, enticing the reader to learn more.",
    price: `${(19.99 + i * 2).toFixed(2)}`,
  }));

  return (
    <Container className="mt-4">
      <h1 className="mb-4">Shop Our Books</h1>
      <Row xs={1} md={2} lg={4} className="g-4">
        {items.map((item) => (
          <Col key={item.id}>
            <Card className="h-100">
              <Card.Img
                variant="top"
                // src={`https://via.placeholder.com/150/888/FFF?text=Book+${item.id}`}
                alt="Book Image"
              />
              <Card.Body className="d-flex flex-column">
                <Card.Title>{item.title}</Card.Title>
                <Card.Text>{item.description}</Card.Text>
                <div className="mt-auto d-flex justify-content-between align-items-center">
                  <span className="fw-bold">${item.price}</span>
                  <Button variant="primary">Add to Cart</Button>
                </div>
              </Card.Body>
            </Card>
          </Col>
        ))}
      </Row>
    </Container>
  );
}
