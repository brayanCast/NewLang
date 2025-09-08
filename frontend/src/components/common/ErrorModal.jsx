import React from "react";
import '../../styles/Modal.css';

const ErrorModal = ({ isOpen, onClose, title = "Error", message, buttonText = "Close" }) => {
    if (!isOpen) return null;

    const handleBackdropClick = (e) => {
        if (e.target === e.currentTarget) {
            onClose();
        }
    };

    return (
        <div className="modal-overlay" onClick={handleBackdropClick}>
            <div className="modal-container error-modal">
                <div className="modal-icon error-icon">
                    <span>×</span>
                </div>
                <h2 className="modal-title error-title">{title}</h2>
                <p className="modal-message">{message}</p>
                <div className="modal-buttons">
                    <button
                        className="modal-button error-btn"
                        onClick={onClose}
                    >
                        {buttonText}
                    </button>
                </div>
            </div>
        </div>
    );
}

export default ErrorModal;

