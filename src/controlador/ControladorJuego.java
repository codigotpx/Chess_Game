package controlador;

import funcionalidades.CargarPgn;
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
        // Lógica para avanzar el movimento
        movimiento.procesarMovimientoSegunPosicion(numeroMovimiento);
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
}
