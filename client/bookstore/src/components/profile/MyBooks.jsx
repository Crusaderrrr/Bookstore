import React, { use, useContext, useState } from "react";
import { Col, Form, Row, Alert } from "react-bootstrap";
import BookCard from "../BookCard";
import "../../style/authorForm.css";
import axiosInstance from "../../config/axiosConfig";
import ButtonGroup from "@mui/material/ButtonGroup";
import Button from "@mui/material/Button";
import { AppContext } from "../../context/AppContext";
import CloseIcon from "@mui/icons-material/Close";
import IconButton from "@mui/material/IconButton";
import Checkbox from "@mui/material/Checkbox";

export default function MyBooks({ books }) {
  const [createBook, setCreateBook] = useState(false);
  const [alertMessage, setAlertMessage] = useState("");
  const [alertType, setAlertType] = useState("");
  const [title, setTitle] = useState("");
  const [description, setDescription] = useState("");
  const [price, setPrice] = useState("");
  const [file, setFile] = useState(null);
  const { theme } = useContext(AppContext);
  const [loading, setLoading] = useState(false);
  const [selectedBooks, setSelectedBooks] = useState([]);

  const handleToggleAll = () => {
    if (selectedBooks.length !== books.length) {
      setSelectedBooks(books.map((book) => book.id));
    } else {
      setSelectedBooks([]);
    }
  };

  const handleCheckboxChange = (bookId, isChecked) => {
    if (isChecked) {
      setSelectedBooks([...selectedBooks, bookId]);
    } else {
      setSelectedBooks(selectedBooks.filter((id) => id !== bookId));
    }
  };

  const handleSubmitBook = async (e) => {
    e.preventDefault();
    setLoading(true);

    if (!file) {
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
        setAlertMessage("Book uploaded successfully");
      }
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

  const handleDeleteSelected = async () => {
    try {
      const response = await axiosInstance.post("/books/delete", selectedBooks);
      console.log(response)
      if (response.status === 200) {
        setSelectedBooks([]);
        // window.location.reload();
      }
    } catch (err) {
      console.error(err);
    }
  };



  return (
    <>
      <div className="d-flex align-items-center mt-3">
        <h1 className="ms-3 mt-2 display-6">My Books</h1>
        <Checkbox className="mt-2" onChange={handleToggleAll}/>
        <ButtonGroup className="ms-auto">
          <Button
            variant="outlined"
            color="success"
            onClick={() => setCreateBook(true)}
          >
            +
          </Button>
          <Button color="error" onClick={handleDeleteSelected}>
            <i className="bi bi-trash3"></i>
          </Button>
        </ButtonGroup>
      </div>
      <Row className="mt-3">
        {books.map((book) => (
          <Col key={book.id} xs={12} md={4} className="mb-4">
            <BookCard
              book={book}
              showCheck={true}
              isChecked={selectedBooks.includes(book.id)}
              onCheckboxChange={handleCheckboxChange}
            />
          </Col>
        ))}
      </Row>
      {createBook && (
        <div className="background-blur">
          <div
            className={`form-center bg-${theme === "dark" ? "dark" : "light"}`}
          >
            <div className="d-flex align-items-center">
              {alertMessage && (
                <Alert className="p-relative ms-auto" variant={alertType}>
                  {alertMessage}
                </Alert>
              )}
              <IconButton
                variant="outline-secondary"
                size="sm"
                className="ms-auto mb-2 "
                onClick={() => setCreateBook(false)}
              >
                <CloseIcon />
              </IconButton>
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
                  required
                />
              </Form.Group>
              <Form.Group className="mb-3">
                <Form.Label>Description</Form.Label>
                <Form.Control
                  type="text"
                  value={description}
                  onChange={(e) => setDescription(e.target.value)}
                  required
                />
              </Form.Group>
              <Form.Group className="mb-3">
                <Form.Label>Price</Form.Label>
                <Form.Control
                  type="text"
                  value={price}
                  onChange={handlePriceChange}
                  required
                />
              </Form.Group>

              <Button
                variant="contained"
                color="success"
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
