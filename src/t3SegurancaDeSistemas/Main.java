package t3SegurancaDeSistemas;

import java.io.FileWriter;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.math.BigInteger;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Arrays;
import java.util.Base64;

import javax.crypto.Cipher;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;

public class Main {
	public static byte[] S;
	public static byte[] IV;
	public static byte[] mensagem;

	public static void main(String[] args) throws Exception {
		System.out.println();
		System.out.println("///////////////////////////////////////");
		System.out.println("ETAPA 1");

		// definicao de P
		String P = "B10B8F96A080E01DDE92DE5EAE5D54EC52C99FBCFB06A3C6";
		P = P + "9A6A9DCA52D23B616073E28675A23D189838EF1E2EE652C0";
		P = P + "13ECB4AEA906112324975C3CD49B83BFACCBDD7D90C4BD70";
		P = P + "98488E9C219A73724EFFD6FAE5644738FAA31A4FF55BCCC0";
		P = P + "A151AF5F0DC8B4BD45BF37DF365C1A65E68CFDA76D4DA708";
		P = P + "DF1FB2BC2E4A4371";

		// Definicao de G
		String G = "A4D1CBD5C3FD34126765A442EFB99905F8104DD258AC507F";
		G = G + "D6406CFF14266D31266FEA1E5C41564B777E690F5504F213";
		G = G + "160217B4B01B886A5E91547F9E2749F4D7FBD7D3B9A92EE1";
		G = G + "909D0D2263F80A76A6A24C087A091F531DBF0A0169B6A28A";
		G = G + "D662A4D18E73AFA32D779D5918D08BC8858F4DCEF97C2A24";
		G = G + "855E6EEB22B3B2E5";

		// definicao de a
		int a = 15;

		// PASSO 1
		System.out.println();
		System.out.println("PASSO 1");
		String A = passo1(G, a, P);

		// escreve o resultado (em hexa) em um TXT
		// (numero pode ser grande demais para o terminal/console printar)
		String nomeDoTxt = "resultDeA";
		gravaResult(A, nomeDoTxt);

		// valor de B recebido pelo professor
		String B = "009268E85A545AC037B3FA82BD59E3700AF1FB1F50BAEED9F3CA16322593E796DCDE9F2FC96EAE9B62264170CCAE74863CAA240C37CA40BEAC3C403E1158E395AD920CB6B3F2C94A69EF548A9B378EE500EFBE5AEDAAE039DDAC0B0D0F7BDC3C0307D2FA9DC8234FC8ECB90FCD4DEAA20672575C9D64C6F5F3F56A8591CE1CC5D5";

		// PASSO 2
		System.out.println();
		System.out.println("PASSO 2");
		BigInteger V = passo2(B, a, P);

		// escreve o resultado (em hexa) em um TXT
		// (numero pode ser grande demais para o terminal/console printar)
		nomeDoTxt = "resultDeV";
		gravaResult(V.toString(), nomeDoTxt);

		// PASSO 3
		System.out.println();
		System.out.println("PASSO 3");
		byte[] S = passo3(V);

		System.out.println("///////////////////////////////////////");
		System.out.println("ETAPA 2");
		System.out.println();

		// Receber uma mensagem do professor (em hexadecimal)
		// cifrada com o AES no modo de operação CBC, e padding
		// Formato da mensagem recebida: [128 bits com IV][mensagem] – em hexadecimal
		String mensagemDoProfessor = "204C34209F8176DD45982714FAE2F0D6A2F32B9E881CA412996755E726C442CB693EBA83690384273C53E30CA1158B5B644585E5E83CA9B12DB2E6D527C891ECF27BA9E7AB7C006C9E773CA8115FFCE5A72B4B7EA3C57C90C7D02FD2EC64906B5CE2D74365E8E867298E49E692C36DB7";

		System.out.println("mensagemDoProfessor: " + mensagemDoProfessor);
		System.out.println();

		// separacao dos artefatos
		// obtem os valores para o cipher
		separacao(mensagemDoProfessor, S);

		// Decifrar a mensagem (ja passando a mensagem em byte) e
		String mensagem = decifra();
		System.out.println("mensagem obtida: ");
		System.out.println(mensagem);

		// inverte ela

		// passa para hexa

		// cifra ela para enviar

	}

	private static String decifra() {
		try {
			// IV
			IvParameterSpec iv = new IvParameterSpec(IV);
			// CHAVE
			SecretKeySpec skeySpec = new SecretKeySpec(S, "AES");
			// define o cipher
			Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5PADDING");
			cipher.init(Cipher.DECRYPT_MODE, skeySpec, iv);
			// descriptografa
			byte[] mensagemEmTextoPlano = cipher.doFinal(mensagem);
			// retorna a mensagem em base64
			return new String(Base64.getEncoder().encodeToString(mensagemEmTextoPlano));
			// return new String(mensagemEmTextoPlano, "UTF-8");
			// return new String(mensagemEmTextoPlano);
		} catch (Exception ex) {
			ex.printStackTrace();
		}
		// caso ocorrer algum erro
		return null;
	}

	private static void separacao(String mensagemDoProfessor, byte[] Scompleto) throws UnsupportedEncodingException {
		// PRIMEIRO: obtencao do S
		// pega os primeiros 128 bits do SHA para obter o S
		System.out.println("S");
		S = Arrays.copyOfRange(Scompleto, 0, 16);

		for (int i = 0; i < S.length; i++) {
			//System.out.println(S[i]);
		}

		System.out.println(Base64.getEncoder().encodeToString(S));
		System.out.println(ByteArrayToString(S));

		// de string (hex) para byte array
		//byte[] mensagemBytes = new BigInteger(mensagemDoProfessor,16).toByteArray();
		byte[] mensagemBytes = mensagemDoProfessor.getBytes("UTF-8");

		// SEGUNDO: obtencao do IV
		// separa os primeiros 128 bits
		System.out.println("IV");
		IV = Arrays.copyOfRange(mensagemBytes, 0, 16);

		for (int i = 0; i < IV.length; i++) {
			//System.out.print(IV[i]);
		}
		
		System.out.println(ByteArrayToString(IV));

		// TERCEIRO: obtencao da mensagem
		// bits apos os primeiros 128
		System.out.println("mensagem");
		mensagem = Arrays.copyOfRange(mensagemBytes, 16, mensagemBytes.length);
		System.out.println(ByteArrayToString(mensagem));
	}

	private static byte[] passo3(BigInteger v) throws NoSuchAlgorithmException, UnsupportedEncodingException {
		// Passo 3: calcular S = SHA256(V) e
		// usar os primeiros 128 bits como senha para se comunicar com o professor.
		MessageDigest digest = MessageDigest.getInstance("SHA-256");
		digest.update(v.toByteArray());
		byte[] digested = digest.digest();

		System.out.println("byte array (S): ");
		for (int i = 0; i < digested.length; i++) {
			System.out.printf(digested[i] + " ");
		}
		System.out.println();
		System.out.println();

		return digested;
	}

	private static BigInteger passo2(String B, int a, String P) {
		// Passo 2: receber um valor B (em hexadecimal) do professo
		// e calcular V = Ba mod p

		// de hexa para decimal
		BigInteger newB = new BigInteger(B, 16);
		BigInteger newP = new BigInteger(P, 16);

		// b elevado ao a
		newB = newB.pow(a);

		// resutlado mod de p
		newB = newB.remainder(newP);

		// retorno do resultado
		return newB;
	}

	private static String passo1(String G, int a, String P) {
		// Passo 1: gerar um valor a menor que p (dado) e calcular A = ga mod p.
		// Enviar o valor de A (em hexadecimal) para o professor.

		// de hexa para decimal
		BigInteger newG = new BigInteger(G, 16);
		BigInteger newP = new BigInteger(P, 16);

		// g elevado ao a
		newG = newG.pow(a);

		// resutlado mod de p
		newG = newG.remainder(newP);

		// retorno do resultado
		return newG.toString(16);
	}

	private static void gravaResult(String texto, String nomeDoTxt) {
		System.out.println(nomeDoTxt + ": " + texto);
		nomeDoTxt = nomeDoTxt + ".txt";
		try {
			FileWriter myWriter = new FileWriter(nomeDoTxt);
			myWriter.write(texto);
			myWriter.close();
			System.out.println("Successfully wrote to the file " + nomeDoTxt);
		} catch (IOException e) {
			System.out.println("An error occurred.");
			e.printStackTrace();
		}

	}

	public static String ByteArrayToString(byte[] ba) {
		StringBuilder sb = new StringBuilder();
		for (byte b : ba) {
			sb.append(String.format("%02X ", b));
		}
		return sb.toString();
	}
}
