package averycowan.checkers;

import info.gridworld.actor.Actor;
import info.gridworld.grid.Grid;
import info.gridworld.grid.Location;
import java.util.ArrayList;

/**
 * This is a utility class used for cloning Gridworld objects
 *
 * @author Avery Cowan
 */
public abstract class Gridworld {
    /**
     * Clones a Location
     */
    public static Location cloneLoc(Location loc) {
        return new Location(loc.getRow(), loc.getCol());
    }
    /**
     * clones a Grid
     *
     * @param locList a list of occupants
     */
    public static Grid<Actor> cloneGrid(Grid<Actor> gr, ArrayList<Location> locList) {
        CheckersGrid clone = new CheckersGrid(8, 8, ((CheckersGrid) gr).getWorld());
        for (Location loc : locList) {
            Object o = gr.get(loc);
            Checker c = (Checker) gr.get(loc);
            if (o instanceof King) {
                new King(c.color).putSelfInGrid(clone, loc);
            } else {
                new Checker(c.color).putSelfInGrid(clone, loc);
            }
        }
        return clone;
    }
}
