/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package averycowan.checkers;

import averycowan.checkers.CheckersWorld;
import info.gridworld.grid.Location;
import info.gridworld.gui.Display;
import info.gridworld.gui.DisplayMap;
import info.gridworld.gui.GridPanel;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.util.ArrayList;
import java.util.ResourceBundle;

/**
 * This class has the purpose of drawing the markers of previous moves.
 *
 * @author Avery Cowan
 */
public class CheckersGridPanel extends GridPanel {
    /**
     * This calls the super(map,res) constructor and saves the world.
     */
    public CheckersGridPanel(DisplayMap map, ResourceBundle res, CheckersWorld cw) {
        super(map, res);
        this.world = cw;
    }
    private CheckersWorld world;
    /**
     * Calls super.paintComponent then draws the last move trackers
     *
     */
    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2 = (Graphics2D) g;
        drawTrackers(g2);
    }
    private void drawTrackers(Graphics2D g2) {
        ArrayList<Tracker> trackers = world.getTrackers();
        for (int index = 0; index < trackers.size(); index++) {
            Location loc = (Location) trackers.get(index).loc;
            int xleft = colToXCoord(loc.getCol());
            int ytop = rowToYCoord(loc.getRow());
            drawTracker(g2, xleft, ytop, trackers.get(index));
        }
    }
    private void drawTracker(Graphics2D g2, int xleft, int ytop, Tracker obj) {
        Rectangle cellToDraw = new Rectangle(xleft, ytop, cellSize, cellSize);
        // Only draw if the object is visible within the current clipping
        // region.
        if (cellToDraw.intersects(g2.getClip().getBounds())) {
            Graphics2D g2copy = (Graphics2D) g2.create();
            g2copy.clip(cellToDraw);
            // Get the drawing object to display this occupant.
            Display displayObj = displayMap.findDisplayFor(obj.getClass());
            displayObj.draw(obj, this, g2copy, cellToDraw);
            g2copy.dispose();
        }
    }
}
