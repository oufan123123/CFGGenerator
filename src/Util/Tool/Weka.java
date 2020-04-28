package Util.Tool;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

import weka.classifiers.Classifier;
import weka.classifiers.lazy.IBk;
import weka.classifiers.trees.J48;
import weka.classifiers.trees.RandomForest;
import weka.core.Instances;
import weka.core.converters.ArffLoader;
import wlsvm.WLSVM;

public class Weka {

	public static String trainFilePath="/home/fan/lab/Family/small-size/exp4/result/Im--0.5-train.arff";
	public static String testFilePath="/home/fan/lab/Family/small-size/exp4/result/Im--0.5-test.arff";
	public static Instances trainData;
	public static Instances testData;
	public static void main(String[] args) throws Exception {
		// TODO Auto-generated method stub
		trainData=getTestData(trainFilePath);
		testData=getTestData(testFilePath);
		ArrayList<Classifier> classifiers=new ArrayList<>();
		classifiers=trainModel(trainData);
		testModel(classifiers, testData);
	}
	public static Instances getTestData(String testFilePath) throws IOException{
		File inputTestFile=new File(testFilePath);
		ArffLoader TestAtf=new ArffLoader();
		TestAtf.setFile(inputTestFile);
		Instances testData=TestAtf.getDataSet();
		testData.setClassIndex(testData.numAttributes()-1);
		return testData;
		
	}
	public static String testModel(ArrayList<Classifier> classifierList, Instances testDataInput) throws Exception{
//		int right=0;  //  当四种分类器结果一致的时候，分类正确的个数
//		int err=0;   //  当四种分类器结果一致的时候，分类错误的个数
		String result="";
		int SVMRight=0;
		int RFRight=0;
		int J48Right=0;
		int KNNRight=0;
		Instances testData = testDataInput;	
		for(int  i = 0;i<testData.numInstances();i++)//测试分类结果
	       {
			double actualName=testData.instance(i).classValue();
			ArrayList<Double> predictNameList=new ArrayList<>(); 
			for(int j=0;j<classifierList.size();j++){
				double predictName=classifierList.get(j).classifyInstance(testData.instance(i));
				predictNameList.add(predictName);	
			}
			if(actualName==predictNameList.get(0)){
				SVMRight ++;
			}
			if(actualName==predictNameList.get(1)){
				RFRight ++;
			}
			if(actualName==predictNameList.get(2)){
				J48Right ++;
			}
			if(actualName==predictNameList.get(3)){
				KNNRight ++;
			}
			
	       }
		double SVMPrediction=Double.valueOf(SVMRight)/Double.valueOf(testData.numInstances());
		double RFPrediction=Double.valueOf(RFRight)/Double.valueOf(testData.numInstances());
		double J48Prediction=Double.valueOf(J48Right)/Double.valueOf(testData.numInstances());
		double KNNPrediction=Double.valueOf(KNNRight)/Double.valueOf(testData.numInstances());
//		System.out.println("Result:");
		System.out.println();
		result +="\n";
		System.out.println("SVM: "+SVMRight+"/"+testData.numInstances()+"---"+SVMPrediction);
		result +="SVM: "+SVMRight+"/"+testData.numInstances()+"---"+SVMPrediction+"\n";
		System.out.println("RF: "+RFRight+"/"+testData.numInstances()+"---"+RFPrediction);
		result +="RF: "+RFRight+"/"+testData.numInstances()+"---"+RFPrediction+"\n";
		System.out.println("J48: "+J48Right+"/"+testData.numInstances()+"---"+J48Prediction);
		result +="J48: "+J48Right+"/"+testData.numInstances()+"---"+J48Prediction+"\n";
		System.out.println("KNN: "+KNNRight+"/"+testData.numInstances()+"---"+KNNPrediction);
		result +="KNN: "+KNNRight+"/"+testData.numInstances()+"---"+KNNPrediction+"\n";
		
	       
		return result;
	}
	public static ArrayList<Classifier> trainModel(Instances trainData) throws Exception{
		ArrayList<Classifier> classifierList=new ArrayList<>(); 
		/*
		 *   0: SVM
		 */
		Classifier SVM=new WLSVM();
	      String [] options=weka.core.Utils.splitOptions("-S 0 -T 0 -K 0 -D 3 -G 0.0 -R 0.0 -N 0.5 "
	      		+ "-M 40.0 -V 1 -C 1.0 -E 0.001 -P 0.1 -seed 1");
	      SVM.setOptions(options);
	      SVM.buildClassifier(trainData);
	      classifierList.add(SVM);
	     /*
	      *  1: Random Forest 
	      */
	      Classifier RF=new RandomForest();
	      String []RFoptions=weka.core.Utils.splitOptions("-I 100 -K 0 -S 1");
	      RF.setOptions(RFoptions);
	      RF.buildClassifier(trainData);
	      classifierList.add(RF);
	      /*
	       *  2: J48
	       */
	      Classifier J48=new J48();
	      String []J48options=weka.core.Utils.splitOptions("-C 0.25 -M 2");
	      J48.setOptions(J48options);
	      J48.buildClassifier(trainData);
	      classifierList.add(J48);
	      /*
	       *  3: KNN
	       */
	      Classifier KNN=new IBk();
	      String []KNNoptions=weka.core.Utils.splitOptions("-K 1 -W 0");
	      KNN.setOptions(KNNoptions);
	      KNN.buildClassifier(trainData);
	      classifierList.add(KNN);
		 return classifierList;
	}
}
