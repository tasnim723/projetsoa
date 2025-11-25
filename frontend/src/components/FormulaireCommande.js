import React, { useState, useEffect } from 'react';
import { useNavigate, useParams } from 'react-router-dom';
import { useAuth } from '../context/AuthContext';
import { getCommandeById, createCommande, updateCommande } from '../services/commandeService';
import api from '../services/api';
import './FormulaireCommande.css';

const FormulaireCommande = () => {
  const { id } = useParams();
  const { user, logout } = useAuth();
  const navigate = useNavigate();

  const handleLogout = () => {
    logout();
    navigate('/login');
  };
  const [loading, setLoading] = useState(false);
  const [error, setError] = useState('');
  const [success, setSuccess] = useState('');
  const [clients, setClients] = useState([]);
  const [formData, setFormData] = useState({
    description: '',
    adresseLivraison: '',
    statut: 'EN_COURS',
    clientId: ''
  });

  useEffect(() => {
    if (user?.role === 'LIVREUR') {
      loadClients();
    }
    if (id) {
      loadCommande();
    }
  }, [id, user]);

  const loadClients = async () => {
    try {
      // Pour simplifier, on récupère tous les clients
      // Dans un vrai projet, on aurait un endpoint dédié
      const response = await api.get('/api/clients');
      setClients(response.data || []);
    } catch (err) {
      console.error('Erreur lors du chargement des clients:', err);
      // Si l'endpoint n'existe pas, on utilise une liste vide
      setClients([]);
    }
  };

  const loadCommande = async () => {
    try {
      setLoading(true);
      const commande = await getCommandeById(id);
      setFormData({
        description: commande.description || '',
        adresseLivraison: commande.adresseLivraison || '',
        statut: commande.statut || 'EN_COURS',
        clientId: commande.clientId || ''
      });
    } catch (err) {
      setError('Erreur lors du chargement de la commande');
      console.error(err);
    } finally {
      setLoading(false);
    }
  };

  const handleSubmit = async (e) => {
    e.preventDefault();
    setError('');
    setSuccess('');
    setLoading(true);

    try {
      const commandeData = {
        description: formData.description,
        adresseLivraison: formData.adresseLivraison,
        statut: formData.statut,
        clientId: parseInt(formData.clientId)
      };

      if (id) {
        await updateCommande(id, commandeData);
        setSuccess('Commande modifiée avec succès');
      } else {
        await createCommande(commandeData);
        setSuccess('Commande créée avec succès');
      }

      setTimeout(() => {
        navigate('/commandes');
      }, 1500);
    } catch (err) {
      setError(err.response?.data?.message || 'Erreur lors de l\'enregistrement de la commande');
      console.error(err);
    } finally {
      setLoading(false);
    }
  };

  const handleChange = (e) => {
    setFormData({
      ...formData,
      [e.target.name]: e.target.value
    });
  };

  if (user?.role !== 'LIVREUR') {
    return (
      <div>
        <div className="header">
          <div className="header-content">
            <h1>Gestion de Livraison</h1>
            <div className="header-actions">
              <span>Connecté en tant que: <strong>{user?.nom}</strong> ({user?.role})</span>
              <button className="btn btn-secondary" onClick={handleLogout}>
                Déconnexion
              </button>
            </div>
          </div>
        </div>
        <div className="container">
          <div className="card">
            <div className="error-message">Vous n'êtes pas autorisé à accéder à cette page.</div>
            <button className="btn btn-primary" onClick={() => navigate('/commandes')} style={{ marginTop: '20px' }}>
              Retour à la liste
            </button>
          </div>
        </div>
      </div>
    );
  }

  return (
    <div>
      <div className="header">
        <div className="header-content">
          <h1>Gestion de Livraison</h1>
          <div className="header-actions">
            <span>Connecté en tant que: <strong>{user?.nom}</strong> ({user?.role})</span>
            <button className="btn btn-secondary" onClick={logout}>
              Déconnexion
            </button>
          </div>
        </div>
      </div>

      <div className="container">
        <div className="card">
          <h2>{id ? 'Modifier la Commande' : 'Nouvelle Commande'}</h2>

          {error && <div className="error-message">{error}</div>}
          {success && <div className="success-message">{success}</div>}

          <form onSubmit={handleSubmit}>
            <div className="form-group">
              <label htmlFor="clientId">Client *</label>
              <select
                id="clientId"
                name="clientId"
                value={formData.clientId}
                onChange={handleChange}
                required
              >
                <option value="">Sélectionner un client</option>
                {clients.map((client) => (
                  <option key={client.id} value={client.id}>
                    {client.nom} ({client.email})
                  </option>
                ))}
              </select>
              {clients.length === 0 && (
                <small style={{ color: '#666', display: 'block', marginTop: '5px' }}>
                  Aucun client disponible. Veuillez créer un client d'abord.
                </small>
              )}
            </div>

            <div className="form-group">
              <label htmlFor="description">Description *</label>
              <textarea
                id="description"
                name="description"
                value={formData.description}
                onChange={handleChange}
                required
                placeholder="Description de la commande"
              />
            </div>

            <div className="form-group">
              <label htmlFor="adresseLivraison">Adresse de livraison *</label>
              <input
                type="text"
                id="adresseLivraison"
                name="adresseLivraison"
                value={formData.adresseLivraison}
                onChange={handleChange}
                required
                placeholder="Adresse complète de livraison"
              />
            </div>

            {id && (
              <div className="form-group">
                <label htmlFor="statut">Statut</label>
                <select
                  id="statut"
                  name="statut"
                  value={formData.statut}
                  onChange={handleChange}
                >
                  <option value="EN_COURS">En cours</option>
                  <option value="LIVREE">Livrée</option>
                  <option value="ANNULEE">Annulée</option>
                </select>
              </div>
            )}

            <div className="actions">
              <button
                type="button"
                className="btn btn-secondary"
                onClick={() => navigate('/commandes')}
                disabled={loading}
              >
                Annuler
              </button>
              <button
                type="submit"
                className="btn btn-primary"
                disabled={loading}
              >
                {loading ? 'Enregistrement...' : id ? 'Modifier' : 'Créer'}
              </button>
            </div>
          </form>
        </div>
      </div>
    </div>
  );
};

export default FormulaireCommande;

