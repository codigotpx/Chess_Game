package modelo;

import java.util.ArrayList;
import java.util.List;

public class Tablero {
   private Casilla[][] tablero;
   private List<String> posicionInicial;
   private int tamañoTablero;
    private boolean verificandoAtaque;

    public Tablero() {
        this.tablero = new Casilla[8][8];
        this.posicionInicial = new ArrayList<>(List.of("R","N","B","Q","K","B","N","R"));
        this.tamañoTablero = 400;
        verificandoAtaque = false;
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
                    tablero[fila][col].getFicha().setPosicion(tablero[fila][col].getId());
                } else if (fila == 1) {
                    tablero[fila][col].setFicha(Ficha.crearPieza("P","B"));
                    tablero[fila][col].getFicha().setPosicion(tablero[fila][col].getId());
                } else if (fila == 6) {
                    tablero[fila][col].setFicha(Ficha.crearPieza("P","W"));
                    tablero[fila][col].getFicha().setPosicion(tablero[fila][col].getId());
                } else if (fila == 7) {
                    tablero[fila][col].setFicha(Ficha.crearPieza(posicionInicial.get(col),"W"));
                    tablero[fila][col].getFicha().setPosicion(tablero[fila][col].getId());
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

    public static boolean esPosicionValida(int fila, int columna) {
        return fila >= 0 && fila < 8 && columna >= 0 && columna < 8;
    }

    public String convertirCoordenadasAId(int fila, int columna) {
        char letraColumna = (char) ('a' + columna); // 0 -> 'a', 1 -> 'b', ..., 7 -> 'h'
        int numeroFila = 8 - fila; // 0 -> 8, 1 -> 7, ..., 7 -> 1
        return "" + letraColumna + numeroFila;
    }
    public int[] convertirIdACoordenadas(String id) {
        char letraColumna = id.charAt(0); // e.g., 'e'
        int numeroFila = Character.getNumericValue(id.charAt(1)); // e.g., 4
        int columna = letraColumna - 'a';
        int fila = 8 - numeroFila;
        return new int[]{fila, columna};
    }




    public boolean estaBajoAtaque(int fila, int columna, String colorRey) {
        if (verificandoAtaque) {
            return false; // Evitar recursión infinita
        }

        verificandoAtaque = true;

        String idCasilla = convertirCoordenadasAId(fila, columna);

        // Obtener todas las piezas enemigas
        List<Ficha> piezasEnemigas = obtenerFichasDelOponente(colorRey);

        for (Ficha ficha : piezasEnemigas) {
            String posicionFicha = ficha.getPosicion();
            int columnaFicha = posicionFicha.charAt(0) - 'a';
            int filaFicha = 8 - Character.getNumericValue(posicionFicha.charAt(1));

            if (ficha instanceof Peon) {
                // Evaluar ataques de peones
                int direccion = ficha.getColor().equals("W") ? -1 : 1;
                if (fila == filaFicha + direccion &&
                        (columna == columnaFicha - 1 || columna == columnaFicha + 1)) {
                    verificandoAtaque = false;
                    return true; // La casilla está bajo ataque de un peón
                }
            } else {
                // Evaluar movimientos válidos de otras piezas
                List<String> movimientos = ficha.movimientosValidos(posicionFicha, this);
                if (movimientos.contains(idCasilla)) {
                    verificandoAtaque = false;
                    return true; // La casilla está bajo ataque de esta pieza
                }
            }
        }

        verificandoAtaque = false;
        return false; // Ninguna pieza enemiga amenaza la casilla
    }




    // Método para obtener todas las fichas del oponente
    public List<Ficha> obtenerFichasDelOponente(String colorRey) {
        List<Ficha> fichasEnemigas = new ArrayList<>();

        for (int fila = 0; fila < 8; fila++) {
            for (int columna = 0; columna < 8; columna++) {
                Ficha ficha = getCasilla(fila, columna).getFicha();
                if (ficha != null && !ficha.getColor().equals(colorRey)) {
                    fichasEnemigas.add(ficha);
                }
            }
        }

        return fichasEnemigas;
    }

}
