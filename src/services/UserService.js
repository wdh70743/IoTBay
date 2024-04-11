import axios from "axios";

class UserService {
    BASE_URL = `http://localhost:8080/api/`;

    register(user){
        return axios.post(this.BASE_URL + 'register', user);
    }

    login(email, password){
        return axios.post(this.BASE_URL + 'login', { email, password })
    }

    loginStaff(email, password, role){
        return axios.post(this.BASE_URL + 'login-staff', { email, password })
    }
}

const userService = new UserService()

export default userService;