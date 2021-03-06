package flavor.pie.example.data;

import org.spongepowered.api.data.DataContainer;
import org.spongepowered.api.data.DataHolder;
import org.spongepowered.api.data.DataQuery;
import org.spongepowered.api.data.DataView;
import org.spongepowered.api.data.manipulator.immutable.common.AbstractImmutableBooleanData;
import org.spongepowered.api.data.manipulator.mutable.common.AbstractBooleanData;
import org.spongepowered.api.data.merge.MergeFunction;
import org.spongepowered.api.data.persistence.AbstractDataBuilder;
import org.spongepowered.api.data.persistence.DataContentUpdater;
import org.spongepowered.api.data.persistence.InvalidDataException;
import org.spongepowered.api.data.value.immutable.ImmutableValue;
import org.spongepowered.api.data.value.mutable.Value;

import java.util.Optional;

// Example of specific singular data
// Example of API-implementation split
// Example of data updating
public class MyBoolDataImpl extends AbstractBooleanData<MyBoolData, MyBoolData.Immutable> implements MyBoolData {
    MyBoolDataImpl(boolean enabled) {
        super(enabled, MyKeys.BOOL_ENABLED, false);
    }

    @Override
    public Value<Boolean> enabled() {
        return getValueGetter();
    }

    @Override
    public Optional<MyBoolData> fill(DataHolder dataHolder, MergeFunction overlap) {
        Optional<MyBoolDataImpl> data_ = dataHolder.get(MyBoolDataImpl.class);
        if (data_.isPresent()) {
            MyBoolDataImpl data = data_.get();
            MyBoolDataImpl finalData = overlap.merge(this, data);
            setValue(finalData.getValue());
        }
        return Optional.of(this);
    }

    @Override
    public Optional<MyBoolData> from(DataContainer container) {
        return from((DataView) container);
    }

    public Optional<MyBoolData> from(DataView view) {
        if (view.contains(MyKeys.BOOL_ENABLED.getQuery())) {
            setValue(view.getBoolean(MyKeys.BOOL_ENABLED.getQuery()).get());
            return Optional.of(this);
        } else {
            return Optional.empty();
        }
    }

    @Override
    public MyBoolDataImpl copy() {
        return new MyBoolDataImpl(getValue());
    }

    @Override
    public Immutable asImmutable() {
        return new Immutable(getValue());
    }

    @Override
    public int getContentVersion() {
        return 2;
    }

    @Override
    public DataContainer toContainer() {
        return super.toContainer().set(MyKeys.BOOL_ENABLED.getQuery(), getValue());
    }

    public static class Immutable extends AbstractImmutableBooleanData<MyBoolData.Immutable, MyBoolData> implements MyBoolData.Immutable {
        Immutable(boolean enabled) {
            super(enabled, MyKeys.BOOL_ENABLED, false);
        }

        @Override
        public MyBoolDataImpl asMutable() {
            return new MyBoolDataImpl(getValue());
        }

        @Override
        public int getContentVersion() {
            return 2;
        }

        @Override
        public DataContainer toContainer() {
            return super.toContainer().set(MyKeys.BOOL_ENABLED.getQuery(), getValue());
        }

        @Override
        public ImmutableValue<Boolean> enabled() {
            return getValueGetter();
        }
    }
    public static class Builder extends AbstractDataBuilder<MyBoolData> implements MyBoolData.Builder {
        Builder() {
            super(MyBoolData.class, 2);
        }

        @Override
        public MyBoolDataImpl create() {
            return new MyBoolDataImpl(false);
        }

        @Override
        public Optional<MyBoolData> createFrom(DataHolder dataHolder) {
            return create().fill(dataHolder);
        }

        @Override
        protected Optional<MyBoolData> buildContent(DataView container) throws InvalidDataException {
            return create().from(container);
        }
    }
    public static class BoolEnabled1To2Updater implements DataContentUpdater {

        @Override
        public int getInputVersion() {
            return 1;
        }

        @Override
        public int getOutputVersion() {
            return 2;
        }

        @Override
        public DataView update(DataView content) {
            return content.set(DataQuery.of('.', "bool.enabled"), content.get(DataQuery.of('.', "bool.isEnabled"))).remove(DataQuery.of('.', "bool.isEnabled"));
        }
    }
}
