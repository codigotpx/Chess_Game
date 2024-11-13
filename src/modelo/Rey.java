package modelo;

public class Rey extends Ficha {

    public Rey(String color) {
        super(color);
    }

    @Override
    public String getTipoFicha() {
        return "K";
    }


    public boolean esMovimientoValido(int filaActual, int colActual, int filaDestino, int colDestino, Ficha[][] tablero, boolean enroquePermitido) {
        int diffFila = Math.abs(filaDestino - filaActual);
        int diffCol = Math.abs(colDestino - colActual);


        if (diffFila <= 1 && diffCol <= 1) {
            if (tablero[filaDestino][colDestino] == null || !tablero[filaDestino][colDestino].color.equals(this.color)) {
                return true;
            }
        }


        if (enroquePermitido && diffFila == 0 && diffCol == 2) {

            return true;
        }

        return false;
    }
}
