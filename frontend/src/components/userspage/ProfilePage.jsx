import React, { useState, useEffect } from "react";
import { Link } from "react-router-dom";
import UserService from "../service/UserService";
import Navbar from "../common/Navbar";
import Footer from "../common/Footer";
import "../../styles/ProfilePage.css"; // Import the CSS file for styling
import myProfileIcon from "../../img/myprofile_icon-removebg-preview.png"; // Import the profile icon
function ProfilePage() {
  const [profileInfo, setProfileInfo] = useState({
    name: "",
    email: "",
    role: "",
    idNumber: "",
  });

  useEffect(() => {
    fetchProfileInfo();
  }, []);

  const fetchProfileInfo = async () => {
    try {
      const token = localStorage.getItem("token");
      const response = await UserService.getMyProfile(token);
      const userProfileData = response.users;
      setProfileInfo({
        name: userProfileData.nameUser,
        email: userProfileData.email,
        role: userProfileData.role,
        idNumber: userProfileData.idNumber,
      });
    } catch (error) {
      console.error("Error fetching profile information: ", error);
    }
  };

  return (
    <div className="profile-page">
      <Navbar />
      <div className="profile-page-content">
        <div className="profile-header">
          <h2>Mi perfil</h2>
          <img
            src={myProfileIcon}
            alt="Profile Icon"
            className="profile-icon"
          />
        </div>
        <div className="profile-info">
          <div className="profile-left">
            <p>Nombre de usuario: </p>
            {profileInfo.role === "ADMIN" && <p>Número de Identificación: </p>}
            <p>Correo Email Registrado: </p>
            <p>Rol: </p>
          </div>
          <div className="profile-right">
            <p>{profileInfo.name}</p>
            {profileInfo.role === "ADMIN" && <p>{profileInfo.idNumber}</p>}
            <p>{profileInfo.email}</p>
            <p>{profileInfo.role}</p>
          </div>
        </div>
        <div className="profile-footer">
          <button>
            <Link to={`/update-user/${profileInfo.id}`}>Actualizar perfil</Link>
          </button>
        </div>
      </div>
      <Footer />
    </div>
  );
}

export default ProfilePage;
