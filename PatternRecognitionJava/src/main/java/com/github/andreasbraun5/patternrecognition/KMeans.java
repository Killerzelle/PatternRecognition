/*
 The MIT License (MIT)

 Copyright (c) 2016 Martin Braun

 Permission is hereby granted, free of charge, to any person obtaining a copy
 of this software and associated documentation files (the "Software"), to deal
 in the Software without restriction, including without limitation the rights
 to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 copies of the Software, and to permit persons to whom the Software is
 furnished to do so, subject to the following conditions:

 The above copyright notice and this permission notice shall be included in all
 copies or substantial portions of the Software.

 THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 SOFTWARE.
 */
package com.github.andreasbraun5.patternrecognition;

import java.util.Arrays;
import java.util.Random;

/**
 * @author Martin Braun
 */
public class KMeans {

	private static final double VALUES[] = {-2.25, 1.07, 0.54, 2.40, 2.57, 1.40};

	private static final Random RANDOM = new Random();

	public static class Wrapper {
		public double mean1;
		public double mean2;

		public final int[] classes;

		public Wrapper(double mean1, double mean2, int valueCount) {
			this.mean1 = mean1;
			this.mean2 = mean2;
			this.classes = new int[valueCount];
		}

		@Override
		public String toString() {
			return "Wrapper{" +
					"mean1=" + mean1 +
					", mean2=" + mean2 +
					", classes=" + Arrays.toString( classes ) +
					'}';
		}
	}

	public static Wrapper kMeansStep(Wrapper in, double[] values) {
		Wrapper out = new Wrapper( in.mean1, in.mean2, values.length );
		System.arraycopy( in.classes, 0, out.classes, 0, values.length );

		for ( int i = 0; i < values.length; ++i ) {
			double curVal = values[i];
			// for one dimensional case, the euclidian distance is just the abs
			double diff1 = Math.abs( curVal - out.mean1 );
			double diff2 = Math.abs( curVal - out.mean2 );

			System.out.println( "diff1 for index " + i + " with value " + curVal + " is " + diff1 );
			System.out.println( "diff2 for index " + i + " with value " + curVal + " is " + diff2 );

			if ( diff1 < diff2 ) {
				out.classes[i] = 1;
				System.out.println( "choosing class 1" );
			}
			else if ( diff2 < diff1 ) {
				out.classes[i] = 2;
				System.out.println( "choosing class 2" );
			}
			else {
				if ( out.classes[i] == 0 ) {
					out.classes[i] = 1 + RANDOM.nextInt( 2 );
					System.out.print(
							"we haven't chosen anything yet, but both diffs were equal, we randomed to class: " + out.classes[i] );
				}
				//don't change if we have already set something
			}
		}

		//recalculate the means
		out.mean1 = calcMean( 1, values, out.classes );
		out.mean2 = calcMean( 2, values, out.classes );

		return out;
	}

	public static double calcMean(int clazz, double[] values, int[] classifications) {
		int countInClass = 0;
		double accumulator = 0;
		for ( int i = 0; i < values.length; ++i ) {
			if ( classifications[i] == clazz ) {
				accumulator += values[i];
				++countInClass;
			}
		}
		return accumulator / countInClass;
	}

	public static void main(String[] args) {
		Wrapper wrapper = new Wrapper( -0.35, 0.5, VALUES.length );
		System.out.println( kMeansStep( wrapper, VALUES ) );
	}

}
