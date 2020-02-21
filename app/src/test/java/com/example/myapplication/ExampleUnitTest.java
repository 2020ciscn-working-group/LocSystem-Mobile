package com.example.myapplication;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
public class ExampleUnitTest {
    @Before//注解
    public void setUp() throws Exception {

        System.out.println("测试开始执行的代码在这里执行，可做初始化操作");
    }

    @After
    private void tearDown() throws Exception {
        System.out.println("测试结束后的执行代码在这里执行，可做释放资源操作");
    }

    @Test
    public void addition_isCorrect() {
        assertEquals(4, 2 + 2);
    }
}