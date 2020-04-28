package TFIDF;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.util.ArrayList;

public class APKInfo {
		private String apkInfoFilePathString;
		private ArrayList<SensitiveMethod>  sensitiveMethodsList=new ArrayList<>();
		
		public APKInfo(String infoFilePath){
			this.apkInfoFilePathString=infoFilePath;
			String sourceFilePath=this.apkInfoFilePathString +"Source.txt";
			String sinkFilePath= this.apkInfoFilePathString +"Sink.txt";
			File sourceFile=new File(sourceFilePath);
			File sinkFile=new File(sinkFilePath);
			iniSensitiveMethodList(sourceFile);
			iniSensitiveMethodList(sinkFile);
		}
		public void iniSensitiveMethodList(File file){
			try {
				String typeString="";
				if(file.getName().contains("Source")){
					typeString="Source";
				}
				else if(file.getName().contains("Sink")){
					typeString="Sink";
				}
				FileReader fReader=new FileReader(file);
				BufferedReader bReader=new BufferedReader(fReader);
				String lineString="";
				while((lineString=bReader.readLine())!=null){
					SensitiveMethod sensitiveMethod=new SensitiveMethod(lineString, typeString);
					sensitiveMethodsList.add(sensitiveMethod);
				}
				bReader.close();
				fReader.close();
			} catch (Exception e) {
				// TODO: handle exception
				e.printStackTrace();
			}
			
		}
		public String getApkInfoFilePathString() {
			return apkInfoFilePathString;
		}
		public void setApkInfoFilePathString(String apkInfoFilePathString) {
			this.apkInfoFilePathString = apkInfoFilePathString;
		}
		public ArrayList<SensitiveMethod> getSensitiveMethodsList() {
			return sensitiveMethodsList;
		}
		public void setSensitiveMethodsList(
				ArrayList<SensitiveMethod> sensitiveMethodsList) {
			this.sensitiveMethodsList = sensitiveMethodsList;
		}
}
