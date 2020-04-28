package APKData.SmaliData;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class SmaliMethod implements Serializable{
	private String methodPackageString;                 //the package name of the method
	private String methodNameString;                    //the name of the method 
	private String methodArgsString;       					 //the args of the method
	private ArrayList<String> methodArgsList=new ArrayList<>();         // the list of the args of the method
	private String returnValue;                                 //the return value of the method
	private ArrayList<String>  smaliCodeStrings=new ArrayList<>();    //the smali code of the method
	//	private ArrayList<AndroidLine> linesList;     //the linesString contains number of AndroidLine in a method
	//	private AndroidLine tmpLine;
		private ArrayList<MethodCalling>  methodCalls=new ArrayList<>();  //a Android method contains number of Android calls
		private int kLine=0;
		
		public SmaliMethod(){
		}
		public SmaliMethod(String packageString){
			this.methodPackageString=packageString;
	   }
		public void addSmaliCode(String smaliCode){
			  smaliCodeStrings.add(smaliCode);
		}
		public void extractCallInformation(){
			  while(kLine<=smaliCodeStrings.size()-1){
				  getMethodInfomation(smaliCodeStrings.get(kLine));
				  kLine++;
			  }
		}
		public String getMethodInfomation(String line){
			 line=line.trim();
			 if(line.startsWith(".method")){
				Pattern pattern=Pattern.compile(".method(.*) (.*?)\\((.*?)\\)(.*)");
				Matcher matcher=pattern.matcher(line);
				if(matcher.find()){
					this.methodNameString=matcher.group(2);
					this.methodArgsString=matcher.group(3);
					this.returnValue=matcher.group(4);
					// 替换关键字
					replaceKeyWords();
				}
			 }
			 else if(line.startsWith("invoke-")){
			extractMethodCall(line);
		}
			 return null;
		}
		public void replaceKeyWords(){
			//替换函数名中包含< 或　＞
			if(this.methodNameString.contains("<")){
				this.methodNameString=this.methodNameString.replace("<", "");
			}
			if(this.methodNameString.contains(">")){
				this.methodNameString=this.methodNameString.replace(">", "");
			}
			//替换包名字符串首字母　Ｌ　
			if(this.methodPackageString.startsWith("L")){
				this.methodPackageString=this.methodPackageString.substring(1);
			}
			// 替换参数类型　
			if(this.methodArgsString.length()>0){
				String newArgString="";
				String args[]=this.methodArgsString.split(";");
				for(int i=0;i<args.length;i++){
					if(args[i].startsWith("L")){
						args[i]=args[i].substring(1);           // 删除第一个字母＂L
					}
					if(args[i].equals("I")){
						args[i]="java.lang.Integer";          //替换　Ｉ　　为　　java.lang.Integer
					}
					if(args[i].equals("Z")){
						args[i]="boolean";                  //替换　Z	为　　boolean
					}
					newArgString +=args[i]+";";
					methodArgsList.add(args[i]);
				}
				this.methodArgsString=newArgString;
			}
			//替换返回值类型
			if(this.returnValue.length()>0){
				if(this.returnValue.equals("V")){
					this.returnValue="void";           //   替换　　V 为void
				}
				if(this.returnValue.startsWith("L")){
					this.returnValue=this.returnValue.substring(1);          //删除第一个字母"L"
				}
				if(this.returnValue.equals("Z")){
					this.returnValue="boolean";              //替换　Z	为　　boolean
				}
				if(this.returnValue.equals("I")){
					this.returnValue="java.lang.Integer";   //替换　I 为　java.lang.Integer
				}
				if(this.returnValue.contains(";")){
					this.returnValue=this.returnValue.replace(";", "");        //删除返回值最后的 ;
					
				}
			}
		}
		public void extractMethodCall(String line){
			if(line.startsWith("invoke")){
				SmaliMethod calleeMethod=new SmaliMethod();
				Pattern pattern=Pattern.compile("invoke-(.*?) \\{.*\\}, (.*?);->(.*?)\\((.*?)\\)(.*)");
				Matcher matcher=pattern.matcher(line);
				if(matcher.find()){
					calleeMethod.methodPackageString=matcher.group(2);
					calleeMethod.methodNameString=matcher.group(3);
					
					calleeMethod.methodArgsString=matcher.group(4);
					calleeMethod.returnValue=matcher.group(5);
					
					calleeMethod.replaceKeyWords();
					MethodCalling tmpCalling=new MethodCalling(this, calleeMethod);
					methodCalls.add(tmpCalling);
				}
			
			}
		}
		public String getEntityMethodString(){
			String resultString=this.methodPackageString+": "+this.methodNameString+"("+this.methodArgsString+")"+returnValue;
			return resultString;
		}
		public void showCallingInformation(){
			for(int i=0;i<methodCalls.size();i++){
				System.out.println(methodCalls.get(i).toString());
			}
		}
		public ArrayList<MethodCalling> getMethodCalls() {
			return methodCalls;
		}
		public void setMethodCalls(ArrayList<MethodCalling> methodCalls) {
			this.methodCalls = methodCalls;
		}
		
}
