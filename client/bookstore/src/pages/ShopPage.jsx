import React, { useEffect, useState } from "react";
import { Container, Row, Col, Card, Button, Pagination } from "react-bootstrap";
import axiosInstance from "../config/axiosConfig";
import BookCard from "../components/BookCard";

export default function ShopPage() {
  const [items, setItems] = useState([]);
  const [currentPage, setCurrentPage] = useState(1);
  const itemsPerPage = 12;
  const indexOfLastItem = currentPage * itemsPerPage;
  const indexOfFirstItem = indexOfLastItem - itemsPerPage;
  const currentItems = items.slice(indexOfFirstItem, indexOfLastItem);
  const totalPages = Math.ceil(items.length / itemsPerPage);

  function BookPagination({ totalPages, currentPage, setCurrentPage }) {
    let items = [];
    for (let number = 1; number <= totalPages; number++) {
      items.push(
        <Pagination.Item
          key={number}
          active={number === currentPage}
          onClick={() => setCurrentPage(number)}
        >
          {number}
        </Pagination.Item>
      );
    }
    return <Pagination>{items}</Pagination>;
  }

  useEffect(() => {
    const fetchBooks = async () => {
      try {
        const response = await axiosInstance.get("/books/all");
        setItems(response.data);
      } catch (err) {
        console.error("Error fetching books:", err);
      }
    };
    fetchBooks();
  }, []);

  return (
    <Container className="mt-4">
      <h1 className="mb-4">Shop Our Books</h1>
      <Row xs={1} md={2} lg={4} className="g-4 mb-5">
        {currentItems.map((book) => (
          <Col key={book.id}>
            <BookCard book={book} />
          </Col>
        ))}
      </Row>
      <div className="d-flex justify-content-center mt-2 mb-3">
        <BookPagination
          totalPages={totalPages}
          currentPage={currentPage}
          setCurrentPage={setCurrentPage}
        />
      </div>
    </Container>
  );
}
