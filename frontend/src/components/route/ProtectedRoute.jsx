import React from 'react';
import { Navigate, Outlet } from 'react-router-dom';
import UserService from '../service/UserService';

const ProtectedRoute = ({ children, adminOnly }) => {
    const isAuthenticated = UserService.isAuthenticated();
    const isAdmin = UserService.isAdmin();

    if (!isAuthenticated) {
        // If the user is not authenticated, redirect to the login page
        return <Navigate to="/login" />;
    }


    if (adminOnly && !isAdmin) {
        // If the route is admin-only and the user is not an admin, redirect to the home page
        return <Navigate to="/homepage" />;
    }

    return children ? children : <Outlet />;

};

export default ProtectedRoute;
