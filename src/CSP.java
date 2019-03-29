import java.util.ArrayList;
import java.util.Random;
import java.util.Stack;

public class CSP {

    int[][] pairs;
    Node root;

    Stack<Node> stack = new Stack();

    public CSP(Node root, int[][] pairs) {

        this.root = root;

        this.pairs = pairs; // student pairs sitting next to each other

        stack.push(this.root);

    }

    public void Search() {

        Node finalNode = null;

        while (!stack.isEmpty()) {
            Node n = stack.peek();
            if (isGoal(n)) {
                finalNode = n;
                break;
            }
            int who = checkMRV(n);
            if(who == -1) {
                stack.pop();
            }
            else successor(n, who);
        }


        getAll2(finalNode);
    }


    private void printFinal(Node finalNode) {
        if (finalNode == null) System.out.println(":///");
        else {
            for (int i = 0; i < finalNode.belongs.length; i++) {
                System.out.print(finalNode.belongs[i] + " ");
            }
        }
    }

    private boolean isGoal(Node n) {
        boolean b = true;
        for (int i = 0; i < n.isFull.length; i++) {
            if (!n.isFull[i]) {
                b = false;
                break;
            }
        }

        return b;
    }

    public void successor(Node n, int who) {

        int rr;


//        ArrayList<Integer> arrayList = new ArrayList();
//        for (int i = 0; i < n.validValues[who].length; i++) {
//            if(n.validValues[who][i] != 0 && n.assign[who][i] == 0){
//                arrayList.add(n.validValues[who][i]);
//                rr = i;
//                break;
//            }
//        }

        rr = LCV(n,who,n.validValues[who]);

        if(rr == -1){
            stack.pop();
        }else {
            n.assign[who][rr] = 1;

            Node child = new Node(n);
            child.inh(n);

            child.wastes[who] = child.wastes[who] - n.validValues[who][rr];
            if (child.wastes[who] <= 0) child.isFull[who] = true;

            child.validValues[who][rr] = 0;
            child.NoOfValids[who]--;
            for (int j = 0; j < child.validValues.length; j++) {
                if(j != who) {
                    if(child.validValues[j][rr] != 0) {
                        child.validValues[j][rr] = 0;
                        child.NoOfValids[j]--;
                    }
                }
            }

            child.belongs[rr] = who + 1;

            ForwardChecking(child, who, n.validValues[who][rr]);

            stack.push(child);
        }

    }

    private void ForwardChecking(Node child, int who, int fruitEnrg) {
        for (int i = 0; i < pairs.length; i++) {
            if ((who + 1) == pairs[i][0]) {
                fc(child, pairs[i][1], fruitEnrg);
            } else if ((who + 1) == pairs[i][1]) {
                fc(child, pairs[i][0], fruitEnrg);
            }
        }
    }

    private void fc(Node child, int ii, int fruitEnrg) {
        ii--;
        for (int i = 0; i < child.validValues[ii].length; i++) {
            if (child.validValues[ii][i] == fruitEnrg) {
                child.validValues[ii][i] = 0;
                child.NoOfValids[ii]--;
            }
        }
    }

    public int checkMRV(Node n) {
        int who = 0;
        int howMuch = Integer.MAX_VALUE;

        for (int i = 0; i < n.NoOfValids.length; i++) {
            if(n.isFull[i] == false) {
                if (n.NoOfValids[i] <= howMuch) {
                    who = i;
                    howMuch = n.NoOfValids[i];
                }
            }
        }

        if (howMuch == 0) return -1;
        else
            return who;
    }

    public int LCV(Node n, int who, int[] arrayList){

        int min;
        int[] m = new int[arrayList.length];

        for (int i = 0; i < pairs.length; i++) {

            if ((who + 1) == pairs[i][0]) {
                int currPair = pairs[i][1]-1;
                for (int j = 0; j < arrayList.length; j++) {
                    if(arrayList[j] != 0 && n.assign[who][j] == 0) {
                        int curr = arrayList[j];
                        for (int k = 0; k < n.validValues[currPair].length; k++) {
//                            if (n.validValues[currPair][k] != 0 && n.assign[currPair][k] == 0) {
                                if (n.validValues[currPair][k] == curr) {
                                    m[j]++;
                                }

//                            }
                        }
                    }else  {
                        m[j] = Integer.MAX_VALUE;
                    }
                }


            } else if ((who + 1) == pairs[i][1]) {
                int currPair = pairs[i][0]-1;
                for (int j = 0; j < arrayList.length; j++) {
                    if(arrayList[j] != 0 && n.assign[who][j] == 0) {
                        int curr = arrayList[j];
                        for (int k = 0; k < n.validValues[currPair].length; k++) {
//                            if (n.validValues[currPair][k] != 0 && n.assign[currPair][k] == 0) {
                                if (n.validValues[currPair][k] == curr) {
                                    m[j]++;
                                }
//                            }
                        }
                    }else{
                        m[j] = Integer.MAX_VALUE;
                    }
                }
            }
        }


        min = getMinValue(m);

        if(m[min] == Integer.MAX_VALUE) return -1;

        return min;
    }


    public int getMinValue(int[] array) {
        int minValue = array[0];
        int k = 0;
        for (int i = 1; i < array.length; i++) {
            if (array[i] <= minValue) {
                minValue = array[i];
                k = i;
            }
        }
        return k;
    }


    public void getAll2(Node finalNode) {
//        System.out.println(getSirs(isSir) + " " + Math.abs(getTotalWaste(wastes)));
        ArrayList a;
        for (int i = 0; i < finalNode.isFull.length; i++) {
            int count = 0;
            a = new ArrayList();
            for (int j = 0; j < finalNode.belongs.length; j++) {
                if(finalNode.belongs[j] == (i+1)){
                    a.add(j + 1);
                    count ++;
                }
            }
            System.out.print(count + " ");
            for (int j = 0; j < a.size(); j++) {
                System.out.print(a.get(j) + " ");
            }
            System.out.println();
        }

    }
}
