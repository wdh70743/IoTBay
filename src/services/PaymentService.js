import axios from 'axios';

class PaymentService {
    BASE_URL = 'http://localhost:8080/api/payments';

    createPayment(paymentData) {
        return axios.post(`${this.BASE_URL}/create`, paymentData)
            .then(response => response.data)
            .catch(error => {
                console.error('Failed to create payment:', error);
                throw error;
            });
    }

    getPaymentById(paymentId) {
        return axios.get(`${this.BASE_URL}/${paymentId}`)
            .then(response => response.data) 
            .catch(error => {
                console.error('Failed to retrieve payment:', error);
                throw error;
            });
    }

    updatePayment(paymentId, paymentData) {
        return axios.put(`${this.BASE_URL}/${paymentId}`, paymentData)
            .then(response => response.data)
            .catch(error => {
                console.error('Failed to update payment:', error);
                throw error;
            });
    }

    getPaymentsByOrder(orderId) {
        return axios.get(`${this.BASE_URL}/order/${orderId}`);
    }

    deletePayment(paymentId) {
        return axios.delete(`${this.BASE_URL}/${paymentId}`);
    }

    searchPaymentsByDate(startDate, endDate) {
        const formattedStartDate = `${startDate}T00:00:00`;
        const formattedEndDate = `${endDate}T23:59:59`;
        return axios.get(`${this.BASE_URL}/search`, {
            params: { start: formattedStartDate, end: formattedEndDate }
        }).then(response => response.data)
          .catch(error => {
              console.error('Failed to search payments:', error.response ? error.response.data : error.message);
              throw error;
          });
    }
}

const paymentService = new PaymentService();
export default paymentService;
