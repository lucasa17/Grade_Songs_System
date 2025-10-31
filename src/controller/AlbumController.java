package controller;

import java.util.List;

import javax.swing.JDialog;
import javax.swing.JOptionPane;

import model.Album;
import model.Artist;
import model.Collection;
import model.ModelException;
import model.User;
import model.data.AlbumDAO;
import model.data.ArtistDAO;
import model.data.CollectionDAO;
import model.data.DAOFactory;
import model.data.UserDAO;
import view.swing.album.IAlbumFormView;
import view.swing.album.IAlbumListView;

public class AlbumController extends JDialog  {
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
    
    public List<Collection> findAllCollections() throws ModelException {
        CollectionDAO collectionDAO = DAOFactory.createCollectionDAO();
        return collectionDAO.findAll();
    }
    
    public List<Artist> findAllArtists() throws ModelException {
        ArtistDAO artistDAO = DAOFactory.createArtistDAO();
        return artistDAO.findAll();
    }
    
    public Artist saveArtist(Artist artist) {
    	ArtistDAO artistDAO = DAOFactory.createArtistDAO();
        try {
			artistDAO.save(artist);
			artist = artistDAO.findByName(artist.getName());
		} catch (ModelException e) {
			JOptionPane.showMessageDialog(this,
                    "Problema ao salvar artista.",
                    "Erro",
                    JOptionPane.ERROR_MESSAGE);			
		}
        return artist;
    }
    
    public Collection saveCollection(Collection collection) {
    	CollectionDAO collectionDAO = DAOFactory.createCollectionDAO();
        try {
        	collectionDAO.save(collection);
			collection = collectionDAO.findByName(collection.getName());
		} catch (ModelException e) {
			JOptionPane.showMessageDialog(this,
                    "Problema ao salvar coleção.",
                    "Erro",
                    JOptionPane.ERROR_MESSAGE);			
		}
        return collection;
    }
}
