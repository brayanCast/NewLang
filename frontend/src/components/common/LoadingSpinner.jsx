import React from 'react';
import { ReactComponent as PacmanLoader } from '../../svg_resources/pacman.svg';

const LoadingSpinner = ({ size = '100px', color = 'white'}) => {
    return (
        <div className="loading-spinner-container"
            style={{
                display: 'flex',
                justifyContent: 'center',
                alignItems: 'center',
                width: '100%',
                height: '100%',
            }}
        >
            <PacmanLoader 
                style={{
                    width: '90%',
                    height: '90%',
                    color: '#000739'
                }}
            />
        </div>
    );
};

export default LoadingSpinner;
