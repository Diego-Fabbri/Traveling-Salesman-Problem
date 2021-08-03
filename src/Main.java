
import java.io.FileNotFoundException;
import java.io.PrintStream;


// Link : https://www.youtube.com/watch?v=QzOLL2tUXKE
public class Main {
    public static void main(String[] args) throws FileNotFoundException{
        System.setOut(new PrintStream("TSP_Random.log"));
        
        int problem_size= 10 ;// < ---- insert problem size
        
        TSP.solveTSP(problem_size);
    }
    
}
