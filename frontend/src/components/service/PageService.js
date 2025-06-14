import axios from "axios";

class PageService {
    static BASE_URL = "http://localhost:8080";

    static async getHomePage() {
        try {
            const response = await axios.get(`${PageService.BASE_URL}/auth/homepage`);
            return response.data;
        } catch (err) {
            console.error("Error obteniendo la página de inicio: ", err);
            throw err;
        }
    }

    static async searchBar(query) {
        try {
            const response = await axios.get(`${PageService.BASE_URL}?search=${query}`)
            return response.data;
        } catch (err) {
            console.error("Error buscando palabras o frases: ", err);
            throw err;
        }
    }

    static async getPhraseOrWord(content) {
        try {
            const response = await axios.get(`${PageService.BASE_URL}?phraseword=${content}`)
            return response.data;
        } catch (err) {
            console.error("Error buscando la frase o palabra: ", err);
            throw err;
        }
    }

}
export default PageService;