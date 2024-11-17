package controlador;

import funcionalidades.CargarPgn;
import modelo.Casilla;
import modelo.Ficha;
import modelo.Movimiento;
import modelo.Tablero;
import vista.PanelJuego;

import javax.swing.*;
import java.awt.event.ActionListener;
import java.io.File;
import java.util.List;

public class ControladorJuego {
    private Tablero tablero;
    private CargarPgn cargarPgn;
    private PanelJuego panelJuego;
    private Movimiento movimiento;
    private int numeroMovimiento;


    public ControladorJuego(Tablero tablero, CargarPgn cargarPgn, PanelJuego panelJuego) {
        this.tablero = tablero;
        this.cargarPgn = cargarPgn;
        this.panelJuego = panelJuego;
        this.movimiento = null;
        this.numeroMovimiento = 0;

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
        movimiento.procesarMovimientoSegunPosicion(numeroMovimiento, tablero);

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
            manejarEnroque(enroqueCorto, enroqueLargo);
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
        Casilla destinoCasilla = tablero.getCasilla(filaDestino, columnaDestino);
        destinoCasilla.setFicha(origen.getFicha()); // Mover la ficha a la casilla destino
        destinoCasilla.getFicha().setPosicion(destino); // Actualizar la posición de la ficha
        origen.setFicha(null); // Vaciar la casilla de origen

        // Actualizar la vista
        panelJuego.actualizarVistaTablero(tablero);

        // Mensaje de confirmación
        JOptionPane.showMessageDialog(panelJuego, "Movimiento realizado: " + tipoFicha + " a " + destino);
    }

    private Casilla encontrarCasillaOrigen(String tipoFicha, String origenColumna, int filaDestino, int columnaDestino, boolean captura) {
        for (int fila = 0; fila < 8; fila++) {
            for (int columna = 0; columna < 8; columna++) {
                Casilla casilla = tablero.getCasilla(fila, columna);
                Ficha ficha = casilla.getFicha();

                if (ficha != null && ficha.getTipoFicha().equals(tipoFicha)) {
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

    private void manejarEnroque(boolean enroqueCorto, boolean enroqueLargo) {
        if (enroqueCorto) {
            // Mover el rey y la torre para el enroque corto
            tablero.getCasilla(7, 6).setFicha(tablero.getCasilla(7, 4).getFicha());
            tablero.getCasilla(7, 6).getFicha().setPosicion("g1");
            tablero.getCasilla(7, 4).setFicha(null);

            tablero.getCasilla(7, 5).setFicha(tablero.getCasilla(7, 7).getFicha());
            tablero.getCasilla(7, 5).getFicha().setPosicion("f1");
            tablero.getCasilla(7, 7).setFicha(null);
        } else if (enroqueLargo) {
            // Mover el rey y la torre para el enroque largo
            tablero.getCasilla(7, 2).setFicha(tablero.getCasilla(7, 4).getFicha());
            tablero.getCasilla(7, 2).getFicha().setPosicion("c1");
            tablero.getCasilla(7, 4).setFicha(null);

            tablero.getCasilla(7, 3).setFicha(tablero.getCasilla(7, 0).getFicha());
            tablero.getCasilla(7, 3).getFicha().setPosicion("d1");
            tablero.getCasilla(7, 0).setFicha(null);
        }

        // Actualizar la vista
        panelJuego.actualizarVistaTablero(tablero);
        JOptionPane.showMessageDialog(panelJuego, "Enroque realizado.");
    }





}
