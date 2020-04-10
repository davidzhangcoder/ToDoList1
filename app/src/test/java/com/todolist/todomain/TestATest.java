package com.todolist.todomain;

import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

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

    @Test
    public void sum1() {
        TestA testA = mock( TestA.class );

        when( testA.sum(2,2) ).thenReturn( 1 );

        assertTrue( testA.sum( 2 ,2 ) == 1 );
    }

    @Test
    public void sum2() {
        TestA testA = mock( TestA.class );

        when( testA.sum(2,2) ).thenReturn( 1 );

        assertTrue( testA.sum( 2 ,2 ) == 1 );
    }

}