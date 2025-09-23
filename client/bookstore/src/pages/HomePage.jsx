import React, { useContext, useEffect, useState } from "react";
import { Carousel, Col, Container, Row } from "react-bootstrap";


export default function HomePage() {
  const [users, setUsers] = useState([]);

    return (
    <Container fluid>
      <Row className="min-vh-100 justify-content-center">
        <Col xs={12} md={9} lg={6} className="mx-auto">
        <h1 className="text-center mt-2 mb-2 display-4 fw-normal">Welcome to our Bookstore!</h1>
          <div style={{ position: "relative", minHeight: "400px" }}>
            <Carousel className="carousel">
              <Carousel.Item>
                <img
                  src="\src\assets\bookstore_carousel_1.jpg"
                  className="carousel-img"
                  style={{ width: "100%", height: "auto" }}
                  alt="slide 1"
                />
              </Carousel.Item>
              <Carousel.Item>
                <img
                  src="\src\assets\bookstore_carousel_2.jpg"
                  className="carousel-img"
                  style={{ width: "100%", height: "auto" }}
                  alt="slide 2"
                />
              </Carousel.Item>
              <Carousel.Item>
                <img
                  src="\src\assets\bookstore_carousel_3.jpg"
                  className="carousel-img"
                  style={{ width: "100%", height: "auto" }}
                  alt="slide 3"
                />
              </Carousel.Item>
            </Carousel>
          </div>
          <hr className=""/>
          <h3 className="mt-3 display-6">
            Bestsellers
          </h3>
        </Col>
      </Row>
    </Container>
  );
}
