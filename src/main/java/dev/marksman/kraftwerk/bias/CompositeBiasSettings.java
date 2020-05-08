package dev.marksman.kraftwerk.bias;

import com.jnape.palatable.lambda.functions.Fn1;
import dev.marksman.kraftwerk.SizeParameters;
import dev.marksman.kraftwerk.constraints.ByteRange;
import dev.marksman.kraftwerk.constraints.CharRange;
import dev.marksman.kraftwerk.constraints.DoubleRange;
import dev.marksman.kraftwerk.constraints.FloatRange;
import dev.marksman.kraftwerk.constraints.IntRange;
import dev.marksman.kraftwerk.constraints.LongRange;
import dev.marksman.kraftwerk.constraints.ShortRange;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;

import static com.jnape.palatable.lambda.functions.builtin.fn1.Id.id;

@AllArgsConstructor(access = AccessLevel.PRIVATE)
final class CompositeBiasSettings implements BiasSettings {
    private final BiasSettings first;
    private final BiasSettings second;

    @Override
    public BiasSetting<Integer> intBias(IntRange range) {
        return compose(bs -> bs.intBias(range));
    }

    @Override
    public BiasSetting<Long> longBias(LongRange range) {
        return compose(bs -> bs.longBias(range));
    }

    @Override
    public BiasSetting<Float> floatBias(FloatRange range) {
        return compose(bs -> bs.floatBias(range));
    }

    @Override
    public BiasSetting<Double> doubleBias(DoubleRange range) {
        return compose(bs -> bs.doubleBias(range));
    }

    @Override
    public BiasSetting<Byte> byteBias(ByteRange range) {
        return compose(bs -> bs.byteBias(range));
    }

    @Override
    public BiasSetting<Short> shortBias(ShortRange range) {
        return compose(bs -> bs.shortBias(range));
    }

    @Override
    public BiasSetting<Character> charBias(CharRange range) {
        return compose(bs -> bs.charBias(range));
    }

    @Override
    public BiasSetting<Integer> sizeBias(SizeParameters sizeParameters) {
        return compose(bs -> bs.sizeBias(sizeParameters));
    }

    private <A> BiasSetting<A> compose(Fn1<BiasSettings, BiasSetting<A>> f) {
        return f.apply(first)
                .match(__ -> f.apply(second),
                        id());
    }

    static BiasSettings compositeBiasSettings(BiasSettings first,
                                              BiasSettings second) {
        return new CompositeBiasSettings(first, second);
    }
}
