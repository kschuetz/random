package dev.marksman.composablerandom;

import com.jnape.palatable.lambda.monad.Monad;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Value;

import java.util.function.Function;

import static dev.marksman.composablerandom.Instruction.flatMapped;
import static dev.marksman.composablerandom.Instruction.mapped;

public interface Generator<A> extends Monad<A, Generator>, ToGenerator<A> {
    Instruction<A> getInstruction();

    @Override
    default Generator<A> toGenerator() {
        return this;
    }

    ;

    @Override
    default <B> Generator<B> fmap(Function<? super A, ? extends B> fn) {
        return generator(mapped(fn, getInstruction()));
    }

    @Override
    default <B> Generator<B> flatMap(Function<? super A, ? extends Monad<B, Generator>> fn) {
        return generator(flatMapped(a -> ((Generator<B>) fn.apply(a))
                        .getInstruction(),
                getInstruction()));
    }

    @Override
    default <B> Generator<B> pure(B b) {
        return generator(Instruction.pure(b));
    }

    static <A> Generator<A> generator(Instruction<A> instruction) {
        return new DefaultGenerator<>(instruction);
    }

    static <A> Generator<A> constant(A a) {
        return generator(Instruction.pure(a));
    }

    @Value
    @AllArgsConstructor(access = AccessLevel.PRIVATE)
    class DefaultGenerator<A> implements Generator<A> {
        private final Instruction<A> instruction;
    }
}
