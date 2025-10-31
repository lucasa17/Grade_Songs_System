package view.swing.song;

import model.Song;

public interface ISongFormView {
	Song getSongFromForm();
    void setSongInForm(Song song);
    void showInfoMessage(String msg);
    void showErrorMessage(String msg);
    void close();
}
