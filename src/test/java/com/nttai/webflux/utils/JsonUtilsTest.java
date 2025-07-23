package com.nttai.webflux.utils;

import com.fasterxml.jackson.core.type.TypeReference;
import org.junit.jupiter.api.Test;

import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

class JsonUtilsTest {
    @Test
    void testObjectToJsonAndToJson() {
        Map<String, Object> map = new HashMap<>();
        map.put("key", "value");
        String json = JsonUtils.objectToJson(map);
        assertTrue(json.contains("key"));
        assertTrue(json.contains("value"));
        String json2 = JsonUtils.getInstance().toJson(map);
        assertEquals(json, json2);
    }

    @Test
    void testGetGenericObject() {
        Map<String, Object> map = new HashMap<>();
        map.put("a", 123);
        map.put("b", "test");
        TestObj obj = JsonUtils.getInstance().getGenericObject(map, TestObj.class);
        assertEquals(123, obj.a);
        assertEquals("test", obj.b);
    }

    static class TestObj {
        public int a;
        public String b;
    }
} 