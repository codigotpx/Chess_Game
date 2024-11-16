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
        CargarPgn cargarPgn = new CargarPgn();
        this.movimientos = cargarPgn.obtenerMovimientos(ruta);
        this.cargarPgn = new CargarPgn();
        this.fichas = Arrays.asList("K", "Q", "R", "B", "N");
    }

    // Método para procesar el movivimiento
    public void procesarMovimiento(String mov) {
        tipoFicha = "P";
        destino = null;
        enroqueCorto = false;
        enroqueLargo = false;
        jacke = false;
        jackeMate = false;
        resultado = null;
        captura = false;

        // Verificar si es un resultado
        if (mov.equals("1-0") || mov.equals("0-1") || mov.equals("1/2-1/2")) {
            resultado = mov;
            return;
        }

        // Verificar si es un enroque corto o largo
        if (mov.equals("O-O")) {
            enroqueCorto = true;
            tipoFicha = "K";
            destino = "O-O";
            return;
        } else if (mov.equals("O-O-O")) {
            enroqueLargo = true;
            tipoFicha = "K";
            destino = "O-O-O";
            return;  // Aquí sale del método si es enroque largo
        }

        // Si el movimiento tiene 2 caracteres, probablemente es un peón moviéndose
        if (mov.length() == 2) {
            tipoFicha = "P";
            origenColumna = String.valueOf(mov.charAt(0));
            destino = mov;
            return;
        }

        // Si el movimiento tiene 3 caracteres, es una ficha moviéndose
        if (mov.length() == 3) {
            tipoFicha = String.valueOf(mov.charAt(0));
            destino = mov.substring(1);
            return;
        }

        // Si el movimiento tiene 4 caracteres, podría ser un movimiento con captura
        if (mov.length() == 4) {
            if (mov.charAt(1) != 'x') {
                tipoFicha = String.valueOf(mov.charAt(0));
                origenColumna = String.valueOf(mov.charAt(1));
                destino = mov.substring(2);
                return;
            } else {
                if (!fichas.contains(String.valueOf(mov.charAt(0)))) {
                    tipoFicha = "P";  // Es un peón
                    origenColumna = String.valueOf(mov.charAt(0));
                    destino = mov.substring(2);
                    captura = true;
                    return;
                } else {
                    tipoFicha = String.valueOf(mov.charAt(0));
                    captura = true;
                    destino = mov.substring(2);
                    return;
                }
            }
        }
    }

    public void procesarMovimientoSegunPosicion(int numero) {
        if (numero >= 0 && numero <= movimientos.size()) {
            procesarMovimiento(movimientos.get(numero));
        }
    }

    // Métodos para obtener los resultados del movimento que esta siendo procesado

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
