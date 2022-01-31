package project.Bumblebees;



import java.util.concurrent.Semaphore;

public class Agent2 extends Thread {
	Bumblebees B ;
	Data [] data;
	Semaphore[] sem;


	 public Agent2 (Bumblebees b,Semaphore[] sem,Data[] data) {
		 this.B=b;
		 this.data=data;
		 this.sem=sem;
	
	 }
	 
	 public void run() {
		//B.optimize();
	 }
	 
	 public void optimize() {
		 
	 }
}
