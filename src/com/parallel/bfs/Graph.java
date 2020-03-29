package com.parallel.bfs;

import java.util.ArrayList;
import java.util.List;
import java.util.PriorityQueue;
import java.util.Queue;
import java.util.Random;

public class Graph {
	private int size;//Number of Nodes
	private int[][] vertices;//Adjacency Matrix
	private Queue<Integer> globalQueue;
	private List<Queue<Integer>> localQueues;
	public List<Queue<Integer>> getLocalQueues() {
		return localQueues;
	}
	public void setLocalQueues(List<Queue<Integer>> localQueues) {
		this.localQueues = localQueues;
	}

	private boolean[] visited;
	private boolean isDone;
	private int counter;
	public Graph(int size,boolean[] visited,int numberOfProcessors){
		this.size = size;
		localQueues = new ArrayList<Queue<Integer>>(numberOfProcessors);
		for(int i=0; i < numberOfProcessors; i++){
			localQueues.add(new PriorityQueue<Integer>());
		}
		vertices = new int[size][size];
		this.visited = visited;
		isDone = false;
		globalQueue = new PriorityQueue<Integer>();
		globalQueue.add(size - 1);
		counter = 0;
		for(int i = 0; i < this.size; i++)
			for(int j = 0; j < this.size; j++){
				Random boolNumber = new Random();
                boolean edge = boolNumber.nextBoolean();
                if(i == j)
                	vertices[i][j] = 1;
                else	
                	vertices[i][j] = edge ? 1 : 0;
			}
	}
	public int getSize(){
		return size;
	}
	
	public synchronized boolean getVisited(int index){
		return visited[index];
	}
	
	public synchronized void setVisited(int index, boolean value){
		visited[index] = value;
	}
	
	public synchronized void addQueue(Queue<Integer> tmp){
		while(!tmp.isEmpty()){
			globalQueue.add(tmp.poll());
		}
	}
	
	public boolean isNeighbour(int node, int neighbour){
		return vertices[node][neighbour]==1;
	}
	
	public synchronized void incrementCounter(){
		counter++;
	}
	
	public boolean isDone() {
		return isDone;
	}
	
	public synchronized void bfs(){
		while(!isDone && globalQueue.isEmpty()){
			try {
				wait();
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
		int index = (int)(Thread.currentThread().getId());
		if(!globalQueue.isEmpty()){
			boolean popped = false;
			int node = globalQueue.poll();
			popped = true;
			while(visited[node]){
				if(globalQueue.isEmpty()){
					isDone = true;
					popped = false;
					break;
				}else{
					node = globalQueue.poll();
					popped = true;
				}
			}
			if(popped){
				visited[node] = true;
				counter++;
				boolean flag = false;
				for(int i = 0; i < size; i++){
					if(node == i)continue;
					if(isNeighbour(node, i) && !visited[i] && !flag){
						localQueues.get(index).add(i);
						flag = true;
					}
					if(isNeighbour(node, i) && !visited[i] && flag){
						globalQueue.add(i);
					}
				}
			}
		}
		if(globalQueue.isEmpty())
			isDone = true;
		if(isDone && counter<size){
			isDone = false;
			for(int i = 0; i < size; i++){
				if(!visited[i])
					globalQueue.add(i);
			}
		}
		notifyAll();
	}
	
}
