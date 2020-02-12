package com.todolist.ui.adapter;

public interface IAdapterAction<E> {

    public void addElement(E element);

    public void addElement(E element , int position);

    public void deleteElement(int position);
}
