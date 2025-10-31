package controller;

import java.util.List;

import javax.swing.JDialog;
import javax.swing.JOptionPane;

import model.Song;
import model.Artist;
import model.Collection;
import model.ModelException;
import model.User;
import model.data.SongDAO;
import model.data.ArtistDAO;
import model.data.CollectionDAO;
import model.data.DAOFactory;
import model.data.UserDAO;
import view.swing.song.ISongFormView;
import view.swing.song.ISongListView;

public class SongController extends JDialog  {
	private final SongDAO songDAO = DAOFactory.createSongDAO();
    private ISongListView songListView;
    private ISongFormView songFormView;

    public void loadSongs() {
        try {
            List<Song> songs = songDAO.findAll();
            songListView.setSongList(songs);
        } catch (ModelException e) {
        	songListView.showMessage("Erro ao carregar Songs: " + e.getMessage());
        }
    }
    
    // Salvar ou atualizar
    public void saveOrUpdate(boolean isNew) {
        Song song = songFormView.getSongFromForm();

        try {
        	song.validate();    	
            //song.setCollection(collection);
        } catch (IllegalArgumentException e) {
        	songFormView.showErrorMessage("Erro de validação: " + e.getMessage());
            return;
        }

        try {
            if (isNew) {
            	songDAO.save(song);
            } else {
            	songDAO.update(song);
            }
            songFormView.showInfoMessage("Song salvo com sucesso!");
            songFormView.close();
        } catch (ModelException e) {
        	songFormView.showErrorMessage("Erro ao salvar: " + e.getMessage());
        }
    }

    // Excluir
    public void deleteSong(Song song) {
        try {
            songDAO.delete(song);
            songListView.showMessage("Song excluído!");
        } catch (ModelException e) {
        	songListView.showMessage("Erro ao excluir: " + e.getMessage());
        }
    }

    public void setSongFormView(ISongFormView songFormView) {
        this.songFormView = songFormView;
    }

    public void setSongListView(ISongListView songListView) {
        this.songListView = songListView;
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
