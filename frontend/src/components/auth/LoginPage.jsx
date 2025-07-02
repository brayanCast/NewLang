import React, { useState } from "react";
import { useNavigate } from "react-router-dom";
import UserService from "../service/UserService";
import VerifyOtp from "../auth/VerifyOtp";
import ChangePasswordModal from "../auth/ChangePasswordModal";
import { useLoading } from "../context/LoadingContext";
import iconoLang from '../../img/iconolang.png';
import iconoViewPasswordOn from '../../img/visibility_24dp_000739_FILL0_wght400_GRAD0_opsz24.png';
import iconoViewPasswordOff from '../../img/visibility_off_24dp_000739_FILL0_wght400_GRAD0_opsz24 (1).png';

function LoginPage() {
    const [email, setEmail] = useState('');
    const [password, setPassword] = useState('');
    const [error, setError] = useState('');
    const [isModalOpen, setIsModalOpen] = useState(false); // Estado para controlar el modal
    const [otpSent, setOtpSent] = useState(false); //Estado para controlar si el otp es enviado
    const [showPassword, setShowPassword] = useState(false); // Estado para mostrar/ocultar contraseña
    const navigate = useNavigate();

    const { startLoading, stopLoading } = useLoading();

    const handleSubmit = async (e) => {
        e.preventDefault();

        setError(''); // Limpiar errores previos

        startLoading();

        try {
            const responseData = await UserService.login(email, password);
            if (responseData && responseData.token) {
                navigate('/homepage');
            } else {
                setError("Respuesta inesperada del servidor. Intente de nuevo");
                alert("Respuesta inesperada del servidor. Intente de nuevo");
            }

        } catch (error) {
            console.error("Error en login: ", error);

            if (error && error.status) {
                switch(error.status) {
                    case 404:
                        setError("Usuario no encontrado, verifique que no haya sido eliminado");
                        alert("Usuario no encontrado, verifique que no haya sido eliminado");
                        break;
                    case 401:
                        setError("Email o contraseña incorrectos por favor verifique las credenciales");
                        alert("Email o contraseña incorrectos por favor verifique lascredenciales");
                        break;
                    case 500:
                        setError("Error en el servidor. Por favor intente más tarde");
                        alert("Error en el servidor. Por favor intente más tarde");
                        break;
                    default:
                        setError(error.message);
                        alert(error.message);
                        break;
                }   
            } else {
                setError(error.message);
                alert(error.message);
            }

            setTimeout(() => {
                setError('');
            }, 5000); //Limpia los mensaje de error después de 5 segundos
        } finally {
            stopLoading();
        }
    };
    
    const handleRegisterRedirect = () => {
        navigate('/register');
    };

    //Funciones para manejar el modal de Olvidó la contraseña
    const closeChangePasswordModal = () => setIsModalOpen(false);

    return (
        <div className="auth-container">

            <div id="left_page">
                <img id="language_icon" src={iconoLang} alt="Palabra idiomas en inglés" />
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
                            <label htmlFor="password-login" className="password-input">Contraseña</label>
                            <div className="password-input-container">
                                <input
                                    id="password-login"
                                    type={showPassword ? "text" : "password"}
                                    value={password}
                                    onChange={(e) => setPassword(e.target.value)}
                                    placeholder="***********************"
                                    required />
                                <div className="password-icon" onClick={() => setShowPassword(!showPassword)}>
                                    {showPassword ? (<img src={iconoViewPasswordOn} alt="Mostrar contraseña" />) 
                                        : (<img src={iconoViewPasswordOff} alt="Ocultar contraseña" />)}
                                </div>
                            </div>
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

                <ChangePasswordModal
                    isOpen={isModalOpen}
                    onClose={closeChangePasswordModal}
                    initialEmail={email} //pasa el email actual del formato del login al modal
                ></ChangePasswordModal>

            </div>
        </div>
    );
}

export default LoginPage;
