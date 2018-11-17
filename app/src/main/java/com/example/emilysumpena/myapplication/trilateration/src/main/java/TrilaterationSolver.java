package com.example.emilysumpena.myapplication.trilateration.src.main.java;

import com.example.emilysumpena.myapplication.trilateration.src.main.java.CoordinateConverter;
import org.ejml.data.SingularMatrixException;
import org.ejml.simple.SimpleMatrix;
import java.util.Arrays;

public class TrilaterationSolver {

	/**
	 * The raw anchor poses
	 */
	private final double[][] rawAnchorCoords;
	/**
	 * True if the anchorPoses are represented by the geographical coordinates (i.e. longitude and latitude)
	 */
	private final boolean isGeoCoord;
	/**
	 * The position of anchors (e.g. the sensors) under local coordinate system (in meter)
	 */
	private double[][] anchorPoses;

	public TrilaterationSolver(double[][] anchorCoords, boolean isGeoCoord) {
		this.rawAnchorCoords = anchorCoords;
		this.isGeoCoord = isGeoCoord;
		if (isGeoCoord) {
			// convert to local coordinate
			this.anchorPoses = new double[rawAnchorCoords.length][2];
			double[] baseGeoCoord = rawAnchorCoords[0];		// we use the first coordinate as the base
			for (int i = 0; i < rawAnchorCoords.length; i++) {
				this.anchorPoses[i] = CoordinateConverter.geoCoord2LocalCoord(rawAnchorCoords[i], baseGeoCoord);
			}
		} else {
			this.anchorPoses = anchorCoords;
		}
	}

	public double[] localize(double[] dists) {
		if (anchorPoses.length != dists.length) {
			throw new IllegalStateException("The number of distance does not match to the number of anchors.");
		}

		// The measurement is unavailable if distance < 0
		final int n = countEffectiveValue(dists);
		double[][] C = new double[n][2];    // C is the set of effective anchor positions
		double[] R = new double[n];         // R is the set of effective distances
		for (int i = 0, j = 0; i < dists.length; i++) {
			if (dists[i] > 0) {
				C[j] = anchorPoses[i];
				R[j] = dists[i];
				j++;
			}
		}

		// solve ||c_i - x|| ^ 2 = D_i ^ 2 (ref: http://inside.mines.edu/~whereman/talks/TurgutOzal-11-Trilateration.pdf)
		// use the first anchor as reference point
		SimpleMatrix A = new SimpleMatrix(n - 1, 2);
		for (int i = 0; i < n - 1; i++) {
			A.set(i, 0, C[i + 1][0] - C[0][0]);
			A.set(i, 1, C[i + 1][1] - C[0][1]);
		}

		SimpleMatrix b = new SimpleMatrix(n - 1, 1);
		for (int i = 0; i < n - 1; i++) {
			b.set(i, 0,
			(R[0] * R[0] - R[i + 1] * R[i + 1] + (C[i + 1][0] - C[0][0]) * (C[i + 1][0] - C[0][0]) + (C[i + 1][1] - C[0][1]) * (C[i + 1][1] - C[0][1])) / 2.0);
		}

		double[] resultCoord = null;
		try {
			SimpleMatrix x = A.solve(b);
			resultCoord = new double[] {x.get(0) + C[0][0], x.get(1) + + C[0][1]};
		} catch (SingularMatrixException e ) {
			throw new IllegalArgumentException("Singular matrix");
		}

		if (isGeoCoord) {
			return CoordinateConverter.localCoord2GeoCoord(resultCoord, rawAnchorCoords[0]);
		} else {
			return resultCoord;
		}

	}

	private int countEffectiveValue(double[] array) {
		int n = 0;
		for (double v : array)
			if (v >= 0) n++;
		return n;
	}

	// for testing
	public static void main(String[] args) {
		double[][] positions = new double[][] { { 5.0, -6.0 }, { 13.0, -15.0 }, { 21.0, -3.0 }, { 12.4, -21.2 } };
		double[] distances = new double[] { 8.06, 13.97, 23.32, 15.31 };

		TrilaterationSolver trilaterationSolver = new TrilaterationSolver(positions, false);
		double[] result = trilaterationSolver.localize(distances);
		System.out.println("Result: " + Arrays.toString(result));
	}

}
