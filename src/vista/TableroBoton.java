package vista;

import funcionalidades.CargarFondo;
import funcionalidades.CargarIcon;
import modelo.Casilla;
import modelo.Ficha;
import modelo.Tablero;


import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class TableroBoton extends JPanel {
    private Tablero tablero;
    private CargarIcon cargarIcon;
    private JButton[][] botonesTablero; // Botones para cada celda del tablero
    private Casilla casilla;
    private ActionListener listener; // Listener global para clics en celdas
    private Image fondo;
    private CargarFondo cargarFondo;

    public TableroBoton(Tablero tablero, CargarIcon cargarIcon, CargarFondo cargarFondo) {
        this.tablero = tablero;
        this.cargarIcon = cargarIcon;
        this.cargarFondo = cargarFondo;
        this.casilla = null;

        fondo = cargarFondo.setFondo("resources/imagenes/fondos/fondo2.png");

        // Inicializar botones del tablero
        botonesTablero = new JButton[8][8];

        // Crear diseño del tablero
        setLayout(new GridBagLayout()); // Usar GridBagLayout para centrar el panel del tablero
        JPanel panelTablero = new JPanel(new GridLayout(8, 8)); // Crear un panel 8x8 para las celdas
        panelTablero.setPreferredSize(new Dimension(400, 400)); // Establecer tamaño fijo del tablero

        for (int fila = 0; fila < 8; fila++) {
            for (int col = 0; col < 8; col++) {
                JButton boton = new JButton();
                boton.setPreferredSize(new Dimension(50, 50)); // Establecer tamaño de cada celda
                boton.setOpaque(true);
                boton.setBorderPainted(false);

                // Alternar colores de las celdas
                if ((fila + col) % 2 == 0) {
                    boton.setBackground(new Color(235, 237, 209, 255)); // Color claro
                } else {
                    boton.setBackground(new Color(119, 149, 86, 255)); // Color oscuro
                }

                // Añadir imagen de la ficha si existe
                Ficha ficha = tablero.getCasilla(fila, col).getFicha();

                if (ficha != null) {
                    String tipoFicha = ficha.getTipoFicha();
                    String colorFicha = ficha.getColor();
                    String rutaImagen = "/resources/imagenes/" + tipoFicha + "_" + colorFicha + ".png";
                    ImageIcon icono = new ImageIcon(cargarIcon.cargarIcon(rutaImagen));
                    boton.setIcon(new ImageIcon(icono.getImage().getScaledInstance(40, 40, Image.SCALE_SMOOTH)));
                }

                // Añadir acción al botón
                int finalFila = fila;
                int finalCol = col;

                boton.addActionListener(new ActionListener() {
                    @Override
                    public void actionPerformed(ActionEvent e) {
                        manejarClickEnCelda(finalFila, finalCol);
                    }
                });

                // Añadir botón al arreglo y al panel
                botonesTablero[fila][col] = boton;
                panelTablero.add(boton);
            }
        }

        // Añadir el panel del tablero al centro del contenedor principal
        add(panelTablero, new GridBagConstraints());
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);

        g.drawImage(fondo, 0, 0, getWidth(), getHeight(), this);
    }

    // Manejar clic en celda
    public void manejarClickEnCelda(int fila, int col) {
        Casilla casilla1 = tablero.getCasilla(fila, col);
        if (casilla1 != null) {
            System.out.println("Clic en celda: (" + fila + ", " + col + ") - Ficha: " + casilla1.getFicha());
            casilla = casilla1;

            // Notificar al listener
            if (listener != null) {
                ActionEvent event = new ActionEvent(casilla1, ActionEvent.ACTION_PERFORMED, "celdaClic");
                listener.actionPerformed(event);
            }
        } else {
            System.out.println("Clic en celda vacía: (" + fila + ", " + col + ")");
            casilla = null;
        }
    }

    // Registrar un ActionListener global para clics en celdas
    public void addBotonClick(ActionListener listener) {
        this.listener = listener;
    }

    public Casilla getCasillaClick() {
        return casilla;
    }

    public void actualizarVistaTablero(Tablero tablero) {
        this.tablero = tablero;

        for (int fila = 0; fila < 8; fila++) {
            for (int col = 0; col < 8; col++) {
                JButton boton = botonesTablero[fila][col];
                Casilla casilla = tablero.getCasilla(fila, col);
                Ficha ficha = casilla.getFicha();

                if (ficha != null) {
                    // Generar ruta del icono según la ficha
                    String tipoFicha = ficha.getTipoFicha();
                    String colorFicha = ficha.getColor();
                    String rutaImagen = "/resources/imagenes/" + tipoFicha + "_" + colorFicha + ".png";

                    // Cargar y escalar el icono
                    ImageIcon icono = new ImageIcon(cargarIcon.cargarIcon(rutaImagen));
                    boton.setIcon(new ImageIcon(icono.getImage().getScaledInstance(40, 40, Image.SCALE_SMOOTH)));
                } else {
                    // Si no hay ficha, eliminar el ícono
                    boton.setIcon(null);
                }
            }
        }

        // Asegurarse de que los cambios se reflejen visualmente
        revalidate();
        repaint();
    }

}
