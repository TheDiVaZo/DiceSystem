package thedivazo.wrapper;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import thedivazo.parserexpression.exception.InterpreterException;
import thedivazo.parserexpression.interpreter.wrapper.WrapperMethod;
import thedivazo.parserexpression.interpreter.wrapper.WrapperObject;

import javax.annotation.Nullable;
import java.lang.constant.ConstantDesc;
import java.util.*;
import java.util.stream.Collectors;

@RequiredArgsConstructor
public abstract class AbstractWrapperObject<T, T1> implements WrapperObject<T, T1> {

    @Getter
    protected final T object;

    @Getter(AccessLevel.PROTECTED)
    protected final Set<WrapperMethod<T, T1, ?>> wrapperMethodSet = new HashSet<>();

    protected static <T2, T3, V1> void addMethod(Set<WrapperMethod<T2, T3,?>> methods, String name, ArgMethod<T2, T3, V1> methodBody, Class<?>... argumentTypes) {
        WrapperMethod<T2, T3, V1> wrapperMethod = new AbstractWrappedMethod<>(name, argumentTypes) {
            @Override
            public V1 execute(WrapperObject<T2, T3> wrapperObjectContext, T3... arguments) {
                return methodBody.getResult(wrapperObjectContext, arguments);
            }

            @Override
            public Optional<? extends ConstantDesc> describeConstable() {
                return Optional.empty();
            }
        };
        methods.add(wrapperMethod);
    }

    protected static <T1,T2, V1> void addMethod(Set<WrapperMethod<T1,T2, ?>> methods,String name, NoArgMethod<T1,T2, V1> methodBody) {
        WrapperMethod<T1,T2, V1> wrapperMethod = new AbstractWrappedMethod<T1,T2, V1>(name, new Class[0]) {
            @Override
            public V1 execute(WrapperObject<T1,T2> wrapperObjectContext, T2 ... arguments) {
                return methodBody.getResult(wrapperObjectContext);
            }

            @Override
            public Optional<? extends ConstantDesc> describeConstable() {
                return Optional.empty();
            }
        };
        methods.add(wrapperMethod);
    }
    @Override
    public boolean hasMethod(String nameMethod, Class<?>... methodArgumentsType) {
        return getWrapperMethodSet().stream().anyMatch(wrapperMethod -> wrapperMethod.equals(nameMethod, methodArgumentsType));
    }

    @Override
    public Set<String> getMethodsName() {
        return getWrapperMethodSet().stream().map(WrapperMethod::getMethodName).collect(Collectors.toSet());
    }

    @Nullable
    @Override
    public Object executeMethod(String nameMethod, T1... methodArguments) throws InterpreterException {
        if(Objects.isNull(object)) return null;
        Optional<WrapperMethod<T, T1, ?>> wrapperMethodFinal = getWrapperMethodSet().stream().filter(wrapperMethod->wrapperMethod.equals(nameMethod, Arrays.stream(methodArguments).map(Object::getClass).toList().toArray(new Class[0]))).findFirst();
        if(wrapperMethodFinal.isEmpty()) return null;
        else return wrapperMethodFinal.get().execute(this, methodArguments);
    }

    interface AnyMethod<T>{}

    @FunctionalInterface
    interface ArgMethod<T,T1, V> extends AnyMethod<T> {
        V getResult(WrapperObject<T, T1> wrapperObjectContext, T1... inputs);
    }

    @FunctionalInterface
    interface NoArgMethod<T,T1, V> extends AnyMethod<T> {
        V getResult(WrapperObject<T, T1> wrapperObjectContext);
    }
}
