package voteit.libs.libutils;

import java.util.ArrayList;

public class Utils {
    static public ArrayList<Character> stringToCharList(String str) {
        ArrayList<Character> list = new ArrayList<>();
        for (char ch : str.toCharArray()) {
            list.add(ch);
        }
        return list;
    }
}
