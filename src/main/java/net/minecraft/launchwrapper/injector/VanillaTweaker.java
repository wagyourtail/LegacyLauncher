//
// Source code recreated from a .class file by Quiltflower
//

package net.minecraft.launchwrapper.injector;

import net.minecraft.launchwrapper.ITweaker;
import net.minecraft.launchwrapper.LaunchClassLoader;

import java.io.File;
import java.util.List;

public class VanillaTweaker implements ITweaker {
    public static File gameDir;
    public static File assetsDir;
    private List<String> args;

    public VanillaTweaker() {
    }

    public void acceptOptions(List<String> args, File gameDir, File assetsDir, String profile) {
        this.args = args;
        VanillaTweaker.gameDir = gameDir;
        VanillaTweaker.assetsDir = assetsDir;
    }

    public void injectIntoClassLoader(LaunchClassLoader classLoader) {
        classLoader.registerTransformer("net.minecraft.launchwrapper.injector.VanillaTweakInjector");
    }

    public String getLaunchTarget() {
        return "net.minecraft.client.Minecraft";
    }

    public String[] getLaunchArguments() {
        return (String[])this.args.toArray(new String[this.args.size()]);
    }
}
