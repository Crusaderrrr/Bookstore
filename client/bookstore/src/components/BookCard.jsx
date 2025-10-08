import React from "react";
import { Card } from "react-bootstrap";
import "../style/bookStyle.css";
import Checkbox from "@mui/material/Checkbox";

export default function BookCard({
  book,
  showCheck,
  isChecked,
  onCheckboxChange,
}) {
  const default_book_cover =
    "https://res.cloudinary.com/dupcshdti/image/upload/v1759743713/book_cover_rllhzg.jpg";

  const handleOpenBookDetails = (id) => {
    window.location.href = `/books/${id}`;
  };

  const onCheckboxChangeInternal = (e) => {
    onCheckboxChange(book.id, e.target.checked);
  };

  return (
    <Card
      className="h-100 custom-card"
      onClick={() => handleOpenBookDetails(book.id)}
    >
      {showCheck && (
        <div className="checkbox-top-left" onClick={(e) => e.stopPropagation()}>
          <Checkbox className="d-flex"  onChange={onCheckboxChangeInternal} checked={isChecked} />
        </div>
      )}
      <div className="card-image-container">
        <Card.Img
          variant="top"
          alt="Book img"
          src={book.bookImage?.url || default_book_cover}
          className="mt-3"
        />
      </div>
      <hr />
      <Card.Body className="d-flex flex-column">
        <Card.Title>{book.title}</Card.Title>
        <Card.Text>{book.description}</Card.Text>
        <div className="mt-auto d-flex justify-content-between align-items-center">
          <span className="fw-bold">${book.price}</span>
        </div>
      </Card.Body>
    </Card>
  );
}
