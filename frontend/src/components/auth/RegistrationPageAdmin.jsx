import React, { useState } from 'react';
import UserService from "../service/UserService";
import { useNavigate } from 'react-router-dom';
import iconoLang from '../../img/iconolang.png';

function RegistrationPageAdmin() {
    const navigate = useNavigate();

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
                        <label htmlFor="password-user-admin">Contraseña</label>
                        <input id="password-user-admin" type="password" placeholder="***********************" 
                        value={formData.password} name="password"
                        onChange={handleInputChange} required />
                    </div>

                    <button className="button" type="submit">Crear</button>

                </form>
            </div>
        </div>

    );

}

export default RegistrationPageAdmin;