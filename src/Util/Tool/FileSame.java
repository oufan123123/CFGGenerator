package Util.Tool;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.util.ArrayList;

import weka.core.parser.java_cup.non_terminal;

public class FileSame {

	public static ArrayList<String> fileAList=new ArrayList<>();
	public static ArrayList<String> fileBList=new ArrayList<>();
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		String fileAPath="/home/fan/data/Family/small-sample-exp/exp4/result/Im--0.5-train-0.5--new.arff";
		String fileBPath="/home/fan/data/Family/small-sample-exp/exp4/result/Im--0.5-train.arff";
		System.out.println(isSameFile(fileAPath, fileBPath));
	}
	public static boolean isSameFile(String filePathA, String filePathB){
		try {
			File fileA=new File(filePathA);
			FileReader fReaderA=new FileReader(fileA);
			BufferedReader bReaderA=new BufferedReader(fReaderA);
			String line="";
			while((line=bReaderA.readLine())!=null){
				fileAList.add(line);
			}
			bReaderA.close();
			fReaderA.close();
			
			File fileB=new File(filePathB);
			FileReader fReaderB=new FileReader(fileB);
			BufferedReader bReaderB=new BufferedReader(fReaderB);
			while((line=bReaderB.readLine())!=null){
				fileBList.add(line);
			}
			bReaderB.close();
			fReaderB.close();
			
			if(fileAList.size()!=fileBList.size()){
				System.out.println("Size is not the same ");
				return false;
			}
			else{
				for(int i=0;i<fileAList.size();i++){
					if(fileAList.get(i).equals(fileBList.get(i))){
						continue;
					}
					else{
						System.out.println("line: "+i);
						return false;
					}
				}
			}
			
			
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}
		return true;
	}
	

}
