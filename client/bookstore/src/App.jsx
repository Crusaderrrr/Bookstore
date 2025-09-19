import { BrowserRouter, Routes, Route } from "react-router-dom";
import HomePage from "./pages/HomePage";
import "bootstrap/dist/css/bootstrap.min.css";
import { ContextProvider } from "./context/AppContext";
import ShopPage from "./pages/ShopPage";
import LoginPage from "./pages/LoginPage";
import MainLayout from "./components/MainLayout";
import AuthLayout from "./components/AuthLayout";
import SignupPage from "./pages/SignupPage";
import CartPage from "./pages/CartPage";
import UserProfilePage from "./pages/UserProfilePage";
import AdminPage from "./pages/AdminPage";

function App() {
  return (
    <ContextProvider>
      <BrowserRouter>
        <Routes>
          <Route element={<AuthLayout />}></Route>
            <Route path="/login" element={<LoginPage />} />
            <Route path="/signup" element={<SignupPage />} />
          <Route path="/" element={<MainLayout />}>
            <Route index element={<HomePage/>}></Route>
            <Route path="shop" element={<ShopPage />} />
            <Route path="cart" element={<CartPage/>} />
            <Route path="profile" element={<UserProfilePage/>}/>
            <Route path="/admin" element={<AdminPage/>}/>
          </Route>
        </Routes>
      </BrowserRouter>
    </ContextProvider>
  );
}

export default App;