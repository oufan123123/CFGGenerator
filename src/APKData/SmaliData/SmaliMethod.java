package APKData.SmaliData;

import java.io.Serializable;
import java.util.*;
import java.util.logging.Logger;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import APKData.SmaliGraph.GraphToGexf;
import Logger.LogFatory;
import APKData.CFGData.*;
import Processing.OpcodeContent;

public class SmaliMethod implements Serializable{
	public String methodPackageString;                 //the package name of the method
	public String methodNameString;                    //the name of the method
	public String methodArgsString;       					 //the args of the method
	public ArrayList<String> methodArgsList=new ArrayList<>();         // the list of the args of the method
	private String returnValue;                                 //the return value of the method
	private ArrayList<String>  smaliCodeStrings=new ArrayList<>();    //the smali code of the method
	private ArrayList<MethodCalling>  methodCalls=new ArrayList<>();  //a Android method contains number of Android calls


	/*******************************cfg模块专属属性*********************************/
	// 一个method对应一个图
	private CfgGraph cfgGraph;

	// 指令存储集合
	volatile Map<String, CfgNode> map;

	// 存储上一个指令
	volatile CfgNode tempNode;

	// goto直接跳转指令
	private List<String> gotoList;
	// if条件跳转
	private List<String> ifList;
	// switch跳转
	private List<String> switchList;
	// 普通非跳转语句
	private List<String> normalList;

	// 存储所有的操作码
	private List<String> allOpcodeList;

	// 存储所有的跳转操作码
	private List<String> allJumpOpcodeList;

	// 存储所有找到的标志操作码
	private Set<String> symbolOrSwitchList;




		private Logger log = LogFatory.getGlobalLogger();
		
		public SmaliMethod(){
			//log.info("开始一个smalimethod的分析：");
		}
		public SmaliMethod(String packageString){
			this.methodPackageString=packageString;
			//log.info("开始一个smalimethod的分析：");
	   }

	public String getMethodNameString() {
		return methodNameString;
	}

	public String getMethodPackageString() {
			return methodPackageString;
	}

	public String getMethodArgsString() {
		return methodArgsString;
	}

	public CfgGraph getCfgGraph() {
		return cfgGraph;
	}

	/***********************************fcg模块*******************************************/
	public void addSmaliCode(String smaliCode){
			  smaliCodeStrings.add(smaliCode);
			 // System.out.println(smaliCode);
		}
	private int kLine=0;
	public void extractCallInformation(){
		while(kLine<=smaliCodeStrings.size()-1){
			getMethodInfomation(smaliCodeStrings.get(kLine));
			kLine++;
		}
	}
	public String getMethodInfomation(String line){
		line=line.trim();
		if(line.startsWith(".method")){
			Pattern pattern=Pattern.compile(".method(.*) (.*?)\\((.*?)\\)(.*)");
			Matcher matcher=pattern.matcher(line);
			if(matcher.find()){
				this.methodNameString=matcher.group(2);
				this.methodArgsString=matcher.group(3);
				this.returnValue=matcher.group(4);
				// 替换关键字
				replaceKeyWords();
			}
		}
		else if(line.startsWith("invoke-")){
			extractMethodCall(line);
		}
		return null;
	}
	public void replaceKeyWords(){
		//替换函数名中包含< 或　＞
		if(this.methodNameString.contains("<")){
			this.methodNameString=this.methodNameString.replace("<", "");
		}
		if(this.methodNameString.contains(">")){
			this.methodNameString=this.methodNameString.replace(">", "");
		}
		//替换包名字符串首字母　Ｌ　
		if(this.methodPackageString.startsWith("L")){
			this.methodPackageString=this.methodPackageString.substring(1);
		}
		// 替换参数类型　
		if(this.methodArgsString.length()>0){
			String newArgString="";
			String args[]=this.methodArgsString.split(";");
			for(int i=0;i<args.length;i++){
				if(args[i].startsWith("L")){
					args[i]=args[i].substring(1);           // 删除第一个字母＂L
				}
				if(args[i].equals("I")){
					args[i]="java.lang.Integer";          //替换　Ｉ　　为　　java.lang.Integer
				}
				if(args[i].equals("Z")){
					args[i]="boolean";                  //替换　Z	为　　boolean
				}
				newArgString +=args[i]+";";
				methodArgsList.add(args[i]);
			}
			this.methodArgsString=newArgString;
		}
		//替换返回值类型
		if(this.returnValue.length()>0){
			if(this.returnValue.equals("V")){
				this.returnValue="void";           //   替换　　V 为void
			}
			if(this.returnValue.startsWith("L")){
				this.returnValue=this.returnValue.substring(1);          //删除第一个字母"L"
			}
			if(this.returnValue.equals("Z")){
				this.returnValue="boolean";              //替换　Z	为　　boolean
			}
			if(this.returnValue.equals("I")){
				this.returnValue="java.lang.Integer";   //替换　I 为　java.lang.Integer
			}
			if(this.returnValue.contains(";")){
				this.returnValue=this.returnValue.replace(";", "");        //删除返回值最后的 ;

			}
		}
	}
	public void extractMethodCall(String line){
		if(line.startsWith("invoke")){
			SmaliMethod calleeMethod=new SmaliMethod();
			Pattern pattern=Pattern.compile("invoke-(.*?) \\{.*\\}, (.*?);->(.*?)\\((.*?)\\)(.*)");
			Matcher matcher=pattern.matcher(line);
			if(matcher.find()){
				calleeMethod.methodPackageString=matcher.group(2);
				calleeMethod.methodNameString=matcher.group(3);

				calleeMethod.methodArgsString=matcher.group(4);
				calleeMethod.returnValue=matcher.group(5);

				calleeMethod.replaceKeyWords();
				MethodCalling tmpCalling=new MethodCalling(this, calleeMethod);
				methodCalls.add(tmpCalling);
			}

		}
	}
		public String getEntityMethodString(){
			String resultString=this.methodPackageString+": "+this.methodNameString+"("+this.methodArgsString+")"+returnValue;
			return resultString;
		}
		public void showCallingInformation(){
			for(int i=0;i<methodCalls.size();i++){
				System.out.println(methodCalls.get(i).toString());
			}
		}
		public ArrayList<MethodCalling> getMethodCalls() {
			return methodCalls;
		}
		public void setMethodCalls(ArrayList<MethodCalling> methodCalls) {
			this.methodCalls = methodCalls;
		}

		/************************************cfg模块*************************************************/

	/**
	 * 初始化映射信息
	 *
	 *
	 *
	 *
	 */
	public void getOpcodeMap() {
		log.info("将各种操作码放到类中");
		this.gotoList = OpcodeContent.getGotoList();
		this.ifList = OpcodeContent.getIfList();
		this.switchList = OpcodeContent.getSwitchList();
		this.normalList = OpcodeContent.getNormalList();
		this.allJumpOpcodeList = OpcodeContent.getJumpOpcodeList();
		this.allOpcodeList = OpcodeContent.getAllOpcodeList();
	}

	/**
	 * 分析一遍当前方法的smali方法，最后得到一个Cfg方法
	 *
	 *
	 *
	 */
	public void  analyse() {
		log.info("开始分析smali代码");
		if (smaliCodeStrings == null || smaliCodeStrings.size() <3 || methodNameString == null) {
			log.severe("smali代码读取出错："+"包名："+methodPackageString +" 函数名："+methodNameString);
		}
		// 处理
		this.cfgGraph = new CfgGraph(methodNameString, methodPackageString);

		// 处理集合
		map = new LinkedHashMap<>();

		// 记录start节点的操作码
		String startOpcode = null;

		symbolOrSwitchList = new HashSet<>();

		for (int i=0;i<smaliCodeStrings.size();i++) {
			String line = smaliCodeStrings.get(i);
			line = line.trim();
			if (line == null || line.equals("")) {
				continue;
			}
			//System.out.println(line);
			// 是否是switch操作块
			boolean isSwitchBlock = judgeIsSwitchBlock(line);
			// 是否是跳转标志(或许是的或许不是跳转标志但是也是":"开头，但是一并放进去)
			boolean isJumpSymbol = judgeIsJumpSymbol(line);
			// 是否是操作码，跳转为0，非跳转为1，不是操作码为2,
			int isOpcode = judgeIsOpcode(line);

			if (startOpcode == null && (isSwitchBlock
			     || isJumpSymbol || isOpcode == 0 || isOpcode == 1)) {
				startOpcode = getOpcodeFromString(line);
				if (!allOpcodeList.contains(startOpcode)) {
					startOpcode = null;
				}

			}
			// 第一种跳转标志处理

			if (isJumpSymbol) {
				//System.out.println("1");
				handleJumpSymbol(line);
				continue;
			}
			// 第二种switch块的处理方式
			if (isSwitchBlock) {
				// 处理switch块
				//System.out.println("2");
				i = handleSwitchBlock(line, i);
				continue;
			}
			// 第三种非switch操作码,分两种，跳转和非跳转的
			if (isOpcode == 0) {
				// 处理跳转操作码
				handleJumpOpcode(line);

				continue;

			} else if (isOpcode == 1){
				// 处理非跳转操作码
				handleUnJumpOpcode(line);
				continue;
			}
		}


		// 搞清楚指向对没有
		//接下来就是处理如何去删除标记节点，将标记替换，注意这里我并没有删除标记节点，因为其作为很小一部分，首先不会占用大量内存，第二就是反正最后统计到向量上也不会用到symbol标记；
		if (symbolOrSwitchList == null || symbolOrSwitchList.size() == 0) {
			log.info("没有跳转标志，请检查日志");
			return;
		}
		for (String symbolOrSwitch:symbolOrSwitchList) {
			turnFatherToChild(symbolOrSwitch);
		}


		// 将map设置到图中，其实可以直接在图中操作map
		this.cfgGraph.setMap(map);

		// 设置开始点
		this.cfgGraph.setStartNode(startOpcode);
		// 邻接图转为二维矩阵向量图
		this.cfgGraph.turnMapToVector();


		if (this.methodNameString.contains("onOptionsItemSelected")) {
			this.cfgGraph.searchStartSet();
			this.cfgGraph.searchEndSet();
		}

	}


	/**
	 * 判断这行是否是switch块
	 *
	 *
	 * @param line 这行代码
	 * @return 判断结果
	 */
	public boolean judgeIsSwitchBlock(String line) {
		if (line.startsWith(".packed-switch") || line.startsWith(".switch-packed")) {
			return true;
		}
		return false;
	}

	/**
	 * 判断这行是否是跳转标志
	 *
	 *
	 * @param line 这行代码
	 * @return 判断结果
	 */
	public boolean judgeIsJumpSymbol(String line) {
		if (line.startsWith(":")) {
			return true;
		}
		return  false;
	}

	/**
	 * 判断这行是否是跳转操作码
	 *
	 *
	 * @param line 这行代码
	 * @return 判断结果
	 */
	public int judgeIsOpcode(String line) {
		String[] s = line.split(" ");
		if (allJumpOpcodeList.contains(s[0])) {
			return 0;
		} else if (allOpcodeList.contains(s[0])) {
			return 1;
		}
		return 2;
	}

	/**
	 * 查看是否是switch块结束标志
	 *
	 *
	 * @param line
	 * @return
	 */
	public boolean isSwitchEnd(String line) {
		if(line.startsWith(".end")) {
			return true;
		}
		return false;
	}

	public void handleJumpSymbol(String line) {
		line = getOpcodeFromString(line);
		CfgNode symbolNode = new CfgNode(line, false, true);
		if (map.containsKey(line)) {
			symbolNode = map.get(line);
			log.info("点在图中已经存在:"+line);
		}
		map.put(line, symbolNode);
		symbolOrSwitchList.add(line);
		log.info("将点加入图中, 点为："+line);

		if (tempNode != null) {
			tempNode.addChildCfgNode(symbolNode);
			symbolNode.addFatherCfgNode(tempNode);
			log.info("为普通标志加父节点:"+symbolNode.getOpcode()+" 父节点："+tempNode.getOpcode());
		} else {
			this.cfgGraph.addStartNode(symbolNode);
		}
		tempNode = symbolNode;

	}

	public int handleSwitchBlock(String line, int i) {
		line = getOpcodeFromString(line);
		log.info("handleSwitchBlock before i:"+i + " --对应语句为："+smaliCodeStrings.get(i));
		CfgNode switchBlockNode = new CfgNode(line, true,false);
		int index = i+1;
		// 将此switch块往下找，找到end为止，然后记住将i顺调至end结束
		while (index < smaliCodeStrings.size() - 1) {
			String switchLine = smaliCodeStrings.get(index);
			if (switchLine == null || switchLine.length() == 0) {
				index++;
				continue;
			}
			switchLine = switchLine.trim();
			if (!isSwitchEnd(switchLine)) {
				CfgNode switchSymbolNode = new CfgNode(switchLine, false, true);
				if (map.containsKey(switchLine)) {
					switchSymbolNode = map.get(switchLine);
					log.info("点在图中已经存在:"+switchLine);
				}
				symbolOrSwitchList.add(switchLine);
				switchBlockNode.addChildCfgNode(switchSymbolNode);
				switchSymbolNode.addFatherCfgNode(switchBlockNode);
				log.info("为switch块中symbol加父节点:"+switchSymbolNode.getOpcode()+" 父节点："+switchBlockNode.getOpcode());
				map.put(switchLine, switchSymbolNode);
				log.info("将点加入图中, 点为："+switchLine);
			} else {
				break;
			}
			index++;
		}

		i = index;
		log.info("handleSwitchBlock after i:"+i + " --对应语句为："+smaliCodeStrings.get(i));

		// 放入map
		map.put(line, switchBlockNode);
		symbolOrSwitchList.add(line);
		log.info("将点加入图中, 点为："+line);

		if (tempNode != null) {
			tempNode.addChildCfgNode(switchBlockNode);
			switchBlockNode.addFatherCfgNode(tempNode);
			log.info("为switch块加父节点:"+switchBlockNode.getOpcode()+" 父节点："+tempNode.getOpcode());
		} else {
			this.cfgGraph.addStartNode(switchBlockNode);
		}
		tempNode = switchBlockNode;

		// 将此块作为temp块，但是一般来说到这里method就结束了

		return i;
	}

	public void handleJumpOpcode(String line) {
		String[] s = line.split(" ");
		// 得到跳转操作码
		CfgNode opcodeNode = new CfgNode(s[0], false, false);
		if (map.containsKey(s[0])) {
			opcodeNode = map.get(s[0]);
			//log.info("点在图中已经存在:"+s[0]);
		}
		map.put(s[0], opcodeNode);
		//log.info("将点加入图中, 点为："+s[0]);
		// 得到跳转标志
		CfgNode symbolNode = null;
		for (int j=0;j<s.length;j++) {
			if (s[j].startsWith(":")) {
				symbolNode = new CfgNode(s[j], false ,true);
				if (map.containsKey(s[j])) {
					symbolNode = map.get(s[j]);
					//log.info("点在图中已经存在:"+s[j]);
				}
				symbolOrSwitchList.add(s[j]);
				map.put(s[j], symbolNode);
				//log.info("将点加入图中, 点为："+s[j]);
				break;
			}
		}
		if (symbolNode == null) {
			log.severe("跳转语句找到跳转操作码但是没找到跳转标志："+"包名："+methodPackageString +" 函数名："+methodNameString);
		}
		// 上一个点指向操作码
		opcodeNode.addChildCfgNode(symbolNode);
		symbolNode.addFatherCfgNode(opcodeNode);
		//log.info("为普通标志加父节点:"+symbolNode.getOpcode()+" 父节点："+opcodeNode.getOpcode());
		// 操作码指向标志

		if (tempNode != null) {
			tempNode.addChildCfgNode(opcodeNode);
			//log.info("为普通标志加父节点:"+opcodeNode.getOpcode()+" 父节点："+tempNode.getOpcode());
		} else {
			this.cfgGraph.addStartNode(opcodeNode);
		}
		// 非goto跳转才能指向下一个点
		if (!gotoList.contains(s[0])) {
			tempNode = opcodeNode;
		} else {
			tempNode = null;
		}

	}


	public void handleUnJumpOpcode(String line) {
		String newLine = getOpcodeFromString(line);
		CfgNode symbolNode = new CfgNode(newLine, false, false);


		if (map.containsKey(newLine)) {
			symbolNode = map.get(newLine);
		}
		if (newLine.startsWith("invoke-")) {
			String invokeMethod = extractCalleeString(line);
			symbolNode.addInvokedNode(invokeMethod);
			this.cfgGraph.addInvokeNode(symbolNode);
		}
		map.put(newLine, symbolNode);
		//log.info("将点加入图中, 点为："+line);
		if (tempNode != null) {
			tempNode.addChildCfgNode(symbolNode);
			//log.info("为普通标志加父节点:"+symbolNode.getOpcode()+" 父节点："+tempNode.getOpcode());
		}
		if (newLine.startsWith("return")) {
			tempNode = null;
			this.cfgGraph.addEndNode(symbolNode);
		} else {
			tempNode = symbolNode;
		}
	}

	public String getOpcodeFromString(String line) {
		String[] s = line.split(" ");
		return s[0];
	}

	public void turnFatherToChild(String symbolOrSwitch) {
		try {
			if (!map.containsKey(symbolOrSwitch)) {
				log.severe("标志不存在错误，可能是哪里处理出错，将其错误的从map中删除了");
				throw new Exception();
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		CfgNode symbolNode = map.get(symbolOrSwitch);

		List<CfgNode> fatherList = symbolNode.getFatherCfgNode();
		List<CfgNode> childList = symbolNode.getChildCfgNode();
		if (fatherList != null && fatherList.size() != 0) {
			log.info("symbol:"+symbolNode.getOpcode()+" 有多少父节点："+symbolNode.getFatherCfgNode().size()+" 有多少子节点被重定向："+symbolNode.getChildCfgNode().size());
			for (CfgNode childNode:childList) {
				log.info("child:"+childNode.getOpcode());
			}
			for (CfgNode fatherNode:fatherList) {
				fatherNode.addChildCfgNode(childList);

			}
		}

	}


	public void generateInterCall(Map<String, SmaliMethod> smaliMethodMap) {
		if (this.cfgGraph == null){
			log.severe("基本的cfg未成功生成，请检查"+methodPackageString+" :"+methodNameString);
		}
		Map<String, CfgNode> mapInGraph = this.cfgGraph.getMap();
		if (methodNameString.contains("onJsConfirm")) {
			System.out.println(methodPackageString+"-"+methodNameString+"-"+methodArgsString);
		}
		this.cfgGraph.turnInterCallToVector(methodNameString, smaliMethodMap);

	}

	public String extractCalleeString(String line){
		SmaliMethod calleeMethod1=new SmaliMethod();
		Pattern pattern1=Pattern.compile("invoke-(.*?) \\{.*\\}, (.*?);->(.*?)\\((.*?)\\)(.*)");
		Matcher matcher1=pattern1.matcher(line);
		if(matcher1.find()){
			calleeMethod1.methodPackageString=matcher1.group(2);
			calleeMethod1.methodNameString=matcher1.group(3);

			calleeMethod1.methodArgsString=matcher1.group(4);
			calleeMethod1.returnValue=matcher1.group(5);

			calleeMethod1.replaceKeyWords();
			return (calleeMethod1.getMethodPackageString()+"-"+calleeMethod1.getMethodNameString()+"-" +calleeMethod1.getMethodArgsString());
		}
		return null;
	}


		
}
