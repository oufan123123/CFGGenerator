package ConstantVar;

import java.io.File;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import APKData.SmaliGraph.Color;
import CandicateFamily.FamilyWeightScore;
import ConstantVar.SourceAndSink.ReadSourceAndSinks;


public class ConstantValue {
	public static final String FAMILIESDIRPATH_STRING="F:/test_processing/family/";
	
	public static final double APKSIM=0.5 ;
//	public static final String FAMILIESDIRPATH_STRING="/home/fan/lab/IR/data/train/";
//	public static final String TRAINDIRPATH_STRING="/home/fan/data/Family/train/";
//	public static final String TESTDIRPATH_STRING="/home/fan/data/Family/test/";
	public static int TopFalNumber=7;
	public static int PackMinDepth=2;
	public static int PackMaxDepth=4;
	
	//public static final int TotalInstanceNumber=634;
	public static double minAvgGraphScore=0.0D;
	public static double minTotalGraphScore=0.0D;
	public static double minScoreSim=0.8D;
	public static double minTotalGraphScoreRatio=0.0D;        //    由于分值从小到大排序，分值划分阈值在第 25%的值上，即选取前 75%的子图
	public static double minSupport=0.5D;            //家族子图的最小支持度为0.5
	public static int minUnknownNum=10;
	private static ConstantValue var;
	
	public static final String CONMMUNITYDETECTIONPYTHONFILEPATH_STRING="/home/fan/lab/Family/file/SICG-community-CDF.py";
	public static final String CONNECEDFILE_STRING="/home/fan/lab/Family/file/ConnectedGraph.py";
	public static final String sourceFilePath="F:/test_processing/sourceSink/Ouput_CatSources_v0_9.txt";
	public static final String sinkFilePath="F:/test_processing/sourceSink/Ouput_CatSinks_v0_9.txt";
	public static ReadSourceAndSinks ss=new ReadSourceAndSinks(sourceFilePath,sinkFilePath);
	public static final String SVMMODEL="/home/fan/data/Family/file/SVM.model";
	
//	public static final String weightScoreFilePath="/home/fan/data/Family/train-final/geinimi/FamilyInfo/MethodWeight.txt";
//	public static FamilyWeightScore WEIGHT_SCORE;
	
	public static Map<String, Color> colorCategoryMap;
	public static Set<String> falNameSet;
	private ConstantValue(){
		colorCategoryMap=new HashMap<>();
		iniColorMap();
		falNameSet=new HashSet<>();
		iniFalName();
//		WEIGHT_SCORE=new FamilyWeightScore(weightScoreFilePath);
	}
	public void iniFalName(){
		/*
		 *   初始化家族集合
		 */
		File falDir=new File(FAMILIESDIRPATH_STRING);
		File fals[]=falDir.listFiles();
		for(int i=0;i<fals.length;i++){
			String name=fals[i].getName();
			falNameSet.add(name);
		}
		
	}
	public void iniValue(){
		this.minTotalGraphScore=0.0D;
		this.minAvgGraphScore=0.0D;
	}
	public void iniColorMap(){
		/*
		 *      敏感源节点颜色分配
		 */
		colorCategoryMap.put("SOURCE$SMS_MMS", new Color("67", "67", "67"));
		colorCategoryMap.put("SOURCE$CALENDAR_INFORMATION", new Color("102", "102", "102"));
		colorCategoryMap.put("SOURCE$DATABASE_INFORMATION", new Color("255", "0", "0"));
		colorCategoryMap.put("SOURCE$NFC", new Color("255", "153", "0"));
		colorCategoryMap.put("SOURCE$BROWSER_INFORMATION", new Color("255", "255", "0"));
		colorCategoryMap.put("SOURCE$EMAIL", new Color("0", "255", "0"));
		colorCategoryMap.put("SOURCE$SYNCHRONIZATION_DATA", new Color("0", "255", "255"));
		colorCategoryMap.put("SOURCE$BLUETOOTH_INFORMATION", new Color("74", "134", "232"));
		colorCategoryMap.put("SOURCE$ACCOUNT_INFORMATION", new Color("0", "0", "255"));
		colorCategoryMap.put("SOURCE$LOCATION_INFORMATION", new Color("153", "0", "255"));
	    colorCategoryMap.put("SOURCE$IMAGE", new Color("39","78","19"));
		colorCategoryMap.put("SOURCE$NO_CATEGORY", new Color("255", "0", "255"));
		colorCategoryMap.put("SOURCE$FILE_INFORMATION", new Color("12", "52", "61"));
		colorCategoryMap.put("SOURCE$CONTACT_INFORMATION", new Color("28", "69", "135"));
		colorCategoryMap.put("SOURCE$UNIQUE_IDENTIFIER", new Color("7", "55", "99"));
		colorCategoryMap.put("SOURCE$NETWORK_INFORMATION", new Color("32", "18", "77"));
		colorCategoryMap.put("SOURCE$SYSTEM_SETTINGS", new Color("76", "17", "48"));
		/*
		 * 　敏感目的节点颜色分配
		 */
		colorCategoryMap.put("SINK$NETWORK", new Color("230", "184", "175"));
		colorCategoryMap.put("SINK$LOG", new Color("244", "204", "204"));
		colorCategoryMap.put("SINK$SMS_MMS", new Color("252", "229", "205"));
		colorCategoryMap.put("SINK$CALENDAR_INFORMATION", new Color("217", "234", "211"));
		colorCategoryMap.put("SINK$VOIP", new Color("208", "224", "227"));
		colorCategoryMap.put("SINK$NFC", new Color("234", "209", "220"));
		colorCategoryMap.put("SINK$BROWSER_INFORMATION", new Color("234", "153", "153"));
		colorCategoryMap.put("SINK$EMAIL", new Color("249", "203", "156"));
	    colorCategoryMap.put("SINK$AUDIO", new Color("255", "229", "153"));
		colorCategoryMap.put("SINK$SYNCHRONIZATION_DATA", new Color("182", "215", "168"));
		colorCategoryMap.put("SINK$LOCATION_INFORMATION", new Color("162", "196", "201"));
		colorCategoryMap.put("SINK$ACCOUNT_SETTINGS", new Color("164", "194", "244"));
		colorCategoryMap.put("SINK$PHONE_CONNECTION", new Color("246", "178", "107"));
		colorCategoryMap.put("SINK$NO_CATEGORY", new Color("255", "217", "102"));
		colorCategoryMap.put("SINK$PHONE_STATE", new Color("147", "196", "125"));
		colorCategoryMap.put("SINK$BLUETOOTH", new Color("118", "165", "175"));
		colorCategoryMap.put("SINK$FILE", new Color("241", "194", "50"));
		colorCategoryMap.put("SINK$CONTACT_INFORMATION", new Color("217", "210", "233"));
		colorCategoryMap.put("SINK$SYSTEM_SETTINGS", new Color("207", "226", "243"));
	}
	public static ConstantValue getVar(){
		if(var==null){
			var=new ConstantValue();
		}
		return var;
	}
}
