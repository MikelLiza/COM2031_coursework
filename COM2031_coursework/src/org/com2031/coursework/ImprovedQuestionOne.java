package org.com2031.coursework;


import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Random;

/**
 * This class implements the Interval Scheduling algorithm as discussed in
 * COM2031. It is a greedy algorithm.
 * 
 * Steve Schneider
 */
import java.util.*;

public class ImprovedQuestionOne {
	private int[][] jobs;	//array of jobs. Each job is [id, startTime, finishTime, value]
	private int[] memo;		//memoization array
	private ArrayList<Integer> includedJobs = new ArrayList<Integer>();		//holds jobs in optimal solution. The id's are id's of the sorted jobs, so must be converted to the original ID's by getJobInfo
	
	public void calcSchedule(int[][] inputJobs){
		jobs= inputJobs;
		memo = new int[jobs.length];	//create memoization array
		
		Arrays.sort(jobs, (a, b) -> Integer.compare(a[2], b[2]));	//Sort jobs by finish time
		
		memo[0]=0;		//base case with no jobs selected
		
		for(int i = 1; i<jobs.length; i++){
			memo[i] = Math.max( jobs[i][3]+memo[latestCompatible(i)],   memo[i-1] );		//add max value if job is included or if it's not included
		}
		
		System.out.println("Memoization array: " + Arrays.toString(memo));
		System.out.println("Maximum profit from the optimal set of jobs = " + memo[memo.length-1]);
		
		findSolutionIterative(memo.length-1);		//Recursively find solution & update includedJobs
		System.out.println("\nJobs Included in optimal solution:");
		for(int i=includedJobs.size()-1; i>=0; i--){		//Loop backwards to display jobs in increasing order of their ID's
			System.out.println(getJobInfo(includedJobs.get(i)));
		}
	}
	
	//Find the index of the job finishing before job i starts (uses jobs[][] array sorted by finish time)
	private int latestCompatible(int i){
		int low = 0, high = i - 1;

		while (low <= high){		//Iterative binary search
			int mid = (low + high) / 2;		//integer division (floor)
			if (jobs[mid][2] <= jobs[i][1]) {
				if (jobs[mid + 1][2] <= jobs[i][1])
					low = mid + 1;
				else
					return mid;
			}
			else
				high = mid - 1;
			System.out.println(i);
		}
		return 0;	//No compatible job was found. Return 0 so that value of placeholder job in jobs[0] can be used
	}
	
	//Iterative version of the recursive code to retrace & find the optimal solution
	public void findSolutionIterative(int j){
		while (j>0){	//Stops when j==0
			int compatibleIndex = latestCompatible(j);	//find latest finishing job that's compatible with job j
			if(jobs[j][3]+ memo[compatibleIndex] > memo[j-1]){	//Case where job j was included (from optimal substructure)
				includedJobs.add(j);	//add job index to solution
				j=compatibleIndex;		//update j to the next job to consider
			}
			else{	//case where job j was NOT included, remove job j from the possible jobs in the solution & look at jobs 1 to (j-1)
				j=j-1;
			}
		}
	}

	//Recursive method to retrace the memoization array & find optimal solution
	private void findSolutionRecursive(int j){
		if(j==0){	//base case
			return;
		}
		else{
			int compatibleIndex = latestCompatible(j);	//find latest finishing job that's compatible with job j
			if(jobs[j][3]+ memo[compatibleIndex] > memo[j-1]){	//Case where job j was included (from optimal substructure)
				includedJobs.add(j);	//add job index to solution
				findSolutionRecursive(compatibleIndex);	//recursively find remaining jobs starting the the latest compatible job
			}
			else{	//case where job j was NOT included, remove job j from the possible jobs in the solution
				findSolutionRecursive(j-1);
			}
		}
	}
	
	//Get a human-readable String representing the job & its 4 parts
	private String getJobInfo(int jobIndex){
		return "Job " + jobs[jobIndex][0] + ":  Time (" + jobs[jobIndex][1] +"-" + jobs[jobIndex][2] +") Value=" + jobs[jobIndex][3];
	}


	public static void main(String args[]) {
		ImprovedQuestionOne scheduler = new ImprovedQuestionOne();
		int[][] inputJobs = {{0,0,0,0},	
				//dummy 0th item to make array indexes line up
			                 {6,6,11,4},
			                 {3,15,18,6},
			                 {7,3,7,6},
			                 {4,11,17,8},
			                 {5,6,8,3},
			                 {8,11,14,5},
							 {1,4,10,7},
							 {2,17,20,8},
							};
		scheduler.calcSchedule(inputJobs);
	}
}

