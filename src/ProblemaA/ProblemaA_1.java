package ProblemaA;
// DAlgo 2015-10
// Problema A    : Solución Propuesta
// Grupo         : 03
// Autor         : Meili Vanegas Hernández, Jairo Iván Bernal Acosta

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.util.Date;
import java.util.Stack;

public class ProblemaA_1 {

	public static void main(String[] args) throws Exception {
		BufferedReader br = new BufferedReader(new InputStreamReader(new FileInputStream("src/ProblemaA/ProblemaASmall.in")));
		File output = new File("src/ProblemaA/ProblemaASmall.out");
		output.delete();
		BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(new FileOutputStream("src/ProblemaA/ProblemaASmall.out")));
		String lineain,data[];
		int M, N;
		
		Date fecha = new Date(); 
		System.out.println("HORA INICIO: " + fecha.getHours() + " HOURS " + fecha.getMinutes() + " MINS " + fecha.getSeconds() + " SECS ");
		
		while (true){
			lineain = br.readLine();
			data = lineain.split(" ");
			M = Integer.parseInt(data[0]);
			N = Integer.parseInt(data[1]);
			if (M==0 && N ==0) break;				// terminaci�n de la entrada
			boolean [][] foto = new boolean [M][N];
			for (int i=0; i!=M; i++){
				lineain = br.readLine();
				for (int j=0; j!=N; j++){
					if (lineain.charAt(j) == '1'){
						foto[i][j] = true;
					}
					else{
						foto[i][j] = false;
					}
				}
			}
			
			int [] resp = new int [2];
			resp = propuesta(foto);
			bw.write(resp[0] + " " + resp[1] + '\n');
			System.out.println(resp[0] + " " + resp[1]);
		}
		bw.close();
		fecha = new Date(); 
		System.out.println("HORA FIN: " + fecha.getHours() + " HOURS " + fecha.getMinutes() + " MINS " + fecha.getSeconds() + " SECS ");
	}
	
	/**
	 * Propuesta de mejora, retorna los valores del �rea del rect�ngulo y cuadrado m�s grande de una foto
	 * @param foto Matriz booleana
	 * @return	�rea del rect�ngulo y cuadrado m�s grande de una foto
	 */
	public static int[] propuesta (boolean [][] foto){
		int cmax = 0;
		int rmax = 0;
		int m = foto.length;
		int n = foto[0].length;
		int i = m - 1;
		int j = n - 1;
		boolean nuevo = true;
		int [][] matriz = new int [m][n];
		int [][] maxVertical = new int [m][n + 1];
		
		while (i >= 0) {
			while (j >= 0) {
				
				//	Hay un uno
				
				if (foto[i][j]){
					
					//	Es interno
					
					if ( i != m - 1 && j != n - 1){
						
						//	Matriz para soluci�n de cuadrados
						
						int uno = matriz[i+1][j];
						int dos = matriz[i][j+1];
						int tres = matriz[i+1][j+1];
						matriz[i][j] = 1 + Math.min((Integer) Math.min(uno, dos), tres);
						
					}
					
					//	Es un borde
					
					else
						matriz[i][j] = 1;
					
					//	Matriz para soluci�n de rect�ngulos
					
					//	Es interno
					
					if (i < m - 1){
						if (maxVertical[i + 1][j + 1] > 0) maxVertical[i][j + 1] = maxVertical[i + 1][j + 1] - 1;
						else nuevo = true;
					}
					
					//	No se ha calculado el histograma
					
					if (nuevo){
						int k = i;
						int suma = 0;
						while (k >= 0) {
							if(foto[k][j]) suma++;
							else k = 0;
							k--;
						}
						maxVertical[i][j + 1] = suma;
					}
				}
				
				//	Es un cero
				
				else{
					matriz[i][j] = 0;
					maxVertical[i][j + 1] = 0;
				}
				cmax = Math.max(matriz[i][j], cmax);
				j--;
			}
			i--;
			j = n - 1;
		}
		
		//Encuentra rect�ngulo con �rea m�xima
		
		i = m - 1;
		while (i >= 0) {
			rmax = Math.max(rmax, rectanguloMaximo(maxVertical[i]));						
			i--;
		}
		
		int [] resp = new int [2];
		resp[0] = cmax*cmax;
		resp[1] = rmax;
		return resp;
	}
	

	/**
	 * Calucula el �rea del rect�ngulo m�s grande dado un histograma
	 * @param arreglo El arreglo que contiene los valores del histograma
	 * @return El �rea del rect�ngulo m�ximo dado un histograma
	 */
	public static int rectanguloMaximo(int [] arreglo){
		int maxArea = 0;
		Stack<Integer> stk = new Stack<Integer>();
		stk.push(0);
		
		for (int i = 1; i < arreglo.length; i++) {
			int actual = arreglo[i];
			
			while(!stk.empty()){
				int tope = stk.peek();
				
				//	Verifica si los valores son crecientes
				
				if (actual > arreglo[tope]){
					stk.push(i);
					break;
				}
				
				//	Elimina el m�s grande de la pila
				
				tope = stk.pop();
				
				int izquierdo = (stk.empty())? 0: stk.peek() + 1;
				int derecho = (arreglo[tope] == actual)? i + 1: i;
				
				int area = (derecho - izquierdo) * arreglo[tope];
				maxArea = (area > maxArea)? area: maxArea;
			}
			
			if (stk.empty()) stk.push(i);
		}
		
		//	Vac�a la pila y recalcula el �rea m�xima
		
		int derecho = arreglo.length;
		while( !stk.empty() ){
			int tope = stk.pop();
			int izquierdo = (stk.empty())? 0: stk.peek() + 1;
			int area = (derecho - izquierdo) * arreglo[tope];
			maxArea = (area > maxArea)? area: maxArea;
		}
		return maxArea;
	}
}

