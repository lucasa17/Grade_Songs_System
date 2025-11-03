package view.swing.album;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.List;
import javax.swing.*;
import javax.swing.table.AbstractTableModel;
import javax.swing.table.DefaultTableCellRenderer;

import controller.AlbumController;
import model.Album;
import model.ModelException;
import model.Song;
import view.swing.song.SongListView;

public class AlbumListView extends JDialog implements IAlbumListView {
    private AlbumController controller;
    private final AlbumTableModel tableModel = new AlbumTableModel();
    private final JTable table = new JTable(tableModel);

    public AlbumListView(JFrame parent) {
        super(parent, "Álbuns", true);
        this.controller = new AlbumController();
        this.controller.setAlbumListView(this);
        refresh();

        setSize(650, 450);
        setLocationRelativeTo(null);

        JScrollPane scrollPane = new JScrollPane(table);
        table.setRowHeight(36);
        
        table.setAutoCreateRowSorter(true);
        table.getTableHeader().addMouseListener(new MouseAdapter() {
            
        	boolean ascending = true;
            @Override
            public void mouseClicked(MouseEvent e) {
                int column = table.columnAtPoint(e.getPoint());
                String columnName = table.getColumnName(column);

                ascending = !ascending;

                List<Album> orderedAlbums = new ArrayList<>();
                try {
                    switch(columnName) {
                        case "Nome":
                        	orderedAlbums = controller.getAlbumsOrderedByName(ascending);
                            break;
                        case "Artista":
                        	orderedAlbums = controller.getAlbumsOrderedByArtist(ascending);
                            break;
                        case "Ano":
                        	orderedAlbums = controller.getAlbumsOrderedByYear(ascending);
                            break;
                        case "Coleção":
                        	orderedAlbums = controller.getAlbumsOrderedByCollection(ascending);
                            break;
                    }
                } catch (ModelException ex) {
                    JOptionPane.showMessageDialog(AlbumListView.this, 
                        "Erro ao ordenar a lista: " + ex.getMessage(),
                        "Erro", JOptionPane.ERROR_MESSAGE);
                }

                tableModel.setAlbums(orderedAlbums);
                refresh();
            }
        });
        
        table.setShowGrid(true);
        table.setGridColor(Color.LIGHT_GRAY);

        DefaultTableCellRenderer centerRenderer = new DefaultTableCellRenderer();
        centerRenderer.setHorizontalAlignment(SwingConstants.CENTER);
        for (int i = 0; i < table.getColumnCount(); i++) {
            table.getColumnModel().getColumn(i).setCellRenderer(centerRenderer);
        }

        JButton addButton = new JButton("Adicionar Álbum");
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
                if (e.isPopupTrigger()) showPopup(e);
            }

            @Override
            public void mouseReleased(MouseEvent e) {
                if (e.isPopupTrigger()) showPopup(e);
            }

            private void showPopup(MouseEvent e) {
                popupMenu.show(e.getComponent(), e.getX(), e.getY());
            }
        });

        editItem.addActionListener(e -> {
            int viewRow = table.getSelectedRow();
            if (viewRow >= 0) {
                int modelRow = table.convertRowIndexToModel(viewRow);

                Album album = tableModel.getAlbumAt(modelRow);
                AlbumFormView form = new AlbumFormView(this, album, controller);
                form.setVisible(true);
                refresh();
            }
        });

        deleteItem.addActionListener(e -> {
            int viewRow = table.getSelectedRow();
            if (viewRow >= 0) {
                int modelRow = table.convertRowIndexToModel(viewRow);
                
                Album album = tableModel.getAlbumAt(modelRow);
                int confirm = JOptionPane.showConfirmDialog(this, "Excluir álbum?", "Confirmação", JOptionPane.YES_NO_OPTION);
                if (confirm == JOptionPane.YES_OPTION) {
                    controller.deleteAlbum(album);
                    refresh();
                }
            }
        });

        JPanel panel = new JPanel(new BorderLayout());
        panel.add(addButton, BorderLayout.EAST);

        add(scrollPane, BorderLayout.CENTER);
        add(panel, BorderLayout.SOUTH);

        JPanel filterPanel = new JPanel();
        filterPanel.setBackground(Color.DARK_GRAY);

        JTextField nameField = new JTextField(8);
        JTextField artistField = new JTextField(8);
        JTextField collectionField = new JTextField(8);

        JButton filterButton = new JButton("Filtrar");
        JButton clearButton = new JButton("Limpar");

        filterPanel.add(new JLabel("Nome:"));
        filterPanel.add(nameField);
        filterPanel.add(new JLabel("Artista:"));
        filterPanel.add(artistField);
        filterPanel.add(new JLabel("Coleção:"));
        filterPanel.add(collectionField);
        filterPanel.add(filterButton);
        filterPanel.add(clearButton);

        add(filterPanel, BorderLayout.NORTH);

        filterButton.addActionListener(e -> {
            String name = nameField.getText().trim();
            String artist = artistField.getText().trim();
            String collection = collectionField.getText().trim();

            try {
                List<Album> filtered = controller.searchAlbums(name, artist, collection);
                tableModel.setAlbums(filtered);
            } catch (ModelException ex) {
                JOptionPane.showMessageDialog(this, "Erro ao filtrar álbuns: " + ex.getMessage(),
                        "Erro", JOptionPane.ERROR_MESSAGE);
            }
        });

        clearButton.addActionListener(e -> {
            nameField.setText("");
            artistField.setText("");
            collectionField.setText("");
            refresh();
        });
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
