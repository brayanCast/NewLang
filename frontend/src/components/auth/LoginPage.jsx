import React, { useState } from "react";
import { useNavigate } from "react-router-dom";
import UserService from "../service/UserService";

function LoginPage() {
    const [email, setEmail] = useState('');
    const [password, setPassword] = useState('');
    const [newPassword, setNewPassword] = useState('');
    const [confirmPassword, setConfirmPassword] = useState('');
    const [error, setError] = useState('');
    const [isModalOpen, setIsModalOpen] = useState(false); // Estado para controlar el modal

    const navigate = useNavigate();

    const handleSubmit = async (e) => {
        e.preventDefault();
        try {
            const userData = await UserService.login(email, password);

            if (userData.token) {
                localStorage.setItem('token', userData.token);
                localStorage.setItem('role', userData.role);
                navigate('/profile');
            }

        } catch (error) {
            console.log(error);
            setError(error.message || 'Error en el inicio de sesión');
            setTimeout(() => {
                setError('');
            }, 500);
        }
    };

    const handleChangePasswordSubmit = (e) => {
        e.preventDefault();
        // Aquí puedes manejar el cambio de contraseña
        // Por ejemplo, enviar los datos a tu servicio
        console.log("Cambio de contraseña solicitado");
        setIsModalOpen(false); // Cerrar el modal después de enviar

        if (setIsModalOpen(true)) {
            document.getElementById("email-login").required = "false";
            document.getElementById("password-login").required = "false";
        } 
    };

    return (
        <div className="auth-container">
            <div id="left_page">
                <img id="language_icon" src="./img/iconolang.png" alt="idiomas" />
                <p id="phrase_app">...Open your mind to the new opportunities</p>
            </div>

            <div id="right_page">
                <div className="button-selection">
                    <h2 className="selection active-tab" id="signIn">Ingreso de Usuario</h2>
                </div>

                {error && <p className="error-message">{error}</p>}
                <form className="form_login form-content" id="formLogin" onSubmit={handleSubmit}>
                    <div className="input_container">
                        <label htmlFor="email-login">Email</label>
                        <input
                            id="email-login"
                            type="email"
                            value={email}
                            onChange={(e) => setEmail(e.target.value)}
                            placeholder="newexample@newmail.com"
                            required
                        />
                    </div>

                    <div className="input_container">
                        <label htmlFor="password-login">Contraseña</label>
                        <input
                            id="password-login"
                            type="password"
                            value={password}
                            onChange={(e) => setPassword(e.target.value)}
                            placeholder="***********************"
                            required />
                    </div>

                    <div className="forgot_password">
                        <button type="button" onClick={() => setIsModalOpen(true)}>¿Olvidó la Contraseña?</button>
                    </div>

                    <button className="button" type="submit">Ingresar</button>

                    <div id="other_login_form">
                        <p>O puedes ingresar usando:</p>
                        <div className="link_logo">
                            <a href="#"><img src="./img/google_logo.webp" alt="Ingreso por cuenta Google" /></a>
                            <a href="#"><img src="./img/outlook_log.png" alt="Ingreso por cuenta Outlook" /></a>
                            <a href="#"><img src="./img/apple_logo.png" alt="Ingreso por cuenta Apple" /></a>
                        </div>
                    </div>

                    <dialog id="modal-window" open={isModalOpen}>
                        <button id="btn-close-modal" className="btn-cancel-change" onClick={() => setIsModalOpen(false)}>X</button>
                        <div className="title-change-password">
                            <h2>Cambiar la contraseña</h2>
                        </div>
                        <form className="change-password-form form-content" id="password-form" onSubmit={handleChangePasswordSubmit}>
                            <div className="input_container">
                                <label htmlFor="email-change">Email</label>
                                <input id="email-change" type="email" value={email} 
                                onChange={(e) => setEmail(e.target.value)} 
                                placeholder="newexample@newmail.com" />
                            </div>
                            <div className="input_container">
                                <label htmlFor="current-password-change">Contraseña Actual</label>
                                <input id="current-password-change" type="password" 
                                value={password} onChange={(e) => setPassword(e.target.value)} 
                                placeholder="***********************" />
                            </div>
                            <div className="input_container">
                                <label htmlFor="new-password-change">Nueva Contraseña</label>
                                <input id="new-password-change" type="password" 
                                value={newPassword} onChange={(e) => setNewPassword(e.target.value)}
                                placeholder="***********************" />
                            </div>
                            <div className="input_container">
                                <label htmlFor="confirm-password-change">Confirmar Contraseña</label>
                                <input id="confirm-password-change" type="password"  
                                value={confirmPassword} onChange={(e) => setConfirmPassword(e.target.value)}
                                placeholder="***********************" />
                            </div>
                            <button className="button" type="submit">Confirmar</button>
                            
                        </form>
                    </dialog>

                </form>
            </div>
        </div>
    );
}

export default LoginPage;
