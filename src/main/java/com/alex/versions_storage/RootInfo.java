package com.alex.versions_storage;

import org.json.simple.JSONObject;

import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

public class RootInfo {
    private JSONObject rootNode;
    private Path rootPath;
    private List<Node> nodes;
    private Path path;

    public RootInfo(Path path, Path rootPath){
        this.path=path;
        this.rootPath =rootPath;
        nodes=new ArrayList<>();

    }

    private class Node{
        private int hashCode;
        private int version;
        Node(int version,int hashCode){
            this.hashCode =hashCode;
            this.version=version;
        }
        public String toString(){
            return String.format("{\"hashCode\":%1$d,\"version\":%2$d}", hashCode,version);
        }
    }
    public Path getPath(){
        return path;
    }
    public String toString(){
        return String.format("{\"rootPath\":\"%1$s\", \"versionsInfo\":%2$s}", rootPath.toString(),nodes.toString());
    }

    public List<Node> getNodes() {
        return Collections.unmodifiableList(nodes);
    }

    public  int getLastVersionDir(){
        return nodes.get(nodes.size()-1).version;
    }
    public JSONObject getRootNode(){
        return rootNode;
    }
    public void setRootNode(JSONObject rootNode){
        if(Objects.isNull(rootNode))
            throw new IllegalArgumentException("rootNode is null");
        this.rootNode=rootNode;
    }

    public void addNode( int version,int hashCode){
        nodes.add(new Node(version,hashCode));
    }


    public int findVersionByHash(int hc) {
        for(var node:nodes){
            if(node.hashCode==hc)return node.version;
        }
        return -1;
    }
    public boolean isVersionExist(int version){
        for(Node node:nodes){
            if(node.version==version){
                return true;
            }
        }
        return false;
    }
}
