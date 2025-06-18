import React, { useState } from 'react';
import UserService from "../service/UserService";
import { useNavigate } from 'react-router-dom';
import { HttpStatusCode } from 'axios';
import iconoLang from '../../img/iconolang.png';
import { useLoading } from '../context/LoadingContext';
import iconoViewPasswordOn from '../../img/visibility_24dp_000739_FILL0_wght400_GRAD0_opsz24.png';
import iconoViewPasswordOff from '../../img/visibility_off_24dp_000739_FILL0_wght400_GRAD0_opsz24 (1).png';

function RegistrationPageUser() {
    const navigate = useNavigate();
    const [showPassword, setShowPassword] = useState(false); // Estado para mostrar/ocultar contraseña
    const { startLoading, stopLoading } = useLoading();

    const [formData, setFormData] = useState({
        nameUser: '',
        email: '',
        password: '',
        role: ['USER']
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
            //Llama al servicio para registrar el usuario
            await UserService.registerUser(formData);
            console.log("Datos a enviar: ", formData);

            //Limpia la información del formulario después del registro exitoso

            setFormData({
                nameUser: '',
                email: '',
                password: '',
                role: ''
            });

            alert('User registered successfully');
            navigate('/login');

        }
        catch (error) {

            if (HttpStatusCode.BadRequest) {
                console.error('Email already exist', error);
                alert('Ya existe un usuario con ese email');

            } else {
                console.error('Error registering user', error);
                alert('An error occurred while registering user');
            }
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
                <form id="form-user" className="registerForm form-content" onSubmit={handleSubmit}>
                    <div className="input_container">
                        <label htmlFor="name-user">Nombre Completo</label>
                        <input id="name-user" type="text" placeholder="John Doe"
                            value={formData.nameUser} name="nameUser"
                            onChange={handleInputChange} required />
                    </div>
                    <div className="input_container">
                        <label htmlFor="email-user">Email</label>
                        <input id="email-user" type="email" placeholder="newexample@newmail.com"
                            value={formData.email} name="email"
                            onChange={handleInputChange} required />
                    </div>

                    <div className="input_container">
                        <label htmlFor="password-user">Contraseña</label>
                        <div className="password-input-container">
                            <input id="password-user" type={showPassword ? "text" : "password"} placeholder="***********************"
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

export default RegistrationPageUser;