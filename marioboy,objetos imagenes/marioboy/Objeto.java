package marioboy;

import java.awt.Graphics2D;
import java.awt.Image;
import javax.swing.ImageIcon;

public class Objeto {
    int x;
    int y;
    int ancho;
    int alto;
    Image imagen;

    public Objeto(int x, int y, int ancho, int alto, String rutaImagen) {
        this.x = x;
        this.y = y;
        this.ancho = ancho;
        this.alto = alto;
        cargarImagen(rutaImagen);
    }

    private void cargarImagen(String rutaImagen) {
        try {
            imagen = new ImageIcon(getClass().getResource(rutaImagen)).getImage();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void dibujar(Graphics2D g2d) {
        g2d.drawImage(imagen, x, y, ancho, alto, null);
    }

    public boolean colisiona(int x, int y, int ancho, int alto) {
        return x < this.x + this.ancho &&
               x + ancho > this.x &&
               y < this.y + this.alto &&
               y + alto > this.y;
    }
}