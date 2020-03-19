package graphics;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.geom.GeneralPath;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.Timer;

@SuppressWarnings("serial")
public class Main extends JPanel implements ActionListener {
    Timer timer;

    private static int maxWidth = 1600;
    private static int maxHeight = 900;

    private static int paddingX = 500;
    private static int paddingY = 250;

    private double angle = 0;

    private double dx = 0;
    private double tx = 0;
    private double dy = 5;
    private double ty = 0;

    private final double center_x = 210;
    private final double center_y = 125;

    public Main() {
        timer = new Timer(10, this);
        timer.start();
    }

    public void paint(Graphics g) {
        Graphics2D g2d = (Graphics2D) g;
        RenderingHints rh = new RenderingHints(RenderingHints.KEY_ANTIALIASING,
        RenderingHints.VALUE_ANTIALIAS_ON);
        rh.put(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);
        g2d.setRenderingHints(rh);

        g2d.setBackground(new Color(138, 3, 0));
        g2d.clearRect(0, 0, maxWidth, maxHeight);


        g2d.setColor(new Color(250, 250, 250));
        BasicStroke bs1 = new BasicStroke(16, BasicStroke.CAP_ROUND,
                BasicStroke.JOIN_ROUND);
        g2d.setStroke(bs1);
        g2d.drawRect(20, 20, 1560, 830);

        g2d.translate(tx, ty);

        g2d.rotate(angle, center_x, center_y);
        g2d.setColor(new Color(250, 250, 250));
        g2d.fillOval(260, 40, 130, 170);

        g2d.setColor(new Color(192, 192, 192));
        g2d.fillOval(265, 45, 120, 160);

        GradientPaint gp = new GradientPaint(5, 25, Color.YELLOW, 20, 2, Color.BLUE, true);
        g2d.setPaint(gp);
        g2d.fillOval(189,67,116, 116);
        g2d.fillOval(345,67,116, 116);

        g2d.setColor(new Color(0, 0, 0));

        double points[][] = {
                { 60, 120 }, { 360, 120 }, { 360, 130 }, { 60, 130 }
        };
        GeneralPath rect = new GeneralPath();
        rect.moveTo(points[0][0], points[0][1]);
        for (int k = 1; k < points.length; k++)
            rect.lineTo(points[k][0], points[k][1]);
        rect.closePath();
        g2d.fill(rect);

    }

    public void actionPerformed(ActionEvent e) {
        if ( tx < 0 ) {
            dx = 0;
            dy = 5;
            tx = 0;
        } else if ( tx > maxWidth - paddingX ) {
            dx = 0;
            dy = -5;
            tx = maxWidth - paddingX;
        }
        if ( ty < 0 ) {
            dy = 0;
            dx = -5;
            ty = 0;
        } else if ( ty > maxHeight - paddingY ) {
            dy = 0;
            dx = 5;
            ty = maxHeight - paddingY;
        }

        tx += dx;
        ty += dy;
        angle -= 0.1;
        repaint();
    }

    public static void main(String[] args) {
        JFrame frame = new JFrame("lab2");
        frame.add(new Main());
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(1600, 900);
        frame.setResizable(false);
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
        Dimension size = frame.getSize();
        Insets insets = frame.getInsets();
        maxWidth = size.width - insets.left - insets.right - 1;
        maxHeight = size.height - insets.top - insets.bottom - 1;
    }
}