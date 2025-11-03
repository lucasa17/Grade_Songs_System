package view.swing.album;

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

import controller.AlbumController;
import model.Album;

public class AlbumListView extends JDialog implements IAlbumListView{
	private AlbumController controller;
    private final AlbumTableModel tableModel = new AlbumTableModel();
    private final JTable table = new JTable(tableModel);

    public AlbumListView(JFrame parent) {
        super(parent, "Albuns", true);
        this.controller = new AlbumController();
        this.controller.setAlbumListView(this);
        refresh();
        
        setSize(650, 450);
        setLocationRelativeTo(null);

        JScrollPane scrollPane = new JScrollPane(table);

        table.setRowHeight(36);

        table.setShowGrid(true);
        table.setGridColor(Color.LIGHT_GRAY);

        JButton addButton = new JButton("Adicionar Coleção");
        addButton.addActionListener(e -> {
        	AlbumFormView form = new AlbumFormView(this, null, controller);
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
                Album album = tableModel.getAlbumAt(row);
                AlbumFormView form = new AlbumFormView(this, album, controller);
                form.setVisible(true);
            }
        	refresh();
        });

        deleteItem.addActionListener(e -> {
            int row = table.getSelectedRow();
            if (row >= 0) {
            	Album album = tableModel.getAlbumAt(row);
                int confirm = JOptionPane.showConfirmDialog(this, "Excluir album?", "Confirmação", JOptionPane.YES_NO_OPTION);
                if (confirm == JOptionPane.YES_OPTION) {
                    controller.deleteAlbum(album);
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
    public void setAlbumList(List<Album> albums) {
        tableModel.setAlbums(albums);
    }

    @Override
    public void showMessage(String msg) {
        JOptionPane.showMessageDialog(this, msg);
    }

    public void refresh() {
        controller.loadAlbums();
    }

    static class AlbumTableModel extends AbstractTableModel {
        private final String[] columns = {"Nome", "Artista", "Ano", "Coleção"};

        private List<Album> albums = new ArrayList<>();

        public void setAlbums(List<Album> albums) {
            this.albums = albums;
            fireTableDataChanged();
        }

        public Album getAlbumAt(int row) {
            return albums.get(row);
        }

        @Override public int getRowCount() { return albums.size(); }

        @Override public int getColumnCount() { return columns.length; }

        @Override public String getColumnName(int col) { return columns[col]; }

        @Override
        public Object getValueAt(int row, int col) {
            Album a = albums.get(row);
            switch (col) {
                case 0: return a.getName();
                case 1: return (a.getArtist() != null) ? a.getArtist().getName() : "";
                case 2: return a.getYear();
                case 3: return (a.getCollection() != null) ? a.getCollection().getName() : "";
                default: return null;
            }
        }
        @Override public boolean isCellEditable(int row, int col) { return false; }
    }
}
