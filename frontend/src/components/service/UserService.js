import axios from "axios"; // Mantén axios para las llamadas sin token si lo deseas
import axiosInstance from "../../utils/AxiosInstances"; // Importa la instancia con interceptores

class UserService {
    static BASE_URL = process.env.REACT_APP_API_URL; // Esto ya no es tan necesario si usas axiosInstance.baseURL

    // Métodos sin token (login, register, OTP, updatePassword)
    // Usan 'axios' directamente o una instancia de axios sin interceptor de token
    static async login(email, password) {
        try {
            const response = await axios.post(`${UserService.BASE_URL}/login`, { email, password });
            const { token, role } = response.data;
            UserService.saveData(token, role);
            return response.data;
        } catch (err) {
            throw err;
        }
    }

    static async registerAdmin(userData) {
        try {
            const response = await axios.post(`${UserService.BASE_URL}/register-admin`, userData);
            return response.data;
        } catch (err) {
            throw err;
        }
    }

    static async registerUser(userData) {
        try {
            const response = await axios.post(`${UserService.BASE_URL}/register`, userData);
            return response.data;
        } catch (err) {
            throw err;
        }
    }

    static async sendOtp(email) {
        try {
            const response = await axios.post(`${UserService.BASE_URL}/send-otp`, { email });
            return response.data;
        } catch (err) {
            throw err;
        }
    }

    static async verifyOtp(email, otp) {
        try {
            const response = await axios.post(`${UserService.BASE_URL}/verify-otp`, { email, otp });
            return response.data;
        } catch (err) {
            throw err;
        }
    }

    static async updatePassword(email, password) {
        try {
            const response = await axios.put(`${UserService.BASE_URL}/update-password`, { email, password });
            return response.data;
        } catch (err) {
            throw err;
        }
    }

    // Métodos que requieren token (usan axiosInstance)
    // Ya no necesitas pasar 'token' como argumento ni obtenerlo aquí,
    // el interceptor de 'axiosInstance' lo añade automáticamente.
    static async getAllUsers() { // Removido 'token' del argumento
        try {
            // El token ya se añade automáticamente por el interceptor
            const response = await axiosInstance.get(`/admin/get-all-users`);
            return response.data;
        } catch (err) {
            throw err; // El interceptor ya maneja 401/403
        }
    }

    static async getMyProfile() { // Removido 'token' del argumento
        try {
            const response = await axiosInstance.get(`/auth/get-profile`);
            return response.data;
        } catch (err) {
            throw err;
        }
    }

    static async getUserById(userId) { // Removido 'token' del argumento
        try {
            const response = await axiosInstance.get(`/admin/get-user/${userId}`);
            return response.data;
        } catch (err) {
            throw err;
        }
    }

    static async deleteUser(userId) { // Removido 'token' del argumento
        try {
            const response = await axiosInstance.delete(`/auth/delete/${userId}`);
            return response.data;
        } catch (err) {
            throw err;
        }
    }

    static async updateUser(userId, userData) { // Removido 'token' del argumento
        try {
            const response = await axiosInstance.put(`/admin/update/${userId}`, userData);
            return response.data;
        } catch (err) {
            throw err;
        }
    }

    static async updateMyProfile(userData) { // Removido 'token' del argumento
        try {
            const response = await axiosInstance.put(`/auth/update-profile`, userData);
            return response.data;
        } catch (err) {
            throw err;
        }
    }

    static async deleteMyProfile() { // Removido 'token' del argumento
        try {
            const response = await axiosInstance.delete(`/auth/delete-profile`);
            return response.data;
        } catch (err) {
            console.error("Error eliminando el perfil:", err);
            throw err;
        }
    }

    // AUTHENTICATION CHECKER & Storage
    static async saveData(token, role) {
        localStorage.setItem('token', token);
        localStorage.setItem('role', role);
    }

    static logout() {
        localStorage.removeItem('token');
        localStorage.removeItem('refreshToken'); // Si usas refresh token
        localStorage.removeItem('role');
        // Importante: No redirijas aquí directamente, el interceptor lo hará o tus componentes
        // que llaman a logout (como en Navbar o UpdateUser si el email cambia).
    }

    static getToken() {
        return localStorage.getItem('token');
    }

    static getRole() {
        return localStorage.getItem('role');
    }

    static isAuthenticated() {
        return !!UserService.getToken();
    }

    static isAdmin() {
        return UserService.isAuthenticated() && UserService.getRole() === 'ADMIN';
    }

    static isUser() {
        return UserService.isAuthenticated() && UserService.getRole() === 'USER';
    }
}

export default UserService;