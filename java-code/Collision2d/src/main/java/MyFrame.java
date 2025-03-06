import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class MyFrame extends JFrame implements Runnable {

    public void displayFrame() {

        this.setLayout(new BorderLayout(50, 50));

        JFrame frame = new JFrame("Line status");
        frame.setSize(500, 300);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        JPanel panel = new JPanel(new BorderLayout()) {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                g.setFont(new Font("Allegra", Font.BOLD, 20));
            }
        };

        JLabel myLabel = new JLabel("Clear", SwingConstants.CENTER);
        panel.add(myLabel);

        // Set up label component for warning
        myLabel.setFont(new Font("Allegra", Font.BOLD, 24));
        myLabel.setPreferredSize(new Dimension(500, 250));
        myLabel.setHorizontalAlignment(SwingConstants.CENTER);
        myLabel.setVerticalAlignment(SwingConstants.CENTER);
        myLabel.setBackground(Color.GRAY);

        frame.add(panel);

        Timer timer = new Timer(100, new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {

                if (CollisionChecker.isJam) {
                    panel.setBackground(Color.RED);
                    myLabel.setText("<html><div  style='text-align: center;'>" + CollisionChecker.jamStatusString + "<br><br> Jam confidence: " +
                            CollisionChecker.collisionConfidence + "</div></html>");
                } else {
                    panel.setBackground(Color.GREEN);
                    myLabel.setText("<html><div  style='text-align: center;'>" + CollisionChecker.jamStatusString + "<br><br> Jam confidence: " +
                            CollisionChecker.collisionConfidence + "</div></html>");
                }
                panel.repaint();
            }
        });
        timer.start();

        frame.revalidate();
        frame.setVisible(true);
    }

    public void run() {
        SwingUtilities.invokeLater(this::displayFrame);
    }
}

