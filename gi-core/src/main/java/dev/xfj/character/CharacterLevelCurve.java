package dev.xfj.character;

import dev.xfj.constants.CharacterStatGrowthCurve;
import dev.xfj.constants.MathType;
import dev.xfj.jsonschema2pojo.avatarcurveexcelconfigdata.AvatarCurveExcelConfigDataJson;

import java.util.List;
import java.util.stream.Collectors;

public class CharacterLevelCurve {
    private final AvatarCurveExcelConfigDataJson data;

    public CharacterLevelCurve(AvatarCurveExcelConfigDataJson data) {
        this.data = data;
    }

    public int getLevel() {
        return data.getLevel();
    }

    public List<CharacterStatCurve> getStatCurves() {
        return data.getCurveInfos()
                .stream()
                .map(curve -> new CharacterStatCurve(
                        CharacterStatGrowthCurve.valueOf(curve.getType()),
                        MathType.valueOf(curve.getArith()),
                        curve.getValue())
                )
                .collect(Collectors.toList());
    }
}
