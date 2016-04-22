package averycowan.checkers;

/*
 * @author Avery Cowan
 *
 * This class defines the properties of each checker piece.
 *
 */
import info.gridworld.grid.*;
import info.gridworld.actor.*;
import java.awt.*;
import java.util.*;

public class Checker extends Actor {//this means checker is a type of actor
    protected static final int VALUE = 10;
    public static final Color WHITE = Color.RED;
    public static final Color BLACK = Color.BLACK;
    public static final Color PLAYER_COLOR = BLACK;
    public static final Color COMPUTER_COLOR = WHITE;
    public static final boolean PLAYER_COLOR_VALUE = true;
    public static final int COLOR_VARIETY = 60;
    protected boolean isReal;
    public final boolean color;
    public Checker() {
        this(PLAYER_COLOR_VALUE);
        isReal = false;
    }
    public Checker(boolean white, boolean real) {
        this(white);
        if (real) {
            super.setColor(newColor(white));
        }
        isReal = real;
    }
    /**
     * makes a new checker
     */
    public Checker(boolean white) {
        super();
        color = white;
    }
    protected Color newColor(boolean b) {
        Color c = null;
        if (b == PLAYER_COLOR_VALUE) {
            c = PLAYER_COLOR;
        } else {
            c = COMPUTER_COLOR;
        }
        int red = c.getRed();
        int green = c.getGreen();
        int blue = c.getBlue();
        if (b == PLAYER_COLOR_VALUE) {
            red += (int) (Math.random() * COLOR_VARIETY);
            green += (int) (Math.random() * COLOR_VARIETY);
            blue += (int) (Math.random() * COLOR_VARIETY);
        } else {
            red -= (int) (Math.random() * COLOR_VARIETY);
            green += (int) (Math.random() * COLOR_VARIETY);
            blue += (int) (Math.random() * COLOR_VARIETY);
        }
        return new Color(red, green, blue);
    }
    protected void protectedSetColor(Color c) {
        super.setColor(c);
    }
    /**
     * calculates and return all directions it can jump or move
     *
     * @return all directions it can jump or move
     */
    public ArrayList<ArrayList<Integer>> getPossibleMoves() {
        Grid<Actor> gr = getGrid();
        Location loc = getLocation();
        ArrayList<ArrayList<Integer>> toReturn = new ArrayList<ArrayList<Integer>>();
        ArrayList<Integer> moves = new ArrayList<Integer>();
        if (color) {//if the piece is red
            Location nwl = loc.getAdjacentLocation(Location.NORTHWEST);//make the location called nwl the location northwest of me
            if (gr.isValid(nwl)) {//if nwl is on the grid
                Checker nw = (Checker) (gr.get(nwl));//make the checker called nw the piece at nwl
                if (nw == null)//if nwl is empty
                {
                    moves.add(Location.NORTHWEST);//add the direction northwest to the list of moves
                }
            }
            Location nel = loc.getAdjacentLocation(Location.NORTHEAST);//same but for northeast
            if (gr.isValid(nel)) {
                Checker ne = (Checker) (gr.get(nel));
                if (ne == null) {
                    moves.add(Location.NORTHEAST);
                }
            }
        } else {//if the piece is black
            Location swl = loc.getAdjacentLocation(Location.SOUTHWEST);//same but for southwast
            if (gr.isValid(swl)) {
                Checker sw = (Checker) (gr.get(swl));
                if (sw == null) {
                    moves.add(Location.SOUTHWEST);
                }
            }
            Location sel = loc.getAdjacentLocation(Location.SOUTHEAST);//same but for southeast
            if (gr.isValid(sel)) {
                Checker se = (Checker) (gr.get(sel));
                if (se == null) {
                    moves.add(Location.SOUTHEAST);
                }
            }
        }
        getPossibleJumps(toReturn, loc, new ArrayList<Integer>());//find what jumps I can make
        if (!toReturn.isEmpty() && !toReturn.get(0).isEmpty()) {
            toReturn.add(0, new ArrayList<Integer>());
        } else {
            for (Integer move : moves) {
                ArrayList<Integer> toAdd = new ArrayList<Integer>();
                toAdd.add(move);
                toReturn.add(toAdd);
            }
        }
        return toReturn;
    }
    /**
     * Changes jumps to include all possibe jumps
     *
     * @param loc the Location to check for jumps from
     * @param strand the Directions already jumped so that the piece soesn't
     * jump any piece more that once
     */
    private void getPossibleJumps(ArrayList<ArrayList<Integer>> jumps, Location loc, ArrayList<Integer> strand) {
        Grid<Actor> gr = getGrid();
        boolean foundJump = false;
        if (color) {
            Location nwl = loc.getAdjacentLocation(Location.NORTHWEST);
            if (gr.isValid(nwl)) {
                Location nwjl = nwl.getAdjacentLocation(Location.NORTHWEST);
                if (gr.isValid(nwjl)) {
                    Checker nw = (Checker) (gr.get(nwl));
                    Checker nwj = (Checker) (gr.get(nwjl));
                    if (nw != null && !nw.color && nwj == null) {
                        ArrayList<Integer> tempStrand = new ArrayList<Integer>();
                        for (int dir : strand) {
                            tempStrand.add(dir);
                        }
                        tempStrand.add(Location.NORTHWEST);
                        getPossibleJumps(jumps, nwjl, tempStrand);
                        foundJump = true;
                    }
                }
            }
            Location nel = loc.getAdjacentLocation(Location.NORTHEAST);
            if (gr.isValid(nel)) {
                Location nejl = nel.getAdjacentLocation(Location.NORTHEAST);
                if (gr.isValid(nejl)) {
                    Checker ne = (Checker) (gr.get(nel));
                    Checker nej = (Checker) (gr.get(nejl));
                    if (ne != null && !ne.color && nej == null) {
                        ArrayList<Integer> tempStrand = new ArrayList<Integer>();
                        for (int dir : strand) {
                            tempStrand.add(dir);
                        }
                        tempStrand.add(Location.NORTHEAST);
                        getPossibleJumps(jumps, nejl, tempStrand);
                        foundJump = true;
                    }
                }
            }
        } else {
            Location swl = loc.getAdjacentLocation(Location.SOUTHWEST);
            if (gr.isValid(swl)) {
                Location swjl = swl.getAdjacentLocation(Location.SOUTHWEST);
                if (gr.isValid(swjl)) {
                    Checker sw = (Checker) (gr.get(swl));
                    Checker swj = (Checker) (gr.get(swjl));
                    if (sw != null && sw.color && swj == null) {
                        ArrayList<Integer> tempStrand = new ArrayList<Integer>();
                        for (int dir : strand) {
                            tempStrand.add(dir);
                        }
                        tempStrand.add(Location.SOUTHWEST);
                        getPossibleJumps(jumps, swjl, tempStrand);
                        foundJump = true;
                    }
                }
            }
            Location sel = loc.getAdjacentLocation(Location.SOUTHEAST);
            if (gr.isValid(sel)) {
                Location sejl = sel.getAdjacentLocation(Location.SOUTHEAST);
                if (gr.isValid(sejl)) {
                    Checker se = (Checker) (gr.get(sel));
                    Checker sej = (Checker) (gr.get(sejl));
                    if (se != null && se.color && sej == null) {
                        ArrayList<Integer> tempStrand = new ArrayList<Integer>();
                        for (int dir : strand) {
                            tempStrand.add(dir);
                        }
                        tempStrand.add(Location.SOUTHEAST);
                        getPossibleJumps(jumps, sejl, tempStrand);
                        foundJump = true;
                    }
                }
            }
        }
        if (!foundJump && !strand.isEmpty()) {
            jumps.add(strand);
        }
    }
    /**
     * does nothing
     */
    public void act() {
    }
    /**
     * does nothing
     */
    public void setColor() {
    }//makes it so the color can't be chenged
    /**
     * changes itself into a king if it is on a end row
     */
    public void check() {
        Grid<Actor> gr = getGrid();
        Location loc = new Location(getLocation().getRow(), getLocation().getCol());
        if ((color && loc.getRow() == 0) || (!color && loc.getRow() == 7)) {
            new King(color, isReal).putSelfInGrid(getGrid(), loc);
        }
    }
    /**
     * checks if a move is possible and if so does it
     *
     * @param l the designated location to move to
     * @param jumpPossible if a jump is possible on the board by any piece of
     * the same color
     * @return if the piece can move the specified locaation
     */
    public boolean doMove(Location l, boolean jumpPossible, boolean real) {
        Grid<Actor> gr = getGrid();
        ArrayList<ArrayList<Integer>> dirList = getPossibleMoves();
        if (jumpPossible) {//any piece can jump
            if (!dirList.isEmpty() && dirList.get(0).isEmpty()) {//this piece can jump
                for (int i = 1; i < dirList.size(); i++) {
                    ArrayList<Integer> dirs = dirList.get(i);
                    Location current = getLocation();
                    for (Integer dir : dirs) {
                        current = current.getAdjacentLocation(dir).getAdjacentLocation(dir);
                    }
                    if (current.equals(l)) {
                        move(true, dirs, l, real);
                        return true;
                    }
                }
            }
        } else {
            for (int i = 0; i < dirList.size(); i++) {
                if (getLocation().getAdjacentLocation(dirList.get(i).get(0)).equals(l)) {
                    move(false, dirList.get(i), l, real);
                    return true;
                }
            }
        }
        return false;
    }
    /**
     * Moves itself
     *
     * @param jump if the desired move is a jump
     * @param dirs the list of directions indicating a move of jump sequence
     * leading to the destination
     * @param l the destination
     */
    public void move(boolean jump, ArrayList<Integer> dirs, Location l, boolean real) {
        CheckersWorld world = ((CheckersGrid) getGrid()).getWorld();
        if (real) {
            world.clearTrackers();
        }
        if (jump) {
            Location current = getLocation();
            for (Integer dir : dirs) {
                if (real) {
                    world.addTracker(new Tracker(current));
                }
                current = current.getAdjacentLocation(dir);
                if (real) {
                    world.addTracker(new Tracker(current));
                }
                getGrid().get(current).removeSelfFromGrid();
                current = current.getAdjacentLocation(dir);
            }
        } else if (real) {
            world.addTracker(new Tracker(getLocation()));
        }
        moveTo(l);
        check();
    }
//  public void blink(){
//    Color c = super.getColor();
//    if(color)
//      setColor(new Color(c.getRed()-60, c.getBlue()+60, c.getGreen()+60));
//    else
//      setColor(new Color(c.getRed()+60, c.getBlue()+60, c.getGreen()+60));
//  }
//  public void moveTo(Location l){
//    blink();
//    try{
//      wait(300);
//    }
//    catch(Exception e){};
//    super.moveTo(l);
//    Color c = super.getColor();
//    if(color)
//      setColor(new Color(c.getRed()+100, c.getBlue()-100, c.getGreen()-100));
//    else
//      setColor(new Color(c.getRed()-100, c.getBlue()-100, c.getGreen()-100));
//  }
    /**
     * This class's toString
     *
     * @return the name of the class, the color value and the location
     */
    public String toString() {
        return getClass().getName() + "[" + (color == PLAYER_COLOR_VALUE) + ", " + getLocation() + "]";
    }
}
