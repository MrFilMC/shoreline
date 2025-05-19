package nick;

import sun.misc.Unsafe;

import java.lang.reflect.AccessibleObject;
import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.Arrays;

@SuppressWarnings({"CatchMayIgnoreException", "unused", "unchecked"})
public class Reflection {
    private static Unsafe unsafe;
    private static Method[] table;

    static {
        try {
            final Field f = Unsafe.class.getDeclaredField("theUnsafe");
            f.setAccessible(true);
            unsafe = (Unsafe) f.get(null);
            table = new Method[]
                    {
                            Class.class.getDeclaredMethod("getDeclaredMethods0", boolean.class),
                            Class.class.getDeclaredMethod("getDeclaredFields0", boolean.class),
                            Class.class.getDeclaredMethod("getDeclaredConstructors0", boolean.class)
                    };

            for (Method method : table) patch(method);
        } catch (Throwable t) {}
    }

    public enum Mask {
        METHODS,
        FIELDS,
        CONSTRUCTORS
    }

    public static <T extends AccessibleObject> boolean patched(final T obj) {
        return unsafe.getBoolean(obj, 12);
    }

    public static <T extends AccessibleObject> void patch(final T obj) {
        if (!patched(obj))
            unsafe.putBoolean(obj, 12, true);
    }

    public static Method getMethod(final Class<?> klass, final String name, final Class<?>... parameters) {
        if (klass == null) throw new RuntimeException("no method found");

        final Method[] methods = getMembers(klass, Mask.METHODS);

        for (final Method method : methods)
            if (method.getName().equals(name) && Arrays.equals(method.getParameterTypes(), parameters)) {
                patch(method);
                return method;
            }

        return getMethod(klass.getSuperclass(), name, parameters);
    }

    public static Field getField(final Class<?> klass, final String name) {
        if (klass == null) throw new RuntimeException("no field found");
        final Field[] fields = getMembers(klass, Mask.FIELDS);

        for (final Field field : fields)
            if (field.getName().equals(name)) {
                patch(field);
                return field;
            }

        return getField(klass.getSuperclass(), name);
    }

    public static <T> Constructor<T> getConstructors(final Class<?> klass, final Class<?>... parameters) {
        final Constructor<?>[] constructors = getMembers(klass, Mask.CONSTRUCTORS);

        for (final Constructor<?> constructor : constructors)
            if (Arrays.equals(constructor.getParameterTypes(), parameters)) {
                patch(constructor);
                return (Constructor<T>) constructor;
            }

        throw new RuntimeException("no constructor found");
    }

    public static <T extends AccessibleObject> T[] getMembers(final Class<?> klass, final Mask mask) {
        try {
            return (T[]) table[mask.ordinal()].invoke(klass, false);
        } catch (Throwable _t) {
            _t.printStackTrace(System.err);
            throw new RuntimeException();
        }
    }
}