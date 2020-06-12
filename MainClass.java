import java.util.ArrayList;
import java.util.Arrays;

public class MainClass {

	public static void main(String[] args) {
		
		
		/////....read from file and encrypt......///// 
		int sizeOfMessage;
		AlgorithmRC6 rc6=new AlgorithmRC6();
		rc6.EncryptFunction();
		/////....read from file and encrypt....../////
		
		
		
		
		/////....take message and go via hash ......///// 
		int hashCodeOfMEssage;
		hashCodeOfMEssage=rc6.getOriginal_Text().hashCode();
		/////....take message and go via hash ....../////
		
		
		
		
		/////.... and encrypt via RSA....../////
		RSA rsa=new RSA();
		byte[] hasRsaEncrypted = rsa.encrypt((Integer.toString(hashCodeOfMEssage)).getBytes());
		/////.... and encrypt via RSA....../////
		
		
		
		
		
		/////.... Concatenate the result....../////
		ArrayList<Byte> bytesToSendQuietlly =new ArrayList<Byte>();
		//First insert the rc6 encrypted text
		byte[] encryptedText= rc6.getEncrypted_text().getBytes();
		for(byte info:encryptedText) {
			bytesToSendQuietlly.add(info);
		}
		sizeOfMessage=bytesToSendQuietlly.size();
		
		for(byte info:hasRsaEncrypted ) {
			bytesToSendQuietlly.add(info);
		}
		
		
		
		
		
		
		
		
		
		/////.... send via internet....../////
		
		
		
		
		
		
		
		
		/////.... seperate them both....../////
		ArrayList<Byte> encryptedMessage = new ArrayList<Byte>();
		int i=0;
		while(sizeOfMessage>0) {
			encryptedMessage.add(bytesToSendQuietlly.get(i));
			sizeOfMessage=sizeOfMessage-1;
			i++;
		}
		
		ArrayList<Byte> encryptedHash= new ArrayList<Byte>();;
		while(i<bytesToSendQuietlly.size()) {
			encryptedHash.add(bytesToSendQuietlly.get(i));
			i++;
		}
		
		
		
		
		
		
		
		//decrypt message and go via hash
		rc6.DecryptionFunction();
		int hashCodeToCompare;
		String decryptedText=rc6.getDecrypted_text().substring(0,rc6.getDecrypted_text().length()-1);
		hashCodeToCompare=decryptedText.hashCode();
		
		
		
		
		
		
		
		//decrypt the hash value ->Compare with the prevoius result if the same we have the result
		byte[] array =new byte[encryptedHash.size()]; 
		int j=0;
		while(j<encryptedHash.size()) {
			array[j]=encryptedHash.get(j);
			j++;
		}
		byte[] decrypted = rsa.decrypt(array);
		String result=new String(decrypted);
		
		if(hashCodeToCompare==Integer.parseInt(result)) {
			System.out.println("The Message is Ok\n");
			System.out.println("The hash value from Message is "+hashCodeToCompare+". The hash value from what was sent is "+result);
		}else {
			System.out.println("The Message was tempered");
		}
		
	}

}
