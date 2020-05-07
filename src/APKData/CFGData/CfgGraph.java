package APKData.CFGData;

import java.util.*;
import java.util.logging.Logger;

import APKData.SmaliData.SmaliMethod;
import Logger.LogFatory;
import Processing.OpcodeContent;

/**
 * 一个CFG对应一个SmaliMethod，即一个函数对应一个CFG
 *
 * @author oufan
 */

public class CfgGraph {

    // 所在包
    private String packageName;

    // 一一对应的点
    private String methodName;

    // 映射所有的操作码到二维数组
    private Map<String, Integer> mapToVector;

    // 用于存储少量的invoke操作码，以加快查找速度
    private Set<CfgNode> invokeSet;

    // 点集合
    private Map<String,CfgNode> map;

    // 开始点集合
    private CfgNode startNode;

    // 结束点集合
    private Set<CfgNode> endSet;

    // symbol总数
    private int symbolNumber;

    // 简单存储为一个二维数组,也是作为后期可直接作为向量的矩阵
    private int[][] opcodeCalling;

    public Logger logger = LogFatory.getGlobalLogger();;

    public CfgGraph(String methodName, String packageName) {
        this.methodName = methodName;
        this.packageName = packageName;
        map = new LinkedHashMap<>();
        endSet = new HashSet<>();
        invokeSet = new HashSet<>();
        mapToVector = OpcodeContent.getMap();
        opcodeCalling = new int[224][224];
        logger.info("包名："+packageName +" 函数名："+methodName+" 初始化一个cfg图");
    }



    public Set<CfgNode> getEndSet() {
        return endSet;
    }

    public String getPackageName() {
        return packageName;
    }

    public String getMethodName() {
        return methodName;
    }

    public void setMap(Map<String,CfgNode> map) {
        this.map = map;
    }
    public Map<String, CfgNode> getMap() {
        return map;
    }

    public int[][] getOpcodeCalling() {
        return opcodeCalling;
    }

    public Set<CfgNode> getInvokeSet() {
        return invokeSet;
    }

    public CfgNode getStartNode() {
        return startNode;
    }

    /**
     * 往cfg中添加节点
     *
     *
     *
     * @param cfgNode 待添加节点
     * @return 返回值
     */
    public boolean addNode(CfgNode cfgNode) {
        if (cfgNode == null) {
            logger.info("包名："+packageName +" 函数名："+methodName+" 添加点为空值");
            return false;
        }
        logger.info("包名："+packageName +" 函数名："+methodName+" 添加了一个点："+cfgNode.getOpcode());
        map.put(cfgNode.getOpcode(),cfgNode);
        return true;
    }

    /**
     * 将邻接图转为二维数组向量
     *
     *
     *
     */
    public void turnMapToVector() {
        try {
            if (map == null || map.size() == 0) {
                logger.severe("邻接图不存在或者生成失败，请检查一下");
                throw new Exception();
            }

            for (Map.Entry<String, CfgNode> entry:map.entrySet()) {
                Integer fatherInt = mapToVector.get(entry.getKey());
                if (fatherInt == null) {
                    continue;
                }
                CfgNode fatherNode = entry.getValue();
                List<CfgNode> childList = fatherNode.getChildCfgNode();
                if (childList == null || childList.size() ==0) {
                    continue;
                }
                for (CfgNode childNode:childList) {
                    Integer childInt = mapToVector.get(childNode.getOpcode());
                    if (childInt == null) {
                        continue;
                    }
                    opcodeCalling[fatherInt][childInt]++;
                }

            }
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    /**
     * 将中间调用转化为特征向量
     *
     *
     * @param smaliMethodMap 存储的smali方法图
     */
    public void turnInterCallToVector(String name, Map<String, SmaliMethod> smaliMethodMap) {
        if (invokeSet.size() == 0) {
            return;
        }
        Iterator<CfgNode> iterator = invokeSet.iterator();
        while (iterator.hasNext()) {
            CfgNode invokeNode = iterator.next();
            if (name.contains("onHideCustomView")) {
                System.out.println("invokeNode:"+invokeNode.getOpcode());
            }
            List<String> invokedList = invokeNode.getInvokedList();

            if (invokedList == null || invokedList.size() == 0) {
                continue;
            }

            for (String invokedString : invokedList) {
                if (name.contains("onHideCustomView")) {
                    System.out.println("invokedMethod:"+invokedString);
                }

                SmaliMethod invokedMethod = smaliMethodMap.get(invokedString);
                if (invokedMethod == null) {
                    continue;
                }
                CfgGraph invokedGraph = invokedMethod.getCfgGraph();
                if (invokedGraph == null) {
                    continue;
                }
                CfgNode invokedStartNode = invokedGraph.getStartNode();
                Set<CfgNode> invokedEndSet = invokedGraph.getEndSet();
                if (invokedStartNode == null || invokedEndSet.size() == 0) {
                    continue;
                }

                List<CfgNode> childList = invokeNode.getChildCfgNode();
                if (childList == null || childList.size() == 0) {
                    logger.severe("invoke作为了结束节点，错误");
                }
                // 调用方的映射数字
                int invokeFirstInteger = mapToVector.get(invokeNode.getOpcode());
                // 调用方的下一个点集合
                List<Integer> invokeSecondList = new ArrayList<>();
                // 被调用方法的起始点
                int invokedStartInteger = mapToVector.get(invokedStartNode.getOpcode());
                // 被调用方法的终止点集合
                List<Integer> invokedEndList = new ArrayList<>();

                if (name.contains("onHideCustomView")) {
                    System.out.println("invokedStart:"+invokedStartNode.getOpcode());
                }
                for (CfgNode invokedEndNode:invokedEndSet) {
                    Integer invokedEndInteger = mapToVector.get(invokedEndNode.getOpcode());
                    if (name.contains("onHideCustomView")) {
                        System.out.println("invokedEnd:"+invokedEndNode.getOpcode());
                    }
                    invokedEndList.add(invokedEndInteger);
                }
                for (CfgNode childNode:childList) {
                    Integer childInteger = mapToVector.get(childNode.getOpcode());
                    if (childInteger == null) {
                        continue;
                    }
                    invokeSecondList.add(childInteger);
                    for (Integer invokedEndInteger:invokedEndList) {
                        opcodeCalling[childInteger][invokedEndInteger]++;
                        if (name.contains("onHideCustomView")) {
                            System.out.println("invokedchildNode:"+childNode.getOpcode());
                            System.out.println("invoked->next");
                            System.out.println("++");
                        }
                    }
                }

                opcodeCalling[invokeFirstInteger][invokedStartInteger]++;
                if (name.contains("onHideCustomView")) {
                    System.out.println("first->invokedstart");

                    System.out.println("++");
                }
            }

        }
    }

    /**
     * 加入开始点
     *
     *
     * @param cfgNode 开始点
     */
    public void addStartNode(CfgNode cfgNode) {
        if (cfgNode == null) {
            return;
        }
        this.startNode = cfgNode;
    }

    /**
     * 加入结束点
     *
     *
     * @param cfgNode 结束点
     */
    public void addEndNode(CfgNode cfgNode) {
        if (cfgNode == null) {
            return;
        }
        this.endSet.add(cfgNode);
    }


    /**
     * 判断是否有end节点
     *
     * @return
     */
    public boolean isHaveNodeInEnd() {
        if (endSet == null || endSet.size() == 0) {
            return false;
        }
        return true;
    }


    /**
     * 测试开始集合有没有错误
     *
     *
     */
    public void searchStartSet() {
        System.out.println(startNode.getOpcode()+" fatherSize:"+ startNode.getFatherCfgNode().size());
    }


    /**
     * 测试结尾集合有没有错误
     *
     *
     */
    public void searchEndSet() {
        Iterator<CfgNode> iterator = endSet.iterator();
        while (iterator.hasNext()) {
            CfgNode endNode = iterator.next();
            System.out.println(endNode.getOpcode()+" childSize:"+ endNode.getChildCfgNode().size());
        }
    }

    /**
     * 设置开始节点
     *
     *
     */
    public void setStartNode(String startOpcode) {
        if (map == null || map.size() == 0) {
            return;
        }
        this.startNode = map.get(startOpcode);
        //System.out.println(startOpcode+"  "+startNode.getOpcode());
    }

    /**
     * 增加invoke节点
     *
     *
     * @param invokeNode invoke点
     */
    public void addInvokeNode(CfgNode invokeNode) {
        if (invokeNode == null) {
            return;
        }
        this.invokeSet.add(invokeNode);
    }



}
