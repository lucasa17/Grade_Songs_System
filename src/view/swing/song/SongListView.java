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
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPopupMenu;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.table.AbstractTableModel;

import controller.SongController;
import model.Song;

public class SongListView extends JDialog implements ISongListView{
	private SongController controller;
    private final SongTableModel tableModel = new SongTableModel();
    private final JTable table = new JTable(tableModel);

    public SongListView(JFrame parent) {
        super(parent, "Albuns", true);
        this.controller = new SongController();
        this.controller.setSongListView(this);
        refresh();
        
        setSize(650, 400);
        setLocationRelativeTo(null);

        JScrollPane scrollPane = new JScrollPane(table);

        table.setRowHeight(36);

        table.setShowGrid(true);
        table.setGridColor(Color.LIGHT_GRAY);

        JButton addButton = new JButton("Adicionar Coleção");
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
                int confirm = JOptionPane.showConfirmDialog(this, "Excluir song?", "Confirmação", JOptionPane.YES_NO_OPTION);
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
        private final String[] columns = {"Nome", "Artista", "Ano", "Coleção"};

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
            Song a = songs.get(row);
            switch (col) {
                case 0: return a.getName();
                default: return null;
            }
        }
        @Override public boolean isCellEditable(int row, int col) { return false; }
    }
}
