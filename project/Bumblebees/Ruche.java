package project.Bumblebees;
import java.util.ArrayList;
import java.util.List;

public class Ruche {
    int n;
    int m;//position de la ruche
    public List<Bumblebee> bee_dans_ruche;
    public Ruche (int n,int m, List<Bumblebee> bee_dans_ruche){
    this.n=n;
    this.m=m;
    this.bee_dans_ruche=bee_dans_ruche;
    }
    public void addBee(Bumblebee bumblebee){
    	bumblebee.n=n;
    	bumblebee.m=m;
        bee_dans_ruche.add(bumblebee);//bee enter in the ruche
        Bumblebees.BeeMap[bumblebee.n][bumblebee.m]=false;// free his last position
        Bumblebees.Bees.remove(bumblebee);
    }
    public  List<Bumblebee> getBee_dans_ruche(){
        return bee_dans_ruche;
}
public boolean remove(Bumblebee bee){
        return bee_dans_ruche.remove(bee);
}


}
