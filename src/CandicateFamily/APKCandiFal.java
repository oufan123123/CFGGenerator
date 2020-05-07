package CandicateFamily;

import Util.Tool.MapSort;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import ConstantVar.ConstantValue;

public class APKCandiFal {
		private String apkPathString="";
		private String actualFamilyString="";
		private String predictFamilyString="";
		private ArrayList<String> sensitiveMethodsList=new ArrayList<>();
		private Map<String, Double> simFamilyScoreMap=new HashMap<String, Double>();
		private List<Entry<String, Double>> candicateFamilyList=new ArrayList<>();
		private boolean inCandicateFal=false;
		private int cadicateIndex=-1;
		public APKCandiFal(String apkString){
			this.apkPathString=apkString;
			iniActualFamilyString(apkString);
			iniSensitiveMethodsList();
			calculateSimFamily();
			this.candicateFamilyList=getCandicateFamilyList();
		}
		public void iniActualFamilyString(String pathString){
			int k1=pathString.lastIndexOf("/apktool/");
			pathString = pathString.substring(0,k1);
			int k2=pathString.lastIndexOf("/");
			this.actualFamilyString=pathString.substring(k2+1);
		}
		public void iniSensitiveMethodsList(){
			String sourceFilePathString=this.apkPathString+"SICG/Source.txt";
			String sinkFilePathString=this.apkPathString+"SICG/Sink.txt";
			readFile(sourceFilePathString);
			readFile(sinkFilePathString);
		}
		public void readFile(String filePath){
			File file=new File(filePath);
			try {
				FileReader fReader=new FileReader(file);
				BufferedReader bReader=new BufferedReader(fReader);
				String lineString="";
				while((lineString=bReader.readLine())!=null){
					String str[]=lineString.split(",");
					String nameString=str[0];
					String typeString="";
					String categoryString=str[1];
					if(filePath.contains("Source")){
						typeString="Source";
					}
					else if(filePath.contains("Sink")){
						typeString="Sink";
					}
					String tmpString=nameString+","+typeString+","+categoryString;
					this.sensitiveMethodsList.add(tmpString);
				}
				bReader.close();
				fReader.close();
				
			} catch (Exception e) {
				// TODO: handle exception
				e.printStackTrace();
			}
		}
		public void calculateSimFamily(){
			ConstantValue.getVar();
			String familyDir=ConstantValue.FAMILIESDIRPATH_STRING;
			File familyFile=new File(familyDir);
			File family[]=familyFile.listFiles();
			for(int i=0;i<family.length;i++){
				double d=simWithOneFamily(family[i]);
				String familyName=family[i].getName();
				this.simFamilyScoreMap.put(familyName, d);
			//	System.out.println(familyName+"###"+d);
			}
		}
		public double simWithOneFamily(File family){
			double simScore=0.0D;
			String weightFilePathString=family.getAbsolutePath()+"/FamilyInfo/MethodWeight.txt";
			Map<String, Double> familyWeightMap=new HashMap<>();
			try {
				File readFile=new File(weightFilePathString);
				FileReader fReader=new FileReader(readFile);
				BufferedReader bufferedReader=new BufferedReader(fReader);
				String lineString="";
				while((lineString=bufferedReader.readLine())!=null){
					String str[]=lineString.split("#");
					String methodNameString=str[0];
					String scoreWeight=str[1];
					double score=Double.valueOf(scoreWeight);
					familyWeightMap.put(methodNameString, score);
				}
				bufferedReader.close();
				fReader.close();
				for(int i=0;i<sensitiveMethodsList.size();i++){
					String tmpString=sensitiveMethodsList.get(i);
					if(familyWeightMap.containsKey(tmpString)){
						double tmpScore=familyWeightMap.get(tmpString);
						simScore +=tmpScore;
					}
				}
			} catch (Exception e) {
				// TODO: handle exception
				e.printStackTrace();
			}
			return simScore;
		}
		public List<Entry<String, Double>> getCandicateFamilyList(){
			List<Entry<String, Double>> familySimScoreList=new ArrayList<>();
			MapSort<String, Double> mapSort=new MapSort<>();
			familySimScoreList=mapSort.sortMapByValue(simFamilyScoreMap);
			ConstantValue.getVar();
			for(int i=0;i<ConstantValue.TopFalNumber;i++){
				Entry<String, Double> entry=familySimScoreList.get(i);
				String nameString=entry.getKey();
				if(this.actualFamilyString.equals(nameString)){
					this.inCandicateFal=true;
					this.cadicateIndex=i+1;
				}
			}
			return familySimScoreList;
		}
		public String showCandicateFamilies(){
			String resultString="";
			ConstantValue.getVar();
			int k=ConstantValue.TopFalNumber;
			resultString += this.actualFamilyString+",";
			int actualNumer=-1;
			for(int i=0;i<k;i++){
				Entry<String, Double> entry=candicateFamilyList.get(i);
				String nameString=entry.getKey();
				Double score=entry.getValue();
				DecimalFormat    df   = new DecimalFormat("######0.000");   
				String value=df.format(score);
				resultString +=nameString+"="+value+",";
				if(nameString.equals(this.actualFamilyString)){
					actualNumer=i+1;
				}
			}
			resultString +="Index: "+actualNumer;
			return resultString;
		}
		public String getApkPathString() {
			return apkPathString;
		}
		public void setApkPathString(String apkPathString) {
			this.apkPathString = apkPathString;
		}
		public String getActualFamilyString() {
			return actualFamilyString;
		}
		public void setActualFamilyString(String actualFamilyString) {
			this.actualFamilyString = actualFamilyString;
		}
		public String getPredictFamilyString() {
			return predictFamilyString;
		}
		public void setPredictFamilyString(String predictFamilyString) {
			this.predictFamilyString = predictFamilyString;
		}
		public Map<String, Double> getSimFamilyScoreMap() {
			return simFamilyScoreMap;
		}
		public void setSimFamilyScoreMap(Map<String, Double> simFamilyScoreMap) {
			this.simFamilyScoreMap = simFamilyScoreMap;
		}
		public boolean isInCandicateFal() {
			return inCandicateFal;
		}
		public void setInCandicateFal(boolean inCandicateFal) {
			this.inCandicateFal = inCandicateFal;
		}
		public int getCadicateIndex() {
			return cadicateIndex;
		}
		public void setCadicateIndex(int cadicateIndex) {
			this.cadicateIndex = cadicateIndex;
		}
		public void setCandicateFamilyList(
				List<Entry<String, Double>> candicateFamilyList) {
			this.candicateFamilyList = candicateFamilyList;
		}
		
}
