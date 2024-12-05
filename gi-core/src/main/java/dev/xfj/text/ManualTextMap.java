package dev.xfj.text;

import dev.xfj.constants.TextParameter;
import dev.xfj.jsonschema2pojo.manualtextmapconfigdata.ManualTextMapConfigDataJson;

import java.util.List;
import java.util.stream.Collectors;

public class ManualTextMap {
    private final ManualTextMapConfigDataJson data;

    public ManualTextMap(ManualTextMapConfigDataJson data) {
        this.data = data;
    }

    public String getId() {
        return data.getTextMapId();
    }

    public String getHash() {
        return data.getTextMapContentTextMapHash();
    }

    public List<TextParameter> getParameters() {
        return data.getParamTypes().stream().map(TextParameter::valueOf).collect(Collectors.toList());
    }
}
