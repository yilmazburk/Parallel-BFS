package com.parallel.bfs;

import java.util.PriorityQueue;
import java.util.Queue;


public class SerialBFS implements Runnable{
	private boolean[] visited;
	private Graph graph;
	private Queue<Integer> queue;
	private int beginNode;
	public SerialBFS(int size,boolean[] visited,int beginPoint){
		graph = new Graph(size,visited,1);
		this.visited = visited;
		queue = new PriorityQueue<Integer>();
		beginNode = beginPoint;
	}
	@Override
	public void run() {
		for(int i = 0;i<graph.getSize();i++){
			visited[i] = false;
		}
		queue.add(beginNode);//Begin Vertex Added To Queue
		while(!queue.isEmpty()){
			int node = queue.poll();
			if(visited[node]==false){
				visited[node] = true;
				for(int i = 0; i<graph.getSize(); i++){
					if(node==i)continue;
					if(graph.isNeighbour(node, i) && visited[i]==false){
						queue.add(i);
					}
				}
			}
		} 
	}

}
