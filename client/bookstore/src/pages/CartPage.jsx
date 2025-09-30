import React, { useEffect, useState } from "react";
import { Container, Table, Button, Form, ButtonToolbar } from "react-bootstrap";
import axiosInstance from "../config/axiosConfig";

export default function CartPage() {
  const [cartItems, setCartItems] = useState([]);
  const [selectedItems, setSelectedItems] = useState([]);

  useEffect(() => {
    const fetchCartItems = async () => {
      try {
        const response = await axiosInstance.get("/cart/items");
        setCartItems(response.data);
      } catch (err) {
        console.error("Error fetching cart items:", err);
      }
    };
    fetchCartItems();
  }, []);

  const handleSelectItem = (id) => {
    setSelectedItems((prevSelected) =>
      prevSelected.includes(id)
        ? prevSelected.filter((itemId) => itemId !== id)
        : [...prevSelected, id]
    );
  };

  const handleSelectAll = (e) => {
    if (e.target.checked) {
      setSelectedItems(cartItems.map((item) => item.id));
    } else {
      setSelectedItems([]);
    }
  };

  const handleRemoveFromCart = async () => {
    try {
      const response = await axiosInstance.post("/cart/remove", selectedItems);
      if (response.status === 200) {
        setCartItems(prev => prev.filter(item => !selectedItems.includes(item.book.id)));
        setSelectedItems([]);
      }
    } catch (err) {
      console.error("Error removing from cart:", err);
    }
  };

  const isAllSelected =
    cartItems.length > 0 && selectedItems.length === cartItems.length;

  return (
    <Container className="mt-4">
      <h1 className="mb-4">Your Shopping Cart</h1>

      <ButtonToolbar className="mb-3">
        <Button
          variant="danger"
          onClick={handleRemoveFromCart}
          disabled={selectedItems.length === 0}
        >
          Delete Selected
        </Button>
      </ButtonToolbar>

      <Table striped bordered hover responsive>
        <thead>
          <tr>
            <th>
              <Form.Check
                type="checkbox"
                checked={isAllSelected}
                onChange={handleSelectAll}
                aria-label="Select all items"
              />
            </th>
            <th>Item</th>
            <th>Quantity</th>
            <th>Price</th>
            <th>Total</th>
          </tr>
        </thead>
        <tbody>
          {cartItems.length > 0 ? (
            cartItems.map((item) => (
              <tr key={item.book.id}>
                <td>
                  <Form.Check
                    type="checkbox"
                    checked={selectedItems.includes(item.book.id)}
                    onChange={() => handleSelectItem(item.book.id)}
                    aria-label={`Select ${item.book.title}`}
                  />
                </td>
                <td>{item.book.title}</td>
                <td>{item.quantity}</td>
                <td>${item.book.price.toFixed(2)}</td>
                <td>${(item.quantity * item.book.price).toFixed(2)}</td>
              </tr>
            ))
          ) : (
            <tr>
              <td colSpan="5" className="text-center">
                Your cart is empty.
              </td>
            </tr>
          )}
        </tbody>
      </Table>
    </Container>
  );
}
