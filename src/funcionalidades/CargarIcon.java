package funcionalidades;

import javax.swing.*;
import java.awt.*;
import java.util.Objects;

public class CargarIcon {
    private String ruta;

    public Image cargarIcon(String ruta) {
        Image imagen = null;
        try {
            imagen = new ImageIcon(Objects.requireNonNull(getClass().getResource(ruta))).getImage();
        } catch (Exception e) {
            System.out.println("Error al cargar imagen" + ruta);
            e.printStackTrace();
        }
        return imagen;
    }
}
