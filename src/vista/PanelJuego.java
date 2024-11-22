package vista;

import funcionalidades.CargarIcon;
import modelo.Ficha;
import modelo.Tablero;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;

public class PanelJuego extends JPanel {
    private Tablero tablero;
    private CargarIcon cargarIcon;
    private JButton botonRetroceder;
    private JButton botonSiguiente;
    private JButton botonCargarPGN;

    public PanelJuego(Tablero tablero, CargarIcon cargarIcon) {
        this.tablero = tablero;
        this.cargarIcon = cargarIcon;

        botonSiguiente = new JButton("Siguiente");
        botonRetroceder = new JButton("Retroceder");
        botonCargarPGN = new JButton("Cargar PGN");

        // Añadir botones y etiquetas al panel
        setLayout(new BorderLayout());
        JPanel panelBotones = new JPanel();
        panelBotones.add(botonSiguiente);
        panelBotones.add(botonRetroceder);
        panelBotones.add(botonCargarPGN);

        add(panelBotones, BorderLayout.SOUTH);

        // Funcionalidad para cargar el archivo PGN
        botonCargarPGN.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                cargarArchivoPGN();
            }
        });
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);

        int tamañoCelda = tablero.getTamañoCelda();

        // Calcular el inicio para centrar el tablero en el panel
        int inicioX = (getWidth() - (tamañoCelda * 8)) / 2;
        int inicioY = (getHeight() - (tamañoCelda * 8)) / 2;

        // Dibujar el tablero y las piezas
        for (int fila = 0; fila < 8; fila++) {
            for (int col = 0; col < 8; col++) {
                Ficha ficha = tablero.getCasilla(fila, col).getFicha();

                // Alternar colores de las celdas
                if ((fila + col) % 2 == 0) {
                    g.setColor(new Color(120, 73, 57, 255)); // Color oscuro
                } else {
                    g.setColor(new Color(92, 51, 49, 255)); // Color claro
                }

                // Dibujar la celda
                int celdaX = inicioX + col * tamañoCelda;
                int celdaY = inicioY + fila * tamañoCelda;
                g.fillRect(celdaX, celdaY, tamañoCelda, tamañoCelda);

                // Dibujar la ficha si hay una en esta posición
                if (ficha != null) {
                    String tipoFicha = ficha.getTipoFicha();
                    String colorFicha = ficha.getColor();
                    String rutaImagen = "/resources/imagenes/" + tipoFicha + "_" + colorFicha + ".png";
                    Image imagen = cargarIcon.cargarIcon(rutaImagen);

                    if (imagen != null) {
                        // Calcular la posición y tamaño de la ficha en la celda
                        int fichaX = celdaX + tamañoCelda / 8;
                        int fichaY = celdaY + tamañoCelda / 8;
                        int fichaTamaño = tamañoCelda * 3 / 4;
                        g.drawImage(imagen, fichaX, fichaY, fichaTamaño, fichaTamaño, this);
                    }
                }
            }
        }
    }

    @Override
    public Dimension getPreferredSize() {
        return new Dimension(400, 400); // Tamaño preferido del panel
    }

    // Métodos para usar los botones en el controlador
    public void addBotonSiguienteListener(ActionListener listener) {
        botonSiguiente.addActionListener(listener);
    }

    public void addBotonRetrocederListener(ActionListener listener) {
        botonRetroceder.addActionListener(listener);
    }

    public void addBotonCargarPGNListener(ActionListener listener) {
        botonCargarPGN.addActionListener(listener);
    }




    // Método para buscar archivo Pgn
    private void cargarArchivoPGN() {
        JFileChooser fileChooser = new JFileChooser();
        int seleccion = fileChooser.showOpenDialog(this);

        if (seleccion == JFileChooser.APPROVE_OPTION) {
            File archivoPGN = fileChooser.getSelectedFile();
            try (BufferedReader br = new BufferedReader(new FileReader(archivoPGN))) {
                String linea;
                StringBuilder contenido = new StringBuilder();

                while ((linea = br.readLine()) != null) {
                    contenido.append(linea).append("\n");
                }

            } catch (IOException e) {
                JOptionPane.showMessageDialog(this, "Error al cargar el archivo PGN.", "Error", JOptionPane.ERROR_MESSAGE);
                e.printStackTrace();
            }
        }

    }

    // Método para actualizar la vista del tablero
    public void actualizarVistaTablero(Tablero tablero) {
        this.tablero = tablero;
        repaint();
    }
}
