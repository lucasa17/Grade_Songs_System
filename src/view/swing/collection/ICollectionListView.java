package view.swing.collection;

import java.util.List;

import model.Collection;

public interface ICollectionListView {
    void setPostList(List<Collection> collections);
    void showMessage(String msg);
}
