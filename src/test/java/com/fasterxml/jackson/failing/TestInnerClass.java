package com.fasterxml.jackson.failing;

import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.databind.*;

public class TestInnerClass extends BaseMapTest
{
    public static class Dog2
    {
        public String name;
        public List<Leg> legs;

        public class Leg {
            public int length;
        }
    }
    
    /*
    /**********************************************************
    /* Tests
    /**********************************************************
     */

    // additional part of [JACKSON-677]
    public void test677() throws Exception
    {
        ObjectMapper mapper = new ObjectMapper();
        // and bit more checking as per later comments
        JavaType t677 = mapper.constructType(Result677.Success677.class);
        assertNotNull(t677);
        Result677.Success677<Integer> s = new Result677.Success677<Integer>(Integer.valueOf(4));
        String json = mapper.writeValueAsString(s);
        assertEquals("{\"value\":4}", json);
    }
    
    // core/[Issue#32]
    public void testInnerList() throws Exception
    {
        Dog2 dog = new Dog2();
        dog.name = "Spike";
        dog.legs = new ArrayList<Dog2.Leg>();
        dog.legs.add(dog.new Leg());
        dog.legs.add(dog.new Leg());
        dog.legs.get(0).length = 5;
        dog.legs.get(1).length = 4;

        ObjectMapper mapper = new ObjectMapper();

        String dogJson = mapper.writeValueAsString(dog);
//        System.out.println(dogJson);
      // output: {"name":"Spike","legs":[{length: 5}, {length: 4}]}

        // currently throws JsonMappingException
        Dog2 dogCopy = mapper.readValue(dogJson, Dog2.class);
        assertEquals(dogCopy.legs.get(1).length, 4);
        // prefer fully populated Dog instance
    }
}

// more fails with [JACKSON-677]
class Result677<T> {
    public static class Success677<K> extends Result677<K> {
        public K value;
        
        public Success677() { }
        public Success677(K k) { value = k; }
    }
}

