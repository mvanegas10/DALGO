package ProblemaB;
// DAlgo 2015-10
// Problema B    : Solución Propuesta
// Grupo         : 03
// Autor         : Jairo Iván Bernal Acosta, Meili Vanegas Hernández

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.EmptyStackException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Queue;
import java.util.Set;
import java.util.Stack;

public class ProblemaB_1 {
	
	public String direc;
	
	public ProblemaB_1(String ubicacion,String salida) throws Exception
	{
		direc=salida;
		cargarDatos(ubicacion);
	}
	
	public void cargarDatos(String ubicacion)throws Exception
	{
		InputStream in=new FileInputStream(new File(ubicacion));
		BufferedReader br = new BufferedReader(new InputStreamReader(in));
		
		String lineain,data[];
		int M, N;
		
		while (true){
			lineain = br.readLine();
			
			if(lineain==null)
			{
				break;	
			}
			else
			{
				String lineaUno = br.readLine();
				String lineaDos = br.readLine();
				
				String rta=procesar(lineaUno, lineaDos);
				escribriRta(rta);
				System.out.println(rta);
			}
			
		}
		
	}
	
	public String procesar(String lineaUno,String lineaDos)
	{
		
		
		String[] cualidadesUno=lineaUno.split(",");
		
		Grafo GC= new Grafo();
		Grafo GA= new Grafo();
		for(int i=0;i<cualidadesUno.length;i++)
		{
			String[] cualidades= cualidadesUno[i].split(" ");
			
			for (int j = 0; j < cualidades.length-1;j++) 
			{
				
				String cualidadA = cualidades[j];
				String cualidadB = cualidades[j+1];
			if(cualidadA.equals("")||cualidadB.equals("")||cualidadB.equals(";")||cualidadA.equals(";")||cualidadA.equals(" ")||cualidadB.equals(" "))
			{
				
			}
			
			else
			{
				
				GA.agrgarVertice(cualidadA);
				GA.agrgarVertice(cualidadB);
				GC.agrgarVertice(cualidadA);
				GC.agrgarVertice(cualidadB);
				//GA.agregarRelacion(cualidadA, cualidadB);
				GC.agregarRelacion(cualidadA, cualidadB);
				
			}
			
			}
			
		}
		
		Grafo GB= new Grafo();
		
		String[] cualidadesDos=lineaDos.split(",");
		
		for(int i=0;i<cualidadesDos.length;i++)
		{
			String[] cualidades= cualidadesDos[i].split(" ");
			
			for (int j = 0; j < cualidades.length-1;j++) 
			{
				
				String cualidadA = cualidades[j];
				String cualidadB = cualidades[j+1];
			if(cualidadA.equals("")||cualidadB.equals("")||cualidadB.equals(";")||cualidadA.equals(";")||cualidadA.equals(" ")||cualidadB.equals(" "))
			{
				
			}
			
			else
			{
				GC.agrgarVertice(cualidadA);
				GC.agrgarVertice(cualidadB);
				
				GB.agrgarVertice(cualidadA);
				GB.agrgarVertice(cualidadB);
				//GB.agregarRelacion(cualidadA, cualidadB);
				GC.agregarRelacion(cualidadA, cualidadB);
			}
			
			}
			
		}
		

		
		return soucion(GC);
	}

	public static void main(String[] args) throws Exception 
	{
		
		ProblemaB_1 pro= new ProblemaB_1("src/ProblemaB/ProblemaBSmall.in","src/ProblemaB/ProblemaBSmall.out");
		
		
		
		
	}
	
	public String soucion(Grafo GB)
	
	{
		String rta="C";
		Atributo ciclo=GB.darCiclo();
		if(ciclo!=null)
		{
			rta="P";
			Atributo aEliminar=GB.darCiclo();
			
			GB.eliminar(aEliminar);
			ciclo=GB.darCiclo();
			if(ciclo!=null)
			{
				rta="N";
			}
		}
		
		return rta;
	}
	
	public void escribriRta(String rta) throws Exception
	{
		OutputStream in=new FileOutputStream(new File(direc));
		OutputStreamWriter br = new OutputStreamWriter(in);
		
		br.write(rta);
		
	}

}


class Grafo
{
	HashMap<String, Atributo> vertices;
	HashMap<String, Atributo> fuentes;
	Stack<Atributo> pendientes;
	Stack<Atributo> marcas;
	
	public Grafo()
	{
		vertices= new HashMap<>();
		fuentes=new HashMap<>();
		pendientes= new Stack<>();
		marcas= new Stack<>();
	}
	
	

	public void eliminar(Atributo aEliminar)
	{
		Iterator<Atributo> iterador= vertices.values().iterator();
		
		while(iterador.hasNext())
		{
			desMarcarTodos();
			Atributo actual= iterador.next();
			
			actual.eliminarSucesor(aEliminar);
		}
		
		vertices.remove(aEliminar.palabra);
		
	}



	public Atributo darCiclo()
	{
		
	Iterator<Atributo> iterador= vertices.values().iterator();
		
		while(iterador.hasNext())
		{
			desMarcarTodos();
			
			Atributo actual= iterador.next();
			if(existeCamino(actual, actual))
			{
				return actual;
			}
		}
		
		return null;
	}



	public int numeroVetcies()
	{
		return vertices.size();
	}
	public boolean agrgarVertice( String atributo )
	{
		Atributo aAgregar= vertices.get(atributo);
		
		 if(aAgregar!=null)
			{
				return false;
			}
			 aAgregar= new Atributo(atributo);
			 vertices.put(atributo, aAgregar);
			 fuentes.put(atributo, aAgregar);
			 return true;
		
	}
	
	public Atributo buscarNodo(Atributo aBuscar )
	{
		return vertices.get(aBuscar.darCualidad());
	}
	
	public boolean agregarRelacion(String origen, String destino)
	{
		Atributo Origen= vertices.get(origen);
		Atributo Destino= vertices.get(destino);
		if(fuentes.containsKey(destino))
		{
			fuentes.remove(Destino);
		}
	
		return Origen.agregarSucesor(Destino);
		
	}
	
	public void desMarcarTodos()
	{
		Iterator<Atributo> iterador= vertices.values().iterator();
		
		while(iterador.hasNext())
		{
			Atributo actual= iterador.next();
			actual.desmarcar();
		}
		
	}
	
	
	public Atributo darSucesor(Atributo at,Atributo Marca)
	{
		return at.darSucesor(Marca,pendientes,marcas);
	}
	
	  public boolean existeCamino(Atributo root,Atributo llegada)
	    {
	        //Since queue is a interface
	        Queue<Atributo> queue = new LinkedList<Atributo>();

	        if(root == null||llegada==null) 
	        	{
	        	return true;
	        	}
	       
	        queue.add(root);
	        boolean primeraVez=true;
	        while(!queue.isEmpty())
	        {
	        	
	         Atributo r = queue.remove(); 
	         
	         if(r==llegada&&!primeraVez)
	         {
	        	 return true;
	         }
	         
	         if(primeraVez)
	         {
	        	 primeraVez=false;
	         }
	         Iterator<Atributo>  hijos=r.darScesores();
	         while(hijos.hasNext())
	         {
	        	 Atributo n=hijos.next();
	        	 if(!n.recorrido())
	                {
	                    queue.add(n);
	                    n.marcar();

	                }
	         }
	            
	        }

	        	return false;
	    }
	
	
	
	
}


class Atributo
{
	Map<String, Atributo> sucesores;

	private boolean recorrido;
	
	String palabra;
	
	public Atributo(String cualidad)
	{
		palabra=cualidad;
		recorrido=false;
		sucesores=new HashMap<>();
	}
	
	public void eliminarSucesor(Atributo aEliminar)
	{
		sucesores.remove(aEliminar.darCualidad());
		
	}

	public String darCualidad()
	{
		return palabra;
	}
	
	public boolean recorrido()
	{
		return recorrido;
	}
	
	public void desmarcar()
	{
		recorrido=false;
	}
	public void marcar()
	{
		recorrido=true;
	}
	
	public boolean agregarSucesor(Atributo atributo)
	{
		Atributo aAgregar=  sucesores.get(atributo.darCualidad());
		if(aAgregar!=null)
		{
			return false;
		}
		 sucesores.put(atributo.darCualidad(), atributo);
		 return true;
		
	}
	public Atributo darSucesor(Atributo Marca,Stack<Atributo> cola, Stack<Atributo> marcas)
	{
		Atributo aRetornar=null;
		Iterator<Atributo> iterador= sucesores.values().iterator();
		int r=0;
		boolean si=true;
		while(iterador.hasNext())
		{
			Atributo actual= iterador.next();
			if(!actual.recorrido()&&si)
			{
				aRetornar= actual;
				si=false;
			}
			if(!actual.recorrido()&&si)
			{
				cola.push(actual);
				marcas.push(Marca);
			}
			r=r+1;
		}
		return aRetornar;
		
	}
	
	public Iterator<Atributo> darScesores()
	{
		return sucesores.values().iterator();
	}
	
	public boolean tieneSucesores()
	{
		int numero= sucesores.size();
		
		if(numero!=0)
		{
			return false;
		}
		
		return true;
	}
	
	
	
}
