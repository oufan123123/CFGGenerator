package Processing;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Logger;

import APKData.CFGData.CfgNode;
import Logger.LogFatory;

/**
 * 用于初始化操作码的类，包含了所有跳转指令和普通非跳转指令
 *
 * 修改，删除nop
 *
 * @author oufan
 */

public class OpcodeContent {

    // goto直接跳转指令
    private static List<String> GOTO_LIST;
    // if条件跳转
    private static List<String> IF_LIST;
    // switch跳转
    private static List<String> SWITCH_LIST;
    // 普通非跳转语句
    private static List<String> NORMAL_LIST;

    // 映射goto
    private static Map<String, CfgNode> gotoToMap;
    // 映射if
    private static Map<String, CfgNode> ifToMap;
    // 映射switch
    private static Map<String, CfgNode> switchToMap;
    // 映射normal
    private static Map<String, CfgNode> normalToMap;


    // 得到所有的操作码
    private static List<String> allOpcodeList;

    // 得到所有跳转操作码
    private static List<String> jumpOpcodeList;


    // 生成一个所有操作码到二维数组的映射map
    private static Map<String, Integer> map;

    // goto直接跳转指令存储地址
    private static String GOTO_PATH = "F:\\CfgDfgGenerator\\opcodeContent\\gotoList.txt";
    // if跳转指令存储地址
    private static String IF_PATH = "F:\\CfgDfgGenerator\\opcodeContent\\ifList.txt";
    // switch跳转指令存储地址
    private static String SWITCH_PATH = "F:\\CfgDfgGenerator\\opcodeContent\\switchList.txt";
    // 普通非跳转指令存储地址
    private static String NORMAL_PATH = "F:\\CfgDfgGenerator\\opcodeContent\\normalList.txt";

    public static List<String> getGotoList() {
        return GOTO_LIST;
    }

    public static List<String> getIfList() {
        return IF_LIST;
    }

    public static List<String> getSwitchList() {
        return SWITCH_LIST;
    }

    public static List<String> getNormalList() {
        return NORMAL_LIST;
    }


    public static Map<String, CfgNode> getGotoToMap() {
        return gotoToMap;
    }

    public static Map<String, CfgNode> getIfToMap() {
        return ifToMap;
    }

    public static Map<String, CfgNode> getSwitchToMap() {
        return switchToMap;
    }

    public static Map<String, CfgNode> getNormalToMap() {
        return normalToMap;
    }

    public static Map<String, Integer> getMap() {
        return map;
    }

    public static List<String> getAllOpcodeList() {
        return allOpcodeList;
    }

    public static List<String> getJumpOpcodeList() {
        return jumpOpcodeList;
    }

    public static void initOpcodeContent() throws Exception {
        Logger logger = LogFatory.getGlobalLogger();

        GOTO_LIST = ReadFile.readFileReturnLines(GOTO_PATH);

        IF_LIST = ReadFile.readFileReturnLines(IF_PATH);

        SWITCH_LIST = ReadFile.readFileReturnLines(SWITCH_PATH);

        NORMAL_LIST = ReadFile.readFileReturnLines(NORMAL_PATH);

        if (GOTO_LIST == null || IF_LIST == null
                || SWITCH_LIST == null || NORMAL_LIST == null) {
            logger.severe("check your opcodeContent path is right, it cannot be loaded without these opcode Files");
            throw new IOException();
        } else {
            logger.info("opcodeContent load right");
        }

        initJumpOpcedeList();
        initOpcodeList();
        initOpcodeToInteger();
        logger.info("初始化opcodeToNode大小结果为： "+"jumpOpcodeList:"+jumpOpcodeList.size()+" allOpcodeList:"+allOpcodeList.size());
    }

    public static void initJumpOpcedeList() {
        jumpOpcodeList = new ArrayList<>();
        jumpOpcodeList.addAll(GOTO_LIST);
        jumpOpcodeList.addAll(IF_LIST);
        jumpOpcodeList.addAll(SWITCH_LIST);
    }

    public static void initOpcodeList() {
        allOpcodeList = new ArrayList<>();
        allOpcodeList.addAll(jumpOpcodeList);
        allOpcodeList.addAll(NORMAL_LIST);
    }

    public static void initOpcodeToInteger() {
        map = new HashMap<>();
        int i=0;
        for (String opcodeString:allOpcodeList) {
            map.put(opcodeString, i++);
        }
    }




}
