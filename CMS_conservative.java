// COMP90056 Assignment A 2019s2

// write your name/login here

public class CMS_conservative implements CMS
{

	private Hash h[];
	private int c[][];
	int width;
	int depth;

	CMS_conservative(int d, int w)
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
		int update = this.query(o) + freq;
		for (int j = 0; j < depth; j++)
		{
			int mapped_row = h[j].h_basic(o, width);
			if (update < c[j][mapped_row]) update = c[j][mapped_row];
			c[j][h[j].h_basic(o, width)] = update;
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
