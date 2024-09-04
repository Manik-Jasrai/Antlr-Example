package expression;

public class Number extends Expression{
    int num;

    public Number(int n) {
        this.num = n;
    }

    @Override
    public String toString() {
        return Integer.valueOf(num).toString();
    }
}
