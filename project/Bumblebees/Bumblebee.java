package project.Bumblebees;
import java.util.ArrayList;
import java.util.Random;

public class Bumblebee {

	private int life;
	private Data solution;
	public int n, m;//position de this
	public int food_n,food_m=0;//position de la bouffe si dispo
	public boolean to_home; // true si l'abeille rentre a la ruche

	private Objective obj;
	/* Constructor */
	/* the bumblebee has a simple solution at the begining */
	public Bumblebee(Objective obj, int  life,int n , int m ) {

		try {
			String msg = "Impossible to create bumblebee object: ";

			if (life <= 0) {
				throw new Exception(msg + " the lifespan is 0 or negative");
			}
			this.life = life;

			if (obj == null) {
				throw new Exception(msg + " the reference to the obj is null");
			}
			this.solution = obj.solutionSample();

			this.obj=obj;
			this.n=n;
			this.m=m;
			to_home=false;

		} catch (Exception e) {
			e.printStackTrace();
			System.exit(1);
		}
	}

	/*
	 * Constructor
	 * 
	 * @param solution : data, the solution that the Queen must communicate to the
	 * new bumblebee
	 */
	public Bumblebee(Data solution, int i, int j, int life,int Food_n,int Food_m) {

		try {
			String msg = "Impossible to create bumblebee object: ";

			if (life <= 0) {
				throw new Exception(msg + " the lifespan is 0 or negative");
			}
			this.life = life;

			if (solution == null) {
				throw new Exception("impossible to generate a new Bumblebee, the solution is null ");
			}

			this.solution = solution;
			to_home=false;
			if(!(Food_n <0 || Food_m<0 || Food_n>20 || Food_m>20)){
				//TODO changer >20 pour taille du monde
				this.food_m=Food_m;
				this.food_n=Food_n;
			}

		} catch (Exception e) {
			e.printStackTrace();
			System.exit(1);
		}
	}
	/* modifies the lifespan of bumblebee */
	public void setLife(int lifeSpan) {
		this.life = lifeSpan;
	}

	public Data getSolution() {
		return solution;
	}
	public int get_n(){return n;}
	public int get_m(){return m;}
	public int getLife(){
		return this.life;
	}

	public void setPosition(int n ,int m ) {this.n=n;this.m=m;}
	public void setFoodposition(int x, int y){this.food_n=x;this.food_m=y;}

	/**
	 * Moove a bee the way it should be done
	 */
	public void movebumblebee() {

		if (!to_home) {
			if (food_m >= 0 && food_n >= 0 && food_n <20 && food_m<20) {
				movetofood();
			} else {
				
				searchingForFood();
			}
		}
		/*else{
				moveToHome();
			}*/
		}

	/**
	 * try to find food mooves to a near position
	 */
	public void searchingForFood(){
	//TODO
		// determiner les bornes
		Random R = new Random();
		int x= R.nextInt(5)-3;
		int y=R.nextInt(5)-3;
		
		if(n+x >=0 && n+x< 20) {
			if(y+m >=0 && m+y< 20) {
				if (!Bumblebees.BeeMap[n+x][m+y]) {//case libre
					if(Bumblebees.artificial_word[n+x][m+y]) {
						//TODO nourriture trouver
						this.life+=2;
						//this.to_home=true;
						Bumblebees.reine.n=x+n;
						Bumblebees.reine.m=y+m;
						this.food_n=21;
						this.food_m=21;
						for(Food fleur :Bumblebees.fleurs) {
							if(fleur.get_n()==x+n && fleur.get_m()==y+m) {
								fleur.decreaselife();
								Bumblebees.ruche.addBee(this);
								
								//System.out.println("food find");
							}
						}
						
					}
					Bumblebees.BeeMap[n][m]=false;//maj coordonnée
					n=n+x;
					m=m+y;
					Bumblebees.BeeMap[n][m]=true;
					//System.out.println("moove");
				}
			}
		}
		
	}

	/**
	 * got a food position moove fast as he can to it
	 */
	public void movetofood(){
	//TODO
		if(Math.abs(n-food_n)<2&&Math.abs(m-food_m)<2){//si je suis a portée de la fleur
			
			boolean stillhere=false;
			for(Food fleur :Bumblebees.fleurs) {
				if(fleur.get_n()==food_n && fleur.get_m()==food_m) {
					fleur.decreaselife();
					stillhere=true;// la fleur existait toujours
					//System.out.println("food find");
				}
			}
			if(stillhere) {
				this.life+=2;
				Bumblebees.reine.n=food_n;
				Bumblebees.reine.n=food_m;
				Bumblebees.ruche.addBee(this);
				
			}
			else {food_n=21;food_m=21;}
			
		}
		else{// je ne suis pas a porté de la fleur 
			if(n!=food_n) {
				if(Math.abs(n-food_n)<2) {
					if(n<food_n) {
						n++;
					}
					else {n--;}
				}
				else {n=n+2;}
			}
			if(m!=food_m) {
				if(Math.abs(m-food_m)<2) {
					if(m<food_m) {
						m++;
					}
					else {m--;}
				}
				else {m=m+2;}
			}
			
			
			
		}// sinon on avance vite
	}

	/**
	 * reached a flower, getting home now with a small life bonus
	 */
	public void moveToHome(){
		
	}

	/**
	 * mutation de this.solution si possible
	 */
	public void mutate() {
		Random x = new Random();
		Data y = new Data(this.solution);
		Data d = y.randomSelectInNeighbour(1, 8);

		if (obj != null) {
			if (obj.value(this.solution) > obj.value(d)) {
				this.solution = new Data(d);
				if(1==x.nextInt(5)) {this.life++;}
				
			}
		}
	}

	/**
	 *  tue la bee si elle a une solution trop mauvaise
	 */
	public boolean reap(){
		if(obj!=null){
		if(obj.lastValue!=null && ( life==0)) {
			//System.out.println("reap\n");
			return true;}
		}
		return false;
		}

	/**
	 * enleve 1 pts de vie a this
	 */
	public void decreaseLife(){
		this.life=life-1;
	}



}
