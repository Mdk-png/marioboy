package marioboy;

import java.awt.Graphics2D;

public class ObjetoDecorador extends Objeto {
    private int zIndex; // Nuevo campo para controlar el orden de dibujado

    public ObjetoDecorador(int x, int y, int ancho, int alto, String rutaImagen, int zIndex) {
        super(x, y, ancho, alto, rutaImagen);
        this.zIndex = zIndex;
    }

    @Override
    public void dibujar(Graphics2D g2d) {
        super.dibujar(g2d);
    }

    @Override
    public boolean colisiona(int x, int y, int ancho, int alto) {
        return false; // Sin colisiones
    }

    public int getZIndex() {
        return zIndex;
    }
}