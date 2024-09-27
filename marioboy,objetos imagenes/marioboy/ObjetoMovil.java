package marioboy;

import java.awt.Graphics2D;

public class ObjetoMovil extends Objeto {
    private int velocidad;
    private boolean moviendoDerecha;
    private int limiteIzquierdo;
    private int limiteDerecho;

    public ObjetoMovil(int x, int y, int ancho, int alto, String rutaImagen, int velocidad, int limiteIzquierdo, int limiteDerecho) {
        super(x, y, ancho, alto, rutaImagen);
        this.velocidad = velocidad;
        this.moviendoDerecha = true;
        this.limiteIzquierdo = limiteIzquierdo;
        this.limiteDerecho = limiteDerecho;
    }

    public void mover() {
        if (moviendoDerecha) {
            x += velocidad;
            if (x + ancho > limiteDerecho) {
                x = limiteDerecho - ancho;
                moviendoDerecha = false;
            }
        } else {
            x -= velocidad;
            if (x < limiteIzquierdo) {
                x = limiteIzquierdo;
                moviendoDerecha = true;
            }
        }
    }

    @Override
    public void dibujar(Graphics2D g2d) {
        super.dibujar(g2d);
    }
}