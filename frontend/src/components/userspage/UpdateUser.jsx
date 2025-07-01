import React, { useState, useEffect } from "react";
import { useNavigate } from "react-router-dom";
import UserService from "../service/UserService";
import ChangePasswordModal from "../auth/ChangePasswordModal";
import { useLoading } from "../context/LoadingContext";
import Navbar from "../common/Navbar";
import Footer from "../common/Footer";
import "../../styles/UpdateUser.css"; //Asegúrate de que la ruta sea correcta

function UpdateUser() {
  const navigate = useNavigate();
  const { startLoading, stopLoading } = useLoading();

  const [userData, setUserData] = useState({
    name: "",
    email: "",
    idNumber: "",
    role: "",
  });

  const [isChangePasswordModalOpen, setChangePasswordModalOpen] = useState(false);

  useEffect(() => {
    fetchUserData(); //Pasa el id a fetchUserDataById
  }, []); //Siempre que hay una cadena de id, lo corre

  const fetchUserData = async () => {

    startLoading();

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
        console.error("La respuesta no contiene datos del usuario", response.users);
        alert("Error al cargar los datos del perfil del usuario");
      }
    } catch (error) {
      console.log("Error al actualizar la información del usuario", error);
    } finally {
      stopLoading();
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

        startLoading();

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
    } finally {
      stopLoading();
    }
  };

  const handleDeleteProfile = async () => {
    const confirmDelete = window.confirm(
      "ADVERTENCIA: ¿Estás seguro de que quieres eliminar tu perfil? Esta acción no se puede deshacer."
    );

    if (confirmDelete) {
      startLoading();

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

        if (response.statusCode === 200) {
          alert("Perfil eliminado correctamente");
          UserService.logout(); //Llama al método logout para limpiar el localStorage
          navigate("/login");
        }

      } catch (error) {
        console.error("Error al eliminar el perfil:", error);
        alert(`Error al eliminar el perfil: ${error.message}`);
      } finally {
        stopLoading();
      }
    }
  };

  return (
    <div className="update-profile">
      <Navbar />

      <div className="update-profile-header">
        <h2>Actualizar Usuario</h2>
      </div>

      <div className="update-profile-container">
        <div className="update-profile-left">
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
          </form>
        </div>

        <div className="update-profile-right">

          <div className="change-password">
            <button type="button" onClick={() => setChangePasswordModalOpen(true)}>Cambiar Contraseña</button>
          </div>

          <div className="delete-profile-button">
            <button type="button" onClick={handleDeleteProfile}>Eliminar</button>
          </div>
        </div>

        <ChangePasswordModal
          isOpen={isChangePasswordModalOpen}
          onClose={() => setChangePasswordModalOpen(false)}
          initialEmail={userData.email} //Pasa el email del perfil actual al modal
        />

      </div>
      <Footer />
    </div >
  );
}

export default UpdateUser;
