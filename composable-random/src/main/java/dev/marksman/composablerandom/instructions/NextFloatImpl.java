package dev.marksman.composablerandom.instructions;

import dev.marksman.composablerandom.Generate;
import dev.marksman.composablerandom.RandomState;
import dev.marksman.composablerandom.Result;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;

@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class NextFloatImpl implements Generate<Float> {
    private static NextFloatImpl INSTANCE = new NextFloatImpl();

    @Override
    public Result<? extends RandomState, Float> generate(RandomState input) {
        return input.nextFloat();
    }

    public static NextFloatImpl nextFloatImpl() {
        return INSTANCE;
    }
}
