package view.swing.album;

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

        setTitle(isNew ? "Novo Album" : "Editar Album");
        setSize(350, 220);
        setLocationRelativeTo(parent);
        setLayout(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5,5,5,5);
        gbc.fill = GridBagConstraints.HORIZONTAL;


        gbc.gridx = 0; gbc.gridy = 2;
        add(new JLabel("Coleção:"), gbc);
        gbc.gridx = 1;
        collectionBox.setEditable(true); 
        add(collectionBox, gbc);
       
        Object selectedItem = collectionBox.getSelectedItem();
        Collection selectedCollection = null;
        
        gbc.gridx = 0; gbc.gridy = 0;
        add(new JLabel("Nome:"), gbc);
        gbc.gridx = 1;
        add(nameField, gbc);
        
        gbc.gridx = 0; gbc.gridy = 1;
        add(new JLabel("Ano:"), gbc);
        gbc.gridx = 1;
        add(yearField, gbc);

        gbc.gridx = 0; gbc.gridy = 3;
        add(new JLabel("Artista:"), gbc);
        gbc.gridx = 1;
        artistBox.setEditable(true);
        add(artistBox, gbc);
        
        JPanel btnPanel = new JPanel();
        btnPanel.add(saveButton);
        btnPanel.add(closeButton);

        gbc.gridx = 0; gbc.gridy = 4; gbc.gridwidth = 2;
        add(btnPanel, gbc);

        if (!isNew) 
        	setAlbumInForm(album);

        carregarCollections();
        carregarArtists();

        saveButton.addActionListener(e -> controller.saveOrUpdate(isNew));
        closeButton.addActionListener(e -> close());
    }

    private void carregarCollections() {
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
    
    private void carregarArtists() {
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
