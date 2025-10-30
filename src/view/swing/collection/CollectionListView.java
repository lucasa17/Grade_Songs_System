package view.swing.collection;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPopupMenu;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.table.AbstractTableModel;

import controller.CollectionController;
import model.Collection;

public class CollectionListView extends JDialog implements ICollectionListView{
	private CollectionController controller;
    private final CollectionTableModel tableModel = new CollectionTableModel();
    private final JTable table = new JTable(tableModel);

    public CollectionListView(JFrame parent) {
        super(parent, "Coleções", true);
        this.controller = new CollectionController();
        this.controller.setCollectionListView(this);
        refresh();
        
        setSize(650, 400);
        setLocationRelativeTo(null);

        JScrollPane scrollPane = new JScrollPane(table);

        table.setRowHeight(36);

        table.setShowGrid(true);
        table.setGridColor(Color.LIGHT_GRAY);

        JButton addButton = new JButton("Adicionar Coleção");
        addButton.addActionListener(e -> {
        	refresh();
        	CollectionFormView form = new CollectionFormView(this, null, controller);
            form.setVisible(true);
        });

        JPopupMenu popupMenu = new JPopupMenu();
        JMenuItem editItem = new JMenuItem("Editar");
        JMenuItem deleteItem = new JMenuItem("Excluir");
        popupMenu.add(editItem);
        popupMenu.add(deleteItem);

        table.addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                int row = table.rowAtPoint(e.getPoint());
                if (row >= 0 && row < table.getRowCount()) {
                    table.setRowSelectionInterval(row, row);
                } else {
                    table.clearSelection();
                }

                if (e.isPopupTrigger()) {
                    showPopup(e);
                }
            }

            @Override
            public void mouseReleased(MouseEvent e) {
                if (e.isPopupTrigger()) {
                    showPopup(e);
                }
            }

            private void showPopup(MouseEvent e) {
                popupMenu.show(e.getComponent(), e.getX(), e.getY());
            }
        });

        editItem.addActionListener(e -> {
            int row = table.getSelectedRow();
            if (row >= 0) {
                Collection collection = tableModel.getCollectionAt(row);
                CollectionFormView form = new CollectionFormView(this, collection, controller);
                form.setVisible(true);
            }
        	refresh();
        });

        deleteItem.addActionListener(e -> {
            int row = table.getSelectedRow();
            if (row >= 0) {
            	Collection collection = tableModel.getCollectionAt(row);
                int confirm = JOptionPane.showConfirmDialog(this, "Excluir coleção?", "Confirmação", JOptionPane.YES_NO_OPTION);
                if (confirm == JOptionPane.YES_OPTION) {
                    controller.deleteCollection(collection);
                    refresh();
                }
            }
        	refresh();
        });

        JPanel panel = new JPanel(new BorderLayout());
        panel.add(addButton, BorderLayout.EAST);

        add(scrollPane, BorderLayout.CENTER);
        add(panel, BorderLayout.SOUTH);

    }

    @Override
    public void setCollectionList(List<Collection> collections) {
        tableModel.setCollections(collections);
    }

    @Override
    public void showMessage(String msg) {
        JOptionPane.showMessageDialog(this, msg);
    }

    public void refresh() {
        controller.loadCollections();
    }

    // Tabela de usuários
    static class CollectionTableModel extends AbstractTableModel {
        private final String[] columns = {"Nome"};
        private List<Collection> collections = new ArrayList<>();

        public void setCollections(List<Collection> collections) {
            this.collections = collections;
            fireTableDataChanged();
        }

        public Collection getCollectionAt(int row) {
            return collections.get(row);
        }

        @Override public int getRowCount() { return collections.size(); }

        @Override public int getColumnCount() { return columns.length; }

        @Override public String getColumnName(int col) { return columns[col]; }

        @Override
        public Object getValueAt(int row, int col) {
            Collection u = collections.get(row);
            switch (col) {
                case 0: return u.getName();
                default: return null;
            }
        }
        @Override public boolean isCellEditable(int row, int col) { return false; }
    }
}
