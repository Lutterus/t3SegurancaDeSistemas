package t3SegurancaDeSistemas;

import java.io.FileWriter;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.math.BigInteger;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
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

		// PASSO 1 - calcula o A
		String A = passo1(G, a, P);

		// escreve o resultado (em hexa) em um TXT
		// (numero pode ser grande demais para o terminal/console printar)
		String nomeDoTxt = "resultDeA";
		System.out.println("Resultado de A:");
		gravaResult(A, nomeDoTxt);
		System.out.println();

		// valor de B recebido pelo professor
		String B = "009268E85A545AC037B3FA82BD59E3700AF1FB1F50BAEED9F3CA16322593E796DCDE9F2FC96EAE9B62264170CCAE74863CAA240C37CA40BEAC3C403E1158E395AD920CB6B3F2C94A69EF548A9B378EE500EFBE5AEDAAE039DDAC0B0D0F7BDC3C0307D2FA9DC8234FC8ECB90FCD4DEAA20672575C9D64C6F5F3F56A8591CE1CC5D5";

		// PASSO 2 - calculo o V
		BigInteger V = passo2(B, a, P);

		// escreve o resultado (em hexa) em um TXT
		// (numero pode ser grande demais para o terminal/console printar)
		nomeDoTxt = "resultDeV";
		System.out.println("Resultado de B:");
		gravaResult(V.toString(), nomeDoTxt);
		System.out.println();

		// PASSO 3 - Calcula o S
		byte[] sCompleto = passo3(V);

		System.out.println("///////////////////////////////////////");
		System.out.println("ETAPA 2");

		// Receber uma mensagem do professor (em hexadecimal)
		// cifrada com o AES no modo de operação CBC, e padding
		// Formato da mensagem recebida: [128 bits com IV][mensagem] – em hexadecimal
		String mensagemDoProfessor = "204C34209F8176DD45982714FAE2F0D6A2F32B9E881CA412996755E726C442CB693EBA83690384273C53E30CA1158B5B644585E5E83CA9B12DB2E6D527C891ECF27BA9E7AB7C006C9E773CA8115FFCE5A72B4B7EA3C57C90C7D02FD2EC64906B5CE2D74365E8E867298E49E692C36DB7";
		System.out.println("mensagemDoProfessor: " + mensagemDoProfessor);
		System.out.println();

		// separacao dos artefatos
		// obtem os valores para o cipher, obtem o S, o IV e MENSAGEM
		separacao(mensagemDoProfessor, sCompleto);

		// Decifrar a mensagem (ja passando a mensagem em byte) e
		byte[] mensagemDescifrada = decifra();
		System.out.println("mensagem em texto: ");
		System.out.println(new String(mensagemDescifrada, StandardCharsets.UTF_8));
		System.out.println("mensagem descriptografada em Base64: ");
		System.out.println(Base64.getEncoder().encodeToString(mensagemDescifrada));
		System.out.println("mensagem descriptografada em hex:");
		System.out.println(byteArrayToHexString(mensagemDescifrada));
		System.out.println();

		// inverte ela
		// String novaMensagem = new String(mensagemDescifrada, StandardCharsets.UTF_8);
		String novaMensagem = "teste";
		novaMensagem = invercao(novaMensagem);
		System.out.println("mensagem invertida: ");
		System.out.println(novaMensagem);
		System.out.println();

		// passa para hexa
		novaMensagem = byteArrayToHexString(novaMensagem.getBytes());
		System.out.println("mensagem em hexa: ");
		System.out.println(novaMensagem);
		System.out.println();

		// gera um novo IV aleatorio
		geraIv();
		S = IV;

		// cifra ela para enviar
		byte[] novaMensagembytes = cifra(novaMensagem);
		System.out.println("mensagem criptografada em Base64: ");
		System.out.println(Base64.getEncoder().encodeToString(novaMensagembytes));
		System.out.println("mensagem criptografada em hex:");
		System.out.println(byteArrayToHexString(novaMensagembytes));
		System.out.println();

	}

	private static byte[] cifra(String mensagemParaCriptografar) {
		try {
			// IV
			IvParameterSpec ivParameterSpec = new IvParameterSpec(IV);
			// CHAVE
			SecretKeySpec secretKeySpec = new SecretKeySpec(S, "AES");
			// DEFINE O CIPHER
			Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
			// EXECUTA O CIPHER
			cipher.init(Cipher.ENCRYPT_MODE, secretKeySpec, ivParameterSpec);
			// RETORNA DESCRIPTOGRAFADA
			return cipher.doFinal(mensagemParaCriptografar.getBytes());
		} catch (Exception ex) {
			ex.printStackTrace();
			// Operation failed
		}
		return null;
	}

	private static void geraIv() {
		try {
			// metodo de geracao de IV recomendado no GIT
			// https://gist.github.com/demisang/716250080d77a7f65e66f4e813e5a636
			SecureRandom secureRandom = new SecureRandom();
			byte[] initVectorBytes = new byte[IV.length / 2];
			secureRandom.nextBytes(initVectorBytes);
			String initVector = byteArrayToHexString(initVectorBytes);
			initVectorBytes = initVector.getBytes("UTF-8");
			IV = initVectorBytes;

			System.out.println("novo IV gerado");
			System.out.println("IV (em hexa): " + initVector);
			System.out.println();
		} catch (Exception ex) {
			ex.printStackTrace();
		}
	}

	private static String invercao(String mensagemParaInverter) {
		String reversed = "";
		for (int i = mensagemParaInverter.length() - 1; i >= 0; i--) {
			reversed = reversed + mensagemParaInverter.charAt(i);
		}
		return reversed;
	}

	private static byte[] decifra() {
		try {
			// IV
			IvParameterSpec iv = new IvParameterSpec(IV);
			// CHAVE
			SecretKeySpec secretKeySpec = new SecretKeySpec(S, "AES");
			// DEFINE O CIPHER
			Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5PADDING");
			// EXECUTA O CIPHER
			cipher.init(Cipher.DECRYPT_MODE, secretKeySpec, iv);
			// RETORNA DESCRIPTOGRAFADA
			return cipher.doFinal(mensagem);
		} catch (Exception ex) {
			ex.printStackTrace();
		}
		// caso ocorrer algum erro
		return null;
	}

	private static void separacao(String mensagemDoProfessor, byte[] Scompleto) throws UnsupportedEncodingException {
		System.out.println("dados obtidos da separacao (IV e mensagem em hexa): ");
		// PRIMEIRO: obtencao do S
		// pega os primeiros 128 bits do SHA para obter o S
		S = Arrays.copyOf(Scompleto, 16);
		System.out.println("S em hexa: " + byteArrayToHexString(S));
		System.out.println("S em Base64: " + Base64.getEncoder().encodeToString(S));
		System.out.println("S length: " + S.length);

		// de string (hex) para byte array
		byte[] mensagemBytes = mensagemDoProfessor.getBytes();

		// SEGUNDO: obtencao do IV
		// separa os primeiros 128 bits
		IV = Arrays.copyOf(mensagemBytes, 16);
		System.out.println("IV: " + byteArrayToHexString(IV));

		// TERCEIRO: obtencao da mensagem
		// bits apos os primeiros 128
		mensagem = Arrays.copyOfRange(mensagemBytes, 16, mensagemBytes.length);
		System.out.println("Mensagem: " + byteArrayToHexString(mensagem));
		System.out.println();		
	}

	private static byte[] passo3(BigInteger v) throws NoSuchAlgorithmException, UnsupportedEncodingException {
		// Passo 3: calcular S = SHA256(V) e
		// usar os primeiros 128 bits como senha para se comunicar com o professor
		MessageDigest digest = MessageDigest.getInstance("SHA-256");
		digest.update(v.toByteArray());
		return digest.digest();
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
		System.out.println(texto);
		nomeDoTxt = nomeDoTxt + ".txt";
		try {
			FileWriter myWriter = new FileWriter(nomeDoTxt);
			myWriter.write(texto);
			myWriter.close();
		} catch (IOException e) {
			System.out.println("An error occurred.");
			e.printStackTrace();
		}

	}

	public static String byteArrayToHexString(byte[] encrypted) {
		final char[] hexArray = { '0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'A', 'B', 'C', 'D', 'E', 'F' };
		char[] hexChars = new char[encrypted.length * 2]; // Each byte has two hex characters (nibbles)
		int v;
		for (int j = 0; j < encrypted.length; j++) {
			v = encrypted[j] & 0xFF; // Cast bytes[j] to int, treating as unsigned value
			hexChars[j * 2] = hexArray[v >>> 4]; // Select hex character from upper nibble
			hexChars[j * 2 + 1] = hexArray[v & 0x0F]; // Select hex character from lower nibble
		}
		return new String(hexChars);
	}
}
