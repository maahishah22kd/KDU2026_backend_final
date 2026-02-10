import { StrictMode } from 'react'
import { createRoot } from 'react-dom/client'
// import './index.css'
import App from './App.tsx'
import { BrowserRouter } from "react-router-dom";
import "./styles/styles.scss";
import { ProductProvider } from "./context/ProductContext";

createRoot(document.getElementById('root')!).render(
  <StrictMode>
    <ProductProvider>
      <BrowserRouter>
        <App />
      </BrowserRouter>
    </ProductProvider>
  </StrictMode>,
)
