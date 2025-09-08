import React, { useState, useEffect } from 'react';
import { useNavigate } from 'react-router-dom';
import ActionServices from '../service/ActionServices';
import Navbar from '../common/Navbar';
import Footer from '../common/Footer';
import { useLoading } from '../context/LoadingContext';
import ConfirmModal from '../common/ConfirmModal';
import SuccessModal from '../common/SuccessModal';
import ErrorModal from '../common/ErrorModal';
import '../../styles/CreateWordExpression.css';

function CreateWordExpression() {
  const { startLoading, stopLoading } = useLoading();
  const [formType, setFormType] = useState('word'); // 'word' o 'expression'
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

  // Estado para las opciones de los campos de selección
  const [categories, setCategories] = useState([]);
  const [levels, setLevels] = useState([]);

  // Estado para mostrar mensajes al usuario
  const [showConfirmModal, setShowConfirmModal] = useState(false);
  const [showSuccessModal, setShowSuccessModal] = useState(false);
  const [showErrorModal, setShowErrorModal] = useState(false);
  const [modalMessage, setModalMessage] = useState('');

  // Obtener categorías y niveles cuando el componente se monta
  useEffect(() => {
    const fetchData = async () => {
      try {
        const [fetchedCategories, fetchedLevels] = await Promise.all([
          ActionServices.getCategoryList(),
          ActionServices.getLevelList()
        ]);

        setCategories(fetchedCategories);
        setLevels(fetchedLevels);
        console.log("Datos actualizados:", new Date().toLocaleTimeString());

      } catch (error) {
        console.error("Error actualizando los datos:", error);
        setModalMessage('Falla al cargar las categorías o niveles.');
        setShowErrorModal(true);
      }
    };
    fetchData();

    const intervalId = setInterval(fetchData, 60000);

    return () => {
      clearInterval(intervalId);
      console.log("Intervalo de actualización detenido");
    }
  }, []);

  // Manejador para los cambios en los campos de entrada
  const handleChange = (e) => {
    const { name, value } = e.target;
    setFormData(prevState => ({ ...prevState, [name]: value }));
  };

  // Manejador para el envío del formulario
  const handleSubmit = async (e) => {
    e.preventDefault();
    setShowConfirmModal(true);
  }

  const handleConfirmCreate = async () => {
    setShowConfirmModal(true);

    try {
      startLoading();
      let response;
      let payload;

      if (formType === 'word') {
        const { englishWord, spanishWord, imageUrl, categoryId, levelId } = formData;
        // Validar que los campos de 'word' no estén vacíos

        payload = { englishWord, spanishWord, imageUrl, categoryId: Number(categoryId), levelId: Number(levelId) };
        response = await ActionServices.createWord(payload);
        console.log("Creación de la palabra con la información", payload);
        setModalMessage("Palabra creada exitosamente", response.message);

      } else if (formType === 'expression') {
        const { englishExpression, spanishExpression, imageUrl, categoryId, levelId } = formData;
        payload = { englishExpression, spanishExpression, imageUrl, categoryId: Number(categoryId), levelId: Number(levelId) };
        response = await ActionServices.createExpression(payload);
        console.log("Creación de la expresión con la información", payload);
        setModalMessage("Expresión creada exitosamente", response.message);

      } else {
        console.error("Tipo de formulario no válido o no se generó correctamente");
      }

      // Reiniciar los campos del formulario
      setFormData({
        englishWord: '',
        spanishWord: '',
        englishExpression: '',
        spanishExpression: '',
        imageUrl: '',
        categoryId: '',
        levelId: ''
      });

      setShowSuccessModal(true);

    } catch (error) {
      console.log('Falla al crear el elemento.', error.message);
      let errorMessage = 'Ocurrio un error al crear el elemento';

      if (error.response) {
        const status = error.response.status;
        const responseMessage = error.response.data.message || 'Ocurrió un error desconocido';

        if (status === 409) {
          errorMessage = responseMessage;
        } else {
          errorMessage = `Error ${status}: ${errorMessage}`;
        }

      } else if (error.request) {
        errorMessage = 'Error al conectar con el servidor, por favor intente más tarde.';

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
      navigate('/list-word-expression');
    }, 1000);
  };



  return (
    <div className="form-container">
      <Navbar />
      <h2>Crear Nueva Palabra/Expresión</h2>
      <form onSubmit={handleSubmit}>
        {/* Selección del tipo */}
        <div className="form-group">
          <label htmlFor="formType">Tipo:</label>
          <select id="formType" name="formType" value={formType} onChange={(e) => setFormType(e.target.value)}>
            <option value="word">Palabra</option>
            <option value="expression">Expresión</option>
          </select>
        </div>

        {/* Campos dinámicos según el tipo de formulario */}
        {formType === 'word' ? (
          <>
            <div className="form-group">
              <label htmlFor="englishWord">Palabra en Inglés:</label>
              <input type="text" id="englishWord" name="englishWord" value={formData.englishWord} onChange={handleChange} required />
            </div>
            <div className="form-group">
              <label htmlFor="spanishWord">Palabra en Español:</label>
              <input type="text" id="spanishWord" name="spanishWord" value={formData.spanishWord} onChange={handleChange} required />
            </div>
          </>
        ) : (
          <>
            <div className="form-group">
              <label htmlFor="englishExpression">Expresión en Inglés:</label>
              <input type="text" id="englishExpression" name="englishExpression" value={formData.englishExpression} onChange={handleChange} required />
            </div>
            <div className="form-group">
              <label htmlFor="spanishExpression">Expresión en Español:</label>
              <input type="text" id="spanishExpression" name="spanishExpression" value={formData.spanishExpression} onChange={handleChange} required />
            </div>
          </>
        )}

        {/* Campos comunes */}
        <div className="form-group">
          <label htmlFor="imageUrl">URL de Imagen:</label>
          <input type="url" id="imageUrl" name="imageUrl" value={formData.imageUrl} onChange={handleChange} required />
        </div>

        <div className="form-group">
          <label htmlFor="categoryId">Categoría:</label>
          <select id="categoryId" name="categoryId" value={formData.categoryId} onChange={handleChange} required>
            <option value="">Seleccionar una Categoría</option>
            {categories.map(category => (
              <option key={category.idCategory} value={category.idCategory}>
                {category.nameCategory}
              </option>
            ))}
          </select>
        </div>

        <div className="form-group">
          <label htmlFor="levelId">Nivel:</label>
          <select id="levelId" name="levelId" value={formData.levelId} onChange={handleChange} required>
            <option value="">Seleccionar un Nivel</option>
            {levels.map(level => (
              <option key={level.idLevel} value={level.idLevel}>
                {level.nameLevel}
              </option>
            ))}
          </select>
        </div>

        <button type="submit" className="submit-button">Crear</button>

      </form>

      {/* Modales para confirmación, éxito y error */}
      <ConfirmModal
        isOpen={showConfirmModal}
        onClose={() => setShowConfirmModal(false)}
        onConfirm={handleConfirmCreate}
        message="¿Estás seguro de que deseas crear este elemento?"
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

export default CreateWordExpression;