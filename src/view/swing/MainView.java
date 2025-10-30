package view.swing;

import view.swing.collection.CollectionListView;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;

public class MainView extends JFrame {
    private static final long serialVersionUID = 1L;

	public MainView() {
        setTitle("Grade Songs System");
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        JMenuBar menuBar = new JMenuBar();

        JMenu menu = new JMenu("Coleções");
        JMenuItem collectionListItem = new JMenuItem("Listar Coleções");
        collectionListItem.addActionListener(e -> new CollectionListView(this).setVisible(true));
        menu.add(collectionListItem);
        menuBar.add(menu);
        
        JMenu postMenu = new JMenu("Albuns");
        JMenuItem postListItem = new JMenuItem("Listar Albuns");
        //postListItem.addActionListener(e -> new PostListView(this).setVisible(true));
        postMenu.add(postListItem);
        menuBar.add(postMenu);

        // Adiciona um menu vazio para empurrar o próximo menu para a direita
        menuBar.add(Box.createHorizontalGlue());

        // Menu "Sair" alinhado à direita
        JMenu menuSair = new JMenu("...");
        JMenuItem sairItem = new JMenuItem("Fechar o sistema");
        sairItem.addActionListener(e -> System.exit(0));
        menuSair.add(sairItem);
        menuBar.add(menuSair);

        setJMenuBar(menuBar);

        JLabel label = new JLabel("Seja bem-vindo!", SwingConstants.CENTER);

        // Painel com padding
        JPanel contentPanel = new JPanel(new BorderLayout());
        contentPanel.setBorder(new EmptyBorder(32, 32, 32, 32)); // padding de 32px
        contentPanel.add(label, BorderLayout.CENTER);

        setContentPane(contentPanel);
    }

    public static void main(String[] args) {
        try {
            UIManager.setLookAndFeel("javax.swing.plaf.nimbus.NimbusLookAndFeel");
        } catch (Exception ex) {
            ex.printStackTrace();
        }

        // Tela de login antes da tela principal
        SwingUtilities.invokeLater(() -> {
            while (true) {
                LoginView login = new LoginView();
                login.setVisible(true);

                if (login.isAuthenticated()) {
                    MainView mainView = new MainView();
                    mainView.setVisible(true);
                    mainView.setExtendedState(JFrame.MAXIMIZED_BOTH);
                    break; // sai do loop
                } else if (login.isRegisterRequested()) {
                    RegisterView registerView = new RegisterView();
                    registerView.setVisible(true);
                    // depois do cadastro, volta ao login automaticamente
                } else {
                    System.exit(0);
                }
            }
        });
    }
}
