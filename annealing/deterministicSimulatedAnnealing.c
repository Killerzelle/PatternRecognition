// compile with gcc stochastic_annealing.c -lm
#include <stdio.h>
#include <time.h>
#include <stdlib.h>
#include <math.h>


//matrix style:
//[0][0], [0][1]
//[1][0], [1][1]

#define NODES 6
#define XDIM NODES
#define YDIM NODES

#define KMAX 30

#define POLL_COUNT 10

typedef int bool;
#define true 1
#define false 0

#define DEBUG true

double weights[] = { 	
	0, 5, -3, 4, 4, 1,
	5, 0, -1, 2, -3, 1,
	-3, -1, 0, 2, 2, 0,
	4, 2, 2, 0, 3, -3,
	4, -3, 2, 3, 0, 5,
	1, 1, 0, -3 , 5, 0
};

double s[] = {
	1,
	-1,
	-1,
	1,
	-1,
	1
};

double sOut[NODES];


void printAll(double* weights, double* s) {
	int i, j;
	printf("W:\n");
	for( i = 0; i < XDIM; ++i) {
		for( j = 0; j < YDIM; ++j) {
			printf("%f ", weights[i*XDIM + j]);
		}
		printf("\n");
	}
	printf("s:\n");
	for( j = 0; j < YDIM; ++j) {
		printf("%f\n", s[j]);
	}
}

double rand01() {
    return (double)rand() / (double) ((unsigned) RAND_MAX + 1);
}

// everybody is a neighbour to everyone
// so we don't need N(i)
int deterministicAnnealingK(double* weights, double* s, double t, double k, double* sOut) {
	int i;
	int j;
	int var;
	double li;

	double tempS[NODES];

	
	for( var = 0; var < NODES; ++var ) {
		tempS[var] = s[var];
	}
	
	{
		for(i=0; i<NODES; ++i) { 
			for(j=0; j<NODES; j++) {
				li += weights[i * NODES + j]*tempS[j];
			}
			tempS[i] = tanh(li/t);
			if( DEBUG ) {
				printf("li: %f\n", li);
				printf("sOut: %f\n", tempS[i]);										
			}
		}
	}
	
	for(var = 0; var < NODES; ++var) {
		sOut[var] = tempS[var];
	}
	

	return 1;
}

double calcEnergy(double* weights, double* s) {
	double E;
	int i, j;

	for( i = 0; i < NODES; ++i) {
		for ( j = 0; j < NODES; ++j ) {
			E += weights[i * NODES + j] * s[i] * s[j];
		}
	}
	E *= -1.0/2.0;
	
	return E;
}

int main() {
	int var;
	int k;
	double E;

	double curTemp = 10;
    	FILE * temp = fopen("deterministic.temp", "w");

	srand(1);

	for(var = 0; var < NODES; ++var) {
		sOut[var] = s[var];
	}

	printAll(weights, sOut);

	for(k = 0; k < KMAX; ++k) {
		printf("curTemp:%f\n", curTemp);
	
		deterministicAnnealingK(weights, sOut, curTemp, KMAX, sOut);
		printAll(weights, sOut);

		E = calcEnergy(weights, sOut);
		printf("Energy: %f\n", E);
		printf("=======\n");
		
		fprintf(temp, "%f %f \n", curTemp, E);
		
		curTemp = 0.9 * curTemp;
	}

	fclose( temp );
	return 0;
}
