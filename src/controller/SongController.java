package controller;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Stream;
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
    
    public List<Song> searchSongs(String name, String artist, String album) throws ModelException {
        List<Song> allSongs = songDAO.findAll(); 
        List<Song> filteredSongs = new ArrayList<>();

        for (Song s : allSongs) {
            boolean matches = true;
            if (name != null && !name.isEmpty() &&
                !s.getName().toLowerCase().contains(name.toLowerCase())) {
                matches = false;
            }

            String artistName = searchNameArtist(s.getAlbum());
            if (artist != null && !artist.isEmpty() &&
                (artistName == null || !artistName.toLowerCase().contains(artist.toLowerCase()))) {
                matches = false;
            }

            if (album != null && !album.isEmpty() &&
                (s.getAlbum() == null || !s.getAlbum().getName().toLowerCase().contains(album.toLowerCase()))) {
                matches = false;
            }

            if (matches) {
                filteredSongs.add(s);
            }
        }
        return filteredSongs;
    }

    public List<Song> getSongsOrderedByGrade(boolean ascending) throws ModelException {
        List<Song> songs = songDAO.findAll();
        songs.sort((s1, s2) -> {
            double g1 = s1.getGrade();
            double g2 = s2.getGrade();
            return ascending ? Double.compare(g1, g2) : Double.compare(g2, g1);
        });
        return songs;
    }


}
