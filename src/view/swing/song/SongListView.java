package view.swing.song;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPopupMenu;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import javax.swing.table.AbstractTableModel;
import javax.swing.table.DefaultTableCellRenderer;

import controller.SongController;
import model.ModelException;
import model.Song;

public class SongListView extends JDialog implements ISongListView{
	private static SongController controller;
    private final SongTableModel tableModel = new SongTableModel();
    private final JTable table = new JTable(tableModel);

    public SongListView(JFrame parent) {
        super(parent, "Músicas", true);
        this.controller = new SongController();
        this.controller.setSongListView(this);
        refresh();
        
        setSize(650, 450);
        setLocationRelativeTo(null);

        JScrollPane scrollPane = new JScrollPane(table);
        
        DefaultTableCellRenderer centerRenderer = new DefaultTableCellRenderer();
        centerRenderer.setHorizontalAlignment(SwingConstants.CENTER);

        for (int i = 0; i < table.getColumnCount(); i++) {
            table.getColumnModel().getColumn(i).setCellRenderer(centerRenderer);
        }
        
        table.setRowHeight(36);
/*
        table.setAutoCreateRowSorter(true);
        table.getTableHeader().addMouseListener(new MouseAdapter() {
            
        	boolean ascending = true;
            @Override
            public void mouseClicked(MouseEvent e) {
                int column = table.columnAtPoint(e.getPoint());
                String columnName = table.getColumnName(column);

                ascending = !ascending;

                List<Song> orderedSongs = new ArrayList<>();
                try {
                    switch(columnName) {
                        case "Nome":
                            orderedSongs = controller.getSongsOrderedByName(ascending);
                            break;
                        case "Artista":
                            orderedSongs = controller.getSongsOrderedByArtist(ascending);
                            break;
                        case "Features":
                            orderedSongs = controller.getSongsOrderedByFeatures(ascending);
                            break;
                        case "Album":
                            orderedSongs = controller.getSongsOrderedByAlbum(ascending);
                            break;
                        case "Nota":
                            orderedSongs = controller.getSongsOrderedByGrade(ascending);
                            break;
                    }
                } catch (ModelException ex) {
                    JOptionPane.showMessageDialog(SongListView.this, 
                        "Erro ao ordenar a lista: " + ex.getMessage(),
                        "Erro", JOptionPane.ERROR_MESSAGE);
                }

                tableModel.setSongs(orderedSongs);
            }
        });
*/
        
        table.setShowGrid(true);
        table.setGridColor(Color.LIGHT_GRAY);

        JButton addButton = new JButton("Adicionar Música");
        addButton.addActionListener(e -> {
        	SongFormView form = new SongFormView(this, null, controller);
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
                Song song = tableModel.getSongAt(row);
                SongFormView form = new SongFormView(this, song, controller);
                form.setVisible(true);
            }
        	refresh();
        });

        deleteItem.addActionListener(e -> {
            int row = table.getSelectedRow();
            if (row >= 0) {
            	Song song = tableModel.getSongAt(row);
                int confirm = JOptionPane.showConfirmDialog(this, "Excluir música?", "Confirmação", JOptionPane.YES_NO_OPTION);
                if (confirm == JOptionPane.YES_OPTION) {
                    controller.deleteSong(song);
                    refresh();
                }
            }
        	refresh();
        });

        JPanel panel = new JPanel(new BorderLayout());
        panel.add(addButton, BorderLayout.EAST);

        add(scrollPane, BorderLayout.CENTER);
        add(panel, BorderLayout.SOUTH);

        JPanel filterPanel = new JPanel();
        filterPanel.setBackground(Color.DARK_GRAY);
        
        JTextField nameField = new JTextField(8);
        JTextField artistField = new JTextField(8);
        JTextField albumField = new JTextField(8);

        JButton filterButton = new JButton("Filtrar");
        JButton clearButton = new JButton("Limpar");

        filterPanel.add(new JLabel("Nome:"));
        filterPanel.add(nameField);
        filterPanel.add(new JLabel("Artista:"));
        filterPanel.add(artistField);
        filterPanel.add(new JLabel("Álbum:"));
        filterPanel.add(albumField);
        filterPanel.add(filterButton);
        filterPanel.add(clearButton);

        add(filterPanel, BorderLayout.NORTH);

        filterButton.addActionListener(e -> {
        	String name = nameField.getText().trim();
            String artist = artistField.getText().trim();
            String album = albumField.getText().trim();            

            List<Song> filteredSongs = null;
			try {
				filteredSongs = controller.searchSongs(name, artist, album);
			} catch (ModelException e1) {
				 JOptionPane.showMessageDialog(this, "Erro na listagem de filtro", "Erro", JOptionPane.ERROR_MESSAGE);
			}
            tableModel.setSongs(filteredSongs);
        });

        clearButton.addActionListener(e -> {
            nameField.setText("");
            artistField.setText("");
            albumField.setText("");
            refresh();
        });
    }

    @Override
    public void setSongList(List<Song> songs) {
        tableModel.setSongs(songs);
    }

    @Override
    public void showMessage(String msg) {
        JOptionPane.showMessageDialog(this, msg);
    }

    public void refresh() {
        controller.loadSongs();
    }

    static class SongTableModel extends AbstractTableModel {
        private final String[] columns = {"Nome", "Artista", "Features", "Album", "Nota"};

        private List<Song> songs = new ArrayList<>();

        public void setSongs(List<Song> songs) {
            this.songs = songs;
            fireTableDataChanged();
        }

        public Song getSongAt(int row) {
            return songs.get(row);
        }

        @Override public int getRowCount() { return songs.size(); }

        @Override public int getColumnCount() { return columns.length; }

        @Override public String getColumnName(int col) { return columns[col]; }

        @Override
        public Object getValueAt(int row, int col) {
            Song s = songs.get(row);
            String artistName = controller.searchNameArtist(s.getAlbum());
            switch (col) {
            	case 0: return s.getName();
                case 1: return artistName;
                case 2: return (s.getFeatures() != null) ? s.getFeatures() : "";
            	case 3: return (s.getAlbum() != null) ? s.getAlbum().getName() : "";
            	case 4: return s.getGrade();
                default: return null;            }
        }
        @Override public boolean isCellEditable(int row, int col) { return false; }
    }
}
