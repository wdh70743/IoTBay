import LandingPage from "./pages/LandingPage";
import LoginPage from "./pages/LoginPage";
import MainPage from "./pages/MainPage";
import RegisterPage from "./pages/RegisterPage";

const routes = [
    {
        path: '/',
        component: <LandingPage />
    },
    {
        path: '/login',
        component: <LoginPage />
    },
    {
        path: '/register',
        component: <RegisterPage />
    },
    {
        path: '/main',
        component: <MainPage />
    },
]

export default routes;