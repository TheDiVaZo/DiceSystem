package thedivazo.parserexpression.interpreter.wrapper;

public interface WrapperMethod<V> {
    String getMethodName();
    Class<?>[] getArgumentTypes();
    V execute(Object... arguments);
}
