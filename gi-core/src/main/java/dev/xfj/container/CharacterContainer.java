package dev.xfj.container;

import dev.xfj.constants.CharacterRarity;
import dev.xfj.database.AvatarData;
import dev.xfj.database.Database;
import dev.xfj.jsonschema2pojo.avatarcurveexcelconfigdata.CurveInfo;
import dev.xfj.jsonschema2pojo.avatarexcelconfigdata.AvatarExcelConfigDataJson;
import dev.xfj.jsonschema2pojo.avatarexcelconfigdata.PropGrowcurve;
import dev.xfj.jsonschema2pojo.avatarpromoteexcelconfigdata.AddProp;
import dev.xfj.jsonschema2pojo.avatarpromoteexcelconfigdata.AvatarPromoteExcelConfigDataJson;
import dev.xfj.jsonschema2pojo.fetterinfoexcelconfigdata.FetterInfoExcelConfigDataJson;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class CharacterContainer {
    private int id;
    private int currentLevel;
    private int currentExperience;
    private int currentAscension;

    public CharacterContainer(int id, int currentLevel, int currentExperience, int currentAscension) {
        this.id = id;
        this.currentLevel = currentLevel;
        this.currentExperience = currentExperience;
        this.currentAscension = currentAscension;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return Database.getInstance().getTranslation(getAvatar().getNameTextMapHash());
    }

    public String getTitle() {
        return Database.getInstance().getTranslation(getFetter().getAvatarTitleTextMapHash());
    }

    public int getRarity() {
        return CharacterRarity.valueOf(getAvatar().getQualityType()).getStarValue();
    }

    public double getBaseHealth() {
        return getBaseStat(getAvatar().getHpBase(), "FIGHT_PROP_BASE_HP");
    }

    public double getBaseAttack() {
        return getBaseStat(getAvatar().getAttackBase(), "FIGHT_PROP_BASE_ATTACK");
    }

    public double getBaseDefense() {
        return getBaseStat(getAvatar().getDefenseBase(), "FIGHT_PROP_BASE_DEFENSE");
    }

    private double getBaseStat(double baseValue, String statType) {
        return (baseValue * getBaseStatMultiplier(statType)) + getExtraBaseStats(getAvatar().getAvatarPromoteId(), statType);
    }

    private double getExtraBaseStats(int promoteId, String selected) {
        return getAscensions(promoteId).values()
                .stream()
                .filter(ascension -> ascension.getPromoteLevel() == currentAscension)
                .flatMap(promotions -> promotions.getAddProps().stream())
                .filter(prop -> prop.getPropType().equals(selected))
                .mapToDouble(AddProp::getValue)
                .findFirst()
                .orElse(-1.0);
    }

    private double getCurveMultiplier(String curveType) {
        return AvatarData.getInstance().avatarCurveConfig
                .stream()
                .filter(level -> level.getLevel() == currentLevel)
                .flatMap(curves -> curves.getCurveInfos().stream())
                .filter(curve -> curve.getType().equals(curveType))
                .map(CurveInfo::getValue)
                .findFirst()
                .orElse(-1.0);
    }

    private double getBaseStatMultiplier(String selected) {
        return getAvatar().getPropGrowCurves()
                .stream()
                .filter(curves -> curves.getType().equals(selected))
                .map(curve -> getCurveMultiplier(curve.getGrowCurve()))
                .findFirst()
                .orElse(-1.0);
    }

    private AvatarExcelConfigDataJson getAvatar() {
        return AvatarData.getInstance().avatarConfig.stream().filter(character -> character.getId() == id).findFirst().orElse(null);
    }

    private FetterInfoExcelConfigDataJson getFetter() {
        return AvatarData.getInstance().fetterInfoConfig.stream().filter(character -> character.getAvatarId() == id).findFirst().orElse(null);
    }

    private Map<Integer, AvatarPromoteExcelConfigDataJson> getAscensions(int promoteId) {
        return AvatarData.getInstance().avatarPromoteConfig
                .stream()
                .filter(ascension -> ascension.getAvatarPromoteId() == promoteId)
                .collect(Collectors.toMap(
                        AvatarPromoteExcelConfigDataJson::getPromoteLevel,
                        data -> data
                ));
    }
}
