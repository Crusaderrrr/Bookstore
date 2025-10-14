import { AppBar, Chip, Drawer, Tab, Tabs } from "@mui/material";
import React, { useEffect, useState } from "react";
import { Col } from "react-bootstrap";
import HourglassFullIcon from "@mui/icons-material/HourglassFull";
import DoneIcon from "@mui/icons-material/Done";
import ErrorIcon from "@mui/icons-material/Error";
import book_cover from "../../assets/book_cover.jpg";

export default function RequestsDrawer({
  requests,
  handleTabChange,
  openDrawer,
  onClose,
  tabValue,
}) {
  const statusMap = ["PENDING", "REJECTED", "APPROVED"];

  const filteredRequests = requests.filter(
    (req) => req.status === statusMap[tabValue]
  );

  console.log(requests)

  return (
    <Drawer open={openDrawer} onClose={onClose} anchor="right">
      <Col
        className="border border-1 rounded-3 shadow-sm"
        style={{ maxWidth: 530 }}
      >
        <h1 className="display-6 text-center">Books</h1>
        <AppBar position="static" color="transparent" className="mb-4">
          <Tabs
            value={tabValue}
            onChange={handleTabChange}
            indicatorColor="primary"
            textColor="primary"
            variant="fullWidth"
          >
            <Tab icon={<HourglassFullIcon />} aria-label="pending" />
            <Tab icon={<ErrorIcon />} aria-label="rejected" />
            <Tab icon={<DoneIcon />} aria-label="approved" />
          </Tabs>
        </AppBar>
        {filteredRequests.length === 0 ? (
          <div className="px-3">
            <p className="text-center text-muted">
              No requests found with status: {statusMap[tabValue]}
            </p>
          </div>
        ) : (
          filteredRequests.map((request) => (
            <div className="px-3" key={request.id}>
              <div className="d-flex">
                <img
                  src={request.imageUrl || book_cover}
                  className="rounded float-start me-3"
                  style={{ maxWidth: "100px", maxHeight: "150px" }}
                ></img>
                <div className="d-flex flex-column me-3">
                  <span className="text-muted fw-light font-monospace mb-2">
                    {new Date(request.createdAt).toLocaleString()}
                  </span>
                  <h5 className="fw-bold mb-0">{request.title} <span className="fs-6 fw-normal">{"(" + request.genre + ")"}</span></h5>
                  <p className="text-muted ms-2 mb-1">
                    By: {request.author.name} {request.author.surname}
                  </p>
                  <p className="description-text mb-2">{request.description}</p>
                  <p className="fw-bold">{request.price}$</p>
                  {request.status === "REJECTED" && (
                    <div>
                      <p className="mt-0 text-danger fw-bold me-2 mb-0">Reason: </p>
                      <span>{request.reason.replace(/\+/g, ' ').slice(0, -1)}</span>
                    </div>
                  )}
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
              <hr />
            </div>
          ))
        )}
      </Col>
    </Drawer>
  );
}
