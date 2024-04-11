import { BrowserRouter as Router, Route, Routes } from 'react-router-dom';
import routes from './routes'

function App() {
  return (
    <Router>
      <Routes>
        {routes.map((route) => {
          return <Route key={route.path} path={route.path} element={route.component} />
        })}
      </Routes>
    </Router>
  );
}

export default App;