/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package averycowan.checkers;

import info.gridworld.actor.Actor;
import info.gridworld.grid.BoundedGrid;

/**
 * This is a custom implementation that remembers the world its it.
 *
 * @author Avery Cowan
 */
public class CheckersGrid extends BoundedGrid<Actor> {
    private CheckersWorld world;
    /**
     * This calls super(rows, cols) and saves the world
     */
    public CheckersGrid(int rows, int cols, CheckersWorld cw) {
        super(rows, cols);
        world = cw;
    }
    /**
     * Gets the world this grid lives in
     */
    public CheckersWorld getWorld() {
        return world;
    }
}
