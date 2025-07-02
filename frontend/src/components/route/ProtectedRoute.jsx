import React, { useEffect, useState } from 'react';
import { Navigate, Outlet, useNavigate } from 'react-router-dom';
import UserService from '../service/UserService';
import { useLoading } from '../context/LoadingContext';
import { isTokenExpired } from '../../utils/AuthUtils'; // Importa la función de utilidad

const ProtectedRoute = ({ adminOnly = false }) => {
    const [isAuthorized, setIsAuthorized] = useState(false);
    const [isLoadingAuth, setIsLoadingAuth] = useState(true); // Indica si la verificación de auth está en curso
    const navigate = useNavigate();
    const { startLoading, stopLoading } = useLoading();

    useEffect(() => {
        const verifyAuth = async () => {
            // El spinner solo se activará si necesitamos una verificación backend
            // o si el token no existe.

            const token = UserService.getToken();
            const role = UserService.getRole();

            // 1. **VERIFICACIÓN LOCAL RÁPIDA**
            if (!token || isTokenExpired(token)) {
                console.log("Token ausente o expirado localmente. Redirigiendo a login.");
                UserService.logout(); // Limpiar por si acaso
                setIsAuthorized(false);
                setIsLoadingAuth(false);
                stopLoading();
                navigate('/login');
                return;
            }

            // Si el token existe y no ha expirado LOCALMENTE,
            // podemos asumir una validez temporal y NO llamar al backend inmediatamente.
            // PERO, si la ruta es adminOnly, aún debemos confirmar el rol del backend.
            // Para simplificar, si el token existe localmente y no está expirado,
            // lo consideramos autorizado de inmediato y solo hacemos la petición
            // al backend si la lógica de rol lo requiere o si hay un error en una petición subsiguiente.

            if (adminOnly && role !== 'ADMIN') {
                // Si la ruta es adminOnly y el rol local no es ADMIN,
                // aún necesitamos verificar con el backend o redirigir directamente si no hay chances.
                // Podríamos hacer una llamada a getMyProfile aquí para confirmar el rol.
                // Sin embargo, para no hacer la petición, simplemente podemos redirigir.
                // Esta es una decisión de UX: ¿queremos que vea la página y luego se le redirija,
                // o redirigirlo antes de cargar nada? Para acceso denegado, es mejor rápido.
                alert('Acceso denegado. Solo los administradores pueden acceder a esta página.');
                setIsAuthorized(false);
                setIsLoadingAuth(false);
                stopLoading();
                navigate('/homepage'); // O a una página de "acceso denegado"
                return;
            }

            // Si llegamos aquí, el token existe localmente, no ha expirado localmente
            // y, si es adminOnly, el rol local parece ser ADMIN.
            // Consideramos al usuario autorizado temporalmente para renderizar la página.
            // La VERIFICACIÓN FINAL del token se hará por el interceptor de Axios
            // en la primera llamada a la API que el componente hijo haga (ej. getMyProfile en ProfilePage).
            setIsAuthorized(true);
            setIsLoadingAuth(false); // No necesitamos spinner si el acceso es inmediato
            stopLoading(); // Asegura que el spinner se oculte si se activó antes.
            
            // **Consideración:** Si quieres una validación backend al cargar CUALQUIER ruta protegida,
            // incluso si el token es válido localmente, descomenta el siguiente bloque.
            // Esto reintroduce una petición, pero es una garantía más fuerte.
            /*
            startLoading(); // Activa el spinner para la validación backend
            try {
                const response = await UserService.getMyProfile(); // Vuelve a validar con el backend
                if (response && response.users && response.users.role === role) {
                    setIsAuthorized(true);
                } else {
                    // Rol no coincide o datos inválidos del perfil
                    UserService.logout();
                    setIsAuthorized(false);
                    navigate('/login');
                }
            } catch (error) {
                console.error("Error en ProtectedRoute al revalidar con el backend:", error);
                // El interceptor ya maneja 401/403. Otros errores pueden ser capturados aquí.
                UserService.logout(); // Si hay error de red o similar, logout
                setIsAuthorized(false);
                navigate('/login');
            } finally {
                setIsLoadingAuth(false);
                stopLoading();
            }
            */
        };

        verifyAuth();
    }, [navigate, adminOnly, startLoading, stopLoading]); // Dependencias

    if (isLoadingAuth) {
        // Mientras se realiza la verificación (si se disparó la verificación backend),
        // no renderizamos nada, y el LoadingProvider mostrará el spinner.
        return null;
    }

    if (!isAuthorized) {
        // Esto solo se alcanzará si isLoadingAuth es false y isAuthorized es false,
        // lo cual ya fue manejado por las redirecciones en el useEffect.
        return <Navigate to="/login" replace />;
    }

    return <Outlet />;
};

export default ProtectedRoute;