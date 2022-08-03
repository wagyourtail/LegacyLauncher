//
// Source code recreated from a .class file by Quiltflower
//

package net.minecraft.launchwrapper;

import java.io.File;
import java.util.List;

public class IndevVanillaTweaker implements ITweaker {
    public static File gameDir;
    public static File assetsDir;
    private List<String> args;

    public IndevVanillaTweaker() {
    }

    public void acceptOptions(List<String> args, File gameDir, File assetsDir, String profile) {
        this.args = args;
        IndevVanillaTweaker.gameDir = gameDir;
        IndevVanillaTweaker.assetsDir = assetsDir;
    }

    public void injectIntoClassLoader(LaunchClassLoader classLoader) {
        classLoader.registerTransformer("net.minecraft.launchwrapper.injector.IndevVanillaTweakInjector");
    }

    public String getLaunchTarget() {
        return "net.minecraft.launchwrapper.injector.AlphaVanillaTweakInjector";
    }

    public String[] getLaunchArguments() {
        return (String[])this.args.toArray(new String[this.args.size()]);
    }
}
