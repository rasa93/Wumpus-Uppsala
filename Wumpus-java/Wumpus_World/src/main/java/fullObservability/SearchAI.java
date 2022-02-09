package fullObservability;

import wumpus.Agent;
import wumpus.World;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.ListIterator;


public class SearchAI extends Agent {

    private class Node{
        State state;
        Node parent;
        int cost;
        public Node(State s, Node parent, int cost){
            this.state=s;
            this.parent=parent;
            this.cost=cost;
        }
    }

    private class State{
        int x;
        int y;
        int dir;
        boolean hasGold;
        boolean hasArrow;
        int cost;
        boolean hasWumpus;
        boolean goldLooted=false;
        public State(int column, int row, int dir, boolean hasGold, boolean hasArrow){
            this.x=column;
            this.y=row;
            this.dir=dir;
            this.hasGold=hasGold;
            this.hasArrow=hasArrow;
        }
        public int getCost(){
            return this.cost;
        }

        public void setCost(int cost){
            this.cost=cost;
        }
        public int getX(){
            return this.x;
        }
        public int getY(){
            return this.y;
        }
        public void setX(int x){
            this.x=x;
        }
        public void setY(int y){
            this.y=y;
        }
        public void setDir(int dir){
            this.dir=dir;
        }
        public int getDir(){
            return this.dir;
        }
        public boolean getHasGold(){
            return this.hasGold;
        }
        public void setHasGold(boolean hasGold){
            this.hasGold=hasGold;
        }
        public boolean getHasArrow(){
            return this.hasArrow;
        }

        public void setArrow(boolean hasArrow){
            this.hasArrow=hasArrow;
        }

        public boolean getHasWumpus(){
            return this.hasWumpus;
        }

        public void setHasWumpus(boolean hasWumpus){
            this.hasWumpus=hasWumpus;
        }
        public boolean getGoldLooted(){
            return goldLooted;
        }
        public void setGoldLooted(boolean goldLooted){
            this.goldLooted=goldLooted;
        }
    }

    private State result(State s, Action a, int column, int row, World.Tile[][] board){
        int agentX=s.getX();
        int agentY= s.getY();
        int dir= s.getDir();
        if(a==Action.TURN_LEFT) {
            s.setCost(s.getCost()-1);


        }
        if(a==Action.TURN_RIGHT) {
            s.setCost(s.getCost()-1);

        }
        if(a==Action.FORWARD) {
            if ( dir == 0 && agentX+1 < column )
                ++agentX;
            else if (dir == 1 && agentY-1 >= 0 )
                --agentY;
            else if ( dir == 2 && agentX-1 >= 0 )
                --agentX;
            else if ( dir == 3 && agentY+1 < row )
                ++agentY;
            return new State(agentX,agentY,dir,s.getHasGold(),s.getHasArrow());
        }
        if(a==Action.SHOOT){
            if (s.hasArrow )
            {
                s.setArrow(false);
                s.setCost(s.getCost()-10);
                if ( dir == 0 )
                {
                    for ( int x = agentX; x < column; ++x )
                        if ( board[x][agentY].getWumpus())
                        {
                            s.setHasWumpus(false);
                        }
                }
                else if ( dir == 1 )
                {
                    for ( int y = agentY; y >= 0; --y )
                        if ( board[agentX][y].getWumpus() )
                        {
                            s.setHasWumpus(false);
                        }
                }
                else if ( dir == 2 )
                {
                    for ( int x = agentX; x >= 0; --x )
                        if ( board[x][agentY].getWumpus() )
                        {
                            s.setHasWumpus(false);
                        }
                }
                else if ( dir == 3 )
                {
                    for ( int y = agentY; y < row; ++y )
                        if ( board[agentX][y].getWumpus() )
                        {
                            s.setHasWumpus(false);
                        }
                }
            }
            return new State(agentX,agentY,dir,s.getHasGold(),s.getHasArrow());
        }
        if(a==Action.GRAB){
            if ( board[agentX][agentY].getGold() )
            {
                s.setHasGold(false);
                s.setGoldLooted(true);

            }

        }
        if(a==Action.CLIMB){
            if ( agentX == 0 && agentY == 0 )
            {
                if ( s.goldLooted)
                    s.setCost(s.getCost()+1000);
            }

        }
        return new State(agentX,agentY,dir,s.getHasGold(),s.getHasArrow());
    }


    private ListIterator<Action> planIterator;
    private double[][] dangers;
    private boolean[][] visited;
    private boolean[][] shoot;







    public SearchAI(World.Tile[][] board) {




            /* The world is board[coloumn][row] with initial position (bottom left) being board[0][0] */

        int r=board.length;
        int c=board[0].length;
        int shoot_num=1;
        LinkedList<Action> plan;
        plan = new LinkedList<Action>();

        // Remove the code below //
        visited = new boolean[c][r];
        int x=0;
        int y=0;
        visited[x][y] = true;



         for (int i = 0; i<8; i++)
             plan.add(Agent.Action.FORWARD);
        plan.add(Action.TURN_LEFT);
        plan.add(Action.TURN_LEFT);
        for (int i = 10; i<18; i++)
            plan.add(Action.FORWARD);
        //plan.add(Action.CLIMB);





        // This must be the last instruction.
        planIterator = plan.listIterator();
    }


    @Override
    public Agent.Action getAction(boolean stench, boolean breeze, boolean glitter, boolean bump, boolean scream) {
        return planIterator.next();
    }

}
