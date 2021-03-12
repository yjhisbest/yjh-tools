package com.yjh.tools.core.util;

import lombok.extern.slf4j.Slf4j;
import org.springframework.core.LocalVariableTableParameterNameDiscoverer;
import org.springframework.expression.EvaluationContext;
import org.springframework.expression.Expression;
import org.springframework.expression.ExpressionParser;
import org.springframework.expression.spel.standard.SpelExpressionParser;
import org.springframework.expression.spel.support.StandardEvaluationContext;

import java.lang.reflect.Method;

@Slf4j
public class SpelUtil {

    /**
     * 解析表达式
     * */
    private static ExpressionParser parser = new SpelExpressionParser();

    private static LocalVariableTableParameterNameDiscoverer discoverer = new LocalVariableTableParameterNameDiscoverer();

    /**
     * 解析spel表达式
     * @param method 方法
     * @param arguments 方法的参数列表
     * @param spel spel表达式
     * @param clazz spel表达式解析弯沉过后返回的类型
     * @param defaultResult 解析spel表达式的默认值
     * */
    public static <T> T parseSpel(Method method, Object[] arguments, String spel, Class<T> clazz, T defaultResult) {
        // 获取方法的参数列表
        String[] params = discoverer.getParameterNames(method);
        if (params == null) {
            return defaultResult;
        }
        EvaluationContext context = new StandardEvaluationContext();
        for (int len = 0; len < params.length; len++) {
            context.setVariable(params[len], arguments[len]);
        }
        try {
            Expression expression = parser.parseExpression(spel);
            return expression.getValue(context, clazz);
        } catch (Exception e) {
            log.error("解析spel表达式出错，将使用默认值{}返回", defaultResult, e);
            return defaultResult;
        }
    }

    /**
     * 创建虚拟容器
     * @param method 方法
     * @param arguments 方法的参数列表
     * */
    public static EvaluationContext buildEvaluationContext(Method method, Object[] arguments) {
        // 获取方法的参数列表
        String[] params = discoverer.getParameterNames(method);
        if (params == null) {
            return null;
        }
        EvaluationContext context = new StandardEvaluationContext();
        for (int len = 0; len < params.length; len++) {
            context.setVariable(params[len], arguments[len]);
        }
        return context;
    }

    /**
     * 解析spel表达式
     * @param spel spel表达式
     * @param clazz spel表达式解析弯沉过后返回的类型
     * @param defaultResult 解析spel表达式的默认值
     * */
    public static <T> T parseSpel(String spel, EvaluationContext context, Class<T> clazz, T defaultResult) {
        if (context == null) {
            return defaultResult;
        }
        try {
            Expression expression = parser.parseExpression(spel);
            return expression.getValue(context, clazz);
        } catch (Exception e) {
            log.error("解析spel表达式出错，将使用默认值{}返回", defaultResult, e);
            return defaultResult;
        }
    }

}
