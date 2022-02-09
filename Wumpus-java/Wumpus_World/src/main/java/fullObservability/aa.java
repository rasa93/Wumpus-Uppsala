package fullObservability;

import wumpus.World;

import java.sql.Array;
import java.util.ArrayList;
import java.util.Optional;

public class aa {
    private static class Node {
        private int x;
        private int y;
        private int dir;

        public Node(int column, int row, int dir) {
            this.x = column;
            this.y = row;
            this.dir = dir;
        }
    }

    public ArrayList<Node> opte(){
        ArrayList<Node> n = new ArrayList<>();
        return n;
    }


    public static void main(String args[]){
        Optional<String> opt = Optional.ofNullable("자바 Optional 객체");
        if(opt.isPresent()) {

            System.out.println(opt.get());

        }
        System.out.println(opt.get().getClass().getName());
        aa a = new aa();
        ArrayList<Node> cc=a.opte();
        System.out.println(cc);
        System.out.println(cc.size());
        }




}
