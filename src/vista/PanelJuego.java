package vista;

import funcionalidades.CargarIcon;
import modelo.Ficha;
import modelo.Tablero;

import javax.swing.*;
import java.awt.*;

public class PanelJuego extends JPanel {
    private Tablero tablero;
    private CargarIcon cargarIcon;

    public PanelJuego(Tablero tablero, CargarIcon cargarIcon) {
        this.tablero = tablero;
        this.cargarIcon = cargarIcon;
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
                    g.setColor(new Color(92, 51, 49, 255)); // Color oscuro
                } else {
                    g.setColor(new Color(120, 73, 57, 255)); // Color claro
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
}
