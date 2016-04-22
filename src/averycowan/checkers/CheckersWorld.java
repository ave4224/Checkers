package averycowan.checkers;

/*
 * @author Avery Cowan
 */
import java.util.*;
import info.gridworld.actor.*;
import info.gridworld.grid.*;

public class CheckersWorld extends ActorWorld {
    private Location lastClicked;
    private static final String MESSAGE = "Checkers";
    private ArrayList<Tracker> trackers;
    /**
     * Value of a Checker when rating
     */
    public static final float CHECKER_VALUE = (float) 1.0;
    /**
     * Value of a King when rating
     */
    public static final float KING_VALUE = (float) 1.9375;
    /**
     * Value of a row advancment when rating
     */
    public static final float ROW_VALUE = (float) 0.003125;
    /**
     * How many moves to look ahead
     */
    public static final int ITERS = 6;
    /**
     * How many moves to look ahead when pieces are low
     */
    public static final int ITER_SUM = ITERS + 7;
    private boolean gameOver;
    private boolean playerTurn;
    private byte reps;
    private ArrayList<Move> lastMove = new ArrayList<Move>();
    public CheckersWorld() {
        super();
        reset();
    }
    public void reset() {
        setGrid(new CheckersGrid(8, 8, this));
        for (int i = 0; i < 3; i++) {
            for (int j = (i + 1) % 2; j < 8; j += 2) {
                add(new Location(i, j), new Checker(false, true));
            }
        }
        for (int i = 5; i < 8; i++) {
            for (int j = (i + 1) % 2; j < 8; j += 2) {
                add(new Location(i, j), new Checker(true, true));
            }
        }
        reps = 0;
        lastClicked = null;
        clearTrackers();
        gameOver = false;
        playerTurn = true;
        setMessage(MESSAGE);
        getGridClasses().clear();//this tricks the World into not letting the user unbound the grid.
        show();
    }
    /**
     * Called by the GUI when a location is clicked
     */
    public boolean locationClicked(Location l) {//I had to modify the base User Interface to make it so users can't overide my program and access the World.
        System.out.println(playerTurn);
        if (gameOver) {
            return true;
        }
        if (!playerTurn) {
            setMessage("It isn't your turn. The computer is thinking.");
            return true;
        }
        playerTurn = false;
        if ((l.getRow() + l.getCol()) % 2 != 1) {
            setMessage("Select a valid space.");
            playerTurn = true;
            return true;
        }
        if (lastClicked == null) {
            Checker checker = (Checker) getGrid().get(l);
            if (checker == null) {
                setMessage("That location is empty. Select a new piece to move.");
                playerTurn = true;
                return true;
            }
            if (!(checker.color)) {
                setMessage("That piece is the wrong color. Select a new piece to move.");
                playerTurn = true;
                return true;
            }
            lastClicked = l;
            setMessage("Piece selected. Select a location to move it.");
            playerTurn = true;
            return true;
        }
        if (lastClicked.equals(l)) {
            lastClicked = null;
            setMessage("Piece selection canceled. Select a new piece to move.");
            playerTurn = true;
            return true;
        }
        show();
        //((Checker)(getGrid().get(l))).blink();
        System.out.println(getGrid());
        if (realMove(l)) {
            playerTurn = false;
            lastMove.add(new Move(l, lastClicked, 1));
            if (lastMove.size() > 6) {
                lastMove.remove(0);
            }
            setMessage("Move successful. Calculating responce.");
            checkGrid();
            draw();
            CheckersWorld t = this;
            new Thread() {
                public void run() {
                    t.getFrame().setRunMenuItemsEnabled(false);
                    show();
                    Move move = getBestComputerMove(getGrid(), Math.max(ITERS, ITER_SUM - getGrid().getOccupiedLocations().size()));
                    if (move.isNull()) {
                        gameOver(Checker.PLAYER_COLOR_VALUE);
                        return;
                    }
                    lastMove.add(move);
                    if (lastMove.size() > 6) {
                        lastMove.remove(0);
                    }
                    lastClicked = move.pLoc;
                    realMove(move.mLoc);
                    checkGrid();
                    draw();
                    playerTurn = true;
                    System.out.println("Thread Over");
                    lastClicked = null;
                    t.getFrame().setRunMenuItemsEnabled(true);
                    show();
                }
            }.start();
        } else//{
        {
            setMessage("Invalid move");
            playerTurn = true;
        }
        //((Checker)(getGrid().get(l))).moveTo(l);
        //}
        lastClicked = null;
        return true;
    }
    /**
     * Called by locationClicked to make a move
     *
     * @return if the move was possible
     */
    private boolean doMove(Location loc) {
        return doMove(loc, lastClicked, getGrid(), false);
    }
    private boolean realMove(Location loc) {
        return doMove(loc, lastClicked, getGrid(), true);
    }
    private static boolean doMove(Location loc, Location piece, Grid<Actor> gr, boolean real) {
        //Actor a = gr.get(piece);
        //if(!(a instanceof AbstractCommandChecker)){
        //  Checker c = (Checker)a;
        Checker c = (Checker) gr.get(piece);
        return c.doMove(loc, checkForJumps(c.color, gr), real);
        //}
        //return false;
    }
    /**
     * rates the board
     *
     * @param gr the grid to be rated
     */
    public static float rateBoard(Grid<Actor> gr) {
        float good = 0;
        float bad = 0;
        ArrayList<Location> locList = gr.getOccupiedLocations();
        for (Location l : locList) {
            Checker c = (Checker) gr.get(l);
            if (c.color == Checker.PLAYER_COLOR_VALUE) {
                if (c instanceof King) {
                    good += KING_VALUE;
                } else {
                    good += CHECKER_VALUE;
                    good += (Math.max(0, 4 - c.getLocation().getRow()) * ROW_VALUE);
                }
            } else if (c instanceof King) {
                bad += KING_VALUE;
            } else {
                bad += CHECKER_VALUE;
                bad += (Math.max(0, c.getLocation().getRow()) - 4 * ROW_VALUE);
            }
        }
        if (good == 0) {
            return 0 - Float.MAX_VALUE;
        }
        if (bad == 0) {
            return Float.MAX_VALUE;
        }
        return good / bad;
    }
    public boolean checkForJumps(boolean color) {
        return checkForJumps(color, getGrid());
    }
    /**
     * checks if any pieces can jump
     */
    public static boolean checkForJumps(boolean color, Grid<Actor> gr) {
        ArrayList<Location> occLocs = gr.getOccupiedLocations();
        for (Location loc : occLocs) {
            //Actor a = gr.get(loc);
            //if(!(a instanceof AbstractCommandChecker)){
            //  Checker c = (Checker)a;
            Checker c = (Checker) gr.get(loc);
            if (c.color == color) {
                ArrayList<ArrayList<Integer>> dirList = ((Checker) gr.get(loc)).getPossibleMoves();
                if (!dirList.isEmpty() && dirList.get(0).isEmpty()) {
                    return true;
                }
            }
            //}
        }
        return false;
    }
    /**
     * finds the best move for the computer to make
     *
     * @param iter the ammount of turns to look ahead
     */
    public Move getBestComputerMove(Grid<Actor> gr, int iter) {
        ArrayList<Location> locList = gr.getOccupiedLocations();
        ArrayList<Move> moves = new ArrayList<Move>();
        boolean jumpPossible = checkForJumps(!Checker.PLAYER_COLOR_VALUE, gr);
        for (Location loc : locList) {//goes through every checker
            Checker checker = (Checker) gr.get(loc);
            if (!checker.color) {
                Grid<Actor> clone = Gridworld.cloneGrid(gr, locList);//makes a copy of the grid
                Location origin = checker.getLocation();//remember where the checker starts
                Checker c = (Checker) clone.get(origin);//gets the counterpart of the checker from the copy grid
                ArrayList<ArrayList<Integer>> dirList = c.getPossibleMoves();
                if (jumpPossible) {//any piece can jump
                    if (!dirList.isEmpty() && dirList.get(0).isEmpty()) {//this piece can jump
                        for (int i = 1; i < dirList.size(); i++) {//goes through the jump sequences
                            ArrayList<Integer> dirs = dirList.get(i);//gets the jump sequence
                            Location current = c.getLocation();//gets the current location
                            for (Integer dir : dirs) {//goes through the jumps
                                current = current.getAdjacentLocation(dir).getAdjacentLocation(dir);//moves the location two blocks in the direction of the jump
                            }
                            c.move(true, dirs, current, false);
                            if (iter < 1) {
                                moves.add(new Move(origin, current, 1 / rateBoard(clone)));
                            } else {
                                moves.add(new Move(origin, current, 1 / getBestPlayerMove(clone, iter - 1).value()));
                            }
                            clone = Gridworld.cloneGrid(gr, locList);
                            c = (Checker) clone.get(origin);
                        }
                    }
                } else {
                    for (int i = 0; i < dirList.size(); i++) {
                        Location l = c.getLocation().getAdjacentLocation(dirList.get(i).get(0));
                        c.move(false, dirList.get(i), l, false);
                        if (iter < 1) {
                            moves.add(new Move(origin, l, 1 / rateBoard(clone)));
                        } else {
                            moves.add(new Move(origin, l, 1 / getBestPlayerMove(clone, iter - 1).value()));
                        }
                        clone = Gridworld.cloneGrid(gr, locList);
                        c = (Checker) clone.get(origin);
                    }
                }
            }
        }
        if (moves.isEmpty()) {
            return new Move(new Location(0, 0), new Location(0, 0), 0 - Float.MAX_VALUE);
        }
        Move highest = moves.get(0);
        for (Move move : moves) {
            if (move.value() > highest.value()) {
                highest = move;
            }
        }
        return highest;
    }
    /**
     * finds the best move for the player to make
     *
     * @param iter the ammount of turns to look ahead
     */
    public Move getBestPlayerMove(Grid<Actor> gr, int iter) {
        ArrayList<Location> locList = gr.getOccupiedLocations();
        ArrayList<Move> moves = new ArrayList<Move>();
        boolean jumpPossible = checkForJumps(Checker.PLAYER_COLOR_VALUE, gr);
        for (Location loc : locList) {//goes through every checker
            Checker checker = (Checker) gr.get(loc);
            if (checker.color) {
                Grid<Actor> clone = Gridworld.cloneGrid(gr, locList);//makes a copy of the grid
                Location origin = checker.getLocation();//remember where the checker starts
                Checker c = (Checker) clone.get(origin);//gets the counterpart of the checker from the copy grid
                ArrayList<ArrayList<Integer>> dirList = c.getPossibleMoves();
                if (jumpPossible) {//any piece can jump
                    if (!dirList.isEmpty() && dirList.get(0).isEmpty()) {//this piece can jump
                        for (int i = 1; i < dirList.size(); i++) {//goes through the jump sequences
                            ArrayList<Integer> dirs = dirList.get(i);//gets the jump sequence
                            Location current = c.getLocation();//gets the current location
                            for (Integer dir : dirs) {//goes through the jumps
                                current = current.getAdjacentLocation(dir).getAdjacentLocation(dir);//moves the location two blocks in the direction of the jump
                            }
                            c.move(true, dirs, current, false);
                            if (iter < 1) {
                                moves.add(new Move(origin, current, rateBoard(clone)));
                            } else {
                                moves.add(new Move(origin, current, 0 - getBestComputerMove(clone, iter - 1).value()));
                            }
                            clone = Gridworld.cloneGrid(gr, locList);
                            c = (Checker) clone.get(origin);
                        }
                    }
                } else {
                    for (int i = 0; i < dirList.size(); i++) {
                        Location l = c.getLocation().getAdjacentLocation(dirList.get(i).get(0));
                        c.move(false, dirList.get(i), l, false);
                        if (iter < 1) {
                            moves.add(new Move(origin, l, rateBoard(clone)));
                        } else {
                            moves.add(new Move(origin, l, 0 - getBestComputerMove(clone, iter - 1).value()));
                        }
                        clone = Gridworld.cloneGrid(gr, locList);
                        c = (Checker) clone.get(origin);
                    }
                }
            }
        }
        if (moves.isEmpty()) {
            return new Move(new Location(0, 0), new Location(0, 0), 0 - Float.MAX_VALUE);
        }
        Move highest = moves.get(0);
        for (Move move : moves) {
            if (move.value() > highest.value()) {
                highest = move;
            }
        }
        return highest;
    }
//  public Move getBestMove(Grid<Actor> gr, boolean color, int iter){
//    ArrayList<Location> locList = gr.getOccupiedLocations();
//    ArrayList<Move> moves = new ArrayList<Move>();
//    boolean jumpPossible = checkForJumps(color, gr);
//    for(Location loc: locList){//goes through every checker
//      Checker checker = (Checker)gr.get(loc);
//      if(checker.color == color){
//        Grid<Actor> clone = Gridworld.cloneGrid(gr, locList);//makes a copy of the grid
//        Location origin = checker.getLocation();//remember where the checker starts
//        Checker c = (Checker)clone.get(origin);//gets the counterpart of the checker from the copy grid
//        ArrayList<ArrayList<Integer>> dirList = c.getPossibleMoves();
//        if(jumpPossible){//any piece can jump
//          if(!dirList.isEmpty() && dirList.get(0).isEmpty()){//this piece can jump
//            for(int i = 1; i < dirList.size(); i++){//goes through the jump sequences
//              ArrayList<Integer> dirs = dirList.get(i);//gets the jump sequence
//              Location current = c.getLocation();//gets the current location
//              for(Integer dir : dirs){//goes through the jumps
//                current = current.getAdjacentLocation(dir).getAdjacentLocation(dir);//moves the location two blocks in the direction of the jump
//              }
//              c.move(true, dirs, current);
//              if(iter<1&&color){
//                moves.add(new Move(origin, current, rateBoard(clone)));
//              }
//              else
//                moves.add(new Move(origin, current, getBestMove(clone, !color, iter-1).value()));
//              clone = Gridworld.cloneGrid(gr, locList);
//              c = (Checker)clone.get(origin);
//            }
//          }
//        }
//        else{
//          for(int i = 0; i < dirList.size(); i++){
//            Location l = c.getLocation().getAdjacentLocation(dirList.get(i).get(0));
//            c.move(false, dirList.get(i), l);
//            if(iter<1&&color){
//              moves.add(new Move(origin, l, rateBoard(clone)));
//            }
//            else
//              moves.add(new Move(origin, l, getBestMove(clone, !color, iter-1).value()));
//            clone = Gridworld.cloneGrid(gr, locList);
//            c = (Checker)clone.get(origin);
//          }
//        }
//      }
//    }
//    if(moves.isEmpty()){
//      return new Move(new Location(0, 0), new Location(0, 0), 0-Float.MAX_VALUE);
//    }
//    Move highest = moves.get(0);
//    for(Move move:moves){
//      if(move.value() > highest.value())
//        highest = move;
//    }
//    return highest;
//  }
    /**
     * checks if any player has won
     */
    public void checkGrid() {
        boolean player = false;
        boolean computer = false;
        Grid<Actor> gr = getGrid();
        ArrayList<Location> occLocs = gr.getOccupiedLocations();
        for (Location l : occLocs) {
            //Actor a = gr.get(l);
            //if(!(a instanceof AbstractCommandChecker)){
            //  Checker c = (Checker)a;
            Checker c = (Checker) gr.get(l);
            boolean color = (((Checker) gr.get(l)).color) == Checker.PLAYER_COLOR_VALUE;
            if (!c.getPossibleMoves().isEmpty()) {
                player = player || color;
                computer = computer || !color;
            }
            //}
        }
        if (!player) {
            gameOver(!Checker.PLAYER_COLOR_VALUE);
        }
        if (!computer) {
            gameOver(Checker.PLAYER_COLOR_VALUE);
        }
    }
    public void step() {
        if (gameOver) {
            return;
        }
        playerTurn = false;
        checkGrid();
        Move move = getBestPlayerMove(getGrid(), ITERS - 1);
        if (move.isNull()) {
            gameOver(!Checker.PLAYER_COLOR_VALUE);
            return;
        }
        lastClicked = move.pLoc();
        doMove(move.mLoc);
        checkGrid();
        move = getBestComputerMove(getGrid(), ITERS);
        if (move.isNull()) {
            gameOver(Checker.PLAYER_COLOR_VALUE);
            return;
        }
        lastClicked = move.pLoc();
        doMove(move.mLoc);
        lastClicked = null;
        playerTurn = true;
    }
    /**
     * ends the game
     *
     * @param color the winning color
     */
    public void gameOver(boolean color) {
        if (color == Checker.PLAYER_COLOR_VALUE) {
            setMessage("You beat the computer.");
        } else {
            setMessage("The computer beat you");
        }
        gameOver = true;
    }
    /**
     * makes a draw
     */
    public void draw() {
        if (lastMove.size() == 6 && lastMove.get(0).isLike(lastMove.get(2)) && lastMove.get(0).isLike(lastMove.get(4)) && lastMove.get(1).isLike(lastMove.get(3)) && lastMove.get(1).isLike(lastMove.get(5))) {
            setMessage("It's a draw.");
            gameOver = true;
        }
    }
    public void addTracker(Tracker loc) {
        System.out.println("Tracking " + loc.loc);
        trackers.add(loc);
    }
//  public void step(){
//    if(REPLAY_MODE){
//      if(gridNum < grids.size()){
//        setGrid(grids.get(gridNum));
//        show();
//        setMessage(""+scores.get(gridNum-1));
//        gridNum++;
//      }
//      else{
//        setGrid(grids.get(0));
//        show();
//        setMessage(MESSAGE);
//        gridNum = 1;
//      }
//    }
//  }
    void clearTrackers() {
        trackers.clear();
    }
    public ArrayList<Tracker> getTrackers() {
        return trackers;
    }
    @Override
    public Set<String> getGridClasses() {
        return new TreeSet<String>();
    }
}
