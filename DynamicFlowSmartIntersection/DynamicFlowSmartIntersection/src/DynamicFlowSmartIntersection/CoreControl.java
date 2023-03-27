package DynamicFlowSmartIntersection;

import java.awt.Color;
import java.awt.EventQueue;
import java.awt.Graphics;
import java.awt.Point;
import java.util.ArrayList;
import java.util.List;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;

import static DynamicFlowSmartIntersection.SmartIntersectionMain.usedColor;
import static DynamicFlowSmartIntersection.SmartIntersectionMain.grid;

public class CoreControl {

    public static class Grid extends JPanel {
        private List<Point> fillCells;
        public Grid() {
            fillCells = new ArrayList<>(25);
        }

        @Override
        protected synchronized void paintComponent(Graphics g) {
            super.paintComponent(g);
            //g.setColor(Color.RED);
            for (Point fillCell : fillCells) {
                int cellX = 6 + (fillCell.x * 6);
                int cellY = 6 + (fillCell.y * 6);
                g.fillRect(cellX, cellY, 6, 6);
                g.setColor(Color.RED);
            }
            g.setColor(Color.BLACK);
            g.drawRect(6, 6, 654, 654);
            for (int i = 6; i <= 660; i += 6) {
                g.drawLine(i, 6, i, 660);
            }
            for (int i = 6; i <= 660; i += 6) {
                g.drawLine(6, i, 660, i);
            }
        }
        public synchronized void fillCell(int x, int y) {
            fillCells.add(new Point(x, y));
            repaint();
        }

        public synchronized void removeCell(int x, int y) {
            fillCells.remove(new Point(x, 108-y));
            repaint();
        }

        public synchronized void switchOnCell(int x, int y) {
            this.fillCell(x, 108-y);
            //repaint();
        }

        public synchronized void switchOffCell(int x, int y) {
            fillCells.remove(new Point(x, 108-y));
            repaint();
        }

        public synchronized void colorRoadMap(){

            JFrame window = new JFrame();
            window.setSize(720, 720);
            window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            window.add(this);
            window.setVisible(true);
            for (int i=0; i<50; i++) {
                for (int j=0; j<50; j++) {
                    this.fillCell(i, j);
                }
            }
            for (int i=53; i<56; i++) {
                for (int j=0; j<50; j++) {
                    this.fillCell(i, j);
                }
            }
            for (int i=59; i<109; i++) {
                for (int j=0; j<50; j++) {
                    this.fillCell(i, j);
                }
            }
            for (int i=0; i<50; i++) {
                for (int j=53; j<56; j++) {
                    this.fillCell(i, j);
                }
            }
            for (int i=59; i<109; i++) {
                for (int j=53; j<56; j++) {
                    this.fillCell(i, j);
                }
            }
            for (int i=0; i<50; i++) {
                for (int j=59; j<109; j++) {
                    this.fillCell(i, j);
                }
            }
            for (int i=53; i<56; i++) {
                for (int j=59; j<109; j++) {
                    this.fillCell(i, j);
                }
            }
            for (int i=59; i<109; i++) {
                for (int j=59; j<109; j++) {
                    this.fillCell(i, j);
                }
            }
        }
    }
}
