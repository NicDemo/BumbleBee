package project.Bumblebees;
import java.util.Random;

public class Reine {
    Data[] solutions;
    int n;
    int m;
    public Reine (){
        solutions = new Data[5];
        n=-1;
        m=-1;
    }
    public Bumblebee Createbee(){
        int r = new Random().nextInt(5);
        Bumblebee bee = new Bumblebee(solutions[r],Bumblebees.artificial_word.length/2,
                Bumblebees.artificial_word[0].length/2,100,n,m);// création de bee data = une de meilleur valeur n et m indice d'une food position TODO changer indice
        return bee;
    }
    public int get_n (){
        return  n;
    }
    public int get_m(){
        return m;
    }
    public void set_food_position(int n, int m ){
        this.n=n;
        this.m=m;
    }
}
