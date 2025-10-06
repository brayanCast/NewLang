import React, { useState, useEffect } from 'react';
import { useNavigate } from 'react-router-dom';
import ActionServices from '../service/ActionServices';
import Navbar from '../common/Navbar';
import Footer from '../common/Footer';
import { useLoading } from '../context/LoadingContext';
import ConfirmModal from '../common/ConfirmModal';
import SuccessModal from '../common/SuccessModal';
import ErrorModal from '../common/ErrorModal';
import '../../styles/CreateLearningRoutine.css';

function CreateLearningRoutine() {
    const { startLoading, stopLoading } = useLoading();
    const navigate = useNavigate();

    const [formData, setFormData] = useState({
        nameRoutine: '',
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

            } catch (error) {
                console.error('Error cargando los datos', error);
                setModalMessage('Error cargando los datos iniciales. Intente nuevamente.');
                setShowErrorModal(true);
            } finally {
                stopLoading();
            }
        };

        fetchInitialData();
    }, [startLoading, stopLoading]);

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
            const selectedLevel = levels.find(lvl => String(lvl.idLevel) === String(value));
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
        setModalMessage('¿Está seguro que desea crear esta rutina de aprendizaje?');
        setShowConfirmModal(true);
    };

    const handleConfirmCreate = async () => {
        setShowConfirmModal(false);

        try {
            startLoading();

            const { nameRoutine, categoryId, levelId } = formData;
            const payload = {
                nameRoutine,
                categoryId: Number(categoryId),
                levelId: Number(levelId)
            };

            const response = await ActionServices.createLearningRoutine(payload);
            console.log("Respuesta de creación de rutina", response);

            setModalMessage('Rutina de aprendizaje creada correctamente');
            setShowSuccessModal(true);

        } catch (error) {
            console.error('Error creando la rutina', error);
            let errorMessage = 'Ocurrió un error desconocido';

            if (error.response) {
                const status = error.response.status;
                const responseMessage = error.response.data.message || error.response.data || 'Ocurrió un error desconocido';

                if (status === 409) {
                    errorMessage = `Ya existe una rutina con ese nombre`;
                } else if (status === 404) {
                    errorMessage = `Categoría o nivel no encontrado`;
                } else {
                    errorMessage = `Error ${status}: ${responseMessage}`;
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
        }
    };

    const handleSuccessModalClose = () => {
        setShowSuccessModal(false);
        setTimeout(() => {
            navigate('/my-routines'); // Ajusta la ruta según tu aplicación
        }, 1000);
    };

    const handleCancel = () => {
        navigate('/my-routines'); // Ajusta la ruta según tu aplicación
    };

    return (
        <div className="form-container">
            <Navbar />
            <h2>Crear Rutina de Aprendizaje</h2>
            <form onSubmit={handleSubmit}>

                <div className="form-group">
                    <label htmlFor="nameRoutine">Nombre de la Rutina:</label>
                    <input
                        type="text"
                        id="nameRoutine"
                        name="nameRoutine"
                        value={formData.nameRoutine}
                        onChange={handleChange}
                        placeholder="Ej: Rutina de Vocabulario Básico"
                        required
                    />
                </div>

                <div className="form-group">
                    <label htmlFor="categoryId">Categoría:</label>
                    <select
                        id="categoryId"
                        name="categoryId"
                        value={formData.categoryId}
                        onChange={handleChange}
                        required
                    >
                        <option value="">Seleccione una categoría</option>
                        {categories.map(category => (
                            <option key={category.idCategory} value={String(category.idCategory)}>
                                {category.nameCategory}
                            </option>
                        ))}
                    </select>
                </div>

                <div className="form-group">
                    <label htmlFor="levelId">Nivel:</label>
                    <select
                        id="levelId"
                        name="levelId"
                        value={formData.levelId}
                        onChange={handleChange}
                        required
                    >
                        <option value="">Seleccione un nivel</option>
                        {levels.map(level => (
                            <option key={level.idLevel} value={String(level.idLevel)}>
                                {level.nameLevel}
                            </option>
                        ))}
                    </select>
                </div>

                {/* Botones de acción */}
                <div className="form-buttons">
                    <button type="submit" className="submit-update-button">
                        Crear Rutina
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
                onConfirm={handleConfirmCreate}
                message={modalMessage}
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

export default CreateLearningRoutine;