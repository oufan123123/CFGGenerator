package CandicateFamily;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class FamilyWeightScore {
			private String weightScoreFilePath;
			private Map<String, Double> MethodScoreMap=new HashMap<>();
			
			public FamilyWeightScore(String weightScoreFilePath){
				this.weightScoreFilePath=weightScoreFilePath;
				iniScoreMap();
			}
			public void iniScoreMap(){
					try {
						File readFile=new File(weightScoreFilePath);
						FileReader fReader=new FileReader(readFile);
						BufferedReader bufferedReader=new BufferedReader(fReader);
						String lineString="";
						while((lineString=bufferedReader.readLine())!=null){
							String str[]=lineString.split("#");
							String methodNameString=str[0];
							String scoreWeight=str[1];
							double score=Double.valueOf(scoreWeight);
							MethodScoreMap.put(methodNameString, score);
						}
						bufferedReader.close();
						fReader.close();
				} catch (Exception e) {
					// TODO: handle exception
					e.printStackTrace();
				}
			}
			public String getWeightScoreFilePath() {
				return weightScoreFilePath;
			}
			public void setWeightScoreFilePath(String weightScoreFilePath) {
				this.weightScoreFilePath = weightScoreFilePath;
			}
			public Map<String, Double> getMethodScoreMap() {
				return MethodScoreMap;
			}
			public void setMethodScoreMap(Map<String, Double> methodScoreMap) {
				MethodScoreMap = methodScoreMap;
			}
			
}
