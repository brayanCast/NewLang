import { createContext, useContext, useState } from "react"
import LoadingSpinner from '../common/LoadingSpinner';

const LoadingContext = createContext();


//Es un Hook (función que permite usar características de React) que permite manejar el estado de carga en la aplicación.
export const useLoading = () => {
    const context = useContext(LoadingContext);
    if (!context) {
        throw new Error("useLoading debe usarse dentro del LoadingProvider");
    }

    return context;
}

// Proveedor de contexto que envuelve la aplicación y proporciona el estado de carga
export const LoadingProvider = ({ children }) => {
    const [isLoading, setIsLoading] = useState(false);

    const startLoading = () => setIsLoading(true); // Comienza y muestra el modal de carga 
    const stopLoading = () => setIsLoading(false); //Termina y oculta el modal de carga

    return (
        <LoadingContext.Provider value={{ startLoading, stopLoading }}>
            {/* Renderiza los componentes hijos dentro del contexto de carga*/}
            {children}
            {isLoading && (
                <dialog className="loading-modal">
                    <div className="loading-content">
                        <LoadingSpinner size="150px" color="#000739"/>
                    </div>
                </dialog>
            )}
        </LoadingContext.Provider>
    );
}

