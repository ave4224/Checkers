package averycowan.checkers;

/*
 * @author Avery Cowan
 */

import info.gridworld.actor.*;
import info.gridworld.grid.*;

import java.awt.*;
import java.util.*;

public class King extends Checker{//this means a king is a type of checker
  protected static final int VALUE = 19;
  /**
   * makes a new King
   */
  public King(boolean white){
    super(white);
  }
  /**
   * makes a new King
   */
  public King(boolean white, boolean real){
    super(white, real);
  }
  /**
    * calculates and return all directions it can jump or move
    * @return all directions it can jump or move
    */
  public ArrayList<ArrayList<Integer>> getPossibleMoves(){
    Grid<Actor> gr = getGrid();
    Location loc = getLocation();
    ArrayList<ArrayList<Integer>> toReturn = new ArrayList<ArrayList<Integer>>();
    ArrayList<Integer> moves = new ArrayList<Integer>();
    
    Location nwl = loc.getAdjacentLocation(Location.NORTHWEST);
    if(gr.isValid(nwl)){
      Checker nw = (Checker)(gr.get(nwl));
      if(nw == null)
        moves.add(Location.NORTHWEST);
    }
    
    Location nel = loc.getAdjacentLocation(Location.NORTHEAST);
    if(gr.isValid(nel)){
      Checker ne = (Checker)(gr.get(nel));
      if(ne == null)
        moves.add(Location.NORTHEAST);
    }
    Location swl = loc.getAdjacentLocation(Location.SOUTHWEST);
    if(gr.isValid(swl)){
      Checker sw = (Checker)(gr.get(swl));
      if(sw == null)
        moves.add(Location.SOUTHWEST);
    }
    
    Location sel = loc.getAdjacentLocation(Location.SOUTHEAST);
    if(gr.isValid(sel)){
      Checker se = (Checker)(gr.get(sel));
      if(se == null)
        moves.add(Location.SOUTHEAST);
    }
    getPossibleJumps(toReturn, loc, new ArrayList<Integer>(), new ArrayList<Actor>());
    if(!toReturn.isEmpty() && !toReturn.get(0).isEmpty()){
      toReturn.add(0, new ArrayList<Integer>());
    }
    else{
      for(Integer move : moves){
        ArrayList<Integer> toAdd = new ArrayList<Integer>();
        toAdd.add(move);
        toReturn.add(toAdd);
      }
    }
    return toReturn;
  }
  private void getPossibleJumps(ArrayList<ArrayList<Integer>> jumps, Location loc, ArrayList<Integer> strand, ArrayList<Actor> jumpedPieces){
    Grid<Actor> gr = getGrid();
    boolean foundJump = false;
    Location nwl = loc.getAdjacentLocation(Location.NORTHWEST);
    if(gr.isValid(nwl)){
      Location nwjl = nwl.getAdjacentLocation(Location.NORTHWEST);
      if(gr.isValid(nwjl)){
        Checker nw = (Checker)(gr.get(nwl));
        Checker nwj = (Checker)(gr.get(nwjl));
        if(nw != null && nw.color!=color && nwj == null && !jumpedPieces.contains(nw)){
            ArrayList<Integer> tempStrand = new ArrayList<Integer>();
            for(int dir : strand)
              tempStrand.add(dir);
            tempStrand.add(Location.NORTHWEST);
            ArrayList<Actor> tempJumpedPieces = new ArrayList<Actor>();
            for(Actor a : jumpedPieces)
              tempJumpedPieces.add(a);
            tempJumpedPieces.add(nw);
            getPossibleJumps(jumps, nwjl, tempStrand, tempJumpedPieces);
            foundJump = true;
          }
        }
      }
      Location nel = loc.getAdjacentLocation(Location.NORTHEAST);
      if(gr.isValid(nel)){
        Location nejl = nel.getAdjacentLocation(Location.NORTHEAST);
        if(gr.isValid(nejl)){
          Checker ne = (Checker)(gr.get(nel));
          Checker nej = (Checker)(gr.get(nejl));
          if(ne != null && ne.color!=color && nej == null && !jumpedPieces.contains(ne)){
            ArrayList<Integer> tempStrand = new ArrayList<Integer>();
            for(int dir : strand)
              tempStrand.add(dir);
            tempStrand.add(Location.NORTHEAST);
            ArrayList<Actor> tempJumpedPieces = new ArrayList<Actor>();
            for(Actor a : jumpedPieces)
              tempJumpedPieces.add(a);
            tempJumpedPieces.add(ne);
            getPossibleJumps(jumps, nejl, tempStrand, tempJumpedPieces);
            foundJump = true;
          }
        }
      }
      Location swl = loc.getAdjacentLocation(Location.SOUTHWEST);
      if(gr.isValid(swl)){
        Location swjl = swl.getAdjacentLocation(Location.SOUTHWEST);
        if(gr.isValid(swjl)){
          Checker sw = (Checker)(gr.get(swl));
          Checker swj = (Checker)(gr.get(swjl));
          if(sw != null && sw.color!=color && swj == null && !jumpedPieces.contains(sw)){
            ArrayList<Integer> tempStrand = new ArrayList<Integer>();
            for(int dir : strand)
              tempStrand.add(dir);
            tempStrand.add(Location.SOUTHWEST);
            ArrayList<Actor> tempJumpedPieces = new ArrayList<Actor>();
            for(Actor a : jumpedPieces)
              tempJumpedPieces.add(a);
            tempJumpedPieces.add(sw);
            getPossibleJumps(jumps, swjl, tempStrand, tempJumpedPieces);
            foundJump = true;
          }
        }
      }
      Location sel = loc.getAdjacentLocation(Location.SOUTHEAST);
      if(gr.isValid(sel)){
        Location sejl = sel.getAdjacentLocation(Location.SOUTHEAST);
        if(gr.isValid(sejl)){
          Checker se = (Checker)(gr.get(sel));
          Checker sej = (Checker)(gr.get(sejl));
          if(se != null && se.color!=color && sej == null && !jumpedPieces.contains(se)){
            ArrayList<Integer> tempStrand = new ArrayList<Integer>();
            for(int dir : strand)
              tempStrand.add(dir);
            tempStrand.add(Location.SOUTHEAST);
            ArrayList<Actor> tempJumpedPieces = new ArrayList<Actor>();
            for(Actor a : jumpedPieces)
              tempJumpedPieces.add(a);
            tempJumpedPieces.add(se);
            getPossibleJumps(jumps, sejl, tempStrand, tempJumpedPieces);
            foundJump = true;
          }
        }
      }
      if(!foundJump && !strand.isEmpty())
        jumps.add(strand);
  }
}