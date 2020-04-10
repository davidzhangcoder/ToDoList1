package com.todolist.todomain.fragment.done;

import com.todolist.app.AppComponent;
import com.todolist.di.FragmentScoped;

import dagger.Component;

@FragmentScoped
@Component(modules = {DoneFragmentModule.class} , dependencies = AppComponent.class )
public interface DoneFragmentComponent {

    void inject(DoneFragment doneFragment);

}
