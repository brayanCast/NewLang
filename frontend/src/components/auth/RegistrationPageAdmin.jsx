import React, { useState } from 'react';
import UserService from "../service/UserService";
import { useNavigate } from 'react-router-dom';
import iconoLang from '../../img/iconolang.png';
import { useLoading } from '../context/LoadingContext';
import iconoViewPasswordOn from '../../img/visibility_24dp_000739_FILL0_wght400_GRAD0_opsz24.png';
import iconoViewPasswordOff from '../../img/visibility_off_24dp_000739_FILL0_wght400_GRAD0_opsz24 (1).png';

function RegistrationPageAdmin() {
    const navigate = useNavigate();

    const { startLoading, stopLoading } = useLoading();
    const [showPassword, setShowPassword] = useState(false); // Estado para mostrar/ocultar contraseña

    const [formData, setFormData] = useState({
        nameUser: '',
        email: '',
        password: '',
        idNumber: '',
        role: ['ADMIN']
    });

    const handleInputChange = (e) => {
        const { name, value } = e.target;
        setFormData({
            ...formData,
            [name]: value
        });
    };

    const handleSubmit = async (e) => {
        e.preventDefault();

        startLoading();

        try {
            await UserService.registerAdmin(formData);

            console.log("Datos a enviar: ", formData);


            //Limpia la información del formulario después del registro exitoso

            setFormData({
                nameUser: '',
                email: '',
                password: '',
                idNumber: '',
                role: ''
            });

            alert('User registered successfully');
            navigate('/login');

        } catch (error) {
            console.error('Error registering user', error);
            alert('An error occurred while registering user');

        } finally {
            stopLoading();
        }
    }

    return (

        <div className='auth-container'>

            <div id="left_page">
                <img id="language_icon" src={iconoLang} alt="idiomas" />
                <p id="phrase_app">...Open your mind to the new opportunities</p>
            </div>

            <div id="right_page">

                <form id="form-user-admin" className="registerForm form-content" onSubmit={handleSubmit}>
                    <div className="input_container">
                        <label htmlFor="name-user-admin">Nombre Completo</label>
                        <input id="name-user-admin" type="text" placeholder="John Doe" value={formData.nameUser} name="nameUser"
                            onChange={handleInputChange} required />
                    </div>
                    <div className="input_container">
                        <label htmlFor="id-user-admin">Número de Identificación</label>
                        <input id="id-user-admin" type="number" placeholder="123456789" min-lenght="0" max="9999999999"
                            value={formData.idNumber} name="idNumber"
                            onChange={handleInputChange} required />
                    </div>
                    <div className="input_container">
                        <label htmlFor="email-user-admin">Email</label>
                        <input id="email-user-admin" type="email" placeholder="newexample@newmail.com"
                            value={formData.email} name="email"
                            onChange={handleInputChange} required />
                    </div>

                    <div className="input_container">
                        <label htmlFor="password-user-admin" className="password-input">Contraseña</label>
                        <div className="password-input-container">                        
                            <input 
                            id="password-user-admin" 
                            type={showPassword ? "text" : "password"} placeholder="***********************"
                            value={formData.password} name="password"
                            onChange={handleInputChange} required />
                                <div className="password-icon" onClick={() => setShowPassword(!showPassword)}>
                                    {showPassword ? (<img src={iconoViewPasswordOn} alt="Mostrar contraseña" />) 
                                        : (<img src={iconoViewPasswordOff} alt="Ocultar contraseña" />)}
                                </div>
                        </div>

                    </div>

                    <button className="button" type="submit">Crear</button>

                </form>
            </div>
        </div>

    );

}

export default RegistrationPageAdmin;