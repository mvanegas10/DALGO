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
import java.util.Stack;

public class ProblemaA_0 {

	public static void main(String[] args) throws Exception {
		BufferedReader br = new BufferedReader(new InputStreamReader(new FileInputStream("src/ProblemaA/Prueba.in")));
		String lineain,data[];
		int M, N;
		
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
			
			int [] prueba = new int [7];
			prueba[0] = 10;
			prueba[1] = 40;
			prueba[2] = 30;
			prueba[3] = 70;
			prueba[4] = 10;
			prueba[5] = 30;
			prueba[6] = 60;
			
			System.out.println(rectanguloMaximo(prueba));
			
			
//			resp = propuestaTres(foto);
//
//			System.out.println(resp[0] + " " + resp[1]);
		}
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
	
	public static int[] propuestaTres(boolean [][] foto){
		int cmax = 0;
		int rmax = 0;
		int m = foto.length;
		int n = foto[0].length;
		int i = m - 1;
		int j = n - 1;
		boolean nuevo = true;
		int [][] matriz = new int [m][n];
		int [][] maxVertical = new int [m][n];
		while (i >= 0) {
			while (j >= 0) {
				if (foto[i][j]){
					if ( i != m - 1 && j != n - 1){
						
						//Matriz para solución de cuadrados
						
						int uno = matriz[i+1][j];
						int dos = matriz[i][j+1];
						int tres = matriz[i+1][j+1];
						matriz[i][j] = 1 + Math.min((Integer) Math.min(uno, dos), tres);
						
					}
					else
						matriz[i][j] = 1;
					
					//Matriz para solución de rectángulos
					
					if (i < m - 1){
						if (maxVertical[i + 1][j] > 0) maxVertical[i][j] = maxVertical[i + 1][j] - 1;
						else nuevo = true;
					}
					
					if (nuevo){
						int k = i;
						int suma = 0;
						while (k >= 0) {
							if(foto[k][j]) suma++;
							else k = 0;
							k--;
						}
						maxVertical[i][j] = suma;
					}
				}
				else{
					matriz[i][j] = 0;
					maxVertical[i][j] = 0;
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
	
	public static int rectanguloMaximo(int [] arreglo){
		int maxArea = 0;
		Stack<Integer> stk = new Stack<Integer>();
		stk.push(0);
		for (int i = 1; i < arreglo.length; i++) {
			int actual = arreglo[i];
			
			while(!stk.empty()){
				int tope = stk.peek();
				
				if (actual > arreglo[tope]){
					stk.push(i);
					break;
				}
				
				tope = stk.pop();
				
				int izquierdo = (stk.empty())? 0: stk.peek();
				int derecho = (arreglo[tope] == actual)? i: i - 1;
				
				int area = (derecho - izquierdo) * arreglo[tope];
				maxArea = (area > maxArea)? area: maxArea;
			}
			
			if (stk.empty()) stk.push(i);
			}
		
		int derecho = arreglo.length;
		while( !stk.empty() ){
			int tope = stk.pop();
			int izquierdo = (stk.empty())? 0: stk.peek();
			int area = (derecho - izquierdo) * arreglo[tope];
			maxArea = (area > maxArea)? area: maxArea;
		}
		return maxArea;
	}
	
	//Solución propuesta
	public static int[] propuesta(boolean [][] foto){
		int iVer = 0;
		int jVer = 0;
		int iHor = 0;
		int jHor = 0;
		int m = foto.length;
		int n = foto[0].length;
		boolean nuevo = true;
		int [][] maxHorizontal = new int [m][n];
		int [][] maxVertical = new int [m][n];
		for(int i=0; i != m; i++){
			for (int j=0; j != n; j++){
				if (foto[i][j]){
					nuevo = !(i > 0 || j > 0);
					if (!nuevo){
						
						if (i > 0){
							if (maxVertical[i - 1][j] > 0)
								maxVertical[i][j] = maxVertical[i - 1][j] - 1;
							else
								nuevo = true;
						}
						if (j > 0){
							if (maxHorizontal[i][j - 1] > 0)
								maxHorizontal[i][j] = maxHorizontal[i][j - 1] - 1;
							else
								nuevo = true;
						}
					}
					if (nuevo){
						int suma = 0;
						for (int k = i; k != m; k++){
							if(foto[k][j])
								suma++;
							else
								k = m-1;
						}
						maxVertical[i][j] = suma;
						if(suma > maxVertical[iVer][jVer]){
							maxVertical[iVer][jVer] = suma;
							iVer = i;
							jVer = j;
						}
						suma = 0;
						for (int k = j; k != n; k++){
							if(foto[i][k])
								suma++;
							else
								k = n-1;
						}
						maxHorizontal[i][j] = suma;
						if(suma > maxHorizontal[iHor][jHor]){
							maxHorizontal[iHor][jHor] = suma;
							iHor = i;
							jHor = j;
						}
					}
				}
			}
		}
		if (maxHorizontal[iHor][jHor] > maxVertical[iVer][jVer]){
			
		}
		int [] resp = new int [2];
		return resp;
	}
	
	
	public static int[] propuestaDos(boolean [][] foto){
		
		int m = foto.length;
		int n = foto[0].length;
		int i = 0;
		int j = 0;
		int iInicial = 0;
		int jInicial = 0;
		int iFinal = iInicial;
		int jFinal = - 1;
		int maximo = 0;
		int cuenta = 0;
		int stop = 0;
		while (i < iFinal + 1){
			if (foto[i][j]){
				cuenta++;
			
				if (j > jInicial ){
					i ++;
					j --;
				}
				else{
					j = i + 1;
					i = 0;
					jFinal = j;
					iFinal ++;
				}	
			}
			else{
				 stop --;
				 if (stop == - 2){
					 maximo = Math.max(cuenta, maximo);
					 stop = 0;
					 iFinal = iInicial;
					 jFinal = jInicial;
					 if (j > jInicial){
						jInicial = iFinal + 1;
						iInicial = 0;
					 }
					 else {
						iInicial = i ++;
						jInicial = j --;
					 }
				 }
				 if (j > jInicial){
					j = iFinal + 1;
					i = 0;
				 }
				 else {
					i = i ++;
					j = j --;
				 }
			}
		}
		int [] resp = new int [2];
		return resp;
	}
	
	
}

