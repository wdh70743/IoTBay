import LandingPage from "./pages/LandingPage";
import LoginPage from "./pages/LoginPage";
import MainPage from "./pages/MainPage";
import RegisterPage from "./pages/RegisterPage";
import ProfilePage from "./pages/ProfilePage";
import CreateProductPage from "./pages/CreateProductPage";
import ManageProductPage from "./pages/ManageProductPage";

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
    {
        path: '/profile',
        component: <ProfilePage />
    },
    {
        path: '/Create',
        component: <CreateProductPage />
    },
    {
        path: '/Manage',
        component: <ManageProductPage />
    }
]

export default routes;