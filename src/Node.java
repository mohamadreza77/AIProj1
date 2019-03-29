import java.util.ArrayList;

public class Node {
    int[][] validValues;
    int[][] assign;
    int[] NoOfValids;
    int[] wastes;
    boolean[] isFull;
    int[] belongs;

    public Node(Node n) {
        this.validValues = new int[n.validValues.length][n.validValues[0].length];
        this.NoOfValids = new int[n.validValues.length];
        this.wastes = new int[n.validValues.length];
        this.isFull = new boolean[n.validValues.length];
        this.assign= new int[n.validValues.length][n.validValues[0].length];
        this.belongs = new int[n.validValues[0].length];
    }
    public Node(int NoS, int NoF) {
        this.validValues = new int[NoS][NoF];
        this.NoOfValids = new int[NoS];
        this.wastes = new int[NoS];
        this.isFull = new boolean[NoS];
        this.assign= new int[NoS][NoF];
        this.belongs = new int[NoF];
    }

    public void setValidValues(int[][] validValues) {
        for (int i = 0; i < validValues.length; i++) {
            for (int j = 0; j < validValues[i].length; j++) {
                this.validValues[i][j] = validValues[i][j];
            }
        }
    }

    public void setAssign(int[][] assign) {
        for (int i = 0; i < assign.length; i++) {
            for (int j = 0; j < assign[i].length; j++) {
                this.assign[i][j] = assign[i][j];
            }
        }
    }

    public void setWastes(int[] wastes) {
        for (int i = 0; i < wastes.length; i++) {
            this.wastes[i] = wastes[i];
        }
    }

    public void setIsFull(boolean[] isFull) {
        for (int i = 0; i < isFull.length; i++) {
            this.isFull[i] = isFull[i];
        }
    }

    public void setBelongs(int[] belongs) {
        for (int i = 0; i < belongs.length; i++) {
            this.belongs[i] = belongs[i];
        }
    }

    public void setNoOfValids(int[] noOfValids) {
        for (int i = 0; i < noOfValids.length; i++) {
            this.NoOfValids[i] = noOfValids[i];
        }
    }

    public void inh(Node n){
        setBelongs(n.belongs);
        setAssign(n.assign);
        setValidValues(n.validValues);
        setIsFull(n.isFull);
        setWastes(n.wastes);
        setNoOfValids(n.NoOfValids);
    }
}
