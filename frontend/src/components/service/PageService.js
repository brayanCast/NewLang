import axiosInstance from "../../utils/AxiosInstances";

class PageService {
    static BASE_URL = "http://localhost:8080/auth/search";


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