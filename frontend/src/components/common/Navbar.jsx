import React, { useState, useEffect, useCallback } from "react";
import UserService from '../service/UserService';
import profilePage from '../userspage/ProfilePage';
import homePage from '../pages/HomePage';
import createWordExpression from '../pages/CreateWordExpression';
import ListWordExpression from '../pages/ListWordExpression';
import ListUsers from "../pages/ListUsers";
import { useNavigate } from 'react-router-dom';
import { useLoading } from '../context/LoadingContext';
import profileIcon from '../../img/perfil.png';
import imgLogo from '../../img/logo_newLang.png'; // Import the logo image
import '../../styles/Navbar.css'; // Import the CSS file for styling

function Navbar() {
  const navigate = useNavigate();
  const [isAdmin, setIsAdmin] = useState(false); // Assuming UserService has isAdmin state
  const { startLoading, stopLoading } = useLoading();

  const handleModifyUser = async () => {
    startLoading();
    navigate("/profile");
    stopLoading();
  };

  // Function to handle logout
  // This function will be called when the user clicks on the "Cerrar Sesión" link
  const handleLogout = () => {
    const confirmLogout = window.confirm("¿Estás seguro de que deseas cerrar sesión?");
    if (confirmLogout) {
      startLoading();

      try {
        UserService.logout(); // Call the logout function from UserService
        window.location.href = "/login"; // Redirect to the login page
      } catch (error) {
        console.error("Error al cerrar sesión: ", error);
        alert("Hubo un error al cerrar la sesión. Por favor, inténtalo de nuevo");
      } finally {
        stopLoading();
      }
    }
  };

  const handleHomePage = () => {
    startLoading();
    navigate("/homepage");
    stopLoading();
  }

  const handleCreateWordExpression = () => {
    startLoading();
    navigate("/create-word-expression");
    stopLoading();
  };

  const handleListWordExpression = () => {
    startLoading();
    navigate("/list-word-expression");
    stopLoading();
  };

  const handleListUsers = () => {
    startLoading();
    navigate("/list-users");
    stopLoading();
  };

  const checkUserRole = useCallback(async () => {
    try {
      const user = await UserService.getMyProfile();
      if (user && user.users.role === "ADMIN") {
        setIsAdmin(true);
      } else {
        setIsAdmin(false);
      }
    } catch (error) {
      console.error("Error checking user role:", error);
    }
  }, []);

  useEffect(() => {
    checkUserRole();
  }, [checkUserRole]);

  return (
    <nav>
      <a href={homePage}
        rel="noopener noreferrer"
        onClick={handleHomePage}>
        <img src={imgLogo} id="img-logo" alt="Logo NewLang" />
      </a>
      <ul className="nav-bar">
        <li>
          <a
            href={homePage}
            rel="noopener noreferrer"
            onClick={handleHomePage}>
            Inicio
          </a>
        </li>

        {isAdmin && (
          <li>
            <a
              href={ListUsers}
              rel="noopener noreferrer"
              onClick={handleListUsers}
            >
              Gestionar
            </a>
            <ul>
              <li>
                <a
                  href={ListUsers}
                  rel="noopener noreferrer"
                  onClick={handleListUsers}
                >
                  Usuarios
                </a>
              </li>
              <li>
                <a 
                    href={ListWordExpression}
                    rel="noopener noreferrer"
                    onClick={handleListWordExpression}
                >
                  Palabras o Expresiones
                </a>
                <ul className="manage-text">

                  <li><a href={createWordExpression}
                    rel="noopener noreferrer"
                    onClick={handleCreateWordExpression}
                  >Crear Palabras/Expresiones</a>
                  </li>

                  <li><a href={ListWordExpression}
                    rel="noopener noreferrer"
                    onClick={handleListWordExpression}
                  >Lista de Palabras/Expresiones</a></li>
                </ul>
              </li>
            </ul>
          </li>
        )}
        <li>
          <a href="#">Rutina</a>
          <ul>
            <li>
              <a href="create-routine.html" rel="noopener noreferrer">
                Crear Rutina
              </a>
            </li>
            <li>
              <a href="modify-routine.html" rel="noopener noreferrer">
                Modificar Rutina
              </a>
            </li>
          </ul>
        </li>

        <li>
          <a href="#">Practicar</a>
          <ul>
            <li>
              <a href="learn-expression.html" rel="noopener noreferrer">
                Expresiones
              </a>
            </li>
            <li>
              <a href="learn-words.html" rel="noopener noreferrer">
                Palabras
              </a>
            </li>
          </ul>
        </li>
        <li>
          <a href="#">Acerca de</a>
        </li>
      </ul>
      <div className="profile-icon">
        <img src={profileIcon} alt="icono de perfil" className="menu-profile" />
        <div className="menu-options">
          <a
            href={profilePage}
            className="menu-option"
            onClick={handleModifyUser}
          >
            Mi Perfil
          </a>
          <a className="menu-option" onClick={handleLogout}>
            Cerrar Sesión
          </a>
        </div>
      </div>
    </nav>
  );
}

export default Navbar;
