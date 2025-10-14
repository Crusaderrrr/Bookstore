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
import book_cover from "../assets/book_cover.jpg";
import { default as MuiButton } from "@mui/material/Button";
import { default as MuiButtonGroup } from "@mui/material/ButtonGroup";
import Chip from "@mui/material/Chip";
import HourglassFullIcon from "@mui/icons-material/HourglassFull";
import DoneIcon from "@mui/icons-material/Done";
import ErrorIcon from "@mui/icons-material/Error";
import AppBar from "@mui/material/AppBar";
import Tabs from "@mui/material/Tabs";
import Tab from "@mui/material/Tab";
import "../style/moderationRequest.css";
import TextField from "@mui/material/TextField";

export default function AdminPage() {
  const [users, setUsers] = useState([]);
  const [selectedUsers, setSelectedUsers] = useState([]);
  const sortedUsers = users.sort((a, b) => {
    a.username.localeCompare(b.username);
  });
  const [value, setValue] = useState(0);
  const [requests, setRequests] = useState([]);
  const [reasonId, setReasonId] = useState("");
  const [reason, setReason] = useState("");
  const [rejectLoading, setRejectLoading] = useState(false);

  const statusMap = ["PENDING", "REJECTED", "APPROVED"];

  const filteredRequests = requests.filter(
    (req) => req.status === statusMap[value]
  );

  const handleChange = (event, newValue) => {
    setValue(newValue);
  };

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
    const fetchRequests = async () => {
      try {
        const response = await axiosInstance.get("/moderation/requests/all");
        setRequests(response.data);
      } catch (err) {
        console.error(err);
      }
    };
    fetchRequests();
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
      setUsers(users.filter((user) => !selectedUsers.includes(user.id)));
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

  const handleApproveRequest = async (id) => {
    try {
      const response = await axiosInstance.post(
        `/moderation/requests/${id}/approve`
      );
      window.location.reload();
    } catch (err) {
      console.error(err);
    }
  };

  const handleRejectRequest = async (id) => {
    setRejectLoading(true);
    try {
      const response = await axiosInstance.post(
        `/moderation/requests/${id}/reject`, reason
      );
      window.location.reload();
      if (response.status === 200) {
        setRejectLoading(false);
      }
    } catch (err) {
      setRejectLoading(false);
      console.error(err);
    }
  };

  const handleAddReason = (id) => {
    setReasonId(id);
  };

  const handleReasonChange = (e) => {
    setReason(e.target.value);
  };

  return (
    <Container>
      <Row className="gap-2 g-3">
        <Col className="mt-3 border border-1 rounded-3 shadow-sm">
          <h1 className="display-6 mb-4 text-center">Users</h1>
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
        <Col className="border border-1 rounded-3 shadow-sm">
          <h1 className="display-6 text-center">Requests</h1>
          <AppBar position="static" color="transparent" className="mb-4">
            <Tabs
              value={value}
              onChange={handleChange}
              indicatorColor="primary"
              textColor="primary"
              centered
            >
              <Tab icon={<HourglassFullIcon />} aria-label="pending" />
              <Tab icon={<ErrorIcon />} aria-label="rejected" />
              <Tab icon={<DoneIcon />} aria-label="approved" />
            </Tabs>
          </AppBar>
          {filteredRequests.map((request) => (
            <div key={request.id} className="px-3">
              <div className="d-flex">
                <img
                  src={request.imageUrl || book_cover}
                  className="rounded float-start me-3"
                  style={{ maxWidth: "100px", maxHeight: "150px" }}
                ></img>
                <div className="d-flex flex-column">
                  <span className="text-muted fw-light font-monospace mb-2">
                    {new Date(request.createdAt).toLocaleString()}
                  </span>
                  <h5 className="fw-bold mb-0">{request.title}</h5>
                  <p className="text-muted ms-2 mb-1">
                    By: {request.author.name} {request.author.surname}
                  </p>
                  <p className="description-text mb-2">{request.description}</p>
                  <p className="fw-bold">{request.price}$</p>
                </div>
                <div className="d-flex flex-column mx-auto">
                  {request.status === "PENDING" && (
                    <Chip
                      label={request.status}
                      variant="outlined"
                      icon={<HourglassFullIcon fontSize="small" />}
                    />
                  )}
                  {request.status === "APPROVED" && (
                    <Chip
                      label={request.status}
                      color="success"
                      icon={<DoneIcon fontSize="small" />}
                    />
                  )}
                  {request.status === "REJECTED" && (
                    <Chip
                      label={request.status}
                      color="error"
                      icon={<ErrorIcon fontSize="small" />}
                    />
                  )}
                </div>
              </div>
              {request.status === "PENDING" && reasonId !== request.id && (
                <div className="d-flex mt-2">
                  <MuiButtonGroup variant="contained" className="mx-auto">
                    <MuiButton
                      variant="contained"
                      color="success"
                      onClick={() => handleApproveRequest(request.id)}
                    >
                      Approve
                    </MuiButton>
                    <MuiButton
                      variant="contained"
                      color="error"
                      onClick={() => handleAddReason(request.id)}
                    >
                      Reject
                    </MuiButton>
                  </MuiButtonGroup>
                </div>
              )}
              {reasonId === request.id && request.status === "PENDING" && (
                <div className="d-flex mt-3">
                  <TextField
                    value={reason}
                    onChange={handleReasonChange}
                    id="reason"
                    label="Reason"
                    variant="standard"
                    fullWidth
                    color="warning"
                    className="me-4"
                  />
                  <MuiButton
                    variant="outlined"
                    color="warning"
                    loading={rejectLoading}
                    onClick={() => handleRejectRequest(request.id)}
                  >
                    Reject
                  </MuiButton>
                </div>
              )}
              <hr />
            </div>
          ))}
        </Col>
      </Row>
    </Container>
  );
}
