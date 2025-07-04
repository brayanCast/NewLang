import React, { useState, useEffect, useRef, useCallback } from "react";
import { useNavigate } from "react-router-dom";
import UserService from "../service/UserService";
import ChangePasswordModal from "../auth/ChangePasswordModal";
import { useLoading } from "../context/LoadingContext";
import Navbar from "../common/Navbar";
import Footer from "../common/Footer";
import "../../styles/UpdateUser.css";

function UpdateUser() {
  const navigate = useNavigate();
  const { startLoading, stopLoading } = useLoading();

  const [userData, setUserData] = useState({
    name: "",
    email: "",
    idNumber: "",
    role: "",
  });

  const originalEmailRef = useRef('');
  const [isChangePasswordModalOpen, setChangePasswordModalOpen] = useState(false);

  const fetchUserData = useCallback(async () => {

    startLoading();

    try {

      const response = await UserService.getMyProfile(); //Llama al servicio para obtener los datos del usuario

      if (response.users) {
        const { email, nameUser, idNumber, role } = response.users;
        setUserData({
          email,
          name: nameUser,
          idNumber,
          role,
        });

        originalEmailRef.current = email;

      } else {
        console.error("La respuesta no contiene datos del usuario", response.users);
        alert("Error al cargar los datos del perfil del usuario");
      }

    } catch (error) {
        console.error("Error al obtener la información del usuario:", error);
        alert(`Error al cargar los datos: ${error.message || "Error desconocido"}`);

    } finally {
      stopLoading();
    }
  }, [startLoading, stopLoading]);


  useEffect(() => {
    fetchUserData(); //Pasa el id a fetchUserDataById
  }, [fetchUserData]); //Siempre que hay una cadena de id, lo corre

  const handleInputChange = (e) => {
    const { name, value } = e.target;
    setUserData((prevUserData) => ({
      ...prevUserData,
      [name]: value,
    }));
  };

  const handleSubmit = async (e) => {
    e.preventDefault();

    const confirmUpdate = window.confirm("Estás seguro de que quieres actualizar tu perfil");
    if (!confirmUpdate) {
      return; //no hace nada en caso del que el usuario cancele
    }

    startLoading();

    try {

      const dataToSend = {
        nameUser: userData.name,
        email: userData.email,
        idNumber: userData.idNumber,
      };

      const response = await UserService.updateMyProfile(dataToSend);
      const emailChanged = userData.email !== originalEmailRef.current;

      alert(response.message || "Perfil actualizado correctamente");

      //Condicional para redirigir al login en caso de que el email haya sido actualizado también
      if (emailChanged) {
        alert("Tu email ha sido cambiado, por lo que debes iniciar sesión nuevamente");
        UserService.logout();
        window.location.href = '/login'; //redirección al la página del login para un nuevo ingreso

      } else {
        navigate('/profile')
      }

    } catch (error) {
      console.error("Error actualizando el perfil", error.message);
      alert(`Error al actualizar el perfil: ${error.response?.data?.message || error.message || "Error desconocido"}`);
    } finally {
      stopLoading();
    }
  };

  const handleDeleteProfile = async () => {
    const confirmDelete = window.confirm(
      "ADVERTENCIA: ¿Estás seguro de que quieres eliminar tu perfil? Esta acción no se puede deshacer."
    );

    if (!confirmDelete) {
      return; //si el usuario cancela la eliminación no hace nada
    }
    startLoading();

    try {

      const response = await UserService.deleteMyProfile();

      if (response.statusCode === 200) {
        alert("Perfil eliminado correctamente");
        UserService.logout(); //Llama al método logout para limpiar el localStorage
        window.location.href = '/login';
        return;
      }

    } catch (error) {
      console.error("Error al eliminar el perfil:", error);
      alert(`Error al eliminar el perfil: ${error.message || "Error desconocido"}`);

    } finally {
      stopLoading();
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
              <button type="submit">Actualizar Perfil</button>
            </div>
          </form>
        </div>

        <div className="update-profile-right">

          <div className="change-password">
            <button type="button" onClick={() => setChangePasswordModalOpen(true)}>Cambiar Contraseña</button>
          </div>

          <div className="delete-profile-button">
            <button type="button" onClick={handleDeleteProfile}>Eliminar Perfil</button>
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
