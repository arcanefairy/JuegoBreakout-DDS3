package breakoutgame;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.ArrayList;
import java.util.List;

public class Breakout extends JPanel implements ActionListener, KeyListener {
    private int pelotaX = 390, pelotaY = 550;  // Inicializa la posición de la bola
    private int plataformaX = 350;  // Inicializa la posición de la paleta
    private int velocidadPX = 1, velocidadPY = 2;  // Inicializa la velocidad de la bola
    private int puntaje = 0;  // Inicializa el puntaje del jugador
    private List<Rectangle> ladrillos = new ArrayList<>();  // Lista de ladrillos activos

    private Timer timer;
    private boolean juegoEnPausa = false;
    private int velocidadJuego = 15;
    private int gameTime = 0;
    private boolean ganaste = false;

    public Breakout() {
        this.setPreferredSize(new Dimension(800, 600));
        this.setBackground(Color.black);
        this.setFocusable(true);
        this.addKeyListener(this);
        timer = new Timer(velocidadJuego, this);
        timer.start();

        // Agrega los ladrillos a la lista
        int ladrilloAncho = 60;
        int ladrilloAlto = 20;
        for (int i = 0; i < 60; i++) {
            int ladrilloX = 70 + i % 10 * 70;
            int ladrilloY = 50 + i / 10 * 30;
            ladrillos.add(new Rectangle(ladrilloX, ladrilloY, ladrilloAncho, ladrilloAlto));
        }
    }

    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        // Dibuja la paleta
        g.setColor(Color.yellow);
        g.fillRect(plataformaX, 550, 100, 8);

        // Dibuja la bola
        g.setColor(Color.red);
        g.fillOval(pelotaX, pelotaY, 20, 20);

        // Dibuja los ladrillos
        g.setColor(Color.blue);
        for (Rectangle ladrillo : ladrillos) {
            g.fillRect(ladrillo.x, ladrillo.y, ladrillo.width, ladrillo.height);
        }

        // Dibuja el puntaje y el tiempo
        g.setColor(Color.white);
        g.setFont(new Font("Century Gothic", Font.PLAIN, 20));
        g.drawString("Puntaje: " + puntaje, 10, 20);
        g.drawString("Tiempo: " + (gameTime / 60) + "s " + (gameTime % 60) + "ms", 650, 20);

        if (ladrillos.isEmpty() && !ganaste) {
            ganaste = true;
            g.setColor(Color.green);
            g.setFont(new Font("Century Gothic", Font.BOLD, 30));
            g.drawString("GANASTE!", 350, 300);

            // Detiene la paleta y la pelota
            velocidadPX = 0;
            velocidadPY = 0;
        }

        if (pelotaY > 600) {
            g.setColor(Color.red);
            g.setFont(new Font("Century Gothic", Font.BOLD, 30));
            g.drawString("GAME OVER", 330, 300);
        }
    }

    public void actionPerformed(ActionEvent e) {
        if (!juegoEnPausa && !ganaste) {
            gameTime++;
            pelotaX += velocidadPX;
            pelotaY += velocidadPY;

            // Colisiones con la paleta
            if (pelotaY > 540 && pelotaX > plataformaX && pelotaX < plataformaX + 100) {
                velocidadPY = -2;
                puntaje++;
            }

            // Colisiones con los ladrillos
            Rectangle pelota = new Rectangle(pelotaX, pelotaY, 20, 20);
            List<Rectangle> ladrillosParaEliminar = new ArrayList<>();

            for (Rectangle ladrillo : ladrillos) {
                if (pelota.intersects(ladrillo)) {
                    velocidadPY = -velocidadPY;
                    ladrillosParaEliminar.add(ladrillo);
                    puntaje += 10;
                }
            }

            // Elimina los ladrillos golpeados
            ladrillos.removeAll(ladrillosParaEliminar);

            // Rebotes en los bordes
            if (pelotaX < 0 || pelotaX > 780) {
                velocidadPX = -velocidadPX;
            }
            if (pelotaY < 0) {
                velocidadPY = -velocidadPY;
            }

            // Game over si la bola cae al suelo
            if (pelotaY > 600) {
                timer.stop();
            }

            repaint();
        }
    }

    public void keyTyped(KeyEvent e) {}

    public void keyPressed(KeyEvent e) {
        int key = e.getKeyCode();
        if (key == KeyEvent.VK_LEFT && !juegoEnPausa && !ganaste) {
            if (plataformaX > 0) {
                plataformaX -= 20;
            }
        }
        if (key == KeyEvent.VK_RIGHT && !juegoEnPausa && !ganaste) {
            if (plataformaX < 700) {
                plataformaX += 20;
            }
        }
        if (key == KeyEvent.VK_SPACE) {
            juegoEnPausa = !juegoEnPausa;
        }
        if (key == KeyEvent.VK_P) {
            juegoEnPausa = !juegoEnPausa;
        }
        if (key == KeyEvent.VK_PLUS || key == KeyEvent.VK_ADD) {
            velocidadJuego -= 5;
            timer.setDelay(velocidadJuego);
        }
        if (key == KeyEvent.VK_MINUS || key == KeyEvent.VK_SUBTRACT) {
            velocidadJuego += 5;
            timer.setDelay(velocidadJuego);
        }
    }
    
    public void keyReleased(KeyEvent e) {}

    public static void main(String[] args) {
        JFrame frame = new JFrame("Breakout Game");
        Breakout game = new Breakout();
        frame.add(game);
        frame.pack();
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setVisible(true);
    }
}