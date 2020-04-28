package APKData.SmaliGraph;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

import ConstantVar.ConstantValue;

public class GraphToGexf {
		private String writeFilePathString;
		private MethodGraph graph;
		public GraphToGexf(MethodGraph graph ,String writeGexfFilePath){
			this.graph=new MethodGraph();
			this.graph=graph;
			this.writeFilePathString=writeGexfFilePath;
			writeFile();
		}
		public void writeFile(){
			try {
				File file=new File(writeFilePathString);
				FileWriter fWriter=new FileWriter(file);
				BufferedWriter bWriter=new BufferedWriter(fWriter);
				String lineString="";
				lineString +="<?xml version='1.0' encoding='UTF-8'?>\n"+
								"<gexf xmlns='qianniao918@qq.com' version='1.2' xmlns:viz='http://www.gexf.net/1.2draft/viz'>"+
								" xmlns:xsi='http://www.w3.org/2001/XMLSchema-instance'"+ 
								" xsi:schemaLocation='http://www.gexf.net/1.2draft http://www.gexf.net/1.2draft/gexf.xsd'>\n"+
								"	<meta lastmodifieddate='2014-9-21'>\n"+
								"		<creator>FanMing</creator>\n"+
								"		<description>An Android APK Method SCN!</description>\n"+
								"	</meta>\n"+
								"	<graph mode='static' defaultedgettype='directed'>\n"+
								"		<nodes>\n";
				bWriter.write(lineString);
				/*
				 *   写入节点信息
				 */
				Set<MethodNode> nodeSet=new HashSet<>();
				nodeSet=graph.getNodeSet();
				Iterator<MethodNode> nodeIterator=nodeSet.iterator();
				while(nodeIterator.hasNext()){
					MethodNode node=new MethodNode();
					node=nodeIterator.next();
					String nodeLabel=node.getNodeLabelString();
					String nodeType=node.getNodeTypeString();
					String nodeCategory=node.getNodeCategoryString();
					String nodeCommonString=node.getCommonString();
					
					String colorTypeString="";
					
					String r="30"; String g="144"; String b="255"; // 默认颜色为湖蓝色
					if(nodeType.equals("Source")){
						Color color=new Color();
						colorTypeString ="SOURCE$"+nodeCategory;
						ConstantValue.getVar();
						color=ConstantValue.colorCategoryMap.get(colorTypeString);
						r=color.getR(); g=color.getG(); b=color.getB();
						r="255"; g="0"; b="0";
					}
					else if(nodeType.equals("Sink")){
						Color color=new Color();
						colorTypeString="SINK$"+nodeCategory;
						ConstantValue.getVar();
						color=ConstantValue.colorCategoryMap.get(colorTypeString);
						r=color.getR(); g=color.getG(); b=color.getB();
						r="255"; g="0"; b="0";
					}
					String tmpLine="		<node id='"+nodeCommonString+"' label='"+colorTypeString+"'>\n"+
											   "			<attvalues></attvalues>\n"+
											   "			<viz:color r='"+r+"' g='"+g+"' b='"+b+"'></viz:color>\n"+
											   "			</node>\n";
					lineString =tmpLine;
					bWriter.write(lineString);
				}
			lineString ="		</nodes>\n";
			lineString +="		<edges>\n";
			bWriter.write(lineString);
			/*
			 *   写入边信息
			 */
			Set<MethodEdge> edgeSet=new HashSet<>();
			edgeSet=graph.getEdgeSet();
			Iterator<MethodEdge> edgeIterator=edgeSet.iterator();
			int id=0;
			while(edgeIterator.hasNext()){
				MethodEdge edge=new MethodEdge();
				edge=edgeIterator.next();
				String srcCommonString=edge.getCallerNode().getCommonString();
				String dstCommonString=edge.getCalleeNode().getCommonString();
				String tmpLine="			<edge id='"+id+"' source='"+srcCommonString+"' target='"+dstCommonString+"'/>\n";
				lineString =tmpLine;
				bWriter.write(lineString);
				id++;
			}
				lineString ="		</edges>\n";
				lineString +="	</graph>\n";
				lineString +="</gexf>";
				bWriter.write(lineString);
				
				bWriter.close();
				fWriter.close();
			} catch (Exception e) {
				// TODO: handle exception
			}
		}
		
}
