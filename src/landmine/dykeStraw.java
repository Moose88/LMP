package landmine;

import jig.Vector;

import java.util.LinkedList;
import java.util.List;

public class dykeStraw {
    private static dykeStraw safeInstance;
    private static dykeStraw attackInstance;

    private dykeStraw(){

    }
    public static dykeStraw getSafeInstance(){
        if(safeInstance == null){
            safeInstance = new dykeStraw();
        }
        return safeInstance;
    }

    public static dykeStraw getAttackInstance(){
        if(attackInstance == null){
            attackInstance = new dykeStraw();
        }
        return attackInstance;
    }

    private int[][] dv;
    private Vector[][] dpi;

    public Vector getPi(Vector me){
        int x = (int)me.getX();
        int y = (int)me.getY();

        return dpi[x][y];
    }

    private LinkedList<Vector> Q;

    private Vector extract_min(List<Vector> vectorList){
        Vector min = null;
        int minDv = Integer.MAX_VALUE;

        for(Vector vector : vectorList){
            if(min == null)
                min = vector;
            else {
                int x = (int)vector.getX();
                int y = (int)vector.getY();
                if(dv[x][y] < minDv){
                    minDv = dv[x][y];
                    min = vector;
                }
            }
        }
        vectorList.remove(min);
        return min;
    }

    private void initSources(Vector[] s){
        int width = dv.length;
        int height = dv[0].length;

        for(int i = 0; i < width; i++){
            for(int j = 0; j < height; j++){
                dv[i][j] = Integer.MAX_VALUE;
            }
        }

        for(Vector vector : s){
            dv[(int)vector.getX()][(int)vector.getY()] = 0;
        }
    }

    private void relax(Vector u, Vector v,int w){
        int ux = (int)u.getX();
        int uy = (int)u.getY();
        int vx = (int)v.getX();
        int vy = (int)v.getY();

        if(dv[vx][vy] > dv[ux][uy] + w){
            dv[vx][vy] = dv[ux][uy] + w;
            dpi[vx][vy] = u;
        }
    }

    public void perform(int[][] G,Vector[] s){
        int width = G.length;
        int height = G[0].length;

        dv = new int[width][height];
        dpi = new Vector[width][height];

        initSources(s);

        Q = new LinkedList<>();
        for(int i=0; i<width; i++){
            for(int j=0; j<height; j++){
                Q.add(new Vector(i,j));
            }
        }

        while(!Q.isEmpty()){
            Vector u = extract_min(Q);
            int ux = (int)u.getX();
            int uy = (int)u.getY();
            int w = G[ux][uy];
            if(w == Integer.MAX_VALUE)
                continue;


            // Up
            if(G[ux][uy-1] != Integer.MAX_VALUE) {
                relax(u, new Vector(ux, uy - 1), w );
            }
            // Down
            if(G[ux][uy+1] != Integer.MAX_VALUE) {
                relax(u, new Vector(ux, uy + 1), w );
            }
            // Left
            if(G[ux-1][uy] != Integer.MAX_VALUE) {
                relax(u, new Vector(ux - 1, uy), w );
            }
            // Right
            if(G[ux+1][uy] != Integer.MAX_VALUE) {
                relax(u, new Vector(ux + 1, uy), w );
            }
        }

//        for(int i = 0; i < dv.length; i++){
//            for(int j = 0; j < dv[i].length; j++){
//                System.out.print(String.format(" %s ",dv[i][j]).replace("], ", "]\n"
//                ).replace("[[", "["
//                ).replace("]]", "]"
//                ).replace(", ", " "
//                ).replace(String.valueOf(Integer.MAX_VALUE),"W"
//                ));
//            }
//            System.out.println();
//        }
    }

}
