/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package averycowan.checkers;

import info.gridworld.actor.Actor;
import info.gridworld.grid.BoundedGrid;

/**
 *
 * @author avecowa
 */
public class CheckersGrid extends BoundedGrid<Actor> {
    private CheckersWorld world;
    public CheckersGrid(int rows, int cols, CheckersWorld cw) {
        super(rows, cols);
        world = cw;
    }
    public CheckersWorld getWorld() {
        return world;
    }
}
