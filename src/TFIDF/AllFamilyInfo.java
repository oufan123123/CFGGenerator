package TFIDF;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

public class AllFamilyInfo {
			private String AllFamilyInfoPathString;
			private int allSampleNumber=0;
			private ArrayList<FamilyInfo> familieInfoList=new ArrayList<>();
			private Map<SensitiveMethod, Integer> allMethodNumberMap=new HashMap<>();
			
			public AllFamilyInfo(String allFamilyDir){
				this.AllFamilyInfoPathString=allFamilyDir;
				iniAllFamiliesInfo(allFamilyDir);
				iniAllMethodNumberMap();
				calculateWeightEachFamily();
			}
			public void writeFamiliesInfo(){
				for(int i=0;i<familieInfoList.size();i++){
					familieInfoList.get(i).writeFamilyInfo(allMethodNumberMap,allSampleNumber);
				}
			}
			public void iniAllFamiliesInfo(String dirPath){
				File file=new File(dirPath);
				File family[]=file.listFiles();
				for(int i=0;i<family.length;i++){
					FamilyInfo familyInfo=new FamilyInfo(family[i].getAbsolutePath()+"/");
					//System.out.println(family[i].getAbsolutePath()+"/");
					familieInfoList.add(familyInfo);
					this.allSampleNumber += familyInfo.getSampleNumber();
				}
			}	
			public void iniAllMethodNumberMap(){
				for(int i=0;i<familieInfoList.size();i++){
					FamilyInfo familyInfo=familieInfoList.get(i);
					Map<SensitiveMethod, Integer> methodNumberMap=new HashMap<>();
					methodNumberMap=familyInfo.getMethodNumberMap();
					Iterator<SensitiveMethod> iterator=methodNumberMap.keySet().iterator();
					while(iterator.hasNext()){
						SensitiveMethod method=iterator.next();
						if(this.allMethodNumberMap.containsKey(method)){
							int k=this.allMethodNumberMap.get(method);
							int add=methodNumberMap.get(method);
							k +=add;
							this.allMethodNumberMap.remove(method);
							this.allMethodNumberMap.put(method, k);
						}
						else {
							int add=methodNumberMap.get(method);
							this.allMethodNumberMap.put(method, add);
						}
					}
				}
			}
			public void calculateWeightEachFamily(){
				for(int i=0;i<familieInfoList.size();i++){
					FamilyInfo familyInfo=familieInfoList.get(i);
					familyInfo.calculateWeight(allMethodNumberMap, allSampleNumber);
				}
			}
			
}
