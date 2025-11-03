package view.swing.song;

import java.awt.*;
import java.util.List;
import javax.swing.*;

import controller.SongController;
import model.Album;
import model.Song;
import model.ModelException;

public class SongFormView extends JDialog implements ISongFormView {

    private final JComboBox<Album> albumBox = new JComboBox<>();
    private final JTextField nameField = new JTextField();
    private final JTextField gradeField = new JTextField();
    private final JTextField featureField = new JTextField();
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
        setSize(450, 350);
        setLocationRelativeTo(parent);
        setLayout(new GridBagLayout());
        setResizable(true);

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        gbc.gridx = 0; gbc.gridy = 0;
        add(new JLabel("Nome:"), gbc);
        gbc.gridy++;
        add(nameField, gbc);

        gbc.gridy++;
        add(new JLabel("Álbum:"), gbc);
        gbc.gridy++;
        add(albumBox, gbc);

        gbc.gridy++;
        add(new JLabel("Nota (0–100):"), gbc);
        gbc.gridy++;
        add(gradeField, gbc);

        gbc.gridy++;
        add(new JLabel("Features:"), gbc);
        gbc.gridy++;
        add(featureField, gbc);

        JPanel btnPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        btnPanel.add(saveButton);
        btnPanel.add(closeButton);

        gbc.gridy++;
        gbc.fill = GridBagConstraints.NONE;
        gbc.anchor = GridBagConstraints.CENTER;
        add(btnPanel, gbc);

        nameField.setPreferredSize(new Dimension(400, 30));
        albumBox.setPreferredSize(new Dimension(400, 30));
        gradeField.setPreferredSize(new Dimension(100, 30));
        featureField.setPreferredSize(new Dimension(400, 30));

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
            JOptionPane.showMessageDialog(this, "Erro ao carregar álbuns: " + e.getMessage(), "Erro",
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
                    "Nota inválida. Digite um número inteiro entre 0 e 100.",
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
