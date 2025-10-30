package controller;

import java.util.List;

import model.Album;
import model.ModelException;
import model.data.AlbumDAO;
import model.data.DAOFactory;
import view.swing.album.IAlbumFormView;
import view.swing.album.IAlbumListView;

public class AlbumController {
	private final AlbumDAO albumDAO = DAOFactory.createAlbumDAO();
    private IAlbumListView albumListView;
    private IAlbumFormView albumFormView;

    public void loadAlbums() {
        try {
            List<Album> albums = albumDAO.findAll();
            albumListView.setAlbumList(albums);
        } catch (ModelException e) {
        	albumListView.showMessage("Erro ao carregar Albums: " + e.getMessage());
        }
    }
    
    // Salvar ou atualizar
    public void saveOrUpdate(boolean isNew) {
        Album album = albumFormView.getAlbumFromForm();

        try {
        	album.validate();    	
            //album.setCollection(collection);
        } catch (IllegalArgumentException e) {
        	albumFormView.showErrorMessage("Erro de validação: " + e.getMessage());
            return;
        }

        try {
            if (isNew) {
            	albumDAO.save(album);
            } else {
            	albumDAO.update(album);
            }
            albumFormView.showInfoMessage("Coleção salvo com sucesso!");
            albumFormView.close();
        } catch (ModelException e) {
        	albumFormView.showErrorMessage("Erro ao salvar: " + e.getMessage());
        }
    }

    // Excluir
    public void deleteAlbum(Album album) {
        try {
            albumDAO.delete(album);
            albumListView.showMessage("Coleção excluída!");
        } catch (ModelException e) {
        	albumListView.showMessage("Erro ao excluir: " + e.getMessage());
        }
    }

    public void setAlbumFormView(IAlbumFormView albumFormView) {
        this.albumFormView = albumFormView;
    }

    public void setAlbumListView(IAlbumListView albumListView) {
        this.albumListView = albumListView;
    }
}
