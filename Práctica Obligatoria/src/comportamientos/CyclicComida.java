package comportamientos;

import java.util.ArrayList;

import auxiliar.Utils;
import jade.core.behaviours.CyclicBehaviour;
import jade.lang.acl.ACLMessage;
import jade.lang.acl.MessageTemplate;
import jade.lang.acl.UnreadableException;

public class CyclicComida extends CyclicBehaviour {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private int NUM_DEF=6;
	private int rand_num, salida;
	
	private ArrayList <ACLMessage> lista_defensoras;
	private ArrayList <ACLMessage> lista_recolectoras;
	private ArrayList <ACLMessage> msg_list;
	
	private ACLMessage msg;
	private Boolean flag = true;
	
	public CyclicComida(ArrayList <ACLMessage> msg)
	{
		super();
		this.msg_list=msg;
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
				salida = (int) Math.floor(Math.random() * 100 +1);
				if(salida >=90 && salida<=95)
				{					
					Thread.sleep(2000);
					
					Utils.enviarMensaje_todos(myAgent, "Finalizar Obrera", "Se acabo");
					myAgent.doDelete();
				}
				else
				{
					System.out.println(myAgent.getLocalName()+": tengo hambre voy a mandar a alguna defensora que me consiga comida");
					
					rand_num = (int) Math.floor(Math.random() * lista_defensoras.size());
					System.out.println(myAgent.getLocalName()+": le he enviado el mensaje a " + lista_defensoras.get(rand_num).getSender().getLocalName());
					
					Utils.enviarMensaje_unico(myAgent, lista_recolectoras, lista_defensoras.get(rand_num));
					msg = receiveMessage();
					
					while(msg.getContentObject() != null)
					{
						msg = receiveMessage();
					}
					System.out.println(myAgent.getLocalName()+": la recolectora " + msg.getSender().getLocalName() + " me ha venido a alimentar");
					
					msg = receiveMessage();
					while(msg.getContentObject() != null)
					{
						msg = receiveMessage();
					}
					
					System.out.println(myAgent.getLocalName()+": la defensora " + msg.getSender().getLocalName() + " ya ha vuelto a su posición, puedo descansar tranquila");
					
					Thread.sleep(10000);
					
					Utils.fakeClear();
					Utils.fakeClear();
				}
		} catch (InterruptedException e) {
			System.err.println("Se interrumpio el sleep");
			e.printStackTrace();
		} catch (UnreadableException e){
			System.err.println("No se ha podido leer el contenido del mensaje");
			e.printStackTrace();
		}
		
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
