package com.todolist.app;

import android.content.Context;

import com.todolist.data.Injection;
import com.todolist.data.source.ToDoItemRepository;
import com.todolist.todomain.TestA;
import com.todolist.todomain.fragment.done.DoneFragment;
import com.todolist.todomain.fragment.todo.ToDoFragment;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;

@Module//(subcomponents = {ToDoFragmentComponent.class} )
public class AppModule {

    private Context context;

    public AppModule(Context context) {
        this.context = context;
    }

    @Provides
    @Singleton //Module的scope要和Component一致
    TestA provideTestA(){
        return new TestA();
    }

    @Provides
    @Singleton
    ToDoItemRepository provideToDoItemRepository() {
        return Injection.provideToDoItemRepository( context );
    }

    @Provides
    @Singleton
    ToDoFragment provideToDoFragment(){
        return ToDoFragment.newInstance();
    }

    @Provides
    @Singleton
    DoneFragment provideDoneFragment(){
        return DoneFragment.newInstance();
    }

}
