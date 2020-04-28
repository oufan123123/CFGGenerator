package APKData.SmaliGraph;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.util.HashSet;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class DotToGraph {
		private String srcDotFilePathString="";
		private MethodGraph graph=new MethodGraph();
		private Set<MethodNode> nodeSet=new HashSet<>();
		private Set<MethodEdge> edgeSet=new HashSet<>();
		
		public DotToGraph(String srcDotFileString){
			this.srcDotFilePathString=srcDotFileString;
			readDotFile();
			graph.setNodeSet(nodeSet);
			graph.setEdgeSet(edgeSet);
			
	//		graph.showNodeSet();
		}
		public DotToGraph(){}
		public void readDotFile(){
			try {
				File file=new File(srcDotFilePathString);
				if(!file.exists()){
					System.err.println("Cannot find file :"+srcDotFilePathString);
				}
				String lineString="";
				FileReader fReader=new FileReader(file);
				BufferedReader bReader=new BufferedReader(fReader);
				lineString=bReader.readLine();
				while((lineString=bReader.readLine())!=null){
					if(lineString.contains(";")){
						if(lineString.contains("->")){
							/*
							 *    抽取一条函数调用
							 */
							lineString=lineString.trim();
							String regex="(.*?)->(.*?)\\[label=(.*?)\\];";
							Pattern pattern=Pattern.compile(regex);
							Matcher matcher=pattern.matcher(lineString);
							if(matcher.find()){
								String srcNodeString=matcher.group(1);
								String dstNodeString=matcher.group(2);
								MethodNode srcNode=new MethodNode();
								MethodNode dstNode=new MethodNode();
								srcNode=strToNode(srcNodeString.replace("\"", ""));
								dstNode=strToNode(dstNodeString.replace("\"", ""));
								MethodEdge edge=new MethodEdge();
								edge.setCallerNode(srcNode);
								edge.setCalleeNode(dstNode);
								edgeSet.add(edge);                                                     //添加到边集合
								nodeSet.add(srcNode);  nodeSet.add(dstNode);      //添加到节点集合
							}
						}
						else {
							/*
							 * 　　抽取一个函数声明
							 */
						}
					}
				}
				bReader.close();
				fReader.close();
			} catch (Exception e) {
				// TODO: handle exception
				e.printStackTrace();
			}
			
		}
		public MethodNode strToNode(String str){
			MethodNode node=new MethodNode();
			String arg[]=str.split(",");
			String name=arg[0];
			String type=arg[1];
			node.setNodeLabelString(name);
			node.setNodeTypeString(type);
			if(type.equals("Normal")){
				node.setNodeCategoryString("");
			}
			else {
				String category=arg[2];
				node.setNodeCategoryString(category);
			}
	//		System.out.println(node.getCommonString());
			return node;
		}
		public String getSrcDotFilePathString() {
			return srcDotFilePathString;
		}
		public void setSrcDotFilePathString(String srcDotFilePathString) {
			this.srcDotFilePathString = srcDotFilePathString;
		}
		public MethodGraph getGraph() {
			return graph;
		}
		public void setGraph(MethodGraph graph) {
			this.graph = graph;
		}
		
}
