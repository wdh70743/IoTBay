import axios from "axios";

class ProductService {
    BASE_URL = `http://localhost:8080/api/product`;

    create(product) {
        return axios.post(`${this.BASE_URL}/create`, product);
    }

    delete(id, userId) {
        return axios.delete(`${this.BASE_URL}/${id}?requestedBy=` + userId);
    }

    update(id, product) {
        return axios.put(`${this.BASE_URL}/${id}`, product);
    }

    getProduct() {
        return axios.get(this.BASE_URL);
    }

    getProductByType(type) {
        return axios.get(`${this.BASE_URL}/by-type?keyword=`+type);
    }

    getProductByName(name) {
        return axios.get(`${this.BASE_URL}/by-name?keyword=`+name);
    }
}

const productService = new ProductService();

export default productService;
