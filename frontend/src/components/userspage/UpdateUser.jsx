import React, { useState, useEffect } from "react";
import { useParams, useNavigate } from 'react-router-dom';
import UserService from '../service/UserService';

function UpdateUser() {
    const navigate = useNavigate();
    const { userId } = useParams();

    const [userData, setUserData] = useState({
        name: '',
        email: '',
        role: ''
    });

    useEffect(() => {
        fetchUserDataById(userId); //Pasa el userId a fetchUserDataById
    }, [userId]); //Siempre que hay una cadena de UserId, lo corre

    const fetchUserDataById = async (userId) => {
        try {
            const token = localStorage.getItem('token'); //Obtiene el token del localStorage
            const response = await UserService.getUserById(userId, token); //Obtiene el usuario por ID
            const { email, name, role } = response.users;
            setUserData({ email, name, role });//Extrae los datos del usuario

        } catch (error) {
            console.log('Error fetching user data', error);
        }
    };

    const handleInputChange = (e) => {
        const { name, value } = e.target;
        setUserData((prevUserData) => ({
            ...prevUserData,
            [name]: value
        }));
    };

    const handleSubmit = async (e) => {
        e.preventDefault();

        try {
            const confirmDelete = window.confirm('Are you sure you want to update this user?');
            if (confirmDelete) {
                const token = localStorage.getItem('token');
                const response = await UserService.updateUser(userId, userData, token);
                console.log(response)
                navigate('/admin/user-management'); //Redirige al perfil de la página o muestra un mensaje de éxito
            }

        } catch (error) {
            console.error('Error updating user profile', error);
            alert(error);
        }
    };

    return (
        <div className="auth-container">
            <h2>Update User</h2>
            <form onSubmit={handleSubmit}>
                <div className="form-group">
                    <label>Name:</label>
                    <input type="text" name="name" value={userData.name} onChange={handleInputChange} />
                </div>
                <div className="form-group">
                    <label>Email:</label>
                    <input type="email" name="email" value={userData.email} onChange={handleInputChange} />
                </div>
                <div className="form-group">
                    <label>Role:</label>
                    <input type="text" name="role" value={userData.role} onChange={handleInputChange} />
                </div>
                <div className="form-group">
                    <label>City:</label>
                    <input type="text" name="city" value={userData.city} onChange={handleInputChange} />
                </div>
                <button type="submit">Update</button>
            </form>
        </div>
    );
}

export default UpdateUser;