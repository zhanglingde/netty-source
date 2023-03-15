package com.ling.advance.chat.protocol;
import com.google.gson.*;

import java.io.*;
import java.lang.reflect.Type;
import java.nio.charset.StandardCharsets;

/**
 * 扩展序列化，反序列化算法
 */
public interface Serializer {

    /**
     * 对象 序列化 为字节数组
     *
     * @param object 被序列化的对象
     * @param <T>    对象类型
     * @return 序列化后的 字节数组
     */
    <T> byte[] serializer(T object);

    /**
     * 字节数组 反序列化为 对象
     *
     * @param clazz 反序列化对象的类型
     * @param bytes 需要反序列化的字节数组
     * @param <T>
     * @return 反序列化后的 对象
     */
    <T> T deserializer(Class<T> clazz, byte[] bytes);


    // 序列化算法
    enum Algorithm implements Serializer {

        Java {
            @Override
            public <T> byte[] serializer(T object) {
                try {
                    ByteArrayOutputStream bos = new ByteArrayOutputStream();
                    ObjectOutputStream oos = new ObjectOutputStream(bos);
                    oos.writeObject(object);
                    return bos.toByteArray();
                } catch (IOException e) {
                    throw new RuntimeException("序列化失败", e);
                }
            }

            @Override
            public <T> T deserializer(Class<T> clazz, byte[] bytes) {
                try {
                    ObjectInputStream ois = new ObjectInputStream(new ByteArrayInputStream(bytes));
                    return (T) ois.readObject();
                } catch (IOException | ClassNotFoundException e) {
                    throw new RuntimeException("序列化失败", e);
                }
            }
        },
        Json{
            @Override
            public <T> byte[] serializer(T object) {
                Gson gson = new GsonBuilder().registerTypeAdapter(Class.class, new ClassCodec()).create();
                String json = gson.toJson(object);
                // 编码和解码使用相同的字符编码
                return json.getBytes(StandardCharsets.UTF_8);
            }

            @Override
            public <T> T deserializer(Class<T> clazz, byte[] bytes) {
                Gson gson = new GsonBuilder().registerTypeAdapter(Class.class,new ClassCodec()).create();
                String json = new String(bytes,StandardCharsets.UTF_8);
                return gson.fromJson(json, clazz);
            }
        }
    }

    class ClassCodec implements JsonSerializer<Class<?>>, JsonDeserializer<Class<?>> {

        @Override
        public Class<?> deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
            try {
                String str = json.getAsString();
                return Class.forName(str);
            } catch (ClassNotFoundException e) {
                throw new JsonParseException(e);
            }
        }

        @Override             //   String.class
        public JsonElement serialize(Class<?> src, Type typeOfSrc, JsonSerializationContext context) {
            // class -> json
            return new JsonPrimitive(src.getName());
        }
    }
}
