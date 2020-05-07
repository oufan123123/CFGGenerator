package Processing;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.List;

/**
 * 单独写一个阅读文件的类，除掉冗余的阅读文件代码
 *
 * @author oufan
 */

public class ReadFile {


    /**
     * 读取文件的每一行代码，去除空行，返回包含行的字符串动态数组
     *
     *
     * @param filePath 文件路径
     * @return 字符串数组
     */
    public static List<String> readFileReturnLines (String filePath) {
        List<String> list = new ArrayList<>();
        try {
            File file = new File(filePath);
            BufferedReader br = new BufferedReader(new FileReader(file));
            String line = null;
            while ((line = br.readLine()) != null) {
                // 去掉空行
                if (!line.equals("")) {
                    list.add(line);
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        return list;
    }
}
