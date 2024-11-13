package modelo;

public class Caballo extends Ficha {

    public Caballo(String color) {
        super(color);
    }

    @Override
    public String getTipoFicha() {
        return "N";
    }


    public boolean esMovimientoValido(int filaActual, int colActual, int filaDestino, int colDestino, Ficha[][] tablero) {
        int diffFila = Math.abs(filaDestino - filaActual);
        int diffCol = Math.abs(colDestino - colActual);


        if ((diffFila == 2 && diffCol == 1) || (diffFila == 1 && diffCol == 2)) {
            if (tablero[filaDestino][colDestino] == null || !tablero[filaDestino][colDestino].color.equals(this.color)) {
                return true;
            }
        }

        return false;
    }
}
