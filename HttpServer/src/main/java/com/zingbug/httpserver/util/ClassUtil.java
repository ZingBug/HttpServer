package com.zingbug.httpserver.util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.IOException;
import java.net.JarURLConnection;
import java.net.URL;
import java.net.URLDecoder;
import java.util.Enumeration;
import java.util.LinkedHashSet;
import java.util.Set;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;

/**
 * Created by ZingBug on 2019/5/14.
 */
public class ClassUtil {
    private static Logger logger= LoggerFactory.getLogger(ClassUtil.class);

    /**
     * 从包package中获取所有的class
     * @param pkg
     * @return
     */
    public static Set<Class<?>> getClzFromPkg(String pkg)
    {
        //第一个class类的集合
        Set<Class<?>> classes=new LinkedHashSet<>();
        //获取包的名字，并进行替换
        String pkgDirName=pkg.replace(".","/");
        try{
            Enumeration<URL> urls=ClassUtil.class.getClassLoader().getResources(pkgDirName);
            while (urls.hasMoreElements())
            {
                URL url=urls.nextElement();
                //得到协议的名称
                String protocol=url.getProtocol();//比如http，file等
                if("file".equals(protocol))
                {
                    //获取包的物理路径
                    String filePath= URLDecoder.decode(url.getFile(),"UTF-8");
                    //以文件的方式扫描整个包下的文件，并添加到集合中
                    findClassesByFile(pkg,filePath,classes);
                }
                else if("jar".equals(protocol))
                {
                    //如果是jar包文件
                    //获取jar
                    JarFile jar=((JarURLConnection)url.openConnection()).getJarFile();
                    //扫描jar包文件，并添加到集合中
                    findClassesByJar(pkg,jar,classes);
                }
            }
        }
        catch (IOException e)
        {
            logger.error("[getClzFromPkg]异常为{}",e);
        }

        return classes;
    }

    /**
     * 在包中获取所有类
     * @param pkgName
     * @param pkgPath
     * @param classes
     */
    private static void findClassesByFile(String pkgName,String pkgPath,Set<Class<?>> classes)
    {
        //获取包的目录，建立一个File
        File dir=new File(pkgPath);
        //如果不存在，或者不是目录就直接返回
        if(!dir.exists()||!dir.isDirectory())
        {
            return;
        }
        File[] dirFiles=dir.listFiles(pathname ->pathname.isDirectory()||pathname.getName().endsWith("class") );
        if(dirFiles==null||dirFiles.length==0)
        {
            return;
        }
        String className;
        Class clz;
        //循环所有文件
        for(File file:dirFiles)
        {
            if(file.isDirectory())
            {
                //目录递归寻找
                findClassesByFile(pkgName+"."+file.getName(),pkgPath+"/"+file.getName(),classes);
                continue;
            }
            //如果是java类文件，去掉后面的.class，只留下类名
            className=file.getName();
            className=className.substring(0,className.length()-6);

            //加载类
            clz=loadClass(pkgName+"."+className);
            //添加到集合中去
            if(clz!=null)
            {
                classes.add(clz);
            }
        }
    }

    /**
     * 在jar包中寻找类
     * @param pkgName
     * @param jar
     * @param classes
     */
    private static void findClassesByJar(String pkgName, JarFile jar,Set<Class<?>> classes)
    {
        String pkgDir=pkgName.replace(".","/");
        //在jar包中，得到一个枚举类
        Enumeration<JarEntry> entry=jar.entries();

        JarEntry jarEntry;
        String name,className;
        Class<?> claze;
        //同样的进行迭代循环
        while (entry.hasMoreElements())
        {
            //获取jar里的一个实类，可以是目录和一些jar包里的其他文件，如META-INF等文件
            jarEntry=entry.nextElement();
            name=jarEntry.getName();
            //如果是以/开头的
            if(name.charAt(0)=='/')
            {
                //获取后面的字符串
                name=name.substring(1);
            }
            if(jarEntry.isDirectory()||!name.startsWith(pkgDir)||!name.endsWith(".class"))
            {
                continue;
            }
            //如果是一个.class文件 而且不是目录
            //去掉后面的".class" 获取真正的类名
            className=name.substring(0,name.length()-6);
            //加载类
            claze=loadClass(className.replace("/","."));
            //添加到集合中去
            if(claze!=null)
            {
                classes.add(claze);
            }
        }
    }


    /**
     * 加载类
     * @param fullClzName 类全名
     * @return
     */
    private static Class<?> loadClass(String fullClzName)
    {
        try{
            return Thread.currentThread().getContextClassLoader().loadClass(fullClzName);
        }
        catch (ClassNotFoundException e)
        {
            logger.error("[loadClass]异常为{}",e);
        }
        return null;
    }
}
