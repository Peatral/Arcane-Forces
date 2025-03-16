package xyz.peatral.arcaneforces.content.shrines.network;

import com.mojang.datafixers.util.Pair;

import java.util.*;
import java.util.function.BiFunction;
import java.util.function.BiPredicate;
import java.util.function.Predicate;

public class Graph<T> {
    private Map<T, Set<T>> adjacency = new HashMap<>();
    private BiFunction<T, T, Double> distanceFunction;

    public Graph(BiFunction<T, T, Double> nodeDistanceFunction) {
        distanceFunction = nodeDistanceFunction;
    }

    public Graph(Map<T, Set<T>> adjacency, BiFunction<T, T, Double> distanceFunction) {
        this.adjacency = adjacency;
        this.distanceFunction = distanceFunction;
    }


    public boolean add(T node) {
        if (adjacency.containsKey(node)) {
            return false;
        }
        adjacency.put(node, new HashSet<>());
        return true;
    }

    public boolean upsertNeighbors(T node, BiPredicate<T, T> neighborPredicate) {
        boolean modified = add(node);
        for (T otherNode : adjacency.keySet()) {
            if (node.equals(otherNode)) {
                continue;
            }
            if (!neighborPredicate.test(node, otherNode)) {
                continue;
            }
            adjacency.computeIfAbsent(node, k -> new HashSet<>()).add(otherNode);
            adjacency.computeIfAbsent(otherNode, k -> new HashSet<>()).add(node);
        }
        return modified;
    }

    public Set<T> getNeighbors(T node) {
        return adjacency.get(node);
    }

    public boolean remove(T node) {
        Set<T> neighbors = adjacency.remove(node);
        if (neighbors == null) {
            return false;
        }
        // For the case this is directed we can't rely on the neighbors
        for (T otherNode : adjacency.keySet()) {
            adjacency.computeIfAbsent(otherNode, k -> new HashSet<>()).remove(node);
        }
        return true;
    }

    public Set<Pair<T, Integer>> getDepths(T node) {
        if (!adjacency.containsKey(node)) {
            return Set.of();
        }
        Set<Pair<T, Integer>> visitedNodes = new HashSet<>();
        Queue<Pair<T, Integer>> queue = new LinkedList<>();
        queue.add(Pair.of(node, 0));
        while (!queue.isEmpty()) {
            Pair<T, Integer> current = queue.remove();
            if (visitedNodes.stream().anyMatch(pair -> pair.getFirst().equals(current.getFirst()))) {
                continue;
            }
            visitedNodes.add(current);
            Set<T> neighborhood = adjacency.getOrDefault(current.getFirst(), Set.of());
            for (T neighbor : neighborhood) {
                if (queue.stream().anyMatch(visitedNode -> visitedNode.getFirst().equals(neighbor))) {
                    continue;
                }
                queue.add(Pair.of(neighbor, current.getSecond() + 1));
            }
        }
        return visitedNodes;
    }

    public T getClosestNode(T start, Predicate<T> filter) {
        HashMap<T, Double> dist = new HashMap<>();
        HashMap<T, T> prev = new HashMap<>();

        Set<T> queue = new HashSet<>();

        for (T node : adjacency.keySet()) {
            dist.put(node, Double.POSITIVE_INFINITY);
            queue.add(node);
        }
        dist.put(start, 0.0);

        while (!queue.isEmpty()) {
            T node = queue.stream().min(Comparator.comparing(dist::get)).get();
            queue.remove(node);

            for (T neighbor : adjacency.getOrDefault(node, Set.of())) {
                if (!queue.contains(neighbor)) {
                    continue;
                }
                double distance = dist.get(node) + distanceFunction.apply(node, neighbor);
                if (distance < dist.get(neighbor)) {
                    dist.put(neighbor, distance);
                    prev.put(neighbor, node);
                }
            }
        }
        return adjacency.keySet().stream().filter(t -> !t.equals(start) && filter.test(t)).min(Comparator.comparing(dist::get)).orElse(null);
    }

    public Map<T, Set<T>> getAdjacency() {
        return adjacency;
    }

    public Set<T> getNodes() {
        return adjacency.keySet();
    }
}

