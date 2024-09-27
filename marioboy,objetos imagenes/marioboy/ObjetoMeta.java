package marioboy;

public class ObjetoMeta extends Objeto {
    private marioboy gameInstance;

    public ObjetoMeta(marioboy gameInstance, int x, int y, int ancho, int alto, String rutaImagen) {
        super(x, y, ancho, alto, rutaImagen);
        this.gameInstance = gameInstance;
    }

    @Override
    public boolean colisiona(int x, int y, int ancho, int alto) {
        if (super.colisiona(x, y, ancho, alto)) {
            marioboy.nivel++;
            marioboy.pj_x = marioboy.inicio_pj_x;
            marioboy.pj_y = marioboy.inicio_pj_y;
            gameInstance.inicializarObjetos();
            return true;
        }
        return false;
    }
}