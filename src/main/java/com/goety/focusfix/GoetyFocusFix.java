package com.goety.focusfix;

import com.Polarice3.Goety.config.SpellConfig;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;

@Mod(GoetyFocusFix.MODID)
public class GoetyFocusFix {
    public static final String MODID = "goety_focus_fix";

    public GoetyFocusFix(FMLJavaModLoadingContext context) {
        context.getModEventBus().addListener(this::onCommonSetup);
    }

    private void onCommonSetup(FMLCommonSetupEvent event) {
        SpellConfig.LaunchDuration.set(0);
        SpellConfig.LaunchCoolDown.set(0);
        SpellConfig.SPEC.save();
    }
}
