package com.alex.versions_storage.storage;

import com.alex.versions_storage.utill.JsonUtill;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.ParseException;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.*;

public class RootInfo{
    private Path rootPath;
    private List<Node> nodes;

    public static class ConstProps {
        private static final String VERSION = "version";
        private static final String ROOT_PATH = "rootPath";
        private static final String VERSIONS_INFO = "versionsInfo";
        private static final String HASH_CODE = "hashCode";
    }

    public RootInfo(Path rootPath) {
        this.rootPath = rootPath;
        nodes = new ArrayList<>();
    }

    private class Node {
        private int hashCode;
        private int version;

        public Node(int version, int hashCode) {
            this.hashCode = hashCode;
            this.version = version;
        }

        public String toString() {
            return String.format("{\"hashCode\":%1$d,\"version\":%2$d}", hashCode, version);
        }
    }

    public String toJSONString() {
        return String.format("{\"rootPath\":\"%1$s\",\"versionsInfo\":%2$s}", rootPath.toString(), nodes.toString());
    }

    public List<Node> getNodes() {
        return Collections.unmodifiableList(nodes);
    }

    public int getLastVersionDir() {
        return nodes.get(nodes.size() - 1).version;
    }

    public void addNode(int version, int hashCode) {
        if (version <= 0) throw new IllegalArgumentException("You try to add a negative version");
        nodes.add(new Node(version, hashCode));
    }

    public int findVersionByHash(int hc) {
        for (var node : nodes) {
            if (node.hashCode == hc) return node.version;
        }
        return -1;
    }

    public boolean isVersionExist(int vers) {
        for (Node node : nodes) {
            if (node.version == vers) {
                return true;
            }
        }
        return false;
    }

    //This method needed to save  the following data: number of version,root path and hashCode to a service JSON-file.
    public void store(Path path) throws IOException {
        try (Writer writer = Files.newBufferedWriter(path)) {
            writer.write(this.toJSONString());
        }
    }

    private RootInfo parseByJSON(JSONObject jRoot) {
        RootInfo rootInfo = null;
        Path rootPath = JsonUtill.getPathProp(jRoot, ConstProps.ROOT_PATH);
        rootInfo = new RootInfo(rootPath);
        JSONArray jNodes = (JSONArray) jRoot.get(ConstProps.VERSIONS_INFO);
        for (Object node : jNodes) {
            var jNode = (JSONObject) node;
            int hash = JsonUtill.getIntProp(jNode, ConstProps.HASH_CODE);
            int version = JsonUtill.getIntProp(jNode, ConstProps.VERSION);
            rootInfo.addNode(version, hash);
        }
        return rootInfo;
    }

    //The method describes a reading data from path(service JSON-file) and create RootInfo-Object  by it.
    public static RootInfo loadFromFile(Path path) throws IOException, ParseException {
        JSONObject jRoot = null;
        try (Reader reader = Files.newBufferedReader(path)) {
            jRoot = JsonUtill.load(reader);
        }
        return new RootInfo(path.getParent()).parseByJSON(jRoot);
    }
}
