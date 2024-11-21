package modelo;

import java.util.ArrayList;
import java.util.List;

public class Caballo extends Ficha {

    private String posicion;

    public Caballo(String color) {
        super(color);
    }

    @Override
    public String getTipoFicha() {
        return "N"; // Representación del caballo
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

        // Movimientos del caballo en forma de "L"
        int[][] desplazamientos = {
                {-2, -1}, // Arriba izquierda
                {-2, 1},  // Arriba derecha
                {2, -1},  // Abajo izquierda
                {2, 1},   // Abajo derecha
                {-1, -2}, // Izquierda arriba
                {1, -2},  // Izquierda abajo
                {-1, 2},  // Derecha arriba
                {1, 2}    // Derecha abajo
        };

        // Iterar sobre cada posible movimiento
        for (int[] desplazamiento : desplazamientos) {
            int nuevaFila = fila + desplazamiento[0];
            int nuevaColumna = columna + desplazamiento[1];

            if (Tablero.esPosicionValida(nuevaFila, nuevaColumna)) {
                // Verificar si la casilla está libre o contiene una ficha del oponente
                Ficha fichaEnDestino = tablero.getCasilla(nuevaFila, nuevaColumna).getFicha();
                if (fichaEnDestino == null || !fichaEnDestino.getColor().equals(color)) {
                    movimientos.add(tablero.convertirCoordenadasAId(nuevaFila, nuevaColumna));
                }
            }
        }

        return movimientos;
    }
}
