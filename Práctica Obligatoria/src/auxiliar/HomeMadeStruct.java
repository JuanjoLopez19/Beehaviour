package auxiliar;

import java.io.Serializable;

public class HomeMadeStruct implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private int index_x;
	private int  index_y;
	private String tipo;
	
	public HomeMadeStruct(int i, int j)
	{
		index_x=i;
		index_y=j;
	}
	
	public HomeMadeStruct(int i, int j, String s)
	{
		index_x=i;
		index_y=j;
		tipo = s;
	}
	
	public HomeMadeStruct() {}
	
	public int getIndex_x() 
	{
		return index_x;
	}
	
	public void setIndex_x(int index_x) 
	{
		this.index_x = index_x;
	}
	
	public int getIndex_y() 
	{
		return index_y;
	}
	
	public void setIndex_y(int index_y) 
	{
		this.index_y = index_y;
	}
	
	
	public String getTipo() {
		return tipo;
	}

	public void setTipo(String tipo) {
		this.tipo = tipo;
	}

	public static String print(HomeMadeStruct m) {
		return String.format( "(" + m.getIndex_x() + "," + m.getIndex_y() +")");
	}
}
