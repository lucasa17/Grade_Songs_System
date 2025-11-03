package view.swing.album;

import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.util.List;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;

import controller.AlbumController;
import model.Album;
import model.Artist;
import model.Collection;
import model.ModelException;
import model.data.ArtistDAO;
import model.data.CollectionDAO;
import model.data.DAOFactory;

public class AlbumFormView extends JDialog implements IAlbumFormView {
	private final JComboBox<Collection> collectionBox = new JComboBox<>();
	private final JTextField nameField = new JTextField(50);
	private final JTextField yearField = new JTextField(4);
	private final JComboBox<Artist> artistBox = new JComboBox<>();
	private final JButton saveButton = new JButton("Salvar");
	private final JButton closeButton = new JButton("Fechar");
	private AlbumController controller;
	private final boolean isNew;
	private final AlbumListView parent;
	private Album album;

    public AlbumFormView(AlbumListView parent, Album album, AlbumController controller) {
        super(parent, true);
        this.controller = controller;
        this.controller.setAlbumFormView(this);

        this.parent = parent;
        this.album = album;
        this.isNew = (album == null);

        setTitle(isNew ? "Novo Álbum" : "Editar Álbum");
        setSize(450, 350);
        setLocationRelativeTo(parent);
        setLayout(new GridBagLayout());
        setResizable(true);

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 10, 5, 10); 
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.anchor = GridBagConstraints.WEST;
        gbc.weightx = 1.0;

        gbc.gridx = 0;
        gbc.gridy = 0;
        add(new JLabel("Coleção:"), gbc);

        gbc.gridy++;
        collectionBox.setPreferredSize(new Dimension(300, 28));
        add(collectionBox, gbc);

        gbc.gridy++;
        add(new JLabel("Nome:"), gbc);
        gbc.gridy++;
        nameField.setPreferredSize(new Dimension(300, 28));
        add(nameField, gbc);

        gbc.gridy++;
        add(new JLabel("Ano:"), gbc);
        gbc.gridy++;
        yearField.setPreferredSize(new Dimension(100, 28));
        add(yearField, gbc);

        gbc.gridy++;
        add(new JLabel("Artista:"), gbc);
        gbc.gridy++;
        artistBox.setEditable(true);
        artistBox.setPreferredSize(new Dimension(300, 28));
        add(artistBox, gbc);

        JPanel btnPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 0));
        btnPanel.add(saveButton);
        btnPanel.add(closeButton);

        gbc.gridy++;
        gbc.fill = GridBagConstraints.NONE;
        gbc.anchor = GridBagConstraints.CENTER;
        add(btnPanel, gbc);

        if (!isNew) 
        	setAlbumInForm(album);

        loadCollections();
        loadArtists();

        saveButton.addActionListener(e -> controller.saveOrUpdate(isNew));
        closeButton.addActionListener(e -> close());
    }

    private void loadCollections() {
        try {
        	
            List<Collection> collections = controller.findAllCollections();
            collectionBox.removeAllItems();

            for (Collection c : collections) {
                collectionBox.addItem(c);
            }

            collectionBox.setEditable(true); 

        } catch (ModelException e) {
            JOptionPane.showMessageDialog(this,
                    "Erro ao carregar albuns: " + e.getMessage(),
                    "Erro",
                    JOptionPane.ERROR_MESSAGE);
        }
    }
    
    private void loadArtists() {
        try {
        	
            List<Artist> artists = controller.findAllArtists();
            artistBox.removeAllItems(); 

            for (Artist a : artists) {
                artistBox.addItem(a);
            }

            artistBox.setEditable(true); 

        } catch (ModelException e) {
            JOptionPane.showMessageDialog(this,
                    "Erro ao carregar albuns: " + e.getMessage(),
                    "Erro",
                    JOptionPane.ERROR_MESSAGE);
        }
    }
    
    @Override
    public Album getAlbumFromForm() {
        if (album == null) {
            album = new Album(0);
        }

        album.setName(nameField.getText());

        try {
            int ano = Integer.parseInt(yearField.getText());
            album.setYear(ano);
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this,
                    "Ano inválido. Digite um número inteiro.",
                    "Erro",
                    JOptionPane.ERROR_MESSAGE);
            return null;
        }

        Object selectedCollectionItem = collectionBox.getSelectedItem();
        Collection selectedCollection = null;

        if (selectedCollectionItem instanceof Collection) {
            selectedCollection = (Collection) selectedCollectionItem;
        } else if (selectedCollectionItem instanceof String) {
            String newName = (String) selectedCollectionItem;
            selectedCollection = new Collection(0);
            selectedCollection.setName(newName);
            
            selectedCollection = controller.searchCollection(selectedCollection);
        }
        album.setCollection(selectedCollection);      

        Object selectedArtistItem = artistBox.getSelectedItem();
        Artist selectedArtist = null;

        if (selectedArtistItem instanceof Artist) {
            selectedArtist = (Artist) selectedArtistItem;
        } else if (selectedArtistItem instanceof String) {
            String newName = (String) selectedArtistItem;
            selectedArtist = new Artist(0);
            selectedArtist.setName(newName);
   
            selectedArtist = controller.searchArtist(selectedArtist);
        }
        
        album.setArtist(selectedArtist);   

        return album;
    }

    @Override
    public void setAlbumInForm(Album album) {
        nameField.setText(album.getName());
        yearField.setText(String.valueOf(album.getYear()));
        
        Collection albumCollection = album.getCollection();
        if (albumCollection != null) {
            for (int i = 0; i < collectionBox.getItemCount(); i++) {
                Collection c = collectionBox.getItemAt(i);
                if (c.getId() == albumCollection.getId()) { // compara pelo ID
                    collectionBox.setSelectedIndex(i);
                    break;
                }
            }
        }

        Artist albumArtist = album.getArtist();
        if (albumArtist != null) {
            for (int i = 0; i < artistBox.getItemCount(); i++) {
                Artist a = artistBox.getItemAt(i);
                if (a.getId() == albumArtist.getId()) { 
                    artistBox.setSelectedIndex(i);
                    break;
                }
            }
        }
    }

    @Override
    public void showInfoMessage(String msg) {
        JOptionPane.showMessageDialog(this, msg, "Informação", JOptionPane.INFORMATION_MESSAGE);
    }

    @Override
    public void showErrorMessage(String msg) {
        JOptionPane.showMessageDialog(this, msg, "Erro", JOptionPane.ERROR_MESSAGE);
    }

    @Override
    public void close() {
        parent.refresh();
        dispose();
    }
}
