package averycowan.checkers;

/*
 * @author Avery Cowan
 */

import info.gridworld.grid.*;
public class Move{
  public final Location pLoc;
  public final Location mLoc;
  public final float value;
  
  public Move(Location p, Location m, float v){
    pLoc = Gridworld.cloneLoc(p);
    mLoc = Gridworld.cloneLoc(m);
    value = v;
  }
  public boolean equals(Move m){
    return pLoc.equals(m.pLoc) && mLoc.equals(m.mLoc) && value == m.value;
  }
  public boolean isLike(Move m){
    return pLoc.equals(m.pLoc) && mLoc.equals(m.mLoc);
  }
  public Location pLoc(){ return Gridworld.cloneLoc(pLoc);}
  public Location mLoc(){ return Gridworld.cloneLoc(mLoc);}
  public float value(){ return value;}
  public boolean isNull(){
    return equals(new Move(new Location(0, 0), new Location(0, 0), 0-Float.MAX_VALUE));
  }
  public String toString(){
    return pLoc+" --> "+mLoc+"/t"+value;
  }
}
  