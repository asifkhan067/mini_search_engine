package com.mycompany.searchengine;

import java.util.ArrayList;
import java.util.Hashtable;

class WordSet {

    String suffix;
    ArrayList<Pair> list;

    WordSet(String suffix) {
        this.suffix = suffix;
    }

    WordSet(String suffix, String address, int count) {
        this.suffix = suffix;
        list = new ArrayList<>();
        add(address, count);
    }

    void add(String address, int count) {
        list.add(new Pair(address, count));
    }
}

class Pair {

    String address;
    int count;

    Pair(String address, int count) {
        this.address = address;
        this.count = count;
    }

    public int getCount() {
        return count;
    }

    public String getAddress() {
        return address;
    }
}

public class Result {

    String prefix;
    Hashtable<String, WordSet> hashtable;

    public Result(String prefix) {
        hashtable = new Hashtable<>();
        this.prefix = prefix;
    }

    public void addWord(String suffix, int count, String address) {
        if (hashtable.containsKey(suffix)) {
            WordSet wordSet = hashtable.get(suffix);
            wordSet.add(address, count);
            hashtable.put(suffix, wordSet);
        } else {
            hashtable.put(suffix, new WordSet(suffix, address, count));
        }
    }
}
