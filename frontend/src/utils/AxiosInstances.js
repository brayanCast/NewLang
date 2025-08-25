import axios from 'axios';
import UserService from '../components/service/UserService';

const axiosInstance = axios.create({
    //baseURL: process.env.REACT_APP_API_URL, //URL base PARA DESPLIEGUE Y CONEXION CON EL BACKEND
    baseURL: "http://localhost:8080", //URL base PARA DESARROLLO
});


//Interceptor de la solicitud: añade el token de cada request que va saliendo.
axiosInstance.interceptors.request.use(
    (config) => {
        const token = UserService.getToken(); //Trae u obtiene el token
        if(token) {
            config.headers.Authorization = `Bearer ${token}`; //Añade el token al encabezado
        }
        return config;
    },
    (error) => {
        return Promise.reject(error);
    }
);

//Interceptor de respuesta con el que se manejan los errores para la autenticación a nivel global
axiosInstance.interceptors.response.use(
    (response) => response, // Si la respuesta es exitosa, simplemente la retorna
    (error) => {
        const originalRequest = error.config;

        // Si es un error 401 (No autorizado) o 403 (Prohibido)
        // Y no es una solicitud que ya estábamos intentando refrescar (para evitar bucles)
        // Y la URL no es de autenticación (login, register, etc.)
        if (
            error.response &&
            (error.response.status === 401 || error.response.status === 403) &&
            !originalRequest._retry &&
            !originalRequest.url.includes('/login') && // No redirigir si es el propio login fallido
            !originalRequest.url.includes('/register') &&
            !originalRequest.url.includes('/send-otp') &&
            !originalRequest.url.includes('/verify-otp') &&
            !originalRequest.url.includes('/update-password')
        ) {
            originalRequest._retry = true;

            // Llama a logout para limpiar el token
            // Nota: Aquí no podemos llamar a UserService.logout directamente sin un import circular.
            // Es mejor tener la lógica de logout aquí o que UserService.logout solo borre del localStorage
            // y el interceptor maneje la redirección.
            localStorage.removeItem('token');
            localStorage.removeItem('refreshToken'); // Si usas refresh token
            localStorage.removeItem('role');

            alert("Tu sesión ha expirado o no estás autorizado. Por favor, inicia sesión nuevamente.");
            window.location.href = '/login'; // ¡FORZAR RECARGA COMPLETA!
                                             // Esto reinicia todo el estado de React.
            return Promise.reject(error);
        }
        return Promise.reject(error); // Para cualquier otro error, simplemente reenvíalo
    }
);

export default axiosInstance;