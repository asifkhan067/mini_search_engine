package com.mycompany.searchengine;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.*;

public class Main {

    bplustree bpt;

    public int titleToNumber(String columnTitle) {
        int ans = 0, r;
        for (int i = 0; i < columnTitle.length(); i++) {
            r = (int) (columnTitle.charAt(i)) - 64;
            ans = ans * 26;
            ans += r;
        }
        return ans;
    }

    public String convertToTitle(int columnNumber) {
        StringBuilder ans = new StringBuilder();
        char s;
        int r;
        while (columnNumber != 0) {
            r = columnNumber % 26;
            columnNumber /= 26;
            if (r == 0) {
                s = 'Z';
                columnNumber--;
            } else {
                s = (char) (64 + r);
            }
            ans.append(s);
        }
        ans = new StringBuilder(new StringBuffer(ans.toString()).reverse().toString());
        return ans.toString();
    }

    public void preProcessor(String address) throws IOException {
        FileReader fileReader = new FileReader(address);
        BufferedReader bufferedReader = new BufferedReader(fileReader);
        String line;
        Hashtable<String, Integer> hS = new Hashtable<>();
        while (true) {
            line = bufferedReader.readLine();
            if(line!=null && line.contains("farm"))
           {
                System.out.println(line);
            }
            if (line != null) {
                String[] st = line.split("[, ?.@();\"'+]");
                for (int i = 0; i < st.length; i++) {
                    String word = st[i].toUpperCase();
                    if (word.length() > 4) {
                        hS.merge(word, 1, Integer::sum);
                    }
                }
            } else {
                break;
            }
        }
        Set<String> keys = hS.keySet();
        for (String key : keys) {
            String prefix = key.substring(0, 5);
            String suffix = key.substring(5);
            int count = hS.get(key);
            int target = titleToNumber(prefix);
            Result result = bpt.search(target);
            if (result == null) {
                Result res = new Result(prefix);
                res.addWord(suffix, count, address);
                bpt.insert(target, res);
            } else {
                result.addWord(suffix, count, address);
            }
        }
    }

    public Main(bplustree bpt) throws IOException {
        this.bpt = bpt;
        init();

    }

    public void init() throws IOException {
        Files.list(Paths.get("C:\\Users\\askha\\OneDrive\\Desktop\\data")).forEach((directory) -> {
            try {
                preProcessor(directory.toString());
            } catch (IOException e) {
                e.printStackTrace();
            }
        });
    }

    public ArrayList<ArrayList<String>> search(String w) {
        String word = w.toUpperCase();
        ArrayList<String> res = new ArrayList<>();
        ArrayList<String> resRel = new ArrayList<>();
        ArrayList<ArrayList<String>> ans = new ArrayList<>();
        String prefix = word.substring(0, 5);
        String suffix = word.substring(5);
        int key = titleToNumber(prefix);
        Result rs;
        rs = bpt.search(key);
        if (rs == null) {
            return null;
        }
        if (rs.hashtable.containsKey(suffix)) {
            ArrayList<Pair> pairArray = rs.hashtable.get(suffix).list;
            pairArray.sort(Comparator.comparingInt(p -> p.count));
            for (Pair pair : pairArray) {
                res.add(pair.address);
            }
        }
        for (String related : rs.hashtable.keySet()) {
            if (!(prefix + related).equals(word)) {
                resRel.add((prefix + related));
            }
        }
        ans.add(res);
        ans.add(resRel);
        return ans;
    }

    public static void main(String[] args) throws IOException {
        bplustree bpt = new bplustree(4);
        Main obj = new Main(bpt);
        Home home;
        home = new Home();
        home.obj = obj;
        home.setVisible(true);
    }
}
