package auxiliar;

import java.io.Serializable;
import java.util.ArrayList;

import jade.lang.acl.ACLMessage;

public class Auxiliar implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private char[][] colmena;
	private ArrayList <ACLMessage> lista_recolectoras;
	
	public Auxiliar(char [][] aux, ArrayList <ACLMessage> aux2)
	{
		colmena = aux;
		lista_recolectoras=aux2;
	}

	public char[][] getColmena() {
		return colmena;
	}

	public void setColmena(char[][] colmena) {
		this.colmena = colmena;
	}

	public ArrayList<ACLMessage> getLista_recolectoras() {
		return lista_recolectoras;
	}

	public void setLista_recolectoras(ArrayList<ACLMessage> lista_recolectoras) {
		this.lista_recolectoras = lista_recolectoras;
	}
}
