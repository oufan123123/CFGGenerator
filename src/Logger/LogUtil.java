package Logger;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.logging.*;

/**
 * 设置日志的基本操作，包括设置等级，添加控制台输出方式，文件输出方式等等
 *
 * @author oufan
 */

public class LogUtil {

    public static final String DATE_PATTERN = "YYYY-MM-DD HH:mm:ss";

    public static final String DATE_NORMAL = "YYYYMMDDHHmmss";

    /**
     * 为日志设置等级
     *
     *
     * @param log 日志
     * @param level 等级
     */
    public static void setLogLevel(Logger log, Level level) {
        log.setLevel(level);
    }

    /**
     * 为日志添加控制台输出方式
     *
     *
     * @param log 日志
     * @param level 等级
     */
    public static void setConsoleHandler(Logger log, Level level) {

        ConsoleHandler consoleHandler = new ConsoleHandler();

        // 设置控制台的输出等级，如果想设置的输出等级高于日志，则会替换为控制台的等级
        consoleHandler.setLevel(level);

        log.addHandler(consoleHandler);
    }

    /**
     * 为日志添加文件输出方式
     *
     *
     * @param log 日志
     * @param level 等级
     * @param filePath 存储日志路径
     */
    public static void setFileHandler(Logger log, Level level, String filePath) {

        FileHandler fileHandler = null;
        try {
            // 设置输出日志输出路径
            fileHandler = new FileHandler(filePath);
            // 设置文件日志输出等级，如果高于日志本身等级，则替换为当前设置的等级
            fileHandler.setLevel(level);
            fileHandler.setFormatter(new Formatter() {
                @Override
                public String format(LogRecord record) {
                    return "[" + getCurrentDate(DATE_PATTERN)+" - level:"
                            +record.getLevel().getName()+"]- [ "+record.getSourceClassName()
                            +" ->:"+record.getSourceMethodName()+"()] output:"+record.getMessage()+"\n";
                }
            });
        } catch (IOException e) {
            e.printStackTrace();
        }
        log.addHandler(fileHandler);
    }

    /**
     * 获取当前的系统时间
     *
     *
     * @return 系统当前时间
     */
    public static String getCurrentDate(String name) {
        Date date = new Date();
        SimpleDateFormat sdf = new SimpleDateFormat(name);
        return sdf.format(date);
    }

}
