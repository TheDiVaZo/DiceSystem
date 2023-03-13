package thedivazo.wrapper;

import java.lang.constant.ConstantDesc;
import java.util.Map;
import java.util.Optional;

public class MapWrapper<K, V, T> extends AbstractWrapperObject<Map<K,V>, T>{
    public MapWrapper(Map<K, V> object) {
        super(object);
        AbstractWrapperObject.addMethod(getWrapperMethodSet(),
                "get",
                (wrapperObjectContext, objects) -> wrapperObjectContext.getObject().get(objects[0]), String.class);
        AbstractWrapperObject.addMethod(getWrapperMethodSet(),
                "isEmpty",
                wrapperObjectContext -> wrapperObjectContext.getObject().isEmpty());
        AbstractWrapperObject.addMethod(getWrapperMethodSet(),
                "size",
                wrapperObjectContext -> wrapperObjectContext.getObject().size());
        AbstractWrapperObject.addMethod(getWrapperMethodSet(),
                "containsKey",
                (wrapperObjectContext, objects) -> wrapperObjectContext.getObject().containsKey(objects[0]), String.class);
        AbstractWrapperObject.addMethod(getWrapperMethodSet(),
                "containsValue",
                (wrapperObjectContext, objects) -> wrapperObjectContext.getObject().containsValue(objects[0]), String.class);
        AbstractWrapperObject.addMethod(getWrapperMethodSet(),
                "keySet",
                wrapperObjectContext -> new CollectionWrapper<>(wrapperObjectContext.getObject().keySet()));
        AbstractWrapperObject.addMethod(getWrapperMethodSet(),
                "values",
                wrapperObjectContext -> new CollectionWrapper<>(wrapperObjectContext.getObject().values()));
    }

    @Override
    public Optional<? extends ConstantDesc> describeConstable() {
        return Optional.empty();
    }
}
