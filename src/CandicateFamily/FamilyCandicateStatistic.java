package CandicateFamily;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.util.ArrayList;

import ConstantVar.ConstantValue;

public class FamilyCandicateStatistic {
			private String familyDirPathString="";
			private String familyName="";
			private File familyDirFile=null;
			private String predictRateString="";
			private int predictNum=0;
			private ArrayList<String> familyPredictStringList=new ArrayList<>();
			public FamilyCandicateStatistic(String familyPath){
				this.familyDirPathString=familyPath;
				File file=new File(familyPath);
				this.familyName=file.getName();
				this.familyDirFile=file;
				statisticAllAPKCandicate();
				writeFamilyPredictListToFile();
			}
			public void writeFamilyPredictListToFile(){
				try {
					String writeDirString=this.familyDirPathString+"FamilyInfo/CandicateFamilies/";
					File candicateFile=new File(writeDirString);
					if(!candicateFile.exists()){
						candicateFile.mkdir();
					}
					ConstantValue.getVar();
					String writeFileString=writeDirString+"top="+ConstantValue.TopFalNumber+".csv";
					File writeFile=new File(writeFileString);
					FileWriter fWriter=new FileWriter(writeFile);
					BufferedWriter bWriter=new BufferedWriter(fWriter);
					String lineString="";
					for(int i=0;i<familyPredictStringList.size();i++){
						lineString=familyPredictStringList.get(i);
						bWriter.write(lineString+"\n");
					}
					lineString = "Predict Rate in Candicate Families:		"+this.predictRateString;
					bWriter.write(lineString);
					bWriter.close();
					fWriter.close();
				} catch (Exception e) {
					// TODO: handle exception
					e.printStackTrace();
				}
			}
			public void statisticAllAPKCandicate(){
				String apkDirPathString=this.familyDirFile.getAbsolutePath()+"/apktool/";
				File dir=new File(apkDirPathString);
				File apks[]=dir.listFiles();
				int k=0;
				for(int i=0;i<apks.length;i++){
					String tmpString=apks[i].getAbsolutePath()+"/";
					APKCandiFal candiFal=new APKCandiFal(tmpString);
					String candicateFamilyInfo=candiFal.showCandicateFamilies();
					this.familyPredictStringList.add(candicateFamilyInfo);
				//	System.out.println(candiFal.showCandicateFamilies());
					if(candiFal.isInCandicateFal()){
						k++;
					}
				}
				this.predictNum=k;
				this.predictRateString=k+"/"+apks.length;
			}
			public String getStatisticString(){
				String resultString= this.familyName+":	"+this.predictRateString;
				return resultString;
			}
			public String getPredictRateString() {
				return predictRateString;
			}
			public void setPredictRateString(String predictRateString) {
				this.predictRateString = predictRateString;
			}
			public int getPredictNum() {
				return predictNum;
			}
			public void setPredictNum(int predictNum) {
				this.predictNum = predictNum;
			}
			public ArrayList<String> getFamilyPredictStringList() {
				return familyPredictStringList;
			}
			public void setFamilyPredictStringList(ArrayList<String> familyPredictStringList) {
				this.familyPredictStringList = familyPredictStringList;
			}
			
}
