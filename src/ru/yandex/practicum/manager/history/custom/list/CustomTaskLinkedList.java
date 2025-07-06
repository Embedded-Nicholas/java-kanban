package ru.yandex.practicum.manager.history.custom.list;

import ru.yandex.practicum.model.Task;

import java.util.*;

public class CustomTaskLinkedList implements CustomTaskList<Task>{
    private Node head;
    private Node tail;
    private int size;
    private HashMap<UUID, Node> tasks;

    public CustomTaskLinkedList() {
        this.head = null;
        this.tail = null;
        this.size = 0;
        this.tasks = new HashMap<UUID, Node>();
    }

    @Override
    public void add(Task task) {
        if (this.tasks.containsKey(task.getId())){
            this.remove(task.getId());
        }

        Node newNode = new Node(null, task, null);
        if (this.size == 0) {
            this.head = newNode;
        } else{
            newNode.previous = this.tail;
            newNode.next = null;
            this.tail.next = newNode;
        }
        this.tail = newNode;
        this.tasks.put(task.getId(), newNode);
        this.size++;
    }

    @Override
    public void remove(UUID taskId) {
        Node nodeToRemove = this.tasks.get(taskId);
        if (nodeToRemove != null) {
            Node previous = nodeToRemove.previous;
            Node next = nodeToRemove.next;

            if (previous != null) {
                previous.next = next;
            } else{
                head = next;
            }

            if (next != null){
                next.previous = previous;
            } else{
                tail = previous;
            }

            this.tasks.remove(taskId);
            this.size--;

        } else{
            throw new IllegalArgumentException("Task not found");
        }
    }

    @Override
    public void clear() {
        Node current = this.head;
        while (current != null) {
            Node next = current.next;
            current.next = null;
            current.previous = null;
            current = next;
        }

        this.head = null;
        this.tail = null;
        this.size = 0;

        if (this.tasks != null) {
            this.tasks.clear();
        }
    }

    @Override
    public void update(Task task) {
        Node nodeToUpdate = this.tasks.get(task.getId());
        if (nodeToUpdate != null) {
            nodeToUpdate.value = task;
        }
    }

    @Override
    public Task get(UUID taskId) {
        Node node = this.tasks.get(taskId);
        return node != null ? node.getValue() : null;
    }

    @Override
    public int size() {
        return this.size;
    }

    public Task getTail() {
        return this.tail.getValue();
    }

    public Task getHead() {
        return this.head.getValue();
    }

    @Override
    public Iterator<Task> iterator() {
        return new Iterator(){
            private Node current = head;

            @Override
            public boolean hasNext() {
                return this.current != null;
            }

            @Override
            public Task next() {
                if (!hasNext()) {
                    throw new NoSuchElementException();
                }
                Task value = current.getValue();
                current = current.next;
                return value;
            }
        };
    }

    private static class Node{
        Node previous;
        Task value;
        Node next;

        public Node(Node previous, Task value, Node next) {
            this.previous = previous;
            this.value = value;
            this.next = next;
        }

        public Task getValue() {
            return this.value;
        }
    }
}
