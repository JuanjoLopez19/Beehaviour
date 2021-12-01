package comportamientos;

import java.util.ArrayList;
import java.util.Scanner;

import auxiliar.HomeMadeStruct;
import auxiliar.Utils;
import jade.core.behaviours.CyclicBehaviour;
import jade.core.behaviours.OneShotBehaviour;
import jade.lang.acl.ACLMessage;
import jade.lang.acl.MessageTemplate;
import jade.lang.acl.UnreadableException;

public class OneShotReunir extends  CyclicBehaviour {
	ArrayList<HomeMadeStruct> aux;
	HomeMadeStruct posIni, aux2;
	char[][] hive;
	ACLMessage msg;
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
		try {
			Thread.sleep(1500);
		} catch (InterruptedException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		Utils.enviarMensaje_todos(myAgent, "Reunir obrera", posIni);
		while(!aux.isEmpty())
		{
			System.out.println("Estoy esperando la respuesta");
			msg = receiveMessage();
			try {
				aux2 = (HomeMadeStruct) msg.getContentObject();
				for(HomeMadeStruct m : aux)
				{
					if(m.getIndex_x()==aux2.getIndex_x() && m.getIndex_y() == aux2.getIndex_y())
					{
						aux.remove(aux2);
						System.out.printf("He recibido la posición(%d,%d)\n", aux2.getIndex_x(), aux2.getIndex_y());
						Utils.enviarMensaje_unico(myAgent,"Completado", msg);
						hive[aux2.getIndex_x()][aux2.getIndex_y()] = 'O';
						printer.dibujarColmena(hive);
					}
				}
				
					
			} catch (UnreadableException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}
	
	public ACLMessage receiveMessage()
	{
		ACLMessage msg = this.myAgent.blockingReceive(MessageTemplate.and(MessageTemplate.MatchPerformative(ACLMessage.REQUEST), MessageTemplate.MatchOntology("ontologia")));
		return msg;
	};
}
