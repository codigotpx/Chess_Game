package modelo;

public class Alfil extends Ficha {

    public Alfil(String color) {
        super(color);
    }

    @Override
    public String getTipoFicha() {
        return "B";
    }


    public boolean esMovimientoValido(int filaActual, int colActual, int filaDestino, int colDestino, Ficha[][] tablero) {
        int diffFila = Math.abs(filaDestino - filaActual);
        int diffCol = Math.abs(colDestino - colActual);

        if (diffFila != diffCol) {
            return false;
        }


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
