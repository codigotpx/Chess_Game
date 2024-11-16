package modelo;

import java.util.ArrayList;
import java.util.List;

public class Alfil extends Ficha {

    private String posicion;

    public Alfil(String color) {
        super(color);
    }

    @Override
    public String getTipoFicha() {
        return "B"; // Representación del alfil
    }

    @Override
    public String getPosicion() {
        return posicion;
    }

    @Override
    public void setPosicion(String posicion) {
        this.posicion = posicion;
    }

    @Override
    public List<String> movimientosValidos(String posicion, Tablero tablero) {
        List<String> movimientos = new ArrayList<>();

        // Conversión de la posición a coordenadas
        int columna = posicion.charAt(0) - 'a'; // 'a' a 'h' -> 0 a 7
        int fila = 8 - Character.getNumericValue(posicion.charAt(1)); // '1' a '8' -> 7 a 0

        // Direcciones de movimiento del alfil: diagonales
        int[][] direcciones = {
                {-1, -1}, // Arriba a la izquierda
                {-1, 1},  // Arriba a la derecha
                {1, -1},  // Abajo a la izquierda
                {1, 1}    // Abajo a la derecha
        };

        // Iterar sobre cada dirección
        for (int[] direccion : direcciones) {
            int filaActual = fila;
            int columnaActual = columna;

            // Seguir moviéndose en esta dirección hasta encontrar un obstáculo
            while (true) {
                filaActual += direccion[0];
                columnaActual += direccion[1];

                if (!Tablero.esPosicionValida(filaActual, columnaActual)) {
                    break; // Fuera del tablero
                }

                if (tablero.getCasilla(filaActual, columnaActual).getFicha() == null) {
                    // Casilla vacía, movimiento válido
                    movimientos.add(tablero.convertirCoordenadasAId(filaActual, columnaActual));
                } else {
                    // Casilla ocupada, verificar si se puede capturar
                    if (!tablero.getCasilla(filaActual, columnaActual).getFicha().getColor().equals(color)) {
                        movimientos.add(tablero.convertirCoordenadasAId(filaActual, columnaActual));
                    }
                    break; // Detenerse al encontrar una pieza
                }
            }
        }

        return movimientos;
    }
}
