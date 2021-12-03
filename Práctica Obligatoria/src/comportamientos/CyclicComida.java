package comportamientos;

import java.util.ArrayList;

import auxiliar.HomeMadeStruct;
import auxiliar.Utils;
import jade.core.behaviours.CyclicBehaviour;
import jade.lang.acl.ACLMessage;

public class CyclicComida extends CyclicBehaviour {

	/**
	 * 
	 */
	private ArrayList <ACLMessage> lista_defensoras;
	private ArrayList <ACLMessage> lista_recolectoras;
	private ArrayList <ACLMessage> msg_list;
	private HomeMadeStruct pos_Reina;
	private Boolean flag = true;
	private int NUM = 15;
	private int NUM_DEF=5;
	private int rand_num;
	private static final long serialVersionUID = 1L;
	public CyclicComida(ArrayList <ACLMessage> msg, HomeMadeStruct posReina)
	{
		super();
		this.msg_list=msg;
		pos_Reina = posReina;
		
	}
	


	@Override
	public void action() {
		if(flag && msg_list.size() != 0)
		{
			lista_defensoras=DefenderList(msg_list);
			lista_recolectoras=RecolectorList(msg_list);
			flag=false;
		}
		try {
			Thread.sleep(5000);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		rand_num = (int) Math.floor(Math.random() * lista_defensoras.size());
		Utils.enviarMensaje_unico(myAgent, lista_recolectoras, lista_defensoras.get(rand_num));
		
		//System.out.println("Pues ya he llegado");
	}
	
	private ArrayList <ACLMessage> DefenderList(ArrayList <ACLMessage> lista)
	{
		ArrayList <ACLMessage> aux = new ArrayList<>();
		for (int i = 0; i< NUM_DEF; i++)
			aux.add(lista.get(i));
		return aux;
	}
	
	private ArrayList <ACLMessage> RecolectorList(ArrayList <ACLMessage> lista)
	{
		ArrayList <ACLMessage> aux = new ArrayList<>();
		for (int i = NUM_DEF; i< lista.size(); i++)
			aux.add(lista.get(i));
		return aux;
	}
}
