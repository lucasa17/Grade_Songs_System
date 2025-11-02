package controller;

import java.util.List;

import model.Collection;
import model.ModelException;
import model.Session;
import model.data.CollectionDAO;
import model.data.DAOFactory;
import view.swing.collection.ICollectionFormView;
import view.swing.collection.ICollectionListView;

public class CollectionController {
	private final CollectionDAO collectionDAO = DAOFactory.createCollectionDAO();
    private ICollectionListView collectionListView;
    private ICollectionFormView collectionFormView;

    public void loadCollections() {
        try {
            List<Collection> collections = collectionDAO.findAll();
            collectionListView.setCollectionList(collections);
        } catch (ModelException e) {
        	collectionListView.showMessage("Erro ao carregar Coleções: " + e.getMessage());
        }
    }
    
    // Salvar ou atualizar
    public void saveOrUpdate(boolean isNew) {
        Collection collection = collectionFormView.getCollectionFromForm();

        try {
        	collection.validate();
        	
        	if (Session.isLogged()) {
                collection.setUser(Session.getLoggedUser());
            } else {
                collectionFormView.showErrorMessage("Nenhum usuário logado!");
                return;
            }
        	
        } catch (IllegalArgumentException e) {
        	collectionFormView.showErrorMessage("Erro de validação: " + e.getMessage());
            return;
        }

        try {
            if (isNew) {
            	collectionDAO.save(collection);
            } else {
            	collectionDAO.update(collection);
            }
            collectionFormView.showInfoMessage("Coleção salvo com sucesso!");
            collectionFormView.close();
        } catch (ModelException e) {
        	collectionFormView.showErrorMessage("Erro ao salvar: " + e.getMessage());
        }
    }

    // Excluir
    public void deleteCollection(Collection collection) {
        try {
            collectionDAO.delete(collection);
            collectionListView.showMessage("Coleção excluída!");
        } catch (ModelException e) {
        	collectionListView.showMessage("Erro ao excluir: " + e.getMessage());
        }
    }

    public void setCollectionFormView(ICollectionFormView collectionFormView) {
        this.collectionFormView = collectionFormView;
    }

    public void setCollectionListView(ICollectionListView collectionListView) {
        this.collectionListView = collectionListView;
    }
}
