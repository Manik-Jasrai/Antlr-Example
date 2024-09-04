package app;

import java.util.List;

import org.antlr.v4.runtime.CharStream;
import org.antlr.v4.runtime.CharStreams; 
import org.antlr.v4.runtime.CommonTokenStream;
import org.antlr.v4.runtime.tree.ParseTree;

import antlr.ExprLexer;
import antlr.ExprParser;
import expression.AntlrToProgram;
import expression.ExpressionProcessor;
import expression.Program;

public class ExpressionApp {
    public static void main(String[] args) {
        if (args.length != 1) {
            System.err.println("Error : file name");
            return;
        }
        String fileName = args[0];
        ExprParser parser = getParser(fileName);

        // build a parse tree
        ParseTree antlrTree =  parser.prog();

        // Create a visitor for converting the parse tree into our model tree(Expression Objects)
        AntlrToProgram progVisitor = new AntlrToProgram();
        Program prog = progVisitor.visit(antlrTree);
        
        if (progVisitor.semanticErrors.isEmpty()) {
            // No errors
            ExpressionProcessor processor = new ExpressionProcessor(prog.expressions);
            List<String> results = processor.getEvaluationResults();

            for(String res : results) {
                System.out.println(res);
            }
        } else {
            for(String err : progVisitor.semanticErrors) {
                System.err.println(err);
            }
        }
    }

    // Generating a parser from the input file
    private static ExprParser getParser(String filename) {
        ExprParser parser = null;
        try {
            CharStream input = CharStreams.fromFileName(filename);
            // To Create the tokens for the file
            ExprLexer lexer = new ExprLexer(input);
            CommonTokenStream token = new CommonTokenStream(lexer);
            parser = new ExprParser(token);
        } catch (Exception e) {
            e.printStackTrace();
        }

        return parser;
    }
}
