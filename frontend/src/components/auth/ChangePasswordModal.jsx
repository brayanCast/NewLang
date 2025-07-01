import React, { useState } from "react";
import { useNavigate } from "react-router-dom";
import UserService from "../service/UserService";
import { useLoading } from "../context/LoadingContext";

function ChangePasswordModal({ isOpen, onClose, initialEmail = '' }) {
    const [email, setEmail] = useState(initialEmail);
    const [error, setError] = useState('');
    const navigate = useNavigate();
    const { startLoading, stopLoading } = useLoading();

    //Sincroniza el email para que se habrá el modal con el correo del usuario logueado
    React.useEffect(() => {
        setEmail(initialEmail);
    }, [initialEmail]);

    const handleSendOtp = async (e) => {
        e.preventDefault();

        setError('');

        if (!email) {
            setError('Por favor, ingrese un correo electrónico');
            return;
        }

        startLoading();

        try {
            const response = await UserService.sendOtp(email);
            const successMessage = response.message || 'OTP enviado a su correo electrónico';
            alert(successMessage);

            navigate('/verify-otp', { state: { email } }); //envía el email a la página de verrificación de otp
            onClose(); //Una vez enviado el otp, cierra el modal

        } catch (error) {
            console.error("Error al enviar el OTP", error);

            if (error && error.message) {
                alert(`Error: ${error.message}`);
                
            }
        } finally {
            stopLoading();
        }
    };

    if (!isOpen) return null;

    return (
        <dialog id="modal-window" open={isOpen}>
            <button id="btn-close-modal" className="btn-cancel-change" onClick={onClose}>X</button>
            <div className="title-change-password">
                <h2>Cambiar la contraseña</h2>
            </div>
            <form className="change-password-form form-content" id="password-form" onSubmit={handleSendOtp}>
                <div className="input_container">
                    <label htmlFor="email-change">Email</label>
                    <input 
                        id="email-change" 
                        type="email" value={email}
                        onChange={(e) => setEmail(e.target.value)}
                        placeholder="newexample@newmail.com" required/>
                </div>
                {error && <p className="error-message">{error}</p>}
                <button id="change-pass-submit" type="button" onClick={handleSendOtp}>Confirmar</button>
            </form>
        </dialog>
    );

}

export default ChangePasswordModal;
