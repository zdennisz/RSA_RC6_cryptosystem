// AMOGH LALE
//alale1@binghamton.edu

	import java.util.Scanner;
	import java.io.BufferedReader;
	import java.io.BufferedWriter;
	import java.io.FileNotFoundException;
	import java.io.FileReader;
	import java.io.FileWriter;
	import java.io.IOException;

	public class AlgorithmRC6 {
		static int w = 32, r = 20;
		static int[] S;
		static int Pw = 0xb7e15163, Qw = 0x9e3779b9;
		public String encrypted_text;
		public String  decrypted_text;
		public String original_Text;
		
		// CODE TO CONVERT HEXADECIMAL NUMBERS IN STRING TO BYTE ARRAY
		public static byte[] hexStringToByteArray(String s) {
			int string_len = s.length();
			byte[] data = new byte[string_len / 2];
			for (int i = 0; i < string_len; i += 2) {
				data[i / 2] = (byte) ((Character.digit(s.charAt(i), 16) << 4) + Character
						.digit(s.charAt(i + 1), 16));
			}
			return data;
		}

		
		
		// CODE TO CONVERT BYTE ARRAY TO HEX FORMAT
		public static String byteArrayToHex(byte[] a) {
			StringBuilder sb = new StringBuilder(a.length * 2);
			for (byte b : a)
				sb.append(String.format("%02x", b & 0xff));
			return sb.toString();
		}
		
		
		
		// KEY SCHEDULING ALGORITHM
		public int[] KeySchedule(byte[] key) {

			
			int[] S = new int[2 * r + 4];
			S[0] = Pw;
			
			int c = key.length / (w/8);
			int[] L = bytestoWords(key,  c);
			
			for (int i = 1; i < (2 * r + 4); i++){
				S[i] = S[i - 1] + Qw;
			}
				
			int A,B,i,j;
			
			A=B=i=j=0;

			int v = 3 * Math.max(c, (2 * r + 4));

			for (int s = 0; s < v; s++) {
				A = S[i] = rotateLeft((S[i] + A + B), 3);
				B = L[j] = rotateLeft(L[j] + A + B, A + B);
				i = (i + 1) % (2 * r + 4);
				j = (j + 1) % c;

			}

			return S;
		}
		
		
		

		// ENCRYPTION ALGORITHM
		public byte[] encryption(byte[] keySchArray){
			
			
			int temp,t,u;
			
			int[] temp_data = new int[keySchArray.length/4];
			
			for(int i =0;i<temp_data.length;i++)
				temp_data[i] = 0;
		
			
			temp_data=convertBytetoInt(keySchArray,temp_data.length);
		
			
			int A,B,C,D;
			
			A=B=C=D=0;
			
			
			A = temp_data[0];
			B = temp_data[1];
			C = temp_data[2];
			D = temp_data[3];
			
			
			
			B = B + S[0];
			D = D + S[1];
			
			int lgw=5;
			
			byte[] outputArr = new byte[keySchArray.length];
			for(int i = 1;i<=r;i++){	
				
				t = rotateLeft(B*(2*B+1),lgw);			
				u = rotateLeft(D*(2*D+1),lgw);			
				A = rotateLeft(A^t,u)+S[2*i];			
				C = rotateLeft(C^u,t)+S[2*i+1];
				
				temp = A;
				A = B;
				B = C;
				C = D;
				D = temp;
			}
			
			A = A + S[2*r+2];
			C = C + S[2*r+3];
			
			temp_data[0] = A;
			temp_data[1] = B;
			temp_data[2] = C;
			temp_data[3] = D;
			
			
			outputArr = convertIntToByte(temp_data,keySchArray.length);
			
			
			return outputArr;
		}
		
		//DECRYPTION ALGORITHM
		public byte[] decryption(byte[] keySchArray){
			
			
			int temp,t,u;
	        int A,B,C,D;
			
			A=B=C=D=0;
			int[] temp_data_decryption = new int[keySchArray.length/4];
			
			for(int i =0;i<temp_data_decryption.length;i++)
				temp_data_decryption[i] = 0;
		
			
			temp_data_decryption=convertBytetoInt(keySchArray,temp_data_decryption.length);
		
			
			A = temp_data_decryption[0];
			B = temp_data_decryption[1];
			C = temp_data_decryption[2];
			D = temp_data_decryption[3];
			
			
			

			C = C - S[2*r+3];
			A = A - S[2*r+2];
			
			int lgw=5;
			
			byte[] outputArr = new byte[keySchArray.length];
			for(int i = r;i>=1;i--){	
				temp = D;
				  D = C;
				  C = B;
				  B = A;
				  A = temp;
					
				u = rotateLeft(D*(2*D+1),lgw);	
				t = rotateLeft(B*(2*B+1),lgw);			
				C= rotateRight(C-S[2*i+1],t)^u;	
				A= rotateRight(A-S[2*i], u)^t;
				
			}
			D=D-S[1];
			B=B-S[0];
			
			
			temp_data_decryption[0] = A;
			temp_data_decryption[1] = B;
			temp_data_decryption[2] = C;
			temp_data_decryption[3] = D;
			
			
			outputArr = convertIntToByte(temp_data_decryption,keySchArray.length);
			
			
			return outputArr;
		}
	    

		// CONVERT INT TO BYTE FORM
		public static byte[] convertIntToByte(int[] integerArray,int length){
		byte[]  int_to_byte=new byte[length];
		for(int i = 0;i<length;i++){
			int_to_byte[i] = (byte)((integerArray[i/4] >>> (i%4)*8) & 0xff);
		}
		
		return int_to_byte;
	}
		
		
		// CONVERT BYTE TO INT FORM
		private static int[] convertBytetoInt(byte[] arr,int length){
			int[]  byte_to_int=new int[length];
			for(int j=0; j<byte_to_int.length; j++)
			{
				byte_to_int[j] = 0;
			}
			
			int counter = 0;
			for(int i=0;i<byte_to_int.length;i++){
				byte_to_int[i] = ((arr[counter++]&0xff))|
							((arr[counter++]&0xff) << 8) |
							((arr[counter++]&0xff) << 16) |
							((arr[counter++]&0xff) << 24);
			}
			return byte_to_int;
			
		}
		
		
		
		// CONVERT BYTE TO WORDS
		private static int[] bytestoWords(byte[] userkey,int c) {
			int[] bytes_to_words = new int[c];
			for (int i = 0; i < bytes_to_words.length; i++)
				bytes_to_words[i] = 0;

			for (int i = 0, off = 0; i < c; i++)
				bytes_to_words[i] = ((userkey[off++] & 0xFF)) | ((userkey[off++] & 0xFF) << 8)
						| ((userkey[off++] & 0xFF) << 16) | ((userkey[off++] & 0xFF) << 24);
			
			return bytes_to_words;
		}
		
		
		// ROTATE LEFT METHOD
		private static int rotateLeft(int val, int pas) {
			return (val << pas) | (val >>> (32 - pas));
		}
		
		//ROTATE RIGHT METHOD
		private static int rotateRight(int val, int pas) {
			return (val >>> pas) | (val << (32-pas));
		}
		
		
		
		public void EncryptFunction() {
			String tempString;
			String given_text;	
			 String text_data;
			 String key_value;
			String []input_text_val;
			BufferedWriter output_to_text_file=null;
			Scanner sc=new Scanner(System.in);
			FileWriter output_file;
			try {
				output_file = new FileWriter("src\\output.txt",false);
				FileReader input_file=new FileReader("src\\input.txt");
				BufferedReader bf=new BufferedReader(input_file);

				given_text=bf.readLine();
				input_text_val=given_text.split(":");
			    text_data=input_text_val[1];
			    key_value=bf.readLine();
			    String []input_key_val=key_value.split(":");
			    tempString=input_key_val[1];
			    
			    original_Text=text_data;
			    tempString=tempString.replace(" ", "");
				
			    text_data=text_data.replace(" ", "");
				
				byte[] key=hexStringToByteArray(tempString);
				byte[] W=hexStringToByteArray(text_data);
				S = new AlgorithmRC6().KeySchedule(key);
					
				byte[] encrypt=new AlgorithmRC6().encryption(W);
				encrypted_text=byteArrayToHex(encrypt);
				encrypted_text = encrypted_text.replaceAll("..", "$0 ");
				output_to_text_file=new BufferedWriter(output_file);
				output_to_text_file.write("ciphertext: "+ encrypted_text);
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}finally
			{
			    if(output_to_text_file != null) {
			        try {
						output_to_text_file.close();
					} catch (IOException e) {
						System.out.println("File cannot be closed");
						e.printStackTrace();
					}
			    }
			}
			
		}

		public void DecryptionFunction() {
			String tempString;
			String given_text;	
			 String text_data;
			 String key_value;
			String []input_text_val;
			int total_lines;
			BufferedWriter output_to_text_file=null;
			Scanner sc=new Scanner(System.in);
			FileWriter output_file;
			try {
			output_file = new FileWriter("src\\output.txt",false);
			FileReader input_file=new FileReader("src\\input.txt");
			BufferedReader bf=new BufferedReader(input_file);
			given_text=bf.readLine();
			input_text_val=given_text.split(":");
		    text_data=this.encrypted_text;
		    key_value=bf.readLine();
		    String []input_key_val=key_value.split(":");
		    tempString=input_key_val[1]; 
		    tempString=tempString.replace(" ", "");
			text_data=text_data.replace(" ", "");
			byte[] key2=hexStringToByteArray(tempString);
			byte[] X=hexStringToByteArray(text_data);
			S = new AlgorithmRC6().KeySchedule(key2);
			byte[] decrypt=new AlgorithmRC6().decryption(X);
			 decrypted_text=byteArrayToHex(decrypt);
			decrypted_text = decrypted_text.replaceAll("..", "$0 "); 
			output_to_text_file=new BufferedWriter(output_file);
			output_to_text_file.write("plaintext: "+ decrypted_text);
			}catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}finally
			{
			    if(output_to_text_file != null) {
			        try {
						output_to_text_file.close();
					} catch (IOException e) {
						System.out.println("File cannot be closed");
						e.printStackTrace();
					}
			    }
			}
		
		}
		

		
	}

