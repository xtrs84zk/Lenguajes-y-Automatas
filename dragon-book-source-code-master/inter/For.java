package inter;

import symbols.Type;

public class For extends Stmt {

    Expr expr;
    Stmt stmt;

    public For() {
        expr = null;
        stmt = null;
    }

    public void init(Expr x, Stmt s) {
        expr = x;
        stmt = s;
        if (expr.type != Type.Bool) expr.error("boolean required in for");
    }

    public void gen(int b, int a) {
        after = a;                // save label a
        expr.jumping(0, a);
        int label = newlabel();   // label for stmt
        emitlabel(label);
        stmt.gen(label, b);
        emit("goto L" + b);
    }
}
