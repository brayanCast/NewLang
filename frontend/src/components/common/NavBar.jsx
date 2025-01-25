import React from "react";
import { Link } from "react-router-dom";
import UserService from "../service/UserService";

function NavBar() {
    const isAutehnticated = UserService.isAuthenticated();
    const isAdmin = UserService.isAdmin();

    const handleLogout = () => {
        const confirmLogout = window.confirm('Are you sure you want to logout this user?');
        if (confirmLogout) {
            UserService.logout();  
        }
    };
    
    return (

        <nav>
            {isAutehnticated && <li><Link to="/">Phegon Dev</Link></li>}
            {isAutehnticated && <li><Link to="/profile">Profile</Link></li>}
            {isAdmin && <li><Link to="/admin/user-management">User Management</Link></li>}
            {isAutehnticated && <li><Link to="/" onClick={handleLogout}>Logout</Link></li>}
        </nav>
    );
}

export default NavBar;