package inter;

public class Stmt extends Node {

    public static Stmt Null = new Stmt();
    public static Stmt Enclosing = Stmt.Null;  // used for break stmts
    int after = 0;                   // saves label after

    public Stmt() {
    }

    public void gen(int b, int a) {
    } // called with labels begin and after
}
