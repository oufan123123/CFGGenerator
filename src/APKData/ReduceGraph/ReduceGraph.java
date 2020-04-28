package APKData.ReduceGraph;

import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

import APKData.SmaliGraph.MethodEdge;
import APKData.SmaliGraph.MethodGraph;
import APKData.SmaliGraph.MethodNode;

public class ReduceGraph {
        private MethodGraph sourceGraph;
        private MethodGraph reduceGraph;
        
        public ReduceGraph(MethodGraph graph){
        	sourceGraph=new MethodGraph();
        	reduceGraph=new MethodGraph();
        	
        	sourceGraph=graph;
        	iniReduceGraphNode();
        }
        public void iniReduceGraphNode(){
        	/*
        	 *     基于敏感源节点和敏感目的节点，依次加入其父节点至新的网络中
        	 */
        	Set<MethodNode> reduceNodeSet=new HashSet<>();
        	Set<MethodEdge> reduceEdgeSet=new HashSet<>();
        	/*
        	 *    首先将敏感节点加入该网络中
        	 */
        	Set<MethodNode> nodeSet=new HashSet<>();
        	nodeSet=sourceGraph.getNodeSet();
        	Iterator<MethodNode> nodeIterator=nodeSet.iterator();
        	while(nodeIterator.hasNext()){
        		MethodNode node=nodeIterator.next();
        		if(node.getNodeTypeString().equals("Source")||node.getNodeTypeString().equals("Sink")){
        			reduceNodeSet.add(node);
        		}
        	}
        	/*
        	 *   再依次添加其父节点
        	 */
            while(true){
            	int k=reduceNodeSet.size();
            	Iterator<MethodNode> reIterator=reduceNodeSet.iterator();
            	Set<MethodNode> addNodes=new HashSet<>();
            	while(reIterator.hasNext()){
            		MethodNode node=new MethodNode();
            		node=reIterator.next();
            		Set<MethodNode> parentsSet=new HashSet<>();
            		parentsSet=node.getParentNodes();
            		Iterator<MethodNode> parentIterator=parentsSet.iterator();
            		while(parentIterator.hasNext()){
            			MethodNode parentNode=new MethodNode();
            			parentNode=parentIterator.next();
            			addNodes.add(parentNode);
            		}
            	}
            	Iterator<MethodNode> addIterator=addNodes.iterator();
            	while(addIterator.hasNext()){
            		MethodNode add=addIterator.next();
            		reduceNodeSet.add(add);
            	}
            	Iterator<MethodEdge> edgeIterator=sourceGraph.getEdgeSet().iterator();
            	while(edgeIterator.hasNext()){
            		MethodEdge edge=edgeIterator.next();
            		MethodNode srcNode=edge.getCallerNode();
            		MethodNode dstNode=edge.getCalleeNode();
            		if(reduceNodeSet.contains(srcNode)&&reduceNodeSet.contains(dstNode)){
            			reduceEdgeSet.add(edge);
            		}
            	}
            	int m=reduceNodeSet.size();
            	if(k==m){
            		break;
            	}
            }
            reduceGraph.setNodeSet(reduceNodeSet);
            reduceGraph.setEdgeSet(reduceEdgeSet);
        }
		public MethodGraph getReduceGraph() {
			return reduceGraph;
		}
		public void setReduceGraph(MethodGraph reduceGraph) {
			this.reduceGraph = reduceGraph;
		}
        
}
