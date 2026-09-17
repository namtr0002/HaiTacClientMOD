package encodetool;

import java.io.BufferedWriter;
import java.io.FileOutputStream;
import java.io.OutputStreamWriter;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

/**
 * Sinh bộ từ điển rối nhẹ nhàng 'a', 'b', 'c', 'd'..
 * Giúp tối ưu Constant Pool, giảm dung lượng JAR tối đa và tương thích 100% KVM.
 */
public class GenerateDict {
    private static final char[] LETTERS = "abcdefghijklmnopqrstuvwxyz".toCharArray();

    public static void main(String[] args) {
        int maxEntries = args.length > 0 ? Integer.parseInt(args[0]) : 1500;
        String outFile = args.length > 1 ? args[1] : "OBF_ULTIMATE.txt";

        List<String> words = new ArrayList<>();

        // 1. Sinh các từ 1 ký tự: a, b, c, d ... z
        for (char c : LETTERS) {
            words.add(String.valueOf(c));
            if (words.size() >= maxEntries) break;
        }

        // 2. Sinh các từ 2 ký tự: aa, ab, ac ... zz
        if (words.size() < maxEntries) {
            for (char c1 : LETTERS) {
                for (char c2 : LETTERS) {
                    words.add("" + c1 + c2);
                    if (words.size() >= maxEntries) break;
                }
                if (words.size() >= maxEntries) break;
            }
        }

        // 3. Sinh các từ 3 ký tự nếu cần (aaa, aab ...)
        if (words.size() < maxEntries) {
            for (char c1 : LETTERS) {
                for (char c2 : LETTERS) {
                    for (char c3 : LETTERS) {
                        words.add("" + c1 + c2 + c3);
                        if (words.size() >= maxEntries) break;
                    }
                    if (words.size() >= maxEntries) break;
                }
                if (words.size() >= maxEntries) break;
            }
        }

        try (BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(outFile), StandardCharsets.UTF_8))) {
            for (String word : words) {
                writer.write(word);
                writer.newLine();
            }
            System.out.println("[+] [HTTH Dict Generator] Generated " + words.size() + " compact 'a b c d..' entries into " + outFile);
        } catch (Exception e) {
            System.err.println("[!] Failed to generate dict: " + e.getMessage());
        }
    }
}
