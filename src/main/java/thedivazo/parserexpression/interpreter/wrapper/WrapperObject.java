package thedivazo.parserexpression.interpreter.wrapper;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import thedivazo.parserexpression.exception.InterpreterException;

import javax.annotation.Nullable;
import java.lang.constant.Constable;
import java.util.*;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

public interface WrapperObject<T1> extends Constable {
    T1 getObject();

    Set<WrapperMethod<?>> getMethods();
    boolean hasMethod(String nameMethod, Class<?>... methodArgumentsType);
    boolean hasMethod(String nameMethod, Collection<?> methodArguments);

    @Nullable
    Object executeMethod(String nameMethod, Collection<?> methodArguments) throws InterpreterException;
}
