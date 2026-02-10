import { Routes, Route } from "react-router-dom";
import HomePage from "./pages/HomePage";
import Navbar from './components/Navbar';
import ProductDetailsPage from "./pages/ProductDetailsPage";
import './App.css'


function App() {
  return (
    <>
      <Navbar />

      <Routes>
        <Route path="/" element={<HomePage />} />
        <Route path="/product/:id" element={<ProductDetailsPage />} />
      </Routes>
    </>
  );
}

export default App
