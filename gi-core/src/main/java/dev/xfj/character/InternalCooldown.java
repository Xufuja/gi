package dev.xfj.character;

import dev.xfj.jsonschema2pojo.attackattenuationexcelconfigdata.AttackAttenuationExcelConfigDataJson;

import java.util.List;

public class InternalCooldown {
    private final AttackAttenuationExcelConfigDataJson data;

    public InternalCooldown(AttackAttenuationExcelConfigDataJson data) {
        this.data = data;
    }

    public String getId() {
        return data.getGroup();
    }

    public double getResetInterval() {
        return data.getResetCycle();
    }

    public List<Integer> getGaugeSequence() {
        return data.getDurabilitySequence();
    }
}
