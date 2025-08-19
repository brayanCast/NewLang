import axios from "axios";
import axiosInstance from "../../utils/AxiosInstances";

class ActionServices {

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

    static async getAllWords() {
        try {
            const response = await axiosInstance.get(`${this.BASE_URL}/auth/word/get-all-words`);
            return response.data;
        } catch (err) {
            throw err;
        }
    }

    static async getAllExpressions() {
        try {
            const response = await axiosInstance.get(`${this.BASE_URL}/auth/expression/get-all-expressions`);
            return response.data;
        } catch (err) {
            throw err;
        }
    }

    static async getWordById(wordId) {
        try {
            const response = await axiosInstance.get(`${this.BASE_URL}/auth/word/get-word/${wordId}`);
            return response.data;
        } catch (err){
            throw err;
        }
    }


    static async getExpressionById(expressionId) {
        try {
            const response = await axiosInstance.get(`${this.BASE_URL}/auth/expression/get-expression/${expressionId}`);
            return response.data;
        } catch (err) {
            throw err;
        }
    }

    static async updateWordById(wordId, wordData) {
        try {
            const response = await axiosInstance.put(`${this.BASE_URL}/admin/word/update-word/${wordId}`, wordData);
            return response.data;
        } catch (err) {
            throw err;
        }
    }

    static async updateExpressionById(expressionId, expressionData) {
        try {
            const response = await axiosInstance.put(`${this.BASE_URL}/admin/expression/update-expression/${expressionId}`, expressionData);
            return response.data;
        } catch (err) {
            throw err;
        }
    }

    static async deleteWordById(wordId) {
        try {
            const response = await axiosInstance.delete(`${this.BASE_URL}/admin/word/delete-by-id/${wordId}`);
            return response.data;
        } catch (err) {
            throw err;
        }
    }

    static async deleteExpressionById(expressionId) {
        try {
            const response = await axiosInstance.delete(`${this.BASE_URL}/admin/expression/delete-by-id/${expressionId}`)
            return response.data;
        } catch (err) {
            throw err;
        }
    }
}

export default ActionServices;