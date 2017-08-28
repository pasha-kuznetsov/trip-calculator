package com.tydbits.TripCalculator.dto;

import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.function.Function;
import java.util.function.IntFunction;
import java.util.stream.Collectors;

public class DtoUtils {
    public static <From, To> List<To> mapToList(From[] array, Function<From, To> map) {
        return Arrays.stream(array).map(map).collect(Collectors.toList());
    }

    public static <From, To> List<To> mapToList(Collection<From> collection, Function<From, To> map) {
        return collection.stream().map(map).collect(Collectors.toList());
    }

    public static <From, To> To[] mapToArray(Collection<From> collection, Function<From, To> map, IntFunction<To[]> generator) {
        return collection.stream().map(map).toArray(generator);
    }
}
