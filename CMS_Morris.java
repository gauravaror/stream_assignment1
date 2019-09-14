// COMP90056 Assignment A 2019s2

// write your name/login here

public class CMS_Morris implements CMS
{

	int width;
	int depth;
	private Hash h[];
	private Morris c[][];

	CMS_Morris(int d, int w)
	{
		width = w;
		depth = d;
		h = new Hash[depth];
		c = new Morris[depth][width];
		for (int l = 0; l < depth; ++l) {
			c[l] = new Morris[width];
			for (int m = 0; m < width; ++m) {
				c[l][m] = new Morris();
			}
		}
		for (int j =0; j<depth; ++j)
		{
			h[j] = new Hash();
		}
	}

	public void update(Object o, int freq)
	{
		for (int j = 0; j < depth; j++)
		{
			c[j][h[j].h_basic(o, width)].increment(freq);
		}
	}

	public int query(Object o)
	{
		int min = c[0][h[0].h_basic(o, width)].mycount();
		int current;
		for (int j=1; j < depth ; j++)
		{
			current = c[j][h[j].h_basic(o, width)].mycount();
			if(current < min)
			{
				min = current;
			}
		}
		return min;
	}
}
