package com.todolist.todomain.fragment.category;

import com.todolist.app.AppComponent;
import com.todolist.di.FragmentScoped;

import dagger.Component;

@Component(modules = {CategoryModule.class} , dependencies = AppComponent.class )
@FragmentScoped
public interface CategoryComponent {
    void inject(CategoryFragment categoryFragment);
}
