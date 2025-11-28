package util;

import java.security.MessageDigest;

public class GestorHash {
	
	public GestorHash() {};
	
	public String gerarHashSHA256(String senha) throws Exception {
		
        MessageDigest digest = MessageDigest.getInstance("SHA-256");
        byte[] hashBytes = digest.digest(senha.getBytes("UTF-8"));
        
        // Converte bytes para String em hexadecimal
        StringBuilder hexString = new StringBuilder();
        for (byte b : hashBytes) {
        	
            String hex = String.format("%02x", b);
            hexString.append(hex);
            
        }
        
        return hexString.toString();
        
    }

}
