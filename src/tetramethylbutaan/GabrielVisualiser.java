package tetramethylbutaan;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import javax.swing.JFrame;
import javax.swing.JPanel;

/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author ramonjanssen
 */
public class GabrielVisualiser extends JFrame
{
    private static final int POINT_SIZE = 5;
    private static final int PANEL_WIDTH = 500;
    private static final int PANEL_HEIGHT = 500;

    JPanel panel = new GraphPanel();
    GabrielGraph graph;
    public GabrielVisualiser(GabrielGraph graph)
    {
        super();
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setSize(PANEL_WIDTH + 8, PANEL_HEIGHT + 35);
        this.graph = graph;
        setVisible(true);
        add(panel);
        this.repaint();
    }

    private class GraphPanel extends JPanel
    {
        public GraphPanel()
        {
            super();
            setSize(PANEL_WIDTH, PANEL_HEIGHT);
        }

        @Override
        protected void paintComponent(Graphics g)
        {
            final double HALF_PANEL_WIDTH_DOUBLE = PANEL_WIDTH/2;
            final double HALF_PANEL_HEIGHT_DOUBLE = PANEL_HEIGHT/2;

            Graphics2D g2d = (Graphics2D) g;
            g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

            drawGrid(g2d);

            g2d.setColor(Color.GRAY);
            for (GraphPoint point : graph.getPoints())
            {
                int x = (int)((point.features[0] + 1)* HALF_PANEL_WIDTH_DOUBLE);
                int y = (int)((point.features[1] + 1)* HALF_PANEL_HEIGHT_DOUBLE);
                
                for (GraphPoint secondPoint : point.getNeighbours())
                {
                    int x2 = (int)((secondPoint.features[0] + 1)* HALF_PANEL_WIDTH_DOUBLE);
                    int y2 = (int)((secondPoint.features[1] + 1)* HALF_PANEL_HEIGHT_DOUBLE);
                    g2d.drawLine(x, y, x2, y2);
                }
            }

            
            for (GraphPoint point : graph.getPoints())
            {
                g2d.setColor(point.classification < 0 ? Color.BLUE : Color.RED);

                int x = (int)((point.features[0] + 1) * HALF_PANEL_WIDTH_DOUBLE);
                int y = (int)((point.features[1] + 1) * HALF_PANEL_HEIGHT_DOUBLE);

                g2d.fillOval(x - POINT_SIZE/2, y - POINT_SIZE/2, POINT_SIZE, POINT_SIZE);
            }
        }

        private void drawGrid(Graphics2D g2d)
        {
            g2d.setColor(Color.LIGHT_GRAY);
            for (int x = 0; x < PANEL_WIDTH; x += PANEL_WIDTH/10)
            {
                g2d.drawLine(x, 0, x, PANEL_HEIGHT);
            }

            for (int y = 0; y < PANEL_HEIGHT; y += PANEL_HEIGHT/10)
            {
                g2d.drawLine(0, y, PANEL_WIDTH, y);
            }
        }
    }
}
