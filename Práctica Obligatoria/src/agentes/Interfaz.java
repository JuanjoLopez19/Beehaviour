package agentes;

import javax.swing.JFrame;
import javax.swing.JPanel;

import auxiliar.HomeMadeStruct;
import auxiliar.Utils;
import comportamientos.OneShotReunir;
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
import jade.util.leap.Serializable;
import jade.wrapper.StaleProxyException;

import javax.swing.JComponent;
import java.awt.Graphics;
import java.awt.Color;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.Image;
import java.io.IOException;
import java.io.InputStream;

import static java.lang.Thread.sleep;
import java.util.function.*;
import java.util.ArrayList;
import java.util.Random;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.imageio.ImageIO;
import javax.swing.ImageIcon;

public class Interfaz extends Agent
{

	private static final long serialVersionUID = 1L;

    private static final double H = Math.sqrt(3) / 2;
    
    private ArrayList<HomeMadeStruct> pos;
    HomeMadeStruct punto;
    HomeMadeStruct auxilar = new HomeMadeStruct();
    
    final int width = 20;
    final int height = 20;
    //Image img_corona = new ImageIcon("reina.png").getImage();
    Image img_corona;
    Image img_defensora;
    Image img_recolectora;
    public JFrame f = new JFrame("BeeHave");
	final Hexagon[][] grid = new Hexagon[height][width];
	public String[][] contenido = new String[20][20];
    
	public void setup() {  
		
		ClassLoader classLoader = Thread.currentThread().getContextClassLoader();
		InputStream input;
		try {
			input= classLoader.getResourceAsStream("reina.png");
			img_corona = ImageIO.read(input);
			
			input= classLoader.getResourceAsStream("defensora.png");
			img_defensora = ImageIO.read(input);
			
			input= classLoader.getResourceAsStream("recolectora.png");
			img_recolectora = ImageIO.read(input);
			
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		
        for(int row = 0; row < height; row++) {
            for(int col = 0; col < width; col++) {
                grid[row][col] = new Hexagon(row, col, 30);
            }
        }
        
        input= classLoader.getResourceAsStream("abeja.png");
		Image image;
		try {
			image = ImageIO.read(input);
	        f.setIconImage(image);
		} catch (IOException e1) {
			e1.printStackTrace();
		}
        f.setResizable(false);
        f.getContentPane().setLayout(new GridLayout());
        
        f.getContentPane().add(new JComponent() {
        	
			private static final long serialVersionUID = 1L;

			@Override public void paint(Graphics g) {              
                g.setColor(new Color(255, 153, 17));
                g.fillRect(0,0,1000,1000);
                g.setColor(new Color(0,0,0));
                final int[] xs = new int[6];
                final int[] ys = new int[6];
                for (Hexagon[] row : grid) {
                  for (Hexagon h: row) {
                    final int[] i = {0};
                    h.foreachVertex((x, y) -> {
                      xs[i[0]] = (int)((double)x);
                      ys[i[0]] = (int)((double)y);
                      i[0]++;
                    });                  
                    g.drawPolygon(xs, ys, 6);
                  }
                }

                for(int i=0; i<20;i++){
                    for(int j=0; j<20;j++){
                        if(contenido[i][j]!= null){
                            Hexagon u = new Hexagon(i,j,30);
                            if(contenido[i][j].equals("R")){
                                g.drawImage(img_corona, (int)(u.getCenterX()-15 ), (int)(u.getCenterY() -15 ), this);
                            } else if (contenido[i][j].equals("O")){
                                g.drawImage(img_defensora, (int)(u.getCenterX()-15 ), (int)(u.getCenterY() -15 ), this);
                            } else if (contenido[i][j].equals("C")){
                                g.drawImage(img_recolectora, (int)(u.getCenterX()-15 ), (int)(u.getCenterY() -15 ), this);
                            }
                           /*g.setFont(new Font("Arial",Font.BOLD,14));
                            g.drawString(
                                    contenido[i][j], 
                                    (int)(u.getCenterX() - 4), 
                                    (int)(u.getCenterY() +5)
                                );*/
                        }
                    }
                } 
            }
        });
        f.setBounds(0, 0, 848, 750);
        f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        f.setVisible(true);
        try {
            Thread.sleep(100);
        } catch (Throwable e) {

        } finally {
            f.repaint();
        }               
        
        try {
        	sleep(1000);
        } catch (InterruptedException ex) {
        	Logger.getLogger(Interfaz.class.getName()).log(Level.SEVERE, null, ex);
        }
    
    // COMPORTAMIENTOS
        OneShotPosicion osp = new OneShotPosicion();
        CyclicDibujar cd = new CyclicDibujar();
    
    setServices();
    
    // AÑADIR COMPORTAMIENTOS
    addBehaviour(osp);
    addBehaviour(cd);
	}
	
	
	protected void takeDown() {
		try 
		{
			System.out.println("\t\t"+getLocalName()+": es hora de acabar con todo...");
			f.getDefaultCloseOperation();
			getContainerController().kill();
		} 
		catch (StaleProxyException e) 
		{
			System.err.println("No se ha podido eliminar el contenedor de la plataforma");
			e.printStackTrace();
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
	        sd.setName("Interfaz: Dibujar");
	        sd.setType("Dibujar");
	        sd.addOntologies("ontologia");
	        sd.addLanguages(new SLCodec().getName());
	        
	        //add the service to the agent
	        dfd.addServices(sd);
	        sd = new ServiceDescription();
	        
	        sd.setName("Interfaz: Reunir");
	        sd.setType("Reunir interfaz");
	        sd.addOntologies("ontologia");
	        sd.addLanguages(new SLCodec().getName());
	        dfd.addServices(sd);
	        
	        sd = new ServiceDescription();
	        
	        sd.setName("Interfaz: Finalizar");
	        sd.setType("Finalizar Obrera");
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
    
    public void ponerAbeja(int x, int y, String s, JFrame f){
        contenido[x][y] = s;
        f.repaint();
    }
    
    public  void quitarAbeja(int x, int y, JFrame f){
        contenido[x][y] = "";
        f.repaint();
    }
    
    
    public class OneShotPosicion extends OneShotBehaviour
	{
		/**
		 * 
		 */
		private static final long serialVersionUID = 1L;
		
		private HomeMadeStruct aux;
		ACLMessage msg;
		public OneShotPosicion() 
		{
			super();
		}
		
		public void action() {
			try {
					ACLMessage msg = receiveMessage();
						
					aux = (HomeMadeStruct) msg.getContentObject();
					if(aux.getIndex_x()==-1 & aux.getIndex_y() ==-1)
						Utils.enviarMensaje_unico(myAgent,aux, msg);
					
				} catch (UnreadableException e) {
					System.err.println("No se ha podido leer el mensaje correctamente");
					e.printStackTrace();
				}
		}
	}
    
    public class CyclicDibujar extends CyclicBehaviour{

    	private ACLMessage msg;
    	private String aux = "comprobador";
		@Override
		public void action() {
			
			msg = receiveMessage();
			
			try {
				//punto = ((HomeMadeStruct)msg.getContentObject());
				if(msg.getContentObject().getClass() == aux.getClass())
				{
					doDelete();
				}
				else if(msg.getContentObject().getClass() == auxilar.getClass())
					{
						punto = ((HomeMadeStruct)msg.getContentObject());
						if(punto.getIndex_x() <0)
						{
							quitarAbeja(-punto.getIndex_x(),punto.getIndex_y(),f);
						} 
						else 
						{
							ponerAbeja(punto.getIndex_x(),punto.getIndex_y(),punto.getTipo(),f);
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
		ACLMessage msg = blockingReceive(MessageTemplate.and(MessageTemplate.MatchPerformative(ACLMessage.REQUEST), MessageTemplate.MatchOntology("ontologia")));
		return msg;
	};
    
    
    static class Hexagon implements Serializable {
        /**
		 * 
		 */
		private static final long serialVersionUID = 1L;
		final int row;
        final int col;
        final double sideLength;
        public Hexagon(int r, int c, double a) {
          this.row = r;
          this.col = c;
          this.sideLength = a;
        }

        double getCenterX() {
          return 2 * H * sideLength * (col + (row % 2) * 0.5);
        }

        double getCenterY() {
          return 3 * sideLength / 2  * row;
        }

        void foreachVertex(BiConsumer<Double, Double> f) {
          double cx = getCenterX();
          double cy = getCenterY();
          f.accept(cx + 0, cy + sideLength);
          f.accept(cx - H * sideLength, cy + 0.5 * sideLength);
          f.accept(cx - H * sideLength, cy - 0.5 * sideLength);
          f.accept(cx + 0, cy - sideLength);
          f.accept(cx + H * sideLength, cy - 0.5 * sideLength);
          f.accept(cx + H * sideLength, cy + 0.5 * sideLength);
        }
      }
}

    
