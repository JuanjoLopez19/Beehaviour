package agentes;

import java.util.ArrayList;
import auxiliar.HomeMadeStruct;
import comportamientos.CyclicComida;
import comportamientos.OneShotDibujarColmena;
import comportamientos.OneShotReunir;
import jade.content.lang.sl.SLCodec;
import jade.core.Agent;
import jade.core.behaviours.CyclicBehaviour;
import jade.domain.DFService;
import jade.domain.FIPAException;
import jade.domain.FIPAAgentManagement.DFAgentDescription;
import jade.domain.FIPAAgentManagement.ServiceDescription;
import jade.lang.acl.ACLMessage;

public class AbejaReina extends Agent {
	
	private static final long serialVersionUID = 1L;
	
	protected CyclicBehaviour cyclicBehaviour;
	private char[][] colmena = new char[25][25];
    char reina = 'R';
	int xInicial, yInicial;
	private ArrayList <HomeMadeStruct> indices = new ArrayList<HomeMadeStruct>();
	private ArrayList <ACLMessage> lista_msg= new ArrayList<>();
	private HomeMadeStruct posIni;
	
	public int getxInicial() {
		return xInicial;
	}

	public void setxInicial(int xInicial) {
		this.xInicial = xInicial;
	}

	public int getyInicial() {
		return yInicial;
	}

	public void setyInicial(int yInicial) {
		this.yInicial = yInicial;
	}

	public char[][] getColmena() {
		return colmena;
	}

	public void setColmena(char[][] colmena) {
		this.colmena = colmena;
	}
	
	public void setup(){
		
		for (int i = 0; i< colmena.length;i++){
			for (int j = 0; j<colmena[0].length; j++){
				colmena[i][j] = ' ';
			}
		}
		xInicial = (int) Math.floor(Math.random() * 23 + 1);
		yInicial = (int) Math.floor(Math.random() * 23 + 1);
		posIni = new HomeMadeStruct(xInicial,yInicial);
		
		//reina.addAttribute(TextAttribute.FOREGROUND, Color.red, 39, 40);
		
		colmena[posIni.getIndex_x()][posIni.getIndex_y()] = reina;
		calcIndices(indices,posIni);
		OneShotDibujarColmena osd = new OneShotDibujarColmena(colmena);
		OneShotReunir osr = new OneShotReunir(posIni, colmena, lista_msg);
		CyclicComida cc = new CyclicComida(lista_msg);
		setServices();
		
		addBehaviour(osd);
		addBehaviour(osr);
		addBehaviour(cc);
		
		
		
	}
	
	private void calcIndices(ArrayList<HomeMadeStruct> l, HomeMadeStruct m)
	{
		int i = m.getIndex_x();
		int j = m.getIndex_y();
		
		HomeMadeStruct aux =  new HomeMadeStruct(i-1,j-1);
		l.add(aux);
		
		aux = new HomeMadeStruct(i-1,j);
		l.add(aux);
		
		aux = new HomeMadeStruct(i-1,j+1);
		l.add(aux);
		
		aux = new HomeMadeStruct(i,j-1);
		l.add(aux);
		
		aux = new HomeMadeStruct(i,j+1);
		l.add(aux);
		
		aux = new HomeMadeStruct(i+1,j-1);
		l.add(aux);
		
		aux = new HomeMadeStruct(i+1,j);
		l.add(aux);
		
		aux = new HomeMadeStruct(i+1,j+1);
		l.add(aux);
	}
	
	private void setServices() 
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
	
}



