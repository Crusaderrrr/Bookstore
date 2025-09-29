import React, { useEffect, useState } from "react";
import { useParams } from "react-router-dom";
import axiosInstance from "../config/axiosConfig";
import { Col, Container, Row } from "react-bootstrap";
import book_cover from "../assets/book_cover.jpg";

export default function BookDetailsPage() {
  const { id } = useParams();
  const [book, setBook] = useState();

  useEffect(() => {
    const fetchBookDetails = async () => {
      try {
        const response = await axiosInstance.get(`/books/${id}`);
        console.log(response.data);
      } catch (err) {
        console.error("Error fetching book details:", err);
      }
    };
    fetchBookDetails();
  }, []);

  return (
    <Container>
      <Row className="align-items-center">
        <Col className="text-center mt-5">
          <img src={book_cover} alt="" style={{ maxHeight: "500px" }} />
        </Col>
        <Col lg={6} className="text-center text-lg-start">
          <h1 className="display-1 text-bold">Book Title</h1>
          <button className="btn btn-primary mt-2 mb-4">Add to Cart</button>
          <h4 className="display-6">Description</h4>
          <p>{book?.description}</p>
          <h4 className="display-6">Author:</h4>
          <></>
        </Col>
      </Row>
    </Container>
  );
}
