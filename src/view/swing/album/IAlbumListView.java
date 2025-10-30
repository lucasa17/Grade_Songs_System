package view.swing.album;

import java.util.List;

import model.Album;

public interface IAlbumListView {
    void setAlbumList(List<Album> albums);
    void showMessage(String msg);
}
