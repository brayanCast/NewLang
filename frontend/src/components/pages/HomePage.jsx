import React, { useState, useEffect, use } from 'react';
import Navbar from '../common/Navbar';
import Footer from '../common/Footer';
import PageService from '../service/PageService'; // Service for API calls
import UserService from '../service/UserService'; // Service for user-related API calls
import TypingEffect from '../effects/TypingEffect'; // Custom typing effect component
import { useLoading } from '../context/LoadingContext'; // Custom hook for loading state management
import '../../styles/HomePage.css'; // Import the CSS file for styling
import searchIcon from '../../img/search_24dp_FFFFFF_FILL0_wght400_GRAD0_opsz24.png'; // Import the search icon


function HomePage() {
    const [searchQuery, setSearchQuery] = useState('');
    const [searchResults, setSearchResults] = useState([]);
    const [searchError, setSearchError] = useState(null);
    const [showSuggestions, setShowSuggestions] = useState(false); // State to control suggestions visibility
    const { startLoading, stopLoading } = useLoading();
    const [userName, setUserName] = useState(''); // State to store the user's name

    useEffect(() => {
        const fetchUserName = async () => {
            try {
                const token = UserService.getToken();
                if (!token) throw new Error("No se encontró token");
                const data = await UserService.getYourProfile(token);
                setUserName(data.users.nameUser);
            } catch (error) {
                console.error("Error al obtener el nombre de usuario:", error);
            }
        };

        fetchUserName();
    }, []);

    const greetingMessage = `¡Hola, ${userName}!`;

    //useEffect to handle search functionality and call to the API
    useEffect(() => {

        // If query is empty, it does not perform a search
        if (searchQuery.trim() === '') {
            setSearchResults([]); //Clear previous results
            setSearchError(null); //p
            setShowSuggestions(false); // Hide suggestions when query is empty
            return;
        }

        //Debounce timer configuration
        const debounceTimer = setTimeout(async () => {
            setSearchError(null); // Clear previous error

            try {
                // Call the searchBar method from PageService with the search query
                const data = await PageService.searchBar(searchQuery);
                setSearchResults(data); // Set search results
                console.log('Datos recibidos', data); // Log the received data 

                if (data.length > 0) {
                    setShowSuggestions(true); // Show suggestions if results are found
                } else {
                    setShowSuggestions(false); // Hide suggestions if no results
                }

            } catch (error) {
                console.error("Error al buscar:", error);
                setSearchResults([]); // Clear results on error
                setSearchError("Error al buscar resultados. Por favor, inténtelo más tarde.");
                setShowSuggestions(false); // Hide suggestions on error
            }
        }, 500); // 500ms or 0.5 second debounce time

        return () => {
            clearTimeout(debounceTimer); // Clear the debounce timer on cleanup
        };
    }, [searchQuery]);

    const handleSuggestionClick = (suggestion) => {
        setSearchQuery(suggestion.text); // Set the search query to the clicked suggestion
        setSearchResults([]); // Clear search results after selection
        setShowSuggestions(false); // Hide suggestions after selection
    };

    const handleFocus = () => {
        if (searchQuery.trim() !== '' && searchResults.length > 0) {
            setShowSuggestions(true); // Show suggestions when input is focused
        }
    };

    const handleBlur = () => {
        setTimeout(() => {
            setShowSuggestions(false); // Hide suggestions when input loses focus
        }, 100);
    };

    return (
        <div className="homepage">
            <Navbar />
            <div className="homepage-content">
                <h1><TypingEffect text={greetingMessage} /></h1>

                <div className='search-bar-wrapper'>
                    <div className='search-bar-container'>
                        <input
                            type='text'
                            placeholder='Buscar palabras o frases...'
                            value={searchQuery}
                            onChange={(e) => setSearchQuery(e.target.value)}
                            onFocus={handleFocus}
                            onBlur={handleBlur}
                            className='search-bar-input'
                        />
                        <button className='search-bar-button'>
                            <img src={searchIcon} alt="Buscar" />
                        </button>
                    </div>
                    {showSuggestions && searchResults.length > 0 && (
                        <ul className='suggestions-list'>
                            {searchResults.map((item) => (
                                <li
                                    key={`${item.type}-${item.id}-${item.text}`} // Use a unique key for each suggestion item
                                    onClick={() => handleSuggestionClick(item)}
                                    className='suggestion-item'>
                                    {item.text} {/* Display phrases or words related to the search*/}
                                </li>
                            ))}
                        </ul>
                    )}
                </div>
                {searchError && <p className='error-message'>{searchError}</p>} {/* Display error message if any */}

            </div>
            <Footer />
        </div>
    );

}

export default HomePage;