//
// Source code recreated from a .class file by Quiltflower
//

package net.minecraft.launchwrapper.injector;

import java.io.File;
import java.util.List;
import java.util.ListIterator;
import javax.imageio.ImageIO;
import net.minecraft.launchwrapper.IClassTransformer;
import net.minecraft.launchwrapper.IndevVanillaTweaker;
import org.objectweb.asm.ClassReader;
import org.objectweb.asm.ClassWriter;
import org.objectweb.asm.tree.AbstractInsnNode;
import org.objectweb.asm.tree.ClassNode;
import org.objectweb.asm.tree.JumpInsnNode;
import org.objectweb.asm.tree.MethodInsnNode;
import org.objectweb.asm.tree.MethodNode;
import org.objectweb.asm.tree.TableSwitchInsnNode;
import org.objectweb.asm.tree.VarInsnNode;

public class IndevVanillaTweakInjector implements IClassTransformer {
    private static String workDirFieldName;

    public IndevVanillaTweakInjector() {
    }

    public byte[] transform(String name, String transformedName, byte[] bytes) {
        if (bytes == null) {
            return null;
        } else {
            if (name.equals("net.minecraft.client.d")) {
                String beep = name.toLowerCase();
            }

            ClassNode classNode = new ClassNode();
            ClassReader classReader = new ClassReader(bytes);
            classReader.accept(classNode, 8);
            if (!classNode.interfaces.contains("java/lang/Runnable")) {
                return bytes;
            } else {
                MethodNode runMethod = null;

                for(MethodNode methodNode : (List<MethodNode>) classNode.methods) {
                    if ("run".equals(methodNode.name)) {
                        runMethod = methodNode;
                        break;
                    }
                }

                if (runMethod == null) {
                    return bytes;
                } else {
                    System.out.println("Probably the minecraft class (it has run && is applet!): " + name);
                    ListIterator<AbstractInsnNode> iterator = runMethod.instructions.iterator();
                    int firstSwitchJump = -1;

                    while(iterator.hasNext()) {
                        AbstractInsnNode instruction = (AbstractInsnNode)iterator.next();
                        if (instruction.getOpcode() == 170) {
                            TableSwitchInsnNode tableSwitchInsnNode = (TableSwitchInsnNode)instruction;
                            firstSwitchJump = runMethod.instructions.indexOf((AbstractInsnNode)tableSwitchInsnNode.labels.get(0));
                        } else if (firstSwitchJump >= 0 && runMethod.instructions.indexOf(instruction) == firstSwitchJump) {
                            int endOfSwitch = -1;

                            while(iterator.hasNext()) {
                                instruction = (AbstractInsnNode)iterator.next();
                                if (instruction.getOpcode() == 167) {
                                    endOfSwitch = runMethod.instructions.indexOf(((JumpInsnNode)instruction).label);
                                    break;
                                }
                            }

                            if (endOfSwitch >= 0) {
                                while(runMethod.instructions.indexOf(instruction) != endOfSwitch && iterator.hasNext()) {
                                    instruction = (AbstractInsnNode)iterator.next();
                                }

                                instruction = (AbstractInsnNode)iterator.next();
                                runMethod.instructions
                                    .insertBefore(
                                        instruction,
                                        new MethodInsnNode(184, "net/minecraft/launchwrapper/injector/IndevVanillaTweakInjector", "inject", "()Ljava/io/File;")
                                    );
                                runMethod.instructions.insertBefore(instruction, new VarInsnNode(58, 2));
                            }
                        }
                    }

                    ClassWriter writer = new ClassWriter(3);
                    classNode.accept(writer);
                    return writer.toByteArray();
                }
            }
        }
    }

    public static File inject() {
        System.out.println("Turning of ImageIO disk-caching");
        ImageIO.setUseCache(false);
        VanillaTweakInjector.loadIconsOnFrames(IndevVanillaTweaker.assetsDir);
        System.out.println("Setting gameDir to: " + IndevVanillaTweaker.gameDir);
        return IndevVanillaTweaker.gameDir;
    }
}
