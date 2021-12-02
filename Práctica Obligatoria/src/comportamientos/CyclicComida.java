package comportamientos;

import java.util.ArrayList;

import auxiliar.HomeMadeStruct;
import jade.core.behaviours.CyclicBehaviour;
import jade.lang.acl.ACLMessage;

public class CyclicComida extends CyclicBehaviour {

	/**
	 * 
	 */
	private ArrayList <ACLMessage> lista_msg;
	private HomeMadeStruct pos_Reina;
	int NUM = 15;
	int NUM_DEF=8;
	private static final long serialVersionUID = 1L;
	public CyclicComida(ArrayList <ACLMessage> msg, HomeMadeStruct posReina)
	{
		super();
		lista_msg=msg;
		pos_Reina = posReina;
	}
	
	
	@Override
	public void action() {
		

	}

}
