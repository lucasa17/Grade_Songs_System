package view.swing.collection;

import model.Collection;

public interface ICollectionFormView {
    Collection getCollectionFromForm();
    void setPostInForm(Collection collection);
    void showInfoMessage(String msg);
    void showErrorMessage(String msg);
    void close();
}
