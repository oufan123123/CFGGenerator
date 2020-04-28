package APKData.SmaliGraph;

import java.io.Serializable;

public class MethodEdge implements Serializable{
		private MethodNode callerNode;
		private MethodNode calleeNode;
		
		
		public MethodEdge(MethodNode srcNode, MethodNode dstNode){
			this.callerNode=srcNode;
			this.calleeNode=dstNode;
		}
		public MethodEdge(){}
		public MethodNode getCallerNode() {
			return callerNode;
		}

		public void setCallerNode(MethodNode callerNode) {
			this.callerNode = callerNode;
		}

		public MethodNode getCalleeNode() {
			return calleeNode;
		}

		public void setCalleeNode(MethodNode calleeNode) {
			this.calleeNode = calleeNode;
		}
		public String getMethodCallingString(){
			String string=callerNode.getNodeLabelString()+" -> "+calleeNode.getNodeLabelString();
			return string;
		}

		@Override
		public int hashCode() {
			// TODO Auto-generated method stub
			String string=this.getMethodCallingString();
			return string.hashCode();
		}

		@Override
		public boolean equals(Object obj) {
			// TODO Auto-generated method stub
			MethodEdge edge=(MethodEdge) obj;
			if(this.getMethodCallingString().equals(edge.getMethodCallingString())){
				return true;
			}
			else {
				return false;
			}
		}
		
}
