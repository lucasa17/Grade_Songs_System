package controller;

import java.util.List;

import javax.swing.JDialog;
import javax.swing.JOptionPane;

import model.Song;
import model.Album;
import model.Artist;
import model.Collection;
import model.Feature;
import model.ModelException;
import model.User;
import model.data.SongDAO;
import model.data.AlbumDAO;
import model.data.ArtistDAO;
import model.data.CollectionDAO;
import model.data.DAOFactory;
import model.data.FeatureDAO;
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
    
    public List<Album> findAllAlbums() throws ModelException {
        AlbumDAO albumDAO = DAOFactory.createAlbumDAO();
        return albumDAO.findAll();
    }
    
    public List<Feature> findAllFeatures() throws ModelException {
        try {
            return featureDAO.findAll();
        } catch (Exception e) {
            throw new ModelException("Erro ao buscar features: " + e.getMessage(), e);
        }
    }
    
	AlbumDAO albumDAO = DAOFactory.createAlbumDAO();
	 public Album searchAlbum(Album album) {
    	try {
			boolean condition = albumDAO.searchByName(album.getName());
			if(condition == true)
				return albumDAO.findByName(album.getName());
		} catch (ModelException e) {
			JOptionPane.showMessageDialog(this,
                    "Problema ao buscar album.",
                    "Erro",
                    JOptionPane.ERROR_MESSAGE);			
		}
    	return saveAlbum(album);
	 }
	 
    private Album saveAlbum(Album album) {
        try {
        	albumDAO.save(album);
        	album = albumDAO.findByName(album.getName());
		} catch (ModelException e) {
			JOptionPane.showMessageDialog(this,
                    "Problema ao salvar album.",
                    "Erro",
                    JOptionPane.ERROR_MESSAGE);			
		}
        return album;
    }
    
    FeatureDAO featureDAO = DAOFactory.createFeatureDAO();
    public Feature searchFeature(Feature feature) {
        try {
            // Tenta buscar a feature pelo nome
            Feature found = featureDAO.findByName(feature.getArtist().getName());
            if (found != null) {
                return found; // se encontrada, retorna
            }
            // Se não encontrada, salva e retorna a nova feature
            return saveFeature(feature);
        } catch (ModelException e) {
            JOptionPane.showMessageDialog(null,
                    "Problema ao buscar ou salvar feature: " + e.getMessage(),
                    "Erro",
                    JOptionPane.ERROR_MESSAGE);
            return null;
        }
    }

    private Feature saveFeature(Feature feature) {
        try {
            featureDAO.save(feature);
            // Retorna a feature salva (recuperando do DB para pegar o ID)
            return featureDAO.findByName(feature.getArtist().getName());
        } catch (ModelException e) {
            JOptionPane.showMessageDialog(null,
                    "Problema ao salvar feature: " + e.getMessage(),
                    "Erro",
                    JOptionPane.ERROR_MESSAGE);
            return null;
        }
    }

}
