package roadhog360.simpleskinbackport.core;

import com.google.common.collect.Lists;
import net.minecraft.util.ResourceLocation;
import org.apache.commons.lang3.tuple.Pair;

import java.util.Arrays;
import java.util.List;
import java.util.UUID;

public class DefaultPlayerSkin {
    private static final ResourceLocation[] DEFAULT_SKINS = new ResourceLocation[] {
        new ResourceLocation("textures/entity/player/slim/alex.png"),
        new ResourceLocation("textures/entity/player/slim/ari.png"),
        new ResourceLocation("textures/entity/player/slim/efe.png"),
        new ResourceLocation("textures/entity/player/slim/kai.png"),
        new ResourceLocation("textures/entity/player/slim/makena.png"),
        new ResourceLocation("textures/entity/player/slim/noor.png"),
        new ResourceLocation("textures/entity/player/slim/steve.png"),
        new ResourceLocation("textures/entity/player/slim/sunny.png"),
        new ResourceLocation("textures/entity/player/slim/zuri.png"),

        new ResourceLocation("textures/entity/player/wide/alex.png"),
        new ResourceLocation("textures/entity/player/wide/ari.png"),
        new ResourceLocation("textures/entity/player/wide/efe.png"),
        new ResourceLocation("textures/entity/player/wide/kai.png"),
        new ResourceLocation("textures/entity/player/wide/makena.png"),
        new ResourceLocation("textures/entity/player/wide/noor.png"),
        new ResourceLocation("textures/entity/player/wide/steve.png"),
        new ResourceLocation("textures/entity/player/wide/sunny.png"),
        new ResourceLocation("textures/entity/player/wide/zuri.png")};

    public static final ResourceLocation ALEX = DEFAULT_SKINS[0];
    public static final ResourceLocation STEVE = DEFAULT_SKINS[15];

    public static final UUID NULL_UUID = UUID.randomUUID(); //Used if the UUID passed in is somehow null.

    public static final String NONE = "NONE";

    public enum Set {
        STEVE_ONLY("The only default skin will be Steve, the tried-and-true classic player model. His arms will always be wide.", STEVE),
        STEVE_ALEX("The default skins will either be wide-armed Steve or slim-armed Alex, as it was in 1.8 to 1.19.2.", STEVE, ALEX),
        ALL_DEFAULTS("Steve, Alex, and all of the new 1.19.3 default skins will be assigned. They all can appear in slim or wide armed variants.", DEFAULT_SKINS),
//        BONUS_CHARACTERS, //Some of the alternate skins from the old legacy console default skin packs. Currently not implemented
        ;

        private final List<Pair<ResourceLocation, Boolean>> skins = Lists.newLinkedList();
        private final String description;

        Set(String description, ResourceLocation... locs) {
            this.description = description;
            for(ResourceLocation location : locs) {
                skins.add(Pair.of(location, location.toString().contains("slim")));
            }
        }

        public ResourceLocation getDefaultSkin(int index) {
            return getEntryFromIndex(index).getLeft();
        }

        public ResourceLocation getDefaultSkin(UUID uuid) {
            return getDefaultSkin(uuid.hashCode());
        }

        public boolean isDefaultSkinSlim(int index) {
            return getEntryFromIndex(index).getRight();
        }

        public boolean isDefaultSkinSlim(UUID uuid) {
            return isDefaultSkinSlim(uuid.hashCode());
        }

        private Pair<ResourceLocation, Boolean> getEntryFromIndex(int index) {
            if(skins.isEmpty()) {
                throw new RuntimeException("Default skin set " + name() + " had empty skin list. What are you doing!?");
            }
            if(skins.size() == 1) {
                return skins.get(0);
            }
            if(skins.size() == 2) {
                return skins.get(index & 1);//1.8 skin choosing logic, returns Alex if UUID hash & 1 == 1
            }
            return skins.get(Math.floorMod(index, skins.size()));//1.19+ skin choosing logic. Uses Math.floorMod to roll the index of skin based on UUID hash
        }

        public String getDescription() {
            return description;
        }

        public static Set valueOfOrNull(String value) {
            return Arrays.stream(Set.values()).filter(skinSet -> skinSet.name().equals(value)).findFirst().orElse(null);
        }
    }
}
