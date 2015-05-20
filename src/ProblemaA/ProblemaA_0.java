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
			resp = propuesta(foto);
			System.out.println(resp[0] + " " + resp[1]);
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
				//Retorna el máximo
				rmax = Math.max(rmax, rmaxij);
				cmax = Math.max(cmax, cmaxij);
			}
		}
		int [] resp = new int [2];
		resp[0] = cmax;
		resp[1] = rmax;
		return resp;
	}
	
	//Solución propuesta
	public static int[] propuesta(boolean [][] foto){
		int cmax = 0;
		int rmax = 0;
		int m = foto.length;
		int n = foto[0].length;
		boolean nuevo = true;
		int [][] maxHorizontal = new int [m][n];
		int [][] maxVertical = new int [m][n];
		for(int i=0; i != m; i++){
			for (int j=0; j != n; j++){
				int maximo = 0;
				if (foto[i][j]){
					nuevo = !(i > 0 || j > 0);
					if (!nuevo){
						
						maxHorizontal[i][j] = (j > 0 && maxHorizontal[i][j - 1] != 0)?(maxVertical[i][j - 1] - 1):1;
						if (i > 0){
							if (maxVertical[i - 1][j] != 0)
								maxVertical[i][j] = maxVertical[i - 1][j] - 1;
							else
								nuevo = true;
						}
						if (j > 0){
							if (maxHorizontal[i][j - 1] != 0)
								maxHorizontal[i][j] = maxVertical[i][j - 1] - 1;
							else
								nuevo = true;
						}
					}
					if (nuevo){
						int suma = 1;
						for (int k = i; k != m; k++){
							if(foto[k][j])
								suma++;
							else
								k = m;
						}
						maxVertical[i][j] = suma;
						suma = 1;
						for (int k = j; k != n; k++){
							if(foto[i][k])
								suma++;
							else
								k = n;
						}
						maxHorizontal[i][j] = suma;
					}
				}
			}
		}
		int [] resp = new int [2];
		resp[0] = cmax;
		resp[1] = rmax;
		return resp;
	}
}