package funcionalidades;

import javax.imageio.ImageIO;
import java.awt.*;
import java.io.IOException;
import java.net.URL;

public class CargarFondo {
    private Image fondo;
    public Image setFondo(String ruta) {
        try {
            // Cargar la imagen del fondo
            URL url = getClass().getClassLoader().getResource(ruta);
            if (url != null) {
                fondo = ImageIO.read(url);
            } else {
                System.out.println("No se encontró la ruta: " + ruta);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return fondo;
    }
}
