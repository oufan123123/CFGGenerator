package TFIDF;

public class SensitiveMethod {
	 private String methodNameString;
	 private String typeString;
	 private String categoryString;
	 
	 public SensitiveMethod(){}
	 public SensitiveMethod(String methodString, String type){
		 String []str=methodString.split(",");
		 if(str.length>2){
			 System.err.print("There is an error while reading the source or sink file in SICG!");
		 }
		 else {
			this.methodNameString=str[0];
			this.categoryString=str[1];
			this.typeString=type;
		}
		 
	 }
	 public String getString(){
		 String resultString=this.methodNameString +"," +this.typeString +"," +this.categoryString;
		 return resultString;
	 }
	public String getMethodNameString() {
		return methodNameString;
	}

	public void setMethodNameString(String methodNameString) {
		this.methodNameString = methodNameString;
	}

	public String getTypeString() {
		return typeString;
	}

	public void setTypeString(String typeString) {
		this.typeString = typeString;
	}

	public String getCategoryString() {
		return categoryString;
	}

	public void setCategoryString(String categoryString) {
		this.categoryString = categoryString;
	}
	@Override
	public boolean equals(Object obj) {
		// TODO Auto-generated method stub
		SensitiveMethod method= (SensitiveMethod)obj;
		String objString= method.getString();
		if(objString.equals(this.getString())){
			return true;
		}
		return false;
	}
	@Override
	public int hashCode() {
		// TODO Auto-generated method stub
		return this.getString().hashCode();
	}
	
}
