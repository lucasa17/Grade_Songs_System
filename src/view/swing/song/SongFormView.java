package view.swing.song;

import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.util.List;

import javax.swing.*;

import controller.SongController;
import model.Album;
import model.Feature;
import model.Song;
import model.ModelException;

public class SongFormView extends JDialog implements ISongFormView {
    private final JComboBox<Album> albumBox = new JComboBox<>();
    private final JTextField nameField = new JTextField(50);
    private final JTextField yearField = new JTextField(4);
    private final JList<Feature> featureList = new JList<>();
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
        gbc.insets = new Insets(5,5,5,5);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        // Nome
        gbc.gridx = 0; gbc.gridy = 0;
        add(new JLabel("Nome:"), gbc);
        gbc.gridx = 1;
        add(nameField, gbc);

        // Ano
        gbc.gridx = 0; gbc.gridy = 1;
        add(new JLabel("Ano:"), gbc);
        gbc.gridx = 1;
        add(yearField, gbc);

        // Album
        gbc.gridx = 0; gbc.gridy = 2;
        add(new JLabel("Album:"), gbc);
        gbc.gridx = 1;
        albumBox.setEditable(true);
        add(albumBox, gbc);

        // Features
        gbc.gridx = 0; gbc.gridy = 3;
        add(new JLabel("Features:"), gbc);
        gbc.gridx = 1;
        featureList.setSelectionMode(ListSelectionModel.MULTIPLE_INTERVAL_SELECTION);
        JScrollPane featureScroll = new JScrollPane(featureList);
        featureScroll.setPreferredSize(new Dimension(200, 80));
        add(featureScroll, gbc);

        // Botões
        JPanel btnPanel = new JPanel();
        btnPanel.add(saveButton);
        btnPanel.add(closeButton);
        gbc.gridx = 0; gbc.gridy = 4; gbc.gridwidth = 2;
        add(btnPanel, gbc);

        // Se for edição, preenche o form
        if (!isNew) {
            setSongInForm(song);
        }

        // Carregar combos e listas
        loadAlbums();
        loadFeatures();

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
            JOptionPane.showMessageDialog(this,
                    "Erro ao carregar albuns: " + e.getMessage(),
                    "Erro",
                    JOptionPane.ERROR_MESSAGE);
        }
    }

    private void loadFeatures() {
        try {
            List<Feature> features = controller.findAllFeatures();
            DefaultListModel<Feature> model = new DefaultListModel<>();
            for (Feature f : features) {
                model.addElement(f);
            }
            featureList.setModel(model);
        } catch (ModelException e) {
            JOptionPane.showMessageDialog(this,
                    "Erro ao carregar features: " + e.getMessage(),
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

        Object selectedAlbumItem = albumBox.getSelectedItem();
        if (selectedAlbumItem instanceof Album) {
            song.setAlbum((Album) selectedAlbumItem);
        }

        // Pegar features selecionadas
        List<Feature> selectedFeatures = featureList.getSelectedValuesList();
        song.setFeatures(selectedFeatures);

        return song;
    }

    @Override
    public void setSongInForm(Song song) {
        nameField.setText(song.getName());
        if (song.getAlbum() != null) {
            albumBox.setSelectedItem(song.getAlbum());
        }

        if (song.getFeatures() != null) {
            int[] selectedIndices = song.getFeatures().stream()
                .mapToInt(f -> ((DefaultListModel<Feature>) featureList.getModel()).indexOf(f))
                .filter(i -> i >= 0)
                .toArray();
            featureList.setSelectedIndices(selectedIndices);
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
