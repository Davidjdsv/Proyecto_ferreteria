package MenuPrincipal;

import javax.swing.*;
import java.awt.*;
import java.util.Objects;

public class JPanelBackground extends JPanel {
    private Image backgroundImage;

    public JPanelBackground(String imagePath) {
        backgroundImage = new ImageIcon(Objects.requireNonNull(getClass().getResource(imagePath))).getImage();
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        g.drawImage(backgroundImage, 0, 0, getWidth(), getHeight(), this);
    }
}
