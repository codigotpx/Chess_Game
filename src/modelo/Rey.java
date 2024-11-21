package modelo;

import java.util.ArrayList;
import java.util.List;

public class Rey extends Ficha {
    private boolean primerMovimiento;

    private String posicion;

    public Rey(String color) {
        super(color);

        this.primerMovimiento = true;
    }

    @Override
    public String getTipoFicha() {
        return "K"; // Representación del rey
    }

    @Override
    public void cambiarMovimientoRealizado() {}

    @Override
    public void setPosicion(String possicion) {
        this.posicion = possicion;
    }

    @Override
    public String getPosicion() {
        return posicion;
    }

    @Override
    public List<String> movimientosValidos(String posicion, Tablero tablero) {
        List<String> movimientos = new ArrayList<>();

        // Conversión de la posición del Rey a coordenadas
        int columna = posicion.charAt(0) - 'a';       // Convierte columna 'a'-'h' a 0-7
        int fila = 8 - Character.getNumericValue(posicion.charAt(1)); // Convierte fila '1'-'8' a 7-0

        // Movimientos posibles del Rey (una casilla en cualquier dirección)
        int[][] desplazamientos = {
                {-1, 0},  // Arriba
                {1, 0},   // Abajo
                {0, -1},  // Izquierda
                {0, 1},   // Derecha
                {-1, -1}, // Diagonal arriba-izquierda
                {-1, 1},  // Diagonal arriba-derecha
                {1, -1},  // Diagonal abajo-izquierda
                {1, 1}    // Diagonal abajo-derecha
        };

        // Iterar sobre cada desplazamiento
        for (int[] desplazamiento : desplazamientos) {
            int nuevaFila = fila + desplazamiento[0];
            int nuevaColumna = columna + desplazamiento[1];

            // Verificar que la nueva posición está dentro del tablero
            if (Tablero.esPosicionValida(nuevaFila, nuevaColumna)) {
                Ficha fichaEnDestino = tablero.getCasilla(nuevaFila, nuevaColumna).getFicha();

                // Verificar que no se mueve a una casilla ocupada por un aliado
                if (fichaEnDestino == null || !fichaEnDestino.getColor().equals(this.color)) {
                    // Verificar que la casilla no está bajo ataque
                    if (!tablero.estaBajoAtaque(nuevaFila, nuevaColumna, this.color)) {
                        movimientos.add(tablero.convertirCoordenadasAId(nuevaFila, nuevaColumna));
                    }
                }
            }
        }

        this.primerMovimiento = true;

        return movimientos;
    }
}
