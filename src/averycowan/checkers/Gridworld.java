package averycowan.checkers;

/*
 * @author Avery Cowan
 */
import info.gridworld.actor.*;
import info.gridworld.grid.*;
import java.util.*;

public abstract class Gridworld {
    /**
     * returns a clone of loc
     */
    public static Location cloneLoc(Location loc) {
        return new Location(loc.getRow(), loc.getCol());
    }
    /**
     * returns a clone of gr
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
