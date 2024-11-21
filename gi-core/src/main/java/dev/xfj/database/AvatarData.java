package dev.xfj.database;

import dev.xfj.character.Character;
import dev.xfj.character.*;
import dev.xfj.jsonschema2pojo.attackattenuationexcelconfigdata.AttackAttenuationExcelConfigDataJson;
import dev.xfj.jsonschema2pojo.avatarexcelconfigdata.AvatarExcelConfigDataJson;
import dev.xfj.jsonschema2pojo.avatarlevelexcelconfigdata.AvatarLevelExcelConfigDataJson;
import dev.xfj.jsonschema2pojo.avatarpromoteexcelconfigdata.AvatarPromoteExcelConfigDataJson;
import dev.xfj.jsonschema2pojo.avatarskilldepotexcelconfigdata.AvatarSkillDepotExcelConfigDataJson;
import dev.xfj.jsonschema2pojo.avatarskillexcelconfigdata.AvatarSkillExcelConfigDataJson;
import dev.xfj.jsonschema2pojo.avatartalentexcelconfigdata.AvatarTalentExcelConfigDataJson;

import java.io.FileNotFoundException;
import java.util.List;
import java.util.Map;

public class AvatarData implements Data {
    private static AvatarData instance;
    private final List<AvatarExcelConfigDataJson> avatarConfig;
    private final List<AvatarSkillExcelConfigDataJson> skillConfig;
    private final List<AvatarSkillDepotExcelConfigDataJson> skillDepotConfig;
    private final List<AvatarLevelExcelConfigDataJson> levelConfig;
    private final List<AvatarPromoteExcelConfigDataJson> avatarPromoteConfig;
    private final List<AvatarTalentExcelConfigDataJson> avatarTalentConfig;
    private final List<AttackAttenuationExcelConfigDataJson> attackAttenuationConfig;

    private AvatarData() throws FileNotFoundException {
        avatarConfig = loadJSONArray(AvatarExcelConfigDataJson.class);
        skillConfig = loadJSONArray(AvatarSkillExcelConfigDataJson.class);
        skillDepotConfig = loadJSONArray(AvatarSkillDepotExcelConfigDataJson.class);
        levelConfig = loadJSONArray(AvatarLevelExcelConfigDataJson.class);
        avatarPromoteConfig = loadJSONArray(AvatarPromoteExcelConfigDataJson.class);
        avatarTalentConfig = loadJSONArray(AvatarTalentExcelConfigDataJson.class);
        attackAttenuationConfig = loadJSONArray(AttackAttenuationExcelConfigDataJson.class);
    }

    public static AvatarData getInstance() throws FileNotFoundException {
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

    public Map<Integer, LevelRequirement> loadLevelRequirements() {
        return loadDataWithIntegerId(LevelRequirement.class, levelConfig, "getLevel");
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
}
