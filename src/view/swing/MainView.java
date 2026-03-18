package view.swing;

import com.formdev.flatlaf.themes.FlatMacDarkLaf;
import controller.AlbumController;
import controller.CollectionController;
import controller.SongController;
import controller.UserController;
import model.Album;
import model.ModelException;
import model.Session;
import model.Song;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.util.List;

public class MainView extends JFrame {
    private static final long serialVersionUID = 1L;

    private final JPanel dashboardPanel = new JPanel(new GridBagLayout());
    private final GridBagConstraints gbc = new GridBagConstraints();

    private final UserController userController = new UserController();
    private final CollectionController collectionController = new CollectionController();
    private final AlbumController albumController = new AlbumController();
    private final SongController songController = new SongController();

    public MainView() {
        setTitle("Grade Songs System");
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        // ===== MENU =====
        JMenuBar menuBar = new JMenuBar();

        JMenu collectionMenu = new JMenu("Coleções");
        JMenuItem collectionListItem = new JMenuItem("Listar Coleções");
        collectionListItem.addActionListener(e -> new view.swing.collection.CollectionListView(this).setVisible(true));
        collectionMenu.add(collectionListItem);
        menuBar.add(collectionMenu);

        JMenu albumMenu = new JMenu("Álbuns");
        JMenuItem albumListItem = new JMenuItem("Listar Álbuns");
        albumListItem.addActionListener(e -> new view.swing.album.AlbumListView(this).setVisible(true));
        albumMenu.add(albumListItem);
        menuBar.add(albumMenu);

        JMenu songMenu = new JMenu("Músicas");
        JMenuItem songListItem = new JMenuItem("Listar Músicas");
        songListItem.addActionListener(e -> new view.swing.song.SongListView(this).setVisible(true));
        songMenu.add(songListItem);
        menuBar.add(songMenu);

        menuBar.add(Box.createHorizontalGlue());
        JMenu menuSair = new JMenu("...");
        JMenuItem sairItem = new JMenuItem("Excluir conta");
        sairItem.addActionListener(e -> {
            int confirm = JOptionPane.showConfirmDialog(this,
                    "Deseja excluir sua conta?", "Confirmação", JOptionPane.YES_NO_OPTION);
            if (confirm == JOptionPane.YES_OPTION) {
                userController.deleteUser(Session.getLoggedUser());
                System.exit(0);
            }
        });
        menuSair.add(sairItem);
        menuBar.add(menuSair);
        setJMenuBar(menuBar);

        String username = userController.searchNameUser(Session.getLoggedUser().getId());

        JLabel welcomeLabel = new JLabel("🎧 Seja bem-vindo, " + username + "!", SwingConstants.CENTER);
        welcomeLabel.setFont(new Font("SansSerif", Font.BOLD, 22));
        welcomeLabel.setBorder(new EmptyBorder(20, 0, 20, 0));

        JButton refreshButton = new JButton("🔄 Atualizar");
        refreshButton.setFont(new Font("SansSerif", Font.PLAIN, 12)); // fonte menor
        refreshButton.setMargin(new Insets(0, 0, 0, 0)); // reduz o espaço interno
        refreshButton.setFocusable(false); // remove foco visual indesejado
        refreshButton.addActionListener(e -> refreshDashboard());
        
        dashboardPanel.setBorder(new EmptyBorder(32, 64, 32, 64));
        gbc.insets = new Insets(20, 4, 6, 4);
        gbc.gridx = 0;
        gbc.anchor = GridBagConstraints.WEST;

        refreshDashboard();

        JPanel topPanel = new JPanel(new BorderLayout());
        topPanel.setBorder(BorderFactory.createEmptyBorder(0, 0, 5, 0)); // menos espaço ao redor
        topPanel.add(welcomeLabel, BorderLayout.NORTH);
        topPanel.add(refreshButton, BorderLayout.SOUTH);

        JPanel mainPanel = new JPanel(new BorderLayout());
        mainPanel.add(topPanel, BorderLayout.NORTH);
        mainPanel.add(new JScrollPane(dashboardPanel), BorderLayout.CENTER);

        setContentPane(mainPanel);
    }

    // 🔁 Método que atualiza todo o painel do dashboard
    public void refreshDashboard() {
        dashboardPanel.removeAll(); // limpa conteúdo anterior

        try {
            int totalCollections = collectionController.getTotalCollections();
            int totalAlbums = albumController.getTotalAlbums();
            int totalSongs = songController.getTotalSongs();

            JLabel summaryLabel = new JLabel(
                    String.format("📊 Coleções: %d   |   Álbuns: %d   |   Músicas: %d",
                            totalCollections, totalAlbums, totalSongs),
                    SwingConstants.CENTER
            );
            summaryLabel.setFont(new Font("SansSerif", Font.PLAIN, 16));

            gbc.gridy = 0;
            gbc.anchor = GridBagConstraints.CENTER;
            dashboardPanel.add(summaryLabel, gbc);
            gbc.anchor = GridBagConstraints.WEST;

            // ===== Top músicas =====
            gbc.gridy++;
            JLabel topSongsLabel = new JLabel("⭐ Top 5 músicas mais bem avaliadas:");
            topSongsLabel.setFont(new Font("SansSerif", Font.BOLD, 18));
            dashboardPanel.add(topSongsLabel, gbc);

            List<Song> topSongs = songController.findTopRatedSongs(5);
            if (topSongs.isEmpty()) {
                gbc.gridy++;
                dashboardPanel.add(new JLabel("Nenhuma música cadastrada ainda 🎶"), gbc);
            } else {
                for (int i = 0; i < topSongs.size(); i++) {
                    Song s = topSongs.get(i);
                    gbc.gridy++;
                    JLabel songLabel = new JLabel(
                            String.format("%d° %s — %s (Nota: %d)",
                                    i + 1, s.getName(),
                                    s.getAlbum().getArtist().getName(),
                                    s.getGrade())
                    );
                    dashboardPanel.add(songLabel, gbc);
                }
            }

            // ===== Top álbuns =====
            gbc.gridy++;
            gbc.insets = new Insets(20, 4, 6, 4);
            JLabel topAlbumsLabel = new JLabel("💿 Top 3 álbuns com melhor média:");
            topAlbumsLabel.setFont(new Font("SansSerif", Font.BOLD, 18));
            dashboardPanel.add(topAlbumsLabel, gbc);

            List<Album> topAlbums = albumController.findTopRatedAlbums(3);
            if (topAlbums.isEmpty()) {
                gbc.gridy++;
                dashboardPanel.add(new JLabel("Nenhum álbum cadastrado ainda 💽"), gbc);
            } else {
                for (int i = 0; i < topAlbums.size(); i++) {
                    Album a = topAlbums.get(i);
                    gbc.gridy++;
                    JLabel albumLabel = new JLabel(
                            String.format("%d° %s — %s (Média: %d)",
                                    i + 1, a.getName(),
                                    a.getArtist().getName(),
                                    (int) a.getAverageRating())
                    );
                    dashboardPanel.add(albumLabel, gbc);
                }
            }

        } catch (ModelException ex) {
            gbc.gridy++;
            dashboardPanel.add(new JLabel("Erro ao carregar dados: " + ex.getMessage()), gbc);
        }

        dashboardPanel.revalidate();
        dashboardPanel.repaint();
    }

    // ====== MAIN ======
    public static void main(String[] args) {
        try {
            UIManager.setLookAndFeel(new FlatMacDarkLaf());
        } catch (Exception ex) {
            ex.printStackTrace();
        }

        SwingUtilities.invokeLater(() -> {
            while (true) {
                LoginView login = new LoginView();
                login.setVisible(true);

                if (login.isAuthenticated()) {
                    MainView mainView = new MainView();
                    mainView.setExtendedState(JFrame.MAXIMIZED_BOTH);
                    mainView.setVisible(true);
                    break;
                } else if (login.isRegisterRequested()) {
                    RegisterView registerView = new RegisterView();
                    registerView.setVisible(true);
                } else {
                    System.exit(0);
                }
            }
        });
    }
}
