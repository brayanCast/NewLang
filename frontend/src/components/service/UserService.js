import axios from "axios";

class UserService {
    static BASE_URL = "http://localhost:8080";


    static async login(email, password){
        try {
            const response = await axios.post(`${UserService.BASE_URL}/login`, { email, password })
            const {token, role} = response.data;
            UserService.saveData(token, role);  
            return response.data;

        } catch (err) {
            throw err;
        }
    }

    static async registerAdmin(userData){
        try {
            const response = await axios.post(`${UserService.BASE_URL}/register-admin`, userData)
            return response.data;

        } catch (err) {
            throw err;
        }
    }

    static async registerUser(userData){
        try {

            const response = await axios.post(`${UserService.BASE_URL}/register`, userData)
            return response.data;

        } catch (err) {
            throw err;
        }
    }


    static async sendOtp(email){
        try {
            const response = await axios.post(`${UserService.BASE_URL}/send-otp`, { email })
            return response.data;
            
        } catch (err) {
            throw err;
            
        }
    }

    static async verifyOtp(email, otp){
        try{
            const response = await axios.post(`${UserService.BASE_URL}/verify-otp`, { email, otp })
            return response.data;
        } 
        catch (err){
            throw err;
        }
    }

    static async updatePassword(email, password){
        try{
            const response = await axios.put(`${UserService.BASE_URL}/update-password`, { email, password })
            return response.data;
        } 
        catch (err){
            throw err;
        }
    }

    static async getAllUsers(token){
        try {
            const token = UserService.getToken();
            if (!token) {throw new Error("No se encontró token")}
            const response = await axios.get(`${UserService.BASE_URL}/admin/get-all-users`,
                {
                    headers: {Authorization: `Bearer ${token}`}
        });
            return response.data;

        } catch (err) {
            throw err;
        }
    }

    static async getMyProfile(token){
        try {
            const token = UserService.getToken();
            if (!token) {throw new Error("No se encontró token")}
            const response = await axios.get(`${UserService.BASE_URL}/auth/get-profile`,
                {
                    headers: {Authorization: `Bearer ${token}`}
        });
            return response.data;

        } catch (err) {
            throw err;
        }
    }

    static async getUserById(userId, token){
        try {
            const token = UserService.getToken();
            if (!token) {throw new Error("No se encontró token")}
            const response = await axios.get(`${UserService.BASE_URL}/admin/get-user/${userId}`,
                {
                    headers: {Authorization: `Bearer ${token}`}
        });
            return response.data;

        } catch (err) {
            throw err;
        }
    }

    static async deleteUser(userId, token){
        try {
            const token = UserService.getToken();
            if (!token) {throw new Error("No se encontró token")}
            const response = await axios.delete(`${UserService.BASE_URL}/auth/delete/${userId}`,
                {
                    headers: {Authorization: `Bearer ${token}`}
        });
            return response.data;

        } catch (err) {
            throw err;
        }
    }

    static async updateUser(userId, userData, token){
        try {
            const token = UserService.getToken();
            if (!token) {throw new Error("No se encontró token")}
            const response = await axios.put(`${UserService.BASE_URL}/admin/update/${userId}`, userData,
                {
                    headers: {Authorization: `Bearer ${token}`}
        });
            return response.data;

        } catch (err) {
            throw err;
        }
    }

    static async updateMyProfile(userData){
        try { 
            const token = UserService.getToken();
            if (!token) {
                throw new Error("No se encontró token");
            }
            const response = await axios.put(`${UserService.BASE_URL}/auth/update-profile`, userData, {
                    headers: {Authorization: `Bearer ${token}`}
                });
                    return response.data;

        } catch (err) {
            throw err;
        }
    }

    static async deleteMyProfile(){
        try {
            const token = UserService.getToken();
            if (!token) {throw new Error("No se encontró token")}
            const response = await axios.delete(`${UserService.BASE_URL}/auth/delete-profile`,
                {
                    headers: {Authorization: `Bearer ${token}`}
        });
            return response.data;
        } catch (err) {
            console.error("Error eliminando el perfil:", err);
            throw err;
        }
    }



    //** AUTHENTICATION CHECKER */

    static async saveData(token, role) {
        localStorage.setItem('token', token);
        localStorage.setItem('role', role);
    }

    static logout(){
        localStorage.removeItem('token')
        localStorage.removeItem('role')
    }

    static getToken(){
        return localStorage.getItem('token');
    }

    static getRole(){
        return localStorage.getItem('role');
    }

    static isAuthenticated(){
        return !!UserService.getToken();
    }

    static isAdmin(){
        return UserService.isAuthenticated && UserService.getRole() === 'ADMIN';   
    }

    static isUser(){
        return UserService.isAuthenticated && UserService.getRole() === 'USER';
    }
}

export default UserService;