package APKData;

import java.io.File;
import java.io.IOException;

import org.apache.commons.lang3.StringUtils;

import brut.androlib.AndrolibException;
import brut.androlib.ApkDecoder;
import brut.directory.DirectoryException;

/*
 *    输入为一个apk文件的绝对路径以及其预设输出路径，调用apktool命令工具，对其进行反编译
 *    
 */
public class APKTool {
	private String inputPathString;
	private String outputPathString;
	public APKTool(String inputPath, String outputPath) throws DirectoryException, IOException{
		ApkDecoder apkDecoder=new ApkDecoder();
		String [] s=StringUtils.split(inputPath,"\\\\|\\.");
		File file=new File(inputPath);
	    String testPath=outputPath;
		try {
			apkDecoder.setOutDir(new File(testPath));
		} catch (AndrolibException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
			
		}
		apkDecoder.setApkFile(file);
		try {
			apkDecoder.decode();
		} catch (AndrolibException e) {
			
			e.printStackTrace();
			
		}
	}
	
	public String getInputPathString() {
		return inputPathString;
	}
	public void setInputPathString(String inputPathString) {
		this.inputPathString = inputPathString;
	}
	public String getOutputPathString() {
		return outputPathString;
	}
	public void setOutputPathString(String outputPathString) {
		this.outputPathString = outputPathString;
	}
	
	
}
