package com.todolist.app;

import com.todolist.todomain.TestA;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;

@Module
public class AppModule {

    @Provides
    @Singleton
    TestA provideTestA(){
        return new TestA();
    }

}
