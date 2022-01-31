package project.Bumblebees;
import java.lang.reflect.Array;
import java.security.PublicKey;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.concurrent.Semaphore;

public class Bumblebees extends binMeta {
    public static ArrayList<Bumblebee> Bees;
    public   Ruche ruche;
    static public boolean[][] artificial_word;
    static public boolean[][] BeeMap;
    int current_generation = 0;
    int MaxGeneration;
    public static List<Food> fleurs;
    public static Reine reine;
    int MaxbeeOutside;
    int g;
    Data [] Solution;

 
    
    /**
     * @param artificial_word_n monde de hauteur n
     * @param artificial_word_m monde de hauteur m
     * @param Max               Maxgénératiion
     * @param nbfleurs          nb de fleur qu'aura notre monde
     * @param MaxbeeOutside     nb max de bee en dehors de la ruche
     * @param nbstartBee        nbr de bee au depart (toute dans la ruche)
     * @param obj               type de problème
     */
    public Bumblebees(int artificial_word_n, int artificial_word_m, int Max, int nbfleurs, int MaxbeeOutside, int nbstartBee, long maxTime, Objective obj) {
        try {
            String msg = "Impossible to create RandomWalk object: ";
            if (maxTime <= 0) throw new Exception(msg + "the maximum execution time is 0 or even negative");
            this.maxTime = maxTime;
            if (obj == null) throw new Exception(msg + "the reference to the objective is null");
            this.obj = obj;
            this.metaName = "BumbleBees";

            artificial_word = new boolean[artificial_word_n][artificial_word_m];
            BeeMap =new boolean[artificial_word_n][artificial_word_m];
            this.Bees = new ArrayList<>();
            this.MaxGeneration = Max;
            this.ruche = new Ruche(artificial_word_n/2, artificial_word_m/2, initBees(nbstartBee));
            artificial_word[artificial_word_n/2][artificial_word_m/2] = true;
            this.fleurs = initFleurs(nbfleurs);
            this.MaxbeeOutside = MaxbeeOutside;
            this.reine = initQueen();
            this.g = 40;
        } catch (Exception e) {
            e.printStackTrace();
            System.exit(1);
        }
    }
    
    /**
     * 
     * @param obj     type de probleme
     * @param maxTime temps maximum of execution
     */


    public Bumblebees(Objective obj, long maxTime) {
        try {
            String msg = "Impossible to create Bumblebees object: ";
            if (maxTime <= 0)
                throw new Exception(msg + "the maximum execution time is 0 or even negative");
            this.maxTime = maxTime;
            if (obj == null)
                throw new Exception(msg + "the reference to the objective is null");
            this.obj = obj;
            this.metaName = "Bumblebees";

 

            /**
             * les nombres qui sont ici sont ceux ui etaient donn�s dans le sujet 
             */
            artificial_word = new boolean[20][20]; // la taille du monde est 20 x 20
            BeeMap = new boolean[20][20]; // la taille de la mapBee doit �tre �gale � celle du monde
            this.g = 40;
            this.ruche = new Ruche(10, 10, initBees(200)); // la ruche est au milieu du monde et on commence � 200 bees
            this.fleurs = initFleurs(20);
            this.MaxbeeOutside = 200;
            this.MaxGeneration = 100000; // le nombre maximum de generation est 100 000
            artificial_word[10][10] = true ; //  pas de fleur a cet emplacement 
            this.Bees = new ArrayList<>();  // creer une une liste vide 
            this.reine = initQueen(); // initialiser la reine 

 

        } catch (Exception e) {
            e.printStackTrace();
            System.exit(1);
        }
    }

    @Override
    public void optimize() {
        //TO DO initialisation du monde(premieres solution de la reine)

        long Optimum = 0;

        Bumblebee Bestbee = ruche.bee_dans_ruche.get(0);
        long startime = System.currentTimeMillis();
        boolean find = false;
        while (current_generation < MaxGeneration && (System.currentTimeMillis() - startime < this.maxTime)&&!find) {

          // System.out.println("Génération : "+current_generation);
            if (obj.value(Bestbee.getSolution()) > Optimum) {// ON A PAS trouver l'optimum on continue d'optimize

                //look for the best BB ==> parcours de la list des bees en dehors et dans la ruche
                for (int i=0; i<Bees.size();i++){
                    if (obj.value(Bees.get(i).getSolution()) < obj.value(Bestbee.getSolution())){
                        Bestbee = Bees.get(i);}
                    Bees.get(i).decreaseLife();// decrease life of each bb;
                    if (Bees.get(i).reap()) {// reap bee if its solution sucks or life equal to 0
                       // System.out.println(Bees.size());
                           Bees.remove(Bees.get(i));
                       // System.out.println(Bees.size());
                        }
                    else {// sinon elle bouge, la façon dont la bee bouge est determiner par la son instance (si elle a une foodposition ou nn)
                            Bees.get(i).movebumblebee();
                        }
                    }
                for (int i=0; i<ruche.bee_dans_ruche.size();i++){
                    if (obj.value(ruche.bee_dans_ruche.get(i).getSolution()) < obj.value(Bestbee.getSolution())) {
                        Bestbee = ruche.bee_dans_ruche.get(i);
                    }
                    ruche.bee_dans_ruche.get(i).decreaseLife();     // decrease life of each bb;

                    if (ruche.bee_dans_ruche.get(i).reap()){
                    	
                      ruche.remove(ruche.bee_dans_ruche.get(i));
                        }
                }


                if (g == 0) {
                    ruche.bee_dans_ruche.add(this.reine.Createbee());
                    g = 15;// born mechanism
                } else {
                    g--;
                }


                if (!ruche.bee_dans_ruche.isEmpty() && (MaxbeeOutside > Bees.size())) {//un bee sort de la ruche si possible
                	ruche.bee_dans_ruche.get(0).food_n=reine.n;// on passe la derniere valeur de food connu
                	ruche.bee_dans_ruche.get(0).food_m=reine.m;
                	ruche.bee_dans_ruche.get(0).movebumblebee();
                    Bees.add(ruche.bee_dans_ruche.get(0));
                    ruche.bee_dans_ruche.remove(ruche.bee_dans_ruche.get(0));
                }


                for (Bumblebee bee : Bees) {
                    bee.mutate();// les bees dehors tentent une mutation
                   }
               for (Bumblebee bee : ruche.bee_dans_ruche) {
                    bee.mutate();// les bee de la ruche tentent une mutation
                }
                current_generation++;

                if (objValue > obj.value(Bestbee.getSolution())){
                    objValue = obj.value(Bestbee.getSolution());
                this.solution=Bestbee.getSolution();}// on range la meilleure solution dans la valeur de retour

               
                int lowervalueindice=0;
                for (int i = 1; i < 5; i++) {
                	if (obj.value(reine.solutions[i])>obj.value(reine.solutions[lowervalueindice]))
                		lowervalueindice=i;
                }
                if (objValue < obj.value(reine.solutions[lowervalueindice])) { //si la valeur de bestbee fait partie des 5 meilleurs valeur
                	reine.solutions[lowervalueindice]= this.solution;//on remplace la plus mauvaise valeur du record de solution par celle la.
                //if(Bees.size()+ruche.bee_dans_ruche.size()>50)
               //	System.out.println("low effectif");
                }
            }
            else {
            	find=true;
                System.out.println("optimum finds in : "+(System.currentTimeMillis() - startime)+"ms");
            }//on a trouvé l'optimum
        }
    }


    /**
     * @param nb nombre de fleur qui vont composer le monde
     * @return une liste de fleurs placer sur la map
     */

    public static List<Food> initFleurs(int nb) {
        List<Food> ret = new ArrayList<>();
        Random M = new Random();
        Random N = new Random();

        while (nb > 0) {
            int nn = N.nextInt(artificial_word.length);
            int mm = M.nextInt(artificial_word[0].length);

            if (!artificial_word[nn][mm]) { // on crée une fleur si la place est libre
                Food F = new Food(20, nn, mm);
                artificial_word[nn][mm] = true;
                ret.add(F);
                nb=nb-1;
            }
        }
        return ret;
    }

    /**
     * @param nb nombre de bee de départ
     * @return List de n Bumblebee
     */
    public List<Bumblebee> initBees(int nb) {
        List<Bumblebee> Bees = new ArrayList<>();
        while (nb > 0) {
            nb--;
            Bumblebee Bee = new Bumblebee(obj,400,artificial_word.length/2, artificial_word[0].length/2);
            Bees.add(Bee);
        }
        return Bees;
    }

    /**
     * init les premieres valeur de la reine pour éviter de comparer null et data lors des premiere iterations.
     */
    public Reine initQueen() {
        Reine reine = new Reine();
        for (int i = 0; i < 5; i++) {
            reine.solutions[i] = ruche.bee_dans_ruche.get(i).getSolution();
        }
        objValue=obj.value(reine.solutions[0]);// initialisation de objvalue a cause de exception optimize l109
        return reine;
    }


    public static void main(String[] args) {


        // BitCounter
        int n = 50;
        Objective obj = new BitCounter(n);
        Bumblebees rw = new Bumblebees(obj,10000);
        System.out.println(rw);
        System.out.println("optimizing ...");
        rw.optimize();
        System.out.println(rw);
        System.out.println("solution : " + rw.solution);
        System.out.println();

        // Fermat
        int exp = 2;
        int ndigits = 10;
        obj = new Fermat(exp, ndigits);

        rw =  new Bumblebees(obj,5000);
        System.out.println(rw);
        System.out.println("optimizing ...");
        rw.optimize();
        System.out.println(rw);
        System.out.println("solution : " +  rw.solution);
        Data x = new Data(rw.solution, 0, ndigits - 1);
        Data y = new Data(rw.solution, ndigits, 2 * ndigits - 1);
        Data z = new Data(rw.solution, 2 * ndigits, 3 * ndigits - 1);
        System.out.print(
                "equivalent to the equation : " + x.posLongValue() + "^" + exp + " + " + y.posLongValue() + "^" + exp);
        if (rw.objValue == 0.0)
            System.out.print(" == ");
        else
            System.out.print(" ?= ");
        System.out.println(z.posLongValue() + "^" + exp);
        System.out.println();

        // ColorPartition
        n = 4;
        int m = 14;
        ColorPartition cp = new ColorPartition(n, m);
        rw = new Bumblebees(20, 20, 100000, 40, 200, 200, 10000, cp);
        System.out.println(rw);
        System.out.println("optimizing ...");
        rw.optimize();
        System.out.println(rw);
        System.out.println("solution : " +  rw.solution);
        cp.value(rw.solution);
        System.out.println("corresponding to the matrix :\n" + cp.show());

    }
    
     

}



