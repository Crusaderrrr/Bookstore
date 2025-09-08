import React, { useState } from "react";
import { Container, Table, Button, Form, ButtonToolbar } from "react-bootstrap";

export default function CartPage() {

  const initialItems = [
    { id: 1, title: "Book Title 1", quantity: 2, price: 19.99 },
    { id: 2, title: "Book Title 2", quantity: 1, price: 24.99 },
    { id: 3, title: "Book Title 3", quantity: 3, price: 15.5 },
    { id: 4, title: "Another Book", quantity: 1, price: 29.99 },
  ];

  const [cartItems, setCartItems] = useState(initialItems);
  const [selectedItems, setSelectedItems] = useState([]);

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

  const handleDeleteSelected = () => {
    setCartItems((prevItems) =>
      prevItems.filter((item) => !selectedItems.includes(item.id))
    );
    setSelectedItems([]);
  };

  const isAllSelected =
    cartItems.length > 0 && selectedItems.length === cartItems.length;

  return (
    <Container className="mt-4">
      <h1 className="mb-4">Your Shopping Cart</h1>

      <ButtonToolbar className="mb-3">
        <Button
          variant="danger"
          onClick={handleDeleteSelected}
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
              <tr key={item.id}>
                <td>
                  <Form.Check
                    type="checkbox"
                    checked={selectedItems.includes(item.id)}
                    onChange={() => handleSelectItem(item.id)}
                    aria-label={`Select ${item.title}`}
                  />
                </td>
                <td>{item.title}</td>
                <td>{item.quantity}</td>
                <td>${item.price.toFixed(2)}</td>
                <td>${(item.quantity * item.price).toFixed(2)}</td>
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
