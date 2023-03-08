package thedivazo.wrapper;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import thedivazo.parserexpression.exception.InterpreterException;
import thedivazo.parserexpression.interpreter.wrapper.WrapperMethod;
import thedivazo.parserexpression.interpreter.wrapper.WrapperObject;

import javax.annotation.Nullable;
import java.lang.constant.ConstantDesc;
import java.util.*;

@RequiredArgsConstructor
public abstract class AbstractWrapperObject<T> implements WrapperObject<T> {

    @Override
    public Optional<? extends ConstantDesc> describeConstable() {
        return Optional.empty();
    }

    interface AnyMethod<T>{}

    @FunctionalInterface
    interface ArgMethod<T> extends AnyMethod<T> {
        T getResult(Object... inputs);
    }

    @FunctionalInterface
    interface NoArgMethod<T> extends AnyMethod<T> {
        T getResult();
    }


    @Getter
    @Nullable
    protected final T object;
    protected final Set<WrapperMethod<?>> methods = new HashSet<>();


    @Override
    public Set<WrapperMethod<?>> getMethods() {
        return Collections.unmodifiableSet(methods);
    }

    protected WrapperMethod<?> getMethod(String nameMethod, Class<?>... methodArgumentsType) throws InterpreterException {
        Optional<WrapperMethod<?>> optionalWrapperMethod = methods.stream().filter(wrapperMethod -> wrapperMethod.equals(nameMethod, methodArgumentsType)).findFirst();
        if(optionalWrapperMethod.isEmpty()) throw new InterpreterException(String.format("ArgMethod \"%s\" not find", nameMethod));
        return optionalWrapperMethod.orElseGet(null);
    }

    @Override
    public boolean hasMethod(String nameMethod, Class<?>... methodArgumentsType) {
        try {
            getMethod(nameMethod, methodArgumentsType);
            return true;
        } catch (InterpreterException e) {
            return false;
        }
    }

    @Override
    public boolean hasMethod(String nameMethod, Collection<?> methodArguments) {
        return hasMethod(nameMethod, methodArguments.stream().map(Object::getClass).toList().toArray(new Class[0]));
    }

    protected boolean addMethod(WrapperMethod<?> wrapperMethod) {
        return methods.add(wrapperMethod);
    }

    protected static <V> void addMethod(Set<WrapperMethod<?>> methods, String nameMethod, NoArgMethod<V> methodBody) {
        addMethod(methods, nameMethod, methodBody, new Class<?>[0]);
    }
    protected static <V> void addMethod(Set<WrapperMethod<?>> methods, String nameMethod, AnyMethod<V> methodBody, Class<?>... argumentsTypes) {
        WrapperMethod<V> wrapperMethod = new AbstractWrappedMethod<V>(nameMethod, argumentsTypes) {
            @Override
            public Optional<? extends ConstantDesc> describeConstable() {
                return Optional.empty();
            }

            @Override
            public V execute(Object... arguments) {
                if(methodBody instanceof AbstractWrapperObject.ArgMethod<V> argMethod) return argMethod.getResult(arguments);
                else if(methodBody instanceof AbstractWrapperObject.NoArgMethod<V> noArgMethod) return noArgMethod.getResult();
                else return null;
            }

        };
        methods.add(wrapperMethod);
    }


    @Override
    public Object executeMethod(String nameMethod, Collection<?> methodArguments) throws InterpreterException {
        WrapperMethod<?> method = getMethod(nameMethod, methodArguments.stream().map(Object::getClass).toList().toArray(new Class[0]));
        return method.execute(methodArguments.toArray(new Object[0]));
    }
}
