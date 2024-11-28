import javax.swing.*;
import View.InicioView;

public class Main {
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            InicioView inicioView = new InicioView();

            JFrame frame = new JFrame("Sistema de Facturación");
            frame.setContentPane(inicioView.getPanel());
            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            frame.pack();
            frame.setLocationRelativeTo(null);
            frame.setVisible(true);

            frame.setSize(800, 600);
            frame.setLocationRelativeTo(null);
            frame.setVisible(true);

            inicioView.mostrarInventario();
        });
    }
}
