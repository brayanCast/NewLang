import React, { useState, useEffect } from 'react';
import UserService from '../service/UserService';
import { Link } from 'react-router-dom';


function ProfilePage() {
    const [profileInfo, setProfileInfo] = useState({name:  '', email: '', role: ''});

    useEffect(() => {
        fetchProfileInfo();
    }, []);

    const fetchProfileInfo = async () => {
        try {
            const token = localStorage.getItem('token');
            const response = await UserService.getYourProfile(token);
            console.log('Fetching profile information', response);
            const userProfileData = response.users;
            setProfileInfo({
                name: userProfileData.nameUser,
                email: userProfileData.email,
                role: userProfileData.role[0]
            });
            
        } catch (error) {
            console.error('Error fetching profile information: ', error);
            
        }
    };

    return (
        <div className="profile-page-container">
            <h2>Profile Information</h2>
            <p>Name: {profileInfo.name}</p>
            <p>Email: {profileInfo.email}</p>
            {profileInfo.role === "ADMIN" && (
                <button><Link to={`/update-user/${profileInfo.id}`}>Uppdate this profile</Link></button>
            )}
        </div>
    )
}

export default ProfilePage;
