import React from 'react';
import { Link } from 'react-router-dom';
import profileIcon from './img/perfil.png';
import imgLogo from './img/logo_newLang.png'; // Import the logo image
import './Navbar.css'; // Import the CSS file for styling
import UserService from '../service/UserService';


function Navbar() {

    const isAuthenticated = UserService.isAuthenticated();
    const isAdmin = UserService.isAdmin();


    const handleLogout = () => {
        const confirmLogout = window.confirm("¿Estás seguro de que deseas cerrar sesión?");
        if (confirmLogout) {
            UserService.logout(); // Call the logout function from UserService
            window.location.href = '/login'; // Redirect to the login page
        }
    }

    return (
        <nav className="navbar">
            <input type="checkbox" id="menu-toggle" className="menu-toggle" />
            <label htmlFor="check" className='checkbtn'>
                <i className="fa-solid fa-bars"></i>
            </label>
            <a href="/homepage.html"><img src={imgLogo} id="img-logo" alt="Logo NewLang" /></a>
            <ul class="nav-bar">
                <li><a href="./homepage.html">Inicio</a></li>
                <li><a href="#">Rutina</a>
                    <ul>
                        <li><a href="create-routine.html" rel="noopener noreferrer">Crear Rutina</a></li>
                        <li><a href="modify-routine.html" rel="noopener noreferrer">Modificar Rutina</a></li>
                    </ul>
                </li>
                <li><a href="#">Practicar</a>
                    <ul>
                        <li><a href="learn-expression.html" rel="noopener noreferrer">Expresiones</a></li>
                        <li><a href="learn-words.html" rel="noopener noreferrer">Palabras</a></li>
                    </ul>
                </li>
                <li><a href="#">Acerca de</a></li>
            </ul>
            <div class="profile-icon">
                <img src={profileIcon} alt="icono de perfil" class="menu-profile" />
                <div class="menu-options">
                    <a href="modify-user.html" class="menu-option">Modificar Usuario</a>
                    <a href="#" class="menu-option">Cerrar Sesión</a>
                </div>
            </div>

        </nav>


    );
}

export default Navbar;