package com.todolist.app;

import com.todolist.todomain.TestA;
import com.todolist.todomain.ToDoFragmentComponent;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;

@Module(subcomponents = {ToDoFragmentComponent.class} )
public class AppModule {

    @Provides
    @Singleton
    TestA provideTestA(){
        return new TestA();
    }

}
