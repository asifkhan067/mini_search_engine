package com.mycompany.searchengine;

import java.lang.*;
import java.util.*;

public class bplustree {

    int m;
    internal_node root;
    leaf_node first_leaf;

    private int binary_search(DictionaryPair[] dps, int num_pairs, int t) {
        Comparator<DictionaryPair> c = new Comparator<DictionaryPair>() {
            @Override
            public int compare(DictionaryPair o1, DictionaryPair o2) {
                Integer a = o1.key;
                Integer b = o2.key;
                return a.compareTo(b);
            }
        };
        return Arrays.binary_search(dps, 0, num_pairs, new DictionaryPair(t, null), c);
    }

    private leaf_node find_leaf_node(int key) {
        Integer[] keys = this.root.keys;
        int i;

        for (i = 0; i < this.root.degree - 1; i++) {
            if (key < keys[i]) {
                break;
            }
        }
        Node child = this.root.child_pointers[i];
        if (child instanceof leaf_node) {
            return (leaf_node) child;
        } else {
            return find_leaf_node((internal_node) child, key);
        }
    }

    private leaf_node find_leaf_node(internal_node node, int key) {

        Integer[] keys = node.keys;
        int i;
        for (i = 0; i < node.degree - 1; i++) {
            if (key < keys[i]) {
                break;
            }
        }
        Node child_node = node.child_pointers[i];
        if (child_node instanceof leaf_node) {
            return (leaf_node) child_node;
        } else {
            return find_leaf_node((internal_node) node.child_pointers[i], key);
        }
    }

    private int find_index_of_pointer(Node[] pointers, leaf_node node) {
        int i;
        for (i = 0; i < pointers.length; i++) {
            if (pointers[i] == node) {
                break;
            }
        }
        return i;
    }

    private int get_mid_point() {
        return (int) Math.ceil((this.m + 1) / 2.0) - 1;
    }

    private void handle_deficiency(internal_node in) {

        internal_node sibling;
        internal_node parent = in.parent;
        if (this.root == in) {
            for (int i = 0; i < in.child_pointers.length; i++) {
                if (in.child_pointers[i] != null) {
                    if (in.child_pointers[i] instanceof internal_node) {
                        this.root = (internal_node) in.child_pointers[i];
                        this.root.parent = null;
                    } else if (in.child_pointers[i] instanceof leaf_node) {
                        this.root = null;
                    }
                }
            }
        } else if (in.left_sibling != null && in.left_sibling.is_landable()) {
            sibling = in.left_sibling;
        } else if (in.right_sibling != null && in.right_sibling.is_landable()) {
            sibling = in.right_sibling;
            int borrowedKey = sibling.keys[0];
            Node pointer = sibling.child_pointers[0];
            in.keys[in.degree - 1] = parent.keys[0];
            in.child_pointers[in.degree] = pointer;
            parent.keys[0] = borrowedKey;
            sibling.remove_pointer(0);
            Arrays.sort(sibling.keys);
            sibling.remove_pointer(0);
            shift_down(in.child_pointers, 1);
        } else if (in.left_sibling != null && in.left_sibling.is_mergeable()) {

        } else if (in.right_sibling != null && in.right_sibling.is_mergeable()) {
            sibling = in.right_sibling;
            sibling.keys[sibling.degree - 1] = parent.keys[parent.degree - 2];
            Arrays.sort(sibling.keys, 0, sibling.degree);
            parent.keys[parent.degree - 2] = null;

            for (int i = 0; i < in.child_pointers.length; i++) {
                if (in.child_pointers[i] != null) {
                    sibling.prepend_child_pointer(in.child_pointers[i]);
                    in.child_pointers[i].parent = sibling;
                    in.remove_pointer(i);
                }
            }

            parent.remove_pointer(in);

            sibling.left_sibling = in.left_sibling;
        }

        if (parent != null && parent.is_deficient()) {
            handle_deficiency(parent);
        }
    }

    private boolean is_empty() {
        return first_leaf == null;
    }

    private int linear_null_search(DictionaryPair[] dps) {
        for (int i = 0; i < dps.length; i++) {
            if (dps[i] == null) {
                return i;
            }
        }
        return -1;
    }

    private int linear_null_search(Node[] pointers) {
        for (int i = 0; i < pointers.length; i++) {
            if (pointers[i] == null) {
                return i;
            }
        }
        return -1;
    }

    private void shift_down(Node[] pointers, int amount) {
        Node[] newPointers = new Node[this.m + 1];
        for (int i = amount; i < pointers.length; i++) {
            newPointers[i - amount] = pointers[i];
        }
        pointers = newPointers;
    }

    private void sort_dictionary(DictionaryPair[] dictionary) {
        Arrays.sort(dictionary, new Comparator<DictionaryPair>() {
            @Override
            public int compare(DictionaryPair o1, DictionaryPair o2) {
                if (o1 == null && o2 == null) {
                    return 0;
                }
                if (o1 == null) {
                    return 1;
                }
                if (o2 == null) {
                    return -1;
                }
                return o1.compareTo(o2);
            }
        });
    }

    private Node[] split_child_pointer(internal_node in, int split) {

        Node[] pointers = in.child_pointers;
        Node[] halfPointers = new Node[this.m + 1];

        for (int i = split + 1; i < pointers.length; i++) {
            halfPointers[i - split - 1] = pointers[i];
            in.remove_pointer(i);
        }

        return halfPointers;
    }

    private DictionaryPair[] split_dictionary(leaf_node ln, int split) {

        DictionaryPair[] dictionary = ln.dictionary;

        DictionaryPair[] halfDict = new DictionaryPair[this.m];

        for (int i = split; i < dictionary.length; i++) {
            halfDict[i - split] = dictionary[i];
            ln.delete(i);
        }

        return halfDict;
    }

    private void split_internal_node(internal_node in) {

        internal_node parent = in.parent;

        int midpoint = get_mid_point();
        int newParentKey = in.keys[midpoint];
        Integer[] halfKeys = split_keys(in.keys, midpoint);
        Node[] halfPointers = split_child_pointer(in, midpoint);

        in.degree = linear_null_search(in.child_pointers);

        internal_node sibling = new internal_node(this.m, halfKeys, halfPointers);
        for (Node pointer : halfPointers) {
            if (pointer != null) {
                pointer.parent = sibling;
            }
        }

        sibling.right_sibling = in.right_sibling;
        if (sibling.right_sibling != null) {
            sibling.right_sibling.left_sibling = sibling;
        }
        in.right_sibling = sibling;
        sibling.left_sibling = in;

        if (parent == null) {

            Integer[] keys = new Integer[this.m];
            keys[0] = newParentKey;
            internal_node newRoot = new internal_node(this.m, keys);
            newRoot.append_child_pointer(in);
            newRoot.append_child_pointer(sibling);
            this.root = newRoot;

            in.parent = newRoot;
            sibling.parent = newRoot;

        } else {

            parent.keys[parent.degree - 1] = newParentKey;
            Arrays.sort(parent.keys, 0, parent.degree);

            int pointerIndex = parent.find_index_of_pointer(in) + 1;
            parent.insert_child_pointer(sibling, pointerIndex);
            sibling.parent = parent;
        }
    }

    private Integer[] split_keys(Integer[] keys, int split) {

        Integer[] halfKeys = new Integer[this.m];

        keys[split] = null;

        for (int i = split + 1; i < keys.length; i++) {
            halfKeys[i - split - 1] = keys[i];
            keys[i] = null;
        }

        return halfKeys;
    }

    public void insert(int key, Result value) {
        if (is_empty()) {

            leaf_node ln = new leaf_node(this.m, new DictionaryPair(key, value));

            this.first_leaf = ln;

        } else {

            leaf_node ln = (this.root == null) ? this.first_leaf : find_leaf_node(key);

            if (!ln.insert(new DictionaryPair(key, value))) {

                ln.dictionary[ln.num_pairs] = new DictionaryPair(key, value);
                ln.num_pairs++;
                sort_dictionary(ln.dictionary);

                int midpoint = get_mid_point();
                DictionaryPair[] halfDict = split_dictionary(ln, midpoint);

                if (ln.parent == null) {

                    Integer[] parent_keys = new Integer[this.m];
                    parent_keys[0] = halfDict[0].key;
                    internal_node parent = new internal_node(this.m, parent_keys);
                    ln.parent = parent;
                    parent.append_child_pointer(ln);

                } else {

                    int newParentKey = halfDict[0].key;
                    ln.parent.keys[ln.parent.degree - 1] = newParentKey;
                    Arrays.sort(ln.parent.keys, 0, ln.parent.degree);
                }

                leaf_node newLeafNode = new leaf_node(this.m, halfDict, ln.parent);

                int pointerIndex = ln.parent.find_index_of_pointer(ln) + 1;
                ln.parent.insert_child_pointer(newLeafNode, pointerIndex);

                newLeafNode.right_sibling = ln.right_sibling;
                if (newLeafNode.right_sibling != null) {
                    newLeafNode.right_sibling.left_sibling = newLeafNode;
                }
                ln.right_sibling = newLeafNode;
                newLeafNode.left_sibling = ln;

                if (this.root == null) {

                    this.root = ln.parent;

                } else {

                    internal_node in = ln.parent;
                    while (in != null) {
                        if (in.is_overfull()) {
                            split_internal_node(in);
                        } else {
                            break;
                        }
                        in = in.parent;
                    }
                }
            }
        }
    }

    public Result search(int key) {

        if (is_empty()) {
            return null;
        }

        leaf_node ln = (this.root == null) ? this.first_leaf : find_leaf_node(key);

        DictionaryPair[] dps = ln.dictionary;
        int index = binary_search(dps, ln.num_pairs, key);

        if (index < 0) {
            return null;
        } else {
            return dps[index].value;
        }
    }

    public bplustree(int m) {
        this.m = m;
        this.root = null;
    }

    public class Node {

        internal_node parent;
    }

    private class internal_node extends Node {

        int max_degree;
        int min_degree;
        int degree;
        internal_node left_sibling;
        internal_node right_sibling;
        Integer[] keys;
        Node[] child_pointers;

        private void append_child_pointer(Node pointer) {
            this.child_pointers[degree] = pointer;
            this.degree++;
        }

        private int find_index_of_pointer(Node pointer) {
            for (int i = 0; i < child_pointers.length; i++) {
                if (child_pointers[i] == pointer) {
                    return i;
                }
            }
            return -1;
        }

        private void insert_child_pointer(Node pointer, int index) {
            for (int i = degree - 1; i >= index; i--) {
                child_pointers[i + 1] = child_pointers[i];
            }
            this.child_pointers[index] = pointer;
            this.degree++;
        }

        private boolean is_deficient() {
            return this.degree < this.min_degree;
        }

        private boolean is_landable() {
            return this.degree > this.min_degree;
        }

        private boolean is_mergeable() {
            return this.degree == this.min_degree;
        }

        private boolean is_overfull() {
            return this.degree == max_degree + 1;
        }

        private void prepend_child_pointer(Node pointer) {
            for (int i = degree - 1; i >= 0; i--) {
                child_pointers[i + 1] = child_pointers[i];
            }
            this.child_pointers[0] = pointer;
            this.degree++;
        }

        private void remove_key(int index) {
            this.keys[index] = null;
        }

        private void remove_pointer(int index) {
            this.child_pointers[index] = null;
            this.degree--;
        }

        private void remove_pointer(Node pointer) {
            for (int i = 0; i < child_pointers.length; i++) {
                if (child_pointers[i] == pointer) {
                    this.child_pointers[i] = null;
                }
            }
            this.degree--;
        }

        private internal_node(int m, Integer[] keys) {
            this.max_degree = m;
            this.min_degree = (int) Math.ceil(m / 2.0);
            this.degree = 0;
            this.keys = keys;
            this.child_pointers = new Node[this.max_degree + 1];
        }

        private internal_node(int m, Integer[] keys, Node[] pointers) {
            this.max_degree = m;
            this.min_degree = (int) Math.ceil(m / 2.0);
            this.degree = linear_null_search(pointers);
            this.keys = keys;
            this.child_pointers = pointers;
        }
    }

    public class leaf_node extends Node {

        int max_num_pairs;
        int min_num_pairs;
        int num_pairs;
        leaf_node left_sibling;
        leaf_node right_sibling;
        DictionaryPair[] dictionary;

        public void delete(int index) {

            this.dictionary[index] = null;

            num_pairs--;
        }

        public boolean insert(DictionaryPair dp) {
            if (this.is_full()) {

                return false;
            } else {

                this.dictionary[num_pairs] = dp;
                num_pairs++;
                Arrays.sort(this.dictionary, 0, num_pairs);

                return true;
            }
        }

        public boolean is_deficient() {
            return num_pairs < min_num_pairs;
        }

        public boolean is_full() {
            return num_pairs == max_num_pairs;
        }

        public boolean is_landable() {
            return num_pairs > min_num_pairs;
        }

        public boolean is_mergeable() {
            return num_pairs == min_num_pairs;
        }

        public leaf_node(int m, DictionaryPair dp) {
            this.max_num_pairs = m - 1;
            this.min_num_pairs = (int) (Math.ceil(m / 2) - 1);
            this.dictionary = new DictionaryPair[m];
            this.num_pairs = 0;
            this.insert(dp);
        }

        public leaf_node(int m, DictionaryPair[] dps, internal_node parent) {
            this.max_num_pairs = m - 1;
            this.min_num_pairs = (int) (Math.ceil(m / 2) - 1);
            this.dictionary = dps;
            this.num_pairs = linear_null_search(dps);
            this.parent = parent;
        }
    }

    public class DictionaryPair implements Comparable<DictionaryPair> {

        int key;
        Result value;

        public DictionaryPair(int key, Result value) {
            this.key = key;
            this.value = value;
        }

        public int compareTo(DictionaryPair o) {
            if (key == o.key) {
                return 0;
            } else if (key > o.key) {
                return 1;
            } else {
                return -1;
            }
        }
    }
}
