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

    /**
     * Creates an encrypted hash of the given text
     * @param text text to encrypt
     * @return encrypted text
     */
    public static String makeHash(String text){
        //Were going to make a reverse shift-4 Caesar cypher
        //get all the characters in the string
        char[] chars = text.toCharArray();
        //create an array to hold the result
        char[] shiftedChars = new char[chars.length];
        char shift = 4; //define the shift
        for(int i = 0; i < chars.length; i++){
            //get the shifted char, starting at the end
            char c = (char)(chars[chars.length - 1 - i] + shift);
            if(c > 'z') {
                //loop around if we got greater than a Z
                shiftedChars[i] = (char)(chars[chars.length - 1 - i] - (26 - shift));
            }
            else {
                //otherwise use the shift char
                shiftedChars[i] = (char)(chars[chars.length - 1 - i] + shift);
            }
        }
        //convert back to a string
        return String.valueOf(shiftedChars);
    }
}
