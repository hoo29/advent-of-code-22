package Days;

import Util.Util;

import java.io.IOException;
import java.util.*;
import java.util.concurrent.ExecutionException;

public class D20 {

    public D20() throws IOException, InterruptedException, ExecutionException {

        List<String> file = Util.readInput(20, false);
        List<Item> seq = new LinkedList<>();
        boolean p2 = true;

        for (int i = 0; i < file.size(); ++i) {
            seq.add(new Item(file.get(i), i, p2));
        }

        for (int mix = 0; mix < (p2 ? 10 : 1); ++mix) {
            int ind = 0;
            while (ind < file.size()) {
                int itemInd = seq.indexOf(new Item("-1", ind, p2));

                Item item = seq.remove(itemInd);

                Item newItem = new Item(item);
                int destInd = Math.floorMod(newItem.val + itemInd, seq.size());

                seq.add(destInd, newItem);

                ++ind;
            }
        }

        int zInd = -1;
        for (int i = 0; i < file.size(); ++i) {
            if (seq.get(i).val == 0) {
                zInd = i;
                break;
            }
        }

        System.out.println(seq.get((zInd + 1000) % file.size()).val + seq.get((zInd + 2000) % file.size()).val
                + seq.get((zInd + 3000) % file.size()).val);

    }

    private static class Item {
        final long val;
        final int index;

        public Item(String val, int index, boolean p2) {
            if (p2) {
                this.val = Integer.parseInt(val) * 811589153L;
            } else {
                this.val = Integer.parseInt(val);
            }
            this.index = index;
        }

        public Item(Item other) {
            this.val = other.val;
            this.index = other.index;
        }

        @Override
        public String toString() {
            return Long.toString(val);
        }

        @Override
        public int hashCode() {
            final int prime = 31;
            int result = 1;
            result = prime * result + index;
            return result;
        }

        @Override
        public boolean equals(Object obj) {
            if (this == obj)
                return true;
            if (obj == null)
                return false;
            if (getClass() != obj.getClass())
                return false;
            Item other = (Item) obj;
            if (index != other.index)
                return false;
            return true;
        }

    }
}
