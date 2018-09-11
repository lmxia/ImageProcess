package com.github.florent37.camerafragment;

import android.util.Log;

import org.codehaus.jackson.map.ObjectMapper;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.ProtocolException;
import java.net.URL;
import java.util.UUID;

public class UploadUtils {
    private static final String TAG = UploadUtils.class.getSimpleName();
    private static final ObjectMapper mapper = new ObjectMapper();
    private static final int TIME_OUT = 30 * 1000;

    private static final String CRLF = "\r\n";
    private static final String CONTENT_TYPE = "multipart/form-data";

    public static final int SUCCESS = 0;
    public static final int FAILURE = -1;
    public static final int FILE_NOT_EXIST = -2;

    public static <T> T postFileToURL(File file, String mimeType, URL url,
                                    String fieldName, final Class<T> responseClass) {
        if (file == null) // 再判断一次，因为可能在选择图片之后，该图片在上传之前被删除
            return null;
        try {
            String boundary = UUID.randomUUID().toString();
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();

            setHttpURLConnection(conn, boundary);
            writeData(conn, boundary, file, mimeType, fieldName);

            int res = conn.getResponseCode();
            Log.e(TAG, "response code:" + res);
            if (res == 200){
                InputStream in = conn.getInputStream();
                T value = null;
                value = (T) mapper.readValue(in, responseClass);
                return value;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    private static void setHttpURLConnection(HttpURLConnection conn,
                                             String boundary) {
        conn.setConnectTimeout(TIME_OUT);
        conn.setReadTimeout(TIME_OUT);

        conn.setDoInput(true); // 允许输入流
        conn.setDoOutput(true); // 允许输出流

        try {
            conn.setRequestMethod("POST");
        } catch (ProtocolException e) {
            e.printStackTrace();
        }
        conn.setRequestProperty("Connection", "keep-alive");
        conn.setRequestProperty("Content-Type", CONTENT_TYPE + "; boundary="
                + boundary);
    }

    private static void writeData(HttpURLConnection conn, String boundary,
                                  File file, String mimeType, String fieldName) throws IOException {
        DataOutputStream requestData = new DataOutputStream(
                conn.getOutputStream());

        requestData.writeBytes("--" + boundary + CRLF);
        requestData.writeBytes("Content-Disposition: form-data; name=\""
                + fieldName + "\"; filename=\"" + file.getName() + "\"" + CRLF);
        requestData.writeBytes("Content-Type: " + mimeType + CRLF + CRLF);

        InputStream fileInput = new FileInputStream(file);
        int bytesRead;
        byte[] buffer = new byte[1024];
        while ((bytesRead = fileInput.read(buffer)) != -1) {
            requestData.write(buffer, 0, bytesRead);
        }
        fileInput.close();
        requestData.writeBytes(CRLF);

        requestData.writeBytes("--" + boundary + "--" + CRLF);
        requestData.flush();
    }
}