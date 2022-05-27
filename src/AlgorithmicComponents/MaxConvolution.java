package AlgorithmicComponents;

import java.util.ArrayList;

import logger.printSchedule;
import util.ConvolutionElement;
import util.Job;

public class MaxConvolution {


    /**
     * native Approach for a max convolution on 2 seqences with equal length.
     * @param seqA
     * @param seqB
     * @param len
     * @return
     */
    public static ConvolutionElement[] nativeApproach(ConvolutionElement[] seqA, ConvolutionElement[] seqB, int len) {

        ConvolutionElement[] result = new ConvolutionElement[len];
        for(int j = 0; j < len; j++) {
            ConvolutionElement bestElement = new ConvolutionElement(0, new ArrayList<>());
            int x = 0;
            int y = j;
            while(y >= 0) {
                if(seqA[y].getProfit() + seqB[x].getProfit() > bestElement.getProfit()) {
                    ArrayList<Job> selected_jobs = new ArrayList<>();
                    selected_jobs.addAll(seqA[y].getJobs());
                    selected_jobs.addAll(seqB[x].getJobs());
                    bestElement = new ConvolutionElement(seqB[x].getProfit() + seqA[y].getProfit(), selected_jobs);
                }
                x++;
                y--;
            }
            result[j] = bestElement;
        }

        return result;
    }


    public static ConvolutionElement[] linearApproach(ConvolutionElement[] seqA, ConvolutionElement[] seqB) {

        ImaginaryMatrix A = new ImaginaryMatrix(seqA, seqB);

        linearApproach_maxCompute(A);
        int result = A.getElement(1, 1);
        return null;
        //REDUCE

    }


    private static ImaginaryMatrix linearApproach_maxCompute(ImaginaryMatrix A) {
        System.out.println(A);
        ImaginaryMatrix B = linearApproach_reduce(A);
        if(B.getColumns() == 1) {
            return null; //TODO
        }
        B.deleteRows();
        return linearApproach_maxCompute(B);
        
    }

    private static ImaginaryMatrix linearApproach_reduce(ImaginaryMatrix A) {        //TODO: nb. Rows as constant (n)
        
        int k = 0;
        while(A.getColumns() > A.getRows()) {
            if(A.getElement(k, k) > A.getElement(k, k + 1)) {
                if(k < A.getRows()) {
                    k++;
                }
                if(k == A.getRows()) {
                    A.deleteColumn(k + 1);;      //delete the next column
                }
            }
            if(A.getElement(k, k) < A.getElement(k, k + 1)) {
                A.deleteColumn(k);
                k--;
            }
        }

        return A;
    }
    
}

class ImaginaryMatrix {

    ConvolutionElement[] seqA;
    ConvolutionElement[] seqB;

    ArrayList<Integer> killedCols = new ArrayList<>(); //TODO array. values are monotone
    ArrayList<Integer> killedRows = new ArrayList<>();

    ImaginaryMatrix(ConvolutionElement[] seqA, ConvolutionElement[] seqB) {
        this.seqA = seqA;
        this.seqB = seqB;
    }

    private int getRealRow(int i) {
        int x = 0;
        while (i > 0) {
            if(!killedRows.contains(x)) {
                i--;
            }
            x++;;
        }
        return x;
    }
    private int getRealColumn(int j) {
        int y = 0;
        while (j > 0) {
            if(!killedCols.contains(y)) {
                j--;
            }
            y++;;
        }
        return y;
    }

    /**
     * A_ij = a_j + b_{i-j}
     * @param i
     * @param j
     * @return
     */
    int getElement(int i, int j) {
        try {
            return seqA[getRealColumn(j)].getProfit() + seqB[getRealRow(i) - getRealColumn(j)].getProfit();

        } catch(IndexOutOfBoundsException e) {
            return Integer.MIN_VALUE;
        }
    }

    int getColumns() {
        return this.seqA.length - killedCols.size();
    }

    int getRows() {
        return this.seqB.length - killedRows.size();
    }
    
    /**
     * Delete every 2nd row. 
     * C <- b[2, 4, ..., 2(n/2)]
     */
    void deleteRows() {
        boolean kill = false;
        for(int i = 0; i < seqB.length; i++) {
            if(this.killedRows.contains(i)) {
                continue;
            }
            kill = !kill;
            if(kill) {
                continue;
            }
            this.killedRows.add(i);

        }
    }
    void deleteColumn(int j) {
        this.killedCols.add(getRealColumn(j));
    }

    @Override
    public String toString() {
        String res = "";
        for(int i = 0; i < this.getRows(); i++) {
            for(int j = 0; j < this.getColumns(); j++) {
                res += this.getElement(i, j) + "|";    
            }
            res += "\n" + "-".repeat(this.getColumns() * 2) + "\n";
        }
        return res;
    }
}
