package controlador;

import funcionalidades.CargarPgn;
import modelo.Casilla;
import modelo.Ficha;
import modelo.Movimiento;
import modelo.Tablero;
import vista.PanelJuego;
import vista.TableroBoton;

import javax.swing.*;
import java.io.File;
import java.util.List;

public class ControladorJuego {
    private Tablero tablero;
    private CargarPgn cargarPgn;
    PanelJuego panelJuego;
    private Movimiento movimiento;
    private int numeroMovimiento;
    private boolean isBlanca; // true: blanca false: negro
    private String colorActual;


    public ControladorJuego(Tablero tablero, CargarPgn cargarPgn, PanelJuego panelJuego) {
        this.tablero = tablero;
        this.cargarPgn = cargarPgn;
        this.panelJuego = panelJuego;
        this.movimiento = null;
        this.numeroMovimiento = 0;
        this.isBlanca = true;
        this.colorActual = "";


        iniciarListener();
    }

    private void iniciarListener() {
        // Listener para avanzar al siguiente movimiento
        this.panelJuego.addBotonSiguienteListener(e -> avanzarMovimeinto());

        // Listener para retroceder al movimiento anterior
        this.panelJuego.addBotonRetrocederListener(e -> retrocederMovimeinto());

        // Listener para cargar el PGN
        this.panelJuego.addBotonCargarPGNListener(e -> cargarArchivoPGN());
    }


    public void avanzarMovimeinto() {
        if (movimiento == null) {
            JOptionPane.showMessageDialog(panelJuego, "No se ha cargado un archivo PGN.");
            return;
        }
        ejecutarMovimiento(numeroMovimiento);
        numeroMovimiento++;
    }



    public void retrocederMovimeinto() {
        // Lógica oara retroceder el movimiento
    }

    public void cargarArchivoPGN() {
        // lógica para cargar el archivo PGN}
        JFileChooser fileChooser = new JFileChooser();
        int seleccion = fileChooser.showOpenDialog(panelJuego);

        if (seleccion == JFileChooser.APPROVE_OPTION) {
            File archivoPGN = fileChooser.getSelectedFile();
            String ruta = archivoPGN.getAbsolutePath();
            movimiento = new Movimiento(ruta);
            JOptionPane.showMessageDialog(panelJuego, "Archivo PGN cargado exitosamente.");

        }
    }

    public void ejecutarMovimiento(int numeroMovimiento) {
        // Procesar el movimiento actual usando la clase Movimiento
        movimiento.procesarMovimiento(numeroMovimiento, tablero);

        // Obtener los datos procesados
        String tipoFicha = movimiento.getTipoFicha();
        String destino = movimiento.getDestino();
        String origenColumna = movimiento.getOrigenColumna();
        boolean captura = movimiento.isCaptura();
        boolean enroqueCorto = movimiento.isEnroqueCorto();
        boolean enroqueLargo = movimiento.isEnroqueLargo();

        // Manejar resultados especiales
        if (movimiento.getResultado() != null) {
            JOptionPane.showMessageDialog(panelJuego, "Resultado de la partida: " + movimiento.getResultado());
            return;
        }

        // Manejar enroques
        if (enroqueCorto || enroqueLargo) {
            cambiarColorMovimiento();
            manejarEnroque(enroqueCorto, enroqueLargo, isBlanca);
            return;
        }

        // Obtener las coordenadas de la casilla de destino
        int filaDestino = 8 - Character.getNumericValue(destino.charAt(1));
        int columnaDestino = destino.charAt(0) - 'a';

        // Encontrar la casilla de origen
        Casilla origen = encontrarCasillaOrigen(tipoFicha, origenColumna, filaDestino, columnaDestino, captura);

        if (origen == null) {
            JOptionPane.showMessageDialog(panelJuego, "Movimiento inválido. No se encontró una ficha adecuada para mover.");
            return;
        }

        // Mover la ficha en el modelo
        if (tipoFicha == "P") {
            origen.getFicha().cambiarMovimientoRealizado();
        }
        Casilla destinoCasilla = tablero.getCasilla(filaDestino, columnaDestino);
        destinoCasilla.setFicha(origen.getFicha()); // Mover la ficha a la casilla destino
        destinoCasilla.getFicha().setPosicion(destino); // Actualizar la posición de la ficha
        origen.setFicha(null); // Vaciar la casilla de orige

        cambiarColorMovimiento();



        // Actualizar la vista
        panelJuego.actualizarVistaTablero(tablero);

        // Mensaje de confirmación
        JOptionPane.showMessageDialog(panelJuego, "Movimiento realizado: " + tipoFicha + " a " + destino);
    }

    private Casilla encontrarCasillaOrigen(String tipoFicha, String origenColumna, int filaDestino, int columnaDestino, boolean captura) {

        colorActual = isBlanca ? "W" : "B";

        for (int fila = 0; fila < 8; fila++) {
            for (int columna = 0; columna < 8; columna++) {
                Casilla casilla = tablero.getCasilla(fila, columna);
                Ficha ficha = casilla.getFicha();

                if (ficha != null && ficha.getTipoFicha().equals(tipoFicha) && ficha.getColor().equals(colorActual)) {
                    // Validar que la ficha pueda llegar a la casilla destino
                    String posicionActual = tablero.convertirCoordenadasAId(fila, columna);
                    List<String> movimientosValidos = ficha.movimientosValidos(posicionActual, tablero);

                    if (movimientosValidos.contains(tablero.convertirCoordenadasAId(filaDestino, columnaDestino))) {
                        // Si se especificó la columna de origen, valida que coincida
                        if (origenColumna != null && !posicionActual.startsWith(origenColumna)) {
                            continue;
                        }

                        return casilla; // Ficha encontrada
                    }
                }
            }
        }

        return null; // No se encontró una ficha válida
    }

    private void manejarEnroque(boolean enroqueCorto, boolean enroqueLargo, boolean esBlanca) {
        int fila = esBlanca ? 0 : 7; // Fila 7 para blancas, fila 0 para negras

        if (enroqueCorto) {
            // Mover el rey y la torre para el enroque corto
            tablero.getCasilla(fila, 6).setFicha(tablero.getCasilla(fila, 4).getFicha());
            tablero.getCasilla(fila, 6).getFicha().setPosicion(esBlanca ? "g1" : "g8");
            tablero.getCasilla(fila, 4).setFicha(null);

            tablero.getCasilla(fila, 5).setFicha(tablero.getCasilla(fila, 7).getFicha());
            tablero.getCasilla(fila, 5).getFicha().setPosicion(esBlanca ? "f1" : "f8");
            tablero.getCasilla(fila, 7).setFicha(null);
        } else if (enroqueLargo) {
            // Mover el rey y la torre para el enroque largo
            tablero.getCasilla(fila, 2).setFicha(tablero.getCasilla(fila, 4).getFicha());
            tablero.getCasilla(fila, 2).getFicha().setPosicion(esBlanca ? "c1" : "c8");
            tablero.getCasilla(fila, 4).setFicha(null);

            tablero.getCasilla(fila, 3).setFicha(tablero.getCasilla(fila, 0).getFicha());
            tablero.getCasilla(fila, 3).getFicha().setPosicion(esBlanca ? "d1" : "d8");
            tablero.getCasilla(fila, 0).setFicha(null);
        }


    // Actualizar la vista
        panelJuego.actualizarVistaTablero(tablero);
        JOptionPane.showMessageDialog(panelJuego, "Enroque realizado.");
    }

    public void cambiarColorMovimiento() {
        isBlanca = !isBlanca;
    }
}
