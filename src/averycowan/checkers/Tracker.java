/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package averycowan.checkers;

import info.gridworld.grid.Location;

/**
 * This class represents a location to be shaded as part of the last move path.
 *
 * @author Avery Cowan
 */
public class Tracker {
    public Location loc;
    public Tracker(Location loc) {
        this.loc = loc;
    }
}
