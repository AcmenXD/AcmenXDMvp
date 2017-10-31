package com.acmenxd.retrofit.converter;

import android.support.annotation.NonNull;

import com.google.gson.Gson;
import com.google.gson.TypeAdapter;
import com.google.gson.stream.JsonReader;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.StringReader;

import okhttp3.ResponseBody;
import retrofit2.Converter;

/**
 * @author AcmenXD
 * @version v1.0
 * @github https://github.com/AcmenXD
 * @date 2017/1/4 15:50
 * @detail 下载json对象拆包类
 */
final class GsonResponseBodyConverter<T> implements Converter<ResponseBody, T> {
    private final Gson gson;
    private final TypeAdapter<T> adapter;

    GsonResponseBodyConverter(@NonNull Gson gson, @NonNull TypeAdapter<T> adapter) {
        this.gson = gson;
        this.adapter = adapter;
    }

    @Override
    public T convert(ResponseBody value) throws IOException {
        String json = value.string();
        try {
            JSONObject newJson = new JSONObject(json);
            newJson.put("json", json);
            json = newJson.toString();
        } catch (JSONException pE) {
            pE.printStackTrace();
        }
        JsonReader jsonReader = gson.newJsonReader(new StringReader(json));
        try {
            T data = adapter.read(jsonReader);
            return data;
        } finally {
            value.close();
        }
    }
}
