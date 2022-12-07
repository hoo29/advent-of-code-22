package Days;

import Util.Util;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Stack;

public class D7 {

    public D7() throws IOException {

        List<String> file = Util.readInput(7, false);

        int i = 1;
        String op;
        Node root = new Node(null, true, "", 0);
        Node current = root;
        while (i < file.size()) {

            String line = file.get(i);
            if (!"$".equals(line.split(" ")[0])) {
                throw new RuntimeException("Invalid state");
            }

            String command = line.split(" ")[1];
            if ("cd".equals(command)) {
                op = "CD";
            } else if ("ls".equals(command)) {
                op = "LS";
            } else {
                throw new RuntimeException("Unknown command " + command);
            }

            if ("CD".equals(op)) {
                String dir = line.split(" ")[2];
                if ("..".equals(dir)) {
                    current = current.parent;
                } else {
                    current = current.children.get(dir);
                    if (current == null) {
                        throw new Error("Could not get dir " + dir);
                    }
                }
                ++i;
            } else if ("LS".equals(op)) {
                ++i;
                while (i < file.size()) {
                    line = file.get(i);
                    if ("$".equals(line.split("")[0])) {
                        break;
                    }
                    String sizeOrDir = line.split(" ")[0];
                    String name = line.split(" ")[1];
                    if ("dir".equals(sizeOrDir)) {
                        current.children.put(name, new Node(current, true, name, 0));
                    } else {
                        int size = Integer.parseInt(sizeOrDir);
                        Node child = new Node(current, false, name, size);
                        current.children.put(name, child);
                    }
                    ++i;
                }
            } else {
                throw new RuntimeException("Unknown op " + op);
            }
        }

        System.out.println(root.size);

        Stack<Node> nodes = new Stack<>();
        List<Node> bigDirs = new ArrayList<>();
        nodes.push(root);
        while (nodes.size() > 0) {
            Node cur = nodes.pop();
            if (cur.parent != null && cur.size <= 100000) {
                bigDirs.add(cur);
            }

            for (Node child : cur.children.values()) {
                if (child.dir) {
                    nodes.push(child);
                }
            }

        }

        int sum = 0;
        for (Node n : bigDirs) {
            sum += n.size;
        }
        System.out.println(sum);

        nodes.clear();
        bigDirs.clear();
        nodes.push(root);
        int target = 30000000 - (70000000 - root.size);
        Node toDelete = root;
        while (nodes.size() > 0) {
            Node cur = nodes.pop();
            if (cur.size >= target && cur.size <= toDelete.size) {
                toDelete = cur;
            }

            for (Node child : cur.children.values()) {
                if (child.dir) {
                    nodes.push(child);
                }
            }

        }
        System.out.println(toDelete.size);
    }

    private static class Node {
        final String name;

        Node parent;
        Map<String, Node> children = new HashMap<>();

        int size;
        boolean dir;

        public Node(Node parent, boolean dir, String name, int size) {
            this.parent = parent;
            this.dir = dir;
            this.name = name;
            this.size = size;
            if (size != 0) {
                this.parent.addSize(size);
            }
        }

        public void addSize(int size) {
            this.size += size;
            if (this.parent != null) {
                this.parent.addSize(size);
            }
        }

        @Override
        public int hashCode() {
            return this.name.hashCode();
        }
    }
}
