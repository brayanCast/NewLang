import React from "react";
import imgFacebook from './img/icono_facebook.png';
import imgLinkedin from './img/icono_linkedin.png';
import imgInstagram from './img/icono_instagram.png';


const Footer = () => {

    return (
        <footer className="footer">
            <div className="footer-content">
                <p>&copy; 2024 NewLang. Todos los derechos reservados.</p>
                <ul className="social-media-links">
                    <a href="https://es-la.facebook.com/"><img src={imgFacebook} alt="Logo de Facebook" /></a>
                    <a href="https://www.linkedin.com/in/brayan-castillo-figueredo/"><img src={imgLinkedin} alt="Logo de Instagram" /></a>
                    <a href="https://www.instagram.com/"><img src={imgInstagram} alt="Logo de Instagram" /></a>
                </ul>
            </div>
        </footer>
    );
}