// import './App.css'
import { Route, Routes } from 'react-router-dom'
import BookingPage from './pages/BookingPage/BookingPage'
import Navbar from './components/Navbar/Navbar';
import BookingConfirmationPage from './pages/BookingConfirmationPage/BookingConfirmationPage';
function App() {

  return (
    <>
      <Navbar />
    <Routes>
      <Route path="/" element={<BookingPage />} />
      <Route path="/confirmation" element={<BookingConfirmationPage />} />
    </Routes>
    </>
  )
}

export default App
