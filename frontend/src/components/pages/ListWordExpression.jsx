
import React, { useState, useEffect } from 'react';
import ActionServices from '../service/ActionServices';
import { useLoading } from '../context/LoadingContext';
import Navbar from '../common/Navbar';
import Footer from '../common/Footer';
import '../../styles/ListWordExpression.css';


function ListWordExpression() {
    const { startLoading, stopLoading } = useLoading();
    const [data, setData] = useState([]);
    const [filteredData, setFilteredData] = useState([]);
    const [error, setError] = useState(null);
    const [searchQuery, setSearchQuery] = useState('');
    const [filterType, setFilterType] = useState('all'); // 'all', 'word', 'expression'
    // Mock de ActionServices para demostración

    useEffect(() => {
        fetchData();
    }, []);

    useEffect(() => {
        filterData();
    }, [searchQuery, filterType, data]);

    const fetchData = async () => {
        try {
            startLoading();
            const [wordsResponse, expressionsResponse] = await Promise.all([
                ActionServices.getAllWords(),
                ActionServices.getAllExpressions()
            ]);

            // Transformar palabras
            const wordsData = wordsResponse.map(word => ({
                id: word.idWord,
                type: 'word',
                englishText: word.englishWord,
                spanishText: word.spanishWord,
                categoryName: word.category?.nameCategory || 'Sin categoría',
                levelName: word.level?.nameLevel || 'Sin nivel',
                imageUrl: word.imageUrl
            }));

            // Transformar expresiones
            const expressionsData = expressionsResponse.map(expression => ({
                id: expression.idExpression,
                type: 'expression',
                englishText: expression.englishExpression,
                spanishText: expression.spanishExpression,
                categoryName: expression.category?.nameCategory || 'Sin categoría',
                levelName: expression.level?.nameLevel || 'Sin nivel',
                imageUrl: expression.imageUrl
            }));

            const combinedData = [...wordsData, ...expressionsData];
            setData(combinedData);
            setError(null);
        } catch (err) {
            console.error('Error fetching data:', err);
            setError('Error al cargar los datos. Por favor, inténtelo más tarde.');
        } finally {
            stopLoading();
        }
    };

    const filterData = () => {
        let filtered = data;

        // Filtrar por tipo
        if (filterType !== 'all') {
            filtered = filtered.filter(item => item.type === filterType);
        }

        // Filtrar por búsqueda
        if (searchQuery.trim()) {
            filtered = filtered.filter(item =>
                item.englishText.toLowerCase().includes(searchQuery.toLowerCase()) ||
                item.spanishText.toLowerCase().includes(searchQuery.toLowerCase()) ||
                item.categoryName.toLowerCase().includes(searchQuery.toLowerCase()) ||
                item.levelName.toLowerCase().includes(searchQuery.toLowerCase())
            );
        }

        setFilteredData(filtered);
    };

    const handleView = (item) => {
        // Para implementación real: navigate(`/${item.type}-detail/${item.id}`)
        alert(`Ver detalles de ${item.type}: ${item.englishText} / ${item.spanishText}`);
        console.log('Navegando a vista de detalles:', item);
    };

    const handleEdit = (item) => {
        // Para implementación real: navigate(`/edit-${item.type}/${item.id}`)
        alert(`Editar ${item.type}: ${item.englishText} / ${item.spanishText}`);
        console.log('Navegando a edición:', item);
    };

    const handleDelete = async (item) => {
        const confirmMessage = `¿Está seguro que desea eliminar ${item.type === 'word' ? 'la palabra' : 'la expresión'}: "${item.englishText} / ${item.spanishText}"?`;

        if (window.confirm(confirmMessage)) {
            try {
                if (item.type === 'word') {
                    await ActionServices.deleteWordById(item.id);
                } else {
                    await ActionServices.deleteExpressionById(item.id);
                }

                // Actualizar la lista después de eliminar
                await fetchData();
                alert(`${item.type === 'word' ? 'Palabra' : 'Expresión'} eliminada exitosamente.`);
            } catch (err) {
                console.error('Error deleting item:', err);
                alert('Error al eliminar. Por favor, inténtelo más tarde.');
            }
        }
    };

    if (error) {
        return (
            <div className="error-container">
                <div className="error-card">
                    <div className="error-icon">
                        <svg fill="none" stroke="currentColor" viewBox="0 0 24 24">
                            <path strokeLinecap="round" strokeLinejoin="round" strokeWidth={2} d="M12 9v2m0 4h.01m-6.938 4h13.856c1.54 0 2.502-1.667 1.732-2.5L13.732 4c-.77-.833-1.732-.833-2.5 0L3.232 16.5c-.77.833.192 2.5 1.732 2.5z" />
                        </svg>
                    </div>
                    <h3 className="error-title">Error de carga</h3>
                    <p className="error-message">{error}</p>
                    <button onClick={fetchData} className="retry-button">
                        Reintentar
                    </button>
                </div>
            </div>
        );
    }

    return (
        <div className="words-expressions-container">
            <Navbar />
            <div className="words-expressions-wrapper">
                <div className="words-expressions-card">
                    {/* Header */}
                    <div className="table-header">
                        <h1 className="table-title">Gestión de Palabras y Expresiones</h1>

                        {/* Filtros y búsqueda */}
                        <div className="filters-container">
                            <div className="search-container">
                                <Search className="search-icon" />
                                <input
                                    type="text"
                                    placeholder="Buscar por texto en inglés, español, categoría o nivel..."
                                    value={searchQuery}
                                    onChange={(e) => setSearchQuery(e.target.value)}
                                    className="search-input"
                                />
                            </div>
                            <select
                                value={filterType}
                                onChange={(e) => setFilterType(e.target.value)}
                                className="filter-select"
                            >
                                <option value="all">Todos</option>
                                <option value="word">Solo Palabras</option>
                                <option value="expression">Solo Expresiones</option>
                            </select>
                        </div>

                        {/* Contador */}
                        <div className="results-counter">
                            Mostrando {filteredData.length} de {data.length} elementos
                        </div>
                    </div>

                    {/* Tabla */}
                    <div className="table-container">
                        <table className="data-table">
                            <thead className="table-head">
                                <tr>
                                    <th className="table-header-cell">Tipo</th>
                                    <th className="table-header-cell">Texto en Inglés</th>
                                    <th className="table-header-cell">Texto en Español</th>
                                    <th className="table-header-cell">Categoría</th>
                                    <th className="table-header-cell">Nivel</th>
                                    <th className="table-header-cell table-header-center">Acciones</th>
                                </tr>
                            </thead>
                            <tbody className="table-body">
                                {filteredData.length === 0 ? (
                                    <tr>
                                        <td colSpan="6" className="empty-state">
                                            <div className="empty-state-content">
                                                <Search className="empty-state-icon" />
                                                <p className="empty-state-title">No se encontraron resultados</p>
                                                <p className="empty-state-text">Intente ajustar los filtros de búsqueda</p>
                                            </div>
                                        </td>
                                    </tr>
                                ) : (
                                    filteredData.map((item) => (
                                        <tr key={`${item.type}-${item.id}`} className="table-row">
                                            <td className="table-cell">
                                                <span className={`type-badge ${item.type === 'word' ? 'type-badge-word' : 'type-badge-expression'}`}>
                                                    {item.type === 'word' ? 'Palabra' : 'Expresión'}
                                                </span>
                                            </td>
                                            <td className="table-cell table-cell-bold">
                                                {item.englishText}
                                            </td>
                                            <td className="table-cell table-cell-regular">
                                                {item.spanishText}
                                            </td>
                                            <td className="table-cell table-cell-regular">
                                                {item.categoryName}
                                            </td>
                                            <td className="table-cell table-cell-regular">
                                                {item.levelName}
                                            </td>
                                            <td className="table-cell table-cell-center">
                                                <div className="actions-container">
                                                    <button
                                                        onClick={() => handleView(item)}
                                                        className="action-button action-button-view"
                                                        title="Ver detalles"
                                                    >
                                                        <Eye className="action-icon" />
                                                    </button>
                                                    <button
                                                        onClick={() => handleEdit(item)}
                                                        className="action-button action-button-edit"
                                                        title="Editar"
                                                    >
                                                        <Edit className="action-icon" />
                                                    </button>
                                                    <button
                                                        onClick={() => handleDelete(item)}
                                                        className="action-button action-button-delete"
                                                        title="Eliminar"
                                                    >
                                                        <Trash2 className="action-icon" />
                                                    </button>
                                                </div>
                                            </td>
                                        </tr>
                                    ))
                                )}
                            </tbody>
                        </table>
                    </div>

                    {/* Footer con información adicional */}
                    {filteredData.length > 0 && (
                        <div className="table-footer">
                            <div className="footer-content">
                                <div className="footer-stats">
                                    Palabras: {filteredData.filter(item => item.type === 'word').length} |
                                    Expresiones: {filteredData.filter(item => item.type === 'expression').length}
                                </div>
                                <button onClick={fetchData} className="refresh-button">
                                    Actualizar datos
                                </button>
                            </div>
                        </div>
                    )}
                </div>
            </div>
            <Footer />
        </div>
    );
}

export default ListWordExpression;