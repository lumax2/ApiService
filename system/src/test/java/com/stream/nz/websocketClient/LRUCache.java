package com.stream.nz.websocketClient;

import java.util.HashMap;
import java.util.Map;

/**
 * @Author cheng hao
 * @Date 27/07/2024
 */
class LRUCache {

    private int capacity;
    private int currentCacheSize;
    // cache Map
    private Map<Integer,Node> cache =  new HashMap<Integer,Node>();
    // double linked list
    private Node start;
    private Node end;

    class Node{
        private Integer key;
        private Integer value;
        private Node pre;
        private Node next;
        public Node(){}
        public Node(Integer key,Integer value){
            this.key = key;
            this.value = value;
        }
        public Node getPre(){
            return pre;
        }
        public Node getNext(){
            return next;
        }
        public void setPre(Node pre){
            this.pre = pre;
        }
        public void setNext(Node next){
            this.next = next;
        }
        public Integer getKey(){
            return this.key;
        }
        public Integer getValue(){
            return this.value;
        }
        public void setValue(Integer value){
            this.value= value;
        }
    }

    public LRUCache(int capacity) {
        this.capacity = capacity;
        this.start = new Node();
        this.end = new Node();
        start.next = end;
        end.pre= start;
    }

    public int get(int key) {
        Node resultNode = cache.get(key);
        if(null == resultNode){
            return -1;
        }
        swapNodeToFront(resultNode);
        return resultNode.getValue();
    }

    public void put(int key, int value) {
        Node cacheNode = cache.get(key);
        if(null==cacheNode){
            // check capacity
            if(currentCacheSize >= capacity){
                this.removeNode(null);
            }
            // Cache
            Node latest = new Node(key,value);
            this.addNodeToFront(latest);
            cache.put(key,latest);
        }else{
            cacheNode.setValue(value);
            swapNodeToFront(cacheNode);
            cache.put(key,cacheNode);
        }
    }

    //swape node to the front
    private void swapNodeToFront(Node toSwap){

        Node toSwapPre = toSwap.getPre();
        Node toSwapNext= toSwap.getNext();
        if(null!=toSwapPre){
            toSwapPre.setNext(toSwapNext);
        }
        if(null!=toSwapNext){
            toSwapNext.setPre(toSwapPre);
        }

        Node oldLatest = start.getNext();
        oldLatest.setPre(toSwap);
        start.setNext(toSwap);

        toSwap.setPre(start);
        toSwap.setNext(oldLatest);
    }

    // add new node to front
    private void addNodeToFront(Node newFrontNode){
        Node oldLatest = start.getNext();
        oldLatest.setPre(newFrontNode);
        start.setNext(newFrontNode);

        newFrontNode.setPre(start);
        newFrontNode.setNext(oldLatest);

        this.currentCacheSize++;
    }

    // remove the end node
    private void removeNode(Node toRemove){
        if(null==toRemove){
            toRemove=end.getPre();
        }
        if(null==toRemove.getPre() && null==toRemove.getNext()){
            return;
        }

        Node toRemovePre = toRemove.getPre();
        Node toRemoveNext = toRemove.getNext();
        if(null!=toRemovePre && null!= toRemovePre.getNext()){
            toRemovePre.setNext(toRemoveNext);
        }
        if(null!= toRemoveNext && null!= toRemoveNext.getPre()){
            toRemoveNext.setPre(toRemovePre);
        }
        cache.remove(toRemove.getKey());

        this.currentCacheSize--;
    }
    public static void main(String[] args){
        LRUCache lruCache = new LRUCache(2);
        lruCache.put(1,1);
        lruCache.put(2,2);
        System.out.println(lruCache.get(1));
        lruCache.put(3,3);
        System.out.println(lruCache.get(2));
        lruCache.put(4,4);
        System.out.println(lruCache.get(1));
        System.out.println(lruCache.get(3));
        System.out.println(lruCache.get(4));
    }

}


