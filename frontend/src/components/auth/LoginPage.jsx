import React, { useState } from "react";
import { useNavigate } from "react-router-dom";
import UserService from "../service/UserService";
import VerifyOtp from "./VerifyOtp";

function LoginPage() {
    const [email, setEmail] = useState('');
    const [password, setPassword] = useState('');
    const [newPassword, setNewPassword] = useState('');
    const [confirmPassword, setConfirmPassword] = useState('');
    const [error, setError] = useState('');
    const [isModalOpen, setIsModalOpen] = useState(false); // Estado para controlar el modal
    const [otpSent, setOtpSent] = useState(false);

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

    const handleSendOtp = async () => {
        try {
            await UserService.sendOtp(email);
            setOtpSent(true);
            setError('OTP enviado al correo electrónico');
        } catch (error) {
            console.error('Error enviando el OTP', error);
            alert('Error al enviar el OTP');
        }
    };

    const handleRegisterRedirect = () => {
        navigate('/register');
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

                {!otpSent ? (

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

                        <div id="register-link">
                            <p>¿Aún no tienes una cuenta? <button type="button" onClick={handleRegisterRedirect}>Registrarse</button></p>
                        </div>
                    </form>
                ) : (
                    <VerifyOtp email={email} setOtpSent={setOtpSent} />
                )}

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
                        <button id="change-pass-submit" type="button" onClick={handleSendOtp}>Confirmar</button>
                    </form>
                </dialog>

            </div>
        </div>
    );
}

export default LoginPage;
