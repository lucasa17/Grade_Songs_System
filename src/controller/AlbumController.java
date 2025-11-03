package controller;

import java.util.List;

import javax.swing.JDialog;
import javax.swing.JOptionPane;

import model.Album;
import model.Artist;
import model.Collection;
import model.ModelException;
import model.data.AlbumDAO;
import model.data.ArtistDAO;
import model.data.CollectionDAO;
import model.data.DAOFactory;
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
        	albumListView.showMessage("Erro ao carregar Albuns: " + e.getMessage());
        }
    }
    
    public void saveOrUpdate(boolean isNew) {
        Album album = albumFormView.getAlbumFromForm();

        try {
        	album.validate();    	
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
            albumFormView.showInfoMessage("Album salvo com sucesso!");
            albumFormView.close();
        } catch (ModelException e) {
        	albumFormView.showErrorMessage("Erro ao salvar: " + e.getMessage());
        }
    }

    // Excluir
    public void deleteAlbum(Album album) {
        try {
            albumDAO.delete(album);
            albumListView.showMessage("Album excluído!");
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
    
	ArtistDAO artistDAO = DAOFactory.createArtistDAO();
	
	 public Artist searchArtist(Artist artist) {
    	try {
			boolean condition = artistDAO.searchByName(artist.getName());
			if(condition == true)
				return artistDAO.findByName(artist.getName());
		} catch (ModelException e) {
			JOptionPane.showMessageDialog(this,
                    "Problema ao buscar artista.",
                    "Erro",
                    JOptionPane.ERROR_MESSAGE);			
		}
    	return saveArtist(artist);
	 }
	 
    private Artist saveArtist(Artist artist) {
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
    
	CollectionDAO collectionDAO = DAOFactory.createCollectionDAO();
    public Collection searchCollection(Collection collection) {
    	try {
			boolean condition = collectionDAO.searchByName(collection.getName());
			if(condition == true)
				return collectionDAO.findByName(collection.getName());
		} catch (ModelException e) {
			JOptionPane.showMessageDialog(this,
                    "Problema ao buscar coleção.",
                    "Erro",
                    JOptionPane.ERROR_MESSAGE);			
		}
    	return saveCollection(collection);
    }

    public Collection saveCollection(Collection collection) {
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
