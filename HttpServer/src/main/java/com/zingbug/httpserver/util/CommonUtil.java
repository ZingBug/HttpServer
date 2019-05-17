package com.zingbug.httpserver.util;

import com.zingbug.httpserver.common.annotation.Controller;
import com.zingbug.httpserver.common.annotation.Filter;
import com.zingbug.httpserver.common.config.HttpServerConfig;
import com.zingbug.httpserver.common.constant.RequestConstants;
import com.zingbug.httpserver.common.em.PageMappingEnum;
import com.zingbug.httpserver.model.FilterModel;
import io.netty.util.internal.StringUtil;
import lombok.extern.slf4j.Slf4j;

import java.io.*;
import java.lang.reflect.Method;
import java.util.*;

/**
 * Created by ZingBug on 2019/5/14.
 */
@Slf4j
public class CommonUtil {
    private static final int SIZE=10240;

    /**
     * 截取uri里面的路径字段信息
     * @param uri
     * @return
     */
    public static String getUri(String uri)
    {
        int pathIndex=uri.indexOf("/");
        int requestIndex=uri.indexOf("?");
        String result;
        if(requestIndex<0)
        {
            result=uri.trim().substring(pathIndex);
        }
        else
        {
            result=uri.trim().substring(pathIndex,requestIndex);
        }
        return result;
    }

    /**
     * 通过编号读取页面的html代码
     * @param code
     * @return
     * @throws IOException
     */
    public static String readPageHtml(int code) throws IOException
    {
        //获取文件内容
        File file=readFile(PageMappingEnum.getPath(code));
        return stream2String(new FileInputStream(file),"UTF-8");
    }

    /**
     * 读取初始化页面的html代码
     * @param indexPath
     * @return
     * @throws IOException
     */
    public static String readIndexHtml(String indexPath) throws IOException
    {
        if(HttpServerConfig.INDEX_CHANGE)
        {
            return readPageHtmlInPath(indexPath);
        }
        //获取jar里面的index页面
        return JarFileUtil.readJarFileIndex(HttpServerConfig.INDEX_PAGE);
    }

    /**
     * 读取初始化页面的html代码
     * @return
     * @throws IOException
     */
    public static String read404Html() throws IOException{
        if(HttpServerConfig.NOT_FOUNT_CHANGE)
        {
            return readPageHtmlInPath(HttpServerConfig.NOT_FOUND_PAGE);
        }
        //获取jar里面固定的文件内容
        return JarFileUtil.readJarFileIndex(HttpServerConfig.NOT_FOUND_PAGE);
    }


    /**
     * 读取resource文件夹底下的文件内容
     * @param url
     * @return
     * @throws IOException
     */
    public static String readFileFromResource(String url) throws IOException{
        //获取文件内容
        File file=readFile(url);
        if(file==null)
        {
            return RequestConstants.PAGE_NOT_FOUND;
        }
        return JarFileUtil.readResources(new FileInputStream(file));
    }


    /**
     * 可以直接读取resource底下的html
     * @param path
     * @return
     */
    public static String readPageHtmlInPath(String path)
    {
        //获取文件内容
        File file=readFile(path);
        if(file==null)
        {
            return null;
        }
        try
        {
            return JarFileUtil.readResources(new FileInputStream(file));
        }
        catch (IOException e)
        {
            log.error("[readPageHtmlInPath] 文件读取异常，信息为{}", e);
            return StringUtil.EMPTY_STRING;
        }
    }

    /**
     * 读取文件信息
     * @param path
     * @return
     */
    public static File readFile(String path)
    {
        //获取文件内容
        if(path.startsWith("/"))
        {
            path=path.substring(path.indexOf("/")+1);
        }
        File file=null;
        try {
            String paths= HttpServerConfig.APPLICATION_CLASS.getClassLoader().getResource(path).getFile();
            file=new File(paths);
        }
        catch (Exception e)
        {
            return null;
        }
        return file;
    }

    /**
     * 文件转换为字符串
     * @param in
     * @param charset
     * @return
     */
    public static String stream2String(InputStream in,String charset)
    {
        StringBuffer sb=new StringBuffer();//StringBuffer是线程安全的
        try
        {
            Reader reader=new InputStreamReader(in,charset);
            int length=0;
            for(char[] c=new char[SIZE];(length=reader.read(c))!=-1;)
            {
                sb.append(c,0,length);
            }
        }
        catch (IOException e)
        {
            log.error("[stream2String]异常为{}",e);
        }
        return sb.toString();
    }

    /**
     * 获取指定的函数方法
     * @param targetMethod
     * @param name
     * @return
     */
    public static Method getMethodByName(Method[] targetMethod,String name)
    {
        for(Method method:targetMethod)
        {
            if(method.getName().equals(name))
            {
                return method;
            }
        }
        return null;
    }

    /**
     * 判断其所实现的接口是否和预期一致
     * @param target
     * @param interfaceClazz
     * @return
     */
    public static boolean isContainInterFace(Object target,Class interfaceClazz)
    {
        //class1.isAssignableFrom(class2)
        //判定此 Class1 对象所表示的类或接口与指定的 Class2 参数所表示的类或接口是否相同，或是否是其超类或超接口。
        //如果是则返回 true；否则返回 false。
        return interfaceClazz.isAssignableFrom(target.getClass());
        /*
        Class<?>[] interfaceNames=target.getClass().getInterfaces();
        String expectName=interfaceClazz.getName();
        for(Class<?> interfaceName:interfaceNames)
        {
            if(interfaceName.getName().equals(expectName))
            {
                return true;
            }
        }
        return false;
        */
    }


    /**
     * 扫描控制器
     * @param path
     * @return
     */
    public static Map<String,String> scanController(String path)
    {
        Map<String,String> result=new HashMap<>(60);
        Set<Class<?>> clazz=ClassUtil.getClzFromPkg(path);//获取该路径下所有类
        for(Class<?> aClass:clazz)
        {
            if(aClass.isAnnotationPresent(Controller.class))//当前类中存在ControllerMapping的注解
            {
                Controller controller=aClass.getAnnotation(Controller.class);
                String url=controller.url();
                result.put(url,aClass.getName());
                log.info("[Controller] " , aClass.getName());
            }
        }
        return result;
    }


    /**
     * 扫描过滤器
     * @param path
     * @return
     * @throws IllegalAccessException
     * @throws InstantiationException
     */
    public static List<FilterModel> scanFilter(String path) throws IllegalAccessException,InstantiationException{
        Set<Class<?>> clazz=ClassUtil.getClzFromPkg(path);
        List<FilterModel> filterModelList=new ArrayList<>();
        for(Class<?> aClass:clazz)
        {
            if(aClass.isAnnotationPresent(Filter.class))//当前类中存在Filter的注解
            {
                Filter filter=aClass.getAnnotation(Filter.class);
                FilterModel filterModel=new FilterModel(filter.order(),filter.name(),aClass.newInstance());
                log.info("[Filter] " , filterModel.toString());
                filterModelList.add(filterModel);
            }
        }

        //对加载的filter进行优先级排序
        filterModelList.sort(new Comparator<FilterModel>() {
            @Override
            public int compare(FilterModel o1, FilterModel o2) {
                if(o1.getOrder()>=o2.getOrder())
                {
                    return -1;
                }
                return 1;
            }
        });
        return filterModelList;
    }

    public static RandomAccessFile readPic(String path)
    {
        RandomAccessFile file=null;
        try {
            file=new RandomAccessFile(readFile(path),"r");
        }
        catch (FileNotFoundException e)
        {
            log.error("[readPic] " ,e);
        }
        return file;
    }

    public static void main(String[] args) throws InstantiationException,IllegalAccessException{

    }




}
