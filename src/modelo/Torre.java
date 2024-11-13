package modelo;

public class Torre extends Ficha {

    public Torre(String color) {
        super(color);
    }

    @Override
    public String getTipoFicha() {
        return "R";
    }


    public boolean esMovimientoValido(int filaActual, int colActual, int filaDestino, int colDestino, Ficha[][] tablero) {
        if (filaActual != filaDestino && colActual != colDestino) {
            return false;
        }


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
}
