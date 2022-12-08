import java.util.List;
import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class Tools {

    //Attributes
    private final static List<Character> lowerCaseAlphabets = List.of('a', 'b', 'c', 'd', 'e', 'f', 'g', 'h', 'i', 'j', 'k', 'l', 'm', 'n', 'o', 'p', 'q', 'r', 's', 't', 'u', 'v', 'w', 'x', 'y', 'z');
    private final static List<Character> upperCaseAlphabets = List.of('A', 'B', 'C', 'D', 'E', 'F', 'G', 'H', 'I', 'J', 'K', 'L', 'M', 'N', 'O', 'P', 'Q', 'R', 'S', 'T', 'U', 'V', 'W', 'X', 'Y', 'Z');
    
    //Methods
    public static String findNextStr(String str){

        StringBuilder result = new StringBuilder();

        if(str.matches("[Z]+")) return result.toString();
        int[] arr = new int[str.length()];
        for(int i = 0; i < str.length(); i++) {
            arr[i] = findNumberOfACharacter(str.charAt(i));
        }
        int index = arr.length-1;
        arr[index] += 1;
        while(index > 0) {
            if(arr[index] == 52) {
                arr[index] %= 52;
                arr[index-1] += 1;
                index--;
            }
            else {
                break;
            }
        }
        for (int value : arr) {
            result.append(findCharcterByItsNumber(value));
        }
        return result.toString();
    }

    public static int findNumberOfACharacter(char c){
        //If the char c is a lower case alphabet
        if(Tools.lowerCaseAlphabets.contains(c)) {
            return c - 'a';
        }
        //If the char c is a upper case alphabet
        if(Tools.upperCaseAlphabets.contains(c)){
            return c - 'A' + 26;
        }

        //If not, return -1
        return -1;
    }

    public static char findCharcterByItsNumber(int n){
        if(n >= 0 && n <= 25) {
            return (char)('a' + n);
        }
        else{
            return (char)('A'+n-26);
        }
    }

    public static String encrypt(String input) {
        try {
          MessageDigest md = MessageDigest.getInstance("MD5");
          byte[] messageDigest = md.digest(input.getBytes());
          BigInteger no = new BigInteger(1, messageDigest);
          String hashtext = no.toString(16);
          while (hashtext.length() < 32) {
            hashtext = "0" + hashtext;
          }
          return hashtext;
        } catch (NoSuchAlgorithmException e) {
          throw new RuntimeException(e);
        }
      }
}
