package expression;

import java.util.*;

public class ExpressionProcessor {
    List<Expression> list;
    Map<String,Integer> values; // Symbol Table for storing values of variables
    public List<String> runtimeErrors;

    public ExpressionProcessor(List<Expression> list) {
        this.list = list;
        values = new HashMap<>();
        this.runtimeErrors = new ArrayList<>();
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
                evaluationResult.add(input + " is " + result);
                if (e instanceof Addition || e instanceof Multiplication) {
                	int otherResult = getOtherResult(e);
                    evaluationResult.add("Secondary : " + input + " is " + otherResult);
                }
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
            Expression left = ((Multiplication)e).left;
            Expression right = ((Multiplication)e).right;

            result = getResult(left) * getResult(right);
        }
        return result;
    }
    
    private int getOtherResult(Expression e) {
        if (e instanceof Number) {
            return ((Number)e).num;
        } else if (e instanceof Variable) {
            String id = ((Variable)e).id;
            return values.get(id);
        } else if (e instanceof Addition) {
            Expression left = ((Addition)e).left;
            Expression right = ((Addition)e).right;
            return getOtherResult(left) - getOtherResult(right);
        } else if (e instanceof Multiplication) {
            Expression left = ((Multiplication)e).left;
            Expression right = ((Multiplication)e).right;
            int leftValue = getOtherResult(left);
            int rightValue = getOtherResult(right);
            
            if (rightValue == 0) {
                String errorMsg = "Runtime error: Division by zero in expression " + e.toString();
                runtimeErrors.add(errorMsg);
                return Integer.MIN_VALUE; // Indicating an error
            }
            
            return leftValue / rightValue;
        }
        
        String errorMsg = "Runtime error: Unknown expression type " + e.getClass().getSimpleName();
        runtimeErrors.add(errorMsg);
        return Integer.MIN_VALUE;
    }
}
