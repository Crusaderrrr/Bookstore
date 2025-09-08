import { BrowserRouter, Routes, Route } from "react-router-dom";
import HomePage from "./pages/HomePage";
import "bootstrap/dist/css/bootstrap.min.css";
import { ThemeProvider } from "./context/ThemeContext";
import ShopPage from "./pages/ShopPage";
import LoginPage from "./pages/LoginPage";
import MainLayout from "./components/MainLayout";
import AuthLayout from "./components/AuthLayout";
import SignupPage from "./pages/SignupPage";
import CartPage from "./pages/CartPage";

function App() {
  return (
    <ThemeProvider>
      <BrowserRouter>
        <Routes>
          <Route path="/login" element={<AuthLayout />}></Route>
            <Route index element={<LoginPage />} />
            <Route path="/signup" element={<SignupPage />} />
          <Route path="/" element={<MainLayout />}>
            <Route path="home" element={<HomePage/>}></Route>
            <Route path="shop" element={<ShopPage />} />
            <Route path="cart" element={<CartPage/>} />
          </Route>
        </Routes>
      </BrowserRouter>
    </ThemeProvider>
  );
}

export default App;
