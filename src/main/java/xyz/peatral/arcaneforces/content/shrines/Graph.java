package xyz.peatral.arcaneforces.content.shrines;

import com.mojang.datafixers.util.Pair;

import java.util.*;
import java.util.function.BiPredicate;

public class Graph<T> {
    private Set<T> nodes = new HashSet<>();
    private Map<T, Set<T>> adjacency = new HashMap<>();

    public Graph() {
    }

    public Graph(Set<T> nodes, Map<T, Set<T>> adjacency) {
        this.nodes = nodes;
        this.adjacency = adjacency;
    }

    public boolean addNode(T node) {
        return nodes.add(node);
    }

    public boolean addAndUpdateNeighbors(T node, BiPredicate<T, T> neighborPredicate) {
        boolean modified = nodes.add(node);
        //if (!modified) {
        //    return false;
        //}
        for (T otherNode : nodes) {
            if (node.equals(otherNode)) {
                continue;
            }
            if (!neighborPredicate.test(node, otherNode)) {
                continue;
            }
            adjacency.computeIfAbsent(node, k -> new HashSet<>()).add(otherNode);
            adjacency.computeIfAbsent(otherNode, k -> new HashSet<>()).add(node);
        }
        return true;
    }

    public boolean setNeighbors(T node, Set<T> neighbors) {
        if (!nodes.contains(node)) {
            return false;
        }
        adjacency.put(node, neighbors);
        return true;
    }

    public Set<T> getNeighbors(T node) {
        return adjacency.get(node);
    }

    public boolean remove(T node) {
        boolean modified = nodes.remove(node);
        if (!modified) {
            return false;
        }
        adjacency.remove(node);
        for (T otherNode : nodes) {
            adjacency.computeIfAbsent(otherNode, k -> new HashSet<>()).remove(node);
        }
        return true;
    }

    public Set<Pair<T, Integer>> getGraph(T node) {
        if (!nodes.contains(node)) {
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

    public Set<T> getAllNodes() {
        return nodes;
    }

    public Map<T, Set<T>> getAdjacency() {
        return adjacency;
    }

    public Set<T> getNodes() {
        return nodes;
    }
}

