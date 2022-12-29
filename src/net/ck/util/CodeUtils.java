package net.ck.util;

import java.util.Objects;

public class CodeUtils
{
    public static Class<?> getRealClass(Object obj)
    {
        Class<?> enclosingClass = obj.getClass().getEnclosingClass();
        return Objects.requireNonNullElseGet(enclosingClass, obj::getClass);
    }

}
