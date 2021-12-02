package agentes;

import auxiliar.HomeMadeStruct;
import auxiliar.Utils;
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
	protected CyclicBehaviour cyclicBehaviour;
	protected OneShotPosicion osp;
	HomeMadeStruct pos, pos_ran;
	public void setup(){
				
		osp = new OneShotPosicion();
		setServices();
		addBehaviour(osp);	
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
        sd.setType("Comer");
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
			System.out.println("Estoy esperando a un mensaje");
			
				ACLMessage msg = receiveMessage();
				try {
					
						aux = (HomeMadeStruct) msg.getContentObject();
						if(aux.getIndex_x()==-1 & aux.getIndex_y() ==-1)
							Utils.enviarMensaje_unico(myAgent,aux, msg);
						
						msg = receiveMessage();
						aux = (HomeMadeStruct) msg.getContentObject();
						pos_ran=aux;
						
						msg = receiveMessage();
						aux = (HomeMadeStruct) msg.getContentObject();
						pos=aux;
						
						System.out.print("Mi posicion es: ");
						HomeMadeStruct.print(pos);
				} catch (UnreadableException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
			}
		}
	}
}