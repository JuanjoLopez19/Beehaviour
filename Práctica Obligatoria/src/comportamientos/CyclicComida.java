package comportamientos;

import java.util.ArrayList;

import auxiliar.Auxiliar;
import auxiliar.HomeMadeStruct;
import auxiliar.Utils;
import jade.core.behaviours.CyclicBehaviour;
import jade.lang.acl.ACLMessage;
import jade.lang.acl.MessageTemplate;
import jade.lang.acl.UnreadableException;

public class CyclicComida extends CyclicBehaviour {

	/**
	 * 
	 */
	private ArrayList <ACLMessage> lista_defensoras;
	private ArrayList <ACLMessage> lista_recolectoras;
	private ArrayList <ACLMessage> msg_list;
	private ACLMessage msg;
	private HomeMadeStruct pos_Reina, pos_aux;
	private Boolean flag = true;
	private int NUM = 15;
	private int NUM_DEF=5;
	private int rand_num;
	private char[][] hive;
	private Auxiliar aux;
	private static final long serialVersionUID = 1L;
	public CyclicComida(ArrayList <ACLMessage> msg, HomeMadeStruct posReina, char [][] colmena)
	{
		super();
		this.msg_list=msg;
		pos_Reina = posReina;
		hive = colmena;
	}
	


	@Override
	public void action() {
		if(flag && msg_list.size() != 0)
		{
			lista_defensoras=DefenderList(msg_list);
			lista_recolectoras=RecolectorList(msg_list);
			flag=false;
		}
		System.out.println("Tengo hambre voy a mandar a alguna defensora que me consiga comida");
		aux = new Auxiliar(hive, lista_recolectoras);
		rand_num = (int) Math.floor(Math.random() * lista_defensoras.size());
		System.out.println("Le he enviado el mensaje a " + lista_defensoras.get(rand_num).getSender().getLocalName());
		Utils.enviarMensaje_unico(myAgent, aux, lista_defensoras.get(rand_num));
		msg = receiveMessage();
		try {
			if(msg.getContentObject() == null)
			{
				System.out.println("La recolectora " + msg.getSender().getLocalName() + " me ha venido a alimentar");
				//msg = receiveMessage();
				
			}
			
			
		} catch (UnreadableException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		System.out.println("Estoy Esperando la respuesta de la obrera");
		msg = receiveMessage();
		block();
		try {
			Thread.sleep(10000);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		// Recibe la colmena cambiada
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
	
	private ACLMessage receiveMessage()
	{
		ACLMessage msg = this.myAgent.blockingReceive(MessageTemplate.and(MessageTemplate.MatchPerformative(ACLMessage.REQUEST), MessageTemplate.MatchOntology("ontologia")));
		return msg;
	};
}
