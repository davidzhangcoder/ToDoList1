package com.todolist.todomain;

import com.todolist.di.ActivityScoped;
import com.todolist.di.FragmentScoped;
import com.todolist.todomain.fragment.done.DoneFragment;
import com.todolist.todomain.fragment.todo.ToDoFragment;
import com.todolist.todomain.fragment.todo.ToDoFragmentComponent;

import dagger.Module;
import dagger.Provides;

@Module(subcomponents = {ToDoFragmentComponent.class} )
@ActivityScoped
public class ToDoMainActivityModule {

//    @Provides
//    ToDoFragment provideToDoFragment(){
//        return ToDoFragment.newInstance();
//    }

//    @Provides
//    DoneFragment provideDoneFragment(){
//        return DoneFragment.newInstance();
//    }

}
