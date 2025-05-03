package dev.xfj.core.services;

import com.google.gson.*;
import com.google.gson.reflect.TypeToken;
import com.google.gson.stream.JsonReader;
import dev.xfj.core.constants.DataPath;
import dev.xfj.core.utils.KeyValue;
import dev.xfj.generated.achievementexcelconfigdata.AchievementExcelConfigDataJson;
import dev.xfj.generated.achievementgoalexcelconfigdata.AchievementGoalExcelConfigDataJson;
import dev.xfj.generated.animalcodexexcelconfigdata.AnimalCodexExcelConfigDataJson;
import dev.xfj.generated.attackattenuationexcelconfigdata.AttackAttenuationExcelConfigDataJson;
import dev.xfj.generated.avatarcodexexcelconfigdata.AvatarCodexExcelConfigDataJson;
import dev.xfj.generated.avatarcostumeexcelconfigdata.AvatarCostumeExcelConfigDataJson;
import dev.xfj.generated.avatarcurveexcelconfigdata.AvatarCurveExcelConfigDataJson;
import dev.xfj.generated.avatarexcelconfigdata.AvatarExcelConfigDataJson;
import dev.xfj.generated.avatarfetterslevelexcelconfigdata.AvatarFettersLevelExcelConfigDataJson;
import dev.xfj.generated.avatarflycloakexcelconfigdata.AvatarFlycloakExcelConfigDataJson;
import dev.xfj.generated.avatarlevelexcelconfigdata.AvatarLevelExcelConfigDataJson;
import dev.xfj.generated.avatarpromoteexcelconfigdata.AvatarPromoteExcelConfigDataJson;
import dev.xfj.generated.avatarskilldepotexcelconfigdata.AvatarSkillDepotExcelConfigDataJson;
import dev.xfj.generated.avatarskillexcelconfigdata.AvatarSkillExcelConfigDataJson;
import dev.xfj.generated.avatartalentexcelconfigdata.AvatarTalentExcelConfigDataJson;
import dev.xfj.generated.bookscodexexcelconfigdata.BooksCodexExcelConfigDataJson;
//import dev.xfj.generated.configavatar.ConfigAvatar;
import dev.xfj.generated.cookbonusexcelconfigdata.CookBonusExcelConfigDataJson;
import dev.xfj.generated.dungeonentryexcelconfigdata.DungeonEntryExcelConfigDataJson;
import dev.xfj.generated.dungeonexcelconfigdata.DungeonExcelConfigDataJson;
import dev.xfj.generated.equipaffixexcelconfigdata.EquipAffixExcelConfigDataJson;
import dev.xfj.generated.fettercharactercardexcelconfigdata.FetterCharacterCardExcelConfigDataJson;
import dev.xfj.generated.fetterinfoexcelconfigdata.FetterInfoExcelConfigDataJson;
import dev.xfj.generated.fettersexcelconfigdata.FettersExcelConfigDataJson;
import dev.xfj.generated.fetterstoryexcelconfigdata.FetterStoryExcelConfigDataJson;
import dev.xfj.generated.furnituresuiteexcelconfigdata.FurnitureSuiteExcelConfigDataJson;
import dev.xfj.generated.homeworldfurnitureexcelconfigdata.HomeWorldFurnitureExcelConfigDataJson;
import dev.xfj.generated.homeworldfurnituretypeexcelconfigdata.HomeWorldFurnitureTypeExcelConfigDataJson;
import dev.xfj.generated.homeworldnpcexcelconfigdata.HomeWorldNPCExcelConfigDataJson;
import dev.xfj.generated.localizationexcelconfigdata.LocalizationExcelConfigDataJson;
import dev.xfj.generated.manualtextmapconfigdata.ManualTextMapConfigDataJson;
import dev.xfj.generated.materialcodexexcelconfigdata.MaterialCodexExcelConfigDataJson;
import dev.xfj.generated.materialexcelconfigdata.MaterialExcelConfigDataJson;
import dev.xfj.generated.materialsourcedataexcelconfigdata.MaterialSourceDataExcelConfigDataJson;
import dev.xfj.generated.monstercurveexcelconfigdata.MonsterCurveExcelConfigDataJson;
import dev.xfj.generated.monsterdescribeexcelconfigdata.MonsterDescribeExcelConfigDataJson;
import dev.xfj.generated.monsterexcelconfigdata.MonsterExcelConfigDataJson;
import dev.xfj.generated.monstermultiplayerexcelconfigdata.MonsterMultiPlayerExcelConfigDataJson;
import dev.xfj.generated.monsterspecialnameexcelconfigdata.MonsterSpecialNameExcelConfigDataJson;
import dev.xfj.generated.monstertitleexcelconfigdata.MonsterTitleExcelConfigDataJson;
import dev.xfj.generated.proudskillexcelconfigdata.ProudSkillExcelConfigDataJson;
import dev.xfj.generated.reliquaryaffixexcelconfigdata.ReliquaryAffixExcelConfigDataJson;
import dev.xfj.generated.reliquarycodexexcelconfigdata.ReliquaryCodexExcelConfigDataJson;
import dev.xfj.generated.reliquaryexcelconfigdata.ReliquaryExcelConfigDataJson;
import dev.xfj.generated.reliquarylevelexcelconfigdata.ReliquaryLevelExcelConfigDataJson;
import dev.xfj.generated.reliquarymainpropexcelconfigdata.ReliquaryMainPropExcelConfigDataJson;
import dev.xfj.generated.reliquarysetexcelconfigdata.ReliquarySetExcelConfigDataJson;
import dev.xfj.generated.rewardexcelconfigdata.RewardExcelConfigDataJson;
import dev.xfj.generated.weaponcurveexcelconfigdata.WeaponCurveExcelConfigDataJson;
import dev.xfj.generated.weaponexcelconfigdata.WeaponExcelConfigDataJson;
import dev.xfj.generated.weaponlevelexcelconfigdata.WeaponLevelExcelConfigDataJson;
import dev.xfj.generated.weaponpromoteexcelconfigdata.WeaponPromoteExcelConfigDataJson;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.InputStream;
import java.lang.reflect.Constructor;
import java.lang.reflect.Method;
import java.lang.reflect.Type;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static dev.xfj.core.constants.DataPath.*;

@Service
public class DatabaseService {
    private static final Logger logger = LoggerFactory.getLogger(DatabaseService.class);
    public String language;
    public Map<String, String> languageMap;
    public Map<String, String> readableMap;
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
    public final List<LocalizationExcelConfigDataJson> localizationConfig;
    public final List<MaterialSourceDataExcelConfigDataJson> materialSourceDataConfig;
    public final List<MonsterExcelConfigDataJson> monsterConfig;
    public final List<MonsterDescribeExcelConfigDataJson> monsterDescribeConfig;
    public final List<MonsterTitleExcelConfigDataJson> monsterTitleConfig;
    public final List<MonsterSpecialNameExcelConfigDataJson> monsterSpecialNameConfig;
    public final List<AnimalCodexExcelConfigDataJson> animalCodexConfig;
    public final List<MonsterCurveExcelConfigDataJson> monsterCurveConfig;
    public final List<MonsterMultiPlayerExcelConfigDataJson> monsterMultiPlayerConfig;
    public final List<AchievementExcelConfigDataJson> achievementConfig;
    public final List<AchievementGoalExcelConfigDataJson> achievementGoalConfig;
    //public final List<ConfigAvatar> configAvatars;

    public DatabaseService() {
        try {
            this.language = "EN";
            setLanguage();
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
            this.localizationConfig = loadJSONArray(LocalizationExcelConfigDataJson.class);
            this.materialSourceDataConfig = loadJSONArray(MaterialSourceDataExcelConfigDataJson.class);
            this.monsterConfig = loadJSONArray(MonsterExcelConfigDataJson.class);
            this.monsterDescribeConfig = loadJSONArray(MonsterDescribeExcelConfigDataJson.class);
            this.monsterTitleConfig = loadJSONArray(MonsterTitleExcelConfigDataJson.class);
            this.monsterSpecialNameConfig = loadJSONArray(MonsterSpecialNameExcelConfigDataJson.class);
            this.animalCodexConfig = loadJSONArray(AnimalCodexExcelConfigDataJson.class);
            this.monsterCurveConfig = loadJSONArray(MonsterCurveExcelConfigDataJson.class);
            this.monsterMultiPlayerConfig = loadJSONArray(MonsterMultiPlayerExcelConfigDataJson.class);
            this.achievementConfig = loadJSONArray(AchievementExcelConfigDataJson.class);
            this.achievementGoalConfig = loadJSONArray(AchievementGoalExcelConfigDataJson.class);
            //this.configAvatars = loadAvatarDirectory();
        } catch (Exception e) {
            throw new RuntimeException(e.getMessage());
        }
    }

    public void setLanguage() {
        try {
            languageMap = loadLanguage(language);
            readableMap = loadReadable(language);
        } catch (FileNotFoundException e) {
            System.err.println(e.getMessage());
        }
    }

    public String getTranslation(String key) {
        return languageMap.get(key);
    }

    public String getManualMappedText(String id) {
        return manualTextMapConfig
                .stream()
                .filter(text -> id.equals(text.getTextMapId()))
                .map(map -> getTranslation(map.getTextMapContentTextMapHash()))
                .findAny()
                .orElse(null);
    }

    public MaterialExcelConfigDataJson getItem(int id) {
        return materialConfig
                .stream()
                .filter(item -> item.getId() == id)
                .findFirst()
                .orElse(null);
    }

    public WeaponExcelConfigDataJson getWeapon(int id) {
        return weaponConfig
                .stream()
                .filter(item -> item.getId() == id)
                .findFirst()
                .orElse(null);
    }

    public KeyValue getItemTextHash(int id) {
        MaterialExcelConfigDataJson item = getItem(id);

        if (item != null) {
            return new KeyValue(item.getNameTextMapHash(), item.getId());
        }

        WeaponExcelConfigDataJson weapon = getWeapon(id);

        if (weapon != null) {
            return new KeyValue(weapon.getNameTextMapHash(), weapon.getId());
        }

        return null;
    }

    private Map<String, String> loadLanguage(String language) throws FileNotFoundException {
        String file = String.format("TextMap%1$s.json", language);

        JsonReader jsonReader = new JsonReader(new FileReader(TEXT_MAP.path + file));
        JsonObject jsonObject = JsonParser.parseReader(jsonReader).getAsJsonObject();
        Type type = TypeToken.getParameterized(Map.class, String.class, String.class).getType();

        Map<String, String> result = new Gson().fromJson(jsonObject, type);
        logger.info(String.format("Loaded: %1$7d entries from %2$s", result.keySet().size(), file));

        return result;
    }

    private Map<String, String> loadReadable(String language) {
        File readableLocation = new File(String.format("%1$s%2$s", READABLE.path, language));
        List<Path> filePaths = findFiles(readableLocation.listFiles(), "txt");

        Map<String, String> result = filePaths.stream()
                .collect(Collectors.toMap(
                        file -> file.getFileName().toString().split("\\.")[0],
                        this::loadTextFile
                ));

        logger.info(String.format("Loaded: %1$7d entries from %2$s", result.keySet().size(), readableLocation));

        return result;
    }

    private List<Path> findFiles(File[] files, String extension) {
        return Arrays.stream(files)
                .filter(file -> file.toString().endsWith(extension))
                .map(File::toPath)
                .collect(Collectors.toList());
    }

    private String loadTextFile(Path path) {
        String text;

        try (InputStream inputStream = Files.newInputStream(path)) {
            byte[] bytes = inputStream.readAllBytes();
            text = new String(bytes, StandardCharsets.UTF_8);
        } catch (Exception e) {
            System.err.println("Failed to open file: " + path);
            throw new RuntimeException(e);
        }

        return text;
    }

    //private List<ConfigAvatar> loadAvatarDirectory() {
    //    return findFiles(new File(AVATAR.path).listFiles(), "json")
    //            .stream()
    //            .filter(file -> !Files.isDirectory(file))
    //            .map(Path::getFileName)
    //            .map(Path::toString)
    //            .filter(file -> file.startsWith("ConfigAvatar"))
    //            .filter(file -> file.chars().filter(character -> character == '_').count() <= 1)
    //            .collect(Collectors.toSet())
    //            .stream()
    //            .map(file -> {
    //                try {
    //                    return loadJSON(ConfigAvatar.class, AVATAR.path, file);
    //                } catch (FileNotFoundException e) {
    //                    throw new RuntimeException(e);
    //                }
    //            })
    //            .collect(Collectors.toList());
    //}

    private <T> List<T> loadJSONArray(Class<T> clazz) throws FileNotFoundException {
        return loadJSONArray(EXCEL_BIN_OUTPUT, clazz);
    }

    private <T> List<T> loadJSONArray(DataPath dataPath, Class<T> clazz) throws FileNotFoundException {
        String file = clazz.getSimpleName().replace("Json", ".json");
        return loadJSONArray(clazz, dataPath.path, file);
    }

    private <T> List<T> loadJSONArray(DataPath dataPath, Class<T> clazz, String file) throws FileNotFoundException {
        return loadJSONArray(clazz, dataPath.path, file);
    }

    private <T> List<T> loadJSONArray(Class<T> clazz, String baseDirectory, String file) throws FileNotFoundException {
        JsonReader jsonReader = new JsonReader(new FileReader(baseDirectory + file));
        JsonElement jsonElement = JsonParser.parseReader(jsonReader).getAsJsonArray();
        Type type = TypeToken.getParameterized(List.class, clazz).getType();
        JsonArray jsonArray = convertFieldNames(jsonElement).getAsJsonArray();

        logger.info(String.format("Loaded: %1$7d entries from %2$s", jsonArray.size(), file));

        return new Gson().fromJson(jsonArray, type);
    }

    private JsonElement convertFieldNames(JsonElement jsonElement) {
        if (jsonElement.isJsonObject()) {
            JsonObject jsonObject = jsonElement.getAsJsonObject();
            JsonObject resultObject = new JsonObject();

            for (String fieldName : jsonObject.keySet()) {
                String newFieldName = applyKeyOverride(fieldName);

                if (!fieldName.equals(newFieldName)) {
                    resultObject.add(newFieldName, convertFieldNames(jsonObject.get(fieldName)));
                } else {
                    resultObject.add(fieldName, convertFieldNames(jsonObject.get(fieldName)));
                }
            }

            return resultObject;
        }

        if (jsonElement.isJsonArray()) {
            JsonArray jsonArray = jsonElement.getAsJsonArray();
            JsonArray resultArray = new JsonArray();

            for (JsonElement arrayElement : jsonArray) {
                resultArray.add(convertFieldNames(arrayElement));
            }

            return resultArray;
        }

        return jsonElement;
    }

    private static String applyKeyOverride(String fieldName) {
        String result = fieldName;

        if (isAllUppercase(fieldName)) {
            return result;
        }

        if (fieldName.contains("_")) {
            return result;
        }

        if (Character.isUpperCase(fieldName.charAt(0))) {
            System.out.println("Adjusting field: " + fieldName);
            result = Character.toLowerCase(fieldName.charAt(0)) + fieldName.substring(1);
        }

        return result;
    }

    private static boolean isAllUppercase(String value) {
        for (char c : value.toCharArray()) {
            if (!Character.isUpperCase(c)) {
                return false;
            }
        }

        return true;
    }

    private <T> T loadJSON(DataPath dataPath, Class<T> clazz) throws FileNotFoundException {
        String file = clazz.getSimpleName().replace("Json", ".json");
        return loadJSON(clazz, dataPath.path, file);
    }

    private <T> T loadJSON(DataPath dataPath, Class<T> clazz, String file) throws FileNotFoundException {
        return loadJSON(clazz, dataPath.path, file);
    }

    private <T> T loadJSON(Class<T> clazz, String baseDirectory, String file) throws FileNotFoundException {
        JsonReader jsonReader = new JsonReader(new FileReader(baseDirectory + file));
        JsonObject jsonObject = JsonParser.parseReader(jsonReader).getAsJsonObject();
        //Type type = TypeToken.getParameterized(Map.class, String.class, clazz).getType();

        logger.info(String.format("Loaded: %1$7d entries from %2$s", jsonObject.keySet().size(), file));

        return new Gson().fromJson(jsonObject, clazz);
    }

    private <T, U> Map<Integer, T> loadDataWithIntegerId(Class<T> returnType, List<U> inputList) {
        return loadDataWithId(Integer.class, returnType, inputList);
    }

    private <T, U> Map<Integer, T> loadDataWithIntegerId(Class<T> returnType, List<U> inputList, String idMethod) {
        return loadDataWithId(Integer.class, returnType, inputList, idMethod);
    }

    private <T, U> Map<String, T> loadDataWithStringId(Class<T> returnType, List<U> inputList) {
        return loadDataWithId(String.class, returnType, inputList);
    }

    private <T, U> Map<String, T> loadDataWithStringId(Class<T> returnType, List<U> inputList, String idMethod) {
        return loadDataWithId(String.class, returnType, inputList, idMethod);
    }

    private <T, U, V> Map<T, U> loadDataWithId(Class<T> identifierType, Class<U> returnType, List<V> inputList) {
        return loadDataWithId(identifierType, returnType, inputList, "getId");
    }

    private <T, U, V> Map<T, U> loadDataWithId(Class<T> identifierType, Class<U> returnType, List<V> inputList, String idMethod) {
        return inputList.stream()
                .collect(Collectors.toMap(
                        input -> getId(identifierType, input, idMethod),
                        unwrappedData -> constructInstance(returnType, unwrappedData))
                );
    }

    private <T, U> Map<Integer, Map<Integer, T>> loadNestedDataWithIds(
            Class<T> returnType,
            List<U> inputList,
            String firstIdMethod,
            String secondIdMethod
    ) {
        return inputList
                .stream()
                .collect(Collectors.groupingBy(
                        unwrappedData -> getId(Integer.class, unwrappedData, firstIdMethod),
                        Collectors.mapping(
                                unwrappedData -> constructInstance(returnType, unwrappedData),
                                Collectors.toMap(
                                        wrappedData -> getId(Integer.class, wrappedData, secondIdMethod),
                                        wrappedData -> wrappedData)
                        )
                ));
    }

    private <T> T getId(Class<T> returnType, Object input, String idMethod) {
        try {
            Method method = input.getClass().getMethod(idMethod);
            return returnType.cast(method.invoke(input));
        } catch (Exception e) {
            throw new RuntimeException(e.getMessage());
        }
    }

    private <T> T constructInstance(Class<T> returnType, Object argument) {
        try {
            Constructor<T> constructor = returnType.getConstructor(argument.getClass());
            return constructor.newInstance(argument);
        } catch (Exception e) {
            throw new RuntimeException(e.getMessage());
        }
    }
}
