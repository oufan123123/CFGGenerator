package Logger;


import java.io.File;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * 设置一个全局日志，存储整个项目运行得到的日志信息
 *
 * @author oufan
 */
public class LogFatory {

    public static final String LOG_NAME = "Global";

    public static final String LOG_FOLDER = "F:\\CfgDfgGenerator\\Log";

    public static String logPath;

    public static Logger globalLogger;

    static {
        logPath = LOG_FOLDER + File.separator +"JDKLog_" + LogUtil.getCurrentDate(LogUtil.DATE_NORMAL) +".log";

        globalLogger = initGlobalLogger();
    }

    /**
     * 初始化全局日志
     *
     *
     * @return 全局日志
     */
    public static Logger initGlobalLogger() {
        Logger logger = Logger.getLogger(LOG_NAME);

        logger.setLevel(Level.ALL);

        LogUtil.setFileHandler(logger, Level.INFO, logPath);

        logger.setUseParentHandlers(false);

        return logger;
    }

    /**
     *  获取全局日志
     *
     *
     * @return 全局日志
     */
    public static Logger getGlobalLogger() {
        return globalLogger;
    }

}
