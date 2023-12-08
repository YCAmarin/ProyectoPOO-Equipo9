import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.image.BufferedImage;
import javax.imageio.ImageIO;
import java.util.Random;

class Plataforma {
    int x, y;
}

public class Window extends JFrame implements Runnable, KeyListener {
    public static final int WIDTH = 400, HEIGHT = 600;
    boolean isRunning;
    static Thread thread;
    BufferedImage view, pantallatrasera, personaje, plataforma;

    Plataforma[] plataformas;
    int x = 100, y = 100, h = 150;
    float dy = 0;
    boolean right, left;

    int score = 0; // Variable para el puntaje

    public Window() {
        setPreferredSize(new Dimension(WIDTH, HEIGHT));
        addKeyListener(this);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            JFrame w = new Window();
            w.setResizable(false);
            w.pack();
            w.setLocationRelativeTo(null);
            w.setVisible(true);
        });
        try {
            thread.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void addNotify() {
        super.addNotify();
        if (thread == null) {
            thread = new Thread(this);
            isRunning = true;
            thread.start();
        }
    }

    public void start() {
        try {
            view = new BufferedImage(WIDTH, HEIGHT, BufferedImage.TYPE_INT_RGB);
            pantallatrasera = ImageIO.read(getClass().getResource("Fondo.png"));
            plataforma = ImageIO.read(getClass().getResource("Plataforma.png"));
            personaje = ImageIO.read(getClass().getResource("personaje.png"));

            plataformas = new Plataforma[20];
            for (int i = 0; i < 10; i++) {
                plataformas[i] = new Plataforma();
                plataformas[i].x = new Random().nextInt(300);
                plataformas[i].y = new Random().nextInt(400);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void update() {
        if (right) {
            x += 3;
            if (x > WIDTH) {
                x = -30;
            }
        }
        if (left) {
            x -= 3;
            if (x < -30) {
                x = WIDTH;
            }
        }
        dy += 0.2;
        y += dy;
        if (y > HEIGHT) {
            showFinalScore();
            restartGame();
        }
        if (y > HEIGHT + 70) {
            dy = -10;
        }
        if (y < h) {
            for (int i = 0; i < 10; i++) {
                y = h;
                plataformas[i].y = plataformas[i].y - (int) dy;
                if (plataformas[i].y > HEIGHT) {
                    plataformas[i].y = 0;
                    plataformas[i].x = new Random().nextInt(WIDTH);
                }
            }
        }
        for (int i = 0; i < 10; i++) {
            if ((x + 50 > plataformas[i].x) && (x + 20 < plataformas[i].x + 68)
                    && (y + 70 > plataformas[i].y) && (y + 70 < plataformas[i].y + 14) &&
                    (dy > 0)) {
                dy = -10;
            }
        }

        score++;
    }

    private void restartGame() {
        x = 100;
        y = 100;
        dy = 0;
        right = false;
        left = false;
        score = 0; // Reinicia el puntaje

        for (int i = 0; i < 10; i++) {
            plataformas[i].x = new Random().nextInt(300);
            plataformas[i].y = new Random().nextInt(500);
        }
    }

    public void draw() {
        Graphics2D g2 = (Graphics2D) view.getGraphics();
        g2.drawImage(pantallatrasera, 0, 0, WIDTH, HEIGHT, null);
        g2.drawImage(personaje, x, y, 30, 80, null);
        for (int i = 0; i < 10; i++) {
            g2.drawImage(plataforma, plataformas[i].x, plataformas[i].y, 80, 30, null);
        }

        // Dibujar el puntaje en la esquina superior derecha
        g2.setColor(Color.BLACK);
        g2.setFont(new Font("Arial", Font.BOLD, 20));
        g2.drawString("Puntaje: " + score, WIDTH - 130, 100);

        Graphics g = getGraphics();
        g.drawImage(view, 0, 0, WIDTH, HEIGHT, null);
        g.dispose();
    }

    // Método para mostrar el puntaje final
    private void showFinalScore() {
        JOptionPane.showMessageDialog(this, "¡Perdiste! Tu puntaje es: " + score + ". Tienes que reiniciar.");
    }

    @Override
    public void run() {
        try {
            requestFocus();
            start();
            while (isRunning) {
                update();
                draw();
                Thread.sleep(1000 / 60);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void keyTyped(KeyEvent e) {
        // Implementación de keyTyped si es necesario
    }

    @Override
    public void keyPressed(KeyEvent e) {
        if (e.getKeyCode() == KeyEvent.VK_RIGHT) {
            right = true;
        }
        if (e.getKeyCode() == KeyEvent.VK_LEFT) {
            left = true;
        }
        // Agregar más código aquí si es necesario
    }

    @Override
    public void keyReleased(KeyEvent e) {
        if (e.getKeyCode() == KeyEvent.VK_RIGHT) {
            right = false;
        }
        if (e.getKeyCode() == KeyEvent.VK_LEFT) {
            left = false;
        }
        // Agregar más código aquí si es necesario
    }
}
