import java.util.concurrent.Callable;
import java.util.concurrent.ThreadLocalRandom;
public class HillClimbing implements Callable<Double>{
	int globalrow;
	double modifyNum;
	double modifiedresult = 0, temporary = 0, rowminimum = Double.MAX_VALUE;
	double[] array = new double[100];
	double[] temparray = new double[100];

	public HillClimbing(double[][] array, int globalrow)
	{
		this.array = array[globalrow];
		this.globalrow = globalrow;
	}
	
	public Double call() throws Exception
	{		
		for(int counter = 0; counter < 350; counter++)
		{
			for(int c = 0; c < 100; c++)
			{
				temparray[c] = array[c];
				temporary += (c+1) * Math.pow(array[c],2);
			}
			for(int c = 0; c < 100; c++)//first row + modification
			{
				modifyNum = ThreadLocalRandom.current().nextDouble(-.5,.5);//generating -.5 to .5
				while(array[c] + modifyNum <= -5.12 || array[c] + modifyNum >= 5.12)
				{
					modifyNum = ThreadLocalRandom.current().nextDouble(-.5,.5);
				}
				array[c] += modifyNum;
				modifiedresult += (c+1) * Math.pow(array[c],2);
			}
			if(temporary < modifiedresult)//revert if greater than previous value
			{
				for(int c = 0; c < 100; c++) {array[c] = temparray[c];}//revert
				modifiedresult = temporary;
			}
			
			rowminimum = getMin(rowminimum, modifiedresult);
			
			modifiedresult = 0;
			temporary = 0;
		}
		return rowminimum;
	}
	
	public double getMin(double a, double b)
	{
		double min = a;
		if(b <= a)
			min = b;
		
		return min;
	}
}