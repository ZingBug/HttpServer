package com.zingbug.httpserver.util;

import jdk.internal.util.xml.impl.Input;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;

/**
 * Created by ZingBug on 2019/5/14.
 */
public class JarFileUtil {

    /**
     * 读取已有页面的内容
     * @param filePathInJar
     * @return
     * @throws IOException
     */
    public static String readJarFileIndex(String filePathInJar) throws IOException{
        InputStream stream=ClassLoader.getSystemResourceAsStream(filePathInJar);
        return readResources(stream);
    }

    /**
     * 配合readJarFileIndex使用
     * @param path
     * @return
     * @throws IOException
     */
    private static String readResources(String path) throws IOException{

        InputStream is=getClassForStatic().getResourceAsStream(path);
        String result=readResources(is);
        return result;
    }

    /**
     * 读取maven的resources底下的内容
     * @param is
     * @return
     * @throws IOException
     */
    public static String readResources(InputStream is) throws IOException{
        final int bufferSize=1024;
        final char[] buffer=new char[bufferSize];
        final StringBuilder out=new StringBuilder();
        Reader in=new InputStreamReader(is,"UTF-8");
        while (true)
        {
            int rsz=in.read(buffer,0,bufferSize);
            if(rsz<0)
            {
                break;
            }
            out.append(buffer,0,rsz);
        }
        is.close();
        return out.toString();
    }

    private static Class<?> getClassForStatic()
    {
        return new Object(){}.getClass();
    }
}
