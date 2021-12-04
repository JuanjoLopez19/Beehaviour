package agentes;

import java.util.ArrayList;

import auxiliar.Auxiliar;
import auxiliar.HomeMadeStruct;
import auxiliar.Utils;
import comportamientos.OneShotDibujarColmena;
import jade.content.lang.sl.SLCodec;
import jade.core.Agent;
import jade.core.behaviours.CyclicBehaviour;
import jade.core.behaviours.OneShotBehaviour;
import jade.domain.DFService;
import jade.domain.FIPAException;
import jade.domain.FIPAAgentManagement.DFAgentDescription;
import jade.domain.FIPAAgentManagement.ServiceDescription;
import jade.lang.acl.ACLMessage;
import jade.lang.acl.MessageTemplate;
import jade.lang.acl.UnreadableException;

public class AbejaObrera extends Agent{

	private static final long serialVersionUID = 1L;
	
	private CyclicComerObrera cco;
	private OneShotPosicion osp;
	private ArrayList<HomeMadeStruct> pos = new ArrayList<>();
	
	private char [][] hive;
	
	private char OBRERA='O';
	private char RECOLECTOR='C';
	
	public void setup(){
				
		osp = new OneShotPosicion();
		cco = new CyclicComerObrera(pos);
		setServices();
		addBehaviour(osp);
		addBehaviour(cco);
		
	}	
	
	private void setServices() 
    {
        //Creates a new Agent descriptor and get its indicator (AID)
        DFAgentDescription dfd = new DFAgentDescription();
        dfd.setName(getAID());
        //Creates a new Service and set up its values
        ServiceDescription sd = new ServiceDescription();
        sd.setName("Abeja Obrera: Reunir");
        sd.setType("Reunir obrera");
        sd.addOntologies("ontologia");
        sd.addLanguages(new SLCodec().getName());
        //add the service to the agent
        dfd.addServices(sd);
        sd = new ServiceDescription();
        sd.setName("Abeja Obrera: Comer");
        sd.setType("Comer obrera");
        sd.addOntologies("ontologia");
        sd.addLanguages(new SLCodec().getName());
        dfd.addServices(sd);
        try
        {
            //Try catch to register the services
            DFService.register(this, dfd);
        }
        catch(FIPAException e)
        {
            System.err.println("Agente"+getLocalName()+": "+e.getMessage());
        }
    }
	
	public ACLMessage receiveMessage()
	{
		ACLMessage msg = blockingReceive(MessageTemplate.and(MessageTemplate.MatchPerformative(ACLMessage.REQUEST), MessageTemplate.MatchOntology("ontologia")));
		return msg;
	};
	
	public class OneShotPosicion extends OneShotBehaviour
	{
		/**
		 * 
		 */
		private static final long serialVersionUID = 1L;
		
		private HomeMadeStruct aux;
		ACLMessage msg;
		boolean flag = true;
		int index = 0;
		public OneShotPosicion() {
			super();
		}
		
		public void action() {
			try {
				Thread.sleep(1000);
			} catch (InterruptedException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
			//System.out.println("Estoy esperando a un mensaje");
			
				ACLMessage msg = receiveMessage();
				try {
					
						aux = (HomeMadeStruct) msg.getContentObject();
						if(aux.getIndex_x()==-1 & aux.getIndex_y() ==-1)
							Utils.enviarMensaje_unico(myAgent,aux, msg);
						
						msg = receiveMessage();
						aux = (HomeMadeStruct) msg.getContentObject();
						pos.add(aux);
						
						msg = receiveMessage();
						aux = (HomeMadeStruct) msg.getContentObject();
						pos.add(aux);
						
						//System.out.print("Mi posicion es: ");
						//HomeMadeStruct.print(pos);
				} catch (UnreadableException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
			}
		}
	}
	
	public class CyclicComerObrera extends CyclicBehaviour{
		/**
		 * 
		 */
		private static final long serialVersionUID = 1L;
		private ACLMessage msg;
		private Auxiliar aux;
		private HomeMadeStruct pos_aux = new HomeMadeStruct();
		private ArrayList<HomeMadeStruct> pos, auxiliar;
		private ArrayList<ACLMessage> RecolectorList;
		private int rand_num;
		public CyclicComerObrera(ArrayList<HomeMadeStruct> punto){
			super();
			pos=punto;
		}
		public void action() {
			try {
				Thread.sleep(5000);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
			// Recibir el mensaje de querer comida por parte de la reina
			msg = receiveMessage();
			try {
				if(msg.getContentObject() == null)
				{
					Utils.enviarMensaje_unico(myAgent, pos.get(1), msg);
				}
				else if (msg.getContentObject().getClass() == pos_aux.getClass())
				{
					pos_aux = (HomeMadeStruct) msg.getContentObject();
					System.out.println("Soy " + myAgent.getLocalName() + " y la obrera " + msg.getSender().getLocalName() + " me ha mandado su posición para alimentar a la reina" + HomeMadeStruct.print(pos_aux));
					Utils.enviarMensaje_todos(myAgent, "Comer Reina", null);
					try {
						Thread.sleep(1500);
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
					Utils.enviarMensaje_unico(myAgent, pos, msg);
				}
				else
				{
					aux = (Auxiliar) msg.getContentObject();
					hive = aux.getColmena();
					RecolectorList = aux.getLista_recolectoras();
					OneShotDibujarColmena.dibujarColmena(hive);
					System.out.println("Soy " + myAgent.getLocalName() + " y la reina me ha mandado que me mueva para comer");
					rand_num = (int) Math.floor(Math.random() * RecolectorList.size());
					Utils.enviarMensaje_unico(myAgent, pos.get(1), RecolectorList.get(rand_num) );
					msg = receiveMessage();
					auxiliar = (ArrayList<HomeMadeStruct>) msg.getContentObject();
					hive[pos.get(1).getIndex_x()][pos.get(1).getIndex_y()]=' ';
					OneShotDibujarColmena.dibujarColmena(hive);
					
					try {
						Thread.sleep(1500);
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
					
					hive[auxiliar.get(1).getIndex_x()][auxiliar.get(1).getIndex_y()]=OBRERA;
					hive[pos.get(1).getIndex_x()][pos.get(1).getIndex_y()]=RECOLECTOR;
					OneShotDibujarColmena.dibujarColmena(hive);
					
				}
			} catch (UnreadableException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
			//HomeMadeStruct.print(pos);
			
		}
	}
}