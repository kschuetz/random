package dev.marksman.kraftwerk;

import dev.marksman.kraftwerk.core.BuildingBlocks;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Value;

import static dev.marksman.kraftwerk.Result.result;

public class SizeSelectors {
    private static int DEFAULT_RANGE = 16;

    @Value
    @AllArgsConstructor(access = AccessLevel.PRIVATE)
    public static class MinMaxPreferred implements SizeSelector {
        private final int min;
        private final int max;
        private final int preferred;

        @Override
        public Result<? extends Seed, Integer> checkedApply(Seed input) {
            Result<Seed, Boolean> s1 = shouldUsePreferred(input);
            if (s1.getValue()) {
                return result(s1.getNextState(), preferred);
            } else {
                return BuildingBlocks.nextIntBetween(min, max, s1.getNextState());
            }
        }
    }

    @Value
    @AllArgsConstructor(access = AccessLevel.PRIVATE)
    public static class MinMax implements SizeSelector {
        private final int min;
        private final int max;

        @Override
        public Result<? extends Seed, Integer> checkedApply(Seed input) {
            return BuildingBlocks.nextIntBetween(min, max, input);
        }
    }

    @Value
    @AllArgsConstructor(access = AccessLevel.PRIVATE)
    public static class MinPreferred implements SizeSelector {
        private final int min;
        private final int preferred;

        @Override
        public Result<? extends Seed, Integer> checkedApply(Seed input) {
            Result<Seed, Boolean> s1 = shouldUsePreferred(input);
            if (s1.getValue()) {
                return result(s1.getNextState(), preferred);
            } else {
                return BuildingBlocks.nextIntBetween(min, min + DEFAULT_RANGE, s1.getNextState());
            }
        }
    }

    @Value
    @AllArgsConstructor(access = AccessLevel.PRIVATE)
    public static class MinOnly implements SizeSelector {
        private final int min;

        @Override
        public Result<? extends Seed, Integer> checkedApply(Seed input) {
            return BuildingBlocks.nextIntBetween(min, min + DEFAULT_RANGE, input);
        }
    }

    @Value
    @AllArgsConstructor(access = AccessLevel.PRIVATE)
    public static class MaxPreferred implements SizeSelector {
        private final int max;
        private final int preferred;

        @Override
        public Result<? extends Seed, Integer> checkedApply(Seed input) {
            Result<Seed, Boolean> s1 = shouldUsePreferred(input);
            if (s1.getValue()) {
                return result(s1.getNextState(), preferred);
            } else {
                return BuildingBlocks.nextIntBounded(max, s1.getNextState());
            }
        }
    }

    @Value
    @AllArgsConstructor(access = AccessLevel.PRIVATE)
    public static class MaxOnly implements SizeSelector {
        private final int max;

        @Override
        public Result<? extends Seed, Integer> checkedApply(Seed input) {
            return BuildingBlocks.nextIntBounded(max, input);
        }
    }

    @Value
    @AllArgsConstructor(access = AccessLevel.PRIVATE)
    public static class PreferredOnly implements SizeSelector {
        private final int preferred;

        @Override
        public Result<? extends Seed, Integer> checkedApply(Seed input) {
            return result(input, preferred);
        }
    }

    @Value
    @AllArgsConstructor(access = AccessLevel.PRIVATE)
    public static class NoSizeParameters implements SizeSelector {
        private static final NoSizeParameters INSTANCE = new NoSizeParameters();

        @Override
        public Result<? extends Seed, Integer> checkedApply(Seed input) {
            return BuildingBlocks.nextIntBounded(DEFAULT_RANGE, input);
        }
    }

    private static Result<Seed, Boolean> shouldUsePreferred(Seed input) {
        Result<? extends Seed, Integer> s1 = BuildingBlocks.nextIntBounded(7, input);
        return result(s1.getNextState(), s1.getValue() < 2);
    }

    public static MinMaxPreferred minMaxPreferredSizeSelector(int min, int max, int preferred) {
        return new MinMaxPreferred(min, max, preferred);
    }

    public static MinMax minMaxSizeSelector(int min, int max) {
        min = Math.max(0, min);
        max = Math.max(max, min);
        return new MinMax(min, max);
    }

    public static MinPreferred minPreferredSizeSelector(int min, int preferred) {
        min = Math.max(0, min);
        preferred = Math.max(0, preferred);
        return new MinPreferred(min, preferred);
    }

    public static MinOnly minOnlySizeSelector(int min) {
        min = Math.max(0, min);
        return new MinOnly(min);
    }

    public static MaxPreferred maxPreferredSizeSelector(int max, int preferred) {
        max = Math.max(0, max);
        preferred = Math.max(0, preferred);
        return new MaxPreferred(max, preferred);
    }

    public static MaxOnly maxOnlySizeSelector(int max) {
        max = Math.max(0, max);
        return new MaxOnly(max);
    }

    public static PreferredOnly preferredOnlySizeSelector(int preferred) {
        preferred = Math.max(0, preferred);
        return new PreferredOnly(preferred);
    }

    public static NoSizeParameters noSizeParametersSizeSelector() {
        return NoSizeParameters.INSTANCE;
    }

    public static SizeSelector sizeSelector(SizeParameters sp) {
        return sp.getMinSize()
                .match(_a -> sp.getMaxSize()
                                .match(_b -> sp.getPreferredSize()
                                                .match(_c -> noSizeParametersSizeSelector(),
                                                        SizeSelectors::preferredOnlySizeSelector),
                                        maxSize -> sp.getPreferredSize()
                                                .match(_d -> maxOnlySizeSelector(maxSize),
                                                        preferred -> maxPreferredSizeSelector(maxSize, preferred))),
                        minSize -> sp.getMaxSize()
                                .match(_e -> sp.getPreferredSize()
                                                .match(_f -> minOnlySizeSelector(minSize),
                                                        preferred -> minPreferredSizeSelector(minSize, preferred)),
                                        maxSize -> sp.getPreferredSize()
                                                .match(_g -> minMaxSizeSelector(minSize, maxSize),
                                                        preferred -> minMaxPreferredSizeSelector(minSize, maxSize, preferred))
                                ));
    }

}