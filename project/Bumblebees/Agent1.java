package project.Bumblebees;

import java.util.Random;
import java.util.concurrent.Semaphore;

import javax.swing.text.DefaultEditorKit.BeepAction;

public class Agent1 extends Thread {
	Bumblebees B ;
	Data [] data;
	Semaphore[] sem;
	int generebee=20;
	/**
	 * 
	 * @param b Monde b
	 * @param sem tableau de semaphore pour gérer les acces concurentiel au tableau de data
	 * @param data
	 */
	 public Agent1 (Bumblebees b,Semaphore[] sem,Data[] data) {
		 this.B=b;
		 this.data=data;
		 this.sem=sem;

	
	 }
	 
	 public void run() {
		 boolean find = false;
		 long startime = System.currentTimeMillis();
		 Bumblebee Bestbee = B.ruche.bee_dans_ruche.get(0);
		 double Optimum = 0;
		 B.objValue=B.obj.value(B.ruche.bee_dans_ruche.get(0).getSolution()) ;
		 while (System.currentTimeMillis() - startime < B.maxTime  &&!find) {
			 if (B.obj.value(Bestbee.getSolution()) != Optimum) {
		 
				 
				 // on recherche la bee avec la meilleur solution 
				 for (int i=1; i<B.ruche.bee_dans_ruche.size();i++){
					 if (B.obj.value(B.ruche.bee_dans_ruche.get(i).getSolution()) < B.obj.value(Bestbee.getSolution())){
						 Bestbee = B.ruche.bee_dans_ruche.get(i);
						 B.ruche.bee_dans_ruche.get(i).decreaseLife();
						 if(B.ruche.bee_dans_ruche.get(i).reap()) {
							 B.ruche.bee_dans_ruche.remove(B.ruche.bee_dans_ruche.get(i));
						 }
					}
				 }
		 
					 for (Bumblebee bee : B.Bees) {
						 bee.mutate();// les bees dehors tentent une mutation
					 }
					 for (Bumblebee bee : B.ruche.bee_dans_ruche) {
						 bee.mutate();// les bee de la ruche tentent une mutation
					 }
         

					 if (B.objValue > B.obj.value(Bestbee.getSolution())){
						 B.objValue = B.obj.value(Bestbee.getSolution());
						 B.solution=Bestbee.getSolution();
					 }
					 for (int i = 0; i<sem.length;i++) {
						 try {
							sem[i].acquire();
							if(B.obj.value(Bestbee.getSolution())<B.obj.value(data[i])) 
								 data[i]=Bestbee.getSolution();
							 sem[i].release();
							 } catch (InterruptedException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
						
					 }
					 // on crée une bee toute les 5 générations 
					 if(generebee==0) {
					 createBee();
					 generebee=5;}
					 generebee--;
						
			 }
					 

			 
			 else {
				find=true;
			 //	System.out.println("Solution : "+B.solution+" valeur "+B.objValue+ " optimum finds in : "+(System.currentTimeMillis() - startime)+"ms");
				}
		 }
		 if(!find) {
			// System.out.println("Solution : "+B.objValue);
		 }
	 }
	 
	 // Crée et Ajoute une bee dans la ruche sa solution fais partie des meilleurs solutions des 3 mondes.
	 public void createBee() {
		 Random R = new Random();
		 int x = R.nextInt(9);
		
			 try {
				sem[x].acquire();
				 Data solution = data[x];
				 sem[x].release();
				 Bumblebee bee = new Bumblebee(solution,0,0,100,0,0);
				 B.ruche.bee_dans_ruche.add(bee);	
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
					 
	 }
	 
}
