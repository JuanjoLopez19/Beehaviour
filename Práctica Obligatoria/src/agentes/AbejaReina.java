package agentes;

import java.util.ArrayList;
import java.util.Random;

import auxiliar.HomeMadeStruct;
import comportamientos.CyclicComida;
import comportamientos.OneShotReunir;
import jade.content.lang.sl.SLCodec;
import jade.core.Agent;
import jade.domain.DFService;
import jade.domain.FIPAException;
import jade.domain.FIPAAgentManagement.DFAgentDescription;
import jade.domain.FIPAAgentManagement.ServiceDescription;
import jade.lang.acl.ACLMessage;
import jade.wrapper.StaleProxyException;

public class AbejaReina extends Agent {
	
	private static final long serialVersionUID = 1L;
	
	private String REINA = "R";
	private int DIM_MAX = 20;
	private int x,y;
	private HomeMadeStruct posIni;
	
	private char[][] colmena = new char[DIM_MAX][DIM_MAX];

	private ArrayList <HomeMadeStruct> indices = new ArrayList<HomeMadeStruct>();
	private ArrayList <ACLMessage> lista_msg= new ArrayList<>();
	private ArrayList <ACLMessage> interfaz_msg= new ArrayList<>();

	public void setup()
	{
		
		for (int i = 0; i < colmena.length;i++)
		{
			for (int j = 0; j < colmena[0].length; j++)
			{
				colmena[i][j] = ' ';
			}
		}
				
		Random random = new Random();
		
		do {
            x = random.nextInt(13) + 2;
            y = random.nextInt(14) + 1;
        } while(y==1 && x%2==0);
		
		posIni = new HomeMadeStruct(x,y,REINA);
		
		calcIndices(indices,posIni);
		
		OneShotReunir osr = new OneShotReunir(posIni, colmena, lista_msg, interfaz_msg);
		CyclicComida cc = new CyclicComida(lista_msg);
		
		setServices();
		
		addBehaviour(osr);
		addBehaviour(cc);	
	}
	
	protected void takeDown() {
		try 
		{
			System.out.println("\t\t"+getLocalName()+": es hora de acabar con todo...");
			getContainerController().kill();
		} 
		catch (StaleProxyException e) 
		{
			System.err.println("No se ha podido eliminar el contenedor de la plataforma");
			e.printStackTrace();
		}
	}
	
	private void calcIndices(ArrayList<HomeMadeStruct> l, HomeMadeStruct m)
	{
		int i = m.getIndex_x();
		int j = m.getIndex_y();
		
		if(i%2==0) {
			HomeMadeStruct aux =  new HomeMadeStruct(i-1,j-1);
			l.add(aux);
			
			aux = new HomeMadeStruct(i-1,j);
			l.add(aux);
			
			aux = new HomeMadeStruct(i,j-1);
			l.add(aux);
			
			aux = new HomeMadeStruct(i,j+1);
			l.add(aux);
			
			aux = new HomeMadeStruct(i+1,j-1);
			l.add(aux);
			
			aux = new HomeMadeStruct(i+1,j);
			l.add(aux);
		} else {
			HomeMadeStruct aux =  new HomeMadeStruct(i-1,j);
			l.add(aux);
			
			aux = new HomeMadeStruct(i-1,j+1);
			l.add(aux);
			
			aux = new HomeMadeStruct(i,j-1);
			l.add(aux);
			
			aux = new HomeMadeStruct(i,j+1);
			l.add(aux);
			
			aux = new HomeMadeStruct(i+1,j);
			l.add(aux);
			
			aux = new HomeMadeStruct(i+1,j+1);
			l.add(aux);
		}
		
		
	
	}
	
	private void setServices() 
    {
		try
	    {
	        //Creates a new Agent descriptor and get its indicator (AID)
	        DFAgentDescription dfd = new DFAgentDescription();
	        dfd.setName(getAID());
	        
	        //Creates a new Service and set up its values
	        ServiceDescription sd = new ServiceDescription();
	        sd.setName("Abeja reina: Reunir");
	        sd.setType("Reunir");
	        sd.addOntologies("ontologia");
	        sd.addLanguages(new SLCodec().getName());
	        
	        //add the service to the agent
	        dfd.addServices(sd);
	        sd = new ServiceDescription();
	        sd.setName("Abeja reina: Comer");
	        sd.setType("Comer");
	        sd.addOntologies("ontologia");
	        sd.addLanguages(new SLCodec().getName());
	        dfd.addServices(sd);
	        
            //Try catch to register the services
            DFService.register(this, dfd);
        }
        catch(FIPAException e)
        {
            System.err.println("Agente"+getLocalName()+": "+e.getMessage());
        }
    }
	
}



