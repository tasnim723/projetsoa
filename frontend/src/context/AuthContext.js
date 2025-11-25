import React, { createContext, useState, useContext, useEffect } from 'react';
import api from '../services/api';

const AuthContext = createContext();

export const useAuth = () => {
  const context = useContext(AuthContext);
  if (!context) {
    throw new Error('useAuth must be used within an AuthProvider');
  }
  return context;
};

export const AuthProvider = ({ children }) => {
  const [user, setUser] = useState(null);
  const [token, setToken] = useState(localStorage.getItem('token'));
  const [loading, setLoading] = useState(true);

  useEffect(() => {
    if (token) {
      const userData = localStorage.getItem('user');
      if (userData) {
        setUser(JSON.parse(userData));
        api.defaults.headers.common['Authorization'] = `Bearer ${token}`;
      }
    }
    setLoading(false);
  }, [token]);

  const login = async (email, password, role) => {
    try {
      const response = await api.post('/api/auth/login', {
        email,
        motDePasse: password,
        role
      });

      const { token: newToken, id, email: userEmail, nom, role: userRole } = response.data;
      
      setToken(newToken);
      setUser({ id, email: userEmail, nom, role: userRole });
      
      localStorage.setItem('token', newToken);
      localStorage.setItem('user', JSON.stringify({ id, email: userEmail, nom, role: userRole }));
      
      api.defaults.headers.common['Authorization'] = `Bearer ${newToken}`;
      
      return { success: true };
    } catch (error) {
      console.error('Erreur de connexion:', error);
      
      // Gestion des erreurs réseau
      if (error.code === 'ECONNREFUSED' || error.message === 'Network Error' || error.code === 'ERR_NETWORK' || !error.response) {
        console.error('Détails de l\'erreur réseau:', {
          code: error.code,
          message: error.message,
          response: error.response,
          config: error.config
        });
        return {
          success: false,
          message: 'Impossible de se connecter au serveur. Vérifiez que le back-end est démarré sur http://localhost:8080 et que CORS est configuré correctement.'
        };
      }
      
      // Gestion des erreurs HTTP
      const serverMessage =
        typeof error.response?.data === 'string'
          ? error.response.data
          : error.response?.data?.message;
      
      return {
        success: false,
        message: serverMessage || error.message || 'Erreur de connexion'
      };
    }
  };

  const register = async (nom, email, password, role) => {
    try {
      const response = await api.post('/api/auth/register', {
        nom,
        email,
        motDePasse: password,
        role
      });

      const { token: newToken, id, email: userEmail, nom: userName, role: userRole } = response.data;
      
      setToken(newToken);
      setUser({ id, email: userEmail, nom: userName, role: userRole });
      
      localStorage.setItem('token', newToken);
      localStorage.setItem('user', JSON.stringify({ id, email: userEmail, nom: userName, role: userRole }));
      
      api.defaults.headers.common['Authorization'] = `Bearer ${newToken}`;
      
      return { success: true };
    } catch (error) {
      console.error('Erreur d\'inscription:', error);
      
      // Gestion des erreurs réseau
      if (error.code === 'ECONNREFUSED' || error.message === 'Network Error' || error.code === 'ERR_NETWORK' || !error.response) {
        console.error('Détails de l\'erreur réseau:', {
          code: error.code,
          message: error.message,
          response: error.response,
          config: error.config
        });
        return {
          success: false,
          message: 'Impossible de se connecter au serveur. Vérifiez que le back-end est démarré sur http://localhost:8080 et que CORS est configuré correctement.'
        };
      }
      
      // Gestion des erreurs HTTP
      const serverMessage =
        typeof error.response?.data === 'string'
          ? error.response.data
          : error.response?.data?.message;
      
      return {
        success: false,
        message: serverMessage || error.message || 'Erreur lors de l\'inscription'
      };
    }
  };

  const logout = () => {
    setToken(null);
    setUser(null);
    localStorage.removeItem('token');
    localStorage.removeItem('user');
    delete api.defaults.headers.common['Authorization'];
  };

  const isAuthenticated = !!token && !!user;

  const value = {
    user,
    token,
    login,
    register,
    logout,
    isAuthenticated,
    loading
  };

  return <AuthContext.Provider value={value}>{children}</AuthContext.Provider>;
};

