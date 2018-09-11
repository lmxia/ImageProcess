package com.github.florent37.camerafragment;

import org.codehaus.jackson.annotate.JsonIgnoreProperties;
import org.codehaus.jackson.annotate.JsonProperty;

import java.io.Serializable;
import java.util.List;

@JsonIgnoreProperties(ignoreUnknown = true)
public class ImageProcessResponse implements Serializable {
    @JsonProperty("code")
    public String code;

    @JsonProperty("desc")
    public String desc;

    @JsonProperty("data")
    public List<List<String>> data;

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public List<List<String>> getData() {
        return data;
    }

    public void setData(List<List<String>> data) {
        this.data = data;
    }
}
