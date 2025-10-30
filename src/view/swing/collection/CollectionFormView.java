package view.swing.collection;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;

import controller.CollectionController;
import model.Collection;
import model.Session;

public class CollectionFormView extends JDialog implements ICollectionFormView {
	 private final JTextField nameField = new JTextField(50);
	    private final JButton saveButton = new JButton("Salvar");
	    private final JButton closeButton = new JButton("Fechar");
	    private CollectionController controller;
	    private final boolean isNew;
	    private final CollectionListView parent;
	    private Collection collection;

	    public CollectionFormView(CollectionListView parent, Collection collection, CollectionController controller) {
	        super(parent, true);
	        this.controller = controller;
	        this.controller.setCollectionFormView(this);

	        this.parent = parent;
	        this.collection = collection;
	        this.isNew = (collection == null);

	        setTitle(isNew ? "Nova Coleção" : "Editar Coleção");
	        setSize(250, 150);
	        setLocationRelativeTo(parent);
	        setLayout(new GridBagLayout());
	        GridBagConstraints gbc = new GridBagConstraints();
	        gbc.insets = new Insets(5,5,5,5);
	        gbc.fill = GridBagConstraints.HORIZONTAL;

	        gbc.gridx = 0; gbc.gridy = 0;
	        add(new JLabel("Nome:"), gbc);
	        gbc.gridx = 1;
	        add(nameField, gbc);

	        JPanel btnPanel = new JPanel();
	        btnPanel.add(saveButton);
	        btnPanel.add(closeButton);

	        gbc.gridx = 1; gbc.gridy = 1; gbc.gridwidth = 1;
	        add(btnPanel, gbc);

	        if (!isNew) 
	        	setCollectionInForm(collection);

	        saveButton.addActionListener(e -> controller.saveOrUpdate(isNew));
	        closeButton.addActionListener(e -> close());
	    }

	    @Override
	    public Collection getCollectionFromForm() {
	        if (collection == null) {
	            collection = new Collection(0);
	            collection.setUser(Session.getLoggedUser());
	        }

	        collection.setName(nameField.getText());

	        // 🔹 Aqui o ID e o USER continuam intactos
	        return collection;
	    }

	    @Override
	    public void setCollectionInForm(Collection collection) {
	        nameField.setText(collection.getName());
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
