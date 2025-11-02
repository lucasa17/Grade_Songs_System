package controller;

import java.util.List;
import javax.swing.JDialog;
import javax.swing.JOptionPane;

import model.Song;
import model.Album;
import model.Artist;
import model.ModelException;
import model.data.SongDAO;
import model.data.AlbumDAO;
import model.data.ArtistDAO;
import model.data.DAOFactory;
import view.swing.song.ISongFormView;
import view.swing.song.ISongListView;

public class SongController extends JDialog {

    private final SongDAO songDAO = DAOFactory.createSongDAO();
    private AlbumDAO albumDAO = DAOFactory.createAlbumDAO();
    private ArtistDAO artistDAO = DAOFactory.createArtistDAO();


    private ISongListView songListView;
    private ISongFormView songFormView;

    public void loadSongs() {
        try {
            List<Song> songs = songDAO.findAll();
            songListView.setSongList(songs);
        } catch (ModelException e) {
            songListView.showMessage("Erro ao carregar Músicas: " + e.getMessage());
        }
    }

    // Salvar ou atualizar uma música
    public void saveOrUpdate(boolean isNew) {
        Song song = songFormView.getSongFromForm();

        try {
            song.validate();
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
            songFormView.showInfoMessage("Música salvo com sucesso!");
            songFormView.close();
        } catch (ModelException e) {
            songFormView.showErrorMessage("Erro ao salvar: " + e.getMessage());
        }
    }

    // Excluir música
    public void deleteSong(Song song) {
        try {
            songDAO.delete(song);
            songListView.showMessage("Música excluída!");
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
        return albumDAO.findAll();
    }

    public Album searchAlbum(Album album) {
        try {
            boolean condition = albumDAO.searchByName(album.getName());
            if (condition) {
                return albumDAO.findByName(album.getName());
            }
        } catch (ModelException e) {
            JOptionPane.showMessageDialog(this, "Problema ao buscar album.", "Erro",
                    JOptionPane.ERROR_MESSAGE);
        }
        return saveAlbum(album);
    }

    private Album saveAlbum(Album album) {
        try {
            albumDAO.save(album);
            return albumDAO.findByName(album.getName());
        } catch (ModelException e) {
            JOptionPane.showMessageDialog(this, "Problema ao salvar album.", "Erro",
                    JOptionPane.ERROR_MESSAGE);
            return album;
        }
    }
    
    public String searchNameArtist(Album album) {
            try {
				album = albumDAO.findById(album.getId());
			} catch (ModelException e) {
	            JOptionPane.showMessageDialog(this, "Problema ao buscar album.", "Erro",
	                    JOptionPane.ERROR_MESSAGE);
			}
            
            Artist artist = new Artist(0);
            try {
				artist = artistDAO.findById(album.getArtist().getId());
			} catch (ModelException e) {
	            JOptionPane.showMessageDialog(this, "Problema ao buscar artista.", "Erro",
	                    JOptionPane.ERROR_MESSAGE);
			}
            
        return artist.getName();
    }
}
