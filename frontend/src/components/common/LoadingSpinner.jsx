import React from 'react';
import { ReactComponent as PacmanLoader } from '../../svg_resources/pacman.svg';
import '../../styles/LoadingSpinner.css'; // AEstilos en css

const LoadingSpinner = ({ size = '100px', color = '#000739' }) => {
    return (
        <div className="loading-spinner-container">
            <PacmanLoader
                style={{
                    width: '90%',
                    height: '90%',
                    color: '#000739',
                }}
            />
         <p>Cargando...</p>
        </div>
    );
};

export default LoadingSpinner;
