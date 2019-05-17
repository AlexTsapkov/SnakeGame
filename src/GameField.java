import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.util.Random;

public class GameField extends JPanel implements ActionListener {
    private final int SIZE = 400;   // Максимальный размер поля
    private final int DOT_SIZE = 16;    //Размер в пикселях, змейки и яблока
    private final int ALL_DOTS = 400;   // Сколько всего игровых едениц может поместиться на игровом поле
    private Image dot, apple, bg, gameover;     // Картинки
    private int appleX, appleY;   // Х позиция и У позиция яблока
    private int[] x = new int[ALL_DOTS];   // Массив для Х позиции змейки
    private int[] y = new int[ALL_DOTS];   // Массив для У позиции змейки
    private int dots;   // Размер змейки в данный момент
    private Timer timer;   // Таймер
    private boolean left;   // Лево
    private boolean right = true;   // Право
    private boolean up;   // Вверх
    private boolean down;   // Вниз
    private boolean inGame = true;   // Мы в игре, или нет
    private int score = 0;
    private String strScore;

    public GameField() {
        setBackground(Color.WHITE);   // Устанавливаем цвет фона
        loadImage();
        initGame();
        addKeyListener(new fieldKeyListener());
        setFocusable(true);
    }

    public void initGame() {     // Метод "В игре", то есть играем или нет
        createGrape();
        dots = 3;
        for (int i = 0; i < dots; i++) {
            x[i] = 48 - i * DOT_SIZE;   // Устанавливает позицию для каждой ячейки змеи
            y[i] = 48;
        }
        timer = new Timer(100, this);
        timer.start();
    }


    public void createGrape() {     // Метод создает новый apple
        appleX = new Random().nextInt(20) * DOT_SIZE;
        appleY = new Random().nextInt(20) * DOT_SIZE;
        for (int i = dots; i > 0; i--) {
            if (x[i] == appleX && y[i] == appleY) {
                appleX = new Random().nextInt(20) * DOT_SIZE;
                appleY = new Random().nextInt(20) * DOT_SIZE;
                i++;
            } else {
                break;
            }
        }
    }

    public void loadImage() {   // Метод загружает картинки
        ImageIcon iig = new ImageIcon("apple.png");
        apple = iig.getImage();
        ImageIcon iid = new ImageIcon("snake.png");
        dot = iid.getImage();
        ImageIcon iibg = new ImageIcon("background2.png");
        bg = iibg.getImage();
        ImageIcon iigo = new ImageIcon("gameover.png");
        gameover = iigo.getImage();
    }

    public void move() {     // Метод отвечающий за передвижение змейки
        for (int i = dots; i > 0; i--) {
            x[i] = x[i - 1];
            y[i] = y[i - 1];
        }
        if (left) {
            x[0] -= DOT_SIZE;
        } else if (right) {
            x[0] += DOT_SIZE;
        } else if (up) {
            y[0] -= DOT_SIZE;
        } else if (down) {
            y[0] += DOT_SIZE;
        }
    }

    public void checkStar() {    // Метод проверяет съела ли змея apple
        if (x[0] == appleX && y[0] == appleY) {
            dots++;
            score += 10;
            createGrape();
        }
    }

    public void checkCollisions() {      // Метод проверяет не случилось ли столкновения
        for (int i = dots; i > 0; i--) {
            if (i > 0 && x[0] == x[i] && y[0] == y[i]) {
                inGame = false;
            }
        }
        if (x[0] > SIZE - 32) {
            inGame = false;
        } else if (x[0] < 0) {
            inGame = false;
        } else if (y[0] > SIZE - 48) {
            inGame = false;
        } else if (y[0] < 0) {
            inGame = false;
        }
    }

    @Override
    public void actionPerformed(ActionEvent e) {// Метод проверяет находимся ли мы еще в игре(еще не проиграли)
        if (inGame) {
            checkStar();
            checkCollisions();
            move();
        }
        repaint();
    }

    @Override
    protected void paintComponent(Graphics g) {     // Метод отрисовывающий все объекты в окне
        super.paintComponent(g);
        if (inGame) {
            g.drawImage(bg, 0, 0, this);
            g.drawImage(apple, appleX, appleY, this);
            for (int i = 0; i < dots; i++) {
                g.drawImage(dot, x[i], y[i], this);
            }
            g.drawImage(apple, 0, 371, this);
            showScore(g);
        } else {
            g.drawImage(bg, 0, 0, this);
            g.drawImage(gameover, 48, 146, this);
            g.drawImage(apple, 0, 371, this);
            showScore(g);
        }
    }

    public void showScore(Graphics g) {// Метод отрисовывающий счет
        strScore = " : " + String.valueOf(score);
        Font f = new Font("Calibri", Font.BOLD, 16);
        g.drawString(strScore, 16, 383);
    }

    class fieldKeyListener extends KeyAdapter {      // Метод проверяющий на какую клавишу нажали
        @Override
        public void keyPressed(KeyEvent e) {
            super.keyPressed(e);
            int key = e.getKeyCode();
            if (key == KeyEvent.VK_LEFT && !right) {
                left = true;
                up = false;
                down = false;
            }
            if (key == KeyEvent.VK_RIGHT && !left) {
                right = true;
                up = false;
                down = false;
            }

            if (key == KeyEvent.VK_UP && !down) {
                up = true;
                left = false;
                right = false;
            }
            if (key == KeyEvent.VK_DOWN && !up) {
                down = true;
                left = false;
                right = false;
            }
            if (key == KeyEvent.VK_ENTER && !inGame) {
                inGame = true;
                recreate();
            }
        }
    }

    public void recreate() {     // Метод обнуляющий все переменные при перезапуске игры
        dots = 3;
        score = 0;
        for (int i = 0; i < dots; i++) {
            x[i] = 48 - i * DOT_SIZE;   // Устанавливает позицию для каждой ячейки змеи
            y[i] = 48;
        }
        createGrape();
        left = false;
        right = true;
        up = false;
        down = false;
    }
}
