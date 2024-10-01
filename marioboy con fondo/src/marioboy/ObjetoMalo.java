package marioboy;

import java.awt.Graphics2D;

public class ObjetoMalo extends Objeto {

    public ObjetoMalo(int x, int y, int ancho, int alto, String rutaImagen) {
        super(x, y, ancho, alto, rutaImagen);
    }
    
    @Override
    public boolean colisiona(int x, int y, int ancho, int alto) {
        if (super.colisiona(x, y, ancho, alto)) {
            marioboy.muertes++;
            marioboy.pj_x = marioboy.inicio_pj_x;
            marioboy.pj_y = marioboy.inicio_pj_y;
            return true;
        }
        return false;
    }

    @Override
    public void dibujar(Graphics2D g2d) {
        super.dibujar(g2d);
    }
}