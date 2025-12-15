import React, { useEffect, useState } from "react";
import { useParams } from "react-router-dom";
import axiosInstance from "../config/axiosConfig";
import {
  Button,
  ButtonGroup,
  Col,
  Container,
  FormControl,
  InputGroup,
  Row,
} from "react-bootstrap";
import "../style/bookStyle.css";

export default function BookDetailsPage() {
  const { id } = useParams();
  const [book, setBook] = useState({});
  const [quantity, setQuantity] = useState(1);
  const [isLiked, setIsLiked] = useState(false);
  const [buttonText, setButtonText] = useState("Add to Cart");
  const default_book_cover = "https://res.cloudinary.com/dupcshdti/image/upload/v1759743713/book_cover_rllhzg.jpg";
  const min = 1;
  const max = 30;

  useEffect(() => {
    const fetchBookDetails = async () => {
      try {
        const response = await axiosInstance.get(`/books/${id}`);
        setBook(response.data);
      } catch (err) {
        console.error("Error fetching book details:", err);
      }
    };
    fetchBookDetails();
  }, []);

  const handleToggleLike = async () => {
    if (isLiked) {
      try {
        const response = await axiosInstance.post("/likes/remove", {bookId : id});
        if (response.status === 200) {
          setIsLiked(false);
        }
      } catch (err) {
        console.error("Error fetching book details:", err);
      }
    } else {
      try {
        const response = await axiosInstance.post("/likes/add", {bookId : id});
        if (response.status === 200) {
          setIsLiked(true);
        }
      } catch (err) {
        console.error("Error fetching book details:", err);
      }
    }
  };

  const handleAddToCart = async () => {
    const formData = new FormData();
    formData.append("bookId", id);
    formData.append("quantity", quantity);
    try {
      const response = await axiosInstance.post("/cart/add", formData);
      if (response.status === 200) {
        setButtonText("Added to Cart");
      }
      console.log("Item added to cart");
    } catch (err) {
      console.error("Error adding to cart:", err);
    }
  };

  const handleChange = (e) => {
    const val = Number(e.target.value) || 1;
    setQuantity(val);
  };

  return (
    <Container>
      <Row className="align-items-center">
        <Col className="text-center mt-5">
          <img src={book.bookImage?.url || default_book_cover} alt="" className="book-cover" />
          <div className="d-flex mt-2 mb-3">
            <Button className="mx-auto text-danger" variant="link" onClick={handleToggleLike}>
              {isLiked ? <i className="bi bi-heart-fill" style={{ fontSize: "1.5rem" }}></i> : <i className="bi bi-heart" style={{ fontSize: "1.5rem" }}></i>}
            </Button>
          </div>
        </Col>
        <Col lg={6} className="text-center text-lg-start">
          <div className="mb-4">
            <h6 className="display-5 text-bolder">{book.title}</h6>
            <span className="text-muted ">
              By {book.authorInfo?.surname} {book.authorInfo?.name}
            </span>
          </div>
          <h3 className=" mt-4">Description</h3>
          <p>{book?.description}</p>
          <h3 className="d-inline">Price:</h3>
          <span className="ms-2 fs-5">{book.price}$</span>
          <div
            className="d-flex align-items-center mx-auto mx-lg-0 me-lg-auto mt-3"
            style={{ maxWidth: "300px" }}
          >
            <span className="me-2" style={{ whiteSpace: "nowrap" }}>
              Quantity:
            </span>
            <InputGroup style={{ maxWidth: "60px" }}>
              <FormControl
                type="number"
                value={quantity}
                onChange={handleChange}
                min={min}
                max={max}
                style={{ textAlign: "center" }}
              />
            </InputGroup>
            <Button className="ms-3" variant={buttonText === "Added to Cart" ? "success" : "primary"} onClick={handleAddToCart}>
              {buttonText}
            </Button>
          </div>
        </Col>
      </Row>
    </Container>
  );
}
