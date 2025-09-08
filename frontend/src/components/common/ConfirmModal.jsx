import React from 'react';
import '../../styles/Modal.css';

const ConfirmModal = ({isOpen, onClose, onConfirm, title= "Alerta", message, confirmText= "Aceptar", cancelText= "Cancelar"}) => {
    if (!isOpen) return null;

    const handleBackdropClick = (e) => {
        if (e.target === e.currentTarget) {
            onClose();
        }
    }

    return (
        <div className="modal-overlay" onClick={handleBackdropClick}>
            <div className="modal-container confirm-modal">
                <div className="modal-icon confirm-icon">
                    <span>!</span>
                </div>
                <h2 className="modal-title">{title}</h2>
                <p className="modal-message">{message}</p>
                <div className="modal-buttons">
                    <button 
                        className="modal-button cancel-btn" 
                        onClick={onClose}
                    >
                        {cancelText}
                    </button>
                    <button 
                        className="modal-button confirm-btn" 
                        onClick={onConfirm}
                    >
                        {confirmText}
                    </button>
                </div>
            </div>
        </div>

    );
}

export default ConfirmModal;