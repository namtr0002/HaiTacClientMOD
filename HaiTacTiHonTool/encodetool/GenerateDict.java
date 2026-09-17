package encodetool;

import java.io.FileWriter;
import java.io.PrintWriter;
import java.io.IOException;
import java.util.Random;
import java.util.Set;
import java.util.LinkedHashSet;

public class GenerateDict {

    public static void main(String[] args) {

        int count = 10000;

        if (args.length > 0) {
            count = Integer.parseInt(args[0]);
        }

        String outputFile = "dictionary.txt";

        if (args.length > 1) {
            outputFile = args[1];
        }

        char[] chars = {
			'ก', 'ข', 'ฃ', 'ค', 'ฅ', 'ฆ',
			'ง', 'จ', 'ฉ', 'ช', 'ซ', 'ฌ',
			'ญ', 'ฎ', 'ฏ', 'ฐ', 'ฑ', 'ฒ',
			'ณ', 'ด', 'ต', 'ถ', 'ท', 'ธ',
			'น', 'บ', 'ป', 'ผ', 'ฝ', 'พ',
			'ฟ', 'ภ', 'ม', 'ย', 'ร', 'ล',
			'ว', 'ศ', 'ษ', 'ส', 'ห', 'ฬ',
			'อ', 'ฮ',

			'ะ', 'า', 'ำ', 'ิ', 'ี', 'ึ',
			'ื', 'ุ', 'ู', 'เ', 'แ', 'โ',
			'ใ', 'ไ', 'ๅ', 'ๆ',

			'็', '่', '้', '๊', '๋', '์'
		};

        Random rand = new Random();

        Set<String> dict = new LinkedHashSet<>();

        while (dict.size() < count) {

            int len = 10 + rand.nextInt(40);

            StringBuilder sb = new StringBuilder();

            for (int i = 0; i < len; i++) {
                sb.append(chars[rand.nextInt(chars.length)]);
            }

            dict.add(sb.toString());
        }

        try (PrintWriter writer = new PrintWriter(new FileWriter(outputFile))) {

            for (String s : dict) {
                writer.println(s);
            }

        } catch (IOException e) {
            e.printStackTrace();
        }

        System.out.println("Generated " + dict.size() + " chaos entries in " + outputFile);
    }
}