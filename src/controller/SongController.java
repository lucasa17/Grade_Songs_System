package controller;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
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
            JOptionPane.showMessageDialog(this, "Problema ao salvar album.", "Erro", JOptionPane.ERROR_MESSAGE);
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
        List<Song> songs = new ArrayList<>(songDAO.findAll());

        Comparator<Song> gradeComparator = (s1, s2) -> {
            Double g1 = (double) s1.getGrade();
            Double g2 = (double) s2.getGrade();

            return Double.compare(g1, g2);
        };

        // Critério secundário para empates (nome, case-insensitive) — torna o resultado estável
        Comparator<Song> nameComparator = Comparator.comparing(
                s -> s.getName() == null ? "" : s.getName().toLowerCase(),
                Comparator.naturalOrder()
        );

        Comparator<Song> fullComparator = gradeComparator.thenComparing(nameComparator);

        songs.sort(fullComparator);

        if (!ascending) {
            Collections.reverse(songs);
        }

        return songs;
    }
    
    public List<Song> getSongsOrderedByName(boolean ascending) throws ModelException {
        List<Song> songs = songDAO.findAll();
        songs.sort((s1, s2) -> {
            String n1 = s1.getName();
            String n2 = s2.getName();
            return ascending ? n1.compareToIgnoreCase(n2) : n2.compareToIgnoreCase(n1);
        });
        return songs;
    }

    public List<Song> getSongsOrderedByArtist(boolean ascending) throws ModelException {
        List<Song> songs = songDAO.findAll();
        songs.sort((s1, s2) -> {
            String a1 = searchNameArtist(s1.getAlbum());
            String a2 = searchNameArtist(s2.getAlbum());
            return ascending ? a1.compareToIgnoreCase(a2) : a2.compareToIgnoreCase(a1);
        });
        return songs;
    }
    
    public List<Song> getSongsOrderedByAlbum(boolean ascending) throws ModelException {
        List<Song> songs = songDAO.findAll();
        songs.sort((s1, s2) -> {
            String al1 = (s1.getAlbum() != null) ? s1.getAlbum().getName() : "";
            String al2 = (s2.getAlbum() != null) ? s2.getAlbum().getName() : "";
            return ascending ? al1.compareToIgnoreCase(al2) : al2.compareToIgnoreCase(al1);
        });
        return songs;
    }

    public List<Song> getSongsOrderedByFeatures(boolean ascending) throws ModelException {
        List<Song> songs = songDAO.findAll();
        songs.sort((s1, s2) -> {
            String f1 = (s1.getFeatures() != null) ? s1.getFeatures() : "";
            String f2 = (s2.getFeatures() != null) ? s2.getFeatures() : "";
            return ascending ? f1.compareToIgnoreCase(f2) : f2.compareToIgnoreCase(f1);
        });
        return songs;
    }

}
