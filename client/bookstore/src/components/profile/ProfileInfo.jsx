import React, { useContext, useState } from "react";
import { Alert, Button, Col, Form, InputGroup, Row } from "react-bootstrap";
import default_profile_image from "../../assets/default_profile_image.jpg";
import "../../style/authorForm.css";
import axiosInstance from "../../config/axiosConfig";
import { Typography, Grid, Avatar, Menu, MenuItem, Badge } from "@mui/material";
import { default as MuiButton } from "@mui/material/Button";
import KeyboardArrowDownIcon from "@mui/icons-material/KeyboardArrowDown";
import LogoutIcon from "@mui/icons-material/Logout";
import FileUploadIcon from "@mui/icons-material/FileUpload";
import VerifiedIcon from "@mui/icons-material/Verified";
import MessageIcon from "@mui/icons-material/Message";
import CloseIcon from "@mui/icons-material/Close";
import IconButton from "@mui/material/IconButton";
import { AppContext } from "../../context/AppContext";

export default function ProfileInfo({
  username,
  email,
  onLogout,
  image,
  onImageChange,
  isAuthor,
  toggleDrawer,
  anchorEl,
  setAnchorEl,
  setIsAuthor,
}) {
  const { theme } = useContext(AppContext);
  const [becomeAuthor, setBecomeAuthor] = useState(false);
  const [name, setName] = React.useState("");
  const [surname, setSurname] = React.useState("");
  const [bio, setBio] = React.useState("");
  const [pseudonym, setPseudonym] = React.useState("");
  const [alertMessage, setAlertMessage] = useState("");
  const [alertType, setAlertType] = useState("");
  const open = Boolean(anchorEl);

  const handleMenuClick = (event) => {
    setAnchorEl(event.currentTarget);
  };

  const handleMenuClose = () => {
    setAnchorEl(null);
  };

  const handleSubmitAuthor = async (event) => {
    event.preventDefault();

    const authorData = {
      name,
      surname,
      bio,
      pseudonym,
    };

    try {
      const response = await axiosInstance.post("/authors/new", authorData);
      if (response.status === 200) {
        setAlertType("success");
        setAlertMessage("You are an author now");
        setIsAuthor(true);
      }
    } catch (err) {
      setAlertMessage(err.response.data.message);
      setAlertType("danger");
      console.error(err);
    }
  };

  const handleBecomeAuthor = () => {
    setBecomeAuthor(!becomeAuthor);
    handleMenuClose();
  };

  return (
    <Row className="justify-content-center">
      <Col className="mx-auto">
        <input
          type="file"
          accept="image/*"
          style={{ display: "none" }}
          id="imageUpload"
          onChange={onImageChange}
        />
        {becomeAuthor && (
          <div className="background-blur">
            <div
              className={`form-center bg-${
                theme === "dark" ? "dark" : "white"
              }`}
            >
              <div className="d-flex align-items-center">
                {alertMessage && (
                  <Alert className="ms-auto" variant={alertType}>
                    {alertMessage}
                  </Alert>
                )}
                <IconButton
                  variant="outline"
                  size="small"
                  className="ms-auto"
                  onClick={() => setBecomeAuthor(false)}
                >
                  <CloseIcon />
                </IconButton>
              </div>
              <Form onSubmit={handleSubmitAuthor}>
                <Form.Group className="mb-3" controlId="formBasicEmail">
                  <Form.Label>Name</Form.Label>
                  <Form.Control
                    type="text"
                    value={name}
                    onChange={(e) => setName(e.target.value)}
                  />
                </Form.Group>
                <Form.Group className="mb-3" controlId="formBasicPassword">
                  <Form.Label>Surname</Form.Label>
                  <Form.Control
                    type="text"
                    value={surname}
                    onChange={(e) => setSurname(e.target.value)}
                  />
                </Form.Group>
                <Form.Group className="mb-3" controlId="formBasicPassword">
                  <Form.Label>Enter your Bio</Form.Label>
                  <Form.Control
                    type="text"
                    value={bio}
                    onChange={(e) => setBio(e.target.value)}
                  />
                </Form.Group>
                <Form.Group className="mb-3" controlId="formBasicPassword">
                  <Form.Label>Pseudonym</Form.Label>
                  <Form.Control
                    type="text"
                    value={pseudonym}
                    onChange={(e) => setPseudonym(e.target.value)}
                  />
                </Form.Group>

                <Button
                  variant="primary"
                  type="submit"
                  className="d-flex mx-auto"
                >
                  Submit
                </Button>
              </Form>
            </div>
          </div>
        )}

        <>
          <Grid
            container
            spacing={2}
            alignItems="center"
            direction={{ xs: "column", md: "row" }}
            sx={{
              p: 2,
              borderRadius: 2,
              boxShadow: 2,
            }}
          >
            <Grid item>
              <Avatar
                alt={username}
                src={image || default_profile_image}
                sx={{ width: 80, height: 80, marginRight: "auto" }}
              />
            </Grid>

            <Grid item>
              <Typography
                variant="h6"
                sx={{ textAlign: { xs: "center", md: "left" } }}
              >
                {username}{" "}
                {isAuthor && (
                  <VerifiedIcon
                    className="ms-0 mb-1"
                    sx={{
                      fontSize: 16,
                      color: "#1e88e5",
                    }}
                  />
                )}
              </Typography>
              <Typography variant="body2">{email}</Typography>
            </Grid>

            <Grid
              item
              sx={{
                mx: { xs: "auto", sm: "auto", md: "auto", lg: "initial" },
                ml: { lg: "auto" },
              }}
            >
              <MuiButton
                variant="outlined"
                onClick={handleMenuClick}
                endIcon={<KeyboardArrowDownIcon />}
              >
                Options
              </MuiButton>
              <Menu
                anchorEl={anchorEl}
                open={open}
                onClose={handleMenuClose}
                anchorOrigin={{ vertical: "bottom", horizontal: "left" }}
                transformOrigin={{ vertical: "top", horizontal: "left" }}
              >
                <MenuItem onClick={onLogout}>
                  Logout {<LogoutIcon className="ms-2" fontSize="small" />}
                </MenuItem>
                <MenuItem
                  onClick={() => document.getElementById("imageUpload").click()}
                >
                  Image {<FileUploadIcon className="ms-2" fontSize="small" />}
                </MenuItem>
                {isAuthor && (
                  <MenuItem onClick={() => toggleDrawer(true)}>
                    Requests {<MessageIcon className="ms-2" fontSize="small" />}
                  </MenuItem>
                )}
                {!isAuthor && (
                  <MenuItem onClick={handleBecomeAuthor}>
                    Author {<VerifiedIcon className="ms-2" fontSize="small" />}
                  </MenuItem>
                )}
              </Menu>
            </Grid>
          </Grid>
        </>
      </Col>
    </Row>
  );
}
