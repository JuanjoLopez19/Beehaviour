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
import jade.wrapper.StaleProxyException;

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
	
	protected void takeDown() {
		System.out.println("\t\t"+getLocalName()+": la reina ha decidido morir, por lo que la colmena ha acabado...");
		try {
			getContainerController().kill();
		} catch (StaleProxyException e) {
			System.err.println("No se ha podido eliminar el contenedor de la plataforma");
			e.printStackTrace();
		}
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
        sd.setType("Finalizar Obrera");
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
					ACLMessage msg = receiveMessage();
						
					aux = (HomeMadeStruct) msg.getContentObject();
					if(aux.getIndex_x()==-1 & aux.getIndex_y() ==-1)
						Utils.enviarMensaje_unico(myAgent,aux, msg);
					
					msg = receiveMessage();
					aux = (HomeMadeStruct) msg.getContentObject();
					pos.add(aux);
					
					msg = receiveMessage();
					aux = (HomeMadeStruct) msg.getContentObject();
					pos.add(aux);
						
				} catch (UnreadableException e) {
					System.err.println("No se ha podido leer el mensaje correctamente");
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
		private String flag = "Variable para finalizar";
		public CyclicComerObrera(ArrayList<HomeMadeStruct> punto){
			super();
			pos=punto;
		}
		
		@SuppressWarnings("unchecked")
		public void action() 
		{
			msg = receiveMessage();
			try {
					Thread.sleep(2000);
					if(msg.getContentObject() == null)
					{
						Utils.enviarMensaje_unico(myAgent, pos.get(1), msg);
					}
					else if (msg.getContentObject().getClass() == pos_aux.getClass())
					{
						pos_aux = (HomeMadeStruct) msg.getContentObject();
						System.out.println(myAgent.getLocalName() + ": la obrera " + msg.getSender().getLocalName() + " me ha mandado su posición para alimentar a la reina" + HomeMadeStruct.print(pos_aux));
						Utils.enviarMensaje_todos(myAgent, "Comer", null);
						
						Thread.sleep(500);
						
						Utils.enviarMensaje_unico(myAgent, pos, msg);
					}
					else if(msg.getContentObject().getClass() == flag.getClass())
					{
						doDelete();
					}
					else
					{
						aux = (Auxiliar) msg.getContentObject();
						hive = aux.getColmena();
						RecolectorList = aux.getLista_recolectoras();
						OneShotDibujarColmena.dibujarColmena(hive);
						System.out.println("\t\t" + myAgent.getLocalName() + ": la reina me ha mandado avisar a una recolectora para que la alimente");
						rand_num = (int) Math.floor(Math.random() * RecolectorList.size());
						System.out.println("\t\t" + myAgent.getLocalName() + ": le envió el mensaje de intercambio a " + RecolectorList.get(rand_num).getSender().getLocalName());
						Utils.enviarMensaje_unico(myAgent, pos.get(1), RecolectorList.get(rand_num) );
						
						msg = receiveMessage();
						auxiliar = (ArrayList<HomeMadeStruct>) msg.getContentObject();
						hive[pos.get(1).getIndex_x()][pos.get(1).getIndex_y()]=' ';
						OneShotDibujarColmena.dibujarColmena(hive);
						
						
						Thread.sleep(1500);
						
						
						hive[auxiliar.get(1).getIndex_x()][auxiliar.get(1).getIndex_y()]=OBRERA;
						hive[pos.get(1).getIndex_x()][pos.get(1).getIndex_y()]=RECOLECTOR;
						OneShotDibujarColmena.dibujarColmena(hive);
						System.out.printf("\t\t"+ myAgent.getLocalName()+": Ya ha comido la reina me vuelvo a mi posicion\n");
						
						Thread.sleep(2500);
						
						hive[auxiliar.get(1).getIndex_x()][auxiliar.get(1).getIndex_y()]=RECOLECTOR;
						hive[pos.get(1).getIndex_x()][pos.get(1).getIndex_y()]=OBRERA;
						OneShotDibujarColmena.dibujarColmena(hive);
						System.out.printf("\t\t"+ myAgent.getLocalName()+": he vuelto a mi posición\n");
						
						Thread.sleep(1000);
						Utils.enviarMensaje_todos(myAgent, "Comer", null);
				}
			} catch (UnreadableException e) {
				System.err.println("No se pudo leer el mensaje correctamente");
				e.printStackTrace();
			} catch (InterruptedException e) {
				System.err.println("Se interrumpio el sleep");
				e.printStackTrace();
			}
		}
	}
}
