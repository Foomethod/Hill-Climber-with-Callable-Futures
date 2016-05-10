import java.util.ArrayList;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Future;
import java.util.concurrent.ThreadLocalRandom;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;
public class MainThreadPoolwithFutures {

	public static void main(String args[])
	{
		double genNum, minimum = Double.MAX_VALUE;
		double[] storage = new double[200];
		double[][] array = new double[200][100];
		HillClimbing[] task = new HillClimbing[200];
		ExecutorService exec = Executors.newFixedThreadPool(5);
		ArrayList<Future<Double>> futurelist = new ArrayList<Future<Double>>();
		
		for(int r = 0; r < 200; r++)//doesn't require synchronization as all threads are working on a different row
		{		
			for(int c = 0; c < 100; c++)
			{
				genNum = ThreadLocalRandom.current().nextDouble(-5.12,5.12);
				array[r][c] = genNum;
			}			
		}
		
		long starttime = System.currentTimeMillis();
		for(int r = 0; r < 200; r++)
		{
			task[r] = new HillClimbing(array,r);
			Future<Double> future = exec.submit(task[r]);
			futurelist.add(future);
			
			try {
				storage[r] = future.get(5000, TimeUnit.MILLISECONDS);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (ExecutionException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (TimeoutException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
		}

		exec.shutdown();
		while(!exec.isTerminated()) {}
		
		for(int a = 0; a < 200; a++)
		{		
			System.out.printf("Minimum for row %d is : %.2f\n",a + 1,storage[a]);
			if(storage[a] < minimum)
			{
				minimum = storage[a];
			}
		}
		long endtime = System.currentTimeMillis();
		
		long totaltime = (endtime - starttime);
		System.out.printf("Cached thread pool...\nMinimum value for all 200 rows: %.2f"
				+ "\nTotal time taken: %dms", minimum,totaltime);
	}
}