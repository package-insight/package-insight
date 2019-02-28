package io.github.westonal.analysis.graph.scc

class SetComparator<T> implements Comparator<Set<T>> {

    private Comparator<T> comparator

    SetComparator(Comparator<T> comparator) {
        this.comparator = comparator
    }

    @Override
    int compare(Set<T> set1, Set<T> set2) {
        return comparator.compare(min(set1), min(set2))
    }

    private T min(Iterable<T> set) {
        return set.min(comparator)
    }
}
