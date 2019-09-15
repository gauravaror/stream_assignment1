// COMP90056 Assignment A 2019s2
/*
 * Author: Gaurav Arora
 * Assignment 1: Stream Computing.
 * Count min Sketch default implementation.
 *
 */

public class CMS_default implements CMS
{

	private int width;
	private int depth;
	private Hash h[];
	private int c[][];

	CMS_default(int d, int w)
	{
		width = w;
		depth = d;
		h = new Hash[depth];
		c = new int[depth][width];
		for (int j =0; j<depth; ++j)
		{
			h[j] = new Hash();
		}
	}

	public void update(Object o, int freq)
	{
		for (int j = 0; j < depth; j++)
		{
			int hash_value  = h[j].h_basic(o, width);
			//System.out.println("GAsgs " + String.valueOf(hash_value));
			try {
			    c[j][hash_value] += freq;
			} catch(Exception e) {
			    System.out.println("GAsgs " + String.valueOf(hash_value) + "    "+String.valueOf(o) + "    "+String.valueOf(width));
			}
		}
	}

	public int query(Object o)
	{
		int min = c[0][h[0].h_basic(o, width)];
		int current;
		for (int j=1; j < depth ; j++)
		{
			current = c[j][h[j].h_basic(o, width)];
			if(current < min)
			{
				min = current;
			}
		}
		return min;
	}
}
