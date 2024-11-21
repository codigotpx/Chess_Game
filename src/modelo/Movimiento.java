package modelo;

import funcionalidades.CargarPgn;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Movimiento {
    private List<String> movimientos;
    private CargarPgn cargarPgn;
    private static String destino;
    private String origenColumna;
    private static String tipoFicha;
    private boolean enroqueCorto;
    private boolean enroqueLargo;
    private boolean jacke;
    private boolean jackeMate;
    private String resultado;
    private List<String> fichas;
    private boolean captura;

    public Movimiento(String ruta) {
        this.cargarPgn = new CargarPgn();
        this.movimientos = cargarPgn.obtenerMovimientos(ruta);
        this.fichas = Arrays.asList("K", "Q", "R", "B", "N");
    }

    public void procesarMovimiento(int movovmiento, Tablero tablero) {
        String mov = movimientos.get(movovmiento);

        System.out.println(mov);

        // Reiniciar todos los atributos
        reiniciarAtributos();

        // Verificar resultado del juego
        if (esResultadoJuego(mov)) return;

        // Verificar enroque
        if (esEnroque(mov)) return;

        // Procesar movimientos según longitud
        switch (mov.length()) {
            case 2:
                procesarMovimientoPeon(mov);
                break;
            case 3:
                procesarMovimientoFicha(mov);
                break;
            case 4:
                procesarMovimientoComplejo(mov);
                break;
            case 5:
                procesarMovimientoComplejo2(mov);
                break;
            default:
                throw new IllegalArgumentException("Movimiento no reconocido: " + mov);
        }
    }

    private void reiniciarAtributos() {
        tipoFicha = "P";
        destino = null;
        origenColumna = null;
        enroqueCorto = false;
        enroqueLargo = false;
        jacke = false;
        jackeMate = false;
        resultado = null;
        captura = false;
    }

    private boolean esResultadoJuego(String mov) {
        if (mov.equals("1-0") || mov.equals("0-1") || mov.equals("1/2-1/2")) {
            resultado = mov;
            return true;
        }
        return false;
    }

    private boolean esEnroque(String mov) {
        if (mov.equals("O-O")) {
            enroqueCorto = true;
            tipoFicha = "K";
            destino = "O-O";
            return true;
        } else if (mov.equals("O-O-O")) {
            enroqueLargo = true;
            tipoFicha = "K";
            destino = "O-O-O";
            return true;
        }
        return false;
    }

    private void procesarMovimientoPeon(String mov) {
        tipoFicha = "P";
        origenColumna = String.valueOf(mov.charAt(0));
        destino = mov;
    }

    private void procesarMovimientoFicha(String mov) {
        tipoFicha = String.valueOf(mov.charAt(0));
        destino = mov.substring(1);
        System.out.printf(" Tipo " + tipoFicha + " movimieto " + destino + " Origen " + origenColumna + "\n");
    }

    private void procesarMovimientoComplejo(String mov) {
        if (mov.charAt(1) == 'x') {
            if (!fichas.contains(String.valueOf(mov.charAt(0)))) {
                tipoFicha = "P";
                origenColumna = String.valueOf(mov.charAt(0));
                destino = mov.substring(2);
                captura = true;
            } else {
                tipoFicha = String.valueOf(mov.charAt(0));
                captura = true;
                destino = mov.substring(2);
            }
        } else if (mov.charAt(3) == '+') {
            tipoFicha = String.valueOf(mov.charAt(0));
            destino = mov.substring(1,3);
            jacke = true;
        } else {
            tipoFicha = String.valueOf(mov.charAt(0));
            origenColumna = String.valueOf(mov.charAt(1));
            destino = mov.substring(2);
        }

        System.out.printf(" Tipo " + tipoFicha + " movimieto " + destino + " Origen " + origenColumna + "\n");
    }

    public void procesarMovimientoComplejo2(String mov) {
        tipoFicha = String.valueOf(mov.charAt(0));
        captura = true;
        destino = mov.substring(2,4);
    }

    // [Resto de los métodos originales se mantienen igual]

    public String getTipoFicha() {
        return tipoFicha;
    }

    public String getDestino() {
        return destino;
    }

    public String getOrigenColumna() {
        return origenColumna;
    }

    public boolean isEnroqueCorto() {
        return enroqueCorto;
    }

    public boolean isEnroqueLargo() {
        return enroqueLargo;
    }

    public boolean isJacke() {
        return jacke;
    }

    public boolean isJackeMate() {
        return jackeMate;
    }

    public String getResultado() {
        return resultado;
    }

    public boolean isCaptura() {
        return captura;
    }

}