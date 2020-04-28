package APKData.SmaliGraph;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.util.HashSet;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/*
 *   输入为一个Gexf文件，将其转换成图
 *   并且更新图中每个节点的入度集合和初度集合
 */
public class GexfToGraph {
			private String gexfFilePath="";
			private MethodGraph graph;
			private Set<MethodNode> nodeSet=new HashSet<>();
			private Set<MethodEdge> edgeSet=new HashSet<>();
			public GexfToGraph(String gexfFileString){
				this.gexfFilePath=gexfFileString;
				graph=new MethodGraph();
				iniGraph();
			}
			public GexfToGraph(){
				graph=new MethodGraph();
			}
			public void iniGraph(){
				try {
					File gexfFile=new File(gexfFilePath);
					FileReader fReader=new FileReader(gexfFile);
					BufferedReader bReader=new BufferedReader(fReader);
					String lineString="";
					while((lineString=bReader.readLine())!=null){
						lineString=lineString.trim();
						if(lineString.startsWith("<node id=")){
							MethodNode node=new MethodNode();
							node=strToNode(lineString);
							nodeSet.add(node);
						}
						else if(lineString.startsWith("<edge id=")){
							MethodEdge edge=new MethodEdge();
							edge=strToEdge(lineString);
							edgeSet.add(edge);
						}
					}
					bReader.close();
					fReader.close();
					
					graph.setNodeSet(nodeSet);
					graph.setEdgeSet(edgeSet);
					// 存储父节点集合以及子节点集合的数据结构是graph对象中的nodeMap
					//  注意nodeSet中的节点对象并不包含父节点以及子节点相关信息
					graph.iniChildrenNodesAndParentNodes();
				} catch (Exception e) {
					// TODO: handle exception
					e.printStackTrace();
				}
			}
			public MethodNode strToNode(String inputString){
				MethodNode node=new MethodNode();
				String regex="<node id='(.*?)' label='(.*?)'>";
				Pattern pattern=Pattern.compile(regex);
				Matcher matcher=pattern.matcher(inputString);
				if(matcher.find()){
					String nodeCommonString=matcher.group(1);
					 node=new MethodNode(nodeCommonString);
					}
				return node;
			}
			public MethodEdge strToEdge(String inputString){
				MethodEdge edge=new MethodEdge();
				String regex="<edge id='(.*?)' source='(.*?)' target='(.*?)'/>";
				Pattern pattern=Pattern.compile(regex);
				Matcher matcher=pattern.matcher(inputString);
				if(matcher.find()){
					String srcNodeCommonString=matcher.group(2);
					String dstNodeCommonString=matcher.group(3);
					MethodNode srcNode=new MethodNode(srcNodeCommonString);
					MethodNode dstNode=new MethodNode(dstNodeCommonString);
					edge=new MethodEdge(srcNode, dstNode);
				}
				return edge;
			}
			public MethodGraph getGraph() {
				return graph;
			}
			public void setGraph(MethodGraph graph) {
				this.graph = graph;
			}
			
}
