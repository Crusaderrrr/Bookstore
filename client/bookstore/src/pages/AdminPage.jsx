import React, { use, useEffect, useState } from "react";
import {
  Button,
  ButtonGroup,
  ButtonToolbar,
  Col,
  Container,
  Form,
  Row,
  Table,
} from "react-bootstrap";
import axios from "axios";

export default function AdminPage() {
  const [users, setUsers] = useState([]);
  const [selectedUsers, setSelectedUsers] = useState([]);

  useEffect(() => {
    const fetchUsers = async () => {
      try {
        const response = await axios.get("http://localhost:8080/users/all", {
          withCredentials: true,
          headers: {
            Authorization: "Bearer " + localStorage.getItem("accessToken"),
          },
        });
        setUsers(response.data);
      } catch (err) {
        console.log(err);
      }
    };
    fetchUsers();
  }, []);

  const handleToggleUser = (e) => {
    if (selectedUsers.includes(e)) {
      setSelectedUsers(selectedUsers.filter((id) => id !== e));
    } else {
      setSelectedUsers([...selectedUsers, e]);
    }
  };

  const handleSelectAll = () => {
    if (selectedUsers.length === users.length) {
      setSelectedUsers([]);
    } else {
      setSelectedUsers(users.map((user) => user.id));
    }
  };

  return (
    <Container>
      <Row>
        <Col className="mt-3">
          <ButtonToolbar
            className="justify-content-between mb-2"
            aria-label="Toolbar with Button groups"
          >
            <ButtonGroup aria-label="First group">
              <Button variant="primary" title="Block user"><i className="bi bi-lock"></i></Button>
              <Button variant="primary"  title="Unblock user"><i className="bi bi-unlock"></i></Button>
            </ButtonGroup>
            <ButtonGroup>
              <Button variant="success" title="Add admin rights"><i className="bi bi-person-plus-fill"></i></Button>
              <Button variant="danger" title="Remove admin rights"><i className="bi bi-person-x-fill"></i></Button>
              <Button variant="secondary" title="Delete user"><i className="bi bi-trash3"></i></Button>
            </ButtonGroup>
          </ButtonToolbar>
          <Table striped bordered hover>
            <thead>
              <tr>
                <th>
                  <Form.Check
                    type="checkbox"
                    label=""
                    checked={selectedUsers.length === users.length}
                    onChange={() => handleSelectAll()}
                  />
                </th>
                <th>Username</th>
                <th>Email</th>
                <th>Role</th>
              </tr>
            </thead>
            <tbody>
              {users.map((user, i) => (
                <tr key={user.id}>
                  <td>
                    <Form.Check
                      type="checkbox"
                      id={`toggle-user-${user.id}`}
                      label=""
                      checked={selectedUsers.includes(user.id)}
                      onChange={() => handleToggleUser(user.id)}
                    />
                  </td>
                  <td>{user.username}</td>
                  <td>{user.email}</td>
                  <td>{user.roles}</td>
                </tr>
              ))}
            </tbody>
          </Table>
        </Col>
      </Row>
    </Container>
  );
}
