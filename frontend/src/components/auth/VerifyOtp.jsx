import React, { useState } from "react";
import { useNavigate } from "react-router-dom";
import UserService from "../service/UserService";

function VerifyOtp({email, setOtpSent}) {
    const [ otp, setOtp ] = useState('');
    const [error, setError] = useState('');
    const navigate = useNavigate();

    const handleVerifyOtp = async (e) => {
        e.PreventDefault();
        try {
            const userData = await UserService.verifyOtp(email, otp);

            if (userData.token) {
                localStorage.setItem('token', userData.token);
                localStorage.setItem('role', userData.role);
                navigate('/profile');
            }

        } catch (error) {
            console.log(error); 
            setError(error.message || 'Error en la verificación del OTP');
            
        }
    };

    return (
        <form onSubmit="handleVerifyOtp">
            {error && <p className="error-message">{error}</p>}
            <div className="input_container">
                <label htmlFor="otp">Ingrese el OTP</label>
                <input
                    id="otp"
                    type="text"
                    value={otp}
                    onChange={(e) => setOtp(e.target.value)}
                    required
                />
            </div>
            <button className="button" type="submit">Verificar OTP</button>
        </form>
    );
}

export default VerifyOtp;   