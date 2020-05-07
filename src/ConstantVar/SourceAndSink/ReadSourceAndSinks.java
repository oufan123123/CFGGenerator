package ConstantVar.SourceAndSink;


import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;

import ConstantVar.ConstantValue;

public class ReadSourceAndSinks {

	public static String sourceFilePathString=ConstantValue.getVar().sourceFilePath;
	public static String sinkFilePathString=ConstantValue.getVar().sinkFilePath;
	private static ArrayList<SSMethod> sourceList;
	private static ArrayList<SSMethod> sinkList;
	private static Map<String, String> sourceStringMap=new HashMap<>();
	private static Map<String, String> sinkStringMap=new HashMap<>();
	private static int categorySourceNum;
	private static int categorySinkNum;
	
	private static Map<String, Integer> categorySourceMap;
	private static Map<String, Integer> categorySinkMap;
	private static Map<String, Integer> sameMethodNameSourceMap;
	private static Map<String, Integer> sameMethodNameSinkMap;
	private static ArrayList<Entry<String, Integer>> sameMethodNameSourceList;  // sorted
	private static ArrayList<Entry<String, Integer>> sameMethodNameSinkList;	//sorted
	
	public ReadSourceAndSinks(String sourcePath,String sinkPath){
		categorySourceMap=new HashMap<>();
		categorySinkMap=new HashMap<>();
		sameMethodNameSourceMap=new HashMap<>();
		sameMethodNameSinkMap=new HashMap<>();
		
		iniSourceAndSink(sourcePath,sinkPath);
		
	}
	public static void iniSourceAndSink(String sourcesFileString, String sinksFileString){
		readSourceAndSinks(sourcesFileString, sinksFileString);
		statistics();
		transData();
	//	showStatisticInformation();
	}
	public static void readSourceAndSinks(String sourcesFileString, String sinksFileString){
		sourceList=new ArrayList<>();
		sinkList=new ArrayList<>();
		try {
			/*
			 *   initial the sourceList
			 */
			File sourceFile=new File(sourcesFileString);
			FileReader sourceReader=new FileReader(sourceFile);
			BufferedReader sourceBufferedReader=new BufferedReader(sourceReader);
			String sourceLineString;
			while((sourceLineString=sourceBufferedReader.readLine())!=null){
				if(sourceLineString.startsWith("<")){
					SSMethod sourceMethod=new SSMethod(sourceLineString);
					if(sourceMethod.isValid()){
						sourceList.add(sourceMethod);
					}
				}
			}
			sourceBufferedReader.close();
			sourceReader.close();
			/*
			 *   initial the sinkList
			 */
			File sinkFile=new File(sinksFileString);
			FileReader sinkReader=new FileReader(sinkFile);
			BufferedReader sinkBufferedReader=new BufferedReader(sinkReader);
			String sinkLineString;
			while((sinkLineString=sinkBufferedReader.readLine())!=null){
				if(sinkLineString.startsWith("<")){
					SSMethod sinkMethod=new SSMethod(sinkLineString);
					if(sinkMethod.isValid()){
						sinkList.add(sinkMethod);
					}
				}
			}
			sinkBufferedReader.close();
			sinkReader.close();
			
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}
	}
	public static void statistics(){
		/*
		 *  statistic the information of the source list
		 */
		for(int i=0;i<sourceList.size();i++){
			SSMethod ssMethod=sourceList.get(i);
			String categoryString=ssMethod.getCategoryString();
			if(categorySourceMap.containsKey(categoryString)){
				int k=categorySourceMap.get(categoryString);
				k=k+1;
				categorySourceMap.remove(categoryString);
				categorySourceMap.put(categoryString, k);
			
			}
			else {
				categorySourceMap.put(categoryString,1);
			}
			String methodNameString=ssMethod.getMethodNameString();
			if(sameMethodNameSourceMap.containsKey(methodNameString)){
				int k=sameMethodNameSourceMap.get(methodNameString);
				k=k+1;
				sameMethodNameSourceMap.remove(methodNameString);
				sameMethodNameSourceMap.put(methodNameString, k);
			}
			else {
				sameMethodNameSourceMap.put(methodNameString, 1);
			}
		}
		/*
		 *  statistic the information of the sink list
		 */
		for(int i=0;i<sinkList.size();i++){
			SSMethod ssMethod=sinkList.get(i);
			String categoryString=ssMethod.getCategoryString();
			if(categorySinkMap.containsKey(categoryString)){
				int k=categorySinkMap.get(categoryString);
				k=k+1;
				categorySinkMap.remove(categoryString);
				categorySinkMap.put(categoryString, k);
			}
			else {
				categorySinkMap.put(categoryString,1);
			}
			String methodNameString=ssMethod.getMethodNameString();
			if(sameMethodNameSinkMap.containsKey(methodNameString)){
				int k=sameMethodNameSinkMap.get(methodNameString);
				k=k+1;
				sameMethodNameSinkMap.remove(methodNameString);
				sameMethodNameSinkMap.put(methodNameString, k);
			}
			else {
				sameMethodNameSinkMap.put(methodNameString, 1);
			}
		}
		
		categorySourceNum=categorySourceMap.keySet().size();
		categorySinkNum=categorySinkMap.keySet().size();
		
		sameMethodNameSourceList=new ArrayList<>(sameMethodNameSourceMap.entrySet());
		Collections.sort(sameMethodNameSourceList, new Comparator<Entry<String, Integer>>() {

			@Override
			public int compare(Entry<String, Integer> o1,
					Entry<String, Integer> o2) {
				// TODO Auto-generated method stub
				return o1.getValue()-o2.getValue();
			}
			
		});
		sameMethodNameSinkList=new ArrayList<>(sameMethodNameSinkMap.entrySet());
		Collections.sort(sameMethodNameSinkList, new Comparator<Entry<String, Integer>>() {

			@Override
			public int compare(Entry<String, Integer> o1,
					Entry<String, Integer> o2) {
				// TODO Auto-generated method stub
				return o1.getValue()-o2.getValue();
			}
			
		});
	}
	public static void transData(){
		for(int i=0;i<sourceList.size();i++){
			String tmpString=sourceList.get(i).getEntityMethodString();
			sourceStringMap.put(tmpString, sourceList.get(i).getCategoryString());
		}
		for(int i=0;i<sinkList.size();i++){
			String tmpString=sinkList.get(i).getEntityMethodString();
			sinkStringMap.put(tmpString, sinkList.get(i).getCategoryString());
		}
	}
	public static void showStatisticInformation(){
		String resultString="";
		resultString +="Category Source Num: "+categorySourceNum+"\n";
		int methodSourceNum=0;
		Iterator<String> sourceIterator=categorySourceMap.keySet().iterator();
		while(sourceIterator.hasNext()){
			String categoryString=sourceIterator.next();
			int k=categorySourceMap.get(categoryString);
			resultString +="SourceCategory#Num: "+categoryString+" # "+k+"\n";
			methodSourceNum +=k;
		}
		resultString +="Total Sources Methods Num: "+methodSourceNum+"\n";
		resultString +="*********************************\n";
		int methodSinkNum=0;
		resultString +="Category Sink Num: "+categorySinkNum+"\n";
		Iterator<String> sinkIterator=categorySinkMap.keySet().iterator();
		while(sinkIterator.hasNext()){
			String categoryString=sinkIterator.next();
			int k=categorySinkMap.get(categoryString);
			resultString +="SinkCategory#Num: "+categoryString+" # "+k+"\n";
			methodSinkNum +=k;
		}
		resultString +="Total Sinks Methods Num: "+methodSinkNum +"\n";
		System.out.println(resultString);
	}
	public static String getSourceFilePathString() {
		return sourceFilePathString;
	}
	public static void setSourceFilePathString(String sourceFilePathString) {
		ReadSourceAndSinks.sourceFilePathString = sourceFilePathString;
	}
	public static String getSinkFilePathString() {
		return sinkFilePathString;
	}
	public static void setSinkFilePathString(String sinkFilePathString) {
		ReadSourceAndSinks.sinkFilePathString = sinkFilePathString;
	}
	public static ArrayList<SSMethod> getSourceList() {
		return sourceList;
	}
	public static void setSourceList(ArrayList<SSMethod> sourceList) {
		ReadSourceAndSinks.sourceList = sourceList;
	}
	public static ArrayList<SSMethod> getSinkList() {
		return sinkList;
	}
	public static void setSinkList(ArrayList<SSMethod> sinkList) {
		ReadSourceAndSinks.sinkList = sinkList;
	}
	
	public static Map<String, String> getSourceStringMap() {
		return sourceStringMap;
	}
	public static void setSourceStringMap(Map<String, String> sourceStringMap) {
		ReadSourceAndSinks.sourceStringMap = sourceStringMap;
	}
	public static Map<String, String> getSinkStringMap() {
		return sinkStringMap;
	}
	public static void setSinkStringMap(Map<String, String> sinkStringMap) {
		ReadSourceAndSinks.sinkStringMap = sinkStringMap;
	}
	public static int getCategorySourceNum() {
		return categorySourceNum;
	}
	public static void setCategorySourceNum(int categorySourceNum) {
		ReadSourceAndSinks.categorySourceNum = categorySourceNum;
	}
	public static int getCategorySinkNum() {
		return categorySinkNum;
	}
	public static void setCategorySinkNum(int categorySinkNum) {
		ReadSourceAndSinks.categorySinkNum = categorySinkNum;
	}
	public static Map<String, Integer> getCategorySourceMap() {
		return categorySourceMap;
	}
	public static void setCategorySourceMap(Map<String, Integer> categorySourceMap) {
		ReadSourceAndSinks.categorySourceMap = categorySourceMap;
	}
	public static Map<String, Integer> getCategorySinkMap() {
		return categorySinkMap;
	}
	public static void setCategorySinkMap(Map<String, Integer> categorySinkMap) {
		ReadSourceAndSinks.categorySinkMap = categorySinkMap;
	}
	public static Map<String, Integer> getSameMethodNameSourceMap() {
		return sameMethodNameSourceMap;
	}
	public static void setSameMethodNameSourceMap(
			Map<String, Integer> sameMethodNameSourceMap) {
		ReadSourceAndSinks.sameMethodNameSourceMap = sameMethodNameSourceMap;
	}
	public static Map<String, Integer> getSameMethodNameSinkMap() {
		return sameMethodNameSinkMap;
	}
	public static void setSameMethodNameSinkMap(
			Map<String, Integer> sameMethodNameSinkMap) {
		ReadSourceAndSinks.sameMethodNameSinkMap = sameMethodNameSinkMap;
	}
	public static ArrayList<Entry<String, Integer>> getSameMethodNameSourceList() {
		return sameMethodNameSourceList;
	}
	public static void setSameMethodNameSourceList(
			ArrayList<Entry<String, Integer>> sameMethodNameSourceList) {
		ReadSourceAndSinks.sameMethodNameSourceList = sameMethodNameSourceList;
	}
	public static ArrayList<Entry<String, Integer>> getSameMethodNameSinkList() {
		return sameMethodNameSinkList;
	}
	public static void setSameMethodNameSinkList(
			ArrayList<Entry<String, Integer>> sameMethodNameSinkList) {
		ReadSourceAndSinks.sameMethodNameSinkList = sameMethodNameSinkList;
	}
	
}
