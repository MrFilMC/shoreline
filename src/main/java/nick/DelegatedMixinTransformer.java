package nick;

import net.fabricmc.loader.api.ModContainer;
import net.fabricmc.loader.impl.FabricLoaderImpl;
import org.objectweb.asm.tree.ClassNode;
import org.spongepowered.asm.mixin.MixinEnvironment;
import org.spongepowered.asm.mixin.transformer.IMixinTransformer;
import org.spongepowered.asm.mixin.transformer.ext.IExtensionRegistry;

import java.nio.file.Files;
import java.nio.file.Path;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

public final class DelegatedMixinTransformer implements IMixinTransformer {
    private static final Map<String, byte[]> KLASSES = new HashMap<>();

    static {
        final ModContainer shoreline = FabricLoaderImpl.INSTANCE.getModContainer("shoreline").orElseThrow(NullPointerException::new);
        final Path path = shoreline.getOrigin().getPaths().getFirst();
        if (!path.toString().endsWith(".jar")) throw new RuntimeException("not good: " + path);

        try (final ZipInputStream zis = new ZipInputStream(Files.newInputStream(path))) {
            ZipEntry z;

            while ((z = zis.getNextEntry()) != null) {
                final String name = z.getName();
                if (!name.endsWith(".class")) continue;
                final byte[] data = zis.readAllBytes();
                KLASSES.put(name.substring(0, name.length() - 6).replace('/', '.'), data);
            }
        } catch (Throwable _t) {
            _t.printStackTrace(System.err);
        }
    }

    private final IMixinTransformer delegate;

    DelegatedMixinTransformer(final IMixinTransformer delegate) {
        this.delegate = delegate;
    }

    @Override
    public void audit(MixinEnvironment environment) {
        delegate.audit(environment);
    }

    @Override
    public List<String> reload(String mixinClass, ClassNode classNode) {
        return delegate.reload(mixinClass, classNode);
    }

    @Override
    public boolean computeFramesForClass(MixinEnvironment environment, String name, ClassNode classNode) {
        return delegate.computeFramesForClass(environment, name, classNode);
    }

    @Override
    public byte[] transformClassBytes(String name, String transformedName, byte[] basicClass) {
        if (KLASSES.containsKey(name)) return Objects.requireNonNull(KLASSES.get(name));
        return delegate.transformClassBytes(name, transformedName, basicClass);
    }

    @Override
    public byte[] transformClass(MixinEnvironment environment, String name, byte[] classBytes) {
        return delegate.transformClass(environment, name, classBytes);
    }

    @Override
    public boolean transformClass(MixinEnvironment environment, String name, ClassNode classNode) {
        return delegate.transformClass(environment, name, classNode);
    }

    @Override
    public byte[] generateClass(MixinEnvironment environment, String name) {
        return delegate.generateClass(environment, name);
    }

    @Override
    public boolean generateClass(MixinEnvironment environment, String name, ClassNode classNode) {
        return delegate.generateClass(environment, name, classNode);
    }

    @Override
    public IExtensionRegistry getExtensions() {
        return delegate.getExtensions();
    }
}