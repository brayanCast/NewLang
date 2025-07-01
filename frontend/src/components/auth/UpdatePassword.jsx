import React, { useState } from "react";
import { useNavigate } from "react-router-dom";
import UserService from "../service/UserService";
import { useLoading } from "../context/LoadingContext";
import iconoViewPasswordOn from '../../img/visibility_24dp_000739_FILL0_wght400_GRAD0_opsz24.png';
import iconoViewPasswordOff from '../../img/visibility_off_24dp_000739_FILL0_wght400_GRAD0_opsz24 (1).png';
import '../../styles/UpdatePassword.css'; // Asegúrate de tener este archivo CSS para los estilos

function UpdatePassword() {

    const [password, setPassword] = useState('');
    const [confirmPassword, setConfirmPassword] = useState('');
    const [error, setError] = useState('');
    const [showPassword, setShowPassword] = useState(false); // Estado para mostrar/ocultar contraseña
    const [showConfirmPassword, setShowConfirmPassword] = useState(false); // Estado para mostrar/ocultar confirmación de contraseña

    const navigate = useNavigate();

    const { startLoading, stopLoading } = useLoading();

    const handleChangePasswordSubmit = async (e) => {
        e.preventDefault();

        startLoading();

        if (password !== confirmPassword) {
            alert('Las contraseñas no coinciden');
            return;
        }

        const email = localStorage.getItem('email');
        console.log(email);

        try {
            await UserService.updatePassword(email, password);
            alert('Contraseña actualizadacon éxito');
            setTimeout(() => {
                navigate('/login');
            }, 2000);

        } catch (error) {
            setError(error.message || 'Error al actualizar la contraseña');
            alert(error.message || 'Error al actualizar la contraseña');
        } finally {
            stopLoading();
        }
    };

    return (
        <div className="update-password-container">
            {error && <p className="error-message">{error}</p>}
            <form onSubmit={handleChangePasswordSubmit}>
                <h2>Cambiar la contraseña</h2>
                <div className="input-container">
                    <label htmlFor="new-password" className="password-input">Nueva Contraseña</label>
                    <div className="password-input-container">
                        <input
                            id="password"
                            type={showPassword ? "text" : "password"}
                            value={password}
                            onChange={(e) => setPassword(e.target.value)}
                            required
                        />
                        <div className="password-icon" onClick={() => setShowPassword(!showPassword)}>
                            {showPassword ? (<img src={iconoViewPasswordOn} alt="Mostrar contraseña" />)
                                : (<img src={iconoViewPasswordOff} alt="Ocultar contraseña" />)}
                        </div>
                    </div>
                </div>

                <div className="input-container">
                    <label htmlFor="confirm-password">Confirmar Contraseña</label>
                    <div className="password-input-container">
                        <div className="password-input-container">
                            <input id="confirm-password"
                                type={showConfirmPassword ? "text" : "password"}
                                value={confirmPassword}
                                onChange={(e) => setConfirmPassword(e.target.value)}
                                required
                            />
                            <div className="password-icon" onClick={() => setShowConfirmPassword(!showConfirmPassword)}>
                                {showConfirmPassword ? (<img src={iconoViewPasswordOn} alt="Mostrar contraseña" />)
                                    : (<img src={iconoViewPasswordOff} alt="Ocultar contraseña" />)}
                            </div>
                        </div>
                    </div>
                </div>
                <button className="button" type="submit">Actualizar Contraseña</button>
            </form>
        </div>
    );
}

export default UpdatePassword;
