import axios from "axios";

class OrderService {
    BASE_URL = `http://localhost:8080/api`;

    createOrder(orderData) {
        if (!orderData.userId) {
            console.error("User ID is undefined");
            return Promise.reject(new Error("User ID is undefined"));
        }
        return axios.post(`${this.BASE_URL}/order/create`, orderData)
            .then(response => response.data)
            .catch(error => {
                console.error('Failed to create order:', error);
                throw error;
            });
    }

    deleteOrder(orderId) {
        return axios.delete(`${this.BASE_URL}/order/${orderId}`);
    }

    updateOrder(orderId, orderData) {
        return axios.put(`${this.BASE_URL}/order/${orderId}`, orderData)
            .then(response => response.data)
            .catch(error => {
                console.error('Failed to update order:', error);
                throw error;
            });
    }

    getOrderById(orderId) {
        return axios.get(`${this.BASE_URL}/order/${orderId}`)
            .then(response => response.data)
            .catch(error => {
                console.error('Failed to get order:', error);
                throw error;
            });
    }

    listOrdersByUser(userId) {
        return axios.get(`${this.BASE_URL}/order/user/${userId}`)
            .then(response => response.data)
            .catch(error => {
                console.error('Failed to list orders:', error);
                throw error;
            });
    }
}

const orderService = new OrderService();
export default orderService;
