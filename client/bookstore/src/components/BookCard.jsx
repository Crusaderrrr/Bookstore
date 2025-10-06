import React from "react";
import { Card } from "react-bootstrap";
import "../style/bookStyle.css";

export default function BookCard({ book }) {

  const default_book_cover = "https://res.cloudinary.com/dupcshdti/image/upload/v1759743713/book_cover_rllhzg.jpg";

  const handleOpenBookDetails = (id) => {
    window.location.href = `/books/${id}`;
  };

  return (
    <Card className="h-100 custom-card" onClick={() => handleOpenBookDetails(book.id)}>
      <div className="card-image-container">
        <Card.Img variant="top" alt="Book img" src={book.bookImage?.url || default_book_cover} className="mt-3"/>
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
