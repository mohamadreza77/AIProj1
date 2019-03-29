import java.util.ArrayList;
import java.util.Random;

public class SimulatedAnnealing {

    int[] fruitsEnrg;
    int[] stdEnrg;
    int[] belongs;
    int[] wastes;
    boolean[] isSir;
    int Temperature = 90_000;

    public SimulatedAnnealing(int[] s2, int[] s3) {

        stdEnrg = s2;// انرژی بچه ها

        fruitsEnrg = s3;// انرژی میوه ها

        belongs = new int[fruitsEnrg.length];// میوه i رو به چه کسی دادیم
        for (int i = 0; i < belongs.length; i++) {
            belongs[i] = 0;// میوه i به کسی تعلق ندارد
        }
        wastes = new int[stdEnrg.length];// بچه i چه قدر انرژی هدر داد
        for (int i = 0; i < wastes.length; i++) {
            wastes[i] = stdEnrg[i];// هدررفت بچه i همان انرژی است = 0
        }
        isSir = new boolean[stdEnrg.length];// بچه i آیا سیر است
        for (int i = 0; i < isSir.length; i++) {
            isSir[i] = false; // هیچ بچه ای سیر نیست
        }
    }

    public void init() {
        Random r = new Random();
        int random1, random2;
        for (int i = 0; i < 2 * fruitsEnrg.length; i++) {
            random1 = (r.nextInt(stdEnrg.length)) + 1;
            random2 = r.nextInt(fruitsEnrg.length);
            if (belongs[random2] == 0) {
                belongs[random2] = random1;
                wastes[random1 - 1] = wastes[random1 - 1] - fruitsEnrg[random2];
                if (wastes[random1 - 1] <= 0) {
                    isSir[random1 - 1] = true;
                }
            }
        }
    }

    public void Go() {
        int[] tempbelongs;
        int which1, which2;
        Random r = new Random();
        int deltaE;
        double possibility;
        for (int j = 0; j < 400_000; j++) {
            tempbelongs = belongs.clone();
            if (j % 50 != 0) {
                which1 = r.nextInt(tempbelongs.length);
                which2 = (r.nextInt(stdEnrg.length)) + 1;
                if (tempbelongs[which1] == 0) {
                    tempbelongs[which1] = which2;
                } else {
                    while (tempbelongs[which1] == which2) {
                        which2 = (r.nextInt(stdEnrg.length)) + 1;
                    }
                    tempbelongs[which1] = which2;
                }

                tempbelongs[which1] = which2;
            } else {
                which1 = r.nextInt(tempbelongs.length);
                tempbelongs[which1] = 0;
            }

            int beforeSirs = getSirs(isSir);
            int beforeTW = getTotalWaste(wastes);
            ArrayList a = calculateSirsAndTotalWastes(tempbelongs);
            int afterSirs = (int) a.get(0);
            int afterTW = (int) a.get(1);

            deltaE = (afterSirs - beforeSirs);
//            if(Temperature > 0) possibility = Math.pow(Math.E, ((double) deltaE / Temperature));
//            else possibility = 0;
//            System.out.println("possiblity : " + possibility + " deltaE " + deltaE + " tem " + Temperature + " devide " + ((double) deltaE / Temperature));


            if (deltaE > 0) {
                belongs = tempbelongs.clone();
                isSir = (boolean[]) a.get(2);
                wastes = (int[]) a.get(3);
            } else if (deltaE == 0) {
                if (beforeTW < afterTW) {
                    belongs = tempbelongs.clone();
                    isSir = (boolean[]) a.get(2);
                    wastes = (int[]) a.get(3);
                } else {
                    deltaE = 3*(afterTW - beforeTW);
                    if(Temperature > 0) possibility = Math.pow(Math.E, ((double) deltaE / (Temperature/1000)));
                    else possibility = 0;
//                    System.out.println("possiblity first : " + possibility + " deltaE " + deltaE + " tem " + Temperature + " devide " + ((double) deltaE / (Temperature/1000)) + " AF " + afterTW + " BEF " + beforeTW);
                    if (r.nextDouble() < possibility) {
                        belongs = tempbelongs.clone();
                        isSir = (boolean[]) a.get(2);
                        wastes = (int[]) a.get(3);
                    }
                }
            } else {
                deltaE = 20*deltaE;
                if(Temperature > 0) possibility = Math.pow(Math.E, ((double) deltaE / (Temperature/1000)));
                else possibility = 0;
//                System.out.println("possiblity second : " + possibility + " deltaE " + deltaE + " tem " + Temperature + " devide " + ((double) deltaE / (Temperature/1000)));
                if (r.nextDouble() < possibility) {
                    belongs = tempbelongs.clone();
                    isSir = (boolean[]) a.get(2);
                    wastes = (int[]) a.get(3);
                }
            }
            Temperature -= 1;
        }
    }

    private ArrayList calculateSirsAndTotalWastes(int[] tempbelongs) {
        int[] tempStdEnrg = stdEnrg.clone();
        int[] tempWastes = stdEnrg.clone();
        boolean[] tempIsSir = new boolean[isSir.length];

        for (int i = 0; i < tempbelongs.length; i++) {
            if (tempbelongs[i] != 0) {
                tempStdEnrg[tempbelongs[i] - 1] = tempStdEnrg[tempbelongs[i] - 1] - fruitsEnrg[i];
                tempWastes[tempbelongs[i] - 1] = tempStdEnrg[tempbelongs[i] - 1];
                if (tempStdEnrg[tempbelongs[i] - 1] <= 0) {
                    tempIsSir[tempbelongs[i] - 1] = true;
                }
            }
        }


        ArrayList a = new ArrayList();
        a.add(getSirs(tempIsSir));
        a.add(getTotalWaste(tempWastes));
        a.add(tempIsSir);
        a.add(tempWastes);

        return a;
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

    public void getAll() {
        System.out.print("stdEnrg :    ");
        for (int i = 0; i < stdEnrg.length; i++) {
            System.out.print(stdEnrg[i] + " ");
        }
        System.out.println();
        System.out.println("##################");

        System.out.print("fruitsEnrg : ");
        for (int i = 0; i < fruitsEnrg.length; i++) {
            System.out.print(fruitsEnrg[i] + " ");
        }
        System.out.println();
        System.out.println("##################");

        System.out.print("belongs :    ");
        for (int i = 0; i < belongs.length; i++) {
            System.out.print(belongs[i] + " ");
        }
        System.out.println();
        System.out.println("##################");

        System.out.print("wastes :    ");
        for (int i = 0; i < wastes.length; i++) {
            System.out.print(wastes[i] + " ");
        }
        System.out.println();
        System.out.println("##################");

        System.out.print("isSir :    ");
        for (int i = 0; i < isSir.length; i++) {
            System.out.print(isSir[i] + " ");
        }
        System.out.println();
        System.out.println("##################");

        System.out.print("sir : " + getSirs(isSir));
        System.out.println();
        System.out.print("tw : " + getTotalWaste(wastes));
        System.out.println();
        System.out.println("&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&");
        System.out.println();
    }

    public void getAll2() {
        System.out.println(getSirs(isSir) + " " + Math.abs(getTotalWaste(wastes)));
        ArrayList a;
        for (int i = 0; i < stdEnrg.length; i++) {
            int count = 0;
            a = new ArrayList();
            for (int j = 0; j < belongs.length; j++) {
                if(belongs[j] == (i+1)){
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
