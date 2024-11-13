package modelo;

public class Peon extends Ficha {

    public Peon(String color) {
        super(color);
    }

    @Override
    public String getTipoFicha() {
        return "P";
    }


    public boolean esMovimientoValido(int filaActual, int colActual, int filaDestino, int colDestino, Ficha[][] tablero) {
        int direccion = this.color.equals("blanco") ? -1 : 1;  // Peones blancos suben, negros bajan


        if (colActual == colDestino && tablero[filaDestino][colDestino] == null) {

            if ((filaActual == 6 && this.color.equals("blanco") || filaActual == 1 && this.color.equals("negro")) &&
                    filaActual + 2 * direccion == filaDestino) {
                return true;
            }

            if (filaActual + direccion == filaDestino) {
                return true;
            }
        }


        if (Math.abs(colDestino - colActual) == 1 && filaActual + direccion == filaDestino &&
                tablero[filaDestino][colDestino] != null && !tablero[filaDestino][colDestino].color.equals(this.color)) {
            return true;
        }

        return false;
    }
}
