package dev.xfj.database;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.google.gson.reflect.TypeToken;
import com.google.gson.stream.JsonReader;
import dev.xfj.jsonschema2pojo.attackattenuationexcelconfigdata.AttackAttenuationExcelConfigDataJson;
import dev.xfj.jsonschema2pojo.avatarcodexexcelconfigdata.AvatarCodexExcelConfigDataJson;
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
import dev.xfj.jsonschema2pojo.bookscodexexcelconfigdata.BooksCodexExcelConfigDataJson;
import dev.xfj.jsonschema2pojo.cookbonusexcelconfigdata.CookBonusExcelConfigDataJson;
import dev.xfj.jsonschema2pojo.dungeonentryexcelconfigdata.DungeonEntryExcelConfigDataJson;
import dev.xfj.jsonschema2pojo.dungeonexcelconfigdata.DungeonExcelConfigDataJson;
import dev.xfj.jsonschema2pojo.equipaffixexcelconfigdata.EquipAffixExcelConfigDataJson;
import dev.xfj.jsonschema2pojo.fettercharactercardexcelconfigdata.FetterCharacterCardExcelConfigDataJson;
import dev.xfj.jsonschema2pojo.fetterinfoexcelconfigdata.FetterInfoExcelConfigDataJson;
import dev.xfj.jsonschema2pojo.fettersexcelconfigdata.FettersExcelConfigDataJson;
import dev.xfj.jsonschema2pojo.fetterstoryexcelconfigdata.FetterStoryExcelConfigDataJson;
import dev.xfj.jsonschema2pojo.furnituresuiteexcelconfigdata.FurnitureSuiteExcelConfigDataJson;
import dev.xfj.jsonschema2pojo.homeworldfurnitureexcelconfigdata.HomeWorldFurnitureExcelConfigDataJson;
import dev.xfj.jsonschema2pojo.homeworldfurnituretypeexcelconfigdata.HomeWorldFurnitureTypeExcelConfigDataJson;
import dev.xfj.jsonschema2pojo.homeworldnpcexcelconfigdata.HomeWorldNPCExcelConfigDataJson;
import dev.xfj.jsonschema2pojo.manualtextmapconfigdata.ManualTextMapConfigDataJson;
import dev.xfj.jsonschema2pojo.materialcodexexcelconfigdata.MaterialCodexExcelConfigDataJson;
import dev.xfj.jsonschema2pojo.materialexcelconfigdata.MaterialExcelConfigDataJson;
import dev.xfj.jsonschema2pojo.proudskillexcelconfigdata.ProudSkillExcelConfigDataJson;
import dev.xfj.jsonschema2pojo.reliquaryaffixexcelconfigdata.ReliquaryAffixExcelConfigDataJson;
import dev.xfj.jsonschema2pojo.reliquarycodexexcelconfigdata.ReliquaryCodexExcelConfigDataJson;
import dev.xfj.jsonschema2pojo.reliquaryexcelconfigdata.ReliquaryExcelConfigDataJson;
import dev.xfj.jsonschema2pojo.reliquarylevelexcelconfigdata.ReliquaryLevelExcelConfigDataJson;
import dev.xfj.jsonschema2pojo.reliquarymainpropexcelconfigdata.ReliquaryMainPropExcelConfigDataJson;
import dev.xfj.jsonschema2pojo.reliquarysetexcelconfigdata.ReliquarySetExcelConfigDataJson;
import dev.xfj.jsonschema2pojo.rewardexcelconfigdata.RewardExcelConfigDataJson;
import dev.xfj.jsonschema2pojo.weaponcurveexcelconfigdata.WeaponCurveExcelConfigDataJson;
import dev.xfj.jsonschema2pojo.weaponexcelconfigdata.WeaponExcelConfigDataJson;
import dev.xfj.jsonschema2pojo.weaponlevelexcelconfigdata.WeaponLevelExcelConfigDataJson;
import dev.xfj.jsonschema2pojo.weaponpromoteexcelconfigdata.WeaponPromoteExcelConfigDataJson;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.lang.reflect.Type;
import java.util.List;
import java.util.Map;

import static dev.xfj.constants.DataPath.TEXT_MAP;

public class Database implements Data {
    public static Database instance;
    public Map<String, String> languageMap;
    public final List<ManualTextMapConfigDataJson> manualTextMapConfig;
    public final List<AvatarExcelConfigDataJson> avatarConfig;
    public final List<AvatarSkillExcelConfigDataJson> skillConfig;
    public final List<AvatarSkillDepotExcelConfigDataJson> skillDepotConfig;
    public final List<AvatarLevelExcelConfigDataJson> levelConfig;
    public final List<AvatarPromoteExcelConfigDataJson> avatarPromoteConfig;
    public final List<AvatarTalentExcelConfigDataJson> avatarTalentConfig;
    public final List<AttackAttenuationExcelConfigDataJson> attackAttenuationConfig;
    public final List<AvatarFlycloakExcelConfigDataJson> avatarFlycloakConfig;
    public final List<AvatarCostumeExcelConfigDataJson> avatarCostumeConfig;
    public final List<AvatarCurveExcelConfigDataJson> avatarCurveConfig;
    public final List<AvatarFettersLevelExcelConfigDataJson> avatarFettersLevelConfig;
    public final List<FetterInfoExcelConfigDataJson> fetterInfoConfig;
    public final List<ProudSkillExcelConfigDataJson> proudSkillConfig;
    public final List<FetterCharacterCardExcelConfigDataJson> fetterCharacterCardConfig;
    public final List<HomeWorldNPCExcelConfigDataJson> homeWorldNPCConfig;
    public final List<FetterStoryExcelConfigDataJson> fetterStoryConfig;
    public final List<FettersExcelConfigDataJson> fettersConfig;
    public final List<AvatarCodexExcelConfigDataJson> avatarCodexConfig;
    public final List<WeaponExcelConfigDataJson> weaponConfig;
    public final List<WeaponPromoteExcelConfigDataJson> weaponPromoteConfig;
    public final List<WeaponLevelExcelConfigDataJson> weaponLevelConfig;
    public final List<WeaponCurveExcelConfigDataJson> weaponCurveConfig;
    public final List<ReliquaryExcelConfigDataJson> reliquaryConfig;
    public final List<ReliquaryMainPropExcelConfigDataJson> reliquaryMainPropConfig;
    public final List<ReliquaryAffixExcelConfigDataJson> reliquaryAffixConfig;
    public final List<ReliquaryLevelExcelConfigDataJson> reliquaryLevelConfig;
    public final List<ReliquaryCodexExcelConfigDataJson> reliquaryCodexConfig;
    public final List<ReliquarySetExcelConfigDataJson> reliquarySetConfig;
    public final List<MaterialExcelConfigDataJson> materialConfig;
    public final List<RewardExcelConfigDataJson> rewardConfig;
    public final List<CookBonusExcelConfigDataJson> cookBonusConfig;
    public final List<FurnitureSuiteExcelConfigDataJson> furnitureSuiteConfig;
    public final List<HomeWorldFurnitureExcelConfigDataJson> homeWorldFurnitureConfig;
    public final List<HomeWorldFurnitureTypeExcelConfigDataJson> homeWorldFurnitureTypeConfig;
    public final List<EquipAffixExcelConfigDataJson> equipAffixConfig;
    public final List<MaterialCodexExcelConfigDataJson> materialCodexConfig;
    public final List<BooksCodexExcelConfigDataJson> booksCodexConfig;
    public final List<DungeonExcelConfigDataJson> dungeonConfig;
    public final List<DungeonEntryExcelConfigDataJson> dungeonEntryConfig;

    public Database() throws FileNotFoundException {
        this.languageMap = loadLanguage("EN");
        this.manualTextMapConfig = loadJSONArray(ManualTextMapConfigDataJson.class);
        this.avatarConfig = loadJSONArray(AvatarExcelConfigDataJson.class);
        this.skillConfig = loadJSONArray(AvatarSkillExcelConfigDataJson.class);
        this.skillDepotConfig = loadJSONArray(AvatarSkillDepotExcelConfigDataJson.class);
        this.levelConfig = loadJSONArray(AvatarLevelExcelConfigDataJson.class);
        this.avatarPromoteConfig = loadJSONArray(AvatarPromoteExcelConfigDataJson.class);
        this.avatarTalentConfig = loadJSONArray(AvatarTalentExcelConfigDataJson.class);
        this.attackAttenuationConfig = loadJSONArray(AttackAttenuationExcelConfigDataJson.class);
        this.avatarFlycloakConfig = loadJSONArray(AvatarFlycloakExcelConfigDataJson.class);
        this.avatarCostumeConfig = loadJSONArray(AvatarCostumeExcelConfigDataJson.class);
        this.avatarCurveConfig = loadJSONArray(AvatarCurveExcelConfigDataJson.class);
        this.avatarFettersLevelConfig = loadJSONArray(AvatarFettersLevelExcelConfigDataJson.class);
        this.fetterInfoConfig = loadJSONArray(FetterInfoExcelConfigDataJson.class);
        this.proudSkillConfig = loadJSONArray(ProudSkillExcelConfigDataJson.class);
        this.fetterCharacterCardConfig = loadJSONArray(FetterCharacterCardExcelConfigDataJson.class);
        this.homeWorldNPCConfig = loadJSONArray(HomeWorldNPCExcelConfigDataJson.class);
        this.fetterStoryConfig = loadJSONArray(FetterStoryExcelConfigDataJson.class);
        this.fettersConfig = loadJSONArray(FettersExcelConfigDataJson.class);
        this.avatarCodexConfig = loadJSONArray(AvatarCodexExcelConfigDataJson.class);
        this.weaponConfig = loadJSONArray(WeaponExcelConfigDataJson.class);
        this.weaponPromoteConfig = loadJSONArray(WeaponPromoteExcelConfigDataJson.class);
        this.weaponLevelConfig = loadJSONArray(WeaponLevelExcelConfigDataJson.class);
        this.weaponCurveConfig = loadJSONArray(WeaponCurveExcelConfigDataJson.class);
        this.reliquaryConfig = loadJSONArray(ReliquaryExcelConfigDataJson.class);
        this.reliquaryMainPropConfig = loadJSONArray(ReliquaryMainPropExcelConfigDataJson.class);
        this.reliquaryAffixConfig = loadJSONArray(ReliquaryAffixExcelConfigDataJson.class);
        this.reliquaryLevelConfig = loadJSONArray(ReliquaryLevelExcelConfigDataJson.class);
        this.reliquaryCodexConfig = loadJSONArray(ReliquaryCodexExcelConfigDataJson.class);
        this.reliquarySetConfig = loadJSONArray(ReliquarySetExcelConfigDataJson.class);
        this.materialConfig = loadJSONArray(MaterialExcelConfigDataJson.class);
        this.rewardConfig = loadJSONArray(RewardExcelConfigDataJson.class);
        this.cookBonusConfig = loadJSONArray(CookBonusExcelConfigDataJson.class);
        this.furnitureSuiteConfig = loadJSONArray(FurnitureSuiteExcelConfigDataJson.class);
        this.homeWorldFurnitureConfig = loadJSONArray(HomeWorldFurnitureExcelConfigDataJson.class);
        this.homeWorldFurnitureTypeConfig = loadJSONArray(HomeWorldFurnitureTypeExcelConfigDataJson.class);
        this.equipAffixConfig = loadJSONArray(EquipAffixExcelConfigDataJson.class);
        this.materialCodexConfig = loadJSONArray(MaterialCodexExcelConfigDataJson.class);
        this.booksCodexConfig = loadJSONArray(BooksCodexExcelConfigDataJson.class);
        this.dungeonConfig = loadJSONArray(DungeonExcelConfigDataJson.class);
        this.dungeonEntryConfig = loadJSONArray(DungeonEntryExcelConfigDataJson.class);
    }

    public static Database getInstance() {
        if (instance == null) {
            try {
                instance = new Database();
            } catch (Exception e) {
                throw new RuntimeException(e.getMessage());
            }
        }

        return instance;
    }


    public void setLanguage(String language) {
        try {
            languageMap = loadLanguage(language);
        } catch (FileNotFoundException e) {
            System.err.println(e.getMessage());
        }
    }

    public Map<String, String> loadLanguage(String language) throws FileNotFoundException {
        String file = String.format("TextMap%1$s.json", language);

        JsonReader jsonReader = new JsonReader(new FileReader(TEXT_MAP.path + file));
        JsonObject jsonObject = JsonParser.parseReader(jsonReader).getAsJsonObject();
        Type type = TypeToken.getParameterized(Map.class, String.class, String.class).getType();

        Map<String, String> result = new Gson().fromJson(jsonObject, type);
        System.out.printf("Loaded: %1$7d entries from %2$s%n", result.keySet().size(), file);

        return result;
    }

    public String getTranslation(String key) {
        return languageMap.get(key);
    }
}
