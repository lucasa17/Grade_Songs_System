package view.swing.collection;

import model.Collection;

public interface ICollectionFormView {
    Collection getCollectionFromForm();
    void setCollectionInForm(Collection collection);
    void showInfoMessage(String msg);
    void showErrorMessage(String msg);
    void close();
}
