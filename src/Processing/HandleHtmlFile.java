package src.Processing;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class HandleHtmlFile {

    public static void main(String [] args) {
        readFile("F:/CfgDfgGenerator/html.txt");
    }

    public static void readFile(String fileName) {
        // 找到的基本符合字符，留待下一步处理
        List<String> list = new ArrayList<>();


        File file = new File(fileName);
        final String START_FLAG = "<table class=\"instruc\">";
        final String END_FLAG = "<h2 id=\"packed-switch\" data-text=\"packed-switch-payload 格式\" tabindex=\"0\">packed-switch-payload 格式</h2>";
        boolean anaylse = false;
        int flag = 3;
        try {
            BufferedReader br = new BufferedReader(new FileReader(file));
            String line = null;
            while ((line = br.readLine()) != null) {
                line = line.trim();
                if (anaylse) {

                    if (line.equals("<tr>")) {
                        flag = 0;
                        continue;
                    } else if (flag == 2) {
                        line = line.replace("<td>","");
                        line = line.replace("</td>", "");
                        System.out.println(line);
                    }
                    flag++;
                }
                if (line.equals(START_FLAG)) {
                    anaylse = true;
                    continue;
                }
                if (line.equals(END_FLAG)) {
                    break;
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
