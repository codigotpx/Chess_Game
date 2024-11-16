package modelo;

import java.util.ArrayList;
import java.util.List;

public class Peon extends Ficha {

    private String posicion;

    private boolean primerMovimiento; // Para controlar si es el primer movimiento

    public Peon(String color) {
        super(color);
        // El peón está en su primer movimiento cuando es creado en la fila inicial
        this.primerMovimiento = true;
    }

    @Override
    public String getTipoFicha() {
        return "P";
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

        // Dirección del peón según su color
        int direccion = color.equals("W") ? -1 : 1; // Blanco sube, negro baja

        // Si es el primer movimiento, permitir movimiento de 1 o 2 casillas hacia adelante
        if (primerMovimiento) {
            // Movimiento normal (un paso adelante)
            if (Tablero.esPosicionValida(fila + direccion, columna) &&
                    tablero.getCasilla(fila + direccion, columna).getFicha() == null) {
                movimientos.add(tablero.convertirCoordenadasAId(fila + direccion, columna));

                // Movimiento doble (dos pasos adelante)
                if (Tablero.esPosicionValida(fila + 2 * direccion, columna) &&
                        tablero.getCasilla(fila + 2 * direccion, columna).getFicha() == null) {
                    movimientos.add(tablero.convertirCoordenadasAId(fila + 2 * direccion, columna));
                }
            }
        } else {
            // Movimiento normal (solo un paso adelante)
            if (Tablero.esPosicionValida(fila + direccion, columna) &&
                    tablero.getCasilla(fila + direccion, columna).getFicha() == null) {
                movimientos.add(tablero.convertirCoordenadasAId(fila + direccion, columna));
            }
        }

        // Captura diagonal izquierda
        if (Tablero.esPosicionValida(fila + direccion, columna - 1) &&
                tablero.getCasilla(fila + direccion, columna - 1).getFicha() != null &&
                !tablero.getCasilla(fila + direccion, columna - 1).getFicha().getColor().equals(color)) {
            movimientos.add(tablero.convertirCoordenadasAId(fila + direccion, columna - 1));
        }

        // Captura diagonal derecha
        if (Tablero.esPosicionValida(fila + direccion, columna + 1) &&
                tablero.getCasilla(fila + direccion, columna + 1).getFicha() != null &&
                !tablero.getCasilla(fila + direccion, columna + 1).getFicha().getColor().equals(color)) {
            movimientos.add(tablero.convertirCoordenadasAId(fila + direccion, columna + 1));
        }

        // Después de que el peón se mueva, cambiar a false, ya no podrá moverse dos casillas en el futuro
        primerMovimiento = false;

        return movimientos;
    }
}
