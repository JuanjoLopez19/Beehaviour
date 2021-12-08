package comportamientos;

import java.util.ArrayList;
import java.util.Random;

import auxiliar.HomeMadeStruct;
import auxiliar.Utils;
import jade.core.behaviours.OneShotBehaviour;
import jade.lang.acl.ACLMessage;
import jade.lang.acl.MessageTemplate;

public class OneShotReunir extends  OneShotBehaviour 
{
	private static final long serialVersionUID = 1L;
	
	private int NUM = 11;
	private int NUM_DEF=6;
	private int i = 0, j=0;
	private String OBRERA="O";
	private String RECOLECTOR="C";
	
	private ArrayList<HomeMadeStruct> aux;
	private ArrayList <ACLMessage> lista_msg;
	private ArrayList<ACLMessage> interfaz_msg;
	
	private HomeMadeStruct[] indices;
	private HomeMadeStruct posIni;
	private HomeMadeStruct aux2=new HomeMadeStruct();
	
	private ACLMessage msg;
	private char[][] hive;
	
	
	
	public OneShotReunir(HomeMadeStruct posIni, char [][] colmena, ArrayList <ACLMessage> msgs, ArrayList <ACLMessage> im ) 
	{
		super();
		this.posIni=posIni;
		hive = colmena;
		lista_msg=msgs;
		interfaz_msg=im;
		
		if(posIni.getIndex_x()%2==0) {
			indices = Utils.indices_par();
		} else {
			indices = Utils.indices_impar();
		}
		
	}

	public void action()
	{
		try {
			
			Utils.enviarMensaje_todos(myAgent, "Reunir interfaz", null);
			
			msg = receiveMessage();
			interfaz_msg.add(msg);
			
			Utils.enviarMensaje_unico(myAgent, posIni,interfaz_msg.get(0));
			
			Utils.enviarMensaje_todos(myAgent, "Reunir obrera", interfaz_msg.get(0));
			while(lista_msg.size()<NUM)
			{
				msg = receiveMessage();
				lista_msg.add(msg);
			}
			
			aux = colocarPosObreraIni(posIni);
			for(ACLMessage m : lista_msg)
			{
				Utils.enviarMensaje_unico(myAgent, aux.get(j), m);
				j++;
			}	
			
			System.out.println(myAgent.getLocalName()+": OBRERAS DISPERSADAS VENID");
			Thread.sleep(2000);
	
			System.out.println(myAgent.getLocalName()+": OBRERAS COLOCANDOSE A MI ALREDEDOR");
			for(int x = 0; x<lista_msg.size(); x++, i++)
			{
				if(x<NUM_DEF)
				{
					Thread.sleep(2000);
					
					aux2.setIndex_x(posIni.getIndex_x()+indices[i].getIndex_x());
					aux2.setIndex_y(posIni.getIndex_y()+indices[i].getIndex_y());
					
					Utils.enviarMensaje_unico(myAgent, new HomeMadeStruct(-aux.get(i).getIndex_x(),aux.get(i).getIndex_y()),interfaz_msg.get(0));
					Utils.enviarMensaje_unico(myAgent, new HomeMadeStruct(aux2.getIndex_x(),aux2.getIndex_y(),"O"),interfaz_msg.get(0));
					
					Utils.enviarMensaje_unico(myAgent, aux2, lista_msg.get(x));
				}
				else
				{
					Utils.enviarMensaje_unico(myAgent, aux.get(x), lista_msg.get(x));
					
					Utils.enviarMensaje_unico(myAgent, new HomeMadeStruct(aux.get(x).getIndex_x(),aux.get(x).getIndex_y(),RECOLECTOR),interfaz_msg.get(0));
				}
			}		
		} catch (InterruptedException e) {
			System.err.println("Se ha interrumpido el sleep");
			e.printStackTrace();
		}
	}
	
	private  ArrayList<HomeMadeStruct> colocarPosObreraIni(HomeMadeStruct posIni) {
		ArrayList<HomeMadeStruct> aux = new ArrayList<>();
		int x, y;
		Random random = new Random();
		boolean repetido;
		for(int i = 0; i<NUM;i++)
		{
			repetido = false;
			do {
	            x = random.nextInt(13) + 2;
	            y = random.nextInt(14) + 1;
	            
	            if(posIni.getIndex_x()%2==0) {
	            	for(HomeMadeStruct p : Utils.indices_par()) {
	            		if( x == p.getIndex_x() && y == p.getIndex_y()) {
	            			repetido = true;
	            			break;
	            		}
	            	}	
	            } else {
	            	for(HomeMadeStruct p : Utils.indices_impar()) {
	            		if( x == p.getIndex_x() && y == p.getIndex_y()) {
	            			repetido = true;
	            			break;
	            		}
	            	}	
	            }
	            
	        } while(y==1 && x%2==0 || hive[x][y]!=' ' || repetido);
			HomeMadeStruct punto = new HomeMadeStruct(x,y,OBRERA);
			Utils.enviarMensaje_unico(myAgent,punto,interfaz_msg.get(0));
			hive[x][y]=OBRERA.charAt(0);
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
