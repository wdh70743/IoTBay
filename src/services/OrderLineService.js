import axios from "axios";

class OrderLineService {
    BASE_URL = `http://localhost:8080/api`;

    addOrderLineItems(orderId, lineItems) {
        return axios.post(`${this.BASE_URL}/order-line-items/create`, {
            orderId,
            lineItems
        });
    }

    deleteOrderLineItem(itemId) {
        return axios.delete(`${this.BASE_URL}/order-line-items/${itemId}`);
    }

    updateOrderLineItem(itemId, itemData) {
        return axios.put(`${this.BASE_URL}/order-line-items/${itemId}`, itemData);
    }

    getOrderLineItems(orderId) {
        return axios.get(`${this.BASE_URL}/order-line-items/order/${orderId}`);
    }
}

const orderLineService = new OrderLineService();
export default orderLineService;