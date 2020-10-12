package tests;

import com.google.gson.Gson;

import java.util.HashMap;

public class MainTest {


    public static void main(String[] args)  {


        HashMap<Integer, Integer> integerIntegerHashMap = new HashMap<>();
        int a = integerIntegerHashMap.get(0) + 6;
        Gson gson = new Gson();
        System.out.println(a);
    }


    static class A {
        public int a;
    }

    static class B extends A {
        public int b;

        B(int a, int b) {
            this.a = a;
            this.b = b;
        }

        @Override
        public String toString() {
            return a + " " + b;
        }
    }
}
