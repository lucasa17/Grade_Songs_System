package view.swing.user;

import controller.UserController;
import model.User;

import javax.swing.*;

public interface IUserFormView {
    User getUserFromForm();
    void setUserInForm(User user);
    void showInfoMessage(String msg);
    void showErrorMessage(String msg);
    void close();
}
