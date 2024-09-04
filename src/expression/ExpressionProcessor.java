package expression;


import java.util.*;
public class ExpressionProcessor {
    List<Expression> list;
    Map<String,Integer> values; // Symbol Table for storing values of variables

    public ExpressionProcessor(List<Expression> list) {
        this.list = list;
        values = new HashMap<>();
    }

    public List<String> getEvaluationResults() {
        List<String> evaluationResult = new ArrayList<>();

        for(Expression e : list) {
            if (e instanceof VariableDeclaration) {
                VariableDeclaration v  = (VariableDeclaration) e;// TypeCast
                values.put(v.id, v.value);
            } else {
                String input = e.toString();
                int result = getResult(e);
                evaluationResult.add(input + "is" + result);
            }
        }
        return evaluationResult;
    }

    private int getResult(Expression e) {
        int result = 0;
        if ( e instanceof Number) {
            result = ((Number)e).num;
        } else if (e instanceof Variable) {
            String id = ((Variable)e).id;
            result = values.get(id);
        } else if (e instanceof Addition) {
            Expression left = ((Addition)e).left;
            Expression right = ((Addition)e).right;

            result = getResult(left) + getResult(right);
        } else {
            Expression left = ((Addition)e).left;
            Expression right = ((Addition)e).right;

            result = getResult(left) * getResult(right);
        }
        return result;
    }
}
