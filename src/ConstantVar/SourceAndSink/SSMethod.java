package ConstantVar.SourceAndSink;

import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class SSMethod {
     private String categoryString;
     private String packageNameString;
     private String returnString;
     private String methodNameString;
     private ArrayList<String> argumentStringList;
     private int argumentNum;
     private ArrayList<String> permissionStringList;
     private int permissionNum;
     
     public SSMethod(String inputString){
    	 if(inputString.startsWith("<")){
    		 // it is an efficient method string
    		 analysisMethod(inputString);
    	 }else {
			// do nothing 
		}
     }
     public void analysisMethod(String str){
    	 //initial the list
    	 argumentStringList=new ArrayList<>();
    	 permissionStringList=new ArrayList<>();
    	 String regString="<(.*?): (.*?) (.*?)\\((.*?)\\)> (.*?)\\((.*?)\\)";
    	 Pattern pattern=Pattern.compile(regString);
    	 Matcher matcher=pattern.matcher(str);
    	 if(matcher.find()){
    		 this.packageNameString=matcher.group(1);
    		 this.returnString=matcher.group(2);
    		 this.methodNameString=matcher.group(3);
    		 String arguString=matcher.group(4);
    		 if(arguString.contains(",")){
    			 String []arg=arguString.split(",");
    			 for(int i=0;i<arg.length;i++){
    				 this.argumentStringList.add(arg[i]);
    			 }
    		 }
    		 else if(arguString.length()>0){
    			 this.argumentStringList.add(arguString);
    		 }
    		 this.argumentNum=argumentStringList.size();
    		 String permissionString=matcher.group(5);
    		 if(permissionString.contains(" ")){
    			 String []permission=permissionString.split(" ");
    			 for(int i=0;i<permission.length;i++){
    				 this.permissionStringList.add(permission[i]);
    			 }
    		 }
    		 else if(permissionString.length()>0){
				this.permissionStringList.add(permissionString);
			}
    		 this.permissionNum=permissionStringList.size();
    		 this.categoryString=matcher.group(6);
    	 }
    
     }
    public boolean isValid(){
    	if(this.categoryString.length()>0 && this.methodNameString.length()>0){
    		return true;
    	}
    	else {
			return false;
		}
    }
    public String getEntityMethodString(){
    	String resultString="";
    	this.packageNameString=this.packageNameString.replace(".", "/");
    	resultString +=this.packageNameString+": "+this.methodNameString;
    	return resultString;
    }
	@Override
	public String toString() {
		// TODO Auto-generated method stub
		String resultString="";
		resultString +="Category: "+this.categoryString+"\n";
		resultString +="PackageName: "+this.packageNameString+"\n";
		resultString +="ReturnType: "+this.returnString+"\n";
		resultString +="MethodName: "+this.methodNameString+"\n";
		resultString +="ArgumentList: ";
		for(int i=0;i<argumentStringList.size();i++){
			resultString +=argumentStringList.get(i)+",";
		}
		resultString +="\n";
		resultString +="ArgumentNum: "+this.argumentNum+"\n";
		resultString +="PermissionList: ";
		for(int i=0;i<permissionStringList.size();i++){
			resultString +=permissionStringList.get(i)+",";
		}
		resultString +="\n";
		resultString +="PermissionNum: "+this.permissionNum+"\n";
		resultString +="*********************\n";
		return resultString;
	}
	public String getCategoryString() {
		return categoryString;
	}
	public void setCategoryString(String categoryString) {
		this.categoryString = categoryString;
	}
	public String getPackageNameString() {
		return packageNameString;
	}
	public void setPackageNameString(String packageNameString) {
		this.packageNameString = packageNameString;
	}
	public String getReturnString() {
		return returnString;
	}
	public void setReturnString(String returnString) {
		this.returnString = returnString;
	}
	public String getMethodNameString() {
		return methodNameString;
	}
	public void setMethodNameString(String methodNameString) {
		this.methodNameString = methodNameString;
	}
	public ArrayList<String> getArgumentStringList() {
		return argumentStringList;
	}
	public void setArgumentStringList(ArrayList<String> argumentStringList) {
		this.argumentStringList = argumentStringList;
	}
	public int getArgumentNum() {
		return argumentNum;
	}
	public void setArgumentNum(int argumentNum) {
		this.argumentNum = argumentNum;
	}
	public ArrayList<String> getPermissionStringList() {
		return permissionStringList;
	}
	public void setPermissionStringList(ArrayList<String> permissionStringList) {
		this.permissionStringList = permissionStringList;
	}
	public int getPermissionNum() {
		return permissionNum;
	}
	public void setPermissionNum(int permissionNum) {
		this.permissionNum = permissionNum;
	}
     
}
