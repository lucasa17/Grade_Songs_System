package view.swing.album;

import model.Album;

public interface IAlbumFormView {
	Album getAlbumFromForm();
    void setAlbumInForm(Album album);
    void showInfoMessage(String msg);
    void showErrorMessage(String msg);
    void close();
}
