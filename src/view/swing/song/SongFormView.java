package view.swing.song;

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

import controller.SongController;
import model.Song;
import model.Artist;
import model.Collection;
import model.ModelException;

public class SongFormView extends JDialog implements ISongFormView {
	private final JComboBox<Collection> collectionBox = new JComboBox<>();
	private final JTextField nameField = new JTextField(50);
	private final JTextField yearField = new JTextField(4);
	private final JComboBox<Artist> artistBox = new JComboBox<>();
	private final JButton saveButton = new JButton("Salvar");
	private final JButton closeButton = new JButton("Fechar");
	private SongController controller;
	private final boolean isNew;
	private final SongListView parent;
	private Song song;

    public SongFormView(SongListView parent, Song song, SongController controller) {
        super(parent, true);
        this.controller = controller;
        this.controller.setSongFormView(this);

        this.parent = parent;
        this.song = song;
        this.isNew = (song == null);

        setTitle(isNew ? "Novo Song" : "Editar Song");
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
        	setSongInForm(song);

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
    public Song getSongFromForm() {
        if (song == null) {
            song = new Song(0);
        }

        song.setName(nameField.getText());

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
        //song.setCollection(selectedCollection);      

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
        
        //song.setArtist(selectedArtist);   

        return song;
    }

    @Override
    public void setSongInForm(Song song) {
        nameField.setText(song.getName());
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
