package shibaInu.tolua;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.util.Hashtable;


/**
 * Lua 文件加载器
 * Created by LOLO on 2020/04/21.
 */
public class LuaFileLoader {

    // 全局实例
    public static LuaFileLoader instance = new LuaFileLoader();


    // lua 文件目录列表
    public String[] dirs = {"./", "./lua/"};
    // 已缓存的文件数据列表
    private Hashtable<String, byte[]> cache = new Hashtable<>();


    /**
     * 获取格式化后的 lua 文件路径
     */
    public String getLuaPath(String path) {
        return path.replace(".", "/") + ".lua";
    }


    /**
     * 加载并返回 lua 文件字节数据
     *
     * @param path lua 文件相对路径，不含 ".lua" 后缀
     */
    public synchronized byte[] loadFile(String path) {
        if (cache.containsKey(path))
            return cache.get(path);

        String luaPath = getLuaPath(path);
        for (String dir : dirs) {
            File file = new File(dir + luaPath);
            if (file.exists()) {
                try {
                    FileInputStream input = new FileInputStream(file);
                    ByteArrayOutputStream output = new ByteArrayOutputStream();
                    byte buf[] = new byte[2048];
                    int len;
                    while ((len = input.read(buf)) != -1)
                        output.write(buf, 0, len);
                    input.close();
                    output.close();
                    byte[] bytes = output.toByteArray();
                    cache.put(path, bytes);
                    System.out.println("load lua file: " + luaPath);
                    return bytes;
                } catch (Exception e) {
                    System.out.println("load lua file error: " + path);
                    return null;
                }
            }
        }
        return null;
    }


    /**
     * path 对应的 lua 文件是否存在
     */
    public synchronized boolean luaFileExists(String path) {
        String luaPath = getLuaPath(path);
        for (String dir : dirs) {
            if (new File(dir + luaPath).exists())
                return true;
        }
        return false;
    }


    /**
     * 清除 lua 文件数据缓存
     *
     * @param path lua 文件相对路径，不含 ".lua" 后缀
     */
    public synchronized void clearCache(String path) {
        cache.remove(path);
    }

    public synchronized void clearAllCache() {
        cache.clear();
    }


    //
}
