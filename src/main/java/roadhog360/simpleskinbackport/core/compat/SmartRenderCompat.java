package roadhog360.simpleskinbackport.core.compat;

import cpw.mods.fml.common.Loader;
import net.minecraft.client.model.ModelRenderer;
import roadhog360.simpleskinbackport.SimpleSkinBackport;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

public class SmartRenderCompat {
    private static Class modelRotationRendererSmartRendererClass;
    private static Method updateLocalsFunc;

    /**
     * Smart Moving has its own copy of these values that it seems to be really bad at updating, so we need to do this.
     * Shart Moving's approach "works" in vanilla but breaks when anything tries to generate its own display lists. (Like... SimpleSkinBackport!)
     * I'm starting to think calling this mod "Smart" is giving it too much credit.
     */
    public static void updateSmartRenderFields(ModelRenderer... renderers) {
        if(smartMovingCompatEnabled()) {
            for (ModelRenderer renderer : renderers) {
                invokeUpdate(renderer);
            }
        }
    }

    /**
     * Initialize reflection for Smart Render-specific fields. Returns false if initialization failed.
     * @return
     */
    public static boolean tryInitSmartRenderFields() {
        if(Loader.isModLoaded("SmartRender")) {
            try {
                modelRotationRendererSmartRendererClass = Class.forName("net.smart.render.ModelRotationRenderer");
                updateLocalsFunc = modelRotationRendererSmartRendererClass.getDeclaredMethod("UpdateLocals");
                updateLocalsFunc.setAccessible(true);
            } catch (ClassNotFoundException | NoSuchMethodException e) {
                //Welp, we tried. Fuck it, let the GL errors commence, it's Smart Render's fault they happen anyway...
                SimpleSkinBackport.LOG.warn("Smart Render compatibility handler failed. Prepare for rendering errors.");
                e.printStackTrace();
                return false;
            }
            return true;
        }
        return false;
    }

    private static void invokeUpdate(ModelRenderer renderer) {
        if(modelRotationRendererSmartRendererClass.isAssignableFrom(renderer.getClass())) {
            try {
                updateLocalsFunc.invoke(renderer);
            } catch (IllegalAccessException | InvocationTargetException e) {
                throw new RuntimeException(e);
            }
        }
    }

    private static boolean smartMovingCompatEnabled() {
        return updateLocalsFunc != null;
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
