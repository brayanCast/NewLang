import React, { useState } from "react";
import { useNavigate } from "react-router-dom";
import UserService from "../service/UserService";
import { useLocation } from "react-router-dom";
import { useLoading } from "../context/LoadingContext";
import '../../styles/VerifyOtp.css'; // Import the CSS file for styling

function VerifyOtp() {
    const location = useLocation();

    const email = location.state?.email;

    const [otp, setOtp] = useState('');
    const [error, setError] = useState('');
    const navigate = useNavigate();
    const { startLoading, stopLoading } = useLoading();

    const handleVerifyOtp = async (e) => {
        e.preventDefault();

        setError('');
        startLoading();
        
        if (!email) {
            setError('No se ha proporcionado un correo electrónico válido');
            alert('No se ha proporcionado un correo electrónico válido');
            return;
        }

        try {
            const userData = await UserService.verifyOtp(email, otp);

            if (userData && userData.includes('OTP verificado con exito')) {
                localStorage.setItem('email', email);
                navigate('/update-password', { state: { email: email } });
            }

        } catch (error) {
            console.log(error);
            const errorMessage = error.response?.data?.message || 'Error en la verificación del OTP';
            alert(errorMessage);

            setTimeout(() => {
                setError('');
            }, 5000);

        } finally {
            stopLoading();
        }        
    };

    return (
        <div className="verify-otp-container">
            <form onSubmit={handleVerifyOtp}>
                <h2>Verificar OTP</h2>
                <p>Se ha enviado un OTP a tu correo electrónico. Por favor, ingrésalo a continuación para continuar.</p>
                {error && <p className="error-message">{error}</p>}
                <div className="input-container">
                    <label htmlFor="otp">Email: </label>
                    <input
                        id="email"
                        type="email"
                        value={email}
                        disabled
                        readOnly
                    />
                </div>
                <div className="input-container">
                    <label htmlFor="otp">Ingrese el OTP: </label>
                    <input
                        id="otp"
                        type="text"
                        value={otp}
                        onChange={(e) => setOtp(e.target.value.replace(/\D/g, '').slice(0, 6))}
                        required
                    />
                </div>
                <button className="button" type="submit">Verificar OTP</button>
            </form>
        </div>
    );
}

export default VerifyOtp;