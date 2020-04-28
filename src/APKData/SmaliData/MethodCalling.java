package APKData.SmaliData;

public class MethodCalling {
		private SmaliMethod callerMethod;
		private SmaliMethod calleeMethod;
		public MethodCalling(){}
		public MethodCalling(SmaliMethod caller, SmaliMethod callee){
		     callerMethod=caller;
		     calleeMethod=callee;
		}
		
		@Override
		public String toString() {
			// TODO Auto-generated method stub
			String resultString=callerMethod.getEntityMethodString()+"\n"+"		-> "+calleeMethod.getEntityMethodString();
			return resultString;
		}

		public SmaliMethod getCallerMethod() {
			return callerMethod;
		}
		public void setCallerMethod(SmaliMethod callerMethod) {
			this.callerMethod = callerMethod;
		}
		public SmaliMethod getCalleeMethod() {
			return calleeMethod;
		}
		public void setCalleeMethod(SmaliMethod calleeMethod) {
			this.calleeMethod = calleeMethod;
		}
		
		
}
