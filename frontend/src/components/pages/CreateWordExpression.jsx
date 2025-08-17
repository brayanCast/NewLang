import React, { useState, useEffect } from "react";
import ActionServices from "../service/ActionServices";
import "../../styles/CreateWordExpression.css";

function CreateWordExpression() {

    const createTextService = {
  // Función simulada para obtener categorías de la base de datos
  fetchCategories: async () => {
    console.log('Fetching categories...');
    return new Promise(resolve => {
      setTimeout(() => {
        resolve([
          { id: 1, name: 'Animals' },
          { id: 2, name: 'Food' },
          { id: 3, name: 'Technology' },
          { id: 4, name: 'Travel' },  
        ]);
      }, 500); // Simula un retraso de red
    });
  },
  
  // Función simulada para obtener niveles de la base de datos
  fetchLevels: async () => {
    console.log('Fetching levels...');
    return new Promise(resolve => {
      setTimeout(() => {
        resolve([
          { id: 1, name: 'Beginner' },
          { id: 2, name: 'Intermediate' },
          { id: 3, name: 'Advanced' },
        ]);
      }, 500); // Simula un retraso de red
    });
  },

  // Función simulada para crear una nueva palabra
  createWord: async (wordData) => {
    console.log('Creating word with data:', wordData);
    return new Promise(resolve => {
      setTimeout(() => {
        resolve({ success: true, message: 'Word created successfully!' });
      }, 1000);
    });
  },

  // Función simulada para crear una nueva expresión
  createExpression: async (expressionData) => {
    console.log('Creating expression with data:', expressionData);
    return new Promise(resolve => {
      setTimeout(() => {
        resolve({ success: true, message: 'Expression created successfully!' });
      }, 1000);
    });
  }
};

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
  const [isLoading, setIsLoading] = useState(true);

  // Obtener categorías y niveles cuando el componente se monta
  useEffect(() => {
    const fetchData = async () => {
      try {
        const [fetchedCategories, fetchedLevels] = await Promise.all([
          createTextService.fetchCategories(),
          createTextService.fetchLevels()
        ]);
        setCategories(fetchedCategories);
        setLevels(fetchedLevels);
      } catch (error) {
        console.error("Error fetching data:", error);
        setMessage('Failed to load categories or levels.');
        setIsSuccess(false);
      } finally {
        setIsLoading(false);
      }
    };
    fetchData();
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

    try {
      let response;
      if (formType === 'word') {
        const { englishWord, spanishWord, imageUrl, categoryId, levelId } = formData;
        // Validar que los campos de 'word' no estén vacíos
        if (!englishWord || !spanishWord || !categoryId || !levelId) {
            setMessage('Please fill out all fields.');
            setIsSuccess(false);
            return;
        }
        const payload = { englishWord, spanishWord, imageUrl, categoryId: Number(categoryId), levelId: Number(levelId) };
        response = await createTextService.createWord(payload);
      } else { // formType es 'expression'
        const { englishExpression, spanishExpression, imageUrl, categoryId, levelId } = formData;
        // Validar que los campos de 'expression' no estén vacíos
        if (!englishExpression || !spanishExpression || !categoryId || !levelId) {
            setMessage('Please fill out all fields.');
            setIsSuccess(false);
            return;
        }
        const payload = { englishExpression, spanishExpression, imageUrl, categoryId: Number(categoryId), levelId: Number(levelId) };
        response = await createTextService.createExpression(payload);
      }
      
      if (response.success) {
        setMessage(response.message);
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
      } else {
        setMessage('Creation failed. Please try again.');
        setIsSuccess(false);
      }
    } catch (error) {
      console.error("Submission error:", error);
      setMessage('An error occurred during submission.');
      setIsSuccess(false);
    }
  };

  if (isLoading) {
    return <div className="loading-container"><p>Loading form data...</p></div>;
  }

    return (
        <div className="form-container">
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
                    <input type="url" id="imageUrl" name="imageUrl" value={formData.imageUrl} onChange={handleChange} required/>
                </div>

                <div className="form-group">
                    <label htmlFor="categoryId">Category:</label>
                    <select id="categoryId" name="categoryId" value={formData.categoryId} onChange={handleChange} required>
                        <option value="">Select a Category</option>
                        {categories.map(category => (
                            <option key={category.id} value={category.id}>
                                {category.name}
                            </option>
                        ))}
                    </select>
                </div>

                <div className="form-group">
                    <label htmlFor="levelId">Level:</label>
                    <select id="levelId" name="levelId" value={formData.levelId} onChange={handleChange} required>
                        <option value="">Select a Level</option>
                        {levels.map(level => (
                            <option key={level.id} value={level.id}>
                                {level.name}
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
        </div>
    );
}

export default CreateWordExpression;