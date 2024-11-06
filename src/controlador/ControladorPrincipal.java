package controlador;

import funcionalidades.CargarIcon;
import modelo.Tablero;
import vista.PanelJuego;

import javax.swing.*;

public class ControladorPrincipal {
    private JFrame marco;

    public ControladorPrincipal(JFrame marco) {
        this.marco= marco;
        iniciarComponentes();
    }

    public void iniciarComponentes() {
        Tablero tablero = new Tablero();
        CargarIcon cargarIcon = new CargarIcon();
        PanelJuego panelJuego = new PanelJuego(tablero, cargarIcon);
        ControladorJuego controladorJuego = new ControladorJuego(tablero);
        marco.setContentPane(panelJuego);
    }
}
