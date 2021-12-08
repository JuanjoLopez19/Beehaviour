package comportamientos;

import auxiliar.Utils;
import jade.core.behaviours.OneShotBehaviour;

public class OneShotDibujarColmena extends OneShotBehaviour {
	private static final long serialVersionUID = 1L;
	char[][] hive;
	public OneShotDibujarColmena(char[][] aux)
	{
		super();
		hive=aux;
	}
	public OneShotDibujarColmena()
	{
		
	}
	
	public void action()
	{
		dibujarColmena(hive);
	}
	
	public static void dibujarColmena(char[][] colmena) {
		Utils.fakeClear();
		System.out.print("\t");
		for(int i = 0; i< colmena.length; i++)
			System.out.print("_");
		System.out.println();
		System.out.print("\t");
		for (int i = 0; i< colmena.length;i++){
			System.out.print("|");
			for (int j = 0; j<colmena[0].length; j++){
				System.out.print(" "+colmena[i][j] +" |");
			}
			System.out.println("");
			System.out.print("\t");
		}
		for(int i = 0; i< colmena.length; i++)
			System.out.print("¯");
		System.out.println();
	}
}
