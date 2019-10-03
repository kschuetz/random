package dev.marksman.composablerandom.primitives;

import dev.marksman.collectionviews.ImmutableNonEmptyVector;
import dev.marksman.composablerandom.GeneratorState;
import dev.marksman.composablerandom.Result;
import dev.marksman.composablerandom.Seed;

import static dev.marksman.composablerandom.Result.result;

public class BiasImpl<Elem> implements GeneratorState<Elem> {
    private final ImmutableNonEmptyVector<Elem> elements;
    private final int biasWeight;
    private final long totalWeight;
    private final GeneratorState<Elem> inner;

    private BiasImpl(ImmutableNonEmptyVector<Elem> elements, long innerWeight, GeneratorState<Elem> inner) {
        this.elements = elements;
        this.biasWeight = elements.size();
        this.totalWeight = Math.max(0, innerWeight) + biasWeight;
        this.inner = inner;
    }

    @Override
    public Result<? extends Seed, Elem> run(Seed input) {
        long n = input.getSeedValue() % totalWeight;
        if (n < biasWeight) {
            Result<? extends Seed, Integer> nextSeed = input.nextInt();
            return result(nextSeed.getNextState(), elements.unsafeGet((int) n));
        } else {
            return inner.run(input);
        }
    }

    public static <Elem> BiasImpl<Elem> biasImpl(ImmutableNonEmptyVector<Elem> elements, long innerWeight, GeneratorState<Elem> inner) {
        return new BiasImpl<>(elements, innerWeight, inner);
    }

}
