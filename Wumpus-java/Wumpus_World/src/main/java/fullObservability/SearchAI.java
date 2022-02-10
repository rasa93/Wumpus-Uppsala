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
        Action a;
        public Node(State s, Node parent, int cost){
            this.state=s;
            this.parent=parent;
            this.cost=cost;
        }

        public State getState(){
            return this.state;
        }
        public int getCost(){
            return this.cost;
        }


    }

    private class State{
        int x;
        int y;
        int dir;
        int cost;
        boolean hasGold;
        boolean hasArrow;
        boolean hasWumpus;
        boolean goldLooted=false;
        public State(int column, int row, int dir, boolean hasGold, boolean hasArrow){
            this.x=column;
            this.y=row;
            this.dir=dir;
            this.hasGold=hasGold;
            this.hasArrow=hasArrow;
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
        public void setCost(int cost){
            this.cost=cost;
        }
        public int getCost(){
            return this.cost;
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
        System.out.println("x: ");
        System.out.println("Action: "+a);
        this.plan.add(a);
        if(a==Action.TURN_LEFT) {
            s.setCost(s.getCost()-1);

        }
        if(a==Action.TURN_RIGHT) {
            s.setCost(s.getCost()-1);

        }
        if(a==Action.FORWARD) {
            s.setCost(s.getCost()-1);
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

    private ArrayList<Node> expand(Node n, int column, int row, World.Tile[][] board){
        ArrayList<Node> nodes = new ArrayList<>();
        State s =n.getState();
        for (Agent.Action a : Agent.Action.values()) {
            State new_s = result(s,a,column,row,board);
            Node new_n = new Node(new_s,n,n.getState().getCost());
            nodes.add(new_n);
        }
        return nodes;

    }

    private Optional<Node> best_fist_search(PriorityQueue<Node> f,Node initial_node, HashMap<State, Node> r, int column,int row,World.Tile[][] board){
        PriorityQueue<Node> frontier = new PriorityQueue<Node>();
        path.add(initial_node);
        Node n=null;
        HashMap<State, Node> reached = new HashMap<>();
        reached=r;
        frontier=f;
        frontier.add(initial_node);
        while(!frontier.isEmpty()){
            n=frontier.poll();
            if(n.getState().getHasGold()){
                return Optional.ofNullable(n);
            }
            for(Node child_n : expand(n,column,row,board)){
                State s =child_n.getState();
                if(!reached.containsKey(s) || child_n.getState().getCost()<reached.get(s).getState().getCost()){
                    reached.put(s,child_n);
                    frontier.add(child_n);
                    path.add(child_n);
                }
            }

        }

        return Optional.empty();
    }

    private ListIterator<Action> planIterator;


    public SearchAI(World.Tile[][] board) {




            /* The world is board[coloumn][row] with initial position (bottom left) being board[0][0] */

        int r=board.length;
        int c=board[0].length;
        State initial_s = new State(c,r,0,true,true);
        Node initial_n = new Node(initial_s,null,0);
        PriorityQueue<Node> frontier = new PriorityQueue<Node>();
        HashMap<State, Node> reached = new HashMap<State, Node>();
        best_fist_search(frontier,initial_n,reached,c,r,board);

        // This must be the last instruction.
        planIterator = plan.listIterator();
        System.out.println(plan);
    }


    @Override
    public Agent.Action getAction(boolean stench, boolean breeze, boolean glitter, boolean bump, boolean scream) {
        return planIterator.next();
    }

}
