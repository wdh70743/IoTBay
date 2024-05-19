import axios from "axios";

class AdminService {
    BASE_URL = `http://localhost:8080/api/admin`;

    deleteUser(id, userId) {
        return axios.delete(`${this.BASE_URL}/adminDelete/${id}?requestedBy=${userId}`);
    }

    getUserById(id) {
        return axios.get(`${this.BASE_URL}/adminRetrive/${id}`);
    }

    deactivateUser(id, userId) {
        return axios.get(`${this.BASE_URL}/adminDeactivate/${id}?requestedBy=${userId}`);
    }

    activateUser(id, userId) {
        return axios.get(`${this.BASE_URL}/adminActivate/${id}?requestedBy=${userId}`);
    }

    createUser(adminId, email, password, firstName, lastName, phoneNumber, role, address) {
        const data = {
            adminId,
            email,
            password,
            firstName,
            lastName,
            phoneNumber,
            role,
            address,
        };
        return axios.post(`${this.BASE_URL}/adminCreate`, data);
    }

    updateUser(id, adminId, email, password, firstName, lastName, phoneNumber, role, address) {
        const data = {
            adminId,
            email,
            password,
            firstName,
            lastName,
            phoneNumber,
            role,
            address,
        };
        return axios.put(`${this.BASE_URL}/adminUpdate/${id}`, data);
    }

}

const adminService = new AdminService();

export default adminService;
