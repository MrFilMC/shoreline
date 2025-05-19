package nick;

import net.fabricmc.api.ModInitializer;
import net.fabricmc.loader.api.entrypoint.PreLaunchEntrypoint;
import org.spongepowered.asm.mixin.transformer.IMixinTransformer;
import sun.misc.Unsafe;

import java.io.DataInputStream;
import java.lang.invoke.*;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.ConcurrentHashMap;

public final class Main implements PreLaunchEntrypoint, ModInitializer {
    private static final String[] EVENTS = new String[]{"net/shoreline/client/cM","net/shoreline/client/cN","net/shoreline/client/cK","net/shoreline/client/cL","net/shoreline/client/cI","net/shoreline/client/cJ","net/shoreline/client/cG","net/shoreline/client/cH","net/shoreline/client/cE","net/shoreline/client/cF","net/shoreline/client/cC","net/shoreline/client/cD","net/shoreline/client/cA","net/shoreline/client/cB","net/shoreline/client/cY","net/shoreline/client/cZ","net/shoreline/client/cW","net/shoreline/client/cX","net/shoreline/client/cU","net/shoreline/client/cV","net/shoreline/client/cS","net/shoreline/client/cT","net/shoreline/client/cQ","net/shoreline/client/cR","net/shoreline/client/cO","net/shoreline/client/cP","net/shoreline/client/bZ","net/shoreline/client/bX","net/shoreline/client/bY","net/shoreline/client/bV","net/shoreline/client/bW","net/shoreline/client/gq","net/shoreline/client/gr","net/shoreline/client/go","net/shoreline/client/gp","net/shoreline/client/gm","net/shoreline/client/gn","net/shoreline/client/gk","net/shoreline/client/gl","net/shoreline/client/gj","net/shoreline/client/gg","net/shoreline/client/gh","net/shoreline/client/ge","net/shoreline/client/gf","net/shoreline/client/gc","net/shoreline/client/gd","net/shoreline/client/gw","net/shoreline/client/gx","net/shoreline/client/gu","net/shoreline/client/gv","net/shoreline/client/gs","net/shoreline/client/gt","net/shoreline/client/fp","net/shoreline/client/fq","net/shoreline/client/fn","net/shoreline/client/fo","net/shoreline/client/fl","net/shoreline/client/fm","net/shoreline/client/fj","net/shoreline/client/fk","net/shoreline/client/fh","net/shoreline/client/fi","net/shoreline/client/ff","net/shoreline/client/fg","net/shoreline/client/fd","net/shoreline/client/fe","net/shoreline/client/fb","net/shoreline/client/fc","net/shoreline/client/ga","net/shoreline/client/gb","net/shoreline/client/fz","net/shoreline/client/fx","net/shoreline/client/fy","net/shoreline/client/fv","net/shoreline/client/fw","net/shoreline/client/ft","net/shoreline/client/fu","net/shoreline/client/fr","net/shoreline/client/fs","net/shoreline/client/fP","net/shoreline/client/eo","net/shoreline/client/ep","net/shoreline/client/fQ","net/shoreline/client/fN","net/shoreline/client/em","net/shoreline/client/en","net/shoreline/client/fO","net/shoreline/client/fL","net/shoreline/client/ek","net/shoreline/client/fM","net/shoreline/client/fJ","net/shoreline/client/ei","net/shoreline/client/ej","net/shoreline/client/fK","net/shoreline/client/fH","net/shoreline/client/eg","net/shoreline/client/eh","net/shoreline/client/fI","net/shoreline/client/ee","net/shoreline/client/fF","net/shoreline/client/fG","net/shoreline/client/ef","net/shoreline/client/ec","net/shoreline/client/ed","net/shoreline/client/fE","net/shoreline/client/ea","net/shoreline/client/fB","net/shoreline/client/fC","net/shoreline/client/eb","net/shoreline/client/fa","net/shoreline/client/fZ","net/shoreline/client/ey","net/shoreline/client/ez","net/shoreline/client/fX","net/shoreline/client/ew","net/shoreline/client/ex","net/shoreline/client/fY","net/shoreline/client/eu","net/shoreline/client/fV","net/shoreline/client/fW","net/shoreline/client/ev","net/shoreline/client/fT","net/shoreline/client/fU","net/shoreline/client/et","net/shoreline/client/eq","net/shoreline/client/er","net/shoreline/client/fS","net/shoreline/client/eO","net/shoreline/client/dn","net/shoreline/client/eP","net/shoreline/client/do","net/shoreline/client/eM","net/shoreline/client/dl","net/shoreline/client/eN","net/shoreline/client/dm","net/shoreline/client/eK","net/shoreline/client/dj","net/shoreline/client/eL","net/shoreline/client/dk","net/shoreline/client/eI","net/shoreline/client/dh","net/shoreline/client/eJ","net/shoreline/client/di","net/shoreline/client/df","net/shoreline/client/eG","net/shoreline/client/dg","net/shoreline/client/eH","net/shoreline/client/dd","net/shoreline/client/eE","net/shoreline/client/de","net/shoreline/client/eF","net/shoreline/client/eC","net/shoreline/client/db","net/shoreline/client/eD","net/shoreline/client/dc","net/shoreline/client/eA","net/shoreline/client/da","net/shoreline/client/eB","net/shoreline/client/fA","net/shoreline/client/dz","net/shoreline/client/dx","net/shoreline/client/eY","net/shoreline/client/dy","net/shoreline/client/eZ","net/shoreline/client/dv","net/shoreline/client/eW","net/shoreline/client/dw","net/shoreline/client/eX","net/shoreline/client/eU","net/shoreline/client/dt","net/shoreline/client/eV","net/shoreline/client/du","net/shoreline/client/dr","net/shoreline/client/eS","net/shoreline/client/ds","net/shoreline/client/eT","net/shoreline/client/eQ","net/shoreline/client/dp","net/shoreline/client/eR","net/shoreline/client/dq","net/shoreline/client/cm","net/shoreline/client/dN","net/shoreline/client/dO","net/shoreline/client/cn","net/shoreline/client/ck","net/shoreline/client/dL","net/shoreline/client/dM","net/shoreline/client/cl","net/shoreline/client/dJ","net/shoreline/client/ci","net/shoreline/client/dK","net/shoreline/client/cj","net/shoreline/client/cg","net/shoreline/client/dH","net/shoreline/client/dI","net/shoreline/client/ch","net/shoreline/client/dF","net/shoreline/client/ce","net/shoreline/client/dG","net/shoreline/client/cf","net/shoreline/client/dD","net/shoreline/client/cc","net/shoreline/client/cd","net/shoreline/client/dE","net/shoreline/client/ca","net/shoreline/client/dB","net/shoreline/client/cb","net/shoreline/client/dC","net/shoreline/client/dA","net/shoreline/client/cy","net/shoreline/client/dZ","net/shoreline/client/cz","net/shoreline/client/dX","net/shoreline/client/cw","net/shoreline/client/dY","net/shoreline/client/cx","net/shoreline/client/dV","net/shoreline/client/cu","net/shoreline/client/dW","net/shoreline/client/cv","net/shoreline/client/dT","net/shoreline/client/cs","net/shoreline/client/dU","net/shoreline/client/ct","net/shoreline/client/dR","net/shoreline/client/cq","net/shoreline/client/dS","net/shoreline/client/cr","net/shoreline/client/dP","net/shoreline/client/co","net/shoreline/client/cp","net/shoreline/client/dQ","net/shoreline/client/si"};
    private static final Map<Long, Long> BOOTSTRAPS = new HashMap<>();
    public static ConcurrentHashMap<Class<?>, net.shoreline.client.n> EVENTBUS = new ConcurrentHashMap<>();
    private static final Unsafe UNSAFE;

    static {
        try {
            final ClassLoader loader = Main.class.getClassLoader();
            final Field _delegate = loader.getClass().getDeclaredField("delegate");
            _delegate.setAccessible(true);
            final Object delegate = _delegate.get(loader);
            final Field _transformer = delegate.getClass().getDeclaredField("mixinTransformer");
            _transformer.setAccessible(true);
            _transformer.set(delegate, new DelegatedMixinTransformer((IMixinTransformer) _transformer.get(delegate)));

            try (final DataInputStream dis = new DataInputStream(Objects.requireNonNull(Main.class.getClassLoader().getResourceAsStream("bootstraps.bin")))) {
                while (dis.available() > 0) {
                    final long k = dis.readLong();
                    final long v = dis.readLong();
                    BOOTSTRAPS.put(Long.reverseBytes(k), Long.reverseBytes(v));
                }
            }

            final Field _u = Unsafe.class.getDeclaredField("theUnsafe");
            _u.setAccessible(true);
            UNSAFE = (Unsafe) _u.get(null);
        } catch (Throwable _t) {
            _t.printStackTrace(System.err);
            throw new RuntimeException();
        }
    }

    @Override
    public void onPreLaunch() {}

    public static long bootstrap(final long k) {
        return Objects.requireNonNull(BOOTSTRAPS.getOrDefault(k, null));
    }

    public static void events(final Object inst) {
        try {
            final Field map = field(net.shoreline.client.e.class, "b", Map.class);
            map.setAccessible(true);
            final ConcurrentHashMap<Class<?>, net.shoreline.client.n> events = EVENTBUS = new ConcurrentHashMap<>();

            for (String str : EVENTS) {
                final Class<?> evt = Class.forName(str.replace('/', '.'));
                events.put(evt, (net.shoreline.client.n) UNSAFE.allocateInstance(net.shoreline.client.n.class));
            }

            map.set(inst, events);
        } catch (Throwable _t) {
            _t.printStackTrace(System.err);
        }
    }

    public static Object a(final Object eventbus, final Object arg) {
        try {
            for (final Method method : arg.getClass().getDeclaredMethods()) {
                final Class<?>[] params = method.getParameterTypes();
                if (params.length != 1 || method.getReturnType() != Void.TYPE) continue;

                if (net.shoreline.client.fR.class.isAssignableFrom(params[0])) {
                    final net.shoreline.client.n instance = (net.shoreline.client.n) UNSAFE.allocateInstance(net.shoreline.client.n.class);
                    instance.b = lambda(arg, method);
                    instance.c = arg;
                    instance.d = 0;

                    net.shoreline.client.n next = Objects.requireNonNull(EVENTBUS.getOrDefault(params[0], null));

                    while (true) {
                        net.shoreline.client.n n = next.a;

                        if (n == null) {
                            next.a = instance;
                            break;
                        } else next = n;
                    }
                }
            }
        } catch (Throwable _t) {
            _t.printStackTrace(System.err);
        }

        return null;
    }

    @SuppressWarnings("ConstantValue")
    public static Object b(final Object eventbus, final Object arg) {
        try {
            for (Map.Entry<?, net.shoreline.client.n> entry : EVENTBUS.entrySet()) {
                net.shoreline.client.n current = entry.getValue();
                if (current.c != null) throw new RuntimeException("invalid linkage");

                while (current != null && current.a != null) {
                    if (current.a.c == arg) current.a = current.a.a;
                    else current = current.a;
                }
            }
        } catch (Throwable _t) {
            _t.printStackTrace(System.err);
        }

        return null;
    }

    public static net.shoreline.client.i lambda(final Object inst, final Method impl) throws Throwable {
        final MethodHandles.Lookup lookup = MethodHandles.lookup();
        Reflection.getField(lookup.getClass(), "allowedModes").set(lookup, -1);
        final MethodHandle handle = lookup.unreflect(impl).bindTo(inst);
        return (o) -> {
            try {
                handle.invoke(o);
            } catch (Throwable _t) {
                _t.printStackTrace(System.err);
            }
        } ;
    }

    private static Field field(final Class<?> k, final String name, final Class<?> t) {
        for (final Field field : k.getDeclaredFields()) {
            if (!field.getType().isAssignableFrom(t)) continue;
            if (!field.getName().equals(name)) continue;
            return field;
        }

        throw new NullPointerException();
    }

    @Override
    public void onInitialize() {
        try {
            net.shoreline.client.b.c(bootstrap(-7983478698850341720L) ^ 0x926045A57B6B070L);
        } catch (Throwable _t) {
            if (_t instanceof InvocationTargetException ex) ex.getTargetException().printStackTrace(System.err);
            else _t.printStackTrace(System.err);
        }
    }
}