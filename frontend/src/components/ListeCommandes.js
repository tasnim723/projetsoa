import React, { useState, useEffect } from 'react';
import { useNavigate } from 'react-router-dom';
import { useAuth } from '../context/AuthContext';
import { getCommandes, deleteCommande } from '../services/commandeService';
import './ListeCommandes.css';

const ListeCommandes = () => {
  const { user, logout } = useAuth();
  const navigate = useNavigate();

  const handleLogout = () => {
    logout();
    navigate('/login');
  };
  const [commandes, setCommandes] = useState([]);
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState('');
  const [currentPage, setCurrentPage] = useState(0);
  const [totalPages, setTotalPages] = useState(0);
  const [totalItems, setTotalItems] = useState(0);
  const [sortBy, setSortBy] = useState('dateCreation');
  const [sortDir, setSortDir] = useState('DESC');

  useEffect(() => {
    loadCommandes();
  }, [currentPage, sortBy, sortDir]);

  const loadCommandes = async () => {
    try {
      setLoading(true);
      const data = await getCommandes(currentPage, 10, sortBy, sortDir);
      setCommandes(data.commandes || []);
      setCurrentPage(data.currentPage || 0);
      setTotalPages(data.totalPages || 0);
      setTotalItems(data.totalItems || 0);
      setError('');
    } catch (err) {
      setError('Erreur lors du chargement des commandes');
      console.error(err);
    } finally {
      setLoading(false);
    }
  };

  const handleDelete = async (id) => {
    if (window.confirm('Êtes-vous sûr de vouloir supprimer cette commande ?')) {
      try {
        await deleteCommande(id);
        loadCommandes();
      } catch (err) {
        setError('Erreur lors de la suppression de la commande');
        console.error(err);
      }
    }
  };

  const handleSort = (field) => {
    if (sortBy === field) {
      setSortDir(sortDir === 'ASC' ? 'DESC' : 'ASC');
    } else {
      setSortBy(field);
      setSortDir('ASC');
    }
    setCurrentPage(0);
  };

  const getStatusBadge = (statut) => {
    const statusMap = {
      'EN_COURS': { class: 'badge-en-cours', label: 'En cours' },
      'LIVREE': { class: 'badge-livree', label: 'Livrée' },
      'ANNULEE': { class: 'badge-annulee', label: 'Annulée' }
    };
    const status = statusMap[statut] || { class: '', label: statut };
    return <span className={`badge ${status.class}`}>{status.label}</span>;
  };

  const formatDate = (dateString) => {
    if (!dateString) return '';
    const date = new Date(dateString);
    return date.toLocaleDateString('fr-FR', {
      year: 'numeric',
      month: '2-digit',
      day: '2-digit',
      hour: '2-digit',
      minute: '2-digit'
    });
  };

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
          <div style={{ display: 'flex', justifyContent: 'space-between', alignItems: 'center', marginBottom: '20px' }}>
            <h2>Liste des Commandes</h2>
            {user?.role === 'LIVREUR' && (
              <button
                className="btn btn-primary"
                onClick={() => navigate('/commandes/nouvelle')}
              >
                + Nouvelle Commande
              </button>
            )}
          </div>

          {error && <div className="error-message">{error}</div>}

          {loading ? (
            <div style={{ textAlign: 'center', padding: '40px' }}>Chargement...</div>
          ) : commandes.length === 0 ? (
            <div className="empty-state">
              <h3>Aucune commande trouvée</h3>
              <p>Vous n'avez pas encore de commandes.</p>
            </div>
          ) : (
            <>
              <div style={{ overflowX: 'auto' }}>
                <table className="table">
                  <thead>
                    <tr>
                      <th onClick={() => handleSort('id')} style={{ cursor: 'pointer' }}>
                        ID {sortBy === 'id' && (sortDir === 'ASC' ? '↑' : '↓')}
                      </th>
                      <th onClick={() => handleSort('description')} style={{ cursor: 'pointer' }}>
                        Description {sortBy === 'description' && (sortDir === 'ASC' ? '↑' : '↓')}
                      </th>
                      <th>Adresse</th>
                      <th onClick={() => handleSort('statut')} style={{ cursor: 'pointer' }}>
                        Statut {sortBy === 'statut' && (sortDir === 'ASC' ? '↑' : '↓')}
                      </th>
                      {user?.role === 'CLIENT' && <th>Livreur</th>}
                      {user?.role === 'LIVREUR' && <th>Client</th>}
                      <th onClick={() => handleSort('dateCreation')} style={{ cursor: 'pointer' }}>
                        Date {sortBy === 'dateCreation' && (sortDir === 'ASC' ? '↑' : '↓')}
                      </th>
                      {user?.role === 'LIVREUR' && <th>Actions</th>}
                    </tr>
                  </thead>
                  <tbody>
                    {commandes.map((commande) => (
                      <tr key={commande.id}>
                        <td>{commande.id}</td>
                        <td>{commande.description}</td>
                        <td>{commande.adresseLivraison}</td>
                        <td>{getStatusBadge(commande.statut)}</td>
                        {user?.role === 'CLIENT' && <td>{commande.livreurNom}</td>}
                        {user?.role === 'LIVREUR' && <td>{commande.clientNom}</td>}
                        <td>{formatDate(commande.dateCreation)}</td>
                        {user?.role === 'LIVREUR' && (
                          <td>
                            <div style={{ display: 'flex', gap: '5px' }}>
                              <button
                                className="btn btn-success"
                                style={{ padding: '5px 10px', fontSize: '14px' }}
                                onClick={() => navigate(`/commandes/modifier/${commande.id}`)}
                              >
                                Modifier
                              </button>
                              <button
                                className="btn btn-danger"
                                style={{ padding: '5px 10px', fontSize: '14px' }}
                                onClick={() => handleDelete(commande.id)}
                              >
                                Supprimer
                              </button>
                            </div>
                          </td>
                        )}
                      </tr>
                    ))}
                  </tbody>
                </table>
              </div>

              {totalPages > 1 && (
                <div className="pagination">
                  <button
                    onClick={() => setCurrentPage(0)}
                    disabled={currentPage === 0}
                  >
                    ««
                  </button>
                  <button
                    onClick={() => setCurrentPage(currentPage - 1)}
                    disabled={currentPage === 0}
                  >
                    «
                  </button>
                  <span>
                    Page {currentPage + 1} sur {totalPages} ({totalItems} commandes)
                  </span>
                  <button
                    onClick={() => setCurrentPage(currentPage + 1)}
                    disabled={currentPage >= totalPages - 1}
                  >
                    »
                  </button>
                  <button
                    onClick={() => setCurrentPage(totalPages - 1)}
                    disabled={currentPage >= totalPages - 1}
                  >
                    »»
                  </button>
                </div>
              )}
            </>
          )}
        </div>
      </div>
    </div>
  );
};

export default ListeCommandes;

