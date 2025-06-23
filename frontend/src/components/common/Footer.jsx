import React from "react";
import imgFacebook from '../../img/icono_facebook.png';
import imgLinkedin from '../../img/icono_linkedin.png';
import imgInstagram from '../../img/icono_instagram.png';
import '../../styles/Footer.css'; // Asegúrate de tener un archivo CSS para estilos


const Footer = () => {

    return (
        <footer className="footer">
            <div className="contact-us">
                <h3>Contact us</h3>
                <a href="#">
                    <p><u>+57 321 765 8957</u></p>
                </a>
                <a href="#">
                    <p><u>newlangapp@gmail.com</u></p>
                </a>
            </div>

            <div className="follow-us">
                <p>&copy; 2024 NewLang. Todos los derechos reservados.</p>
                <ul className="social-media">
                    <a href="https://es-la.facebook.com/"><img src={imgFacebook} alt="Logo de Facebook" /></a>
                    <a href="https://www.linkedin.com/in/brayan-castillo-figueredo/"><img src={imgLinkedin} alt="Logo de Instagram" /></a>
                    <a href="https://www.instagram.com/"><img src={imgInstagram} alt="Logo de Instagram" /></a>
                </ul>
            </div>

            <div className="legal">
                <h3>Legal</h3>
                <a href="#">
                    <p><u>Terms</u></p>
                </a>
                <a href="#">
                    <p><u>Privacy</u></p>
                </a>
            </div>
        </footer >
    );
}

export default Footer;