import React from 'react';
import { BrowserRouter, Routes, Route, Navigate } from 'react-router-dom';
import LoginPage from './components/auth/LoginPage';
import RegistrationPageUser from './components/auth/RegistrationPageUser';
import RegistrationPageAdmin from './components/auth/RegistrationPageAdmin';
import UpdateUser from './components/userspage/UpdateUser';
import UserManagementPage from './components/userspage/UserManagementPage';
import ProfilePage from './components/userspage/ProfilePage';
import VerifyOtp from './components/auth/VerifyOtp';
import UpdatePassword from './components/auth/UpdatePassword';
import HomePage from './components/pages/HomePage';
import ProtectedRoute from './components/route/ProtectedRoute';
import { LoadingProvider } from './components/context/LoadingContext';


function App() {
  return (
    <BrowserRouter>
      <LoadingProvider>
        <div className="App">
          <div className="content">
            <Routes>
              {/* Rutas Públicas */}
              <Route exact path="/" element={<LoginPage />} />
              <Route exact path="/login" element={<LoginPage />} />
              <Route path="/register" element={<RegistrationPageUser />} />
              <Route path="/register-admin" element={<RegistrationPageAdmin />} />
              <Route path="/verify-otp" element={<VerifyOtp />} />
              <Route path="/update-password" element={<UpdatePassword />} />

              {/* Rutas Protegidas Generales */}
              <Route element={<ProtectedRoute />}>
                <Route path="/homepage" element={<HomePage />} />
                <Route path="/profile" element={<ProfilePage />} />
                {/* Si UpdateUser siempre carga el perfil del usuario logueado,
                    mantén la ruta sin ':userId' aquí y en ProfilePage el Link. */}
                <Route path="/update-user" element={<UpdateUser />} />
              </Route>

              {/* Rutas Protegidas para Administradores */}
              <Route element={<ProtectedRoute adminOnly={true} />}>
                <Route path="/user-management" element={<UserManagementPage />} />
              </Route>

              {/* Ruta Catch-all */}
              <Route path="*" element={<Navigate to="/login" replace />} />
            </Routes>
          </div>
        </div>
      </LoadingProvider>
    </BrowserRouter>
  );
}

export default App;