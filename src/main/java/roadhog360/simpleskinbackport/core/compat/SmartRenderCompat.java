package roadhog360.simpleskinbackport.core.compat;

import cpw.mods.fml.common.Loader;
import net.minecraft.client.model.ModelRenderer;

import java.lang.reflect.Field;

public class SmartRenderCompat {
    private static Class modelRotationRendererSmartRendererClass;
    private static Field updateDisplayListField;
    private static Field updateCompiledField;
    private static boolean smartRenderSlopInitFailed;

    /**
     * Smart Moving has its own copy of these values that it seems to be really bad at updating, so we need to do this.
     * It "works" in vanilla but breaks when anything tries to generate its own display lists.
     * I'm starting to think calling this mod "Smart" is giving it too much credit.
     */
    public static void doSmartRenderCompat(ModelRenderer renderer) {
        if(!smartRenderSlopInitFailed && Loader.isModLoaded("SmartRender")) {
            tryInitSmartRenderFields();
            if(modelRotationRendererSmartRendererClass.isAssignableFrom(renderer.getClass())) {
                try {
                    updateDisplayListField.set(renderer, renderer.displayList);
                    updateCompiledField.set(renderer, true);
                } catch (IllegalAccessException e) {
                    throw new RuntimeException(e);
                }
            }
        }
    }

    /**
     * Also low-effort because I was sick of everything I threw at Smart Render messing up.
     * This was actually a last-ditch effort before I called it quits and it actually worked
     * Yer welcome to PR an improvement
     */
    public static void updateSmartRenderFields(ModelRenderer... renderers) {
        if(!smartRenderSlopInitFailed && Loader.isModLoaded("SmartRender")) {
            tryInitSmartRenderFields();
            for (ModelRenderer renderer : renderers) {
                if (modelRotationRendererSmartRendererClass.isAssignableFrom(renderer.getClass())) {
                    try {
                        //Same reason as above, duplicate fields that seldom are updated properly yada yada yada
                        updateDisplayListField.set(renderer, renderer.displayList);
                    } catch (IllegalAccessException e) {
                        throw new RuntimeException(e);
                    }
                }
            }
        }
    }

    /**
     * Initialize reflection for Smart Render-specific fields. Returns false if initialization failed.
     * @return
     */
    private static boolean tryInitSmartRenderFields() {
        if(modelRotationRendererSmartRendererClass == null || updateDisplayListField == null || updateCompiledField == null) {
            try {
                modelRotationRendererSmartRendererClass = Class.forName("net.smart.render.ModelRotationRenderer");
                updateDisplayListField = modelRotationRendererSmartRendererClass.getDeclaredField("displayList");
                updateCompiledField = modelRotationRendererSmartRendererClass.getDeclaredField("compiled");
            } catch (ClassNotFoundException | NoSuchFieldException ignored) {
                //Welp, we tried. Fuck it, let the GL errors commence, it's Smart Render's fault they happen anyways...
                smartRenderSlopInitFailed = true;
                return false;
            }
        }
        return true;
    }


        //For some ungodly reason, SmartRender's ModelRotationRenderer stores a "base" field in itself... which can also have a "base" field
        //Storing duplicate fields or generally just nesting things is a common theme in Smart Render.
        //These fixes took so long because it was hard to follow what exactly Smart Render was trying to accomplish.
        //I initially thought we needed to iterate through them, apparently not.
        //I'll leave this commented here in case I need to do this in the future. Here's how to iterate through all nested "base" fields.
        //References a now-deleted "baseSmartRenderField" Field handle, which references protected ModelRotationRenderer base in ModelRotationRenderer in Smart Render
//                    ModelRenderer fieldObj = renderer;
//                    do {
//                        displayListSmartRenderField.set(fieldObj, renderer.displayList);
//                        compiledSmartRenderField.set(fieldObj, true);
//                        fieldObj = baseSmartRenderField.get(fieldObj);
//                    } while (fieldObj != null);
}
