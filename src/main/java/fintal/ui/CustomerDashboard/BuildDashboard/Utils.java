package main.java.fintal.ui.CustomerDashboard.BuildDashboard;

import javax.swing.*;
import java.awt.*;
import java.text.DecimalFormat;

public class Utils {
    // Utils Classes for Building Customer Dashboard
    public static class CounterLabel extends JLabel {
        public CounterLabel(double start, double end, int ms) {
            super();
            DecimalFormat fmt = new DecimalFormat("#,##0.00");
            int frames = Math.max(1, ms / 30);
            double step = (end - start) / frames;

            final double[] current = {start};

            Timer t = new Timer(30, null);
            t.addActionListener(e -> {
                current[0] += step;
                if (current[0] >= end) {
                    current[0] = end;
                    t.stop();
                }
                setText("<html>"
                        + "<span style='font-size:10px;'>PKR</span> "
                        + "<span style='font-size:30px;'>" + fmt.format(current[0]) + "</span>"
                        + "</html>");

            });
            t.setInitialDelay(200);
            t.start();
        }

    }


    public static class AlphaFadeLabel extends JLabel {
        private float alpha = 0;

        public AlphaFadeLabel(String txt, int ms) {
            super(txt);
            setForeground(new Color(0, 0, 0, 0));
            int frames = Math.max(1, ms / 30);
            float delta = 1f / frames;
            Timer t = new Timer(30, null);
            t.addActionListener(e -> {
                alpha += delta;
                if (alpha >= 1) {
                    alpha = 1;
                    t.stop();
                }
                setForeground(new Color(0, 0, 0, (int) (alpha * 255)));
            });
            t.start();
        }
    }

    public static class RoundedPanel extends JPanel {
        private final int r;

        public RoundedPanel(int r) {
            this.r = r;
            setOpaque(false);
        }

        protected void paintComponent(Graphics g) {
            Graphics2D g2 = (Graphics2D) g.create();
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            g2.setColor(getBackground());
            g2.fillRoundRect(0, 0, getWidth(), getHeight(), r, r);
            super.paintComponent(g2);
            g2.dispose();
        }
    }
}
