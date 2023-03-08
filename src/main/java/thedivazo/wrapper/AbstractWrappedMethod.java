package thedivazo.wrapper;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import thedivazo.parserexpression.interpreter.wrapper.WrapperMethod;

import java.util.Arrays;
import java.util.Objects;

@Getter
@RequiredArgsConstructor
public abstract class AbstractWrappedMethod<V> implements WrapperMethod<V> {
    @Getter
    private final String methodName;
    @Getter
    private final Class<?>[] argumentTypes;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof AbstractWrappedMethod<?> that)) return false;
        return equals(that.getMethodName(), that.getArgumentTypes());
    }

    @Override
    public boolean equals(String methodName, Class<?>[] argumentTypes) {
        return getMethodName().equals(methodName) && Arrays.equals(getArgumentTypes(), argumentTypes);
    }

    @Override
    public int hashCode() {
        int result = Objects.hash(getMethodName());
        result = 31 * result + Arrays.hashCode(getArgumentTypes());
        return result;
    }

}
