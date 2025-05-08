package com.matrix.matrixcalculator;

public class Matrix {
    private Fraction[][] matrix;
	private boolean aug;
	
	public Matrix(int row, int col, String entries, boolean aug) {
		matrix = new Fraction[row][col];
		this.aug=aug;
		addEntries(entries);
	}
	public Matrix(int row,int col) {
		matrix = new Fraction[row][col];
		this.aug=false;
	}
	public Matrix(int row,int col,String entries) {
		matrix = new Fraction[row][col];
		this.aug=false;
		this.addEntries(entries);
	}
	public int getRows() {
		return matrix.length;
	}
	public int getColumns() {
		return matrix[0].length;
	}
	public Fraction[][] getMatrix() {
		return matrix;
	}
	public Matrix getIdentityMatrix(){
		if(getRows()!=getColumns()) throw new IllegalArgumentException("Must be an nxn matrix");
		Matrix result =  new Matrix(getRows(),getColumns());
		int indicator=0;
		for(int i=0;i<getRows();i++) {
			for(int j=0;j<getColumns();j++) {
				if(j==indicator) result.getMatrix()[i][j] = new Fraction(1);
				else result.getMatrix()[i][j] = new Fraction(0);
			}
			indicator++;
		}
		return result;
	}
	public boolean getAug() {
		return aug;
	}
	public Fraction getDeterminant(Matrix matrix) {
		if(getRows()!=getColumns()) throw new IllegalArgumentException("Must be nxn matrix");
		Fraction sum = new Fraction(0);
		if(matrix.getRows()==1&&matrix.getColumns()==1)
			return matrix.getMatrix()[0][0];
		if(matrix.getRows()==2&&matrix.getColumns()==2) 
			return (matrix.getMatrix()[0][0].multiply(matrix.getMatrix()[1][1])).subtract(matrix.getMatrix()[0][1].multiply(matrix.getMatrix()[1][0]));
		else {
			for(int i=0;i<matrix.getColumns();i++) {
				String temp = "";
				for(int j=0;j<matrix.getRows();j++) 
					for(int k=0;k<matrix.getColumns();k++) {
							if(k!=i&&j!=0)temp+=matrix.getMatrix()[j][k]+",";
						}
				Matrix result = new Matrix(matrix.getRows()-1,matrix.getColumns()-1,temp.substring(0,temp.length()-1));
				if(i%2==0||i==0)sum = sum.add(getDeterminant(result).multiply(matrix.getMatrix()[0][i]));
				else sum = sum.add(getDeterminant(result).multiply(matrix.getMatrix()[0][i].multiply(-1)));
				
			}
			return sum;
		}
	}
	public Matrix getAdjoint() {
		if(getColumns()!=getRows()) throw new IllegalArgumentException("Must be nxn matrix");
		String entries = "";
		String temp = "";
		for(int i=0;i<getRows();i++) {
			for(int j=0;j<getColumns();j++) {
				temp="";
				for(int k=0;k<getRows();k++) {
					for(int x=0;x<getColumns();x++) {
						if(k!=i&&x!=j) temp+=matrix[k][x]+",";
					}
				}
			if((i+j)%2==0)entries+=getDeterminant(new Matrix(getRows()-1,getColumns()-1,temp.substring(0,temp.length()-1)))+",";
			else {
				Fraction determinant = getDeterminant(new Matrix(getRows()-1,getColumns()-1,temp.substring(0,temp.length()-1))).multiply(-1);
				entries+=determinant+",";
			}
		}
		}
		return new Matrix(getRows(),getColumns(),entries.substring(0,entries.length()-1)).transpose();
	}
	public void setMatrix(Fraction[][] newMatrix) {
		matrix = newMatrix;
	}
	public Matrix multiply(Matrix other) {
		Matrix product = new Matrix(this.getRows(),other.getColumns());
		int rowIndex = 0;
		int colIndex = 0;
		if(this.getColumns()!=other.getRows()) 
			throw new IllegalArgumentException("These matrices cannot be multiplied");
		for(int i=0;i<this.getRows();i++)
			for(int j=0;j<other.getColumns();j++) {
				Fraction entry = new Fraction(0);
				for(int k=0;k<this.getColumns();k++) {
					entry = entry.add(this.getMatrix()[i][k].multiply(other.getMatrix()[k][j]));
					if(k==this.getColumns()-1) {
						product.getMatrix()[rowIndex][colIndex] = entry;
						colIndex+=1;
						if(colIndex==other.getColumns()) {
							rowIndex+=1;
							colIndex=0;
						}
					}
					}
				}
		
		return product;
	}
	public Matrix multiply(Fraction c) {
		Matrix result = new Matrix(getRows(),getColumns());
		for(int i=0;i<getRows();i++)
			for(int j=0;j<getColumns();j++) {
				result.getMatrix()[i][j] = getMatrix()[i][j].multiply(c);
			}
		return result;
	}
	public  void addEntries(String entries) {
		int index=0;
		String[] arrOfEntries = entries.split(",");
		if(arrOfEntries.length!=this.getRows()*this.getColumns())
			throw new IllegalArgumentException("Invalid amount of entries");
		for(int i=0;i<this.getRows();i++) 
			for(int j=0;j<this.getColumns();j++) {
				String[] frac = arrOfEntries[index].split("/");
				String[] decimal = arrOfEntries[index].split("\\.");
				if(frac.length==2 && !arrOfEntries[index].contains(".")) this.getMatrix()[i][j] = new Fraction(Integer.parseInt(frac[0]),Integer.parseInt(frac[1]));
				else if(decimal.length==2){
					Fraction newFraction = new Fraction(0);
					this.getMatrix()[i][j] = newFraction.makeFraction(Double.parseDouble(arrOfEntries[index]));

				}
				else this.getMatrix()[i][j] = new Fraction(Integer.parseInt(arrOfEntries[index]));
				index++;
					
			}
	
	}
	public Matrix add(Matrix other) {
		Matrix sum = new Matrix(this.getRows(),this.getColumns());
		if(this.getRows()!=other.getRows()||this.getColumns()!=other.getColumns())
			throw new IllegalArgumentException("Matrices cannot be added");
		for(int i=0;i<this.getRows();i++)
			for(int j=0;j<this.getColumns();j++) {
				sum.getMatrix()[i][j] = this.getMatrix()[i][j].add(other.getMatrix()[i][j]);
	}
		return sum;
	}
	public Matrix add(Matrix other, int c1, int c2) {
		Matrix sum = new Matrix(this.getRows(),this.getColumns());
		if(this.getRows()!=other.getRows()||this.getColumns()!=other.getColumns())
			throw new IllegalArgumentException("Matrices cannot be added");
		for(int i=0;i<this.getRows();i++)
			for(int j=0;j<this.getColumns();j++) {
				sum.getMatrix()[i][j] = this.getMatrix()[i][j].multiply(c1).add(other.getMatrix()[i][j].multiply(c2));
	}
		return sum;
	}
	public Matrix subtract(Matrix other){
		Matrix difference = new Matrix(this.getRows(),this.getColumns());
		if(this.getRows()!=other.getRows()||this.getColumns()!=other.getColumns())
			throw new IllegalArgumentException("Matrices cannot be subtracted");
		for(int i=0;i<this.getRows();i++)
			for(int j=0;j<this.getColumns();j++) {
				difference.getMatrix()[i][j] = this.getMatrix()[i][j].subtract(other.getMatrix()[i][j]);
	}
		return difference;
	}
	public Matrix subtract(Matrix other,int c1,int c2){
		Matrix difference = new Matrix(this.getRows(),this.getColumns());
		if(this.getRows()!=other.getRows()||this.getColumns()!=other.getColumns())
			throw new IllegalArgumentException("Matrices cannot be subtracted");
		for(int i=0;i<this.getRows();i++)
			for(int j=0;j<this.getColumns();j++) {
				difference.getMatrix()[i][j] = this.getMatrix()[i][j].multiply(c1).subtract(other.getMatrix()[i][j].multiply(c2));
	}
		return difference;
	}
	public void manipulateRow(int r1,int r2, int c, String operation) {
		if(operation.equals("+")) 
		for(int i=0;i<this.getColumns();i++) {
			this.getMatrix()[r1][i] = this.getMatrix()[r1][i].add(this.getMatrix()[r2][i].multiply(c));
		}
		else if(operation.equals("-"))
			for(int i=0;i<this.getColumns();i++) {
				this.getMatrix()[r1][i] = this.getMatrix()[r1][i].subtract(this.getMatrix()[r2][i].multiply(c));
			}
		else if(operation.equals("*")) 
			for(int i=0;i<this.getColumns();i++) {
				this.getMatrix()[r1][i] = (this.getMatrix()[r1][i]).multiply(this.getMatrix()[r2][i].multiply(c));
			}
	}
	public void manipulateRow(int r1,int r2, double c, String operation) {
		if(operation.equals("+")) 
		for(int i=0;i<this.getColumns();i++) {
			this.getMatrix()[r1][i] = this.getMatrix()[r1][i].add(this.getMatrix()[r2][i].multiply((double)c));
		}
		else if(operation.equals("-"))
			for(int i=0;i<this.getColumns();i++) {
				this.getMatrix()[r1][i] = this.getMatrix()[r1][i].subtract(this.getMatrix()[r2][i].multiply((double)c));
			}
		else if(operation.equals("*")) 
			for(int i=0;i<this.getColumns();i++) {
				this.getMatrix()[r1][i] = (this.getMatrix()[r1][i]).multiply(this.getMatrix()[r2][i].multiply((double)c));
			}
	}
	public void manipulateRow(int r1,int r2, String c, String operation) {
		Fraction frac = processMultiplier(c);
		if(operation.equals("add")) 
		for(int i=0;i<this.getColumns();i++) {
			this.getMatrix()[r1][i] = this.getMatrix()[r1][i].add(this.getMatrix()[r2][i].multiply(frac));
		}
		else if(operation.equals("subtract"))
			for(int i=0;i<this.getColumns();i++) {
				this.getMatrix()[r1][i] = this.getMatrix()[r1][i].subtract(this.getMatrix()[r2][i].multiply(frac));
			}
		else if(operation.equals("multiply")) 
			for(int i=0;i<this.getColumns();i++) {
				this.getMatrix()[r1][i] = (this.getMatrix()[r1][i]).multiply(this.getMatrix()[r2][i].multiply(frac));
			}
	}
	public void manipulateRow(int r1,int c) {
		for(int i=0;i<this.getMatrix()[r1].length;i++)
			this.getMatrix()[r1][i] = this.getMatrix()[r1][i].multiply(c);
	}
	public void manipulateRow(int r1,String c) {
		Fraction frac = processMultiplier(c);
		for(int i=0;i<this.getMatrix()[r1].length;i++)
			this.getMatrix()[r1][i] = this.getMatrix()[r1][i].multiply(frac);
	}
	public void manipulateRow(int r1, double c) {
		for(int i=0;i<this.getMatrix()[r1].length;i++)
			this.getMatrix()[r1][i] = this.getMatrix()[r1][i].multiply((double)c);
	}
	public void switchRows(int r1,int r2) {
		if(r1>=this.getMatrix().length||r2>=this.getMatrix().length)
			throw new IllegalArgumentException("Rows do not exist for this matrix");
		else {
			Fraction[] temp = new Fraction[this.getMatrix()[r1].length];
			for(int i=0;i<this.getMatrix()[r1].length;i++)
				temp[i] = this.getMatrix()[r1][i];
			for(int i=0;i<this.getMatrix()[r1].length;i++)
				this.getMatrix()[r1][i] = this.getMatrix()[r2][i];
			for(int i=0;i<this.getMatrix()[r2].length;i++)
				this.getMatrix()[r2][i] = temp[i];
		}
		
	}
	public Matrix transpose() {
		Matrix result = new Matrix(this.getColumns(),this.getRows());
		for(int i=0;i<this.getRows();i++)
			for(int j=0;j<this.getColumns();j++) 
				result.getMatrix()[j][i] = this.getMatrix()[i][j];
		return result;
	}
	public Matrix exponent(int power) {
		if(power<=0) throw new IllegalArgumentException("Exponent must be a positive integer");
		Matrix result = new Matrix(getRows(),getColumns());
		for(int i=1;i<power;i++) {
			if(i==1)
				result = this.multiply(this);
			else
				result = result.multiply(this);
		}
		return result;
	}
	public Matrix inverse() {
		if(getDeterminant(this).equals(0)) throw new IllegalArgumentException("Matrix not invertible");
		Matrix result = new Matrix(getRows(),getColumns());
		
		result.setMatrix(getAdjoint().multiply(getDeterminant(this).getReciprocal()).getMatrix());
		return result;
	}
	public Matrix RREF() {
		Matrix result = this;
		int rowCount = getRows();
		int colCount = getColumns();
		int lead=0;
		for(int r=0;r<rowCount;r++) {
			if(lead>=colCount) break;
			int i=r;
			while(result.getMatrix()[i][lead].equals(new Fraction(0))) {
				i++;
				if(i==rowCount) {
					i=r;
					lead++;
					if(lead==colCount) return result;
				}
			}
			Fraction[] temp = result.getMatrix()[i];
			result.getMatrix()[i] = result.getMatrix()[r];
			result.getMatrix()[r] = temp;
			Fraction div = result.getMatrix()[r][lead];
			for(int j=0;j<colCount;j++) result.getMatrix()[r][j] = result.getMatrix()[r][j].divide(div);
			for(int k=0; k<rowCount;k++) {
				if(k!=r) {
					Fraction mult = result.getMatrix()[k][lead];
					for(int j=0;j<colCount;j++) result.getMatrix()[k][j]= result.getMatrix()[k][j].subtract(mult.multiply(result.getMatrix()[r][j]));
				}
			}
			lead++;
			System.out.println(result);
		}
		return result;
	}
	public Fraction processMultiplier(String c){
		String[] isFrac = c.split("/");
		if(isFrac.length==2&&!c.contains(".")) return new Fraction(Integer.parseInt(isFrac[0]),
		Integer.parseInt(isFrac[1]));
		else {
			Fraction frac = new Fraction(0);
			frac = frac.makeFraction(Double.parseDouble(c));
			return frac;
		}
	}
	public String toString() {
		String result = "";
		for(int i=0;i<matrix.length;i++) 
			for(int j=0;j<matrix[0].length;j++) {
					if(i==matrix.length-1&&j==matrix[0].length-1) result+=matrix[i][j];
					else
					result+=matrix[i][j]+",";
				}
		return result;
	}
}
