package comportamientos;

import java.util.ArrayList;
import auxiliar.HomeMadeStruct;
import auxiliar.Utils;
import jade.core.behaviours.OneShotBehaviour;
import jade.lang.acl.ACLMessage;
import jade.lang.acl.MessageTemplate;

public class OneShotReunir extends  OneShotBehaviour {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private int NUM = 15;
	private int NUM_DEF=8;
	private int i = 0, j=0;
	private char OBRERA='O';
	private char RECOLECTOR='C';
	
	private ArrayList<HomeMadeStruct> aux;
	private ArrayList <ACLMessage> lista_msg;
	
	private HomeMadeStruct[] indices = Utils.indices();
	private HomeMadeStruct posIni;
	private HomeMadeStruct aux2=new HomeMadeStruct();
	
	private ACLMessage msg;
	private char[][] hive;
	
	
	
	public OneShotReunir(HomeMadeStruct posIni, char [][] colmena, ArrayList <ACLMessage> msgs) 
	{
		super();
		this.posIni=posIni;
		hive = colmena;
		lista_msg=msgs;
	}

	public void action()
	{
		try {
		Utils.enviarMensaje_todos(myAgent, "Reunir obrera", new HomeMadeStruct(-1,-1));
		while(lista_msg.size()<NUM)
		{
			msg = receiveMessage();
			lista_msg.add(msg);
		}
		
		aux = colocarPosObreraIni();
		for(ACLMessage m : lista_msg)
		{
			Utils.enviarMensaje_unico(myAgent, aux.get(j), m);
			j++;
		}	
		
		System.out.println("\t\t"+myAgent.getLocalName()+": OBRERAS DISPERSADAS VENID");
		Thread.sleep(2000);
		OneShotDibujarColmena.dibujarColmena(hive);	

		for(int x = 0; x<lista_msg.size(); x++, i++)
		{
			if(x<NUM_DEF)
			{
				System.out.println("\t\t"+myAgent.getLocalName()+": OBRERAS COLOCANDOSE A MI ALREDEDOR");
				Thread.sleep(2000);
				
				aux2.setIndex_x(posIni.getIndex_x()+indices[i].getIndex_x());
				aux2.setIndex_y(posIni.getIndex_y()+indices[i].getIndex_y());
				
				hive[aux2.getIndex_x()][aux2.getIndex_y()]=OBRERA;
				hive[aux.get(i).getIndex_x()][aux.get(i).getIndex_y()]=' ';
				
				Utils.enviarMensaje_unico(myAgent, aux2, lista_msg.get(x));
				OneShotDibujarColmena.dibujarColmena(hive);	
			}
			else
			{
				Utils.enviarMensaje_unico(myAgent, aux.get(x), lista_msg.get(x));
				hive[aux.get(i).getIndex_x()][aux.get(i).getIndex_y()]=RECOLECTOR;
			}
		}
		
		OneShotDibujarColmena.dibujarColmena(hive);	
		
		} catch (InterruptedException e) {
			System.err.println("Se ha interrumpido el sleep");
			e.printStackTrace();
		}
	}
	
	private  ArrayList<HomeMadeStruct> colocarPosObreraIni() {
		ArrayList<HomeMadeStruct> aux = new ArrayList<>();
		int x, y;
		for(int i = 0; i<NUM;i++)
		{
			do
			{
			    x = (int) Math.floor(Math.random() * 23 + 1);
			    y = (int) Math.floor(Math.random() * 23 + 1);
			}while(hive[x][y]!=' ');
			HomeMadeStruct punto = new HomeMadeStruct(x,y);
			hive[x][y]=OBRERA;
			aux.add(punto);
		}
		return aux;
		
	}
	
	private ACLMessage receiveMessage()
	{
		ACLMessage msg = this.myAgent.blockingReceive(MessageTemplate.and(MessageTemplate.MatchPerformative(ACLMessage.REQUEST), MessageTemplate.MatchOntology("ontologia")));
		return msg;
	};
	
}
