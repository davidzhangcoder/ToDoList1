package com.todolist.todomain;

import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

public class TestATest {

    private TestA testA;

    @Before
    public void setUp() throws Exception {
        testA = new TestA();
    }

    @Test
    public void sum() {
        assertTrue( testA.sum( 2 ,2 ) == 4 );
    }
}