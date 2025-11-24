public class MatrixNorm {
    public double getMatrixRowNorm(double[][] matrix) {
        double max = 0;
        double sum;
        for (int i = 0; i < matrix.length; i++) {
            sum = 0;
            for (int j = 0; j < matrix.length; j++) {
                sum = sum + Math.abs(matrix[j][i]);
            }
            max = Math.max(max, sum);
        }
        return max;
    }

    public double getRowNorm(double[] row) {
        double max = -1;
        for (int i = 0; i < row.length; i++) {
            max = Math.max(max, Math.abs(row[i]));
        }
        return max;
    }

    public double getColumnNorm(double[][] matrix) {
        double max = 0;
        double sum;
        for (int i = 0; i < matrix.length; i++) {
            sum = 0;
            for (int j = 0; j < matrix.length; j++) {
                sum = sum + Math.abs(matrix[j][i]);
            }
            max = Math.max(max, sum);
        }
        return max;
    }

    public double getFrobeniNorm(double[][] matrix) {
        double sum = 0;
        for (int i = 0; i < matrix.length; i++) {
            for (int j = 0; j < matrix.length; j++) {
                sum += Math.pow(matrix[i][j], 2);
            }
        }
        return Math.sqrt(sum);
    }
}
