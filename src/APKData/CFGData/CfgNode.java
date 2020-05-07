package APKData.CFGData;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

import Logger.LogFatory;


/**
 * CFG图的一个点，表示一个opcode操作码，可暂时存储DFG数据
 *
 *
 * @author oufan
 */
public class CfgNode {

    // 对应操作码
    private String opcode;

    // 存储父亲节点
    private List<CfgNode> fatherCfgNode;

    // 下一个操作码
    private List<CfgNode> childCfgNode;

    // 是否是switch块
    private boolean isSwitchBlock;

    // 是否是跳转标志
    private boolean isJumpSymbol;

    // D.in集合
    private List<String> inList;

    // D.out集合
    private List<String> outList;

    // 调用函数对应的图
    private List<String> invokedList;

    // 打印日志信息
    private Logger logger = LogFatory.getGlobalLogger();

    public CfgNode(String opcode, boolean isSwicthBlock, boolean isJumpSymbol) {
        this.opcode = opcode;
        this.isSwitchBlock = isSwicthBlock;
        this.isJumpSymbol = isJumpSymbol;
        if (!isJumpSymbol && !isSwicthBlock) {
            inList = new ArrayList<>();
            outList = new ArrayList<>();
        }

        invokedList = new ArrayList<>();
        childCfgNode = new ArrayList<>();
        fatherCfgNode = new ArrayList<>();
        //logger.info("初始化一个cfg中的点" + opcode +" 是否为jump标志:" + isJumpSymbol +" 是否是switch块:" + isSwicthBlock);
    }


    public String getOpcode() {
        return opcode;
    }

    public List<CfgNode> getChildCfgNode() {
        return childCfgNode;
    }

    public boolean isSwitchBlock() {
        return isSwitchBlock;
    }

    public boolean isJumpSymbol() {
        return isJumpSymbol;
    }

    public List<CfgNode> getFatherCfgNode() {
        return fatherCfgNode;
    }

    public List<String> getInList() {
        return inList;
    }

    public List<String> getOutList() {
        return outList;
    }

    public List<String> getInvokedList() {
        return invokedList;
    }

    /*******************************cfg操作*************************************/
    /**
     * 添加操作码指向的下一个操作码
     *
     *
     * @param node 子操作码
     * @return 是否插入成功
     */
    public boolean addChildCfgNode(CfgNode node) {
        if (node == null) {
            logger.severe("为操作码 "+opcode +" 添加孩子为空");
        }
        return this.childCfgNode.add(node);
    }

    /**
     * 添加操作码指向的下一个操作码
     *
     *
     * @param node 父操作码
     * @return 是否插入成功
     */
    public boolean addFatherCfgNode(CfgNode node) {
        if (node == null) {
            logger.severe("为操作码 "+opcode +" 添加孩子为空");
        }
        return this.fatherCfgNode.add(node);
    }

    /**
     * 添加操作码的父节点
     *
     *
     * @param nodeList 孩子节点
     * @return 是否插入成功
     */
    public boolean addChildCfgNode(List<CfgNode> nodeList) {
        if (nodeList == null) {
            logger.severe("为操作码 "+opcode +" 添加父亲为空");
        }
        return this.childCfgNode.addAll(nodeList);
    }


   public void addInvokedNode(String invokeString) {
        if (invokeString == null) {
            return;
        }
        this.invokedList.add(invokeString);
   }


    /**
     * 所有具有相同名称的操作码为一个点
     *
     *
     *
     * @return hash值
     */
    public int hashCode() {
        return opcode.hashCode();
    }

    /**
     *判断是否是一个操作码
     *
     *
     *
     */
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        } else if (o == null) {
            return false;
        } else if (this.getClass() != this.getClass()) {
            return false;
        }
        CfgNode other = (CfgNode)o;
        return this.opcode.equals(other.opcode);

    }





}
