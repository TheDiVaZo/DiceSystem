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

public interface WrapperObject<T, T1> extends Constable {
    T getObject();

    Set<String> getMethodsName();
    boolean hasMethod(String nameMethod, Class<?>... methodArgumentsType);

    @Nullable
    Object executeMethod(String nameMethod, T1... methodArguments) throws InterpreterException;
}
