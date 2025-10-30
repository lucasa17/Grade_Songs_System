package view.swing.song;

import java.util.List;

import model.Song;

public interface ISongListView {
    void setSongList(List<Song> songs);
    void showMessage(String msg);
}
