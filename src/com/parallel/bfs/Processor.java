package com.parallel.bfs;

import java.util.PriorityQueue;
import java.util.Queue;

public class Processor extends Thread  {
	
	private int threadNumber;
	private Graph graph;
	public Processor(Graph g,int id){
		this.threadNumber = id;
		setName("Processor "+id);
		graph = g;
	}
	
	
	@Override
	public long getId() {
		return threadNumber;
	}

	@Override
	public void run() {
		while(!graph.isDone()){
			graph.bfs();
			yield();
			subBfs(graph.getLocalQueues().get(threadNumber));
		}
	}
	public int getThreadNumber() {
		return threadNumber;
	}
	public void setThreadNumber(int threadNumber) {
		this.threadNumber = threadNumber;
	}
	
	public void subBfs(Queue<Integer> localQueue){
		Queue<Integer> tmpQueue = new PriorityQueue<Integer>();
		while(!localQueue.isEmpty()){
			int node = localQueue.poll();
			if(!graph.getVisited(node)){
				graph.setVisited(node,true);
				graph.incrementCounter();
				boolean toLocal = true;
				for(int i = 0; i<graph.getSize(); i++){
					if(node==i)continue;
					if(graph.isNeighbour(node, i) && !toLocal && !graph.getVisited(i)){
						tmpQueue.add(i);
						
					}
					if(graph.isNeighbour(node, i) && toLocal && !graph.getVisited(i)){
						localQueue.add(i);
						toLocal = false;
					}
				}
			}
		}
		graph.addQueue(tmpQueue);
	}
}
