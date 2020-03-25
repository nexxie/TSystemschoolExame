package com.tsystems.javaschool.tasks.calculator;

import java.util.*;

public class Calculator {

    /**
     * evaluate1 statement represented as string.
     *
     * @param statement mathematical statement containing digits, '.' (dot) as decimal mark,
     *                  parentheses, operations signs '+', '-', '*', '/'<br>
     *                  Example: <code>(1 + 38) * 4.5 - 1 / 2.</code>
     * @return string value containing result of evaluation or null if statement is invalid
     */
    private static String operators = "+-*/";
    private static String delimiters = "() ";
    private static Map<String, Integer> priorityMap;
    private static Set<String> delimiterSet;
    private static Set<String> operatorSet;
    static {
        priorityMap = new HashMap<>();
        delimiterSet = new HashSet<>();
        operatorSet = new HashSet<>();
        priorityMap.put("(", 1);
        priorityMap.put("-", 2);
        priorityMap.put("+", 2);
        priorityMap.put("*", 3);
        priorityMap.put("/", 3);
        for (int i = 0; i < delimiters.length(); i++) {
            delimiterSet.add("" + delimiters.charAt(i));
        }
        for (int i = 0; i < operators.length(); i++) {
            operatorSet.add("" + operators.charAt(i));
        }
    }

    private static boolean isDelimiter(String token) {
        return delimiterSet.contains(token);
    }

    private static boolean isOperator(String token) {
        return operatorSet.contains(token);
    }

    private static int priority(String token) throws CalculatorParseException{
        int priority = priorityMap.get(token);
        if (token == null)
            throw new CalculatorParseException("wrong priority item");
        return priority;
    }

    private static TokenType typeRecognizer(String token) {
        if (token.equals(" "))
            return TokenType.SPACE;
        if (isOperator(token))
            return TokenType.OPERATOR;
        if (isDelimiter(token))
            return TokenType.DELIMITER;
        return TokenType.OPERAND;
    }

    private enum TokenType{
        SPACE,
        OPERAND,
        OPERATOR,
        DELIMITER
    }

    public static List<String> parse(String infix) throws CalculatorParseException {
        List<String> strOut = new ArrayList<String>();
        Deque<String> stack = new ArrayDeque<String>();
        StringTokenizer tokenizer = new StringTokenizer(infix, delimiters + operators, true);
        String prev = "";
        String curr = "";
        while (tokenizer.hasMoreTokens()) {
            curr = tokenizer.nextToken();
            switch (typeRecognizer(curr)) {
                case SPACE:
                    continue;

                case OPERAND:
                    strOut.add(curr);
                    break;

                case DELIMITER:
                    if (curr.equals("(")) stack.push(curr);
                    else if (curr.equals(")")) {
                        while (!stack.peek().equals("(")) {
                            strOut.add(stack.pop());
                            if (stack.isEmpty()) {
                                throw new CalculatorParseException("Parentheses unpaired");
                            }
                        }
                        stack.pop();
                    }
                    break;

                case OPERATOR:
                    if (!tokenizer.hasMoreTokens()) {
                        throw new CalculatorParseException("Incorrect syntax");
                    }
                    if (curr.equals("-") && (prev.equals("") || (isDelimiter(prev)  && !prev.equals(")")))) {
                        strOut.add("0");
                    }
                    else {
                        while (!stack.isEmpty() && (priority(curr) <= priority(stack.peek()))) {
                            strOut.add(stack.pop());
                        }

                    }
                    stack.push(curr);
                    break;
            }
            if (isOperator(prev) && isOperator(curr))
                throw new CalculatorParseException("two operands in a row operand");
            prev = curr;
        }

        while (!stack.isEmpty()) {
            if (isOperator(stack.peek())) strOut.add(stack.pop());
            else {
                throw new CalculatorParseException("Parentheses unpaired");
            }
        }
        return strOut;
    }

    public static Double calc(List<String> strOut) throws CalculateException {
        Deque<Double> stack = new ArrayDeque<Double>();
        Double a, b;
        for (String x : strOut) {
            switch (x) {
                case "+":
                    stack.push(stack.pop() + stack.pop());
                    break;

                case "-":
                    b = stack.pop();
                    a = stack.pop();
                    stack.push(a - b);
                    break;

                case "*":
                    stack.push(stack.pop() * stack.pop());
                    break;

                case "/":
                    b = stack.pop();
                    if (b == 0)
                        throw new CalculateException("division by null");
                    a = stack.pop();
                    stack.push(a / b);
                    break;

                default:
                    try {
                        stack.push(Double.parseDouble(x));
                        break;
                    } catch (NumberFormatException e) {
                        throw new CalculateException("Element parse error");
                    }
            }
        }
        return stack.pop();
    }

    public String evaluate(String statement) {
        if (statement == null || statement == "")
            return null;
        String stringResult;
        try {
            Double doubleResult = calc(parse(statement));
            if (doubleResult == Math.floor(doubleResult)) {
                stringResult = String.valueOf(doubleResult.intValue());
            } else {
                stringResult = String.valueOf(doubleResult);
            }
        } catch (CalculatorParseException cpe) {
            System.out.println(cpe.getMessage());
            return null;
        } catch (CalculateException ce){
            System.out.println(ce.getMessage());
            return null;
        }
        return stringResult;
    }

}