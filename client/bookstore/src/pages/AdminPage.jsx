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
import axiosInstance from "../config/axiosConfig";

export default function AdminPage() {
  const [users, setUsers] = useState([]);
  const [selectedUsers, setSelectedUsers] = useState([]);
  const sortedUsers = users.sort((a, b) => {a.username.localeCompare(b.username)})

  useEffect(() => {
    const fetchUsers = async () => {
      try {
        const response = await axiosInstance.get("/users/all");
        console.log(response.data);
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

  const handleDeleteSelected = async () => {
    if (selectedUsers.length === 0) {
      return;
    }

    try {
      const response = await axiosInstance.post("/users/delete", selectedUsers);
      setUsers(users.filter(user => !selectedUsers.includes(user.id)));
      setSelectedUsers([]);
    } catch (err) {
      console.error(err);
    }
  };

  const handleBlockUsers = async () => {
    if (selectedUsers.length === 0) {
      return;
    }

    try {
      const response = await axiosInstance.post("/users/block", selectedUsers);
      setSelectedUsers([]);
      window.location.reload();
    } catch (err) {
      console.error(err);
    }
  };

  const handleUnblockUsers = async () => {
    if (selectedUsers.length === 0) {
      return;
    }

    try {
      const response = await axiosInstance.post(
        "/users/unblock",
        selectedUsers
      );
      setSelectedUsers([]);
      window.location.reload();
    } catch (err) {
      console.error(err);
    }
  };

  const handleGiveAdminRights = async () => {
    if (selectedUsers.length === 0) {
      return;
    }

    try {
      const response = await axiosInstance.post(
        "/users/make_admin",
        selectedUsers
      );
      setSelectedUsers([]);
      window.location.reload();
    } catch (err) {
      console.error(err);
    }
  };

  const handleRemoveAdminRights = async () => {
    if (selectedUsers.length === 0) {
      return;
    }

    try {
      const response = await axiosInstance.post(
        "/users/remove_admin",
        selectedUsers
      );
      setSelectedUsers([]);
      window.location.reload();
    } catch (err) {
      console.error(err);
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
              <Button
                variant="primary"
                title="Block user"
                onClick={handleBlockUsers}
              >
                <i className="bi bi-lock"></i>
              </Button>
              <Button
                variant="primary"
                title="Unblock user"
                onClick={handleUnblockUsers}
              >
                <i className="bi bi-unlock"></i>
              </Button>
            </ButtonGroup>
            <ButtonGroup>
              <Button
                variant="success"
                title="Add admin rights"
                onClick={handleGiveAdminRights}
              >
                <i className="bi bi-person-plus-fill"></i>
              </Button>
              <Button
                variant="danger"
                title="Remove admin rights"
                onClick={handleRemoveAdminRights}
              >
                <i className="bi bi-person-x-fill"></i>
              </Button>
              <Button
                variant="secondary"
                title="Delete user"
                onClick={handleDeleteSelected}
              >
                <i className="bi bi-trash3"></i>
              </Button>
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
                <th>Active</th>
              </tr>
            </thead>
            <tbody>
              {sortedUsers.map((user, i) => (
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
                  <td>{user.active ? "Yes" : "No"}</td>
                </tr>
              ))}
            </tbody>
          </Table>
        </Col>
      </Row>
    </Container>
  );
}
