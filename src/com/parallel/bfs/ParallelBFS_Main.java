package com.parallel.bfs;

import java.util.Calendar;

import com.parallel.bfs.SerialBFS;

public class ParallelBFS_Main {

	public static void main(String[] args) {
		long start, finish;
		final int  numberOfNodes = 5000;
		final int numberOfCore = 4;
		
		boolean[] visited = new boolean[numberOfNodes];
		for(int i = 0; i<numberOfNodes; i++){
			visited[i] = false;
		}
		Graph graph = new Graph(numberOfNodes,visited,numberOfCore);
		/**
		 * Parallel BFS 
		 * */
		start = Calendar.getInstance().getTimeInMillis();
		Thread[] processors = new Processor[numberOfCore];
		for(int i = 0; i<numberOfCore; i++){
			processors[i] = new Processor(graph,i);
			processors[i].start();
		}
		for(int i = 0; i<numberOfCore; i++){
			try {
				processors[i].join();
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
		finish = Calendar.getInstance().getTimeInMillis();
		System.out.println("Parallel Time "+(finish-start)); 
		boolean success = true;
		for(int i = 0;i<numberOfNodes;i++){
			if(!visited[i]){
				success = false;
				System.out.println("Failure");
				break;
			}
		}
		if(success)
			System.out.println("Success ");
		/**
		 * Serial BFS 
		 * */
		for(int i = 0;i<numberOfNodes;i++){
			visited[i] = false;
		}
		start = Calendar.getInstance().getTimeInMillis();
		SerialBFS serialDFS = new SerialBFS(numberOfNodes, visited, numberOfNodes-1);
		Thread serial = new Thread(serialDFS);
		serial.start();
		try {
			serial.join();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		finish = Calendar.getInstance().getTimeInMillis();
		System.out.println("Serial Time "+(finish-start)); 
		success = true;
		for(int i = 0;i<numberOfNodes;i++){
			if(!visited[i]){
				success = false;
				System.out.println("Failure");
				break;
			}
		}
		if(success)
			System.out.println("Success ");
	}

}
