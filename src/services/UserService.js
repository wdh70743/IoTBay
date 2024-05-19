import axios from "axios";

class UserService {
    BASE_URL = `http://localhost:8080/api/`;

    register(user){
        return axios.post(this.BASE_URL + 'register', user)
    }

    login(email, password){
        return axios.post(this.BASE_URL + 'login', { email, password })
    }

    loginStaff(email, password){
        return axios.post(this.BASE_URL + 'login-staff', { email, password })
    }

    loginAdmin(email, password){
        return axios.post(this.BASE_URL + 'login-admin', {email, password})
    }

    update(id, user){
        return axios.put(this.BASE_URL + `user/${id}`, user)
    }

    delete(id){
        return axios.delete(this.BASE_URL + `user/${id}`)
    }

    logout(loginDetails) {
        return axios.post(`${this.BASE_URL}logout-user`, loginDetails);
    }

    logoutStaff(loginDetails) {
        return axios.post(`${this.BASE_URL}logout-staff`, loginDetails);
    }

    logoutAdmin(loginDetails) {
        return axios.post(`${this.BASE_URL}logout-admin`, loginDetails);
    }

    retrieve(id){
        return axios.get(`${this.BASE_URL}user/${id}`);
    }
    
    retrieveLog(id){
        return axios.get(`${this.BASE_URL}user/${id}/logs`);
    }
    

}

const userService = new UserService()

export default userService;