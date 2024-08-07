package net.ck.mtbg.util.utils;

import java.util.Objects;

public class CodeUtils
{
    public static Class<?> getRealClass(Object obj)
    {
        Class<?> enclosingClass = obj.getClass().getEnclosingClass();
        return Objects.requireNonNullElseGet(enclosingClass, obj::getClass);
    }


    /**
     * thank you
     * <a href="https://www.baeldung.com/java-filename-without-extension#:~:text=getNameWithoutExtension()%20method.,from%20the%20given%20filename%20easily">https://www.baeldung.com/java-filename-without-extension#:~:text=getNameWithoutExtension()%20method.,from%20the%20given%20filename%20easily.</a>.
     *
     * @param filename
     * @param removeAllExtensions
     * @return string without file extensions
     */
    public static String removeFileExtension(String filename, boolean removeAllExtensions)
    {
        if (filename == null || filename.isEmpty())
        {
            return filename;
        }

        String extPattern = "(?<!^)[.]" + (removeAllExtensions ? ".*" : "[^.]*$");
        return filename.replaceAll(extPattern, "");
    }


    /**
     * thank you <a href="https://www.specialagentsqueaky.com/blog-post/bbxg29pd/2010-10-10-getting-a-methods-calling-method-in-java/">
     * https://www.specialagentsqueaky.com/blog-post/bbxg29pd/2010-10-10-getting-a-methods-calling-method-in-java/</a>
     *
     * @return
     */
    public static String getCallingMethodName()
    {
        StackTraceElement stack = Thread.currentThread().getStackTrace()[3];
        return stack.getClassName() + "." + stack.getMethodName();
    }
}
