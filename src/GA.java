import java.util.ArrayList;
import java.util.Arrays;
import java.util.Random;

public class GA {

    private int[] fruitsEnrg;
    private int[] stdEnrg;

    private int[][] belongs;
    private int[] finalBelongs;

    private int[][] wastes;
    private int[] finalWastes;

    private boolean[][] isSir;
    private boolean[] finalIsSir;

    private final int population = 1000;
    private double[][] values = new double[population][3]; //خانه صفر برای تعداد سیر ها...یک برای تعداد هدررفت...دو برای اشاره گر

    GA(int[] s2, int[] s3) {

        stdEnrg = s2;// انرژی بچه ها

        fruitsEnrg = s3;// انرژی میوه ها

        belongs = new int[population][fruitsEnrg.length];
        finalBelongs = new int[fruitsEnrg.length];

        wastes = new int[population][stdEnrg.length];
        for (int i = 0; i < wastes.length; i++) {
            wastes[i] = stdEnrg.clone();
        }
        finalWastes = new int[stdEnrg.length];

        isSir = new boolean[population][stdEnrg.length];
        finalIsSir = new boolean[stdEnrg.length];

    }

    void init() {
        Random r = new Random();
        int random1, random2;
        for (int j = 0; j < belongs.length; j++) {

            for (int i = 0; i < 2 * fruitsEnrg.length; i++) {
                random1 = (r.nextInt(stdEnrg.length)) + 1;
                random2 = r.nextInt(fruitsEnrg.length);
                if (belongs[j][random2] == 0) {
                    belongs[j][random2] = random1;
                    wastes[j][random1 - 1] = wastes[j][random1 - 1] - fruitsEnrg[random2];
                    if (wastes[j][random1 - 1] <= 0) {
                        isSir[j][random1 - 1] = true;
                    }
                }
            }
        }

        calculateVals();
    }

    private void calculateVals() {
        values = new double[population][3];
        getSirs(isSir);// پر کردن ستون صفر آرایه ولیو
        getTotalWaste(wastes);// پر کردن ستون یک آرایه ولیو
        setPointers();// پر کردن ستون دو آرایه ولیو
        sort();
    }

    private void setPointers() {
        for (int i = 0; i < values.length; i++) {
            values[i][2] = i;
        }
    }

    private void sort() {
        java.util.Arrays.sort(values, new java.util.Comparator<double[]>() {
            public int compare(double[] a, double[] b) {
                return Double.compare(b[0], a[0]);
            }
        });
    }

    public void Go() {
        int which1, which2, howMuch;
        Random r = new Random();

        long start = System.currentTimeMillis();
        double end = start + 4.8 * 1000;
        int count = 0;
        while (System.currentTimeMillis() < end) {
            count++;

            howMuch = (r.nextInt(belongs[0].length));

            which1 = (int) Math.floor(new Random().nextGaussian() * 170 + 500);
            if (which1 > (population - 1)) which1 = population - 1;
            if (which1 < 0) which1 = 0;

            which2 = (int) Math.floor(new Random().nextGaussian() * 170 + 500);
            if (which2 > (population - 1)) which2 = population - 1;
            if (which2 < 0) which2 = 0;

            crossOver((int) values[which1][2], (int) values[which2][2], howMuch);


            if (count % 100 == 0) {
                mutation();
            }

            for (int i = 0; i < wastes.length; i++) {
                wastes[i] = stdEnrg.clone();
            }
            isSir = new boolean[population][stdEnrg.length];
            calculateSirsAndTotalWastes();
            calculateVals();

        }

        //calculate final points
        int[] maxx = GetFinal();
        finalBelongs = belongs[maxx[2]];
        finalIsSir = isSir[maxx[2]];
        finalWastes = wastes[maxx[2]];
        getTotalWaste(finalWastes);
        getSirs(finalIsSir);
    }

    private void mutation() {
        int howMuch = population / 50;
        Random r = new Random();
        int which1, which2;
        for (int i = 0; i < howMuch; i++) {
            which1 = r.nextInt(belongs.length);
            which2 = r.nextInt(belongs[0].length);
            belongs[which1][which2] = r.nextInt(stdEnrg.length) + 1;
        }
    }

    private int[] GetFinal() {
        int[] maxx = new int[3];
        double max1 = (int) values[0][0];
        double max2 = (int) values[0][1];
        int which = (int) values[0][2];

        for (int i = 1; i < values.length; i++) {
            if (values[i][0] != max1) {
                break;
            } else {
                if (values[i][1] > max2) {
                    max2 = values[i][1];
                    which = (int) values[i][2];
                }
            }
        }

        maxx[0] = (int) max1;
        maxx[1] = (int) max2;
        maxx[2] = which;
        return maxx;
    }

    private void crossOver(int which1, int which2, int howMuch) {
        int[] temp = Arrays.copyOfRange(belongs[which1], 0, howMuch);
        int[] temp1 = Arrays.copyOfRange(belongs[which2], 0, howMuch);
        System.arraycopy(temp, 0, belongs[which2], 0, howMuch);
        System.arraycopy(temp1, 0, belongs[which1], 0, howMuch);
    }

//    private void calculateSirsAndTotalWastes(int male, int female) {
//        int m = (int) values[male][2];
//        int f = (int) values[female][2];
//
//        wastes[m] = stdEnrg.clone();
//        wastes[f] = stdEnrg.clone();
//        isSir[m] = new boolean[stdEnrg.length];
//        isSir[f] = new boolean[stdEnrg.length];
//
//        for (int i = 0; i < belongs[m].length; i++) {
//            if (belongs[m][i] != 0) {
//                wastes[m][belongs[m][i] - 1] = wastes[m][belongs[m][i] - 1] - fruitsEnrg[i];
//                if (wastes[m][belongs[m][i] - 1] <= 0) {
//                    isSir[m][belongs[m][i] - 1] = true;
//                }
//            }
//
//        }
//        for (int i = 0; i < belongs[f].length; i++) {
//            if (belongs[f][i] != 0) {
//                wastes[f][belongs[f][i] - 1] = wastes[f][belongs[f][i] - 1] - fruitsEnrg[i];
//                if (wastes[f][belongs[f][i] - 1] <= 0) {
//                    isSir[f][belongs[f][i] - 1] = true;
//                }
//            }
//
//        }
//    }

    private void calculateSirsAndTotalWastes() {
        for (int j = 0; j < belongs.length; j++) {
            for (int i = 0; i < belongs[j].length; i++) {
                if (belongs[j][i] != 0) {
                    wastes[j][belongs[j][i] - 1] = wastes[j][belongs[j][i] - 1] - fruitsEnrg[i];
                    if (wastes[j][belongs[j][i] - 1] <= 0) {
                        isSir[j][belongs[j][i] - 1] = true;
                    }
                }
            }
        }
    }

    private void getTotalWaste(int[][] s) {
        int tw;
        for (int i = 0; i < s.length; i++) {
            tw = 0;
            for (int j = 0; j < s[i].length; j++) {
                if (s[i][j] < 0) {
                    tw += s[i][j];
                }
            }
            values[i][1] = tw;
        }
    }

    private void getSirs(boolean[][] s) {
        int isSir;
        for (int i = 0; i < s.length; i++) {
            isSir = 0;
            for (int j = 0; j < s[i].length; j++) {
                if (s[i][j]) {
                    isSir++;
                }
            }
            values[i][0] = isSir;
        }
    }


    public int getTotalWaste(int[] s) {
        int tw = 0;
        for (int i = 0; i < s.length; i++) {
            if (s[i] < 0) tw += s[i];
        }
        return tw;
    }

    public int getSirs(boolean[] s) {
        int isSir = 0;
        for (int i = 0; i < s.length; i++) {
            if (s[i]) isSir++;
        }
        return isSir;
    }

    void getAll() {
        System.out.print("stdEnrg :    ");
        for (int aStdEnrg : stdEnrg) {
            System.out.print(aStdEnrg + " ");
        }
        System.out.println();
        System.out.println("##################");

        System.out.print("fruitsEnrg : ");
        for (int aFruitsEnrg : fruitsEnrg) {
            System.out.print(aFruitsEnrg + " ");
        }
        System.out.println();
        System.out.println("##################");

        System.out.print("belongs :    ");
        System.out.println();
        for (int[] belong : belongs) {
            for (int aBelong : belong) {
                System.out.print(aBelong + " ");
            }
            System.out.println();
        }
        System.out.println("##################");

        System.out.print("wastes :    ");
        System.out.println();
        for (int[] waste : wastes) {
            for (int aWaste : waste) {
                System.out.print(aWaste + " ");
            }
            System.out.println();
        }
        System.out.println("##################");

        System.out.print("isSir :    ");
        System.out.println();
        for (boolean[] anIsSir : isSir) {
            for (boolean anAnIsSir : anIsSir) {
                System.out.print(anAnIsSir + " ");
            }
            System.out.println();
        }
        System.out.println("##################");

        System.out.print("values :    ");
        System.out.println();
        for (double[] value : values) {
            for (double aValue : value) {
                System.out.print(aValue + " ");
            }
            System.out.println();
        }

//        System.out.print("race :    ");
//        for (int race : race) {
//            System.out.print(race + " ");
//        }
//        System.out.println("");
//        System.out.println("##################");

        System.out.print("finalBelongs :    ");
        for (int finalBelongs : finalBelongs) {
            System.out.print(finalBelongs + " ");
        }
        System.out.println("");
        System.out.println("##################");

        System.out.print("finalIsSir :    ");
        for (boolean finalIsSir : finalIsSir) {
            System.out.print(finalIsSir + " ");
        }
        System.out.println("");
        System.out.println("##################");

        System.out.print("finalWastes :    ");
        for (int finalWastes : finalWastes) {
            System.out.print(finalWastes + " ");
        }
        System.out.println("");
        System.out.println("##################");

        System.out.print("sir : " + getSirs(finalIsSir));
        System.out.println();
        System.out.print("tw : " + getTotalWaste(finalWastes));

        System.out.println();
        System.out.println("&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&");
        System.out.println();
    }

    public void getAll2() {
        System.out.println(getSirs(finalIsSir) + " " + Math.abs(getTotalWaste(finalWastes)));
        ArrayList a;
        for (int i = 0; i < stdEnrg.length; i++) {
            int count = 0;
            a = new ArrayList();
            for (int j = 0; j < finalBelongs.length; j++) {
                if(finalBelongs[j] == (i+1)){
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