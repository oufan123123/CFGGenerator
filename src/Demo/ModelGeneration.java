package Demo;

import java.io.File;
import java.io.IOException;
import java.util.logging.Logger;

import APKData.SmaliGraph.SICG;
import Processing.OpcodeContent;
import brut.directory.DirectoryException;

import Logger.LogFatory;

public class ModelGeneration {

	Logger logger = LogFatory.getGlobalLogger();
	//public static String inputFilePath="/home/fan/data/Family/demoTest/demo3/1e303d5a5787dd32045a22c85b068547.apk";
	public static String inputFilePath="F:/test_processing/7.apk";
	public static void main(String[] args) throws Exception {
		// TODO Auto-generated method stub
		// the input format of apk file is like:   a/b/c.apk
		/*
		 * int k=args.length; if(k==0){ System.err.println("There is no argument!"); }
		 * else{ inputFilePath=args[0]; construct(inputFilePath); }
		 */
		OpcodeContent.initOpcodeContent();
		construct(inputFilePath);

	}
	public static void construct(String inputFilePath) throws DirectoryException, IOException{
		int k=inputFilePath.lastIndexOf("/");
		String outAPKFile=inputFilePath.substring(0,k)+"/apktool/";
		String apkName=inputFilePath.substring(k+1);
		File dirFile=new File(outAPKFile);
		if(!dirFile.exists()){
			dirFile.mkdir();
		}
		if(inputFilePath.endsWith(".apk")){
				long startTime=System.currentTimeMillis();
				SICG sicg=new SICG(inputFilePath,dirFile.getAbsolutePath()+"/"+apkName);
				long endTime=System.currentTimeMillis();
				long useTime=endTime-startTime;
		}
	
	}

}
