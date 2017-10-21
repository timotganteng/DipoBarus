#include <stdio.h>
#include <math.h>
#include <tgmath.h>

int main ()
{
	int n; unsigned long long res;
	while (scanf ("%d", &n) != EOF)
	{
		unsigned long long end;
		res = 0;
		//fungsi adalah 2n^2 -1, diambil dari deret bertingkat
		end = (2 * (unsigned long long)powl((n+1)/2,2))- 1;
		//printf ("%ld\n", end);
		res = end + end - 2 + end - 4;
		printf ("%llu\n", res);
	}
}
