//
// Source code recreated from a .class file by Quiltflower
//

package net.minecraft.launchwrapper.injector;

import java.awt.Frame;
import java.awt.Image;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.util.Arrays;
import java.util.List;
import java.util.ListIterator;
import javax.imageio.ImageIO;
import net.minecraft.launchwrapper.IClassTransformer;
import net.minecraft.launchwrapper.VanillaTweaker;
import org.lwjgl.opengl.Display;
import org.objectweb.asm.ClassReader;
import org.objectweb.asm.ClassWriter;
import org.objectweb.asm.Label;
import org.objectweb.asm.Type;
import org.objectweb.asm.tree.AbstractInsnNode;
import org.objectweb.asm.tree.ClassNode;
import org.objectweb.asm.tree.FieldNode;
import org.objectweb.asm.tree.MethodNode;

public class VanillaTweakInjector implements IClassTransformer {
    private static String workDirFieldName;

    public VanillaTweakInjector() {
    }

    public byte[] transform(String name, String transformedName, byte[] bytes) {
        if (bytes == null) {
            return null;
        } else if (!"net.minecraft.client.Minecraft".equals(name)) {
            return bytes;
        } else {
            ClassNode classNode = new ClassNode();
            ClassReader classReader = new ClassReader(bytes);
            classReader.accept(classNode, 8);
            MethodNode mainMethod = null;

            for(MethodNode methodNode : (List<MethodNode>) classNode.methods) {
                if ("main".equals(methodNode.name)) {
                    mainMethod = methodNode;
                    break;
                }
            }

            if (mainMethod == null) {
                return bytes;
            } else {
                FieldNode workDirNode = null;

                for(FieldNode fieldNode : (List<FieldNode>) classNode.fields) {
                    String fileTypeDescriptor = Type.getDescriptor(File.class);
                    if (fileTypeDescriptor.equals(fieldNode.desc) && (fieldNode.access & 8) == 8) {
                        workDirNode = fieldNode;
                        break;
                    }
                }

                MethodNode injectedMethod = new MethodNode();
                Label label = new Label();
                injectedMethod.visitLabel(label);
                injectedMethod.visitLineNumber(9001, label);
                injectedMethod.visitMethodInsn(184, "net/minecraft/launchwrapper/injector/VanillaTweakInjector", "inject", "()Ljava/io/File;");
                injectedMethod.visitFieldInsn(179, "net/minecraft/client/Minecraft", workDirNode.name, "Ljava/io/File;");
                ListIterator<AbstractInsnNode> iterator = mainMethod.instructions.iterator();

                while(iterator.hasNext()) {
                    AbstractInsnNode insn = (AbstractInsnNode)iterator.next();
                    if (insn.getOpcode() == 177) {
                        mainMethod.instructions.insertBefore(insn, injectedMethod.instructions);
                    }
                }

                ClassWriter writer = new ClassWriter(3);
                classNode.accept(writer);
                return writer.toByteArray();
            }
        }
    }

    public static File inject() {
        System.out.println("Turning of ImageIO disk-caching");
        ImageIO.setUseCache(false);
        loadIconsOnFrames(VanillaTweaker.assetsDir);
        System.out.println("Setting gameDir to: " + VanillaTweaker.gameDir);
        return VanillaTweaker.gameDir;
    }

    public static void loadIconsOnFrames(File assetsDir) {
        try {
            File smallIcon = new File(assetsDir, "icons/icon_16x16.png");
            File bigIcon = new File(assetsDir, "icons/icon_32x32.png");
            System.out.println("Loading current icons for window from: " + smallIcon + " and " + bigIcon);
            Display.setIcon(new ByteBuffer[]{loadIcon(smallIcon), loadIcon(bigIcon)});
            Frame[] frames = Frame.getFrames();
            if (frames != null) {
                List<Image> icons = Arrays.asList(ImageIO.read(smallIcon), ImageIO.read(bigIcon));

                for(Frame frame : frames) {
                    try {
                        frame.setIconImages(icons);
                    } catch (Throwable var10) {
                        var10.printStackTrace();
                    }
                }
            }
        } catch (IOException var11) {
            var11.printStackTrace();
        }

    }

    private static ByteBuffer loadIcon(File iconFile) throws IOException {
        BufferedImage icon = ImageIO.read(iconFile);
        int[] rgb = icon.getRGB(0, 0, icon.getWidth(), icon.getHeight(), null, 0, icon.getWidth());
        ByteBuffer buffer = ByteBuffer.allocate(4 * rgb.length);

        for(int color : rgb) {
            buffer.putInt(color << 8 | color >> 24 & 0xFF);
        }

        buffer.flip();
        return buffer;
    }
}
