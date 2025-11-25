import api from './api';

export const getCommandes = async (page = 0, size = 10, sortBy = 'dateCreation', sortDir = 'DESC') => {
  const response = await api.get('/api/commandes', {
    params: { page, size, sortBy, sortDir }
  });
  return response.data;
};

export const getCommandeById = async (id) => {
  const response = await api.get(`/api/commandes/${id}`);
  return response.data;
};

export const createCommande = async (commande) => {
  const response = await api.post('/api/commandes', commande);
  return response.data;
};

export const updateCommande = async (id, commande) => {
  const response = await api.put(`/api/commandes/${id}`, commande);
  return response.data;
};

export const deleteCommande = async (id) => {
  await api.delete(`/api/commandes/${id}`);
};

