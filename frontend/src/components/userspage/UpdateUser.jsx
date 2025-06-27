import React, { useState, useEffect } from "react";
import { useNavigate } from "react-router-dom";
import UserService from "../service/UserService";
import Navbar from "../common/Navbar";
import Footer from "../common/Footer";
import "../../styles/UpdateUser.css"; //Asegúrate de que la ruta sea correcta

function UpdateUser() {
  const navigate = useNavigate();

  const [userData, setUserData] = useState({
    name: "",
    email: "",
    idNumber: "",
    role: "",
  });

  useEffect(() => {
    fetchUserData(); //Pasa el id a fetchUserDataById
  }, []); //Siempre que hay una cadena de id, lo corre

  const fetchUserData = async () => {
    try {
      const token = localStorage.getItem("token"); //Obtiene el token del localStorage
        if (!token) {
          throw new Error(
            "No se encontró token, debes iniciar sesión nuevamente"
          );
          navigate("/login"); //Redirige al usuario a la página de inicio de sesión
          return;
        }

      const response = await UserService.getMyProfile(); //Llama al servicio para obtener los datos del usuario
      if (response.users) {
        const { email, nameUser, idNumber, role } = response.users;
        setUserData({
          email,
          name: nameUser,
          idNumber,
          role,
        });
      } else {
        console.error(
          "La respuesta no contiene datos del usuario",
          response.users
        );
        alert("Error al cargar los datos del perfil del usuario");
      }
    } catch (error) {
      console.log("Error al actualizar la información del usuario", error);
    }
  };

  const handleInputChange = (e) => {
    const { name, value } = e.target;
    setUserData((prevUserData) => ({
      ...prevUserData,
      [name]: value,
    }));
  };

  const handleSubmit = async (e) => {
    e.preventDefault();

    try {
      const confirmUpdate = window.confirm(
        "Estás seguro de que quieres actualizar tu perfil"
      );
      if (confirmUpdate) {
        const token = localStorage.getItem("token");
        if (!token) {
          console.error(
            "No se encontró token, debes iniciar sesión nuevamente"
          );
          navigate("/login");
          return;
        }

        const dataToSend = {
          nameUser: userData.name,
          email: userData.email,
          idNumber: userData.idNumber,
        };

        const response = await UserService.updateMyProfile(dataToSend);
        alert("Perfil actualizado correctamente");
        console.log(response);
        navigate("/profile"); //Redirige al perfil de la página o muestra un mensaje de éxito
      }
    } catch (error) {
      console.error("Error actualizando el perfil", error);
      alert(error);
    }
  };

  const handleDeleteProfile = async () => {
    const confirmDelete = window.confirm(
      "ADVERTENCIA: ¿Estás seguro de que quieres eliminar tu perfil? Esta acción no se puede deshacer."
    );

    if (confirmDelete) {
      try {
        const token = localStorage.getItem("token");
        if (!token) {
          console.error(
            "No se encontró token, debes iniciar sesión nuevamente para eliminar el perfil"
          );
          navigate("/login");
          return;
        }
        const response = await UserService.deleteMyProfile();

        if (response.success) {
          alert("Perfil eliminado correctamente");
          UserService.logout(); //Llama al método logout para limpiar el localStorage
          navigate("/login");
        } else {
          alert("Error al eliminar el perfil");
        }
      } catch (error) {
        console.error("Error al eliminar el perfil:", error);
        alert(`Error al eliminar el perfil: ${error.message}`);
      }
    }
  };

  return (
    <div className="update-profile">
      <div className="update-profile-container">
        <Navbar />
        <h2>Update User</h2>
        <form onSubmit={handleSubmit}>
          <div className="form-group">
            <label>Nombre del Usuario:</label>
            <input
              type="text"
              name="name"
              value={userData.name}
              onChange={handleInputChange}
            />
          </div>

          <div className="form-group">
            <label>Email:</label>
            <input
              type="email"
              name="email"
              value={userData.email}
              onChange={handleInputChange}
            />
          </div>

          {userData.role === "ADMIN" && (
            <div className="form-group">
              <label>Número de Identificación:</label>
              <input
                type="text"
                name="idNumber"
                value={userData.idNumber}
                onChange={handleInputChange}
              />
            </div>
          )}

          <div className="form-group">
            <label>Rol:</label>
            <input type="text" name="role" value={userData.role} readOnly />
          </div>

          <div className="update-profile-button">
            <button type="submit">Actualizar</button>
          </div>
          <div className="delete-profile-button">
            <button type="button" onClick={handleDeleteProfile}>Eliminar</button>
          </div>
        </form>
        <Footer />
      </div>
    </div>
  );
}

export default UpdateUser;
