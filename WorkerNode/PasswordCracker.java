import java.util.List;

public class PasswordCracker {

    //Methods
    public static String findPassword(List<String[]> list, String target){
        int count = 0;
        for (String[] pair : list) {
            if (pair.length != 2) return null;
            String begin = pair[0];
            String end = pair[1];
            String str = begin;
            String md5Str = Tools.encrypt(str);
            boolean flag = true;
            //compare the target and all the strings encrypted with MD5 by brute force to crack the password
            while (!md5Str.equals(target)) {
                if (count % 1000000 == 0)
                    System.out.println("Handling request: " + target + ". Has processed to: " + str);
                count++;
                if (str.equals(end)) {
                    flag = false;
                    break;
                }
                str = Tools.findNextStr(str);
                md5Str = Tools.encrypt(str);
            }
            if (flag && str != null && !str.equals("")) return str;
        }
        return null;
    }
}