package project.Bumblebees;
import java.util.List;
import java.util.Random;

public class Food {

	private int lifeSpan;
	private int n, m;
	private Random R = new Random();

	public Food(int life, int n, int m) {

		try {
			String msg = "impossible to create  a food object: ";

			if (life <= 0) {
				throw new Exception(msg + " the lifespan is 0 or negative");
			}
			this.lifeSpan = life;

			if (n < 0 || m < 0) {
				throw new Exception(msg + " the position is null  negative ");
			}
			this.n=n;
			this.m=m;

		} catch (Exception e) {
			e.printStackTrace();
			System.exit(1);
		}
	}

	public int get_n() {
		return n;
	}
	public int get_m(){
		return m;
	}

	public void setPosition(int n , int m ) {
		this.n =n;
		this.m=m;
	}
	public void decreaselife() {
		this.lifeSpan--;
		if(this.lifeSpan==0)
			this.phoenix();
		
	}

	public void phoenix(){// when lifespan reach 0 the flower die and reborn somewhere else in the world as an other new flower.
			Random x = new Random();
			Random y =new Random();
			int n = x.nextInt(Bumblebees.artificial_word.length);
			int m = x.nextInt(Bumblebees.artificial_word[0].length);
			while (!Bumblebees.artificial_word[n][m]){
				n=x.nextInt(Bumblebees.artificial_word.length);
				m = x.nextInt(Bumblebees.artificial_word[0].length);
			}// TO DO changer les coordinates par des coordonées valid
			this.n=n;
			this.m=m;
			lifeSpan=20;
			//System.out.println("phoenix");
		
	}



}


