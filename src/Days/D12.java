package Days;

import Util.Util;

import java.io.IOException;
import java.util.*;

public class D12 {

    public D12() throws IOException {
        List<String> file = Util.readInput(12, false);

        List<Node> unvisited = new ArrayList<>();
        List<List<Node>> nodes = new ArrayList<>();

        Node dest = null;
        for (int y = 0; y < file.size(); ++y) {
            List<Node> row = new ArrayList<>();
            String line = file.get(y);
            for (int x = 0; x < line.length(); ++x) {
                char c = line.charAt(x);
                Node node = new Node(c, x, y);
                row.add(node);
                unvisited.add(node);

                if (c == 'E') {
                    dest = node;
                }

                if (x > 0) {
                    row.get(x - 1).addNeighbour(node);
                    node.addNeighbour(row.get(x - 1));
                }

                if (y > 0) {
                    nodes.get(y - 1).get(x).addNeighbour(node);
                    node.addNeighbour(nodes.get(y - 1).get(x));
                }
            }
            nodes.add(row);
        }

        while (unvisited.size() > 0) {
            unvisited.sort((a, b) -> a.tentDistance - b.tentDistance);
            Node current = unvisited.remove(0);
            if (current.tentDistance == Integer.MAX_VALUE) {
                System.out.println("All nodes visited");
                break;
            }
            current.visit();
        }

        System.out.println(dest.tentDistance);
    }

    private static class Node {

        int tentDistance = Integer.MAX_VALUE;

        List<Node> neighbours = new ArrayList<>();

        int x;
        int y;

        char height;

        public Node(char height, int x, int y) {
            this.height = height;
            this.x = x;
            this.y = y;
            if (height == 'S' || height == 'a') {
                this.height = 'a';
                tentDistance = 0;
            }
            if (height == 'E') {
                this.height = 'z';
            }

        }

        public void addNeighbour(Node node) {
            if (node.height <= height + 1) {
                neighbours.add(node);
            }
        }

        public void visit() {
            neighbours.forEach((n) -> {
                int dist = 1 + tentDistance;
                if (dist < n.tentDistance) {
                    n.tentDistance = dist;
                }
            });
        }

        @Override
        public boolean equals(Object obj) {
            if (obj == null) {
                return false;
            } else if (obj == this) {
                return true;
            } else if (!(obj instanceof Node)) {
                return false;
            } else {
                return this.x == ((Node) obj).x && this.y == ((Node) obj).y;
            }
        }

        @Override
        public int hashCode() {
            return Objects.hash(this.x, this.y);
        }
    }
}
