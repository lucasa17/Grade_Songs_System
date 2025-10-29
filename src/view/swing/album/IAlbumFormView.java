package view.swing.album;

import model.Album;

public interface IAlbumFormView {
	Album getCollectionFromForm();
    void setPostInForm(Album album);
    void showInfoMessage(String msg);
    void showErrorMessage(String msg);
    void close();
}
