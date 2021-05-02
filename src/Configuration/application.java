package Configuration;

import java.util.ArrayList;

public class application {

	public static void main(String[] args) {
		/*-----------------------------------------------------------------------*/
		// Rayon de Communication et de Couverture 
   		int RayonComm=1,RayonSens=1;
		// crée une region pour la station HECN
		Region HECN=new Region(5,5);
		HECN.setOn(true);
		// cree une liste des Regions de la zone 
		ArrayList<Region> Regions=new ArrayList<>();
		for (int i=0;i<=10;i++) {
			for (int j=0;j<=10;j++) {
				Regions.add(new Region(i,j));
			}
		}
		// set Regions 
		Regions.remove(HECN);
		// Cree une Zone x=30 et y=30
				zoneWSN zone=new zoneWSN(10,10,RayonComm,RayonSens,HECN,Regions);
		/*-----------------------------------------------------------------------*/
 /*
Region r1=new Region(1,0); Regions.get(Regions.indexOf(r1)).BasculeStatut();
 Region r2=new Region(3,0); Regions.get(Regions.indexOf(r2)).BasculeStatut();
 Region r3=new Region(7,0); Regions.get(Regions.indexOf(r3)).BasculeStatut();
 Region r4=new Region(9,0); Regions.get(Regions.indexOf(r4)).BasculeStatut();
 Region r5=new Region(1,1); Regions.get(Regions.indexOf(r5)).BasculeStatut();
 Region r6=new Region(5,1); Regions.get(Regions.indexOf(r6)).BasculeStatut();
 Region r7=new Region(10,1); Regions.get(Regions.indexOf(r7)).BasculeStatut();
 Region r8=new Region(1,2); Regions.get(Regions.indexOf(r8)).BasculeStatut();
 Region r9=new Region(6,2); Regions.get(Regions.indexOf(r9)).BasculeStatut();
 Region r10=new Region(8,2); Regions.get(Regions.indexOf(r10)).BasculeStatut();
 Region r11=new Region(1,3); Regions.get(Regions.indexOf(r11)).BasculeStatut();
 Region r12=new Region(3,3); Regions.get(Regions.indexOf(r12)).BasculeStatut();
 Region r13=new Region(6,3); Regions.get(Regions.indexOf(r13)).BasculeStatut();
 Region r14=new Region(10,3); Regions.get(Regions.indexOf(r14)).BasculeStatut();
 Region r15=new Region(6,4); Regions.get(Regions.indexOf(r15)).BasculeStatut();
 Region r16=new Region(2,5); Regions.get(Regions.indexOf(r16)).BasculeStatut();
 Region r17=new Region(4,5); Regions.get(Regions.indexOf(r17)).BasculeStatut();
 Region r18=new Region(8,5); Regions.get(Regions.indexOf(r18)).BasculeStatut();
 Region r19=new Region(0,6); Regions.get(Regions.indexOf(r19)).BasculeStatut();
 Region r20=new Region(8,6); Regions.get(Regions.indexOf(r20)).BasculeStatut();
 Region r21=new Region(10,6); Regions.get(Regions.indexOf(r21)).BasculeStatut();
 Region r22=new Region(3,7); Regions.get(Regions.indexOf(r22)).BasculeStatut();
 Region r23=new Region(6,7); Regions.get(Regions.indexOf(r23)).BasculeStatut();
 Region r24=new Region(0,8); Regions.get(Regions.indexOf(r24)).BasculeStatut();
 Region r25=new Region(3,8); Regions.get(Regions.indexOf(r25)).BasculeStatut();
 Region r26=new Region(9,8); Regions.get(Regions.indexOf(r26)).BasculeStatut();
 Region r27=new Region(5,9); Regions.get(Regions.indexOf(r27)).BasculeStatut();
 Region r28=new Region(6,9); Regions.get(Regions.indexOf(r28)).BasculeStatut();
 Region r29=new Region(9,9); Regions.get(Regions.indexOf(r29)).BasculeStatut();
 Region r30=new Region(1,10); Regions.get(Regions.indexOf(r30)).BasculeStatut();
 Region r31=new Region(2,10); Regions.get(Regions.indexOf(r31)).BasculeStatut();
 Region r32=new Region(7,10); Regions.get(Regions.indexOf(r32)).BasculeStatut();
 
      Solution s=new Solution(zone,Regions);
      System.out.println(s.isValide());
 
		/*-----------------------------Recuit S imulé-----------------------------*/
				        // Nombre Itération Nb
		     	int Nb=10000;
				// Température  Initiale et alpha=0.98 et beta=10
				double T=758.0667881255503;
				// Solution Initial 
				Solution sol=new Solution (zone);
				sol.Affichage();
				// crée Meta Esclave 
				MetaEsclave recuitS=new recuitSimuleM(sol, T, Nb,0.947559409532568,0.3304471675714796,TypeRecuitSimule.Reduction_par_palier);
				long tempsDebut = System.currentTimeMillis(); 
				recuitS.function();
				long tempsFin = System.currentTimeMillis();
				Solution resultat=recuitS.getMeilleurSOl();
				System.out.print("\n\n RESULTAT -> "); resultat.Affichage();
				float sec = (tempsFin - tempsDebut) / 1000F; 
				float minutes=sec/60F;
				System.out.println("\n\n\t\t\t\t Temps D'execution : "+sec+"s "+minutes+" min");
		        /*-----------------------------------------------------------------------*/
				/*---------------------------Genetic -------------------------*/		
	         	// Nombre de Solution 
	       /*	int nb=15000;  
				// crée MetaEsclave genetic Population ou Incremental 
				MetaEsclave Genetic1=new geneticM(nb ,8, 0.6510168053183788,zone);
				// executé Meta :
				long tempsDebut = System.currentTimeMillis(); 
				Genetic1.function();
				long tempsFin = System.currentTimeMillis(); 
				System.out.print("\n\n RESULTAT -> ");
				Genetic1.getMeilleurSOl().Affichage();
				System.out.println(((geneticM)Genetic1).getType().toString());
				float sec = (tempsFin - tempsDebut) / 1000F; 
				float minutes=sec/60F;
				System.out.println("\n\n\t\t\t\t Temps D'execution : "+sec+"s "+minutes+" min");
				
				/*----------------------------------------------------------------------*/
	        	/*--------------------------Biogeography-based optimization------------------------*/		
		/*		// Nombre de Solution 
	          	int n=15000;
				// crée MetaEsclave BBO
				MetaEsclave Meta=new BBoM(33,0.8321032808339923,0.12786789651225205,zone,n,0.8005219539133305);
				// executé Meta :
				long tempsDebut = System.currentTimeMillis(); 
				Meta.function();
				long tempsFin = System.currentTimeMillis(); 
				System.out.print("\n\n RESULTAT -> ");
				Meta.getMeilleurSOl().Affichage();
				float sec = (tempsFin - tempsDebut) / 1000F; 
				float minutes=sec/60F;
				System.out.println("\n\n\t\t\t\t Temps D'execution : "+sec+"s "+minutes+" min");
				/*----------------------------------------------------------------------*/
		        /*--------------------------Reglage Paramétres Biogeography-based optimization------------------------*/		
		/*		ReglageParametres param=new ReglageParametres(zone);
		        long tempsDebut = System.currentTimeMillis(); 
		        param.deepLocalSearchBBO();
		        long tempsFin = System.currentTimeMillis(); 
		        float sec = (tempsFin - tempsDebut) / 1000F; 
				float minutes=sec/60F;
				System.out.println("\n\n\t\t\t\t Temps D'execution : "+sec+"s "+minutes+" min");
		        /*----------------------------------------------------------------------*/
                /*--------------------------Reglage Paramétres GENETIC------------------------*/		
		/*		ReglageParametres param=new ReglageParametres(zone);
		        long tempsDebut = System.currentTimeMillis(); 
		        param.deepLocalSearchGenetic();
		        long tempsFin = System.currentTimeMillis(); 
		        float sec = (tempsFin - tempsDebut) / 1000F; 
				float minutes=sec/60F;
				System.out.println("\n\n\t\t\t\t Temps D'execution : "+sec+"s "+minutes+" min");
		        /*----------------------------------------------------------------------*/
				/*--------------------------Reglage Paramétres Recuit Simulé------------------------*/		
	    /*        ReglageParametres param=new ReglageParametres(zone);
		        long tempsDebut = System.currentTimeMillis(); 
		        param.PreparerInitSolution();
		        param.deepLocalSearchRecuitSimule(TypeRecuitSimule.Reduction_par_palier);
		        long tempsFin = System.currentTimeMillis(); 
		        float sec = (tempsFin - tempsDebut) / 1000F; 
				float minutes=sec/60F;
				System.out.println("\n\n\t\t\t\t Temps D'execution : "+sec+"s "+minutes+" min");
				/*----------------------------------------------------------------------*/
		        /*--------------------------TESTE GENERALE ------------------------*/	
	     /*        recuitSimuleP rsp=new recuitSimuleP(756.04738726525,0.983543150464383,0.12336204702940838);
		         geneticP agp=new geneticP(8,0.6510168053183788);
		         BBoP bbop=new BBoP(30,0.8321032808339923,0.12786789651225205,0.8005219539133305);
	             ReglageParametres param=new ReglageParametres(zone,rsp,agp,bbop);   
	             MetaMaitre Maitre=new MetaMaitre(zone,param,15000);
	             long tempsDebut = System.currentTimeMillis(); 
	             Maitre.processus();
	             long tempsFin = System.currentTimeMillis(); 
			     float sec = (tempsFin - tempsDebut) / 1000F; 
			     float minutes=sec/60F;
			     System.out.println("\n\n\n \t RESULTAT FINAL : "); Maitre.getFinalSOl().Affichage();
				 System.out.println("\n\n\t\t\t\t Temps D'execution : "+sec+"s "+minutes+" min");
		        /*----------------------------------------------------------------------*/
	}
	
}
