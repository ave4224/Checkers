package averycowan.checkers;

import info.gridworld.grid.Location;

/**
 * This class is used to represent a potential move.
 *
 * Treat this like an arrow from one location to another. Additionally, is is
 * given a value used by the AI to evaluate if the move is good.
 *
 * @author Avery Cowan
 */
public class Move {
    /**
     * The start of the arrow
     */
    public final Location pLoc;
    /**
     * The end of the arrow
     */
    public final Location mLoc;
    /**
     * AI assigned value used to compare move options
     */
    public final float value;
    /**
     * Constructs a Move object
     *
     * @param p Starting location
     * @param m Ending location
     * @param v AI assigned value
     */
    public Move(Location p, Location m, float v) {
        pLoc = Gridworld.cloneLoc(p);
        mLoc = Gridworld.cloneLoc(m);
        value = v;
    }
    /**
     * Standard .equals
     *
     * @return if all fields are equal
     */
    public boolean equals(Move m) {
        return pLoc.equals(m.pLoc) && mLoc.equals(m.mLoc) && value == m.value;
    }
    /**
     * Like .equals but value is ignored
     *
     * @param m
     * @return if pLoc and mLoc are equal
     */
    public boolean isLike(Move m) {
        return pLoc.equals(m.pLoc) && mLoc.equals(m.mLoc);
    }
    /**
     * This returns a clone of pLoc.
     */
    public Location pLoc() {
        return Gridworld.cloneLoc(pLoc);
    }
    /**
     * This returns a clone of pLoc.
     */
    public Location mLoc() {
        return Gridworld.cloneLoc(mLoc);
    }
    /**
     * Accessor for the value of the move.
     */
    public float value() {
        return value;
    }
    /**
     * Instead of using null to denote a lack of move, a Move returning true for
     * isNull() is used.
     */
    public boolean isNull() {
        return equals(new Move(new Location(0, 0), new Location(0, 0), 0 - Float.MAX_VALUE));
    }
    /**
     * You know what this does
     *
     * @return pLoc + " --> " + mLoc + "/t" + value;
     */
    public String toString() {
        return pLoc + " --> " + mLoc + "/t" + value;
    }
}
