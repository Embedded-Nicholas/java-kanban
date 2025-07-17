package ru.yandex.practicum.manager.history.custom.list;

import ru.yandex.practicum.model.Task;

class Node {
    private Node previous;
    private Task value;
    private Node next;

    public Node getPrevious() {
        return previous;
    }

    public void setPrevious(Node previous) {
        this.previous = previous;
    }

    public void setValue(Task value) {
        this.value = value;
    }

    public Node getNext() {
        return next;
    }

    public void setNext(Node next) {
        this.next = next;
    }

    public Node(Node previous, Task value, Node next) {
        this.previous = previous;
        this.value = value;
        this.next = next;
    }

    public Task getValue() {
        return this.value;
    }
}
