package net.henrycmoss.bb.input;

import com.mojang.blaze3d.platform.InputConstants;
import net.minecraft.client.KeyMapping;
import net.minecraftforge.common.util.Lazy;
import org.lwjgl.glfw.GLFW;

public class KeyMappings {

    public static final Lazy<KeyMapping> R_KEY = Lazy.of(() ->
            new KeyMapping("key.bb.force",
                    InputConstants.Type.KEYSYM,
                    GLFW.GLFW_KEY_R,
                    "key.categories.bb")
    );
}
