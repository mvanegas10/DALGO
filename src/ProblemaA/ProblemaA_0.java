package ProblemaA;
// DAlgo 2015-10
// Problema A    : Solución ingenua
// Grupo         : 00
// Autor         : Rodrigo Cardoso

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

public class ProblemaA_0 {

	public static void main(String[] args) throws Exception {
		BufferedReader br = new BufferedReader(new InputStreamReader(new FileInputStream("src/ProblemaA/Prueba.in")));
		String lineain,data[];
		int M, N;
		
		Date fecha = new Date(); 
		System.out.println(fecha.getHours() + " H " + fecha.getMinutes() + " MIN " + fecha.getSeconds() + " SECS ");
		
		while (true){
			lineain = br.readLine();
			data = lineain.split(" ");
			M = Integer.parseInt(data[0]);
			N = Integer.parseInt(data[1]);
			if (M==0 && N ==0) break;				// terminación de la entrada
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
			System.out.println(resp[0] + " " + resp[1]);
		}
		
		fecha = new Date(); 
		System.out.println(fecha.getHours() + " H " + fecha.getMinutes() + " MIN " + fecha.getSeconds() + " SECS ");
	}

	//Solución ingenua
	public static int[] solve(boolean [][] foto){
		int cmax = 0;
		int rmax = 0;
		int m = foto.length;
		int n = foto[0].length;

		for(int i=0; i != m; i++){
			for (int j=0; j != n; j++){
				int cmaxij = 0;
				int rmaxij = 0;
				for (int p1=0; p1!=i+1; p1++){
					for(int p2=p1; p2!=i+1; p2++){
						for (int q1=0; q1!=j+1; q1++){
							for(int q2=q1; q2!=j+1; q2++){
								int r = p1;
								int rcent = p2+1;
								int s = q1;
								while (r != rcent){
									if (!foto[r][s]){
										rcent = r;
									}
									else{
										if (s!=q2) s++;
										else{
											r++;
											s = q1;
										}
									}
								}
								if (r == p2+1){
									int area = (p2-p1+1)*(q2-q1+1);
									rmaxij = Math.max(rmaxij,area);
									if (p2-p1+1 == q2-q1+1){
										cmaxij = Math.max(cmaxij,area);
									}
								}
							}
						}
					}
				}
				//Actualiza el máximo
				rmax = Math.max(rmax, rmaxij);
				cmax = Math.max(cmax, cmaxij);
			}
		}
		int [] resp = new int [2];
		resp[0] = cmax;
		resp[1] = rmax;
		return resp;
	}
	
	
	/**
	 * Propuesta de mejora, retorna los valores del área del rectángulo y cuadrado más grande de una foto
	 * @param foto Matriz booleana
	 * @return	área del rectángulo y cuadrado más grande de una foto
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
						
						//	Matriz para solución de cuadrados
						
						int uno = matriz[i+1][j];
						int dos = matriz[i][j+1];
						int tres = matriz[i+1][j+1];
						matriz[i][j] = 1 + Math.min((Integer) Math.min(uno, dos), tres);
						
					}
					
					//	Es un borde
					
					else
						matriz[i][j] = 1;
					
					//	Matriz para solución de rectángulos
					
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
		
		//Encuentra rectángulo con área máxima
		
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
	 * Calucula el área del rectángulo más grande dado un histograma
	 * @param arreglo El arreglo que contiene los valores del histograma
	 * @return El área del rectángulo máximo dado un histograma
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
				
				//	Elimina el más grande de la pila
				
				tope = stk.pop();
				
				int izquierdo = (stk.empty())? 0: stk.peek() + 1;
				int derecho = (arreglo[tope] == actual)? i + 1: i;
				
				int area = (derecho - izquierdo) * arreglo[tope];
				maxArea = (area > maxArea)? area: maxArea;
			}
			
			if (stk.empty()) stk.push(i);
		}
		
		//	Vacía la pila y recalcula el área máxima
		
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

