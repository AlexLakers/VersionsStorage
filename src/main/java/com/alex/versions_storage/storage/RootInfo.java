package com.alex.versions_storage.storage;

import com.alex.versions_storage.utill.JsonUtill;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.ParseException;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.*;

/**
 * It describes the part of versions storage - rootInfo.json.
 * @see 'com.googlecode.json-simple'
 */
public class RootInfo {
    private Path rootPath;
    private List<Node> nodes;

    /**
     * It contains all the necessary properties that are used in the version storage(rootInfo.json).
     */
    public static class ConstProps {
        private static final String VERSION = "version";
        private static final String ROOT_PATH = "rootPath";
        private static final String VERSIONS_INFO = "versionsInfo";
        private static final String HASH_CODE = "hashCode";
    }

    /**
     * Creates a new RootInfo with path to selected root directory.It path will be used for the root path property.
     *
     * @param rootPath value of root path property.
     */
    public RootInfo(Path rootPath) {
        this.rootPath = rootPath;
        nodes = new ArrayList<>();

    }

    /**
     * It describes one of nested nodes in the version storage(rootInfo.json).
     * Every node includes version value and hash code value.
     */
    private class Node {
        private int hashCode;
        private int version;

        /**
         * Creates a new Node with {@link Node#version version} and {@link Node#hashCode hashCode}.
         *
         * @param version  value for version property
         * @param hashCode value for hash code property.
         */
        public Node(int version, int hashCode) {
            this.hashCode = hashCode;
            this.version = version;
        }

        /**
         * Returns a string in the format .JSON.
         * It includes the fallowing properties:{@link Node#version Version},{@link Node#hashCode HashCode}.
         *
         * @return string(.JSON).
         */
        public String toString() {
            return String.format("{\"hashCode\":%1$d,\"version\":%2$d}", hashCode, version);
        }

    }

    /**
     * Returns a string in the format .JSON.
     * It includes the fallowing properties:{@link Node#version Version},{@link Node#hashCode HashCode},{@link RootInfo#rootPath RootPath}.
     *
     * @return string(.JSON).
     */
    public String toJSONString() {
        return String.format("{\"rootPath\":\"%1$s\",\"versionsInfo\":%2$s}", rootPath.toString(), nodes.toString());
    }

    public List<Node> getNodes() {
        return Collections.unmodifiableList(nodes);
    }

    /**
     * Returns the latest version of changes selected root directory.
     *
     * @return - last version.
     */
    public int getLastVersionDir() {
        return nodes.get(nodes.size() - 1).version;
    }

    /**
     * Performs the adding a new Node with the version value, and the hash code value.
     * The selected root directory contains these values.
     * Adding occurs to list of{@link RootInfo#nodes nodes}.
     *
     * @param version  -version value of selected directory.
     * @param hashCode - hash code value.
     */
    public void addNode(int version, int hashCode) {
        if (version <= 0) throw new IllegalArgumentException("You try to add a negative version");
        nodes.add(new Node(version, hashCode));
    }

    /**
     * Returns the founded version of changes selected root directory with using hash code.
     * of selected root directory.
     *
     * @param hc hash code value.
     * @return version value or -1 if hash code value is not found.
     */
    public int findVersionByHash(int hc) {
        for (var node : nodes) {
            if (node.hashCode == hc) return node.version;
        }
        return -1;
    }

    /**
     * Returns the boolean result of search version value in the list{@link RootInfo#nodes nodes}.
     *
     * @param vers version value.
     * @return true if version exists,else - false.
     */
    public boolean isVersionExist(int vers) {
        for (Node node : nodes) {
            if (node.version == vers) {
                return true;
            }
        }
        return false;
    }

    /**
     * Performs the saving(writing) all the necessary data:{@link RootInfo#nodes Nodes},{@link RootInfo#rootPath RootPath}
     * about selected root directory to version storage(rootInfo.json).
     *
     * @param path path to version storage(rootInfo.json).
     * @throws IOException if path to version storage(database.dat) is not found or other IO errors have been detected.
     */
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

    /**
     * Returns new RootInfo from version storage(rootInfo.json) with path to it.
     * Parsing process occurs with the algorithm that's declared in {@link RootInfo#parseByJSON(JSONObject) ParseByJSON}.
     *
     * @param path to version storage(rootInfo.json).
     * @return new RootInfo from rootInfo.json.
     * @throws IOException    if path to version storage(database.dat) is not found or other IO errors have been detected.
     * @throws ParseException if versions storage(rootInfo.json) has incorrect json-structure which impossible for parsing.
     * @see 'com.googlecode.json-simple'
     */
    public static RootInfo loadFromFile(Path path) throws IOException, ParseException {
        JSONObject jRoot = null;
        try (Reader reader = Files.newBufferedReader(path)) {
            jRoot = JsonUtill.load(reader);
        }
        return new RootInfo(path.getParent()).parseByJSON(jRoot);

    }
}