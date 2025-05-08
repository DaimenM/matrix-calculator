package com.matrix.matrixcalculator;

public class Fraction {
    private int numerator;
	private int denominator;
	
	public Fraction(int n, int d) {
		numerator = n;
		denominator = d;
		reduce();
	}
	public Fraction(int n) {
		numerator = n;
		denominator  = 1;
	}
	public int getNumerator() {
		return numerator;
	}
	public int getDenominator() {
		return denominator;
	}
	public Fraction getReciprocal() {
		return new Fraction(denominator,numerator);
	}
	public void setNumerator(int n) {
		numerator = n;
	}
	public void setDenominator(int d) {
		
		denominator = d;
	}
	public void reduce() {
		int divisor=1;
		int counter=1;
		while(counter<=Math.abs(numerator)&&counter<=Math.abs(denominator)) {
			if(numerator%counter==0&&denominator%counter==0)
				divisor = counter;
			counter++;
		}
		setNumerator(numerator/divisor);
		setDenominator(denominator/divisor);
	}
	public Fraction makeFraction(double num) {
	      double epsilon = 0.00000000000001;
	        int numerator = 0;
	        int denominator = 1;
	        double fraction = numerator / (double) denominator;
	        
	        while (Math.abs(fraction - num) > epsilon) {
	            if (fraction < num) {
	                numerator++;
	            } else {
	                denominator++;
	                numerator = (int) (num * denominator);
	            }
	            fraction = numerator / (double) denominator;
	        }
	        return new Fraction(numerator,denominator);
		
	}
	public Fraction multiply(double num) {
		Fraction fraction = makeFraction(num);
			Fraction result = new Fraction(numerator*fraction.getNumerator(),denominator*fraction.getDenominator());
			result.reduce();
			return result;
	}
	public Fraction multiply(int num) {
			Fraction result = new Fraction(numerator*num,denominator);
			result.reduce();
			return result;
	}
	public Fraction multiply(Fraction fraction) {
		Fraction result = new Fraction(0);
		result.setNumerator(numerator*fraction.getNumerator());
		result.setDenominator(denominator*fraction.getDenominator());
		result.reduce();
		return result;
	}
	public Fraction divide(Fraction fraction) {
		Fraction result = new Fraction(0);
		fraction = fraction.getReciprocal();
		result = this.multiply(fraction);
		return result;
	}
	public Fraction add(double num) {
		Fraction result = new Fraction(0);
		Fraction fraction = makeFraction(num);
		int temp = getDenominator();
		result.setNumerator(numerator*fraction.getDenominator());
		result.setDenominator(denominator*fraction.getDenominator());
		fraction.setNumerator(fraction.getNumerator()*temp);
		fraction.setDenominator(fraction.getDenominator()*temp);
		result.setNumerator(result.getNumerator()+fraction.getNumerator());
		result.reduce();
		return result;
	}
	public Fraction add(int num) {
		
		Fraction fraction = new Fraction(num);
		Fraction result = new Fraction(0);
		int temp = getDenominator();
		result.setNumerator(numerator*fraction.getDenominator());
		result.setDenominator(denominator*fraction.getDenominator());
		fraction.setNumerator(fraction.getNumerator()*temp);
		fraction.setDenominator(fraction.getDenominator()*temp);
		result.setNumerator(result.getNumerator()+fraction.getNumerator());
		result.reduce();
		return result;
	}
	public Fraction add(Fraction fraction) {
		Fraction result = new Fraction(0);
		int temp = getDenominator();
		result.setNumerator(numerator*fraction.getDenominator());
		result.setDenominator(denominator*fraction.getDenominator());
		fraction.setNumerator(fraction.getNumerator()*temp);
		fraction.setDenominator(fraction.getDenominator()*temp);
		result.setNumerator(result.getNumerator()+fraction.getNumerator());
		result.reduce();
		return result;
	}
	public Fraction subtract(double num) {
		Fraction result=new Fraction(0);
		Fraction fraction = makeFraction(num);
		int temp = getDenominator();
		result.setNumerator(numerator*fraction.getDenominator());
		result.setDenominator(denominator*fraction.getDenominator());
		fraction.setNumerator(fraction.getNumerator()*temp);
		fraction.setDenominator(fraction.getDenominator()*temp);
		result.setNumerator(result.getNumerator()-fraction.getNumerator());
		result.reduce();
		return result;
	}
	public Fraction subtract(int num) {
		Fraction result = new Fraction(0);
		Fraction fraction = new Fraction(num);
		int temp = getDenominator();
		result.setNumerator(numerator*fraction.getDenominator());
		result.setDenominator(denominator*fraction.getDenominator());
		fraction.setNumerator(fraction.getNumerator()*temp);
		fraction.setDenominator(fraction.getDenominator()*temp);
		result.setNumerator(result.getNumerator()-fraction.getNumerator());
		result.reduce();
		return this;
	}
	public Fraction subtract(Fraction fraction) {
		Fraction result = new Fraction(0);
		int temp = getDenominator();
		result.setNumerator(numerator*fraction.getDenominator());
		result.setDenominator(denominator*fraction.getDenominator());
		fraction.setNumerator(fraction.getNumerator()*temp);
		fraction.setDenominator(fraction.getDenominator()*temp);
		result.setNumerator(result.getNumerator()-fraction.getNumerator());
		result.reduce();
		return result;
	}
	public boolean equals(Fraction other) {
		other.reduce();
		return(getNumerator()==other.getNumerator()&&getDenominator()==other.getDenominator());
	}
	public boolean equals(int n) {
		Fraction other = new Fraction(n);
		return(getNumerator()==other.getNumerator()&&getDenominator()==other.getDenominator());
	}
	public boolean equals(double n) {
		Fraction other = makeFraction(n);
		return(getNumerator()==other.getNumerator()&&getDenominator()==other.getDenominator());
	}
	
	
	
	public String toString() {
		if(denominator<0) {
			setNumerator(-numerator);
			setDenominator(-denominator);
		}
		if(numerator==0||denominator==1) {
			return numerator+"";
		}
		else
			return numerator+"/"+denominator;
	}
}
