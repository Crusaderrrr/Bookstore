import React from "react";
import { Outlet } from "react-router-dom";
import AppNavbar from "./Navbar";

export default function MainLayout() {
  return (
    <>
      <AppNavbar />
      <main>
        <Outlet />
      </main>
    </>
  );
}
