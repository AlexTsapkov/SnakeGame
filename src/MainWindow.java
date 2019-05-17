import javax.swing.*;

public class MainWindow extends JFrame {
    public MainWindow() {
        setTitle("Змейка");
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);   // При нажатии на крестик в правом верхнем углу экрана программа прекращает свою работу
        setSize(400, 427);   // Ширина и длина окна
        setLocation(500, 200);
        add(new GameField());
        setVisible(true);
    }

    public static void main(String[] args) {
        MainWindow mw = new MainWindow();
    }
}
