package edu.example.ocfl;
import edu.example.ocfl.createdate_as_fixity.OCFLTestWithCreateDateAsFixity;
import edu.example.ocfl.size_as_fixity.OCFLTestWithSizeAsFixity;

public class Main {

    public static void main(String[] args) {
        OCFLTestWithSizeAsFixity app = new OCFLTestWithSizeAsFixity();
        app.run();
        
        OCFLTestWithCreateDateAsFixity app2 = new OCFLTestWithCreateDateAsFixity();
        app2.run();

    }

}
