package fullObservability;

import wumpus.Agent;
import wumpus.World;

import java.util.*;


public class SearchAI extends Agent {
    ArrayList<Node> path = new ArrayList<>();
    LinkedList<Action> plan=new LinkedList<>();
    private class Node{

        State state;
        Node parent;
        int cost;

        public Node(State s, Node parent, int cost){
            this.state=s;
            this.parent=parent;
            this.cost=cost;
        }

        public State getState(){
            return this.state;
        }
        public void setState(State s){
            this.state = s;
        }
        public int getCost() { return cost; }
        public void setCost(int cost){this.cost=cost; }

        public void setParent(Node n){
            this.parent = n;
        }
        public Node getParent(Node n){
            return this.parent;
        }

    }

    private class State implements Comparable<State>{
        int x;
        int y;
        int colDimension;
        int rowDimension;
        int dir;

        boolean hasGold;
        boolean hasArrow;
        boolean hasWumpus;
        boolean pit;
        boolean goldLooted;
        boolean bump;
        public State(int x, int y, int column, int row, int dir, boolean hasGold,
                     boolean hasArrow, boolean wumpus, boolean pit, boolean goldLooted){
            this.x=x;
            this.y=y;
            this.colDimension=column;
            this.rowDimension=row;
            this.dir=dir;
            this.hasGold=hasGold;
            this.hasArrow=hasArrow;
            this.hasWumpus=wumpus;
            this.pit=pit;
            this.goldLooted=goldLooted;
        }

        @Override
        public int compareTo(State s){
            if (this.x == s.getX() & this.y == s.getY() & this.dir == s.getDir()
                    & this.hasGold == s.getHasGold()
                    & this.hasArrow == s.getHasArrow() & this.hasWumpus == s.getHasWumpus()
                    & this.goldLooted == s.getGoldLooted()) {
                return 0;
            }
            else
                return -1;

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

        public int getColDimension() {
            return colDimension;
        }

        public int getRowDimension() {
            return this.rowDimension;
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

        public boolean getHasPit(){
            return this.pit;
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



    private Node result(Node n, Action a, World.Tile[][] board){
        State s = n.getState();
        int agentX=n.getState().getX();
        int agentY= n.getState().getY();
        int dir= s.getDir();
        int cost=n.getCost();
        boolean hasGold=s.getHasGold();
        boolean hasArrow=s.getHasArrow();
        boolean hasWumpus=s.getHasWumpus();
        boolean pit=s.getHasPit();
        boolean goldLooted=s.getGoldLooted();

        if(a==Action.TURN_LEFT) {
            if (--dir < 0) {;
                dir=3;
            }
        }
        if(a==Action.TURN_RIGHT) {
            if (++dir > 3) {
                dir=0;
            }
        }
        if(a==Action.FORWARD) {

            cost-=1;
            if ( dir == 0 && agentX+1 < s.getColDimension() )
                agentX=agentX+1;
            else if (dir == 1 && agentY-1 >= 0 )
                agentY=agentY-1;
            else if ( dir == 2 && agentX-1 >= 0 )
                agentX=agentX-1;
            else if ( dir == 3 && agentY+1 < s.getRowDimension() )
                agentY=agentY+1;
            if ( s.getHasWumpus() || s.getHasPit())
            {
                cost=cost-1000;
            }
            if ( board[agentX][agentY].getPit() || board[agentX][agentY].getWumpus() && !s.getHasWumpus() )
            // wumpus only appears one time in this game.
            {
                cost -= 1000;
            }
            if(board[agentX][agentY].getGold()){
                hasGold=true;
            }
            State new_s = new State(agentX,agentY,s.getColDimension(),s.getRowDimension(),dir,hasGold,
                    hasArrow,hasWumpus, pit, goldLooted);
            return new Node(new_s, n, cost);
        }
        if(a==Action.SHOOT){
            if (s.getHasArrow() )
            {
                hasArrow=false;
                cost=cost-10;
                if ( dir == 0 )
                {
                    for ( int x = agentX; x < s.getColDimension(); ++x )
                        if ( board[x][agentY].getWumpus() && s.getHasWumpus())
                        {
                            hasWumpus=false;
                        }

                }
                else if ( dir == 1 )
                {
                    for ( int y = agentY; y >= 0; --y )
                        if ( board[agentX][y].getWumpus() && s.getHasWumpus() )
                        {
                            hasWumpus=false;
                        }
                }
                else if ( dir == 2 )
                {
                    for ( int x = agentX; x >= 0; --x )
                        if ( board[x][agentY].getWumpus() && s.getHasWumpus() )
                        {
                            hasWumpus=false;
                        }
                }
                else if ( dir == 3 )
                {
                    for ( int y = agentY; y < s.getRowDimension(); ++y )
                        if ( board[agentX][y].getWumpus() && s.getHasWumpus())
                        {
                            hasWumpus=false;
                        }
                }
            }
            State new_s = new State(agentX,agentY,s.getColDimension(),s.getRowDimension(),dir,hasGold,
                    hasArrow,hasWumpus, pit, goldLooted);
            return new Node(new_s, n, cost);
        }
        if(a==Action.GRAB){
            if ( s.getHasGold() )
            {
                hasGold=false;
                goldLooted=true;
            }
        }
        if(a==Action.CLIMB){
            if ( agentX == 0 && agentY == 0 )
            {
                if (s.getGoldLooted())
                    cost+=1000;
            }
        }
        State new_s = new State(agentX,agentY,s.getColDimension(),s.getRowDimension(),dir,hasGold,
                hasArrow,hasWumpus, pit, goldLooted);
        return new Node(new_s, n, cost);
    }

    private ArrayList<Node> expand(Node n, World.Tile[][] board){
        ArrayList<Node> nodes = new ArrayList<>();
        State s =n.getState();
        for (Agent.Action a : Agent.Action.values()) {
            //State new_s = result(s,a, board);
            Node new_n = result(n,a,board);
            nodes.add(new_n);
        }
        return nodes;

    }
    // Optional<Node>
    private Optional<Node> best_fist_search(PriorityQueue<Node> frontier,Node initial_node, HashMap<State, Node> reached, World.Tile[][] board){
        //this.path.add(initial_node);
        Node return_node=null;
        frontier.add(initial_node);
        while(!frontier.isEmpty()){
            Node n=frontier.poll();
            if(n.getState().getHasGold()){
                return Optional.ofNullable(n);
            }
            for(Node child_n : expand(n, board)){
                State s =child_n.getState();
                if(!reached.containsKey(s) || child_n.getCost()<reached.get(s).getCost()){
                    reached.put(s,child_n);
                    frontier.add(child_n);
                    child_n.setParent(n);
                }
            }
        }
        return Optional.ofNullable(return_node);

        //return Optional.empty();

    }

    private ListIterator<Action> planIterator;


    public void SearchAI(World.Tile[][] board) {

        /* The world is board[coloumn][row] with initial position (bottom left) being board[0][0] */


        int r=board.length;
        int c=board[0].length;
        State initial_s = new State(0,0,c,r,0, board[0][0].getGold(),true,
                board[0][0].getWumpus(), board[0][0].getPit(), false);
        Node initial_n = new Node(initial_s,null,0);
        PriorityQueue<Node> frontier = new PriorityQueue<>();
        HashMap<State, Node> reached = new HashMap<State, Node>();
        best_fist_search(frontier,initial_n,reached, board);

        // This must be the last instruction.
        planIterator = plan.listIterator();

        System.out.println(plan);
        System.out.println("ps: "+path.size());
        for(int i=0;i<path.size();i++){
            System.out.print("x: "+path.get(i).getState().getX()+" ");
            System.out.println("y: "+path.get(i).getState().getY());
        }

    }


    @Override
    public Agent.Action getAction(boolean stench, boolean breeze, boolean glitter, boolean bump, boolean scream) {
        return planIterator.next();
    }

}