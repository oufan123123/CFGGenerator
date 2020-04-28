package APKData.SmaliGraph;

import java.io.Serializable;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

import APKData.SmaliData.SmaliMethod;
import ConstantVar.ConstantValue;
import ConstantVar.SourceAndSink.ReadSourceAndSinks;
import ConstantVar.SourceAndSink.SSMethod;

public class MethodNode implements Serializable{
	private SmaliMethod nodeMethod;
	private String nodeLabelString="";
	private String nodeTypeString="";
	private Set<MethodNode> childrenNodes=new HashSet<>();
	private Set<MethodNode> parentNodes=new HashSet<>();
	private String nodeCategoryString="";
	private String commonString="";
	
	public MethodNode(){}
	public MethodNode(SmaliMethod method){
		this.nodeMethod=method;
		this.nodeLabelString=method.getEntityMethodString();
		//System.out.println(nodeLabelString);
		int k=nodeLabelString.indexOf("(");
		String packMethodString=nodeLabelString.substring(0,k);
		iniType(packMethodString);
	}
	public MethodNode(String commonString){
		String methodName="";
		String typeString="";
		String categoryString="";
		if(commonString.contains("Normal")){
			String arg[]=commonString.split(",");
			methodName=arg[0];
			typeString="Normal";
		}
		else{
			String arg[]=commonString.split(",");
			methodName=arg[0];
			typeString=arg[1];
			categoryString=arg[2];
		}
		this.nodeLabelString=methodName;
		this.nodeTypeString=typeString;
		this.nodeCategoryString=categoryString;
	}
	public void iniType(String str){
		 if(isSourceMethod(str)){
			 this.nodeTypeString="Source";
		 }
		 else if(isSinkMethod(str)){
			 this.nodeTypeString="Sink";
		 }
		 else
			 this.nodeTypeString="Normal";
	}
	public boolean isSourceMethod(String str){
		Map<String, String> sourceMap=new HashMap<String, String>();
		ConstantValue.getVar();
		sourceMap=ReadSourceAndSinks.getSourceStringMap();
		if(sourceMap.containsKey(str)){
			String categoryString=sourceMap.get(str);
			this.nodeCategoryString=categoryString;
			return true;
		}
		else
			return false;
	}
	public boolean isSinkMethod(String str){
		Map<String, String> sinkMap=new HashMap<String,String>();
	    ConstantValue.getVar();
		sinkMap=ReadSourceAndSinks.getSinkStringMap();
		if(sinkMap.containsKey(str)){
			String categoryString=sinkMap.get(str);
			this.nodeCategoryString=categoryString;
			return true;
		}
		else
			return false;
	}
	public String getCommonString(){
		String result=this.nodeLabelString+","+this.nodeTypeString+","+this.nodeCategoryString;
		return result;
	}
	public void addChildNode(MethodNode childNode){
		this.childrenNodes.add(childNode);
	}
	public void addParentNode(MethodNode parentNode){
		this.parentNodes.add(parentNode);
	}
	public SmaliMethod getNodeMethod() {
		return nodeMethod;
	}
	public void setNodeMethod(SmaliMethod nodeMethod) {
		this.nodeMethod = nodeMethod;
	}
	public String getNodeLabelString() {
		return nodeLabelString;
	}
	public void setNodeLabelString(String nodeLabelString) {
		this.nodeLabelString = nodeLabelString;
	}
	public String getNodeTypeString() {
		return nodeTypeString;
	}
	public void setNodeTypeString(String nodeTypeString) {
		this.nodeTypeString = nodeTypeString;
	}
	
	public Set<MethodNode> getChildrenNodes() {
		return childrenNodes;
	}
	public void setChildrenNodes(Set<MethodNode> childrenNodes) {
		this.childrenNodes = childrenNodes;
	}
	public Set<MethodNode> getParentNodes() {
		return parentNodes;
	}
	public void setParentNodes(Set<MethodNode> parentNodes) {
		this.parentNodes = parentNodes;
	}
	
	public String getNodeCategoryString() {
		return nodeCategoryString;
	}
	public void showParentNodes(){
		Iterator<MethodNode> parNodeIterator=this.parentNodes.iterator();
		while(parNodeIterator.hasNext()){
			MethodNode node=parNodeIterator.next();
			System.out.println(node.getCommonString());
		}
	}
	public void showChildrenNodes(){
		Iterator<MethodNode> chIterator=this.childrenNodes.iterator();
		System.out.println(childrenNodes.size());
		while(chIterator.hasNext()){
			MethodNode node=chIterator.next();
			System.out.println(node.getCommonString());
		}
	}
	public void setNodeCategoryString(String nodeCategoryString) {
		this.nodeCategoryString = nodeCategoryString;
	}
	@Override
	public int hashCode() {
		// TODO Auto-generated method stub
		return this.nodeLabelString.hashCode();
	}
	@Override
	public boolean equals(Object obj) {
		// TODO Auto-generated method stub
		MethodNode tmpNode=(MethodNode)obj;
		if(this.nodeLabelString.equals(tmpNode.nodeLabelString)){
			return true;
		}
		else {
			return false;
		}
	}
	
}
