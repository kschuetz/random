package dev.marksman.composablerandom.instructions;

import dev.marksman.composablerandom.Generate;
import dev.marksman.composablerandom.RandomState;
import dev.marksman.composablerandom.Result;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;

@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class NextIntBoundedImpl implements Generate<Integer> {
    private final int bound;

    @Override
    public Result<? extends RandomState, Integer> generate(RandomState input) {
        return input.nextIntBounded(bound);
    }

    public static NextIntBoundedImpl nextIntBoundedImpl(int bound) {
        return new NextIntBoundedImpl(bound);
    }
}
