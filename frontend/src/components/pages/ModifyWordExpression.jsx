import React, { useState, useEffect } from 'react';
import { useParams, useNavigate } from 'react-router-dom';
import ActionServices from '../service/ActionServices';
import Navbar from '../common/Navbar';
import Footer from '../common/Footer';
import { useLoading } from '../context/LoadingContext';
import ConfirmModal from '../common/ConfirmModal';
import SuccessModal from '../common/SuccessModal';
import ErrorModal from '../common/ErrorModal';
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
        nameCategory: '',
        levelId: '',
        nameLevel: ''
    });

    const [categories, setCategories] = useState([]);
    const [levels, setLevels] = useState([]);

    const [showConfirmModal, setShowConfirmModal] = useState(false);
    const [showSuccessModal, setShowSuccessModal] = useState(false);
    const [showErrorModal, setShowErrorModal] = useState(false);
    const [modalMessage, setModalMessage] = useState('');

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
                        categoryId: String(elementData.categoryResponseDTO.idCategory) || '',
                        nameCategory: elementData.categoryResponseDTO.nameCategory || '',
                        levelId: String(elementData.levelResponseDTO.idLevel) || '',
                        nameLevel: elementData.levelResponseDTO.nameLevel || ''
                    });
                } else if (type === 'expression') {
                    elementData = await ActionServices.getExpressionById(id);
                    setFormData({
                        englishWord: '',
                        spanishWord: '',
                        englishExpression: elementData.englishExpression || '',
                        spanishExpression: elementData.spanishExpression || '',
                        imageUrl: elementData.imageUrl || '',
                        categoryId: String(elementData.categoryResponseDTO.idCategory) || '',
                        nameCategory: elementData.categoryResponseDTO.nameCategory || '',
                        levelId: String(elementData.levelResponseDTO.idLevel) || '',
                        nameLevel: elementData.levelResponseDTO.nameLevel || ''
                    });
                } else {
                    throw new Error('Tipo de elemento no válido');
                }

            } catch (error) {
                console.error('Error cargando los datos', error);
                setModalMessage('Error cargando los datos iniciales. Intente nuevamente.');
                setShowErrorModal(true);
            } finally {
                stopLoading();
            }
        };

        if (type && id && (type === 'word' || type === 'expression')) {
            fetchInitialData();
        } else {
            setModalMessage('Parámetros de URL inválidos');
            setShowErrorModal(true);
            stopLoading();
        }

    }, [type, id, startLoading, stopLoading]);

    const handleChange = (e) => {
        const { name, value } = e.target;

        if (name === 'categoryId') {
            const selectedCategory = categories.find(cat => String(cat.idCategory) === String(value));
            setFormData(prevState => ({
                ...prevState,
                categoryId: value,
                nameCategory: selectedCategory ? selectedCategory.nameCategory : ''

            }));

        } else if (name === 'levelId') {
            const selectedLevel = levels.find(cat => String(cat.idLevel) === String(value));
            setFormData(prevState => ({
                ...prevState,
                levelId: value,
                nameLevel: selectedLevel ? selectedLevel.nameLevel : ''
            }));
        } else {
            setFormData(prevState => ({
                ...prevState,
                [name]: value,
            }));
        }
    };

    const handleSubmit = async (e) => {
        e.preventDefault();
        setShowConfirmModal(true);
    };

    // Función para manejar la confirmación de la actualización y enviar la petición a la API
    const handleConfirmUpdate = async () => {
        setShowConfirmModal(false);

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

                setModalMessage('Palabra actualizada correctamente');

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

                setModalMessage('Expresión actualizada correctamente');
            }

        } catch (error) {
            console.error('Error actualizando el elemento', error);
            let errorMessage = 'Ocurrió un error desconocido';

            if (error.response) {
                const status = error.response.status;
                const responseMessage = error.response.data.message || 'Ocurrió un error desconocido';

                if (status === 409) {
                    errorMessage = responseMessage;
                } else if (status === 404) {
                    errorMessage = `Error: ${type} no encontrado`;
                } else {
                    errorMessage = `Error ${status}: ${errorMessage}`;
                }
            } else if (error.request) {
                errorMessage = 'Error: No se recibió respuesta del servidor';
            } else {
                errorMessage = 'Ocurrió un error al preparar la petición';
            }

            setModalMessage(errorMessage);
            setShowErrorModal(true);

        } finally {
            stopLoading();
            if (!showErrorModal) setShowSuccessModal(true);

        }
    };

    const handleSuccessModalClose = () => {
        setShowSuccessModal(false);
        setTimeout(() => {
            navigate('/list-word-expression');
        }, 1000);
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
                        <option value={formData.categoryId}>{formData.nameCategory}</option>

                        {categories
                            .filter(category => String(category.idCategory) !== String(formData.categoryId))
                            .map(category => (
                                <option key={category.idCategory} value={String(category.idCategory)}>
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
                        <option value={formData.levelId}>{formData.nameLevel}</option>
                        {levels
                            .filter(level => String(level.idLevel) !== String(formData.levelId))
                            .map(level => (
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
            </form>

            {/* Modales */}
            <ConfirmModal
                isOpen={showConfirmModal}
                onClose={() => setShowConfirmModal(false)}
                onConfirm={handleConfirmUpdate}
                message="¿Está seguro que desea actualizar este elemento?"
            />

            <SuccessModal
                isOpen={showSuccessModal}
                onClose={handleSuccessModalClose}
                message={modalMessage}
            />

            <ErrorModal
                isOpen={showErrorModal}
                onClose={() => setShowErrorModal(false)}
                message={modalMessage}
            />

            <Footer />
        </div>
    );
}

export default ModifyWordExpression;