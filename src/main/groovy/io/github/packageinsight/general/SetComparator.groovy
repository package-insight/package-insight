package io.github.packageinsight.general

class SetComparator<T> implements Comparator<Set<T>> {

    private final Comparator<T> comparator

    SetComparator(Comparator<T> comparator) {
        this.comparator = comparator
    }

    @Override
    int compare(Set<T> set1, Set<T> set2) {
        comparator.compare(min(set1), min(set2))
    }

    private T min(Iterable<T> set) {
        set.min(comparator)
    }
}
