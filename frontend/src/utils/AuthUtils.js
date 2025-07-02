export const isTokenExpired = (token) => {
    if (!token) return true;
    try {
        const payload = JSON.parse(atob(token.split('.')[1])); // Decodifica el payload del JWT
        // 'exp' es el tiempo de expiración en segundos desde la época UNIX
        const expirationTime = payload.exp * 1000; // Convertir a milisegundos
        return Date.now() >= expirationTime; // Retorna true si la fecha actual es mayor o igual a la expiración
    } catch (error) {
        console.error("Error al decodificar o verificar el token:", error);
        return true; // Si hay un error, considera el token como expirado o inválido
    }
};
