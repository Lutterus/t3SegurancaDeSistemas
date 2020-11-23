package t3SegurancaDeSistemas;

import java.io.BufferedReader;
import java.io.FileWriter;
import java.io.IOException;
import java.math.BigInteger;

public class Main {
	public static void main(String[] args) {
		//definicao de P
		String P = "B10B8F96A080E01DDE92DE5EAE5D54EC52C99FBCFB06A3C6";
		P = P + "9A6A9DCA52D23B616073E28675A23D189838EF1E2EE652C0";
		P = P + "13ECB4AEA906112324975C3CD49B83BFACCBDD7D90C4BD70";
		P = P + "98488E9C219A73724EFFD6FAE5644738FAA31A4FF55BCCC0";
		P = P + "A151AF5F0DC8B4BD45BF37DF365C1A65E68CFDA76D4DA708";
		P = P + "DF1FB2BC2E4A4371";
		
		//Definicao de G
		String G = "A4D1CBD5C3FD34126765A442EFB99905F8104DD258AC507F";
		G = G + "D6406CFF14266D31266FEA1E5C41564B777E690F5504F213";
		G = G + "160217B4B01B886A5E91547F9E2749F4D7FBD7D3B9A92EE1";
		G = G + "909D0D2263F80A76A6A24C087A091F531DBF0A0169B6A28A";
		G = G + "D662A4D18E73AFA32D779D5918D08BC8858F4DCEF97C2A24";
		G = G + "855E6EEB22B3B2E5";
		
		//definicao de a
		int a = 15;
		
		//resultado do passo 1
		String A = passo1(G, a, P);
		
		//print do resultado
		System.out.println(A);
		
		//escreve o numero em um TXT 
		//(numero pode ser grande demais para o terminal/console printar)
		String nomeDoTxt = "resultDeA";
		gravaResult(A, nomeDoTxt);
		
		
		
		//valor de B recebido pelo professor
		String B = "009268E85A545AC037B3FA82BD59E3700AF1FB1F50BAEED9F3CA16322593E796DCDE9F2FC96EAE9B62264170CCAE74863CAA240C37CA40BEAC3C403E1158E395AD920CB6B3F2C94A69EF548A9B378EE500EFBE5AEDAAE039DDAC0B0D0F7BDC3C0307D2FA9DC8234FC8ECB90FCD4DEAA20672575C9D64C6F5F3F56A8591CE1CC5D5";
		
		//resultado do passo 2
		String V = passo2(B, a, P);
		nomeDoTxt = "resultDeV";
		gravaResult(V, nomeDoTxt);
		
		

	}

	private static String passo2(String B, int a, String P) {
		//Passo 2: receber um valor B (em hexadecimal) do professo 
				//e calcular V = Ba mod p
				
				//de hexa para decimal		
				BigInteger newB = new BigInteger(B, 16);
				BigInteger newP = new BigInteger(P, 16);
				
				//b elevado ao a		
				newB = newB.pow(a);
				
				//resutlado mod de p		
				newB = newB.remainder(newP);
				
				//retorno do resultado
				return newB.toString(16);
	}

	private static String passo1(String G, int a, String P ) {
		//Passo 1: gerar um valor a menor que p (dado) e calcular A = ga mod p. 
		//Enviar o valor de A (em hexadecimal) para o professor.
		
		//de hexa para decimal		
		BigInteger newG = new BigInteger(G, 16);
		BigInteger newP = new BigInteger(P, 16);
		
		//g elevado ao a		
		newG = newG.pow(a);
		
		//resutlado mod de p		
		newG = newG.remainder(newP);
		
		//retorno do resultado
		return newG.toString(16);
	}
	
	private static void gravaResult(String texto, String nomeDoTxt) {
		nomeDoTxt = nomeDoTxt+".txt";
		try {
		      FileWriter myWriter = new FileWriter(nomeDoTxt);
		      myWriter.write(texto);
		      myWriter.close();
		      System.out.println("Successfully wrote to the file.");
		    } catch (IOException e) {
		      System.out.println("An error occurred.");
		      e.printStackTrace();
		    }
		
	}

}
