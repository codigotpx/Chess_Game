import controlador.ControladorPrincipal;

import javax.swing.*;

public class Main {
    public static void main(String[] args) {
        JFrame marco = new JFrame();
        marco.setTitle("Ajedrez");
        marco.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        marco.setSize(1090, 800);
        marco.setLocationRelativeTo(null);

        ControladorPrincipal controladorPrincipal = new ControladorPrincipal(marco);

        marco.setVisible(true);
    }
}