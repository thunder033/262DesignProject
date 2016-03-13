package FPTS.Data;

/**
 * @author: Alexander Kidd
 * Created: 3/13/2016
 * Revised: 3/13/2016
 * Description: Responsible for validating
 * user login credentials and hashing passwords
 * as security features.
 */
public class Authenticator {

    public static String makeHash(String text){
        char[] chars = text.toCharArray();
        char[] shiftedChars = new char[chars.length];
        char shift = 4;
        for(int i = 0; i < chars.length; i++){
            char c = (char)(chars[chars.length - 1 - i] + shift);
            if(c > 'z') {
                shiftedChars[i] = (char)(chars[chars.length - 1 - i] - (26 - shift));
            }
            else {
                shiftedChars[i] = (char)(chars[chars.length - 1 - i] + shift);
            }
        }

        return String.valueOf(shiftedChars);
    }
}
