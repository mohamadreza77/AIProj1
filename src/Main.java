import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        Scanner s = new Scanner(System.in);

        int NoS = s.nextInt();
        int NoF = s.nextInt();

        int[] Enrgs = new int[NoS];
        for (int i = 0; i < Enrgs.length; i++) {
            Enrgs[i] = s.nextInt();
        }

        int[] fruits = new int[NoF];
        for (int i = 0; i < fruits.length; i++) {
            fruits[i] = s.nextInt();
        }

//        int[][] validValues = new int[NoS][NoF];

//        for (int i = 0; i < NoS; i++) {
//            for (int j = 0; j < NoF; j++) {
//                validValues[i][j] = (fruits[j]);
//            }
//        }

        int Nopairs = s.nextInt();
        int[][] pairs = new int[Nopairs][2];
        for (int i = 0; i < Nopairs; i++) {
            pairs[i][0] = s.nextInt();
            pairs[i][1] = s.nextInt();
        }

        Node node = new Node(NoS,NoF);
        for (int i = 0; i < NoS; i++) {
            for (int j = 0; j < NoF; j++) {
                node.validValues[i][j] = (fruits[j]);
            }
        }
        for (int i = 0; i < node.validValues.length; i++) {
            node.NoOfValids[i] = NoF;
        }
        for (int i = 0; i < node.validValues.length; i++) {
            node.wastes[i] = Enrgs[i];
        }
        for (int i = 0; i < node.validValues.length; i++) {
            node.isFull[i] = false;
        }
//        long start = System.currentTimeMillis();

//        HillClimbing hc = new HillClimbing(Ss, Fs);
//        hc.init();
//        hc.Go();
//        hc.getAll2();

//        SimulatedAnnealing sa = new SimulatedAnnealing(Ss, Fs);
//        sa.init();
//        sa.Go();
//        sa.getAll2();

//        GA ga = new GA(Ss, Fs);
//        ga.init();
//        ga.Go();
//        ga.getAll2();

        CSP csp = new CSP(node,pairs);
        csp.Search();



        long end = System.currentTimeMillis();

//        NumberFormat formatter = new DecimalFormat("#0.00000");
//        System.out.print("Execution time is " + formatter.format((end - start) / 1000d) + " seconds");

    }
}