import './App.css'
import { Route, Routes } from 'react-router-dom'
import RegistrationPage from './pages/RegistrationPage/RegistrationPage'
import RegistrationConfirmationPage from './pages/RegistrationConfirmationPage/RegistrationConfirmationPage'
function App() {

  return (
  
     <Routes>
      <Route path="/" element={<RegistrationPage />} />
      <Route path="/confirmation" element={<RegistrationConfirmationPage />} />
    </Routes>
    
  )
}

export default App
