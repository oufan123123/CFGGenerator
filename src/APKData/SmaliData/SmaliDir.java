package APKData.SmaliData;

import java.io.File;
import java.util.ArrayList;

/*
 *     输入为一个apk反编译后的文件夹对象, File 类型
 *     并且构建了一个文件树类，树上的中间节点表示文件夹，叶子节点表示smali文件
 */
public class SmaliDir {
	private String smaliDirPath;   // 当前smali文件夹的绝对路径
	private fileTree tree;    // 文件数对象
	private ArrayList<String> classFilePathList=new ArrayList<>();
	private ArrayList<String> dirFilePathList=new ArrayList<>();
	private ArrayList<SmaliClass> classesList=new ArrayList<>();
	private int fileNum;
	public  SmaliDir(File file){
		String apkPathString=file.getAbsolutePath();
		String smaliDirPath=apkPathString+"/smali/";
		File smaliDirFile=new File(smaliDirPath);
		if(smaliDirFile.exists()){
			this.smaliDirPath=smaliDirFile.getAbsolutePath()+"/";
			tree=new fileTree(smaliDirFile); // 构建文件树
			fileNum=classFilePathList.size();
			System.out.println("文件："+file.getAbsolutePath());
			System.out.println("文件夹数目："+dirFilePathList.size());
			System.out.println("文件数目："+fileNum);
		}
		else {
			System.err.print(" There is no smali file in the apk file: "+ apkPathString);
		}
	}
	
	public String getSmaliDirPath() {
		return smaliDirPath;
	}
	public void setSmaliDirPath(String smaliDirPath) {
		this.smaliDirPath = smaliDirPath;
	}
	public fileTree getTree() {
		return tree;
	}
	public void setTree(fileTree tree) {
		this.tree = tree;
	}
	public ArrayList<String> getClassFilePathList() {
		return classFilePathList;
	}
	public void setClassFilePathList(ArrayList<String> classFilePathList) {
		this.classFilePathList = classFilePathList;
	}
	public ArrayList<String> getDirFilePathList() {
		return dirFilePathList;
	}
	public void setDirFilePathList(ArrayList<String> dirFilePathList) {
		this.dirFilePathList = dirFilePathList;
	}
	public ArrayList<SmaliClass> getClassesList() {
		return classesList;
	}
	public void setClassesList(ArrayList<SmaliClass> classesList) {
		this.classesList = classesList;
	}
	public int getFileNum() {
		return fileNum;
	}
	public void setFileNum(int fileNum) {
		this.fileNum = fileNum;
	}

	/*
	 *       the files construct a tree,each node stands for a file
	 */
	class fileTree{
		private fileNode rootFileNode;
		public fileTree(File file){
			rootFileNode=new fileNode(file);
			addTreeNode(rootFileNode);
		}
		public void addTreeNode(fileNode tmpNode){
			File [] files=tmpNode.nodeFile.listFiles();
			if(files.length>0){
				for(int i=0;i<files.length;i++){
					fileNode node=new fileNode(files[i]);
					tmpNode.addNode(node);
					if(files[i].isDirectory()){
						dirFilePathList.add(files[i].getAbsolutePath());
						addTreeNode(node);
					}
					else { // this node is a file which end with smali , then analysis this file
						 classFilePathList.add(files[i].getAbsolutePath());
						 SmaliClass smaliClass=new SmaliClass(files[i]);
						 classesList.add(smaliClass);
					}
				}
			}
		}
	}
	/*
	 * 
	 */
	class fileNode{
		private File nodeFile;
		private fileNode parentNode;
		private String nodeType;      // 若为 dir　表示该节点为文件夹，　若为　File　表示该节点为smali文件
		private ArrayList<fileNode> childrenNodes=new ArrayList<>();
		public fileNode(File file){
			this.nodeFile=file;
			parentNode=null;
			if(file.isDirectory()){
				this.nodeType="Dir";
			}
			else {
				this.nodeType="File";
			}
		}
		public void addNode(fileNode node){
			node.parentNode=this;
			this.childrenNodes.add(node);
		}
		
	}
}
