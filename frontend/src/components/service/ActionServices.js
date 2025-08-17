import axiosInstance from "../../utils/AxiosInstances";

class ActionSevices {

    static BASE_URL = "http://localhost:8080";

    static async getCategoryList() {

        try {
            const response = await axiosInstance.get(`${this.BASE_URL}/category/auth/get-all-categories`);
            return response.data;

        } catch (err) {
            throw err;
        }
    }

    static async getLevelList() {

        try {
            const response = await axiosInstance.get(`${this.BASE_URL}/level/auth/get-all-levels`);
            return response.data;

        } catch (err) {
            throw err;
        }
    }

    static async createWord(wordData) {
        try {
            const response = await axiosInstance.post(`${this.BASE_URL}/admin/word/create`, wordData);
            return response.data;
        } catch (err) {
            throw err;
        }
    }

    static async createExpression(expressionData) {
        try {
            const response = await axiosInstance.post(`${this.BASE_URL}/admin/expression/create`, expressionData);
            return response.data;
        } catch (err) {
            throw err;
        }
    }
}

export default ActionSevices;