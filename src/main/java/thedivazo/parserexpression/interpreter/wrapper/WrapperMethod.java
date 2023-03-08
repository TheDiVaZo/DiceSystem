package thedivazo.parserexpression.interpreter.wrapper;

import java.lang.constant.Constable;
import java.util.Arrays;

public interface WrapperMethod<V> extends Constable {
    String getMethodName();
    Class<?>[] getArgumentTypes();
    V execute(Object... arguments);

    boolean equals(String methodName, Class<?>[] argumentTypes);
}
