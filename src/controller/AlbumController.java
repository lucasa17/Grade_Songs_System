package controller;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
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
    
    public List<Album> searchAlbums(String name, String artist, String collection) throws ModelException {
        List<Album> allAlbums = albumDAO.findAll(); 
        List<Album> filteredAlbums = new ArrayList<>();

        for (Album a : allAlbums) {
            boolean matches = true;

            if (name != null && !name.isEmpty() &&
                !a.getName().toLowerCase().contains(name.toLowerCase())) {
                matches = false;
            }

            if (artist != null && !artist.isEmpty() &&
                (a.getArtist() == null || !a.getArtist().getName().toLowerCase().contains(artist.toLowerCase()))) {
                matches = false;
            }

            if (collection != null && !collection.isEmpty() &&
                (a.getCollection() == null || !a.getCollection().getName().toLowerCase().contains(collection.toLowerCase()))) {
                matches = false;
            }

            if (matches) {
                filteredAlbums.add(a);
            }
        }

        return filteredAlbums;
    }
    
    public List<Album> getAlbumsOrderedByYear(boolean ascending) throws ModelException {
        List<Album> albums = new ArrayList<>(albumDAO.findAll());

        Comparator<Album> gradeComparator = (s1, s2) -> {
            Double g1 = (double) s1.getYear();
            Double g2 = (double) s2.getYear();

            return Double.compare(g1, g2);
        };

        // Critério secundário para empates (nome, case-insensitive) — torna o resultado estável
        Comparator<Album> nameComparator = Comparator.comparing(
                s -> s.getName() == null ? "" : s.getName().toLowerCase(),
                Comparator.naturalOrder()
        );

        Comparator<Album> fullComparator = gradeComparator.thenComparing(nameComparator);

        albums.sort(fullComparator);

        if (!ascending) {
            Collections.reverse(albums);
        }

        return albums;
    }
    
    public List<Album> getAlbumsOrderedByName(boolean ascending) throws ModelException {
        List<Album> albums = albumDAO.findAll();
        albums.sort((s1, s2) -> {
            String n1 = s1.getName();
            String n2 = s2.getName();
            return ascending ? n1.compareToIgnoreCase(n2) : n2.compareToIgnoreCase(n1);
        });
        return albums;
    }

    public List<Album> getAlbumsOrderedByArtist(boolean ascending) throws ModelException {
        List<Album> albums = albumDAO.findAll();
        albums.sort((s1, s2) -> {
            String a1 = s1.getArtist().getName();
            String a2 = s2.getArtist().getName();
            return ascending ? a1.compareToIgnoreCase(a2) : a2.compareToIgnoreCase(a1);
        });
        return albums;
    }

    public List<Album> getAlbumsOrderedByCollection(boolean ascending) throws ModelException {
        List<Album> albums = albumDAO.findAll();
        albums.sort((s1, s2) -> {
            String f1 = (s1.getCollection().getName() != null) ? s1.getCollection().getName() : "";
            String f2 = (s2.getCollection().getName() != null) ? s2.getCollection().getName() : "";
            return ascending ? f1.compareToIgnoreCase(f2) : f2.compareToIgnoreCase(f1);
        });
        return albums;
    }

}
