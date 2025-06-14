import React from 'react';
import Navbar from '../common/Navbar';
import Footer from '../common/Footer';
import '../../styles/HomePage.css'; // Import the CSS file for styling


function HomePage() {

    return (
        <div className="homepage">
            <Navbar />
            <div className="homepage-content">
                <h1>Welcome to the Home Page</h1>
                <p>This is the main content area of the home page.</p>
                {/* Add more content here as needed */}
            </div>
            <Footer />
        </div>
    );

} 

export default HomePage;