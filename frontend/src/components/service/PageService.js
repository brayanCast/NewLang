import axiosInstance from "../../utils/AxiosInstances";

class PageService {
    //static BASE_URL = process.env.REACT_APP_API_URL + "/auth/search"; //URL base PARA DESPLIEGUE
    static BASE_URL = "http://localhost:8080/auth/search"; //URL base PARA DESARROLLO


    static async searchBar(query) {
        try {
            const response = await axiosInstance.get(`${this.BASE_URL}/suggestions`, {
                params: { query: query }
            });
            return response.data;
        } catch (err) {
            console.error("Error buscando las palabras o frases: ", err);
            throw err;
        }
    }

    

    static async getPhraseOrWord(query) {
        try {
            const response = await axiosInstance.get(`${this.BASE_URL}/global`, {
                params: {query: query}
            });
            return response.data;
        } catch (err) {
            console.error("Error buscando la frase o palabra: ", err);
            throw err;
        }
    }

    
}
export default PageService;