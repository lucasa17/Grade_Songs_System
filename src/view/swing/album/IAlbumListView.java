package view.swing.album;

import java.util.List;

import model.Album;

public interface IAlbumListView {
    void setPostList(List<Album> albums);
    void showMessage(String msg);
}
