import React, { useState } from 'react';
import { useNavigate, Link } from 'react-router-dom';
import { useAuth } from '../context/AuthContext';
import './Login.css';

const Login = () => {
  const [email, setEmail] = useState('');
  const [password, setPassword] = useState('');
  const [role, setRole] = useState('CLIENT');
  const [error, setError] = useState('');
  const [loading, setLoading] = useState(false);
  const { login } = useAuth();
  const navigate = useNavigate();

  const handleSubmit = async (e) => {
    e.preventDefault();
    setError('');
    setLoading(true);

    const result = await login(email, password, role);

    if (result.success) {
      navigate('/commandes');
    } else {
      setError(result.message || 'Erreur de connexion');
    }

    setLoading(false);
  };

  return (
    <div className="login-container">
      <div className="login-card">
        <h2>Connexion</h2>
        <form onSubmit={handleSubmit}>
          <div className="form-group">
            <label>Rôle</label>
            <div className="role-selector">
              <button
                type="button"
                className={role === 'CLIENT' ? 'active' : ''}
                onClick={() => setRole('CLIENT')}
              >
                Client
              </button>
              <button
                type="button"
                className={role === 'LIVREUR' ? 'active' : ''}
                onClick={() => setRole('LIVREUR')}
              >
                Livreur
              </button>
            </div>
          </div>

          <div className="form-group">
            <label htmlFor="email">Email</label>
            <input
              type="email"
              id="email"
              value={email}
              onChange={(e) => setEmail(e.target.value)}
              required
              placeholder="exemple@email.com"
            />
          </div>

          <div className="form-group">
            <label htmlFor="password">Mot de passe</label>
            <input
              type="password"
              id="password"
              value={password}
              onChange={(e) => setPassword(e.target.value)}
              required
              placeholder="••••••••"
            />
          </div>

          {error && <div className="error-message">{error}</div>}

          <button type="submit" className="btn btn-primary" disabled={loading} style={{ width: '100%' }}>
            {loading ? 'Connexion...' : 'Se connecter'}
          </button>
        </form>

        <div style={{ marginTop: '20px', textAlign: 'center' }}>
          <p>
            Vous n'avez pas de compte ?{' '}
            <Link to="/signup" style={{ color: '#007bff', textDecoration: 'none' }}>
              S'inscrire
            </Link>
          </p>
        </div>


      </div>
    </div>
  );
};

export default Login;

