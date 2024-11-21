package modelo;

import java.util.ArrayList;
import java.util.List;

public class Dama extends Ficha {

    private String posicion;

    public Dama(String color) {
        super(color);
    }

    @Override
    public String getTipoFicha() {
        return "Q"; // Representación de la dama
    }

    @Override
    public String getPosicion() {
        return posicion;
    }

    @Override
    public void cambiarMovimientoRealizado() {}

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

        // Direcciones de movimiento: combinando las de la torre y el alfil
        int[][] direcciones = {
                {-1, 0},  // Arriba
                {1, 0},   // Abajo
                {0, -1},  // Izquierda
                {0, 1},   // Derecha
                {-1, -1}, // Diagonal arriba-izquierda
                {-1, 1},  // Diagonal arriba-derecha
                {1, -1},  // Diagonal abajo-izquierda
                {1, 1}    // Diagonal abajo-derecha
        };

        // Iterar sobre cada dirección
        for (int[] direccion : direcciones) {
            int filaActual = fila;
            int columnaActual = columna;

            // Seguir moviéndose en esta dirección hasta encontrar un obstáculo
            while (true) {
                filaActual += direccion[0];
                columnaActual += direccion[1];

                // Verificar si la posición está dentro del tablero
                if (!Tablero.esPosicionValida(filaActual, columnaActual)) {
                    break; // Fuera del tablero
                }

                Casilla casillaActual = tablero.getCasilla(filaActual, columnaActual);
                Ficha fichaEnCasilla = casillaActual.getFicha();

                // Si la casilla está vacía, es un movimiento válido
                if (fichaEnCasilla == null) {
                    movimientos.add(tablero.convertirCoordenadasAId(filaActual, columnaActual));
                }
                // Si hay una ficha, verificar si se puede capturar
                else {
                    // Solo agregar si la ficha es de color diferente
                    if (!fichaEnCasilla.getColor().equals(this.color)) {
                        movimientos.add(tablero.convertirCoordenadasAId(filaActual, columnaActual));
                    }
                    // Detener la búsqueda en esta dirección después de encontrar una pieza
                    break;
                }
            }
        }

        return movimientos;
    }
}
