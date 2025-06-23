import React, { useState, useEffect } from "react";
import profileIcon from "../../img/perfil.png";
import imgLogo from "../../img/logo_newLang.png"; // Import the logo image
import "../../styles/Navbar.css"; // Import the CSS file for styling
import UserService from "../service/UserService";
import profilePage from "../userspage/ProfilePage";
import { useNavigate } from "react-router-dom";

function Navbar() {
  const navigate = useNavigate();
  const [isAdmin, setIsAdmin] = useState(false); // Assuming UserService has isAdmin state

  const handleModifyUser = () => {
    UserService.getMyProfile();
    navigate("/profile");
  };

  // Function to handle logout
  // This function will be called when the user clicks on the "Cerrar Sesión" link
  const handleLogout = () => {
    const confirmLogout = window.confirm(
      "¿Estás seguro de que deseas cerrar sesión?"
    );
    if (confirmLogout) {
      UserService.logout(); // Call the logout function from UserService
      window.location.href = "/login"; // Redirect to the login page
    }
  };

  useEffect(() => {
    const checkUserRole = async () => {
      try {
        const user = await UserService.getMyProfile();
        if (user && user.users.role === "ADMIN") {
          setIsAdmin(true);
        }
      } catch (error) {
        console.error("Error checking user role:", error);
      }
    };
    checkUserRole();
  }, []);

  return (
    <nav>
      <a href="http://localhost:3000/homepage">
        <img src={imgLogo} id="img-logo" alt="Logo NewLang" />
      </a>
      <ul className="nav-bar">
        <li>
          <a href="http://localhost:3000/homepage">Inicio</a>
        </li>

        {isAdmin && (
        <li>
          <a href="#">Gestionar</a>
          <ul>
            <li>
              <a href="create-routine.html" rel="noopener noreferrer">
                Usuarios
              </a>
            </li>
            <li>
              <a href="modify-routine.html" rel="noopener noreferrer">
                Palabras o Expresiones
              </a>
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
            Modificar Usuario
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
