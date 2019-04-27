package mycompany.com.luanvan.utils;

public class Number {
    public static String convertNumber(int number) {
        int gia = number;
        StringBuilder giaText = new StringBuilder();
        if (gia >= 1000000) {
            giaText.append(gia / 1000000).append(",");
            gia = gia - (gia / 1000000) * 1000000;
        }
        if (gia >= 1000) {
            giaText.append(gia / 1000).append(",000");
//            gia = gia - (gia / 1000) * 1000;
        }
        if (gia < 1000) {
            giaText.append(gia);
        }
        return giaText.toString();
    }
}
