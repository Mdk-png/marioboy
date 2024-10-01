package marioboy;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;

public class Texto {
    private String texto;
    private int x;
    private int y;
    private int tamañoFuente;
    private Color color;

    public Texto(String texto, int x, int y, int tamañoFuente, Color color) {
        this.texto = texto;
        this.x = x;
        this.y = y;
        this.tamañoFuente = tamañoFuente;
        this.color = color;
    }

    public void dibujar(Graphics2D g2d) {
        g2d.setColor(color);
        g2d.setFont(new Font("Arial", Font.PLAIN, tamañoFuente));
        g2d.drawString(texto, x, y);
    }
}