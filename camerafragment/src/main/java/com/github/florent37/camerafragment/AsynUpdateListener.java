package com.github.florent37.camerafragment;

import java.util.List;

public interface AsynUpdateListener {
    void onDataReceivedSuccess(List<List<String>> listData);
    void onDataReceivedFailed();
}
