package agentes;

import java.io.IOException;
import java.io.Serializable;

import auxiliar.HomeMadeStruct;
import auxiliar.Utils;
import jade.content.lang.sl.SLCodec;
import jade.core.Agent;
import jade.core.behaviours.CyclicBehaviour;
import jade.core.behaviours.OneShotBehaviour;
import jade.domain.DFService;
import jade.domain.FIPAException;
import jade.domain.FIPAAgentManagement.DFAgentDescription;
import jade.domain.FIPAAgentManagement.Envelope;
import jade.domain.FIPAAgentManagement.ServiceDescription;
import jade.lang.acl.ACLMessage;
import jade.lang.acl.MessageTemplate;
import jade.lang.acl.UnreadableException;

public class AbejaObrera extends Agent{

	private static final long serialVersionUID = 1L;
	protected CyclicBehaviour cyclicBehaviour;
	protected OneShotPosicion osp;
	HomeMadeStruct pos;
	public void setup(){
				
		/*cyclicBehaviour = new CyclicBehaviour(this){
			private static final long serialVersionUID = 1L;
			public void action(){
				ACLMessage msg = receiveMessage();
				try {
					int aux [] = (int[]) msg.getContentObject();
					System.out.printf("La reina me ha enviado su posicion: (%d,%d)", aux[0], aux[1]);
					aux[0]-=1;
					aux[1]-=1;
					enviarMensaje(myAgent,aux, msg);
				} catch (UnreadableException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} 
				
			}
		};*/
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
	
	public void enviarMensaje(Agent agent, Object obj, ACLMessage msg)
	{
		ACLMessage aclMessage = new ACLMessage(ACLMessage.REQUEST);
    	aclMessage.addReceiver(msg.getSender());
    	
        aclMessage.setOntology("ontologia");
        //el lenguaje que se define para el servicio
        aclMessage.setLanguage(new SLCodec().getName());
        //el mensaje se transmita en XML
        aclMessage.setEnvelope(new Envelope());
		//cambio la codificacion de la carta
		aclMessage.getEnvelope().setPayloadEncoding("ISO8859_1");
        //aclMessage.getEnvelope().setAclRepresentation(FIPANames.ACLCodec.XML); 
		try {
			aclMessage.setContentObject((Serializable)obj);
			agent.send(aclMessage);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	
	public class OneShotPosicion extends OneShotBehaviour{
		/**
		 * 
		 */
		private static final long serialVersionUID = 1L;
		private HomeMadeStruct aux;
		private HomeMadeStruct [] indices = indices();
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
			while(flag)
			{
				ACLMessage msg = receiveMessage();
				try {
					if(msg.getContentObject().getClass() != "hola".getClass())
					{
						aux = (HomeMadeStruct) msg.getContentObject();
						aux.setIndex_x(aux.getIndex_x()+indices[index].getIndex_x());
						aux.setIndex_y(aux.getIndex_y()+indices[index].getIndex_y());
						index++;
						Utils.enviarMensaje_unico(myAgent,aux, msg);
					}
					else
						{
						System.out.println("He recibido la confirmación");
						pos=aux;
						flag = false;
						}
				} catch (UnreadableException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}
	
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
}