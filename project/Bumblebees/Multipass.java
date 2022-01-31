package project.Bumblebees;

import java.util.concurrent.Semaphore;

public class Multipass  {
	long maxtime;
	Objective obj;
	public Multipass(Objective obj,long maxtime){
		this.obj=obj;
		this.maxtime=maxtime;	
	}
	public void optimize() {
		Objective obj1 = obj.clone();
		Objective obj2 = obj.clone();
		Objective obj3 = obj.clone();
		Bumblebees B = new Bumblebees(obj3,maxtime);
		Bumblebees B1 = new Bumblebees(obj1,maxtime);
		Bumblebees B2 = new Bumblebees(obj2,maxtime);
		Data [] data = new Data[10];// tableaux des meilleures valeurs qui se partagent les threads
		Semaphore[] sem= new Semaphore[10];
		for (int i = 0;i<sem.length;i++) {
			sem[i]=new Semaphore(1);
			data[i]= obj.solutionSample();
		}
		// on crée 3 mondes identiques qui vont s"executer en parallele tout en se partageant les meilleures solutions.
		// 
		
		Agent1 a2 = new Agent1 (B1,sem,data);	
		Agent1 a1 = new Agent1(B,sem,data);
		Agent1 a3 = new Agent1(B2,sem,data);
		a1.start();
		a2.start();
		a3.start();
		long startime = System.currentTimeMillis();
		
	while(a1.isAlive()||a2.isAlive()||a3.isAlive()) {}
		if(B.objValue>B1.objValue && B.objValue>B2.objValue) {
			System.out.println("Solution : "+B.solution+" valeur "+B.objValue+ " optimum finds in : "+(System.currentTimeMillis() - startime)+"ms");
		}
		else if((B1.objValue>B.objValue && B1.objValue>B2.objValue)) {
			System.out.println("Solution : "+B1.solution+" valeur "+B1.objValue+ " optimum finds in : "+(System.currentTimeMillis() - startime)+"ms");
		}
		else {System.out.println("Solution : "+B2.solution+" valeur "+B2.objValue+ " optimum finds in : "+(System.currentTimeMillis() - startime)+"ms");}				
	}		 
		
	public static void main (String[] args)  {
		Objective obj = new Fermat(6, 10);
		Multipass m = new Multipass(obj,10000);
		m.optimize();
	}
}
