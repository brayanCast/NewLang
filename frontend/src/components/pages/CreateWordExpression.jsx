import React, { useState, useEffect } from 'react';
import ActionServices from '../service/ActionServices';
import Navbar from '../common/Navbar';
import Footer from '../common/Footer';
import { useLoading } from '../context/LoadingContext';
import '../../styles/CreateWordExpression.css';

function CreateWordExpression() {
  const { startLoading, stopLoading } = useLoading();
  const [formType, setFormType] = useState('word'); // 'word' o 'expression'
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
  const [message, setMessage] = useState('');
  const [isSuccess, setIsSuccess] = useState(false);


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
        setMessage('Falla al cargar las categorías o niveles.');
        setIsSuccess(false);
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
    setMessage('');
    setIsSuccess(false);
    startLoading();

    try {
      let response;
      let payload;
      if (formType === 'word') {
        const { englishWord, spanishWord, imageUrl, categoryId, levelId } = formData;
        // Validar que los campos de 'word' no estén vacíos

        payload = { englishWord, spanishWord, imageUrl, categoryId: Number(categoryId), levelId: Number(levelId) };
        response = await ActionServices.createWord(payload);
        console.log("Creación de la palabra con la información", payload);

      } else if (formType === 'expression') {
        const { englishExpression, spanishExpression, imageUrl, categoryId, levelId } = formData;
        payload = { englishExpression, spanishExpression, imageUrl, categoryId: Number(categoryId), levelId: Number(levelId) };
        response = await ActionServices.createExpression(payload);
        console.log("Creación de la expresión con la información", payload);

      } else {
        console.error("Tipo de formulario no válido o no se generó correctamente");
      }

      setMessage("creado exitosamente", response.message);
      setIsSuccess(true);
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

    } catch (error) {
      console.error("Error en el envío:", error);
      setIsSuccess(false);

      if (error.response) {
        const status = error.response.status;
        const errorMessage = error.response.data.message || 'Ocurrió un error desconocido';

        if (status === 409) {
          setMessage(`Error: ${errorMessage}`);
        } else {
          setMessage(`Error ${status}: ${errorMessage}`);
        }

      } else if (error.request) {
        setMessage('Error al conectar con el servidor, por favor intente más tarde.');

      } else {
        setMessage('Ocurrió un error al preparar la petición');
      }

    } finally {
      stopLoading();
    }
  };

  return (
    <div className="form-container">
      <Navbar />
      <h2>Crear Nueva Palabra/Expresión</h2>
      <form onSubmit={handleSubmit}>
        {/* Selección del tipo */}
        <div className="form-group">
          <label htmlFor="formType">Type:</label>
          <select id="formType" name="formType" value={formType} onChange={(e) => setFormType(e.target.value)}>
            <option value="word">Word</option>
            <option value="expression">Expression</option>
          </select>
        </div>

        {/* Campos dinámicos según el tipo de formulario */}
        {formType === 'word' ? (
          <>
            <div className="form-group">
              <label htmlFor="englishWord">English Word:</label>
              <input type="text" id="englishWord" name="englishWord" value={formData.englishWord} onChange={handleChange} required />
            </div>
            <div className="form-group">
              <label htmlFor="spanishWord">Spanish Word:</label>
              <input type="text" id="spanishWord" name="spanishWord" value={formData.spanishWord} onChange={handleChange} required />
            </div>
          </>
        ) : (
          <>
            <div className="form-group">
              <label htmlFor="englishExpression">English Expression:</label>
              <input type="text" id="englishExpression" name="englishExpression" value={formData.englishExpression} onChange={handleChange} required />
            </div>
            <div className="form-group">
              <label htmlFor="spanishExpression">Spanish Expression:</label>
              <input type="text" id="spanishExpression" name="spanishExpression" value={formData.spanishExpression} onChange={handleChange} required />
            </div>
          </>
        )}

        {/* Campos comunes */}
        <div className="form-group">
          <label htmlFor="imageUrl">Image URL</label>
          <input type="url" id="imageUrl" name="imageUrl" value={formData.imageUrl} onChange={handleChange} required />
        </div>

        <div className="form-group">
          <label htmlFor="categoryId">Category:</label>
          <select id="categoryId" name="categoryId" value={formData.categoryId} onChange={handleChange} required>
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
          <select id="levelId" name="levelId" value={formData.levelId} onChange={handleChange} required>
            <option value="">Select a Level</option>
            {levels.map(level => (
              <option key={level.idLevel} value={level.idLevel}>
                {level.nameLevel}
              </option>
            ))}
          </select>
        </div>

        <button type="submit" className="submit-button">Create</button>

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

export default CreateWordExpression;