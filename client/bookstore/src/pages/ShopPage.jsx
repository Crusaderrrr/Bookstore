import React, { useEffect, useState } from "react";
import { Container, Row, Col, Card, Button } from "react-bootstrap";
import axiosInstance from "../config/axiosConfig";
import BookCard from "../components/BookCard";

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

  return (
    <Container className="mt-4">
      <h1 className="mb-4">Shop Our Books</h1>
      <Row xs={1} md={2} lg={4} className="g-4">
        {items.map((book) => (
          <Col key={book.id}>
            <BookCard book={book}/>
          </Col>
        ))}
      </Row>
    </Container>
  );
}
