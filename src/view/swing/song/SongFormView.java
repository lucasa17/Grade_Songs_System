package view.swing.song;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.util.List;
import javax.swing.*;

import controller.SongController;
import model.Album;
import model.Artist;
import model.Song;
import model.ModelException;

public class SongFormView extends JDialog implements ISongFormView {

    private final JComboBox<Album> albumBox = new JComboBox<>();
    private final JTextField nameField = new JTextField(50);
    private final JTextField gradeField = new JTextField(3);
    private final JTextField featureField = new JTextField(50);
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

        setTitle(isNew ? "Nova Música" : "Editar Música");
        setSize(400, 300);
        setLocationRelativeTo(parent);
        setLayout(new GridBagLayout());

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        gbc.gridx = 0; gbc.gridy = 0;
        add(new JLabel("Nome:"), gbc);
        gbc.gridx = 1;
        add(nameField, gbc);

        gbc.gridx = 0;gbc.gridy = 1;
        add(new JLabel("Album:"), gbc);
        gbc.gridx = 1;
        add(albumBox, gbc);

        gbc.gridx = 0; gbc.gridy = 2;
        add(new JLabel("Nota:"), gbc);
        gbc.gridx = 1;
        add(gradeField, gbc);

        gbc.gridx = 0;gbc.gridy = 3;
        add(new JLabel("Features:"), gbc);
        gbc.gridx = 1;
        add(featureField, gbc);

        JPanel btnPanel = new JPanel();
        btnPanel.add(saveButton);
        btnPanel.add(closeButton);

        gbc.gridx = 0;
        gbc.gridy = 4;
        gbc.gridwidth = 2;
        add(btnPanel, gbc);

        if (!isNew) {
            setSongInForm(song);
        }

        loadAlbums();

        saveButton.addActionListener(e -> controller.saveOrUpdate(isNew));
        closeButton.addActionListener(e -> close());
    }

    private void loadAlbums() {
        try {
            List<Album> albums = controller.findAllAlbums();
            albumBox.removeAllItems();
            for (Album a : albums) {
                albumBox.addItem(a);
            }
            albumBox.setEditable(true);
        } catch (ModelException e) {
            JOptionPane.showMessageDialog(this, "Erro ao carregar albuns: " + e.getMessage(), "Erro",
                    JOptionPane.ERROR_MESSAGE);
        }
    }

    @Override
    public Song getSongFromForm() {
        if (song == null) {
            song = new Song(0);
        }
        song.setName(nameField.getText());
        song.setFeatures(featureField.getText());

        try {
            int grade = Integer.parseInt(gradeField.getText());
            song.setGrade(grade);
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this,
                    "Ano inválido. Digite um número inteiro entre 0 e 100.",
                    "Erro",
                    JOptionPane.ERROR_MESSAGE);
            return null;
        }
        
        Object selectedAlbumItem = albumBox.getSelectedItem();
        if (selectedAlbumItem instanceof Album) {
            song.setAlbum((Album) selectedAlbumItem);
        }
        
        return song;
    }

    @Override
    public void setSongInForm(Song song) {    	
        nameField.setText(song.getName());
        gradeField.setText(String.valueOf(song.getGrade()));
        featureField.setText(song.getFeatures());

        Album songAlbum = song.getAlbum();
        if (songAlbum != null) {
            for (int i = 0; i < albumBox.getItemCount(); i++) {
                Album a = albumBox.getItemAt(i);
                if (a.getId() == songAlbum.getId()) { 
                	albumBox.setSelectedIndex(i);
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
