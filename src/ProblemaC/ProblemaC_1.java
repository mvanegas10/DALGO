package ProblemaC;
//DAlgo 2015-10
//Problema C    : Solución Propuesta
//Grupo         : 03
//Autor         : Meili Vanegas Hernández, Jairo Iván Bernal Acosta

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.PriorityQueue;
import java.util.Stack;
import java.io.BufferedReader;

class Vertice
{
    public int[] coordenadas;
    public ArrayList<Vertice> arcos;
    public boolean marcado;
    public Vertice(int c1, int c2, int c3) { coordenadas = new int[3]; coordenadas[0] = c1; coordenadas[1] = c2; coordenadas[2] = c3; marcado = false; arcos = new ArrayList<Vertice>(); }
}

class Dijkstra
{
	public static ArrayList<Vertice> nodosIniciales;	
	public static HashMap<int[], Vertice> nodosFinalesLadoA;
	public static HashMap<int[], Vertice> nodosFinalesLadoB;
	public Dijkstra (){ nodosIniciales = new ArrayList<Vertice>(); nodosFinalesLadoA = new HashMap<int[], Vertice>(); nodosFinalesLadoB = new HashMap<int[], Vertice>();}
	public static boolean hayCamino(){
		boolean respuesta = false;
		boolean salioA = false;
		boolean salioB = false;
		Collection<Vertice> salidaA = nodosFinalesLadoA.values();
		Collection<Vertice> salidaB = nodosFinalesLadoB.values();
		Stack<Vertice> pila = new Stack<Vertice>();
		for (Vertice inicio : nodosIniciales) {
			inicio.marcado = true;
			if (nodosFinalesLadoA.containsKey(inicio.coordenadas) && nodosFinalesLadoB.containsKey(inicio.coordenadas)) return true; 
			pila.addAll(inicio.arcos);
			while (!salioA || !salioB){
				if(!pila.empty()){
					Vertice actual = pila.pop();
					if (!actual.marcado)
					{
						actual.marcado = true;
						salioA = (nodosFinalesLadoA.containsKey(actual.coordenadas));
						salioB = (nodosFinalesLadoB.containsKey(actual.coordenadas));
						pila.addAll(actual.arcos);
					}
				}
				else break;
			}
			pila.clear();
		}
		respuesta = salioA && salioB;
		return respuesta;
	}
}

class Grafo
{
	public HashMap<int[], Vertice> nodos;
	public Grafo (int n) { 
		nodos = new HashMap<int[],Vertice>();
		for (int i = 0; i <= n; i++) {
			for (int j = 0; j <= n; j++) {
				for (int k = 0; k <= n; k++) {
					if ((i+j+k) == n) { Vertice ver = new Vertice(i, j, k); nodos.put(ver.coordenadas, ver); };
				}
			}
		}
	}
}

public class ProblemaC_1{
	public static void main(String[] args) throws Exception {
		BufferedReader br = new BufferedReader(new InputStreamReader(new FileInputStream("src/ProblemaC/ProblemaCSmall.in")));
		File output = new File("src/ProblemaC/ProblemaCSmall.out");
		output.delete();
		BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(new FileOutputStream("src/ProblemaC/ProblemaCSmall.out")));
		
		String lineain,data[];
		int M, N;
		
		Date fecha = new Date(); 
		System.out.println(fecha.getHours() + " H " + fecha.getMinutes() + " MIN " + fecha.getSeconds() + " SECS ");
		
		while (true){
			lineain = br.readLine();
			data = lineain.split(" ");
			N = Integer.parseInt(data[0]);
			M = Integer.parseInt(data[1]);
			Dijkstra dijkstra = new Dijkstra();
			Grafo grafo = new Grafo(N);
			boolean respuesta = false;
			if (N==0 && M ==0) break;				// terminaci�n de la entrada
			for (int i=0; i!=M; i++){
				lineain = br.readLine();
				String[] coordenadas = lineain.split(" ");
				int a = Integer.parseInt(coordenadas[0]);
				int b = Integer.parseInt(coordenadas[1]);
				int c = Integer.parseInt(coordenadas[2]);
				Vertice vertice = new Vertice(a, b, c);
				for (int j1 = -1; j1 < 2; j1++){
					for (int j2 = 0; j2 < 2; j2++) {
						for (int j3 = 0; j3 < 2; j3++) {
							int coord1 = vertice.coordenadas[0] + j1;
							int coord2 = vertice.coordenadas[1] + j2;
							int coord3 = vertice.coordenadas[2] + j3;
							if (coord1 >= 0 && coord2 >= 0 && coord3 >= 0 && (coord1 + coord2 + coord3) == 3){
								int[] coor = {vertice.coordenadas[0] + j1, vertice.coordenadas[1] + j2, vertice.coordenadas[2] + j3};
								if (grafo.nodos.get(coor) != null){
									grafo.nodos.get(coor).arcos.add(vertice);
									vertice.arcos.add(grafo.nodos.get(coor));
								}
							}
						}
					}
				}
				if (a == 0) dijkstra.nodosIniciales.add(vertice);
				if (b == 0) dijkstra.nodosFinalesLadoA.put(vertice.coordenadas,vertice);
				if (c == 0) dijkstra.nodosFinalesLadoB.put(vertice.coordenadas,vertice);
			}
			boolean resultado = dijkstra.hayCamino();
			String ganador = resultado? "" + 'N':"" + 'B';
			System.out.println(ganador);
			bw.write(ganador + '\n');
		}
		bw.close();
		fecha = new Date(); 
		System.out.println(fecha.getHours() + " H " + fecha.getMinutes() + " MIN " + fecha.getSeconds() + " SECS ");
	}
}
