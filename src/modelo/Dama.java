package modelo;

public class Dama extends Ficha {

    public Dama(String color) {
        super(color);
    }

    @Override
    public String getTipoFicha() {
        return "Q";
    }


    public boolean esMovimientoValido(int filaActual, int colActual, int filaDestino, int colDestino, Ficha[][] tablero) {

        if (filaActual == filaDestino || colActual == colDestino) {
            return esMovimientoValidoTorre(filaActual, colActual, filaDestino, colDestino, tablero);
        }


        int diffFila = Math.abs(filaDestino - filaActual);
        int diffCol = Math.abs(colDestino - colActual);
        if (diffFila == diffCol) {
            return esMovimientoValidoAlfil(filaActual, colActual, filaDestino, colDestino, tablero);
        }

        return false;
    }


    private boolean esMovimientoValidoTorre(int filaActual, int colActual, int filaDestino, int colDestino, Ficha[][] tablero) {
        // Verificar si hay algo en el camino
        if (filaActual == filaDestino) {
            int inicio = Math.min(colActual, colDestino) + 1;
            int fin = Math.max(colActual, colDestino);
            for (int i = inicio; i < fin; i++) {
                if (tablero[filaActual][i] != null) {
                    return false;
                }
            }
        } else {
            int inicio = Math.min(filaActual, filaDestino) + 1;
            int fin = Math.max(filaActual, filaDestino);
            for (int i = inicio; i < fin; i++) {
                if (tablero[i][colActual] != null) {
                    return false;
                }
            }
        }

        return true;
    }


    private boolean esMovimientoValidoAlfil(int filaActual, int colActual, int filaDestino, int colDestino, Ficha[][] tablero) {
        int diffFila = Math.abs(filaDestino - filaActual);
        int dirFila = (filaDestino > filaActual) ? 1 : -1;
        int dirCol = (colDestino > colActual) ? 1 : -1;

        for (int i = 1; i < diffFila; i++) {
            if (tablero[filaActual + i * dirFila][colActual + i * dirCol] != null) {
                return false;
            }
        }

        return true;
    }
}
