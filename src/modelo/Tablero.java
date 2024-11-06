package modelo;

import java.util.ArrayList;
import java.util.List;

public class Tablero {
   private Casilla[][] tablero;
   private List<String> posicionInicial;
   private int tamañoTablero;

    public Tablero() {
        this.tablero = new Casilla[8][8];
        this.posicionInicial = new ArrayList<>(List.of("R","N","B","Q","K","B","N","R"));
        this.tamañoTablero = 400;
        inicializarTablero();
    }

    // Método para inicializar el tablero
    private void inicializarTablero() {
        for (int fila = 0; fila < 8; fila++) {
            for(int col = 0; col < 8; col++) {
                String id = (char)('a' + col) + String.valueOf(8 - fila);
                tablero[fila][col] = new Casilla(id);

                if (fila == 0) {
                    tablero[fila][col].setFicha(Ficha.crearPieza(posicionInicial.get(col),"B"));
                } else if (fila == 1) {
                    tablero[fila][col].setFicha(Ficha.crearPieza("P","B"));
                } else if (fila == 6) {
                    tablero[fila][col].setFicha(Ficha.crearPieza("P","W"));
                } else if (fila == 7) {
                    tablero[fila][col].setFicha(Ficha.crearPieza(posicionInicial.get(col),"W"));
                }
            }
        }
    }

    // Método para obtener las casillas
    public Casilla getCasilla(int fila, int columna) {
        return tablero[fila][columna];
    }

    // Método para obtener el tamaño de las celdass
    public int getTamañoCelda() {
        return tamañoTablero / 8;
    }

    // Método para obtener el tamaño
    public int getTamañoTablero() {
        return tamañoTablero;
    }

}
