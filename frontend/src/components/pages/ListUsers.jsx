import React, { useState, useEffect, useCallback } from 'react';
import { useNavigate } from 'react-router-dom';
import UserService from '../service/UserService';
import { Eye, Edit, Trash2, Search } from 'lucide-react';
import { useLoading } from '../context/LoadingContext';
import Navbar from '../common/Navbar';
import Footer from '../common/Footer';
import ConfirmModal from '../common/ConfirmModal';
import SuccessModal from '../common/SuccessModal';
import ErrorModal from '../common/ErrorModal';
import '../../styles/ListUsers.css';

function ListUsers() {
    const { startLoading, stopLoading } = useLoading();
    const navigate = useNavigate();
    const [users, setUsers] = useState([]);
    const [filteredUsers, setFilteredUsers] = useState([]);
    const [error, setError] = useState(null);
    const [userToDelete, setUserToDelete] = useState(null);
    const [searchQuery, setSearchQuery] = useState('');
    const [filterRole, setFilterRole] = useState('all'); // 'all', 'ADMIN', 'USER'


    const [showConfirmModal, setShowConfirmModal] = useState(false);
    const [showSuccessModal, setShowSuccessModal] = useState(false);
    const [showErrorModal, setShowErrorModal] = useState(false);
    const [modalMessage, setModalMessage] = useState('');

    const fetchUsers = useCallback(async () => {
        try {
            startLoading();
            const response = await UserService.getAllUsers();

            // El backend retorna un objeto RequestResp con userList
            const usersData = response.userList || [];
            setUsers(usersData);
            setError(null);
        } catch (err) {
            console.error('Error fetching users:', err);
            setError('Error al cargar los usuarios. Por favor, inténtelo más tarde.');
        } finally {
            stopLoading();
        }
    }, [startLoading, stopLoading]);

    const filterUsers = useCallback(() => {
        let filtered = users;

        // Filtrar por rol
        if (filterRole !== 'all') {
            filtered = filtered.filter(user => user.role === filterRole);
        }

        // Filtrar por búsqueda (nombre o email)
        if (searchQuery.trim()) {
            filtered = filtered.filter(user =>
                user.nameUser.toLowerCase().includes(searchQuery.toLowerCase()) ||
                user.email.toLowerCase().includes(searchQuery.toLowerCase())
            );
        }

        setFilteredUsers(filtered);
    }, [users, searchQuery, filterRole]);

    useEffect(() => {
        fetchUsers();
    }, [fetchUsers]);

    useEffect(() => {
        filterUsers();
    }, [filterUsers]);

    const handleView = (user) => {
        alert(`Ver detalles del usuario: ${user.nameUser} (${user.email})`);
        console.log('Ver detalles del usuario:', user);
        // Puedes implementar navegación a una página de detalles si lo deseas
        // navigate(`/user-detail/${user.idUser}`);
    };

    const handleEdit = (user) => {
        startLoading();
        navigate(`/update-user/${user.idUser}`);
        stopLoading();
    };

    const handleDelete = (user) => {
        setUserToDelete(user); // Guardar el usuario
        setModalMessage(`¿Está seguro que desea eliminar al usuario "${user.nameUser}" (${user.email})?`);
        setShowConfirmModal(true);
    }

    const confirmDelete = async () => {
        if (!userToDelete) return;
        setShowConfirmModal(false);

        try {
            startLoading();
            await UserService.deleteUser(userToDelete.idUser);
            await fetchUsers();
            setModalMessage(`Usuario ${userToDelete.nameUser} eliminado exitosamente.`);
            setShowSuccessModal(true);
            setUserToDelete(null); // Limpiar el item

        } catch (err) {
            console.error('Error deleting user:', err);
            alert('Error al eliminar el usuario. Por favor, inténtelo más tarde.');
        } finally {
            stopLoading();
        }
    }

    const cancelDelete = () => {
        setShowConfirmModal(false);
        setUserToDelete(null); // Limpiar el item
    };

    const handleSuccessModalClose = () => {
        setShowSuccessModal(false);
        setTimeout(() => {
            navigate('/list-users');
        }, 1000);
    };


    const getRoleLabel = (role) => {
        return role === 'ADMIN' ? 'Administrador' : 'Usuario';
    };

    if (error) {
        return (
            <div className="error-container">
                <div className="error-card">
                    <div className="error-icon">
                        <svg fill="none" stroke="currentColor" viewBox="0 0 24 24">
                            <path strokeLinecap="round" strokeLinejoin="round" strokeWidth={2} d="M12 9v2m0 4h.01m-6.938 4h13.856c1.54 0 2.502-1.667 1.732-2.5L13.732 4c-.77-.833-1.732-.833-2.5 0L3.232 16.5c-.77.833.192 2.5 1.732 2.5z" />
                        </svg>
                    </div>
                    <h3 className="error-title">Error de carga</h3>
                    <p className="error-message">{error}</p>
                    <button onClick={fetchUsers} className="retry-button">
                        Reintentar
                    </button>
                </div>
            </div>
        );
    }

    return (
        <div className="users-container">
            <Navbar />
            <div className="users-wrapper">
                <div className="users-card">
                    {/* Header */}
                    <div className="table-header">
                        <h1 className="table-title">Gestión de Usuarios</h1>

                        {/* Filtros y búsqueda */}
                        <div className="filters-container">
                            <div className="search-container">
                                <Search className="search-icon" />
                                <input
                                    type="text"
                                    placeholder="Buscar por nombre o correo electrónico..."
                                    value={searchQuery}
                                    onChange={(e) => setSearchQuery(e.target.value)}
                                    className="search-input"
                                />
                            </div>
                            <select
                                value={filterRole}
                                onChange={(e) => setFilterRole(e.target.value)}
                                className="filter-select"
                            >
                                <option value="all">Todos</option>
                                <option value="ADMIN">Administradores</option>
                                <option value="USER">Usuarios</option>
                            </select>
                        </div>

                        {/* Contador */}
                        <div className="results-counter">
                            Mostrando {filteredUsers.length} de {users.length} usuarios
                        </div>
                    </div>

                    {/* Tabla */}
                    <div className="table-container">
                        <table className="data-table">
                            <thead className="table-head">
                                <tr>
                                    <th className="table-header-cell">Nombre de Usuario</th>
                                    <th className="table-header-cell">Correo Electrónico</th>
                                    <th className="table-header-cell">Rol</th>
                                    <th className="table-header-cell table-header-center">Acciones</th>
                                </tr>
                            </thead>
                            <tbody className="table-body">
                                {filteredUsers.length === 0 ? (
                                    <tr>
                                        <td colSpan="4" className="empty-state">
                                            <div className="empty-state-content">
                                                <Search className="empty-state-icon" />
                                                <p className="empty-state-title">No se encontraron resultados</p>
                                                <p className="empty-state-text">Intente ajustar los filtros de búsqueda</p>
                                            </div>
                                        </td>
                                    </tr>
                                ) : (
                                    filteredUsers.map((user) => (
                                        <tr key={user.idUser} className="table-row">
                                            <td className="table-cell table-cell-bold">
                                                {user.nameUser}
                                            </td>
                                            <td className="table-cell table-cell-regular">
                                                {user.email}
                                            </td>
                                            <td className="table-cell">
                                                <span className={`role-badge ${user.role === 'ADMIN' ? 'role-badge-admin' : 'role-badge-user'}`}>
                                                    {getRoleLabel(user.role)}
                                                </span>
                                            </td>
                                            <td className="table-cell table-cell-center">
                                                <div className="actions-container">
                                                    <button
                                                        onClick={() => handleView(user)}
                                                        className="action-button action-button-view"
                                                        title="Ver detalles"
                                                    >
                                                        <Eye className="action-icon" />
                                                    </button>
                                                    <button
                                                        onClick={() => handleEdit(user)}
                                                        className="action-button action-button-edit"
                                                        title="Editar"
                                                    >
                                                        <Edit className="action-icon" />
                                                    </button>
                                                    <button
                                                        onClick={() => handleDelete(user)}
                                                        className="action-button action-button-delete"
                                                        title="Eliminar"
                                                    >
                                                        <Trash2 className="action-icon" />
                                                    </button>
                                                </div>
                                            </td>
                                        </tr>
                                    ))
                                )}
                            </tbody>
                        </table>
                    </div>

                    {/* Footer con información adicional */}
                    {filteredUsers.length > 0 && (
                        <div className="table-footer">
                            <div className="footer-content">
                                <div className="footer-stats">
                                    Administradores: {filteredUsers.filter(user => user.role === 'ADMIN').length} |
                                    Usuarios: {filteredUsers.filter(user => user.role === 'USER').length}
                                </div>
                                <button onClick={fetchUsers} className="refresh-button">
                                    Actualizar datos
                                </button>
                            </div>
                        </div>
                    )}
                </div>
            </div>

            {/* Modales */}
            <ConfirmModal
                isOpen={showConfirmModal}
                onClose={cancelDelete}
                onConfirm={confirmDelete}
                message={modalMessage}
            />

            <SuccessModal
                isOpen={showSuccessModal}
                onClose={handleSuccessModalClose}
                message={modalMessage}
            />

            <ErrorModal
                isOpen={showErrorModal}
                onClose={() => setShowErrorModal(false)}
                message={modalMessage}
            />


            <Footer />
        </div>
    );
};

export default ListUsers;