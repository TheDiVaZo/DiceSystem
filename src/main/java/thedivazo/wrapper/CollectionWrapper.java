package thedivazo.wrapper;

import lombok.AccessLevel;
import lombok.Getter;
import thedivazo.parserexpression.exception.InterpreterException;
import thedivazo.parserexpression.interpreter.wrapper.WrapperMethod;
import thedivazo.parserexpression.interpreter.wrapper.WrapperObject;

import javax.annotation.Nullable;
import java.lang.constant.ConstantDesc;
import java.util.Collection;
import java.util.HashSet;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

public class CollectionWrapper<E, T> extends AbstractWrapperObject<Collection<E>, T> {

    public CollectionWrapper(Collection<E> object) {
        super(object);
        AbstractWrapperObject.addMethod(getWrapperMethodSet(),
                "isEmpty",
                wrapperObjectContext -> wrapperObjectContext.getObject().isEmpty());
        AbstractWrapperObject.addMethod(getWrapperMethodSet(),
                "size",
                wrapperObjectContext -> wrapperObjectContext.getObject().size());
        AbstractWrapperObject.addMethod(getWrapperMethodSet(),
                "contains",
                (wrapperObjectContext, objects) -> wrapperObjectContext.getObject().contains(objects[0]), WrapperObject.class);
        AbstractWrapperObject.addMethod(getWrapperMethodSet(),
                "containsAll",
                (wrapperObjectContext, objects) -> wrapperObjectContext.getObject().contains(objects[0]), CollectionWrapper.class);
    }


    @Override
    public Optional<? extends ConstantDesc> describeConstable() {
        return Optional.empty();
    }
}
