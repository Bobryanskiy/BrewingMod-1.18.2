package net.bobr.testmod.sound;

import net.bobr.testmod.TestMod;
import net.minecraft.sound.SoundEvent;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;

public class ModSounds {
    public static SoundEvent BURP = registerSoundEvent("burp");

    private static SoundEvent registerSoundEvent(String name) {
        Identifier id = new Identifier(TestMod.MOD_ID, name);
        return Registry.register(Registry.SOUND_EVENT, id, new SoundEvent(id));
    }
}
