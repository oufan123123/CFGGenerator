package APKData.SmaliGraph;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

import APKData.APKTool;
import APKData.ReduceGraph.ReduceGraph;
import Util.Tool.DirDelete;
import brut.directory.DirectoryException;

/*
 *     最外层封装类，输入为一个apk文件路径，输出为一个文件夹，文件夹内是apk文件的
 *     反编译文件，并且新建一个子文件夹 SICG，内部包含
 *     SICG-source.gexf   完整的函数调用关系图
 *     SICG-reduce.gexf　约减的函数调用关系图
 *     SICG-reduce.dot    约减的函数调用关系图的dot存储格式
 *     source.txt　敏感源节点列表
 *     sink.txt　敏感目的节点列表
 *     Information.txt	该文件的基本相关信息，包括文件名，smali文件数目，节点个数，节点边数，敏感源节点个数，敏感目的节点个数
 *     
 */
public class SICG {
		private MethodGraph graph;
		private MethodGraph reduceGraph;
		private String apkFileString;
		private String outFileString;
		private String writeFileString;
		public SICG(String inAPKFileString, String outFileString) throws DirectoryException, IOException{
			this.apkFileString=inAPKFileString;
			this.outFileString=outFileString;
			apktool();  // 用apktool反编译到输出目录
			iniGraph();  // 构建初始化FCG图，未删减版本,分析代码显示是一个有向图，
			newSICGFile();  // 创建输出SICG文件夹目录，后序分析得到的东西存在
			writeInformationTXT();
			writeSourceTXT();
			writeSinkTXT();
			writeSourceGraphGexf();
			writeReducedGraphGexf();
			writeGraphDotFile();
			deleteMoreFile();
		}
		/*
		 *   删除多余的文件，如反编译后的源码文件夹以及资源文件夹
		 */
		public void deleteMoreFile(){
			File outfile=new File(outFileString);
			File files[]=outfile.listFiles();
			for(int i=0;i<files.length;i++){
				String name=files[i].getAbsolutePath();
				if((!name.contains("SICG"))&&(!name.contains("AndroidManifest.xml"))){
					DirDelete delete=new DirDelete();
					delete.deleteDir(files[i]);
				}
			}
		}
		public void apktool() throws DirectoryException, IOException{
			APKTool apkTool=new APKTool(apkFileString, outFileString);
		}
		public void iniGraph(){
			File file=new File(outFileString);
			 MethodGraph graph=new MethodGraph(file);
			 this.graph=new MethodGraph();
			 this.graph=graph;
		}
		public void newSICGFile(){
			try {
				File file=new File(outFileString+"/SICG/");
				file.mkdir();
				this.writeFileString=outFileString+"/SICG/";
			} catch (Exception e) {
				// TODO: handle exception
				e.printStackTrace();
			}
		}
		public void writeInformationTXT(){
			String writeString=this.writeFileString+"Information.txt";
			try {
				File  file=new File(writeString);
				FileWriter fWriter=new FileWriter(file);
				BufferedWriter bWriter=new BufferedWriter(fWriter);
				String lineString= this.graph.getGraphInformation();
				bWriter.write(lineString);
				bWriter.close();
				fWriter.close();
				System.out.println("Finish writing Information.txt");
			} catch (Exception e) {
				// TODO: handle exception
				e.printStackTrace();
			}
		}
		public void writeSourceTXT(){
			String writeString=this.writeFileString+"Source.txt";
			try {
				File  file=new File(writeString);
				FileWriter fWriter=new FileWriter(file);
				BufferedWriter bWriter=new BufferedWriter(fWriter);
				String lineString="";
				Set<MethodNode> sourceNodes=new HashSet<>();
				sourceNodes=this.graph.getSourceNodeSet();
				if(sourceNodes.size()==0){
					System.out.println("No Source Nodes!!!");
				}
				Iterator<MethodNode> sourceIterator=sourceNodes.iterator();
				while(sourceIterator.hasNext()){
					MethodNode node=sourceIterator.next();
					String nameString=node.getNodeLabelString();
					String categoryString=node.getNodeCategoryString();
					lineString += nameString +","+categoryString+"\n";
				}
				bWriter.write(lineString);
				bWriter.close();
				fWriter.close();
				System.out.println("Finish writing Source.txt");
			} catch (Exception e) {
				// TODO: handle exception
				e.printStackTrace();
			}
		}
		public void writeSinkTXT(){
			String writeString=this.writeFileString+"Sink.txt";
			try {
				File  file=new File(writeString);
				FileWriter fWriter=new FileWriter(file);
				BufferedWriter bWriter=new BufferedWriter(fWriter);
				String lineString="";
				Set<MethodNode> sinkNodes=new HashSet<>();
				sinkNodes=this.graph.getSinkNodeSet();
				if(sinkNodes.size()==0){
					System.out.println("No Source Nodes!!!");
				}
				Iterator<MethodNode> sinkIterator=sinkNodes.iterator();
				while(sinkIterator.hasNext()){
					MethodNode node=sinkIterator.next();
					String nameString=node.getNodeLabelString();
					String categoryString=node.getNodeCategoryString();
					lineString += nameString +","+categoryString+"\n";
				}
				bWriter.write(lineString);
				bWriter.close();
				fWriter.close();
				System.out.println("Finish writing Sink.txt");
			} catch (Exception e) {
				// TODO: handle exception
				e.printStackTrace();
			}
		}
		public void writeSourceGraphGexf(){
			String writeString=this.writeFileString+"SourceGraph.gexf";
			GraphToGexf graphToGexf=new GraphToGexf(this.graph, writeString);
			System.out.println("Finish writing SourceGraph.gexf");
		}
		public void writeReducedGraphGexf(){
			String writeString=this.writeFileString+"ReducedGraph.gexf";
			ReduceGraph reduceGraph=new ReduceGraph(this.graph);
			GraphToGexf graphToGexf=new GraphToGexf(reduceGraph.getReduceGraph(), writeString);
			System.out.println("Finish writing ReducedGraph.gexf");
			this.reduceGraph=new MethodGraph();
			this.reduceGraph=reduceGraph.getReduceGraph();
		}
		public void writeGraphDotFile(){
			String writeString=this.writeFileString+"ReducedGraph.dot";
			GraphToDot graphToDot=new GraphToDot(this.reduceGraph, writeString);
			System.out.println("Finish writing ReducedGraph.dot");
		}
}
