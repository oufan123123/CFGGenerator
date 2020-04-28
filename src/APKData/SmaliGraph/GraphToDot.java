package APKData.SmaliGraph;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

public class GraphToDot {
	private String writeFilePathString;
	private MethodGraph graph;
	public GraphToDot(MethodGraph srcGraph, String writeString){
		this.graph=new MethodGraph();
		this.graph=srcGraph;
		writeFilePathString=writeString;
		writeFile();
	}
	public void writeFile(){
		try {
			File file=new File(writeFilePathString);
			FileWriter fWriter=new FileWriter(file);
			BufferedWriter bWriter=new BufferedWriter(fWriter);
			String lineString="";
			lineString="digraph G{\n";
			bWriter.write(lineString);
			Set<MethodNode> nodeSet=new HashSet<>();
			nodeSet=this.graph.getNodeSet();
			Iterator<MethodNode> nodeIterator=nodeSet.iterator();
			while(nodeIterator.hasNext()){
				MethodNode node=nodeIterator.next();
				String nodeName=node.getCommonString();
				lineString="	\""+nodeName+"\";\n";
				bWriter.write(lineString);
			}
			Set<MethodEdge> edgeSet=new HashSet<>();
			edgeSet=this.graph.getEdgeSet();
			Iterator<MethodEdge> edgeIterator=edgeSet.iterator();
			while(edgeIterator.hasNext()){
				MethodEdge edge=edgeIterator.next();
				String srcString=edge.getCallerNode().getCommonString();
				String dstString=edge.getCalleeNode().getCommonString();
				int k=srcString.indexOf(":");
				String labelString=srcString.substring(0,k);   //label　标为源节点的类名
				lineString="	\""+srcString+"\"->\""+dstString+"\"[label=\""+labelString+"\"];\n";
				bWriter.write(lineString);
			}
			lineString="}";
			bWriter.write(lineString);
			bWriter.close();
			fWriter.close();
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}
	}
}
