import React, { useState, useEffect } from 'react';
import { useParams, useNavigate } from 'react-router-dom';
import ActionServices from '../service/ActionServices';
import Navbar from '../common/Navbar';
import Footer from '../common/Footer';
import { useLoading } from '../context/LoadingContext';
import '../../styles/ModifyWordExpression.css';

function ModifyWordExpression() {
    const { startLoading, stopLoading } = useLoading();
    const { type, id } = useParams();
    const navigate = useNavigate();

    const [formData, setFormData] = useState({
        englishWord: '',
        spanishWord: '',
        englishExpression: '',
        spanishExpression: '',
        imageUrl: '',
        categoryId: '',
        levelId: ''
    });

    const [categories, setCategories] = useState([]);
    const [levels, setLevels] = useState([]);

    const [message, setMessage] = useState('');
    const [isSuccess, setIsSuccess] = useState(false);

    useEffect(() => {
        const fetchInitialData = async () => {
            try {
                startLoading();
                const [fetchedCategories, fetchedLevels] = await Promise.all([
                    ActionServices.getCategoryList(),
                    ActionServices.getLevelList()
                ]);
                setCategories(fetchedCategories);
                setLevels(fetchedLevels);
                let elementData;

                if (type === 'word') {
                    elementData = await ActionServices.getWordById(id);
                    setFormData({
                        englishWord: elementData.englishWord || '',
                        spanishWord: elementData.spanishWord || '',
                        englishExpression: '',
                        spanishExpression: '',
                        imageUrl: elementData.imageUrl || '',
                        categoryId: elementData.categoryResponseDTO.categoryId || '',
                        levelId: elementData.levelResponseDTO.levelId || ''
                    });
                } else if (type === 'expression') {
                    elementData = await ActionServices.getExpressionById(id);
                    setFormData({
                        englishWord: '',
                        spanishWord: '',
                        englishExpression: elementData.englishExpression || '',
                        spanishExpression: elementData.spanishExpression || '',
                        imageUrl: elementData.imageUrl || '',
                        categoryId: elementData.categoryResponseDTO.categoryId || '',
                        levelId: elementData.levelResponseDTO.levelId || ''
                    });
                } else {
                    throw new Error('Tipo de elemento no válido');
                }

                console.log("Datos cargados", elementData);

            } catch (error) {
                console.error('Error cargando los datos', error);
                setMessage('Error al cargar los datos del elemento');
                setIsSuccess(false);
            } finally {
                stopLoading();
            }
        };

        if (type && id && (type === 'word' || type === 'expression')) {
            fetchInitialData();
        } else {
            setMessage('Parámetros de url inválidos');
            setIsSuccess(false);
            stopLoading();
        }

    }, [type, id, startLoading, stopLoading]);

    const handleChange = (e) => {
        const { name, value } = e.target;
        setFormData(prevState => ({
            ...prevState,
            [name]: value
        }));
    };

    const handleSubmit = async (e) => {
        e.preventDefault();
        setMessage('');
        setIsSuccess(false);

        try {
            startLoading();
            let response;
            let payload;

            if (type === 'word') {
                const { englishWord, spanishWord, imageUrl, categoryId, levelId } = formData;
                payload = {
                    englishWord,
                    spanishWord,
                    imageUrl,
                    categoryId: Number(categoryId),
                    levelId: Number(levelId)
                };
                response = await ActionServices.updateWordById(id, payload);
                console.log("Respuesta de actualización de palabra", response);
            } else if (type === 'expression') {
                const { englishExpression, spanishExpression, imageUrl, categoryId, levelId } = formData;
                payload = {
                    englishExpression,
                    spanishExpression,
                    imageUrl,
                    categoryId: Number(categoryId),
                    levelId: Number(levelId)
                };
                response = await ActionServices.updateExpressionById(id, payload);
                console.log("Respuesta de actualización de expresión", response);
            }

            setMessage(response.message || 'Elemento actualizado correctamente');
            setIsSuccess(true);

            setTimeout(() => {
                navigate(`/list-word-expression`);
            }, 2000);

        } catch (error) {
            console.error('Error actualizando el elemento', error);
            setIsSuccess(false);

            if (error.response) {
                const status = error.response.status;
                const errorMessage = error.response.data.message || 'Ocurrió un error desconocido';

                if (status === 409) {
                    setMessage(`Error: ${errorMessage}`);
                } else if (status === 404) {
                    setMessage('Error: Elemento no encontrado');
                } else {
                    setMessage(`Error ${status}: ${errorMessage}`);
                }
            } else if (error.request) {
                setMessage('Error: No se recibió respuesta del servidor');
            } else {
                setMessage('Ocurrió un error al preparar la petición');
            }

        } finally {
            stopLoading();
        }
    };

    const handleCancel = () => {
        navigate(`/list-word-expression`);
    };

    if (!type || !id || (type !== 'word' && type !== 'expression')) {
        return (
            <div className="form-container">
                <Navbar />
                <div className="error-container">
                    <h2>Error: Parámetros de URL inválidos</h2>
                    <button onClick={() => navigate('/list-word-expression')} className='submit-button'>Volver a la lista</button>
                </div>
                <Footer />
            </div>
        );
    }

    return (
        <div className="form-container">
            <Navbar />
            <h2>Modificar {type === 'word' ? 'Palabra' : 'Expresión'}</h2>
            <form onSubmit={handleSubmit}>

                <div className="form-group">
                    <label>Tipo:</label>
                    <div className='form-info'>
                        {type === 'word' ? 'Palabra' : 'Expresión'}
                    </div>
                </div>

                {/* Campos específicos según el tipo */}
                {type === 'word' ? (
                    <>
                        <div className="form-group">
                            <label htmlFor="englishWord">English Word:</label>
                            <input
                                type="text"
                                id="englishWord"
                                name="englishWord"
                                value={formData.englishWord}
                                onChange={handleChange}
                                required
                            />
                        </div>
                        <div className="form-group">
                            <label htmlFor="spanishWord">Spanish Word:</label>
                            <input
                                type="text"
                                id="spanishWord"
                                name="spanishWord"
                                value={formData.spanishWord}
                                onChange={handleChange}
                                required
                            />
                        </div>
                    </>
                ) : (
                    <>
                        <div className="form-group">
                            <label htmlFor="englishExpression">English Expression:</label>
                            <input
                                type="text"
                                id="englishExpression"
                                name="englishExpression"
                                value={formData.englishExpression}
                                onChange={handleChange}
                                required
                            />
                        </div>
                        <div className="form-group">
                            <label htmlFor="spanishExpression">Spanish Expression:</label>
                            <input
                                type="text"
                                id="spanishExpression"
                                name="spanishExpression"
                                value={formData.spanishExpression}
                                onChange={handleChange}
                                required
                            />
                        </div>
                    </>
                )}

                {/* Campos comunes */}
                <div className="form-group">
                    <label htmlFor="imageUrl">Image URL:</label>
                    <input
                        type="url"
                        id="imageUrl"
                        name="imageUrl"
                        value={formData.imageUrl}
                        onChange={handleChange}
                        required
                    />
                </div>

                <div className="form-group">
                    <label htmlFor="categoryId">Category:</label>
                    <select
                        id="categoryId"
                        name="categoryId"
                        value={formData.categoryId}
                        onChange={handleChange}
                        required
                    >
                        <option value="">Select a Category</option>
                        {categories.map(category => (
                            <option key={category.idCategory} value={category.idCategory}>
                                {category.nameCategory}
                            </option>
                        ))}
                    </select>
                </div>

                <div className="form-group">
                    <label htmlFor="levelId">Level:</label>
                    <select
                        id="levelId"
                        name="levelId"
                        value={formData.levelId}
                        onChange={handleChange}
                        required
                    >
                        <option value="">Select a Level</option>
                        {levels.map(level => (
                            <option key={level.idLevel} value={level.idLevel}>
                                {level.nameLevel}
                            </option>
                        ))}
                    </select>
                </div>

                {/* Botones de acción */}
                <div className="form-buttons">
                    <button type="submit" className="submit-update-button">
                        Actualizar
                    </button>
                    <button type="button" onClick={handleCancel} className="cancel-button">
                        Cancelar
                    </button>
                </div>

                {message && (
                    <p className={`form-message ${isSuccess ? 'success' : 'error'}`}>
                        {message}
                    </p>
                )}

            </form>
            <Footer />
        </div>
    );
}

export default ModifyWordExpression;
