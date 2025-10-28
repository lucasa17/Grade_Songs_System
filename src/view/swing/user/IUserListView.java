package view.swing.user;

import java.util.List;

import model.User;

public interface IUserListView {
    void setUserList(List<User> users);
    void showMessage(String msg);
}
