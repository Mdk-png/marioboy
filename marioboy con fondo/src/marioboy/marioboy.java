package marioboy;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.EventQueue;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.Timer;
import javax.swing.border.EmptyBorder;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import javax.swing.ImageIcon;
import java.awt.Image;
import java.awt.Toolkit;
import java.util.Comparator;

public class marioboy extends JFrame {

    private JPanel contentPane;
    private GamePanel gamePanel;
    private Image idleImage;
    private Image runImage;
    private Image jumpImage;
    private Image fallImage;
    private Image dashImage;
    private Image currentImage; // Imagen actual

    // PROPIEDADES DEL PJ
    static int pj_x = 40;
    static int pj_y = 560;
    static int inicio_pj_x = 40;
    static int inicio_pj_y = 560;
    int pj_ancho = 32;
    int pj_alto = 32;
    int velMov = 5;
    private Color dashColor = Color.RED;
    private Color defaultColor = Color.CYAN;
    
    // PROPIEDADES JUEGO
    int ypiso = 560;
    boolean rPressed = false, spacePressed = false, sPressed = false, aPressed = false, dPressed = false;
    private boolean isPaused = false;
    private boolean isGameOver = false;

    // Propiedades del dash
    int dashVel = 20;
    int dashDuration = 10;
    int dashTicks = 0;
    int dashCooldown = 500;
    int dashCooldownTicks = 0;
    boolean hasDashed = false;
    boolean canDashInAir = true;
    
 // Propiedades del salto
    boolean jumping = false;
    boolean onGround =false;
    int jumpVelocity = 0;
    int gravity = 2 ;
    int initialJumpVelocity = -28;
   int fallVelocity = 0;
    int maxFallVelocity = 10;
    // Niveles y muertes
    public static int nivel = 1;
    public static int muertes = 0;

    // Temporizador
    Timer timer;
    
    // Lista objeto y textos
    private List<Objeto> objetos;
    private List<ObjetoDecorador> objetosDecorativos; 
    private List<Texto> textos;
    
    // Partículas
    private List<Particula> particulas;
    private Random random = new Random();

    public static void main(String[] args) {
        EventQueue.invokeLater(new Runnable() {
            public void run() {
                try {
                    marioboy frame = new marioboy();
                    frame.setVisible(true);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }
    private void cargarImagenes() {
        try {
            idleImage = new ImageIcon(getClass().getResource("/imagen/pollo.gif")).getImage();
            runImage = new ImageIcon(getClass().getResource("/imagen/pollocorre.gif")).getImage();
            jumpImage = new ImageIcon(getClass().getResource("/imagen/POLLOSALTARIN.png")).getImage();
            fallImage = new ImageIcon(getClass().getResource("/imagen/POLLOCAYENDO.png")).getImage();
            dashImage = new ImageIcon(getClass().getResource("/imagen/POLLOLEVITA.png")).getImage();
            currentImage = idleImage; // Estado inicial
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public marioboy() {
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setBounds(10, 10, 1000, 700);
        setResizable(false);
        gamePanel = new GamePanel();
        gamePanel.setBorder(new EmptyBorder(5, 5, 5, 5));
        gamePanel.setLayout(new BorderLayout(0, 0));
        setContentPane(gamePanel);
        objetos = new ArrayList<>();
        objetosDecorativos = new ArrayList<>(); 
        inicializarObjetos();

        cargarImagenes();
        textos = new ArrayList<>();
        inicializarTextos();
        
        particulas = new ArrayList<>();
        
        
        timer = new Timer(16, new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                gamePanel.actionPerformed(e);
                repaint();
                if (dashCooldownTicks > 0) {
                    dashCooldownTicks--;
                }
            }
        });
        timer.start();

        addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                int key = e.getKeyCode();
                if (isGameOver) {
                    nivel = 1;
                    muertes = 0;
                    isGameOver = false;
                    inicializarObjetos();
                    inicializarTextos();
                    return;
                }
                if (key == KeyEvent.VK_SPACE) {
                    spacePressed = true;
                    startJump();
                }
                if (key == KeyEvent.VK_S) sPressed = true;
                if (key == KeyEvent.VK_A) aPressed = true;
                if (key == KeyEvent.VK_D) dPressed = true;
                if (key == KeyEvent.VK_N) dash();
                if (key == KeyEvent.VK_R) {                	
                	pj_y = inicio_pj_y;
                	pj_x = inicio_pj_x;
                }
                if (key == KeyEvent.VK_ESCAPE) {
                    isPaused = !isPaused; 
                    return;
                }
            }

            @Override
            public void keyReleased(KeyEvent e) {
                int key = e.getKeyCode();
                if (key == KeyEvent.VK_SPACE) spacePressed = false;
                if (key == KeyEvent.VK_S) sPressed = false;
                if (key == KeyEvent.VK_A) aPressed = false;
                if (key == KeyEvent.VK_D) dPressed = false;
            }
        });
        setFocusable(true);
    }

    void inicializarObjetos() {
        objetos.clear(); 
        objetosDecorativos.clear();
        if (nivel == 1) {
        	objetosDecorativos.add(new ObjetoDecorador(0, 0, 1000, 700, "/imagen/playa.gif", 0));
        	objetos.add(new Objeto(-1, ypiso - 900, 1, 1000, "/imagen/cubo.png")); //paredes
        	objetos.add(new Objeto(999, ypiso - 900, 5, 1000, "/imagen/cubo.png")); 

        	objetos.add(new Objeto(0, ypiso + 30, 1000, 100, "/imagen/pisopasto.png")); 
        	
        	objetos.add(new Objeto(300, ypiso - 50, 100, 40, "/imagen/ptarena.png")); 
            objetos.add(new Objeto(450, ypiso - 100, 50, 40, "/imagen/cubo.png"));
            objetos.add(new Objeto(550, ypiso - 50, 100, 40,"/imagen/ptarena.png" ));
            objetos.add(new Objeto(865, ypiso - 0, 150, 40,"/imagen/ptarena.png" ));
            objetos.add(new Objeto(900, ypiso - 50, 30, 2,"/imagen/ptarena.png")); 
            objetos.add(new ObjetoMeta(this,900, ypiso - 50, 30, 50, "/imagen/puerta.png")); 

            objetos.add(new ObjetoMovil(470, ypiso - 180, 100, 40, "/imagen/puerta.png", 4, 470, 740 ));
            objetos.add(new ObjetoMalo(316, ypiso + 0 , 127, 40, "/imagen/pinchos.png"));
        } 
        if (nivel == 2) {
        	objetos.add(new Objeto(-1, ypiso - 900, 1, 1000, "/imagen/cubo.png")); //paredes
        	objetos.add(new Objeto(999, ypiso - 900, 5, 1000, "/imagen/cubo.png")); 
            
            objetos.add(new ObjetoMovil(470, ypiso - 180, 100, 40, "/imagen/puerta.png", 4, 470, 740 ));
            objetos.add(new ObjetoMalo(316, ypiso + 0 , 127, 40, "/imagen/pinchos.png"));
        } 
        pj_x = inicio_pj_x;
        pj_y = inicio_pj_y;
        onGround = false;
    }
    
    void inicializarTextos() {
        textos.clear();

         if (nivel == 1) {
            textos.add(new Texto("TUTORIAL", getWidth() / 2 - 90, 60, 30, Color.white));
            textos.add(new Texto("A izquierda", getWidth() / 2 - 70, 100, 20, Color.white));
            textos.add(new Texto("D derecha", getWidth() / 2 - 70, 140, 20, Color.white));
            textos.add(new Texto("ESPACIO saltar", getWidth() / 2 - 90, 180, 20, Color.white));
            textos.add(new Texto("la heladera te teletransporta al siguiente nivel", getWidth() / 2 - 230, 200, 20, Color.WHITE));
        } else if (nivel == 2) {
            textos.add(new Texto("TUTORIAL", getWidth() / 2 - 90, 60, 30, Color.BLACK));
            textos.add(new Texto("R reiniciar nivel", getWidth() / 2 - 85, 100, 20, Color.BLACK));       
        } else if (nivel == 3) {
            textos.add(new Texto("TUTORIAL", getWidth() / 2 - 90, 60, 30, Color.BLACK));
            textos.add(new Texto("N dash", getWidth() / 2 - 50, 100, 20, Color.BLACK));
        } else if (nivel == 4) {
        	textos.add(new Texto("TUTORIAL", getWidth() / 2 - 90, 60, 30, Color.BLACK));
            textos.add(new Texto("Los objetos azules se mueven", getWidth() / 2 - 150, 100, 20, Color.BLACK));
        } else if (nivel == 5) {
        	textos.add(new Texto("TUTORIAL", getWidth() / 2 - 90, 60, 30, Color.BLACK));
            textos.add(new Texto("Los objetos rojos te matan", getWidth() / 2 - 150, 100, 20, Color.BLACK));
        }
    }


    private void startJump() {
        if (onGround) {
            jumping = true;
            onGround = false;
            jumpVelocity = initialJumpVelocity;
            crearParticulasSalto();
        }

}
    private void crearParticulasSalto() {
        for (int i = 0; i < 10; i++) {
            particulas.add(new Particula(pj_x + pj_ancho / 2, pj_y + pj_alto));
        }
    }

    private void performJump() {
        if (jumping || !onGround) {
            int nextY = pj_y + jumpVelocity;
            boolean colisionVertical = false;

            for (Objeto objeto : objetos) {
                if (objeto.colisiona(pj_x, (int)nextY, pj_ancho, pj_alto)) {
                    if (jumpVelocity > 0) { // Cayendo
                        pj_y = objeto.y - pj_alto;
                        onGround = true;
                        fallVelocity = 0;
                    } else { // Subiendo
                        pj_y = objeto.y + objeto.alto;
                        jumpVelocity = 0; // Detener el ascenso si golpea algo arriba
                    }
                    jumping = false;
                    colisionVertical = true;
                    break;
                }
            }

            if (!colisionVertical) {
                pj_y = nextY;
                if (pj_y >= ypiso) {
                    pj_y = ypiso;
                    onGround = true;
                    jumping = false;
                    jumpVelocity = 0;
                    fallVelocity = 0;
                } else {
                    onGround = false;
                }
            }

            if (jumping) {
                jumpVelocity += gravity;
                if (jumpVelocity > 0) {
                    jumping = false;
                    fallVelocity = jumpVelocity;
                }
            } else {
                fallVelocity += gravity;
                if (fallVelocity > maxFallVelocity) {
                    fallVelocity = maxFallVelocity;
                }
            }
        }
    }
    private void applyGravity() {
        if (!onGround) {
            int nextY = pj_y + fallVelocity;
            boolean colisionVertical = false;

            for (Objeto objeto : objetos) {
                if (objeto.colisiona(pj_x, (int)nextY, pj_ancho, pj_alto)) {
                    pj_y = objeto.y - pj_alto;
                    onGround = true;
                    fallVelocity = 0;
                    colisionVertical = true;
                    break;
                }
            }

            if (!colisionVertical) {
                pj_y = nextY;
                if (pj_y >= ypiso) {
                    pj_y = ypiso;
                    onGround = true;
                    fallVelocity = 0;
                } else {
                    onGround = false;
                    fallVelocity += gravity;
                    if (fallVelocity > maxFallVelocity) {
                        fallVelocity = maxFallVelocity;
                    }
                }
            }
        }
    }
    private void dash() {
        if (dashCooldownTicks <= 0 && !hasDashed && (onGround || canDashInAir)) {
            dashTicks = dashDuration;
            hasDashed = true;
            dashCooldownTicks = dashCooldown / 16;
        }
    }

    private void performDash() {
        if (dashTicks > 0) {
            int dashX = 0;
            if (aPressed) dashX -= dashVel;
            if (dPressed) dashX += dashVel;

            int futuroPJ_x = pj_x + dashX;

            boolean colision = false;
            for (Objeto objeto : objetos) {
                if (objeto.colisiona(futuroPJ_x, pj_y, pj_ancho, pj_alto)) {
                    colision = true;
                    break;
                }
            }

            if (!colision) {
                pj_x = futuroPJ_x;
            }

            dashTicks--;
            if (dashTicks <= 0) {
                hasDashed = false;
            }
        }
    }

    private boolean checkCollision(int x, int y, int ancho, int alto) {
        for (Objeto objeto : objetos) {
            if (objeto.colisiona(x, y, ancho, alto)) {
                return true;
            }
        }
        return false;
    }

    class GamePanel extends JPanel {
        @Override
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);
            Graphics2D g2d = (Graphics2D) g;
            objetosDecorativos.sort(Comparator.comparingInt(ObjetoDecorador::getZIndex));
            for (ObjetoDecorador objetoDecorador : objetosDecorativos) {
                objetoDecorador.dibujar(g2d);
            }
            g2d.setColor(Color.red);
            g2d.drawString("Nivel: " + nivel, 20, 20);
            g2d.drawString("Muertes: " + muertes, 20, 40);
            Image currentImage = idleImage; // Por defecto, estado idle

            if (dPressed || aPressed) {
                currentImage = runImage; // Estado correr
            }
            if (jumping) {
                currentImage = jumpImage; // Estado saltar
            }
            if (!onGround && fallVelocity > 0) {
                currentImage = fallImage; // Estado caer
            }
            if (hasDashed) {
                currentImage = dashImage; // Estado dash
            }

            // Dibujar la imagen del personaje
            g2d.drawImage(currentImage, pj_x, pj_y, null);
            
      
            dibujarObjeto(g2d, 0, ypiso + 40, getWidth(), 200, Color.BLACK);
            for (Objeto objeto : objetos) {
                objeto.dibujar(g2d);
            }
            for (Objeto objeto : objetos) {
                objeto.dibujar(g2d);
                if (objeto instanceof ObjetoMovil) {
                    ((ObjetoMovil) objeto).mover();
                }
            }
            for (Texto texto : textos) {
                texto.dibujar(g2d);
            }
            
            // Dibujar partículas
            for (int i = particulas.size() - 1; i >= 0; i--) {
                Particula p = particulas.get(i);
                p.actualizar();
                p.dibujar(g2d);
                if (p.vida <= 0) {
                    particulas.remove(i);
                }
            }

            if (marioboy.this.isPaused) {
                g2d.setColor(Color.BLACK);
                g2d.setFont(new Font("Arial", Font.BOLD, 48));
                g2d.drawString("PAUSADO", getWidth() / 2 - 100, getHeight() / 2);
            }

            if (isGameOver) {
                g2d.setColor(Color.RED);
                g2d.setFont(new Font("Arial", Font.BOLD, 48));
                g2d.drawString("FIN DEL JUEGO", getWidth() / 2 - 150, getHeight() / 2);
            }
        }

        public void actionPerformed(ActionEvent e) {
            if (!isPaused && !isGameOver) {
            	int futuroPJ_x = pj_x;

                // Movimiento
                if (dPressed && pj_x <= getWidth() - pj_ancho) {
                    futuroPJ_x += velMov;
                    currentImage = runImage; // Cambiar a correr
                } else if (aPressed && pj_x >= 0) {
                    futuroPJ_x -= velMov;
                    currentImage = runImage; // Cambiar a correr
                } else {
                    currentImage = idleImage; // Cambiar a idle
                }

                // Saltar
                if (jumping) {
                    currentImage = jumpImage;
                } else if (!onGround) {
                    currentImage = fallImage; // Caer
                }

                // Dash
                if (dashTicks > 0) {
                    currentImage = dashImage;
                }
                boolean colisionaEnX = false;
                for (Objeto objeto : objetos) {
                    if (objeto.colisiona(futuroPJ_x, pj_y, pj_ancho, pj_alto)) {
                        colisionaEnX = true;
                        break;
                    }
                }

                if (!colisionaEnX) {
                    pj_x = futuroPJ_x;
                }

                performJump();
                applyGravity();
                performDash();

                // Verificar si el personaje está sobre algún objeto
                boolean sobreObjeto = false;
                for (Objeto objeto : objetos) {
                    if (objeto.colisiona(pj_x, pj_y + 1, pj_ancho, pj_alto)) {
                        sobreObjeto = true;
                        onGround = true;
                        break;
                    }
                }

                // Si no está sobre un objeto, asegurarse de que no esté en el suelo
                if (!sobreObjeto && pj_y < ypiso) {
                    onGround = false;
                }

                // Verificar si el personaje ha caído fuera de la pantalla
                if (pj_y > getHeight()) {
                    pj_x = inicio_pj_x;
                    pj_y = inicio_pj_y;
                    muertes++;
                    onGround = true;
                    jumping = false;
                    fallVelocity = 0;
                }

                if (nivel == 10) {
                    isGameOver = true;
                }
            }
            repaint();
        }
        public void dibujarObjeto(Graphics2D g2d, int x, int y, int ancho, int alto, Color color) {
            g2d.setColor(color);
            g2d.fillRect(x, y, ancho, alto);
        }
    }

    class Particula {
        int x, y;
        int velocidadX, velocidadY;
        int vida;
        Color color;

        public Particula(int x, int y) {
            this.x = x;
            this.y = y;
            this.velocidadX = random.nextInt(5) - 2;
            this.velocidadY = random.nextInt(5) - 7;
            this.vida = random.nextInt(20) + 10;
            this.color = new Color(random.nextFloat(), random.nextFloat(), random.nextFloat());
        }

        public void actualizar() {
            x += velocidadX;
            y += velocidadY;
            velocidadY += 1; // Simular gravedad
            vida--;
        }

        public void dibujar(Graphics2D g2d) {
            g2d.setColor(color);
            g2d.fillRect(x, y, 3, 3);
        }
    }
}