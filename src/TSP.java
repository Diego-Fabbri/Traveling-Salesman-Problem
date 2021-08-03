
import ilog.concert.*;
import ilog.cplex.*;

import java.awt.Color;
import java.awt.Paint;
import java.io.File;
import java.io.IOException;
import java.text.NumberFormat;
import java.util.Iterator;
import java.util.List;


// Taken from : https://www.youtube.com/watch?v=QzOLL2tUXKE
public class TSP {
    
    public static void solveTSP(int n){
        // Random instance generation
        double[] xPos = new double[n];
        double[] yPos = new double[n];
        
        for(int i = 0; i < n; i++){
            xPos[i] = Math.random() * 100;// x coordinate
            yPos[i] = Math.random() * 100;// y coordinate
        }
        
        double[][] c = new double[n][n];
        
        for(int i = 0; i < n; i++){
            for(int j = 0; j < n; j++){
                c[i][j] = Math.sqrt(Math.pow(xPos[j] - xPos[i], 2) + Math.pow(yPos[j] - yPos[i], 2));
                // il costo c_ij è la distanza euclidea tra i punti i e j
            }
        }
        
        //Modeling
        try{
            IloCplex cplex = new IloCplex();
            // Variables
            IloNumVar[][] x = new IloNumVar[n][];
            for(int i = 0; i < n; i++){
                x[i] = cplex.boolVarArray(n);
            }
            IloNumVar[] u = new IloNumVar[n];
            for(int i = 0; i < n; i++){
                u[i] = cplex.numVar(0, Double.MAX_VALUE);
            }
            
            //Objective
            IloLinearNumExpr objective = cplex.linearNumExpr();
            for (int i = 0; i < n; i++) {
                for (int j = 0; j < n; j++) {
                    objective.addTerm(c[i][j], x[i][j]);
                }
            }
            cplex.addMinimize(objective);
            
            //Constraints
            for(int j = 0; j < n; j++){//degree constraints.
                IloLinearNumExpr exp = cplex.linearNumExpr();
                for(int i = 0; i < n; i++){
                    if(i != j)
                        exp.addTerm(1, x[i][j]);
                }
                cplex.addEq(exp, 1);
            }
            
            for(int i = 0; i < n; i++){//degree constraints.
                IloLinearNumExpr exp = cplex.linearNumExpr();
                for(int j = 0; j < n; j++){
                    if(j != i)
                        exp.addTerm(1, x[i][j]);
                }
                cplex.addEq(exp, 1);
            }
            
            for(int i = 1; i < n; i++){//connectivity constraints).
                for(int j = 1; j < n; j++){
                    if(i != j){
                       IloLinearNumExpr exp = cplex.linearNumExpr();
                       exp.addTerm(1, u[i]);
                       exp.addTerm(-1, u[j]);
                       exp.addTerm((n - 1), x[i][j]);
                       cplex.addLe(exp, (n - 2));
                    }
                }
            }
            
            //Solving
            cplex.solve();
            System.out.println();
        System.out.println("Solution status = "+ cplex.getStatus());
        System.out.println();
            cplex.exportModel("TSP_Random.lp");
            System.out.println();
            System.out.println("Costs:");
            for (int i = 0; i < n; i++) {
                System.out.println("From Node : " + i);
                for (int j = 0; j < n; j++) {
                System.out.print("---> to Node "+ j +" cost c["+ i +"]["+ j + "] is: " + c[i][j]);
                System.out.println();
               }
                 System.out.println();
            
            }
            
            
            
            System.out.println("Optimal value = " + cplex.getObjValue());
            System.out.println();

            for (int i = 0; i < n; i++) {
                for (int j = 0; j < n; j++) {
                    if(cplex.getValue(x[i][j])!= 0){
                        System.out.println("x[" + i + "]" + "[" + j + "]" + " = " + cplex.getValue(x[i][j]));
                    }

                }

            }

        } catch (IloException exc) {
            exc.printStackTrace();
        }
    }

}
