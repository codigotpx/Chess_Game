package vista;

import funcionalidades.CargarFondo;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.net.URL;
import java.net.http.WebSocket;

public class MenuPanel extends JPanel {
    private JButton botonCargarPgn;
    private JButton botonJugar;
    private Image fondo;
    private CargarFondo cargarFondo;

    public MenuPanel(CargarFondo cargarFondo) {
        // Crear botones con colores personalizados
        this.botonCargarPgn = new JButton("Cargar PGN");
        this.botonJugar = new JButton("Jugar");
        customizeButton(botonCargarPgn);
        customizeButton(botonJugar);
        this.cargarFondo = cargarFondo;

        fondo = cargarFondo.setFondo("resources/imagenes/fondos/fondo.png");

        // Crear un título
        JLabel titulo = new JLabel("Ajedrez", JLabel.CENTER);
        titulo.setFont(new Font("Serif", Font.BOLD, 32));
        titulo.setForeground(new Color(255, 255, 255)); // Gris oscuro

        // Crear un panel para centrar el contenido
        JPanel contenidoPanel = new JPanel();
        contenidoPanel.setLayout(new BoxLayout(contenidoPanel, BoxLayout.Y_AXIS));
        contenidoPanel.setBackground(new Color(240, 240, 240)); // Fondo claro
        contenidoPanel.setAlignmentX(Component.CENTER_ALIGNMENT);

        // Añadir el título al panel de contenido
        titulo.setAlignmentX(Component.CENTER_ALIGNMENT);
        contenidoPanel.add(titulo);

        // Añadir espacio entre el título y los botones
        contenidoPanel.add(Box.createRigidArea(new Dimension(0, 30)));
        contenidoPanel.setBackground(new Color(31, 26, 26));

        // Crear un panel para los botones
        JPanel botonesPanel = new JPanel();
        botonesPanel.setLayout(new GridLayout(1, 2, 20, 0)); // Dos botones en una fila
        botonesPanel.setBackground(new Color(31, 26, 26)); // Fondo claro
        botonesPanel.setBorder(BorderFactory.createEmptyBorder(10, 50, 10, 50));
        botonesPanel.add(botonCargarPgn);
        botonesPanel.add(botonJugar);

        // Añadir el panel de botones al panel de contenido
        contenidoPanel.add(botonesPanel);

        // Centrar todo en el panel principal
        setLayout(new GridBagLayout());
        setBackground(new Color(240, 240, 240)); // Fondo claro
        add(contenidoPanel);
    }

    private void customizeButton(JButton button) {
        button.setBackground(new Color(123, 227, 123)); // Verde claro
        button.setForeground(Color.WHITE); // Texto blanco
        button.setFocusPainted(false);
        button.setFont(new Font("SansSerif", Font.BOLD, 16));
        button.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(255, 255, 255)),
                BorderFactory.createEmptyBorder(10, 20, 10, 20)
        ));
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        g.drawImage(fondo, 0, 0,getWidth(),getHeight(), null);

    }


    public void addListenerBotonCargarPgn(ActionListener listener) {
        botonCargarPgn.addActionListener(listener);
    }

    public void addListenerBotonJugar(ActionListener listener) {
        botonJugar.addActionListener(listener);
    }

}
