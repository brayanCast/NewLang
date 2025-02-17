    import React, { useState } from "react";
    import { useNavigate } from "react-router-dom";
    import UserService from "../service/UserService";

    function UpdatePassword() {

        const [password, setPassword] = useState('');
        const [confirmPassword, setConfirmPassword] = useState('');
        const [error, setError] = useState('');

        const navigate = useNavigate();

        const handleChangePasswordSubmit = async (e) => {
            e.preventDefault();

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
            }
        };

        return (
            <div className="update-password-container">
                <h2>Cambiar la contraseña</h2>
                {error && <p className="error-message">{error}</p>}
                <form onSubmit={handleChangePasswordSubmit}>
                    <div className="input-container">
                        <label htmlFor="new-password">Nueva Contraseña</label>
                        <input id="password" 
                            type="password" 
                            value={password} 
                            onChange={(e) => setPassword(e.target.value)} 
                            required
                        />
                    </div>

                    <div className="input-container">
                        <label htmlFor="confirm-password">Confirmar Contraseña</label>
                        <input id="confirm-password" 
                            type="password" 
                            value={confirmPassword} 
                            onChange={(e) => setConfirmPassword(e.target.value)} 
                            required
                        />
                    </div>

                    <button className="button" type="submit">Actualizar Contraseña</button>
                </form>
            </div>
        );
    }

    export default UpdatePassword;
