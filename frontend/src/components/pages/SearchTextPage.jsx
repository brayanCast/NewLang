import React, {useState, useEffect} from "react";
import { useParams } from "react-router-dom";
import Navbar from "../common/Navbar";
import Footer from "../common/Footer";
import PageService from "../../components/service/PageService";
import '../../styles/SearchTextPage.css';

function SearchTextPage() {
    const { query } = useParams();
    const [result, setResult] = useState(null);
    const [loading, setLoading ] = useState(true);
    const [ error, setError ] = useState(null);

    useEffect(() => {
        const fetchSearchResults = async () => {
            setLoading(true);
            setError(null);

            try {
                const decodeQuery = decodeURIComponent(query);
                const token = localStorage.getItem('token');

                const data = await PageService.getPhraseOrWord(decodeQuery);
                setResult(data);
            } catch (err) {
                alert("Error en la búsqueda", err);
                setError("No se encontraron resultados para el termino de búsqueda");
            } finally {
                setLoading(false);
            }
        

        };
        fetchSearchResults();
    }, [query]);

        const renderResultCard = () => {
        // Renderizado condicional basado en el tipo de resultado
        if (result.type === 'word') {
            const word = result.data;
            return (
                <div className="result-card word-card">
                    <img src={word.imageUrl} alt={word.englishWord} className="result-image" />
                    <div className="result-info">
                        <h3>{word.englishWord}</h3>
                        <p><strong>Traducción:</strong> {word.spanishWord}</p>
                        <p><strong>Categoría:</strong> {word.categoryResponseDTO?.nameCategory}</p>
                        <p><strong>Nivel:</strong> {word.levelResponseDTO?.nameLevel}</p>
                    </div>
                </div>
            );
        } else if (result.type === 'expression') {
            const expression = result.data;
            return (
                <div className="result-card expression-card">
                    <img src={expression.imageUrl} alt={expression.englishExpression} className="result-image" />
                    <div className="result-info">
                        <h3>{expression.englishExpression}</h3>
                        <p><strong>Traducción:</strong> {expression.spanishExpression}</p>
                        <p><strong>Categoría:</strong> {expression.categoryResponseDTO?.nameCategory}</p>
                        <p><strong>Nivel:</strong> {expression.levelResponseDTO?.nameLevel}</p>
                    </div>
                </div>
            );
        }
        return null;
    };

    if (loading) {
        return <div className="loading-state">Cargando...</div>;
    }

    if (error) {
        return <div className="error-state">{error}</div>;
    }

    if (!result) {
        return <div className="no-results">No se encontraron resultados para "{query}".</div>;
    }

    return (
        <div className="search-detail-page">
            <Navbar />
            <div className="search-detail-content">
                <h2>Resultados de búsqueda para: "{query}"</h2>
                {renderResultCard()}
            </div>
            <Footer />
        </div>
    );
}

export default SearchTextPage;
