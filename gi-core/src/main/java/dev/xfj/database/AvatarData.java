package dev.xfj.database;

import dev.xfj.character.Character;
import dev.xfj.character.*;
import dev.xfj.jsonschema2pojo.attackattenuationexcelconfigdata.AttackAttenuationExcelConfigDataJson;
import dev.xfj.jsonschema2pojo.avatarcostumeexcelconfigdata.AvatarCostumeExcelConfigDataJson;
import dev.xfj.jsonschema2pojo.avatarcurveexcelconfigdata.AvatarCurveExcelConfigDataJson;
import dev.xfj.jsonschema2pojo.avatarexcelconfigdata.AvatarExcelConfigDataJson;
import dev.xfj.jsonschema2pojo.avatarfetterslevelexcelconfigdata.AvatarFettersLevelExcelConfigDataJson;
import dev.xfj.jsonschema2pojo.avatarflycloakexcelconfigdata.AvatarFlycloakExcelConfigDataJson;
import dev.xfj.jsonschema2pojo.avatarlevelexcelconfigdata.AvatarLevelExcelConfigDataJson;
import dev.xfj.jsonschema2pojo.avatarpromoteexcelconfigdata.AvatarPromoteExcelConfigDataJson;
import dev.xfj.jsonschema2pojo.avatarskilldepotexcelconfigdata.AvatarSkillDepotExcelConfigDataJson;
import dev.xfj.jsonschema2pojo.avatarskillexcelconfigdata.AvatarSkillExcelConfigDataJson;
import dev.xfj.jsonschema2pojo.avatartalentexcelconfigdata.AvatarTalentExcelConfigDataJson;
import dev.xfj.jsonschema2pojo.fetterinfoexcelconfigdata.FetterInfoExcelConfigDataJson;
import dev.xfj.jsonschema2pojo.proudskillexcelconfigdata.ProudSkillExcelConfigDataJson;

import java.util.List;
import java.util.Map;

public class AvatarData implements Data {
    private static AvatarData instance;
    public final List<AvatarExcelConfigDataJson> avatarConfig;
    public final List<AvatarSkillExcelConfigDataJson> skillConfig;
    public final List<AvatarSkillDepotExcelConfigDataJson> skillDepotConfig;
    private final List<AvatarLevelExcelConfigDataJson> levelConfig;
    public final List<AvatarPromoteExcelConfigDataJson> avatarPromoteConfig;
    public final List<AvatarTalentExcelConfigDataJson> avatarTalentConfig;
    private final List<AttackAttenuationExcelConfigDataJson> attackAttenuationConfig;
    private final List<AvatarFlycloakExcelConfigDataJson> avatarFlycloakConfig;
    private final List<AvatarCostumeExcelConfigDataJson> avatarCostumeConfig;
    public final List<AvatarCurveExcelConfigDataJson> avatarCurveConfig;
    private final List<AvatarFettersLevelExcelConfigDataJson> avatarFettersLevelConfig;
    public final List<FetterInfoExcelConfigDataJson> fetterInfoConfig;
    public final List<ProudSkillExcelConfigDataJson> proudSkillConfig;

    private AvatarData() {
        try {
            avatarConfig = loadJSONArray(AvatarExcelConfigDataJson.class);
            skillConfig = loadJSONArray(AvatarSkillExcelConfigDataJson.class);
            skillDepotConfig = loadJSONArray(AvatarSkillDepotExcelConfigDataJson.class);
            levelConfig = loadJSONArray(AvatarLevelExcelConfigDataJson.class);
            avatarPromoteConfig = loadJSONArray(AvatarPromoteExcelConfigDataJson.class);
            avatarTalentConfig = loadJSONArray(AvatarTalentExcelConfigDataJson.class);
            attackAttenuationConfig = loadJSONArray(AttackAttenuationExcelConfigDataJson.class);
            avatarFlycloakConfig = loadJSONArray(AvatarFlycloakExcelConfigDataJson.class);
            avatarCostumeConfig = loadJSONArray(AvatarCostumeExcelConfigDataJson.class);
            avatarCurveConfig = loadJSONArray(AvatarCurveExcelConfigDataJson.class);
            avatarFettersLevelConfig = loadJSONArray(AvatarFettersLevelExcelConfigDataJson.class);
            fetterInfoConfig = loadJSONArray(FetterInfoExcelConfigDataJson.class);
            proudSkillConfig = loadJSONArray(ProudSkillExcelConfigDataJson.class);
        } catch (Exception e) {
            throw new RuntimeException(e.getMessage());
        }
    }

    public static AvatarData getInstance() {
        if (instance == null) {
            instance = new AvatarData();
        }

        return instance;
    }

    public Map<Integer, Character> loadCharacters() {
        return loadDataWithIntegerId(Character.class, avatarConfig);
    }

    public Map<Integer, Talent> loadTalents() {
        return loadDataWithIntegerId(Talent.class, skillConfig);
    }

    public Map<Integer, TalentTree> loadTalentTrees() {
        return loadDataWithIntegerId(TalentTree.class, skillDepotConfig);
    }

    public Map<Integer, CharacterLevel> loadLevelRequirements() {
        return loadDataWithIntegerId(CharacterLevel.class, levelConfig, "getLevel");
    }

    public Map<Integer, Map<Integer, CharacterAscension>> loadCharacterAscensions() {
        return loadNestedDataWithIds(
                CharacterAscension.class,
                avatarPromoteConfig,
                "getAvatarPromoteId",
                "getAscension");
    }

    public Map<Integer, Constellation> loadConstellations() {
        return loadDataWithIntegerId(Constellation.class, avatarTalentConfig, "getTalentId");
    }

    public Map<String, InternalCooldown> loadInternalCooldown() {
        return loadDataWithStringId(InternalCooldown.class, attackAttenuationConfig, "getGroup");
    }

    public Map<Integer, Glider> loadGliders() {
        return loadDataWithIntegerId(Glider.class, avatarFlycloakConfig, "getFlycloakId");
    }

    public Map<Integer, CharacterSkin> loadSkins() {
        return loadDataWithIntegerId(CharacterSkin.class, avatarCostumeConfig, "getSkinId");
    }

    public Map<Integer, CharacterLevelCurve> loadLevelCurves() {
        return loadDataWithIntegerId(CharacterLevelCurve.class, avatarCurveConfig, "getLevel");
    }

    public Map<Integer, CharacterFriendshipLevel> loadFriendshipLevelRequirements() {
        return loadDataWithIntegerId(CharacterFriendshipLevel.class, avatarFettersLevelConfig, "getFetterLevel");
    }
}
