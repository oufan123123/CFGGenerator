package TFIDF;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import Util.Tool.MapSort;

public class FamilyInfo {
		private String familyDirPathString;
		private int sampleNumber;
		private ArrayList<APKInfo> apkInfoList=new ArrayList<>();
		private Map<SensitiveMethod, Integer> methodNumberMap=new HashMap<>();
		private Map<SensitiveMethod, Double> methodWeightMap=new HashMap<>();
		private List<Entry<SensitiveMethod, Integer>> methodNumberList=new ArrayList<>();
		private List<Entry<SensitiveMethod, Double>> methodWeightList=new ArrayList<>();
		
		
		public FamilyInfo(String dirPathString){
			this.familyDirPathString=dirPathString;
			iniApkInfoList();
			iniMethodNumberMap();
			MapSort sort=new MapSort();
			this.methodNumberList= sort.sortMapByValue((HashMap<SensitiveMethod, Integer>) this.methodNumberMap);
		//	writeFamilyInfo();
		}
		public void iniApkInfoList(){
			String apkToolDirString= familyDirPathString +"apktool/";
			File apktoolFile=new File(apkToolDirString);
			File disApkInfo[] = apktoolFile.listFiles();
			this.sampleNumber=disApkInfo.length;
			for(int i=0;i<disApkInfo.length;i++){
				String apkInfoString=disApkInfo[i].getAbsolutePath() +"/SICG/";
				APKInfo apkInfo=new APKInfo(apkInfoString);
				apkInfoList.add(apkInfo);
			}
		}
		public void iniMethodNumberMap(){
			for(int i=0;i<apkInfoList.size();i++){
				APKInfo apkInfo=apkInfoList.get(i);
				ArrayList<SensitiveMethod> senMethodsList=new ArrayList<>();
				senMethodsList = apkInfo.getSensitiveMethodsList();
				for(int j=0;j<senMethodsList.size();j++){
					SensitiveMethod sensitiveMethod=senMethodsList.get(j);
					if(this.methodNumberMap.containsKey(sensitiveMethod)){
					//	System.out.println(sensitiveMethod.getString());
						int k=this.methodNumberMap.get(sensitiveMethod);
						k ++;
						this.methodNumberMap.remove(sensitiveMethod);
						this.methodNumberMap.put(sensitiveMethod, k);
					}
					else {
						this.methodNumberMap.put(sensitiveMethod, 1);
					}
				}
			}
		}
		public void calculateWeight(Map<SensitiveMethod, Integer> allMap, int totalNumber){
			Iterator<SensitiveMethod> iterator=this.methodNumberMap.keySet().iterator();
			while(iterator.hasNext()){
				SensitiveMethod method=iterator.next();
				double k1=Double.valueOf(this.methodNumberMap.get(method));
				double k2=Double.valueOf(sampleNumber);
				double d1=Double.valueOf(allMap.get(method));
				double d2=Double.valueOf(totalNumber);
				double result=(k1/k2)*(Math.log10(d2/d1));
				this.methodWeightMap.put(method, result);
			}
			MapSort<SensitiveMethod, Double> mapSort=new MapSort<>();
			this.methodWeightList= mapSort.sortMapByValue(methodWeightMap);
		}
		public void showMethodNumberMap(){
			for(int i=0;i<methodNumberList.size();i++){
				Entry<SensitiveMethod, Integer> entry=methodNumberList.get(i);
				System.out.println(entry.getKey().getString()+"# "+entry.getValue());
			}
		}
		public void writeFamilyInfo(Map<SensitiveMethod, Integer> allMap, int totalNum){
			String familyInfoDirString=this.familyDirPathString +"FamilyInfo/";
			File file=new File(familyInfoDirString);
			if(!file.exists()){
				file.mkdir();
			}
			String basicInfoFileString=familyInfoDirString +"BasicInfo.txt";
			String methodNumberString=familyInfoDirString +"MethodNumber.txt";
			String methodWeightString=familyInfoDirString +"MethodWeight.txt";
			
			writeMethodNumberFile(methodNumberString, allMap, totalNum);
			writeMethodWeightFile(methodWeightString, allMap, totalNum);
		}
		public void writeMethodWeightFile(String writeString, Map<SensitiveMethod, Integer> allMap, int totalNum){
			try {
				File file=new File(writeString);
				FileWriter fWriter=new FileWriter(file);
				BufferedWriter bWriter=new BufferedWriter(fWriter);
				for(int i=0;i<methodWeightList.size();i++){
					Entry<SensitiveMethod, Double> entry=methodWeightList.get(i);
					DecimalFormat    df   = new DecimalFormat("######0.000");   
					String value=df.format(entry.getValue());
					int numInFal=this.methodNumberMap.get(entry.getKey());
					int numInAll=allMap.get(entry.getKey());
					
					String lineString = entry.getKey().getString()+"#"+value+"#"+numInFal+"/"+sampleNumber+"#"+numInAll+"/"+totalNum+"\n";
					bWriter.write(lineString);
				}
				bWriter.close();
				fWriter.close();
			} catch (Exception e) {
				// TODO: handle exception
				e.printStackTrace();
			}
		}
		public void writeMethodNumberFile(String writeString, Map<SensitiveMethod, Integer> allMap, int totalNum){
			try {
				File file=new File(writeString);
				FileWriter fWriter=new FileWriter(file);
				BufferedWriter bWriter=new BufferedWriter(fWriter);
				for(int i=0;i<methodNumberList.size();i++){
					Entry<SensitiveMethod, Integer> entry=methodNumberList.get(i);
					SensitiveMethod method=entry.getKey();
					int number=entry.getValue();
					int numInAll=allMap.get(method);
					String lineString = method.getString()+"#"+number+"#"+number+"/"+this.sampleNumber+"#"+number+"/"+totalNum+"\n";
					bWriter.write(lineString);
				}
				bWriter.close();
				fWriter.close();
				
			} catch (Exception e) {
				// TODO: handle exception
				e.printStackTrace();
			}
		}
		public String getFamilyDirPathString() {
			return familyDirPathString;
		}
		public void setFamilyDirPathString(String familyDirPathString) {
			this.familyDirPathString = familyDirPathString;
		}
		public int getSampleNumber() {
			return sampleNumber;
		}
		public void setSampleNumber(int sampleNumber) {
			this.sampleNumber = sampleNumber;
		}
		public ArrayList<APKInfo> getApkInfoList() {
			return apkInfoList;
		}
		public void setApkInfoList(ArrayList<APKInfo> apkInfoList) {
			this.apkInfoList = apkInfoList;
		}
		public Map<SensitiveMethod, Integer> getMethodNumberMap() {
			return methodNumberMap;
		}
		public void setMethodNumberMap(Map<SensitiveMethod, Integer> methodNumberMap) {
			this.methodNumberMap = methodNumberMap;
		}
		public Map<SensitiveMethod, Double> getMethodWeightMap() {
			return methodWeightMap;
		}
		public void setMethodWeightMap(Map<SensitiveMethod, Double> methodWeightMap) {
			this.methodWeightMap = methodWeightMap;
		}
		public List<Entry<SensitiveMethod, Integer>> getMethodNumberList() {
			return methodNumberList;
		}
		public void setMethodNumberList(
				List<Entry<SensitiveMethod, Integer>> methodNumberList) {
			this.methodNumberList = methodNumberList;
		}
		public List<Entry<SensitiveMethod, Double>> getMethodWeightList() {
			return methodWeightList;
		}
		public void setMethodWeightList(
				List<Entry<SensitiveMethod, Double>> methodWeightList) {
			this.methodWeightList = methodWeightList;
		}
		
}
