import LandingPage from "./pages/LandingPage";
import LoginPage from "./pages/LoginPage";
import MainPage from "./pages/MainPage";
import RegisterPage from "./pages/RegisterPage";
import ProfilePage from "./pages/ProfilePage";
import CreateProductPage from "./pages/CreateProductPage";
import ManageProductPage from "./pages/ManageProductPage";
import AdminPage from "./pages/AdminPage";


const routes = [
    { path: '/', component: <LandingPage /> },
    { path: '/login', component: <LoginPage /> },
    { path: '/register', component: <RegisterPage /> },
    { path: '/main', component: <MainPage /> },
    { path: '/profile', component: <ProfilePage /> },
    { path: '/create', component: <CreateProductPage /> },
    { path: '/edit/:id', component: <ManageProductPage /> },
    { path: '/admin', component: <AdminPage /> },
];

export default routes;
