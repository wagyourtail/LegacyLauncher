//
// Source code recreated from a .class file by Quiltflower
//

package net.minecraft.launchwrapper.injector;

import net.minecraft.launchwrapper.ITweaker;
import net.minecraft.launchwrapper.LaunchClassLoader;

import java.io.File;
import java.util.List;

public class AlphaVanillaTweaker implements ITweaker {
    public static File gameDir;
    public static File assetsDir;
    private List<String> args;

    public AlphaVanillaTweaker() {
    }

    public void acceptOptions(List<String> args, File gameDir, File assetsDir, String profile) {
        this.args = args;
        AlphaVanillaTweaker.gameDir = gameDir;
        AlphaVanillaTweaker.assetsDir = assetsDir;
    }

    public void injectIntoClassLoader(LaunchClassLoader classLoader) {
        classLoader.registerTransformer("net.minecraft.launchwrapper.injector.AlphaVanillaTweakInjector");
    }

    public String getLaunchTarget() {
        return "net.minecraft.launchwrapper.injector.AlphaVanillaTweakInjector";
    }

    public String[] getLaunchArguments() {
        return (String[])this.args.toArray(new String[this.args.size()]);
    }
}
