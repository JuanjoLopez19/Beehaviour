package comportamientos;

import java.util.ArrayList;
import java.util.Scanner;

import auxiliar.HomeMadeStruct;
import auxiliar.Utils;
import jade.core.behaviours.OneShotBehaviour;
import jade.lang.acl.ACLMessage;
import jade.lang.acl.MessageTemplate;
import jade.lang.acl.UnreadableException;

public class OneShotReunir extends  OneShotBehaviour {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	ArrayList<HomeMadeStruct> aux;
	HomeMadeStruct[] indices = indices();
	HomeMadeStruct posIni, aux2=new HomeMadeStruct();
	char[][] hive;
	ACLMessage msg;
	int NUM = 8; 
	int i = 0, j=0;
	ArrayList <ACLMessage> lista_msg= new ArrayList<>();
	Scanner sc = new Scanner(System.in);
	OneShotDibujarColmena printer = new OneShotDibujarColmena();
	public OneShotReunir(ArrayList<HomeMadeStruct> indices, HomeMadeStruct posIni, char [][] colmena) {
		super();
		aux=indices;
		this.posIni=posIni;
		hive = colmena;
	}

	public void action()
	{
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
		
		printer.dibujarColmena(hive);

		for(ACLMessage m : lista_msg)
		{
			try {
				Thread.sleep(1000);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			aux2.setIndex_x(posIni.getIndex_x()+indices[i].getIndex_x());
			aux2.setIndex_y(posIni.getIndex_y()+indices[i].getIndex_y());
			hive[aux2.getIndex_x()][aux2.getIndex_y()]='O';
			hive[aux.get(i).getIndex_x()][aux.get(i).getIndex_y()]=' ';
			Utils.enviarMensaje_unico(myAgent, aux2, m);
			printer.dibujarColmena(hive);	
			i++;
		}		
	}
	
	private  ArrayList<HomeMadeStruct> colocarPosObreraIni() {
		ArrayList<HomeMadeStruct> aux = new ArrayList<>();
		int x, y;
		for(int i = 0; i<NUM;i++)
		{
			do{
			    x = (int) Math.floor(Math.random() * 23 + 1);
			    y = (int) Math.floor(Math.random() * 23 + 1);
			}while(hive[x][y]!=' ');
			HomeMadeStruct punto = new HomeMadeStruct(x,y);
			hive[x][y]='O';
			aux.add(punto);
		}
		return aux;
		
	}
	
	private ACLMessage receiveMessage()
	{
		ACLMessage msg = this.myAgent.blockingReceive(MessageTemplate.and(MessageTemplate.MatchPerformative(ACLMessage.REQUEST), MessageTemplate.MatchOntology("ontologia")));
		return msg;
	};
	
	private HomeMadeStruct[] indices()
	{
		HomeMadeStruct[] aux = new HomeMadeStruct[8];
		aux[0]= new HomeMadeStruct(-1,-1);
		aux[1]= new HomeMadeStruct(-1,0);
		aux[2]= new HomeMadeStruct(-1,1);
		aux[3]= new HomeMadeStruct(0,-1);
		aux[4]= new HomeMadeStruct(0,1);
		aux[5]= new HomeMadeStruct(1,-1);
		aux[6]= new HomeMadeStruct(1,0);
		aux[7]= new HomeMadeStruct(1,1);
		return aux;
	}
}
