import React from "react";
import '../../styles/Modal.css';

const SuccessModal = ({ isOpen, onClose, title = "Exito", message, buttonText = "Continue" }) => {
    if (!isOpen) return null;

    const handleBackdropClick = (e) => {
        if (e.target === e.currentTarget) {
            onClose();
        }
    };

    return (
        <div className="modal-overlay" onClick={handleBackdropClick}>
            <div className="modal-container success-modal">
                <div className="modal-icon success-icon">
                    <span>✓</span>
                </div>
                <h2 className="modal-title success-title">{title}</h2>
                <p className="modal-message">{message}</p>
                <div className="modal-buttons">
                    <button
                        className="modal-button success-btn"
                        onClick={onClose}
                    >
                        {buttonText}
                    </button>
                </div>
            </div>
        </div>
    );
}

export default SuccessModal;