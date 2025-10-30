package view.swing.album;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;

import controller.AlbumController;
import model.Album;
import model.Session;

public class AlbumFormView extends JDialog implements IAlbumFormView {
	private final JTextField nameField = new JTextField(50);
	private final JTextField yearField = new JTextField(4);
	private final JTextField artistField = new JTextField(50);
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
        setSize(350, 180);
        setLocationRelativeTo(parent);
        setLayout(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5,5,5,5);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        gbc.gridx = 0; gbc.gridy = 0;
        add(new JLabel("Nome:"), gbc);
        gbc.gridx = 1;
        add(nameField, gbc);
        
        gbc.gridx = 0; gbc.gridy = 1;
        add(new JLabel("Ano:"), gbc);
        gbc.gridx = 1;
        add(yearField, gbc);

        gbc.gridx = 0; gbc.gridy = 2;
        add(new JLabel("Artista:"), gbc);
        gbc.gridx = 1;
        add(artistField, gbc);
        
        JPanel btnPanel = new JPanel();
        btnPanel.add(saveButton);
        btnPanel.add(closeButton);

        gbc.gridx = 0; gbc.gridy = 3; gbc.gridwidth = 2;
        add(btnPanel, gbc);

        if (!isNew) 
        	setAlbumInForm(album);

        saveButton.addActionListener(e -> controller.saveOrUpdate(isNew));
        closeButton.addActionListener(e -> close());
    }

    @Override
    public Album getAlbumFromForm() {
        if (album == null) {
            album = new Album(0);
            //album.setCollection(Session.getLoggedUser());
        }

        album.setName(nameField.getText());
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
