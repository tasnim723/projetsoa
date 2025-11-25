import React from 'react';
import { BrowserRouter as Router, Routes, Route, Navigate } from 'react-router-dom';
import Login from './components/Login';
import SignUp from './components/SignUp';
import ListeCommandes from './components/ListeCommandes';
import FormulaireCommande from './components/FormulaireCommande';
import { AuthProvider, useAuth } from './context/AuthContext';
import './App.css';

const PrivateRoute = ({ children }) => {
  const { isAuthenticated } = useAuth();
  return isAuthenticated ? children : <Navigate to="/login" />;
};

function App() {
  return (
    <AuthProvider>
      <Router>
        <div className="App">
          <Routes>
            <Route path="/login" element={<Login />} />
            <Route path="/signup" element={<SignUp />} />
            <Route
              path="/commandes"
              element={
                <PrivateRoute>
                  <ListeCommandes />
                </PrivateRoute>
              }
            />
            <Route
              path="/commandes/nouvelle"
              element={
                <PrivateRoute>
                  <FormulaireCommande />
                </PrivateRoute>
              }
            />
            <Route
              path="/commandes/modifier/:id"
              element={
                <PrivateRoute>
                  <FormulaireCommande />
                </PrivateRoute>
              }
            />
            <Route path="/" element={<Navigate to="/commandes" />} />
          </Routes>
        </div>
      </Router>
    </AuthProvider>
  );
}

export default App;

