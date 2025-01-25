import React, { useState } from 'react';
import UserService from "../service/UserService";
import { useNavigate } from 'react-router-dom';

function RegistrationPage() {
    const navigate = useNavigate();

    const [fomrData, setFormData] = useState({
        name: '',
        email: '',
        password: '',
        role: ''
    });

    const handleInputChange = (e) => {
        const { name, value } = e.target;
        setFormData({
            ...fomrData,
            [name]: value
        });
    };

    const handleSubmit = async (e) => {
        e.preventDefault();

        try {
            const token = localStorage.getItem('token');
            await UserService.register(fomrData, token);

            //Limpia la información del formulario después del registro exitoso

            setFormData({
                name: '',
                email: '',
                password: '',
                role: ''
            });

            alert('User registered successfully');
            navigate('/admin/user-management');

        } catch (error) {
            console.error('Error registering user', error);
            alert('An error occurred while registering user');

        }
    }

    return (

        <div className='auth-container'>

            <div id="left_page">
                <img id="language_icon" src="img/iconolang.png" alt="idiomas" />
                <p id="phrase_app">...Open your mind to the new opportunities</p>
            </div>

            <div id="right_page">
                <form id="form-user" className="disappeared" onSubmit={handleSubmit}>
                    <div className="input_container">
                        <label htmlFor="">Nombre Completo</label>
                        <input id="name-user" type="text" placeholder="John Doe" onChange={handleInputChange} required />
                    </div>
                    <div className="input_container">
                        <label htmlFor="email-user">Email</label>
                        <input id="email-user" type="email" placeholder="newexample@newmail.com" onChange={handleInputChange} required />
                    </div>

                    <div className="input_container">
                        <label htmlFor="password-user">Contraseña</label>
                        <input id="password-user" type="password" placeholder="***********************" onChange={handleInputChange} required />
                    </div>

                    <button className="button" type="submit">Crear</button>

                    <div id="other_register_form">
                        <label htmlFor="#">O puedes crear el usuario usando:</label>
                        <div class="link_logo">
                            <a href="#"><img src="./img/google_logo.webp" alt="Ingreso por cuenta Google" /></a>
                            <a href="#"><img src="./img/outlook_log.png" alt="Ingreso por cuenta Outlook" /></a>
                            <a href="#"><img src="./img/apple_logo.png" alt="Ingreso por cuenta Apple" /></a>
                        </div>
                    </div>
                </form>

                <form id="form-user-admin" className="disappeared">
                    <div className="input_container">
                        <label htmlFor="name-user-admin">Nombre Completo</label>
                        <input id="name-user-admin" type="text" placeholder="John Doe" onChange={handleInputChange} required />
                    </div>
                    <div className="input_container">
                        <label htmlFor="id-user-admin">Número de Identificación</label>
                        <input id="id-user-admin" type="number" placeholder="123456789" min-lenght="0" max="9999999999" onChange={handleInputChange} required />
                    </div>
                    <div className="input_container">
                        <label htmlFor="email-user-admin">Email</label>
                        <input id="email-user-admin" type="email" placeholder="newexample@newmail.com" onChange={handleInputChange} required />
                    </div>

                    <div className="input_container">
                        <label htmlFor="password-user-admin">Contraseña</label>
                        <input id="password-user-admin" type="password" placeholder="***********************" onChange={handleInputChange} required />
                    </div>

                    <button className="button" type="submit">Crear</button>

                    <div id="other_register_form">
                        <label htmlFor="#">O puedes crear el usuario usando:</label>
                        <div className="link_logo">
                            <a href="#"><img src="./img/google_logo.webp" alt="Ingreso por cuenta Google" /></a>
                            <a href="#"><img src="./img/outlook_log.png" alt="Ingreso por cuenta Outlook" /></a>
                            <a href="#"><img src="./img/apple_logo.png" alt="Ingreso por cuenta Apple" /></a>
                        </div>
                    </div>
                </form>
            </div>
        </div>

    );

}

export default RegistrationPage;